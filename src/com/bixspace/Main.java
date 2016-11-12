package com.bixspace;

import com.bixspace.utils.FaceRecognition;
import org.bytedeco.javacpp.opencv_core;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.bytedeco.javacpp.opencv_core.cvGetSeqElem;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;

public class Main {

    public static void main(String[] args) {

        try {
            FaceRecognition reconocer = FaceRecognition.getInstance();

            //Entrenamiento
            opencv_core.IplImage[] trainImages = new opencv_core.IplImage[10];
            for(int i=0; i< 10; i++){
                trainImages[i]=cvLoadImage("C:\\tutor_assistance\\data\\images\\jose zea"+i+".jpg");
                opencv_core.CvSeq faces = reconocer.detectFace(trainImages[i]);
                opencv_core.CvRect r = new opencv_core.CvRect(cvGetSeqElem(faces,0));
                trainImages[i]=reconocer.preprocessImage(trainImages[i], r);
            }
            reconocer.learnNewFace("Jose Zea", trainImages);

            //Reconocimiento
            opencv_core.IplImage target = new opencv_core.IplImage();
            target = cvLoadImage("C:\\facerecognizer\\data\\images\\cr7_target.jpg");
            opencv_core.CvSeq faces2 = reconocer.detectFace(target);
            opencv_core.CvRect r2 = new opencv_core.CvRect(cvGetSeqElem(faces2,0));
            target=reconocer.preprocessImage(target, r2);
            System.out.println("Persona Identificada: "+reconocer.identifyFace(target));
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }/*
        System.out.println("Cargar y Mostrar una Imagen con OpenCV en Java y Netbeans");
        System.loadLibrary("opencv_java2413");*/
    }
}
