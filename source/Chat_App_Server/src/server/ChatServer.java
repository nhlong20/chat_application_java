package server;

import server.utils.AccountManager;
import server.utils.ServerLog;
import server.views.ServerGUI;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * server
 *
 * @created by ASUS - StudentID : 18120449
 * @Date 6/13/2021 - 10:54 AM
 * @Description
 */
public class ChatServer extends Thread {
    private final int serverPort;
    private List<ChatWorker> chatWorkers = new ArrayList<>();
    public static final AccountManager accountManager = new AccountManager();
    private ServerSocket serverSocket;

    public ChatServer(int serverPort) {
        this.serverPort = serverPort;
    }

    public List<ChatWorker> getChatWorkers() {
        return chatWorkers;
    }
    public void removeChatWorker(ChatWorker chatWorker){
        chatWorkers.remove(chatWorker);
    }
    @Override
    public void run() {
        try  {
            serverSocket = new ServerSocket(serverPort);
            ServerGUI.serverLog.log(ServerLog.INFO, "Chat Server is listening on port " + serverPort);
            while (true) {
                Socket cSocket = serverSocket.accept();
                ChatWorker chatWorker = new ChatWorker(this, cSocket);
                ServerGUI.serverLog.log(ServerLog.INFO, "Client " + cSocket + " đã kết nối");
                chatWorkers.add(chatWorker);
                chatWorker.start();
            }
        } catch (SocketException e) {
        } catch (Exception e){
            ServerGUI.serverLog.log(ServerLog.ERROR, "File Server: " + e.getMessage());
        }
    }
    public void stopServer(){
        try {
            serverSocket.close();
        } catch (Exception ex){
            ServerGUI.serverLog.log(ServerLog.ERROR, ex.getMessage());
        }
    }
}
