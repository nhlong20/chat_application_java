package client;

import client.views.ChatBoxGUI;
import client.views.clientGUI.ClientGUI;
import client.views.LoginGUI;

import javax.swing.*;

/**
 * client
 *
 * @created by ASUS - StudentID : 18120449
 * @Date 6/13/2021 - 5:15 PM
 * @Description
 */
public class Main {
    public enum ViewControl {
        LOGIN,
        CLIENT,
        CHAT_BOX
    }
    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
        }

        invokeGUI(ViewControl.LOGIN, null);

    }

    public static void invokeGUI(ViewControl view, String username) {
        switch (view) {
            case LOGIN: {
                SwingUtilities.invokeLater(LoginGUI::new);
                break;
            }
            case CLIENT: {
                new ClientGUI(username); // Sender username
                break;
            }
            case CHAT_BOX: {
                SwingUtilities.invokeLater(() ->
                        new ChatBoxGUI(username) // Receiver username
                );
                break;
            }

        }
    }
}
