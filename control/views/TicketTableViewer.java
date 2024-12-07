package control.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import control.JFrameC;
import control.utils.TicketTable;
import types.Ticket;

import java.awt.event.MouseEvent;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TicketTableViewer extends JFrame {
    private JFrameC window;
    private GridBagConstraints gbc = new GridBagConstraints();
    private JPanel panel = new JPanel(new GridBagLayout());
    private TicketTable ticketTable;
    private static JTextField searchField = new JTextField();
    private static JButton searchButton = new JButton("Buscar");
    private static JCheckBox dniCheckBox = new JCheckBox("DNI");
    private static JCheckBox nameCheckBox = new JCheckBox("Nombres");

    public void updateData(ArrayList<Ticket> filterTickets) {
        panel.removeAll();
        if (searchField.getText().length() < 1)
            ticketTable = new TicketTable(window.getTicketsManage().getTicketsAttended());
        else
            ticketTable = new TicketTable(filterTickets);
        JTable table = new JTable(ticketTable);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.rowAtPoint(e.getPoint());

                    if (row >= 0) {
                        Ticket ticket = window.getTicketsManage().getTicketsAttended().get(row);

                        JFrame detailFrame = new JFrame("Detalles del ticket " + ticket.getName());
                        detailFrame.setSize(300, 200);

                        JPanel panel = new JPanel();
                        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                        panel.add(new JLabel("Ticket: " + ticket.getName()));
                        panel.add(new JLabel(
                                "Cliente: " + ticket.getClient().getName() + " " + ticket.getClient().getLastName()
                                        + " (" + ticket.getClient().getDNI() + ")"));
                        panel.add(new JLabel("Atendido por: " + ticket.getAtention().getName()));
                        panel.add(new JLabel(
                                "Creado el: " + formatDateTime(ticket.getCreateAt())));
                        panel.add(new JLabel(
                                "Tiempo de demora: " + getTimeDifference(ticket.getCreateAt(), ticket.getSolvedAt())));
                        panel.add(new JLabel("Resuelto el: " + formatDateTime(ticket.getSolvedAt())));
                        panel.add(new JLabel("Razón: " + ticket.getReason()));
                        panel.add(new JLabel("Respuesta: " + ticket.getResponse()));

                        detailFrame.add(panel);
                        detailFrame.setVisible(true);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(searchField, gbc);
        gbc.gridy = 0;
        panel.add(searchButton);
        gbc.gridx = 2;
        panel.add(dniCheckBox, gbc);
        gbc.gridy = 1;
        panel.add(nameCheckBox, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        panel.add(scrollPane, gbc);
        add(panel);
        revalidate();
        repaint();
    }

    public TicketTableViewer(JFrameC window) {
        this.window = window;
        searchField.setPreferredSize(new Dimension(200, 30));
        updateData(null);

        setTitle("Historial de Tickets");
        setLayout(new BorderLayout());
        add(panel);

        dniCheckBox.setSelected(true);
        dniCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (nameCheckBox.isSelected()) {
                    dniCheckBox.setSelected(true);
                    nameCheckBox.setSelected(false);
                } else {
                    dniCheckBox.setSelected(true);
                }
            }
        });

        nameCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (dniCheckBox.isSelected()) {
                    nameCheckBox.setSelected(true);
                    dniCheckBox.setSelected(false);
                } else {
                    nameCheckBox.setSelected(true);
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ArrayList<Ticket> filter = new ArrayList<Ticket>();
                window.getTicketsManage().getTicketsAttended().stream().forEach(ticket -> {
                    if (dniCheckBox.isSelected() && searchField.getText().length() > 1
                            && Integer.toString(ticket.getClient().getDNI()).startsWith(searchField.getText())) {
                        filter.add(ticket);
                    }
                    if (nameCheckBox.isSelected() && searchField.getText().length() > 1
                            && ticket.getClient().getName().startsWith(searchField.getText())) {
                        filter.add(ticket);
                    }
                });
                updateData(filter);
            }
        });

        setSize(500, 700);
        setMinimumSize(new Dimension(500, 700));
        setMaximumSize(new Dimension(500, 700));
        setLocationRelativeTo(window);
        setVisible(true);
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        return dateTime.format(formatter);
    }

    public String getTimeDifference(LocalDateTime createAt, LocalDateTime solvedAt) {
        Duration duration = Duration.between(createAt, solvedAt);

        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

}
