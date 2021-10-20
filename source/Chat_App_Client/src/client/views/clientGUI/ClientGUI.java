package client.views.clientGUI;

import client.Client;
import client.Main;
import client.listeners.IUserStatusListener;
import client.views.listmaganer.UserListManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * client.views
 *
 * @created by ASUS - StudentID : 18120449
 * @Date 6/15/2021 - 9:26 PM
 * @Description
 */


public class ClientGUI extends JFrame implements IUserStatusListener {
    private JPanel clientPanel;
    private JList userList;
    private JList groupList;
    private JLabel usernameLabel;
    private JButton logoutBtn;
    private JButton changePasswordBtn;
    private JLabel serverPortLabel;
    private JLabel serverIpLabel;
    private String currentAccount;
    private UserListManager userListManager;
    private Client client;
    public ClientGUI(String username) {
        currentAccount = username;
        this.initComponents();
        client.addUserStatusListener(this);
        this.addEventListener();
        Client.setCurrentFrame(this);
        this.pack();
        // this following method must call after pack() method to set Java App Window to center of your computer screen
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void initComponents() {
        this.setTitle("Chat Application");
        this.client = Client.getInstance();
        this.setContentPane(clientPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        logoutBtn.setIcon(getIcon("logout_icon.png"));
        userListManager = new UserListManager(userList);
        usernameLabel.setText(currentAccount);

        serverIpLabel.setText(client.getServerIP());
        serverPortLabel.setText(String.valueOf(client.getServerPort()));

    }

    private ImageIcon getIcon(String filename) {
        ImageIcon icon = null;
        try {
            File file = new File("resources/" +filename);
            if(file == null) return null;
            BufferedImage image = ImageIO.read(file);
            icon = new ImageIcon(image.getScaledInstance(20, 20, image.SCALE_SMOOTH));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return icon;
    }

    private void addEventListener() {
        logoutBtn.addActionListener(e -> onLogout());
        userList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String receiver = String.valueOf(userList.getSelectedValue());
                    openChatBox(receiver);
                }
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onLogout();
            }
        });
        // call onCancel() on ESCAPE
        clientPanel.registerKeyboardAction(e -> onLogout(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onLogout() {
        Client client = Client.getInstance();
        try {
            if(client.disconnectServer()){
                Main.invokeGUI(Main.ViewControl.LOGIN, null);
            } else {
                JOptionPane.showMessageDialog(null, "Có lỗi xảy ra trong quá trình đăng xuất", "Lỗi đăng xuất", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void openChatBox(String receiver) {
        Main.invokeGUI(Main.ViewControl.CHAT_BOX, receiver);
    }

    @Override
    public void update(int status, String username) {
        String statusS = status == 1 ? "online" : "offline";
        System.out.println(username + " is " + statusS);

        if (status == 1) userListManager.addElement(username);
        else userListManager.removeElement(username);
    }
}
