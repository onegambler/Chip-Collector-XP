package com.chipcollector.util;

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
}

