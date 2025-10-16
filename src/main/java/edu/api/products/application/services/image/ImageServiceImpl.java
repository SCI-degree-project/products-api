package edu.api.products.application.services.image;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

@Service
public class ImageServiceImpl implements ImageService {

    @Override
    public String calculateAspectRatio(String imagePath) {
        try {
            URL url = new URL(imagePath);

            Image image = ImageIO.read(url);

            int width = image.getWidth(null);
            int height = image.getHeight(null);

            if(width > 0 && height > 0) {
                double ratio = (double) width / height;
                return String.format("%.2f", ratio);
            } else {
                throw new IllegalArgumentException("Cannot calculate aspect ratio for image " + imagePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Can't read the image URL", e);
        }
    }
}
