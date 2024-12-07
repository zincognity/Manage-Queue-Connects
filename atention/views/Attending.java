package atention.views;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.*;

import atention.utils.ButtonCustom;
import atention.utils.Countup;

import java.awt.Font;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

public class Attending extends JPanel {
    private JLabel labelTicketName;
    private JLabel labelClientDNI;
    private JLabel labelClientNames;
    private JLabel labelTimeDetail;
    private Countup countup;
    private JTextArea reason;
    private JTextArea response;
    private JButton submit;

    public JLabel getLabelTicketName() {
        return this.labelTicketName;
    }

    public JLabel getLabelClientDNI() {
        return this.labelClientDNI;
    }

    public JLabel getLabelClientNames() {
        return this.labelClientNames;
    }

    public Countup getCountup() {
        return this.countup;
    }

    public JTextArea getReason() {
        return this.reason;
    }

    public JTextArea getResponse() {
        return this.response;
    }

    public JButton getSubmit() {
        return this.submit;
    }

    public Attending(GridBagConstraints gbc) {
        ArrayList<Component> components = new ArrayList<Component>();

        setLayout(new GridBagLayout());
        gbc.insets = new Insets(15, 10, 15, 10);

        components.add(new JLabel("TICKET:"));
        components.add((this.labelTicketName) = new JLabel("Desconocido"));
        components.add(new JLabel("DNI:"));
        components.add((this.labelClientDNI) = new JLabel("Desconocido"));
        components.add(new JLabel("NOMBRES:"));
        components.add((this.labelClientNames) = new JLabel("Desconocido"));
        components.add(new JLabel("TIEMPO TRANSCURRIDO:"));
        components.add((this.labelTimeDetail) = new JLabel("00:00:00"));
        countup = new Countup(labelTimeDetail);
        components.add(new JLabel("RAZÓN:"));
        components.add(new JScrollPane((this.reason) = new JTextArea(8, 20)));
        components.add(new JLabel("RESPUESTA:"));
        components.add(new JScrollPane((this.response) = new JTextArea(8, 20)));
        components.add((this.submit) = new ButtonCustom("GUARDAR", new Dimension(150, 40)));

        int index = 0;
        gbc.gridwidth = 1;

        Font menuFont = new Font("SansSerif", Font.BOLD, 16);
        Color textColor = Color.decode("#004DFF");

        for (Component component : components) {
            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                label.setFont(menuFont);
                if (label != labelTicketName && label != labelClientDNI && label != labelClientNames
                        && label != labelTimeDetail) {
                    gbc.gridx = 0;
                    gbc.gridy = index++;
                    gbc.anchor = GridBagConstraints.LINE_START;
                    add(label, gbc);
                } else {
                    label.setForeground(textColor);
                    gbc.gridx = 1;
                    gbc.gridy = index - 1;
                    gbc.anchor = GridBagConstraints.LINE_START;
                    add(label, gbc);
                }
            } else if (component instanceof JScrollPane) {
                gbc.gridx = 1;
                gbc.gridy = index - 1;
                gbc.gridwidth = 2;
                gbc.weightx = 1.0;
                gbc.weighty = 0.5;
                gbc.fill = GridBagConstraints.BOTH;
                add(component, gbc);
            } else if (component instanceof JButton) {
                component.setFont(menuFont);
                gbc.gridx = 0;
                gbc.gridy = index++;
                gbc.gridwidth = 3;
                gbc.fill = GridBagConstraints.NONE;
                gbc.anchor = GridBagConstraints.CENTER;
                add(component, gbc);
            }
        }
    }
}
