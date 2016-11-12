package com.bixspace.view;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import static com.bixspace.utils.TransformToImage.MatToBufferedImage;

/**
 * Created by MIGUEL ZEA on 30/10/2016.
 */
public class CaptureImageFromCameraView extends JPanel {
    BufferedImage image;

    public static void main (String args[]) throws InterruptedException{
        open();
        /*
        if(!camera.isOpened()){
            System.out.println("Error");
        }
        else {
            while(true){

                if (camera.read(frame)){

                    BufferedImage image = t.MatToBufferedImage(frame);

                    t.window(image, "Original Image", 0, 0);

                    t.window(t.grayscale(image), "Processed Image", 40, 60);

                    //t.window(t.loadImage("ImageName"), "Image loaded", 0, 0);

                    break;
                }
            }
        }
        camera.release();*/
    }

    private static void open(){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        CaptureImageFromCameraView t = new CaptureImageFromCameraView();
        VideoCapture camera = new VideoCapture(0);

        Mat frame = new Mat();
        camera.read(frame);
        JFrame fr = new JFrame();
        Dimension d = new Dimension(400,450);
        fr.setSize(d);
        fr.setVisible(true);
        AccessVideo av = new AccessVideo(camera,400,400);
        JPanel buttonPanel = new JPanel();
        Button takePicture = new Button("Tomar Foto");

        buttonPanel.add(takePicture);
        fr.add(av);
        fr.add(buttonPanel,BorderLayout.PAGE_END);
        fr.setLocationRelativeTo(null);
        takePicture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(camera.read(frame)){
                    BufferedImage image = MatToBufferedImage(frame);
                    t.window(image, "Original Image");
                }

            }
        });
        while (camera.isOpened()){
            av.repaint();
        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }

    public CaptureImageFromCameraView() {
    }

    public CaptureImageFromCameraView(BufferedImage img) {
        image = img;
    }

    //Show image on window
    public void window(BufferedImage img, String text) {
        JFrame frame0 = new JFrame();
        frame0.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame0.setTitle(text);
        frame0.setSize(img.getWidth(), img.getHeight() + 60);
        frame0.setLocationRelativeTo(null);
        frame0.setVisible(true);
        frame0.add(new CaptureImageFromCameraView(img));
        JPanel buttonPanel = new JPanel();
        Button button = new Button("Grabar");
        buttonPanel.add(button);
        frame0.add(buttonPanel,BorderLayout.PAGE_END);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveImage(img);
            }
        });
    }

    //Load an image
    public BufferedImage loadImage(String file) {
        BufferedImage img;

        try {
            File input = new File(file);
            img = ImageIO.read(input);

            return img;
        } catch (Exception e) {
            System.out.println("erro");
        }

        return null;
    }

    //Save an image
    public void saveImage(BufferedImage img) {

        try {
            File outputfile = new File("C:/facerecognizer/data/xd.png");
            if(outputfile.exists()){
                outputfile.delete();
            }
            ImageIO.write(img, "png", outputfile);
        } catch (Exception e) {
            System.out.println("error");
        }
    }

    //Grayscale filter
    public BufferedImage grayscale(BufferedImage img) {
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color c = new Color(img.getRGB(j, i));

                int red = (int) (c.getRed() * 0.299);
                int green = (int) (c.getGreen() * 0.587);
                int blue = (int) (c.getBlue() * 0.114);

                Color newColor =
                        new Color(
                                red + green + blue,
                                red + green + blue,
                                red + green + blue);

                img.setRGB(j, i, newColor.getRGB());
            }
        }

        return img;
    }
}
