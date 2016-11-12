package com.bixspace;

import com.bixspace.view.LandingView;
import org.opencv.core.Core;

/**
 * Created by MIGUEL ZEA on 9/11/2016.
 */
public class TutorAssistance {
    public static void main (String args[]){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        LandingView landingView = new LandingView();
        landingView.show();
    }
}
