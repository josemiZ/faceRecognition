package com.bixspace.view;

import com.bixspace.utils.TransformToImage;
import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by MIGUEL ZEA on 30/10/2016.
 */
public class AccessVideo extends JPanel {
    private VideoCapture camera = new VideoCapture();
    private int width;
    private int height;

    AccessVideo(VideoCapture camView,int height,int width) {
        camera = camView;
        this.width=width;
        this.height=height;
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Mat[] mat = new Mat[1];

        int width=this.width;
        int height=this.height;
        int xpos = 0;
        int ypos = 0;
        int y = 1;
        for (int i = 0; i < 1; i++) {
            mat[i] = new Mat();

            camera.read(mat[i]);
            BufferedImage image = TransformToImage.MatToBufferedImage(mat[i]);
            g.drawImage(image, xpos, ypos, width, height, null);
            xpos = xpos + width;
            if (y == 3) {
                xpos = 0;
                ypos = ypos + height;
                y = 0;
            }
            y++;
        }
    }
}
