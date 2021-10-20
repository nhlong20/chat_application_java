package server.views;

import server.ChatServer;
import server.FileTransferServer;
import server.config.ServerConfig;
import server.utils.ClientListManager;
import server.utils.ServerLog;

import javax.swing.*;

/**
 * server.views
 *
 * @created by ASUS - StudentID : 18120449
 * @Date 6/19/2021 - 1:40 AM
 * @Description
 */
public class ServerGUI extends JFrame{
    private JTextField serverIpTF;
    private JTextField portTF;
    private JButton startServerBtn;
    private JTextArea serverLogTextArea;
    private JList clientList;
    public static ClientListManager clientListManager;
    private JPanel mainPanel;
    private JButton stopServerBtn;
    private JLabel chatServerStatus;
    private JTextField filePort;
    public static ServerLog serverLog;
    private boolean serverStarted;
    private ChatServer chatServer;
    private FileTransferServer fileTransferServer;

    public ServerGUI() {
        this.setTitle("Chat Application - Server");
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addEventListener();
        this.initComponents();
        this.pack();
        // this following method must call after pack() method to set Java App Window to center of your computer screen
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    private void initComponents() {
        serverIpTF.setText(ServerConfig.CHAT_SERVER_IP);
        portTF.setText(String.valueOf(ServerConfig.CHAT_SERVER_PORT));
        serverLog = new ServerLog(serverLogTextArea);
        clientListManager = new ClientListManager(clientList);
        filePort.setText(String.valueOf(ServerConfig.MEDIA_SERVER_PORT));
        this.serverStarted = false;
        chatServerStatus.setText("Status: Server đang ngắt kết nối");
        stopServerBtn.setEnabled(false);

    }
    private void addEventListener(){
        startServerBtn.addActionListener(e -> onStart());
        stopServerBtn.addActionListener(e->onStop());
    }

    private void onStop() {
        if(!serverStarted) return;
        String message = "Bạn có muốn dừng server lại";
        int answer = JOptionPane.showConfirmDialog(new JFrame(), message);
        if (answer == JOptionPane.YES_OPTION) {
            // User clicked YES.
            ServerGUI.serverLog.log(ServerLog.INFO, "Chat Server và File Server đã ngắt kết nối");
            chatServer.stopServer();
            fileTransferServer.stopServer();
            clientListManager.removeAll();
            startServerBtn.setEnabled(true);
            stopServerBtn.setEnabled(false);
            chatServerStatus.setText("Status: Server đang ngắt kết nối");
        } else if (answer == JOptionPane.NO_OPTION) {
            return;
        }
    }


    private void onStart() {
        String port = portTF.getText();
        // Check invalid input
        if(port.length() == 0){
            errorHandler("Lỗi đăng nhập", "Port không được bỏ trống");
            return;
        }
        if(!ServerGUI.isNumeric(port)){
            errorHandler("Lỗi đăng nhập", "Port phải là số");
            return;
        }

        chatServer = new ChatServer(Integer.parseInt(port));
        chatServer.start();
        chatServerStatus.setText("Status: Chat server Ip is " + serverIpTF.getText() + " on port " + port);
        fileTransferServer = new FileTransferServer(ServerConfig.MEDIA_SERVER_PORT, ServerConfig.MEDIA_SERVER_DIR);
        fileTransferServer.start();
        serverStarted = true;
        startServerBtn.setEnabled(false);
        stopServerBtn.setEnabled(true);

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
    public void errorHandler(String type, String err_msg){
        JOptionPane.showMessageDialog(null, err_msg,
                type, JOptionPane.ERROR_MESSAGE);
    }
}
