package control.views;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class MenuBar extends JMenuBar {
    private JMenu serverBar;
    private JMenuItem startServer;
    private JMenuItem restartServer;
    private JMenuItem stopServer;

    private JMenu configBar;
    private JMenuItem configMenu;

    private JMenu helpBar;

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

    public MenuBar() {
        serverBar = new JMenu("Servidor");
        serverBar.setMnemonic('S');

        startServer = new JMenuItem("Iniciar Servidor");
        startServer.setMnemonic('G');
        serverBar.add(startServer);

        restartServer = new JMenuItem("Reiniciar Servidor");
        restartServer.setMnemonic('R');
        serverBar.add(restartServer);

        stopServer = new JMenuItem("Detener Servidor");
        stopServer.setMnemonic('Z');

        serverBar.add(stopServer);
        add(serverBar);

        configBar = new JMenu("Configuración");
        configBar.setMnemonic('M');

        configMenu = new JMenuItem("Cambiar Puerto");
        configMenu.setMnemonic('C');
        configBar.add(configMenu);

        add(configBar);

        helpBar = new JMenu("Ayuda");
        helpBar.setMnemonic('H');
        add(helpBar);
    }
}
