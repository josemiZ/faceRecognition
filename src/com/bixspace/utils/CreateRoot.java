package com.bixspace.utils;

import java.io.File;

/**
 * Created by MIGUEL ZEA on 12/11/2016.
 */
public class CreateRoot {
    public static void createWorkspace(){
        File directory = new File("C:\\tutor_assistance\\data\\images\\training");
        if(!directory.exists()){
            if(directory.mkdirs()){
                System.out.println("Creado");

            }else{
                System.out.println("Error");
            }
        }
    }
}
