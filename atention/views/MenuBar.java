package atention.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;

public class MenuBar extends JMenuBar {
    private JMenu serverBar;
    private JMenuItem connectServer;
    private JMenuItem disconnectServer;

    private JMenu configBar;
    private JMenuItem changePort;
    private JMenuItem activeAutomatic;

    private JMenu helpBar;
    private JMenuItem help;

    public JMenuItem getConnectServer() {
        return this.connectServer;
    }

    public JMenuItem getDisconnectServer() {
        return this.disconnectServer;
    }

    public JMenuItem getChangePort() {
        return this.changePort;
    }

    public JMenuItem getActiveAutomatic() {
        return this.activeAutomatic;
    }

    public JMenuItem getHelp() {
        return this.help;
    }

    public MenuBar() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createEmptyBorder());
        setOpaque(false);

        add(Box.createHorizontalGlue());

        Font menuFont = new Font("Arial SemiBold", Font.PLAIN, 16);
        Color textColor = Color.decode("#454545");

        serverBar = new JMenu("Servidor");
        serverBar.setFont(menuFont);
        serverBar.setForeground(textColor);
        serverBar.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        serverBar.setOpaque(true);

        connectServer = new JMenuItem("Conectar Servidor");
        connectServer.setFont(menuFont);
        serverBar.add(connectServer);

        disconnectServer = new JMenuItem("Desconectar Servidor");
        disconnectServer.setFont(menuFont);
        serverBar.add(disconnectServer);

        add(serverBar);
        add(Box.createRigidArea(new Dimension(20, 0)));

        configBar = new JMenu("Configuración");
        configBar.setFont(menuFont);
        configBar.setForeground(textColor);
        configBar.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        configBar.setOpaque(true);

        changePort = new JMenuItem("Cambiar Puerto");
        changePort.setFont(menuFont);
        configBar.add(changePort);

        activeAutomatic = new JMenuItem("Cambiar a automatico / manual");
        activeAutomatic.setFont(menuFont);
        configBar.add(activeAutomatic);

        add(configBar);
        add(Box.createRigidArea(new Dimension(20, 0)));

        helpBar = new JMenu("Ayuda");
        helpBar.setFont(menuFont);
        helpBar.setForeground(textColor);
        helpBar.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        helpBar.setOpaque(true);

        help = new JMenuItem("Obtener ayuda");
        help.setFont(menuFont);
        helpBar.add(help);

        add(helpBar);
        add(Box.createHorizontalGlue());
    }

}
