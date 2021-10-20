package server.utils;

import javax.swing.*;
import java.util.Date;

/**
 * server.utils
 *
 * @created by ASUS - StudentID : 18120449
 * @Date 6/19/2021 - 10:34 AM
 * @Description
 */
public class ServerLog {
    private JTextArea logArea;
    public final static String INFO = "Info";
    public final static String WARNING = "Warning";
    public final static String ERROR = "Error";

    public ServerLog(JTextArea logArea){
        this.logArea = logArea;
        logArea.setLineWrap(true);
    }

    public void log(String type, String message){
        String log = "["+new Date()+"] ["+type+"] "+ message +"\n";
        logArea.append(log);
    }
}
