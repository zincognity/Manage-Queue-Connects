package atention.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import atention.utils.ButtonCustom;
import types.Ticket;

public class TicketsData extends JPanel {
    private JLabel ticketsQueue;
    private JPanel ticketsPanel;
    private ArrayList<JButton> ticketsButton;

    public JLabel getTicketsQueue() {
        return this.ticketsQueue;
    }

    public ArrayList<JButton> getTicketButton() {
        return this.ticketsButton;
    }

    public void updateData(ArrayList<Ticket> tickets) {
        ticketsPanel.removeAll();
        ticketsButton.clear();

        ticketsPanel.setLayout(new GridBagLayout());
        GridBagConstraints panelGbc = new GridBagConstraints();
        panelGbc.insets = new Insets(5, 5, 5, 5);
        panelGbc.fill = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < tickets.size(); i++) {
            Ticket ticket = tickets.get(i);
            JButton tktButton = new ButtonCustom(ticket.getName(), new Dimension(100, 40));

            panelGbc.gridx = i % 3;
            panelGbc.gridy = i / 3;
            ticketsPanel.add(tktButton, panelGbc);
            ticketsButton.add(tktButton);
        }
        revalidate();
        repaint();
    }

    public TicketsData() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);

        ImageIcon icon = new ImageIcon("atention/images/atention.png");
        Image scaledImage = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        topPanel.add(imageLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel ticketsMessage = new JLabel("TICKETS EN ESPERA");
        topPanel.add(ticketsMessage, gbc);

        gbc.gridx = 1;
        ticketsQueue = new JLabel("0");
        topPanel.add(ticketsQueue, gbc);

        add(topPanel, BorderLayout.NORTH);

        ticketsPanel = new JPanel(new GridBagLayout());
        ticketsButton = new ArrayList<JButton>();
        add(ticketsPanel, BorderLayout.CENTER);
    }

}
