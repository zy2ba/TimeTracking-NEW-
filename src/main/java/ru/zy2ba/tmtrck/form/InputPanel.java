package ru.zy2ba.tmtrck.form;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Zy2ba on 22.05.2015.
 */
class InputPanel extends JPanel {

    private JTextField username;
    private JTextField passWord;
    private JTextField userlastname;

    public InputPanel() {

        super(new GridBagLayout());
        JLabel lblN = new JLabel("Имя:");
        JLabel lblLN = new JLabel("Фамилия");
        JLabel lblPW = new JLabel("Пароль");
        username = new JTextField(20);
        username.setText("root");
        userlastname = new JTextField(20);
        userlastname.setText("root");
        passWord = new JPasswordField(20);
        passWord.setText("root");
        add(lblN, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 0, 0));
        add(username, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        add(lblLN, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 0, 0));
        add(userlastname, new GridBagConstraints(1, 1, 1, 1, 1, 0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        add(lblPW, new GridBagConstraints(0, 2, 1, 1, 0, 0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 5), 0, 0));
        add(passWord, new GridBagConstraints(1, 2, 1, 1, 1, 0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));
    }


    public String getUsername() {
        if (username.getText() == null) return "";
        return username.getText();
    }

    public String getUserlastname() {
        if (userlastname.getText() == null) return "";
        return userlastname.getText();
    }

    public String getPassWord() {
        if (passWord.getText() == null) return "";
        return passWord.getText();
    }
}
