package atention.views;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;

public class Attending extends JPanel {
    private JLabel labelTicketName;
    private JLabel labelClientName;
    private JTextArea reason;
    private JTextArea response;
    private JCheckBox solved;
    private JButton submit;

    public JLabel getLabelTicketName() {
        return this.labelTicketName;
    }

    public JLabel getClientName() {
        return this.labelClientName;
    }

    public JTextArea getReason() {
        return this.reason;
    }

    public JTextArea getResponse() {
        return this.response;
    }

    public JCheckBox getSolved() {
        return this.solved;
    }

    public JButton getSubmit() {
        return this.submit;
    }

    public Attending(GridBagConstraints gbc) {
        ArrayList<JLabel> labelsStatic = new ArrayList<>();
        ArrayList<JLabel> labelsResponse = new ArrayList<>();

        /* CONFIG */
        setLayout(new GridBagLayout());
        gbc.insets = new Insets(15, 10, 15, 10);

        /* LABELS AND FIELDS */
        labelsStatic.add(new JLabel("TICKET:"));
        labelsResponse.add((labelTicketName = new JLabel("Desconocido")));
        labelsStatic.add(new JLabel("CLIENTE"));
        labelsResponse.add((labelClientName = new JLabel("Desconocido")));
        /* ADDS */
        int index = 1;
        gbc.gridwidth = 1;

        Font menuFont = new Font("Arial Semibold", Font.PLAIN, 16);
        Color textColor = Color.decode("#004DFF");

        for (JLabel label : labelsStatic) {
            label.setFont(menuFont);
            gbc.gridx = 0;
            gbc.gridy = index;
            gbc.anchor = GridBagConstraints.LINE_START;

            add(label, gbc);

            gbc.gridx = 1;
            gbc.gridy = index;
            labelsResponse.get(index - 1).setForeground(textColor);
            labelsResponse.get(index - 1).setFont(menuFont);

            add(labelsResponse.get(index - 1), gbc);
            index++;
        }

        reason = new JTextArea(8, 40);
        reason.setPreferredSize(new Dimension(200, 30));
        reason.setFont(menuFont);
        gbc.gridx = 0;
        gbc.gridy = index;
        gbc.anchor = GridBagConstraints.LINE_START;

        add(reason, gbc);

        response = new JTextArea(8, 40);
        response.setPreferredSize(new Dimension(200, 30));
        response.setFont(menuFont);
        gbc.gridx = 0;
        gbc.gridy = index + 1;
        gbc.anchor = GridBagConstraints.LINE_START;

        add(response, gbc);

        solved = new JCheckBox("RESUELTO");
        solved.setFont(menuFont);
        gbc.gridx = 0;
        gbc.gridy = index + 2;
        gbc.anchor = GridBagConstraints.LINE_START;

        add(solved, gbc);

        submit = new JButton("GUARDAR");
        submit.setFont(menuFont);
        gbc.gridx = 0;
        gbc.gridy = index + 3;
        gbc.anchor = GridBagConstraints.LINE_START;

        add(submit, gbc);
    }
}
