package com.bixspace.view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by MIGUEL ZEA on 9/11/2016.
 */
public class ShowPhotoView extends JPanel {
    BufferedImage image;

    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }

    public ShowPhotoView(BufferedImage image){
        this.image=image;
    }
}
