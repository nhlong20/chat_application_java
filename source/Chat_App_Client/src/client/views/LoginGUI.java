package client.views;

import client.Client;
import client.utils.EmojiUtil;
import client.views.dialogs.RegisterDlg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * client.views
 *
 * @created by ASUS - StudentID : 18120449
 * @Date 6/13/2021 - 1:14 PM
 * @Description
 */
public class LoginGUI extends JFrame {
    private JTextField usernameTF;
    private JPasswordField passwordF;
    private JButton loginBtn;
    private JPanel loginPanel;
    private JButton registerBtn;
    private JTextField portTF;
    private JComboBox serverIpComboBox;

    public LoginGUI() {
        this.setTitle("Chat Application - Đăng nhập");
        this.setContentPane(loginPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Client.setCurrentFrame(this);
        this.addEventListener();
        this.initComponents();
        this.pack();
        // this following method must call after pack() method to set Java App Window to center of your computer screen
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void initComponents() {
        InetAddress ip;
        String localhost = "127.0.0.1";
        try {
            ip = InetAddress.getLocalHost();
            serverIpComboBox.addItem(ip.getHostAddress());
            serverIpComboBox.addItem(localhost);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        portTF.setText("3000");
    }

    private void addEventListener(){
        loginBtn.addActionListener(e -> onSubmit());
        registerBtn.addActionListener(e-> onRegister());
        passwordF.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode()==KeyEvent.VK_ENTER){
                    onSubmit();
                }
            }
        });
    }


    private String validateInput(String username, String password){
        if (username == null || username.trim().length() == 0 || password.trim().length() == 0) {
            return "Tài khoản hoặc mật khẩu không được bỏ trống";
        }
        return null;
    }
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    private void onSubmit(){
        new Thread(() ->{
            String serverIp = (String) serverIpComboBox.getSelectedItem();
            String port = portTF.getText();
            String username = usernameTF.getText();
            String password = String.valueOf(passwordF.getPassword());

            // Check invalid input
            if(port.length() == 0){
                errorHandler("Lỗi đăng nhập", "Port không được bỏ trống");
                return;
            }
            if(!LoginGUI.isNumeric(port)){
                errorHandler("Lỗi đăng nhập", "Port phải là số");
                return;
            }
            String error_check = validateInput(username,password);
            if(error_check != null){
                errorHandler("Lỗi đăng nhập", error_check);
                return;
            }
            // Establish connection to server
            Client client = Client.getInstance(serverIp, Integer.parseInt(port));
            if(!client.connectServer()){
                JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi trong quá trình kết nối", "Kết nối thất bại", JOptionPane.ERROR_MESSAGE);
                return;
            }
            client.sendLoginRequest(username,password);
        }).start();
    }
    private void onRegister() {
        RegisterDlg registerDlg = new RegisterDlg();
    }
    public void errorHandler(String type, String err_msg){
        JOptionPane.showMessageDialog(null, err_msg,
                type, JOptionPane.ERROR_MESSAGE);
    }
}
