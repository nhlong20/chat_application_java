package server;

import server.views.ServerGUI;

import javax.swing.*;

/**
 * server
 *
 * @created by ASUS - StudentID : 18120449
 * @Date 6/16/2021 - 10:23 PM
 * @Description
 */
public class ServerManager {
    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
        SwingUtilities.invokeLater(ServerGUI::new);
    }
}
