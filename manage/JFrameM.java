package manage;

import javax.swing.*;

import tickets.utils.ConfigLoader;

import java.awt.*;
import java.awt.event.*;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class JFrameM extends JFrame {
    private static ConfigLoader config = new ConfigLoader("manage/config/config.properties");
    private Socket socket;
    private DataOutputStream output;
    private DataInputStream input;
    private int index;
    private JLabel labelMessage;

    public void initialize() {
        try {
            socket = new Socket(config.getProperty("control.server"),Integer.parseInt(config.getProperty("control.port")));
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());

            output.writeUTF("GET_ATENTIONS_PLATFORM");
            String atentionString = input.readUTF();
            String[] atentions = atentionString.split(",");

         /* PANEL */
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);

         /* LABELS AND FIELDS */

         /* BUTTONS */

         /* ACTIONS */

         /* ADDS */
            for (String string : atentions) {
                String[] values = string.split(":");
                labelMessage = new JLabel(values[0] + ":" + values[2]);
                gbc.gridx = 0;
                gbc.gridy = Integer.parseInt(values[1]);
                gbc.gridwidth = 2;
                gbc.anchor = GridBagConstraints.CENTER;
                panel.add(labelMessage, gbc);
            }
            add(panel);

         /* CONFIGS */
            setTitle("Manage");
            setSize(400, 200);
            setMinimumSize(new Dimension(300, 200));
            setLocationRelativeTo(null);
            setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                        try {
                            output.writeUTF("CLOSE_TICKET_PLATFORM" + "/" + index);
                            output.flush();
                        } catch (IOException e1) {
                            System.err.println("Error al enviar el mensaje: " + e1.getMessage());
                            e1.printStackTrace();
                        } finally {
                            closeResources();
                            dispose();
                        }
                        dispose();
                }
            });
            setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeResources() {
        try {
            if (output != null) output.close();
            if (input != null) input.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
