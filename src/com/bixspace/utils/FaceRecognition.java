package com.bixspace.utils;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_face;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import static org.bytedeco.javacpp.helper.opencv_objdetect.cvHaarDetectObjects;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_face.createLBPHFaceRecognizer;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvSaveImage;
import static org.bytedeco.javacpp.opencv_imgproc.*;

/**
 * Created by MIGUEL ZEA on 29/10/2016.
 */
public class FaceRecognition {
    private static String faceDataFolder = "C:\\tutor_assistance\\data\\";
    private static String imageDataFolder = faceDataFolder + "images\\";
    private static final String CASCADE_FILE = "C:\\tutor_assistance\\data\\haarcascade_frontalface_alt.xml";
    private static final String BinaryFile = faceDataFolder + "frBinary.dat";
    private static final String personNameMappingFileName = faceDataFolder + "personNumberMap.properties";

    private final opencv_objdetect.CvHaarClassifierCascade cascade = new opencv_objdetect.CvHaarClassifierCascade(cvLoad(CASCADE_FILE));
    private Properties dataMap = new Properties();
    private static FaceRecognition instance = new FaceRecognition();

    private static final int NUM_IMAGES_PER_PERSON =10;
    double binaryTreshold = 100;
    int highConfidenceLevel = 70;


    private opencv_face.FaceRecognizer fr_binary = null;

    private FaceRecognition() {
        createModels();
        loadTrainingData();
    }

    public static FaceRecognition getInstance() {
        return instance;
    }

    private void createModels() {
        fr_binary = createLBPHFaceRecognizer(1, 8, 8, 8, binaryTreshold);
    }

