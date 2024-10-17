package atention;

import javax.swing.*;

import atention.utils.ConfigLoader;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;



public class JFrameA extends JFrame {
    private static ConfigLoader config = new ConfigLoader("atention/config/config.properties");
    private Socket socket;
    private DataOutputStream output;
    private DataInputStream input;
    private int index;
    private JLabel labelMessage;
    private JButton buttonMessage;

    public void initialize() {
        try {
            socket = new Socket(config.getProperty("control.server"),Integer.parseInt(config.getProperty("control.port")));
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());
            output.writeUTF("NEW_ATENTION_PLATFORM" + "/" + config.getProperty("atention.name"));
            index = Integer.parseInt(input.readUTF());

            output.writeUTF("GET_QUEUE");
            String queueString = input.readUTF();
            

        /* PANEL */
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);

         /* LABELS AND FIELDS */

         /* BUTTONS */

         /* ACTIONS */

         /* ADDS */
            if(queueString.length() > 0) {
                String[] queue = queueString.split(",");
                for (String string : queue) {
                    String[] values = string.split(":");
                    labelMessage = new JLabel(values[0]);
                    gbc.gridx = 0;
                    gbc.gridy = Integer.parseInt(values[1]);
                    buttonMessage = new JButton("Atender Ticket");
                    panel.add(labelMessage, gbc);
                    gbc.gridx = 1;
                    panel.add(buttonMessage, gbc);
                }
            } else {
                JLabel asd = new JLabel("Libre");
                panel.add(asd);
            }
            add(panel);

         /* CONFIGS */
            setTitle("Atención");
            setSize(400, 200);
            setMinimumSize(new Dimension(300, 200));
            setLocationRelativeTo(null);
            setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                        try {
                            output.writeUTF("CLOSE_ATENTION_PLATFORM" + "/" + Integer.toString(index));
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
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
