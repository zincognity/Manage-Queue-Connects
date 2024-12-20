package control.views;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.*;

import tickets.utils.LabelCustom;

import java.awt.Font;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

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
        ArrayList<JLabel> labelsStatic = new ArrayList<>();
        ArrayList<JLabel> labelsResponse = new ArrayList<>();

        setLayout(new GridBagLayout());
        gbc.insets = new Insets(10, 10, 10, 10);

        ImageIcon icon = new ImageIcon("control/images/config.png");
        Image scaledImage = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(imageLabel, gbc);

        Font menuFont = new Font("SansSerif", Font.BOLD, 14);

        labelsStatic.add(new JLabel("DIRECCIÓN IP:"));
        labelsResponse.add((labelIP = new LabelCustom("DESCONOCIDA", menuFont)));
        labelsStatic.add(new JLabel("PUERTO:"));
        labelsResponse.add((labelPort = new LabelCustom("DESCONOCIDA", menuFont)));
        labelsStatic.add(new JLabel("ATENCIONES TRABAJANDO:"));
        labelsResponse.add(labelAtentions = new LabelCustom("0", menuFont));
        labelsStatic.add(new JLabel("PANTALLAS TRABAJANDO:"));
        labelsResponse.add((labelManages = new LabelCustom("0", menuFont)));
        labelsStatic.add(new JLabel("TICKETS TRABAJANDO:"));
        labelsResponse.add((labelTickets = new LabelCustom("0", menuFont)));
        labelsStatic.add(new JLabel("TAMAÑO MÁXIMO DE LA COLA:"));
        labelsResponse.add((labelQueueSizeMax = new LabelCustom("DESCONOCIDA", menuFont)));
        labelQueueSize = new JLabel("0");

        int index = 1;
        gbc.gridwidth = 1;

        for (JLabel label : labelsStatic) {
            label.setFont(menuFont);
            gbc.gridx = 0;
            gbc.gridy = index;
            gbc.anchor = GridBagConstraints.LINE_START;

            add(label, gbc);

            gbc.gridx = 1;
            gbc.gridy = index;
            gbc.anchor = GridBagConstraints.CENTER;
            add(labelsResponse.get(index - 1), gbc);
            index++;
        }

        JLabel labelQueueSizeStatic = new JLabel("Personas en cola");
        labelQueueSizeStatic.setFont(menuFont);
        labelQueueSizeStatic.setHorizontalAlignment(SwingConstants.CENTER);
        labelQueueSizeStatic.setBorder(BorderFactory.createEmptyBorder(180, 0, 0, 0));
        gbc.gridx = 0;
        gbc.gridy = index;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        Color backgroundColor = Color.decode("#F0F0F0");
        setBackground(backgroundColor);
        add(labelQueueSizeStatic, gbc);
    }

    @Override
    protected void paintComponent(Graphics box) {
        super.paintComponent(box);
        box.setColor(Color.decode("#95d7d3"));

        int squareSize = 160;
        int x = (getWidth() - squareSize) / 2;
        int y = getHeight() - squareSize - 50;

        box.fillRect(x, y, squareSize, squareSize);

        box.setColor(Color.decode("#454545"));
        box.setFont(new Font("Arial", Font.BOLD, 72));

        FontMetrics metrics = box.getFontMetrics(box.getFont());
        int textX = x + (squareSize - metrics.stringWidth(labelQueueSize.getText())) / 2;
        int textY = y + ((squareSize - metrics.getHeight()) / 2) + metrics.getAscent();

        box.drawString(labelQueueSize.getText(), textX, textY);
    }

}