    public opencv_core.CvSeq detectFace(opencv_core.IplImage originalImage) {
        opencv_core.CvSeq faces = null;
        Loader.load(opencv_objdetect.class);
        try {
            opencv_core.IplImage grayImage = opencv_core.IplImage.create(originalImage.width(), originalImage.height(), IPL_DEPTH_8U, 1);
            cvCvtColor(originalImage, grayImage, CV_BGR2GRAY);
            opencv_core.CvMemStorage storage = opencv_core.CvMemStorage.create();
            faces = cvHaarDetectObjects(grayImage, cascade, storage, 1.1, 1, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return faces;
    }

    public String identifyFace(opencv_core.IplImage image) {
        String personName = "";
        Set keys = dataMap.keySet();
        if (keys.size() > 0) {
            int[] ids = new int[1];
            double[] distance = new double[1];
            int result = -1;
            FrameConverter frameConverter = new OpenCVFrameConverter.ToIplImage();
            Frame frame = frameConverter.convert(image);
            FrameConverter frameConverter1 = new OpenCVFrameConverter.ToMat();
            Mat mat = (Mat) frameConverter1.convert(frame);
            fr_binary.predict(mat, ids, distance);
            result = ids[0];

            if (result > -1 && distance[0]<highConfidenceLevel) {
                personName = (String) dataMap.get("" + result);
            }
        }

        return personName;
    }

    public boolean learnNewFace(String personName, opencv_core.IplImage[] images) throws Exception {
        int memberCounter = dataMap.size();
        if(dataMap.containsValue(personName)){
            Set keys = dataMap.keySet();
            Iterator ite = keys.iterator();
            while (ite.hasNext()) {
                String personKeyForTraining = (String) ite.next();
                String personNameForTraining = (String) dataMap.getProperty(personKeyForTraining);
                if(personNameForTraining.equals(personName)){
                    memberCounter = Integer.parseInt(personKeyForTraining);
                }
            }
        }
        dataMap.put("" + memberCounter, personName);
        storeTrainingImages(personName, images);
        retrainAll();

        return true;
    }


    public opencv_core.IplImage preprocessImage(opencv_core.IplImage image, opencv_core.CvRect r){
        opencv_core.IplImage gray = cvCreateImage(cvGetSize(image), IPL_DEPTH_8U, 1);
        opencv_core.IplImage roi = cvCreateImage(cvGetSize(image), IPL_DEPTH_8U, 1);
        opencv_core.CvRect r1 = new opencv_core.CvRect(r.x()-10, r.y()-10, r.width()+10, r.height()+10);
        cvCvtColor(image, gray, CV_BGR2GRAY);
        cvSetImageROI(gray, r1);
        cvResize(gray, roi, CV_INTER_LINEAR);
        cvEqualizeHist(roi, roi);
        return roi;
    }

    private void retrainAll() throws Exception {
        Set keys = dataMap.keySet();
        if (keys.size() > 0) {
            MatVector trainImages = new MatVector(keys.size() * NUM_IMAGES_PER_PERSON);
            CvMat trainLabels = CvMat.create(keys.size() * NUM_IMAGES_PER_PERSON, 1, CV_32SC1);
            Iterator ite = keys.iterator();
            int count = 0;

            System.err.print("Cargando imagenes para entrenamiento ...");
            while (ite.hasNext()) {
                String personKeyForTraining = (String) ite.next();
                String personNameForTraining = (String) dataMap.getProperty(personKeyForTraining);
                IplImage[] imagesForTraining = readImages(personNameForTraining);

                for (int i = 0; i < imagesForTraining.length; i++) {
                    trainLabels.put(count, 0, Integer.parseInt(personKeyForTraining));
                    IplImage grayImage = IplImage.create(imagesForTraining[i].width(), imagesForTraining[i].height(), IPL_DEPTH_8U, 1);
                    cvCvtColor(imagesForTraining[i], grayImage, CV_BGR2GRAY);
                    FrameConverter frameConverter = new OpenCVFrameConverter.ToIplImage();
                    Frame frame = frameConverter.convert(grayImage);
                    FrameConverter frameConverter1 = new OpenCVFrameConverter.ToMat();
                    Mat mat = (Mat) frameConverter1.convert(frame);
                    trainImages.put(count,mat);
                    count++;
                }
            }

            System.err.println("hecho.");

            System.err.print("Realizando entrenamiento ...");
            FrameConverter frameConverter = new OpenCVFrameConverter.ToIplImage();
            Frame frame = frameConverter.convert(trainLabels.asIplImage());
            FrameConverter frameConverter1 = new OpenCVFrameConverter.ToMat();
            Mat m = (Mat) frameConverter1.convert(frame);
            fr_binary.train(trainImages,m );
            System.err.println("hecho.");
            storeTrainingData();
        }

    }

    private void loadTrainingData() {
        try {
            File personNameMapFile = new File(personNameMappingFileName);
            if (personNameMapFile.exists()) {
                FileInputStream fis = new FileInputStream(personNameMappingFileName);
                dataMap.load(fis);
                fis.close();
            }


            File binaryDataFile = new File(BinaryFile);
            if(!binaryDataFile.exists())
                binaryDataFile.createNewFile();

            fr_binary.load(BinaryFile);
            System.err.println("hecho");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void storeTrainingData() throws Exception {
        System.err.print("Almacenando modelos ...");

        File binaryDataFile = new File(BinaryFile);
        if (binaryDataFile.exists()) {
            binaryDataFile.delete();
        }
        fr_binary.save(BinaryFile);

        File personNameMapFile = new File(personNameMappingFileName);
        if (personNameMapFile.exists()) {
            personNameMapFile.delete();
        }
        FileOutputStream fos = new FileOutputStream(personNameMapFile, false);
        dataMap.store(fos, "");
        fos.close();

        System.err.println("hecho.");
    }


    private void storeTrainingImages(String personName, IplImage[] images) {
        for (int i = 0; i < images.length; i++) {
            String imageFileName = imageDataFolder + "training\\" + personName + "_" + i + ".bmp";
            File imgFile = new File(imageFileName);
            if (imgFile.exists()) {
                imgFile.delete();
            }
            cvSaveImage(imageFileName, images[i]);
        }
    }

    private IplImage[] readImages(String personName) {
        File imgFolder = new File(imageDataFolder);
        IplImage[] images = null;
        if (imgFolder.isDirectory() && imgFolder.exists()) {
            images = new IplImage[NUM_IMAGES_PER_PERSON];
            for (int i = 0; i < NUM_IMAGES_PER_PERSON; i++) {
                String imageFileName = imageDataFolder + "training\\" + personName + "_" + i + ".bmp";
                IplImage img = cvLoadImage(imageFileName);
                images[i] = img;
            }

        }
        return images;
    }
}
