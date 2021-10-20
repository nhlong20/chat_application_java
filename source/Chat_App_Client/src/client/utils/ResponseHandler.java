package client.utils;

import client.Client;
import client.Main;
import client.listeners.IMessageListener;
import client.listeners.IUserStatusListener;
import messages.MessageData;
import messages.MessageType;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * client.utils
 *
 * @created by ASUS - StudentID : 18120449
 * @Date 6/17/2021 - 4:46 PM
 * @Description
 */
public class ResponseHandler extends Thread {
    Client client;
    public ResponseHandler(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            MessageData msg = null;
            while ((msg = (MessageData) client.inStream.readObject()) != null) {
                String msgType = msg.getType();
                switch (msgType) {
                    case MessageType.REG_FAIL: {
                        JOptionPane.showMessageDialog(null, msg.getData(), "Lỗi đăng ký", JOptionPane.ERROR_MESSAGE);
                        break;
                    }
                    case MessageType.REG_SUCCESS: {
                        JOptionPane.showMessageDialog(null, "Đăng ký thành công", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                    case MessageType.LOGIN_FAIL: {
                        JOptionPane.showMessageDialog(null, msg.getData(),
                                "Đăng nhập thất bại", JOptionPane.ERROR_MESSAGE);
                        break;
                    }
                    case MessageType.LOGIN_SUCCESS: {
                        Client.currentAccount = msg.getData();
                        JOptionPane.showMessageDialog(null, "Đăng nhập thành công",
                                "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        Main.invokeGUI(Main.ViewControl.CLIENT, Client.currentAccount);
                        break;
                    }

                    case MessageType.USER_ONLINE: {
                        // Update online user List
                        for (IUserStatusListener userStatusListener : client.userStatusListeners) {
                            userStatusListener.update(1, msg.getData());
                        }
                        break;
                    }
                    case MessageType.USER_OFFLINE: {
                        // Update online user List
                        for (IUserStatusListener userStatusListener : client.userStatusListeners) {
                            userStatusListener.update(0, msg.getData());
                        }
                        break;
                    }
                    case MessageType.ON_EMOJI:
                    case MessageType.ON_MESSAGE:
                    case MessageType.ON_FILE: {
                        // Update online user List
                        for (IMessageListener messageListener : client.messageListeners) {
                            messageListener.onReceivedMessage(msg);
                        }
                        break;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            try {
                client.socket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
