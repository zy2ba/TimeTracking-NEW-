package ru.zy2ba.tmtrck.form;

import ru.zy2ba.tmtrck.entity.ActivityTypes.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Zy2ba on 22.05.2015.
 */
class ActivityInputPanel extends JPanel implements ActivityInterface{
    private JLabel jlControlHourPanel;
    private JButton jbPlusOne;
    private JButton jbPlusTen;
    private JButton jbMinus;
    private JButton jbClear;
    private double factor;
    private int numberOf;

    public ActivityInputPanel(int mode) {

        super(new GridBagLayout());
        switch (mode) {
            case 2:
                factor = Exam.getFactor();
                break;
            case 3:
                factor = KursProject.getFactor();
                break;
            case 4:
                factor = KursRab.getFactor();
                break;
            case 5:
                factor = DiplomaProject.getFactor();
                break;
            case 6:
                factor = Practice.getFactor();
                break;
            case 7:
                factor = Zachet.getFactor();
                break;
            default:
                factor = 1.0;
                break;
        }
        jlControlHourPanel = new JLabel("0 часов, 0 человек");
        jbPlusOne = new JButton("Добавить человека");
        jbPlusOne.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                numberOf++;
                refresh();
            }
        });

        jbPlusTen = new JButton("Добавить десять человек");
        jbPlusTen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                numberOf += 10;
                refresh();
            }
        });
        jbClear = new JButton("Сбросить");
        jbClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                numberOf = 0;
                jlControlHourPanel.setText("0 часов, 0 человек");
            }
        });
        jbMinus = new JButton("Убрать одного человека");
        jbMinus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (numberOf > 0) {
                    numberOf--;
                    refresh();
                }
            }
        });

        add(jlControlHourPanel, new GridBagConstraints(GridBagConstraints.RELATIVE, GridBagConstraints.RELATIVE, GridBagConstraints.REMAINDER, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        add(jbPlusOne, new GridBagConstraints(GridBagConstraints.RELATIVE, GridBagConstraints.RELATIVE, GridBagConstraints.REMAINDER, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        add(jbPlusTen, new GridBagConstraints(GridBagConstraints.RELATIVE, GridBagConstraints.RELATIVE, GridBagConstraints.REMAINDER, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        add(jbMinus, new GridBagConstraints(GridBagConstraints.RELATIVE, GridBagConstraints.RELATIVE, GridBagConstraints.REMAINDER, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        add(jbClear, new GridBagConstraints(GridBagConstraints.RELATIVE, GridBagConstraints.RELATIVE, GridBagConstraints.REMAINDER, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    }

    private void refresh() {
        jlControlHourPanel.setText((numberOf) + " человек, " + numberOf * factor + " часов");
    }

    public int getNumberOf() {
        return numberOf;
    }

}
