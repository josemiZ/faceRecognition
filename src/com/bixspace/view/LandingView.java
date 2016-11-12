package com.bixspace.view;



import com.bixspace.core.BaseView;
import com.bixspace.utils.CreateRoot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by MIGUEL ZEA on 9/11/2016.
 */
public class LandingView implements BaseView {
    private JFrame landingFrame;
    JPanel mainPanel;
    Button registerButton;
    Button assistanceButton;

    public LandingView() {
        prepareGUI();
    }


    @Override
    public void prepareGUI() {
        landingFrame = new JFrame("Asistencia Docente");
        landingFrame.setSize(400,350);

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        registerButton = new Button("Registrar Docente");
        assistanceButton = new Button("Tomar asistencia Docente");

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=1;
        c.gridy=0;
        c.ipady=15;
        c.insets = new Insets(0,0,40,0);
        mainPanel.add(assistanceButton,c);

        c.gridy=1;
        c.insets=new Insets(0,0,0,0);
        mainPanel.add(registerButton,c);

        landingFrame.add(mainPanel);
        landingFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        landingFrame.setLocationRelativeTo(null);

        CreateRoot.createWorkspace();

        addClickListeners();
    }

    private void addClickListeners(){
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterView registerView= new RegisterView();
                registerView.show();
                landingFrame.dispose();
            }
        });
        assistanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RecognizeFaceView recognizeFaceFromCameraView = new RecognizeFaceView();
                recognizeFaceFromCameraView.setVisible(true);
                landingFrame.dispose();
            }
        });
    }

    @Override
    public void show() {
        landingFrame.setVisible(true);
    }
}
