package com.chipcollector.util;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Dimension2D;
import javafx.scene.image.Image;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static java.util.Objects.requireNonNull;

public class ImageConverter {

    public static BufferedImage rawBytesToBufferedImage(byte[] bytes) throws IOException {
        requireNonNull(bytes, "Image byte array cannot be null");
        try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
            return ImageIO.read(inputStream);
        }
    }

    public static byte[] bufferedImageToRawBytes(BufferedImage image, String format) throws IOException {
        requireNonNull(image, "Image cannot be null");

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(1000)) {
            ImageIO.write(image, format, baos);
            return baos.toByteArray();
        }
    }

    public static byte[] imageToRawBytes(Image image, String format) throws IOException {
        requireNonNull(image, "Image cannot be null");
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        return bufferedImageToRawBytes(bufferedImage, format);
    }

    public static BufferedImage resizeImage(BufferedImage bufferedImage, int targetSize) {
        return Scalr.resize(bufferedImage, targetSize);
    }

    public static BufferedImage imageToBufferedImage(Image image) {
        return SwingFXUtils.fromFXImage(image, null);
    }
}

