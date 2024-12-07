package manage.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import types.Atention;
import types.Ticket;
import manage.utils.LabelCustom;

public class AtentionStatus extends JPanel {
    private JPanel atentionsPanel;

    public void updateData(ArrayList<Atention> atentions, ArrayList<Ticket> tickets) {
        atentionsPanel.removeAll();

        atentionsPanel.setLayout(new GridBagLayout());
        GridBagConstraints panelGbc = new GridBagConstraints();
        panelGbc.insets = new Insets(5, 5, 5, 5);
        panelGbc.fill = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < atentions.size(); i++) {
            panelGbc.gridx = 0;
            panelGbc.gridy = i;
            JLabel atentionLabel = new LabelCustom(atentions.get(i).getName(), null);
            atentionsPanel.add(atentionLabel, panelGbc);

            Color color = getAtention(atentions.get(i), tickets).startsWith("DISPONIBLE") ? new Color(142, 228, 129)
                    : new Color(255, 138, 138);
            panelGbc.gridx = 1;
            JLabel atentionStatus = new LabelCustom(getAtention(atentions.get(i), tickets), color);
            atentionsPanel.add(atentionStatus, panelGbc);
        }
    }

    private String getAtention(Atention atention, ArrayList<Ticket> tickets) {
        for (Ticket tkt : tickets) {
            if (tkt.getAtention() != null && !tkt.isSolved()) {
                if (tkt.getAtention().getName().equals(atention.getName()))
                    return tkt.getName();
            }
        }
        return "DISPONIBLE";
    }

    public AtentionStatus() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);

        ImageIcon icon = new ImageIcon("manage/images/manage.png");
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
        JLabel atentionsMessage = new JLabel("ESTADO DE ATENCIÓN");
        atentionsMessage.setFont(new Font("SansSerif", Font.BOLD, 14));
        topPanel.add(atentionsMessage, gbc);

        add(topPanel, BorderLayout.NORTH);

        atentionsPanel = new JPanel(new GridBagLayout());
        add(atentionsPanel, BorderLayout.CENTER);
    }
}
