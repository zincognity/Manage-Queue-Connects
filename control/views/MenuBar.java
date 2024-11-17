package control.views;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.BoxLayout;

import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.UIManager;

public class MenuBar extends JMenuBar {
    private JMenu serverBar;
    private JMenuItem startServer;
    private JMenuItem restartServer;
    private JMenuItem stopServer;

    private JMenu configBar;
    private JMenuItem configMenu;

    private JMenu helpBar;
    private JMenuItem help;

    public JMenuItem getConfigMenu() {
        return this.configMenu;
    }

    public JMenuItem getStartServer() {
        return this.startServer;
    }

    public JMenuItem getRestartServer() {
        return this.restartServer;
    }

    public JMenuItem getStopServer() {
        return this.stopServer;
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

        Font menuFont = new Font("Arial Semibold", Font.PLAIN, 16);
        Color textColor = Color.decode("#454545");

        serverBar = new JMenu("Servidor");
        serverBar.setFont(menuFont);
        serverBar.setForeground(textColor);
        serverBar.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        serverBar.setOpaque(true);

        startServer = new JMenuItem("Iniciar Servidor");
        startServer.setFont(menuFont);
        serverBar.add(startServer);

        restartServer = new JMenuItem("Reiniciar Servidor");
        restartServer.setFont(menuFont);
        serverBar.add(restartServer);

        stopServer = new JMenuItem("Detener Servidor");
        stopServer.setFont(menuFont);
        serverBar.add(stopServer);

        add(serverBar);
        add(Box.createRigidArea(new Dimension(20, 0)));

        configBar = new JMenu("Configuración");
        configBar.setFont(menuFont);
        configBar.setForeground(textColor);
        configBar.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        configBar.setOpaque(true);

        configMenu = new JMenuItem("Cambiar Puerto");
        configMenu.setFont(menuFont);
        configBar.add(configMenu);

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
