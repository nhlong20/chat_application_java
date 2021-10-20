package client.views.dialogs;

import client.Client;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RegisterDlg extends JDialog {
    private JPanel contentPane;
    private JButton registerBtn;
    private JButton cancelBtn;
    private JTextField usernameTF;
    private JPasswordField passwordF;
    private JPasswordField confirmPasswordF;

    public RegisterDlg() {
        this.setTitle("Chat Application - Đăng ký tài khoản");
        setContentPane(contentPane);
        setModal(true);
        this.addEventListener();

        this.setModal(true);
        this.pack();
        // this following method must call after pack() method to set Java App Window to center of your computer screen
        this.setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addEventListener() {
        registerBtn.addActionListener(e -> onRegist());
        cancelBtn.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onRegist() {
        String username = usernameTF.getText();
        String password = String.valueOf(passwordF.getPassword());
        String confirmPassword = String.valueOf(confirmPasswordF.getPassword());
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Bạn đã nhập sai mật khẩu xác nhận, vui lòng kiểm tra lại", "Lỗi đăng ký", JOptionPane.ERROR_MESSAGE);
            return;
        }
        new Thread(() ->{
            Client client = Client.getInstance();
            client.connectServer();
            client.sendRegisterRequest(username,password);
        }).start();
        dispose();

    }


    private void onCancel() {
        dispose();
    }
}
