package control.views;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

public class ServerData extends JPanel {
    private JLabel labelIP;
    private JLabel labelPort;
    private JLabel labelQueueSizeMax;
    private JLabel labelQueueSize;
    private JLabel labelManages;
    private JLabel labelTickets;
    private JLabel labelAtentions;

    public JLabel getLabelIP() {
        return this.labelIP;
    }

    public JLabel getLabelPort() {
        return this.labelPort;
    }

    public JLabel getLabelQueueSizeMax() {
        return this.labelQueueSizeMax;
    }

    public JLabel getLabelQueueSize() {
        return this.labelQueueSize;
    }

    public JLabel getLabelManages() {
        return this.labelManages;
    }

    public JLabel getLabelTickets() {
        return this.labelTickets;
    }

    public JLabel getLabelAtentions() {
        return this.labelAtentions;
    }

    public ServerData(GridBagConstraints gbc) {
        ArrayList<JLabel> labelsStatic = new ArrayList<JLabel>();
        ArrayList<JLabel> labelsResponse = new ArrayList<JLabel>();

        /* CONFIG */
        setLayout(new GridBagLayout());
        gbc.insets = new Insets(15, 10, 15, 10);

        /* LABELS AND FIELDS */
        labelsStatic.add(new JLabel("DIRECCIÓN IP:"));
        labelsResponse.add((labelIP = new JLabel("Desconocida")));
        labelsStatic.add(new JLabel("PUERTO:"));
        labelsResponse.add((labelPort = new JLabel("Desconocida")));
        labelsStatic.add(new JLabel("TAMAÑO MÁXIMO DE LA COLA:"));
        labelsResponse.add((labelQueueSizeMax = new JLabel("Desconocida")));
        labelsStatic.add(new JLabel("PERSONAS EN LA COLA:"));
        labelsResponse.add((labelQueueSize = new JLabel("0")));
        labelsStatic.add(new JLabel("ATENCIONES TRABAJANDO:"));
        labelsResponse.add(labelAtentions = new JLabel("0"));
        labelsStatic.add(new JLabel("PANTALLAS TRABAJANDO:"));
        labelsResponse.add((labelManages = new JLabel("0")));
        labelsStatic.add(new JLabel("TICKETS TRABAJANDO:"));
        labelsResponse.add((labelTickets = new JLabel("0")));

        /* ADDS */
        int index = 0;
        for (JLabel label : labelsStatic) {
            gbc.gridx = 0;
            gbc.gridy = index;
            gbc.anchor = GridBagConstraints.LINE_START;
            add(label, gbc);

            gbc.gridx = 1;
            gbc.gridy = index;
            add(labelsResponse.get(index), gbc);
            index++;
        }
    }
}
