package com.bixspace.view;



import com.bixspace.core.BaseView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by MIGUEL ZEA on 9/11/2016.
 */
public class RegisterView implements BaseView {
    private JFrame jFrame;
    private Button registerButton;
    private Button cancelButton;
    private TextField nameText;
    private TextField lastNameText;


    public RegisterView() {
        prepareGUI();
    }

    @Override
    public void prepareGUI() {
        jFrame = new JFrame("Registro de Docentes");
        jFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        Label titleRegister = new Label("Registro de docente");
        Font font = new Font("SansSerif", Font.BOLD, 45);
        titleRegister.setFont(font);
        c.gridx=0;
        c.gridy=0;
        c.insets = new Insets(40,50,40,20);
        mainPanel.add(titleRegister,c);

        Label inputDataRegister = new Label("Ingrese Datos del Docente");
        Font font2 = new Font("SansSerif", Font.BOLD, 20);
        inputDataRegister.setFont(font2);
        c.gridx=0;
        c.gridy=1;
        c.insets = new Insets(0,5,0,0);
        mainPanel.add(inputDataRegister,c);


        JPanel nameTutorPanel = new JPanel();
        Label nameTitle = new Label("Nombre");
        Font nameFont = new Font("SanSerif",Font.BOLD,15);
        nameTitle.setFont(nameFont);
        nameTutorPanel.add(nameTitle);

        nameText = new TextField(25);
        nameTutorPanel.add(nameText);

        c.gridx=0;
        c.gridy=2;
        mainPanel.add(nameTutorPanel,c);

        JPanel lastNameTutorPanel = new JPanel();
        Label lastNameTitle = new Label("Apellido");
        lastNameTitle.setFont(nameFont);
        lastNameTutorPanel.add(lastNameTitle);

        lastNameText = new TextField(25);
        lastNameTutorPanel.add(lastNameText);

        c.gridy=3;
        mainPanel.add(lastNameTutorPanel,c);

        JPanel codeTutorPanel = new JPanel();
        Label codeTitle = new Label("Código");
        codeTitle.setFont(nameFont);
        codeTutorPanel.add(codeTitle);

        TextField codeText = new TextField(25);
        codeTutorPanel.add(codeText);

        c.gridy=4;
        mainPanel.add(codeTutorPanel,c);

        JPanel passwordTutorPanel = new JPanel();
        Label passwordTitle = new Label("Contraseña");
        passwordTitle.setFont(nameFont);
        passwordTutorPanel.add(passwordTitle);

        JPasswordField passwordField = new JPasswordField(17);
        passwordTutorPanel.add(passwordField);

        c.gridy=5;
        mainPanel.add(passwordTutorPanel,c);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        registerButton = new Button("Registrar");
        cancelButton = new Button("Cancelar");

        GridBagConstraints d = new GridBagConstraints();
        d.fill = GridBagConstraints.HORIZONTAL;
        d.gridx=0;
        d.gridy=0;
        d.ipady=15;
        d.insets = new Insets(20,5,20,5);

        buttonPanel.add(registerButton,d);

        d.gridx=1;
        buttonPanel.add(cancelButton,d);

        c.gridy=6;
        mainPanel.add(buttonPanel,c);

        jFrame.add(mainPanel,BorderLayout.CENTER);
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        addClickListeners();
    }

    private void addClickListeners(){
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.dispose();
                RegisterFaceView registerFaceView = new RegisterFaceView(nameText.getText(),lastNameText.getText());
                registerFaceView.setVisible(true);

            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.dispose();
                LandingView landingView= new LandingView();
                landingView.show();

            }
        });
    }

    @Override
    public void show() {
        jFrame.setVisible(true);
    }
}
