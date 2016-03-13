package com.chipcollector.util;

import com.chipcollector.domain.BlobImage;
import javafx.embed.swing.SwingFXUtils;
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


    public static Image rawBytesToImage(byte[] imageByteArray) throws IOException {
        return SwingFXUtils.toFXImage(rawBytesToBufferedImage(imageByteArray), null);
    }

    public static BufferedImage resizeImage(BufferedImage bufferedImage, int targetSize) {
        return Scalr.resize(bufferedImage, targetSize);
    }

    public static BufferedImage imageToBufferedImage(Image image) {
        return SwingFXUtils.fromFXImage(image, null);
    }

    public static BlobImage getBlobImageFromImage(Image image) throws IOException {
        BufferedImage originalImage = ImageConverter.imageToBufferedImage(image);
        byte[] thumbnail = bufferedImageToRawBytes(resizeImage(originalImage, 90), PNG_FORMAT);
        byte[] resizedImage = bufferedImageToRawBytes(resizeImage(originalImage, 120), PNG_FORMAT);
        BlobImage blobImage = new BlobImage();
        blobImage.setImage(resizedImage);
        blobImage.setThumbnail(thumbnail);
        return blobImage;
    }

    public static final String PNG_FORMAT = "png";
}

