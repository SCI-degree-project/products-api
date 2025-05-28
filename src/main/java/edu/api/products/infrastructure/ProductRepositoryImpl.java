package edu.api.products.infrastructure;

import edu.api.products.domain.Material;
import edu.api.products.domain.Product;
import edu.api.products.domain.Style;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
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
    public Page<Product> search(String name, Style style, List<Material> materials, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);

        List<Predicate> predicates = buildPredicates(cb, root, name, style, materials);

        query.where(cb.and(predicates.toArray(new Predicate[0])));
        query.select(root).distinct(true);

        TypedQuery<Product> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        long count = countQuery(name, style, materials);

        return new PageImpl<>(typedQuery.getResultList(), pageable, count);
    }

    private long countQuery(String name, Style style, List<Material> materials) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Product> root = countQuery.from(Product.class);

        List<Predicate> predicates = buildPredicates(cb, root, name, style, materials);

        countQuery.select(cb.countDistinct(root)).where(cb.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(countQuery).getSingleResult();
    }

    private List<Predicate> buildPredicates(
            CriteriaBuilder cb,
            Root<Product> root,
            String name,
            Style style,
            List<Material> materials
    ) {
        List<Predicate> predicates = new ArrayList<>();

        if (name != null && !name.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }

        if (style != null) {
            predicates.add(cb.equal(root.get("style"), style));
        }

        if (materials != null && !materials.isEmpty()) {
            predicates.add(root.join("materials").in(materials));
        }

        return predicates;
    }
}
