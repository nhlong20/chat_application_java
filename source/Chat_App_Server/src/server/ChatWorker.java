package server;

import messages.MessageData;
import messages.MessageType;
import server.entities.Account;
import server.utils.ServerLog;
import server.views.ServerGUI;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

/**
 * server
 *
 * @created by ASUS - StudentID : 18120449
 * @Date 6/14/2021 - 1:38 PM
 * @Description
 */
public class ChatWorker extends Thread {
    private final ChatServer server;
    private final Socket cSocket;
    private Account account = null;
    private ObjectInputStream inStream;
    private ObjectOutputStream outStream;

    public ChatWorker(ChatServer server, Socket cSocket) {
        this.server = server;
        this.cSocket = cSocket;
    }

    @Override
    public void run() {
        try {
            inStream = new ObjectInputStream(cSocket.getInputStream());
            outStream = new ObjectOutputStream(cSocket.getOutputStream());
            MessageData request = null;
            while ((request = (MessageData) inStream.readObject()) != null) {
                String type = request.getType();

                switch (type) {
                    case MessageType.REGISTER: {
                        handleRegist(request.getData());
                        break;
                    }
                    case MessageType.LOGIN: {
                        handleLogin(request.getData());
                        break;
                    }
                    case MessageType.LOGOUT: {
                        handleLogout();
                        break;
                    }
                    case MessageType.ON_EMOJI:
                    case MessageType.ON_FILE:
                    case MessageType.ON_MESSAGE: {
                        handleMessage(request);
                        break;
                    }
                }
            }
            outStream.flush();
            inStream.close();
            cSocket.close();
            ServerGUI.serverLog.log(ServerLog.INFO, cSocket +" đã ngắt kết nối");
        } catch (IOException | ClassNotFoundException ex) {
            ServerGUI.serverLog.log(ServerLog.INFO, cSocket +" đã ngắt kết nối");
        }
    }

    public Account getAccount() {
        return account;
    }

    private void sendMessage(String type, String message, String sender, String receiver) {
        try {
            MessageData messageData = new MessageData(type, message, sender, receiver);
            outStream.writeObject(messageData);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            ServerGUI.serverLog.log(ServerLog.ERROR, " xảy ra lỗi trong quá trình gửi tin " + type + " " + message + " " + receiver);
        }
    }
    private void sendMessage(String type) {
        try {
            MessageData messageData = new MessageData(type);
            outStream.writeObject(messageData);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            ServerGUI.serverLog.log(ServerLog.ERROR, " xảy ra lỗi trong quá trình gửi tin" + type);
        }
    }

    private void handleRegist(String data) {
        // Beautify data and get account
        String[] tokens = data.split(" ");
        String username = tokens[0];
        String password = tokens[1];
        Account account = server.accountManager.getUser(username, password);
        if (account != null) {
            ServerGUI.serverLog.log(ServerLog.ERROR, username +" đăng ký thất bại");
            sendMessage(MessageType.REG_FAIL, "Tài khoản đã tồn tại", null,null);
            return;
        }
        server.accountManager.addUser(username,password);
        sendMessage(MessageType.REG_SUCCESS);
        ServerGUI.serverLog.log(ServerLog.INFO, username + " đăng ký thành công");

    }

    private void handleLogin(String data) {
        // Beautify data and get account
        String[] tokens = data.split(" ");
        String username = tokens[0];
        String password = tokens[1];
        Account account = server.accountManager.getUser(username, password);

        if (account == null) {
            sendMessage(MessageType.LOGIN_FAIL, "Bạn đã nhập sai tên đăng nhập hoặc mật khẩu", null,null);
            return;
        }

        this.account = account;
        // Send success login response to user
        sendMessage(MessageType.LOGIN_SUCCESS, username, null,null);
        ServerGUI.serverLog.log(ServerLog.INFO, username +" đăng nhập thành công");
        ServerGUI.clientListManager.addElement(username);
        List<ChatWorker> chatWokers = server.getChatWorkers();

        for (ChatWorker chatWorker : chatWokers) {
            String currentUsername = chatWorker.getAccount().getUsername();
            if (currentUsername.equals(this.account.getUsername())) continue;

            // [Broadcast] Send current user status
            chatWorker.sendMessage(MessageType.USER_ONLINE, account.getUsername(), null, chatWorker.getAccount().getUsername());

            // [Unicast] Send to current user others' status
            this.sendMessage(MessageType.USER_ONLINE, chatWorker.getAccount().getUsername(), null, null);
        }
    }

    private void handleLogout() throws IOException {
        cSocket.close();
        server.removeChatWorker(this);
        ServerGUI.clientListManager.removeElement(account.getUsername());

        List<ChatWorker> chatWorkers = server.getChatWorkers();
        for (ChatWorker chatWorker : chatWorkers) {
            if (chatWorker.cSocket == null || chatWorker.cSocket.isClosed()) continue;
            chatWorker.sendMessage(MessageType.USER_OFFLINE, account.getUsername(), null, null);
        }
        ServerGUI.serverLog.log(ServerLog.INFO, account.getUsername() +" đã đăng xuất");
    }

    private void handleMessage(MessageData msg) {
        List<ChatWorker> chatWokers = server.getChatWorkers();
        String receiver = msg.getReceiver();
        String sender = msg.getSender();
        String data = msg.getData();

        // Send message to right receiver
        for (ChatWorker chatWorker : chatWokers) {
            if (!chatWorker.getAccount().getUsername().equals(receiver)) continue;
            chatWorker.sendMessage(msg.getType(), data, sender, receiver);
        }

        // Log message to server
        ServerGUI.serverLog.log(ServerLog.INFO, sender + " gửi tới " + receiver + ": " + data);
    }


}
