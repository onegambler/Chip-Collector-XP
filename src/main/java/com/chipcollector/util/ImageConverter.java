package com.chipcollector.util;

import com.chipcollector.domain.BlobImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.imgscalr.Scalr;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static java.util.Objects.requireNonNull;

public class ImageConverter {

    public static byte[] getRawBytesFromBufferedImage(BufferedImage image, String format) throws IOException {
        requireNonNull(image, "Image cannot be null");

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(1000)) {
            ImageIO.write(image, format, baos);
            return baos.toByteArray();
        }
    }

    public static BufferedImage getResizedImage(BufferedImage bufferedImage, int targetSize) {
        return Scalr.resize(bufferedImage, targetSize);
    }

    public static BufferedImage getBufferedImageFromImage(Image image) {
        return SwingFXUtils.fromFXImage(image, null);
    }

    public static BlobImage getBlobImageFromImage(Image image) throws IOException {
        BufferedImage originalImage = getBufferedImageFromImage(image);
        byte[] thumbnail = getRawBytesFromBufferedImage(getResizedImage(originalImage, 90), PNG_FORMAT);
        byte[] resizedImage = getRawBytesFromBufferedImage(getResizedImage(originalImage, 120), PNG_FORMAT);
        BlobImage blobImage = new BlobImage();
        blobImage.setImage(resizedImage);
        blobImage.setThumbnail(thumbnail);
        return blobImage;
    }

    @NotNull
    public static Image getImageFromByteArray(byte[] imageByteArray) {
        InputStream byteArrayInputStream = new ByteArrayInputStream(imageByteArray);
        return new Image(byteArrayInputStream);
    }

    @NotNull
    public static Image getImageFromByteArray(byte[] imageByteArray, double width, double height) {
        InputStream byteArrayInputStream = new ByteArrayInputStream(imageByteArray);
        return new Image(byteArrayInputStream, width, height, true, true);
    }

    public static final String PNG_FORMAT = "png";
}

