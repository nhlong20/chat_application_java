package server;

import server.utils.ServerLog;
import server.views.ServerGUI;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * server
 *
 * @created by ASUS - StudentID : 18120449
 * @Date 6/18/2021 - 6:25 PM
 * @Description
 */
public class FileTransferServer extends Thread {
    private final int serverPort;
    private ServerSocket serverFileSocket;
    private final String fileDir;
    private List<FileTranferWorker> fileTranferWorkers = new ArrayList<>();

    public FileTransferServer(int mediaServerPort, String mediaServerDir) {
        this.serverPort = mediaServerPort;
        this.fileDir = mediaServerDir;
    }

    @Override
    public void run() {
        try {
            serverFileSocket  = new ServerSocket(serverPort);
            ServerGUI.serverLog.log(ServerLog.INFO, "File Transfer Server is listening on port " + serverPort);
            while(!serverFileSocket.isClosed()){
                Socket cSocket = serverFileSocket.accept();
                FileTranferWorker fileTranferWorker = new FileTranferWorker(this, cSocket, fileDir);
                fileTranferWorker.start();
            }
        } catch (SocketException e) {
        } catch (Exception e){
            ServerGUI.serverLog.log(ServerLog.ERROR, "File Server: " + e.getMessage());
        }
    }
    public void stopServer(){
        try {
            serverFileSocket.close();
        } catch (Exception ex){
            ServerGUI.serverLog.log(ServerLog.ERROR, "File Server: " + ex.getMessage());
        }
    }

}