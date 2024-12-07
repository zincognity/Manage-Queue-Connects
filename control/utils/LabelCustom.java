package control.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class LabelCustom extends JLabel {
    public LabelCustom(String label, Font font) {
        setText(label);
        setForeground(new Color(50, 50, 50));
        setFont(font);
        setPreferredSize(new Dimension(150, 35));
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(254, 254, 254));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

        super.paintComponent(g);
    }
}