package edu.api.products.infrastructure.product;

import edu.api.products.application.dto.ProductSearchCriteria;
import edu.api.products.domain.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepositoryImpl implements IProductRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Product> search(ProductSearchCriteria criteria, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);

        List<Predicate> predicates = buildPredicates(cb, root, criteria);

        query.select(root).where(cb.and(predicates.toArray(new Predicate[0]))).distinct(true);

        if (criteria.sortBy() != null && !criteria.sortBy().isBlank()) {
            Path<?> sortPath = root.get(criteria.sortBy());
            Order order = "desc".equalsIgnoreCase(criteria.direction())
                    ? cb.desc(sortPath)
                    : cb.asc(sortPath);
            query.orderBy(order);
        }

        TypedQuery<Product> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        long total = countQuery(criteria);
        return new PageImpl<>(typedQuery.getResultList(), pageable, total);
    }

    private long countQuery(ProductSearchCriteria criteria) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Product> root = countQuery.from(Product.class);
        List<Predicate> predicates = buildPredicates(cb, root, criteria);

        countQuery.select(cb.countDistinct(root)).where(cb.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(countQuery).getSingleResult();
    }

    private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<Product> root, ProductSearchCriteria criteria) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.name() != null && !criteria.name().isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + criteria.name().toLowerCase() + "%"));
        }

        if (criteria.style() != null) {
            predicates.add(cb.equal(root.get("style"), criteria.style()));
        }

        if (criteria.materials() != null && !criteria.materials().isEmpty()) {
            predicates.add(root.join("materials").in(criteria.materials()));
        }

        if (criteria.tenantId() != null) {
            predicates.add(cb.equal(root.get("tenantId"), criteria.tenantId()));
        }

        predicates.add(cb.isFalse(root.get("isDeleted")));

        return predicates;
    }
}
