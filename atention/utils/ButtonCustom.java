package atention.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;

public class ButtonCustom extends JButton {
    public ButtonCustom(String label, Dimension dimension) {
        setText(label);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setForeground(new Color(50, 50, 50));
        setFont(new Font("SansSerif", Font.BOLD, 14));
        setPreferredSize(dimension);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(195, 255, 245));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

        if (getModel().isPressed()) {
            g2.setColor(new Color(201, 255, 196));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        }

        super.paintComponent(g);
    }
}
