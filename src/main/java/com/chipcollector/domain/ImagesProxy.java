package com.chipcollector.domain;

import java.awt.image.BufferedImage;

public interface ImagesProxy {

    BufferedImage getFrontImage() throws Exception;

    void setFrontImage(BufferedImage image);

    BufferedImage getBackImage() throws Exception;

    void setBackImage(BufferedImage image) throws Exception;
}