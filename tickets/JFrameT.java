package tickets;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;



public class JFrameT extends JFrame {
    private final JTextField dni = new JTextField(15);
    private Socket socket;
    private DataOutputStream output;
    private DataInputStream input;
    private String name = "T01";
    private int index;

    public void initialize() {
        try {
            socket = new Socket("localhost",6000);
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());

            output.writeUTF("NEW_TICKET_PLATFORM" + "/" + name);
            index = Integer.parseInt(input.readUTF());
         /* PANEL */
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);

         /* LABELS AND FIELDS */
            JLabel label = new JLabel("DNI:");
            dni.setPreferredSize(new Dimension(200, 30));

         /* BUTTONS */
            JButton button = new JButton("Pedir Ticket");

         /* ACTIONS */
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onSubmit(dni);
                }
            });

         /* ADDS */
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.LINE_END;
            panel.add(label, gbc);

            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.LINE_START;
            panel.add(dni, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            panel.add(button, gbc);

            add(panel);

         /* CONFIGS */
            setTitle("Tickets");
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
                            System.out.println(input.readUTF());
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

    private void onSubmit(JTextField field) {
        JFrame frame = new JFrame();
        if(field.getText().length() < 8){
            frame.setVisible(false);
            return;
        }

        try {
            output.writeUTF("DNI_REQUEST" + "/" + field.getText());
            System.out.println("a");
        } catch (Exception e) {
        }

        frame.setTitle("DNI ingresado: " + field.getText()); 
        frame.setSize(300, 150);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
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
