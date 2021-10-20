package client.views;

import client.Client;
import client.listeners.IMessageListener;
import client.utils.EmojiUtil;
import client.views.listmaganer.MessageListManager;
import client.config.Config;
import messages.MessageData;
import messages.MessageType;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * client.views
 *
 * @created by ASUS - StudentID : 18120449
 * @Date 6/13/2021 - 1:23 PM
 * @Description
 */

enum EnterMode {
    SEND_MESSAGE,
    FEED_LINE
}

public class ChatBoxGUI extends JFrame implements IMessageListener {
    private JButton sendBtn;
    private JPanel mainPanel;
    private JButton fileTranferBtn;
    private JLabel receiverLabel;
    private JComboBox enterModeComboBox;
    private JTextArea messgeEnterArea;
    private JList chatTextList;
    private JPanel emoPanel;
    private String receiver;
    private EnterMode enterMode;
    private MessageListManager messageListManager;
    private JFileChooser fileChooser;
    private Client client;

    public ChatBoxGUI(String receiver) {
        this.receiver = receiver;
        System.out.println("Receiver: " + receiver);
        this.initComponents();
        client = Client.getInstance();
        client.addMessageListener(this);
        this.addEventListener();
        this.messageListManager = new MessageListManager(this, chatTextList, Client.currentAccount);
        this.pack();
        // this following method must call after pack() method to set Java App Window to center of your computer screen
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void initComponents() {
        this.setTitle("Chat Application - " + receiver);
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fileTranferBtn.setIcon(getIcon("file_icon.png"));
        sendBtn.setIcon(getIcon("send_icon.png"));
        fileChooser = new JFileChooser();
        receiverLabel.setText(receiver);
        enterMode = EnterMode.SEND_MESSAGE;
        chatTextList.setBackground(new Color(14, 22, 33));
        mainPanel.setBackground(new Color(23, 33, 43));

        ArrayList<String> emojiNames = EmojiUtil.getAllEmojiNames();
        for(String emojiName : emojiNames){
            JButton button = new JButton(EmojiUtil.getIcon(emojiName));
            button.setPreferredSize(new Dimension(32, 32));
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setBackground(null);
            button.addActionListener(e -> onEmoji(emojiName));
            emoPanel.add(button);
        }
    }

    private void onEmoji(String emojiName) {
        new Thread(() -> {
            try {
                MessageData msg = new MessageData(MessageType.ON_EMOJI, emojiName, Client.currentAccount, receiver);
                client.outStream.writeObject(msg);
                messgeEnterArea.setText("");
                messageListManager.addElement(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private ImageIcon getIcon(String filename) {
        ImageIcon icon = null;
        try {
            File file = new File("resources/" +filename);
            if(file == null) return null;
            BufferedImage image = ImageIO.read(file);
            icon = new ImageIcon(image.getScaledInstance(24, 24, image.SCALE_SMOOTH));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return icon;
    }

    private void addEventListener() {
        sendBtn.addActionListener(e -> onSend());
        fileTranferBtn.addActionListener(e -> onUpload());
        enterModeComboBox.addActionListener(e -> {
            int index = enterModeComboBox.getSelectedIndex();
            if (index == 0) enterMode = EnterMode.SEND_MESSAGE;
            else enterMode = EnterMode.FEED_LINE;
        });
        messgeEnterArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (enterMode == EnterMode.SEND_MESSAGE) onSend();
                }
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        // call onCancel() on ESCAPE
        mainPanel.registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onUpload() {
        fileChooser.setDialogTitle("Chọn File Tải Lên");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = fileChooser.getSelectedFile();
        System.out.println("uploading " + file.getPath());

        try {
            FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
            Socket cFileSocket = new Socket(Config.MEDIA_SERVER_IP, Config.MEDIA_SERVER_PORT);
            DataOutputStream dataOutputStream = new DataOutputStream(cFileSocket.getOutputStream());

            // Prepare data
            byte[] fileNameBytes = file.getName().getBytes();
            byte[] fileDataBytes = new byte[(int) file.length()];
            byte[] commandTypeBytes = MessageType.UPLOAD_FILE.getBytes();

            fileInputStream.read(fileDataBytes);

            // Send the command first
            dataOutputStream.writeInt(commandTypeBytes.length);
            dataOutputStream.write(commandTypeBytes);

            // Send file name after
            dataOutputStream.writeInt(fileNameBytes.length);
            dataOutputStream.write(fileNameBytes);

            // Send file content next
            dataOutputStream.writeInt(fileDataBytes.length);
            dataOutputStream.write(fileDataBytes);

            // display file to download
            String message = file.getName();
            MessageData msg = new MessageData(MessageType.ON_FILE, message, Client.currentAccount, receiver);
            client.outStream.writeObject(msg);
            messageListManager.addElement(msg);
            cFileSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDownload(String filename) {
        boolean isDownloaded = false;
        fileChooser.setDialogTitle("Chọn thư mục lưu file");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File fileDir = fileChooser.getSelectedFile();

        String desDir = fileDir.getAbsolutePath() + "/" + filename;
        System.out.println("downloading " + filename);
        System.out.println("dest " + desDir);
        try {
            // Send download request
            Socket cFileSocket = new Socket(Config.MEDIA_SERVER_IP, Config.MEDIA_SERVER_PORT);
            DataOutputStream outStream = new DataOutputStream(cFileSocket.getOutputStream());
            DataInputStream inStream = new DataInputStream(cFileSocket.getInputStream());

            // Prepare data
            byte[] fileNameBytes = filename.getBytes();
            byte[] commandTypeBytes = MessageType.DOWNLOAD_FILE.getBytes();

            // Send the command first
            outStream.writeInt(commandTypeBytes.length);
            outStream.write(commandTypeBytes);

            // Send file name after
            outStream.writeInt(fileNameBytes.length);
            outStream.write(fileNameBytes);

            // Handle download response
            int fileDataLength = inStream.readInt();
            if (fileDataLength > 0) {
                byte[] fileDataBytes = new byte[fileDataLength];
                inStream.readFully(fileDataBytes, 0, fileDataLength);
                File downloadFile = new File(desDir);

                // Save file to server
                FileOutputStream fileOS = new FileOutputStream(downloadFile);
                fileOS.write(fileDataBytes);
                fileOS.close();
            }
            inStream.close();
            outStream.close();
            cFileSocket.close();
            isDownloaded = true;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (isDownloaded) {
            JOptionPane.showMessageDialog(null, "Đã tải xuống File thành công (" + desDir + ")", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Đã xảu ra lỗi trong quá trình tải file", "Tải file thất bại", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onSend() {
        new Thread(() -> {
            try {
                String message = messgeEnterArea.getText();
                MessageData msg = new MessageData(MessageType.ON_MESSAGE, message, Client.currentAccount, receiver);
                client.outStream.writeObject(msg);
                messgeEnterArea.setText("");
                messageListManager.addElement(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    @Override
    public void onReceivedMessage(MessageData msg) {
        // Update gui
        if (!receiver.equals(msg.getSender())) return;
        messageListManager.addElement(msg);
    }
}
