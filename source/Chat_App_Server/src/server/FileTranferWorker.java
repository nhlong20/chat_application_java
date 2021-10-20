package server;

import messages.MessageType;

import java.io.*;
import java.net.Socket;

/**
 * server
 *
 * @created by ASUS - StudentID : 18120449
 * @Date 6/14/2021 - 1:38 PM
 * @Description
 */
public class FileTranferWorker extends Thread {
    private FileTransferServer server;
    private Socket cFileSocket;
    private String fileDir;
    private DataInputStream inStream;
    private DataOutputStream outputStream;

    public FileTranferWorker(FileTransferServer server, Socket cSocket, String fileDir) {
        this.server = server;
        this.cFileSocket = cSocket;
        this.fileDir = fileDir;
    }

    @Override
    public void run() {
        try {
            inStream = new DataInputStream(cFileSocket.getInputStream());
            outputStream = new DataOutputStream(cFileSocket.getOutputStream());

            int commandTypeLength = inStream.readInt();
            if (commandTypeLength > 0) {
                byte[] commandTypeBytes = new byte[commandTypeLength];
                inStream.readFully(commandTypeBytes, 0, commandTypeLength);
                String type = new String(commandTypeBytes);
                switch (type) {
                    case MessageType.UPLOAD_FILE: {
                        handleUploadFile();
                        break;
                    }
                    case MessageType.DOWNLOAD_FILE: {
                        handleDownloadFile();
                        break;
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleDownloadFile() throws IOException {
        int fileNameLength = inStream.readInt();
        if (fileNameLength > 0) {
            byte[] fileNameBytes = new byte[fileNameLength];
            inStream.readFully(fileNameBytes, 0, fileNameLength);
            String fileName = new String(fileNameBytes);

            // Response download request
            File file = new File(fileDir + fileName);
            if (file == null) return;
            FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
            DataOutputStream dataOutputStream = new DataOutputStream(cFileSocket.getOutputStream());

            // Prepare data
            byte[] fileDataBytes = new byte[(int) file.length()];
            fileInputStream.read(fileDataBytes);

            // Send file content next
            dataOutputStream.writeInt(fileDataBytes.length);
            dataOutputStream.write(fileDataBytes);
        }
    }

    private void handleUploadFile() throws IOException {
        int fileNameLength = inStream.readInt();
        if (fileNameLength > 0) {
            byte[] fileNameBytes = new byte[fileNameLength];
            inStream.readFully(fileNameBytes, 0, fileNameLength);
            String fileName = new String(fileNameBytes);
            int fileDataLength = inStream.readInt();

            if (fileDataLength > 0) {
                byte[] fileDataBytes = new byte[fileDataLength];
                inStream.readFully(fileDataBytes, 0, fileDataLength);
                File uploadDir = new File(fileDir);
                File uploadFile = null;
                if(!uploadDir.exists()){
                    if(uploadDir.mkdir()){
                        uploadFile = new File(fileDir + fileName);
                    }
                }
                // Save file to server
                FileOutputStream fileOS = new FileOutputStream(uploadFile);
                fileOS.write(fileDataBytes);
                fileOS.close();
            }
        }
    }
}
