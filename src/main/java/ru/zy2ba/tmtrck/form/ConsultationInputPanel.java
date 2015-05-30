package ru.zy2ba.tmtrck.form;

import ru.zy2ba.tmtrck.entity.ActivityTypes.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Zy2ba on 22.05.2015.
 */
public class ConsultationInputPanel extends JPanel implements ActivityInterface{
    private final JLabel jlControlHourPanel;
    private final double factor;
    private int numberOf;

    public ConsultationInputPanel() {

        super(new GridBagLayout());
        factor = Consult.getFactor();
        jlControlHourPanel = new JLabel("0 консультаций, 0 часов");
        JButton jbPlusOne = new JButton("Добавить консультацию");
        jbPlusOne.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                numberOf++;
                refresh();
            }
        });
        JButton jbClear = new JButton("Сбросить");
        jbClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                numberOf = 0;
                jlControlHourPanel.setText("0 консультаций, 0 часов");
            }
        });
        JButton jbMinus = new JButton("Убрать одну консультацию");
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
        add(jbMinus, new GridBagConstraints(GridBagConstraints.RELATIVE, GridBagConstraints.RELATIVE, GridBagConstraints.REMAINDER, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        add(jbClear, new GridBagConstraints(GridBagConstraints.RELATIVE, GridBagConstraints.RELATIVE, GridBagConstraints.REMAINDER, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    }

    private void refresh() {
        jlControlHourPanel.setText((numberOf) + " консультаций, " + numberOf * factor + " часов");
    }

    public int getNumberOf() {
        return numberOf;
    }
}
