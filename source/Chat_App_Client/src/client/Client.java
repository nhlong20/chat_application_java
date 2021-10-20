package client;

import client.listeners.IMessageListener;
import client.listeners.IUserStatusListener;
import client.utils.ResponseHandler;
import client.config.Config;
import messages.MessageData;
import messages.MessageType;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Client
 *
 * @created by ASUS - StudentID : 18120449
 * @Date 6/13/2021 - 10:59 AM
 * @Description
 */
public class Client {
    private static Client instance;
    public String serverIP;
    public int serverPort;
    public ObjectOutputStream outStream;
    public ObjectInputStream inStream;
    public Socket socket;
    public static JFrame currentFrame;
    public static String currentAccount;
    private ResponseHandler responseHandler;
    public List<IUserStatusListener> userStatusListeners = new ArrayList<>();
    public ArrayList<IMessageListener> messageListeners = new ArrayList<>();

    public Client() {
        this.serverIP = Config.CHAT_SERVER_IP;
        this.serverPort = Config.CHAT_SERVER_PORT;
    }
    public Client(String serverIP, int serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }
    public static Client getInstance(){
        if(instance == null){
            synchronized(Client.class){
                if(instance == null){
                    instance = new Client();
                }
            }
        }
        return instance;
    }

    public static Client getInstance(String serverIP, int serverPort){
        if(instance == null){
            synchronized(Client.class){
                if(instance == null){
                    instance = new Client(serverIP,serverPort);
                }
            }
        }
        return instance;
    }
    public String getServerIP(){
        return serverIP;
    }
    public int getServerPort(){
        return serverPort;
    }
    public static void setCurrentFrame(JFrame frame) {
        if (currentFrame != null) currentFrame.dispose();
        currentFrame = frame;
    }

    public  void addUserStatusListener(IUserStatusListener userStatusListener) {
        userStatusListeners.add(userStatusListener);
    }

    public  void addMessageListener(IMessageListener messageListener) {
        messageListeners.add(messageListener);
    }

    public boolean connectServer() {

        if (socket != null && !socket.isClosed()) {
            return true;
        }
        try {
            socket = new Socket(serverIP, serverPort);
            System.out.println("Connected to server on port " + socket.getPort());
            socket.setSoTimeout(Config.TIMEOUT);
            // Send data to server by output stream
            outStream = new ObjectOutputStream(socket.getOutputStream());
            // Get server response by input stream
            inStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            return false;
        }
        responseHandler = new ResponseHandler(this);
        responseHandler.start();
        return true;
    }
    public boolean disconnectServer() throws IOException {
        if (socket.isClosed()) return true;
        try {
            MessageData msg = new MessageData(MessageType.LOGOUT);
            outStream.writeObject(msg);
        }catch (IOException ex) {
            return false;
        }
        instance = null;
        currentAccount = null;
        inStream.close();
        outStream.close();
        responseHandler.interrupt();
        socket.close();
        return true;
    }

    public void sendLoginRequest(String username, String password) {
        try {
            String data = username + " " + password;
            // Send login message to server
            MessageData msg = new MessageData(MessageType.LOGIN, data, null, null);
            outStream.writeObject(msg);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void sendRegisterRequest(String username, String password) {
        try {
            String data = username + " " + password;
            // Send reg message to server
            MessageData msg = new MessageData(MessageType.REGISTER, data, null, null);
            outStream.writeObject(msg);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
