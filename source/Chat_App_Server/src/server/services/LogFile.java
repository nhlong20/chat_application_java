package server.services;

import java.io.*;
import java.util.Date;

/**
 * server.services
 *
 * @created by ASUS - StudentID : 18120449
 * @Date 6/14/2021 - 11:20 AM
 * @Description
 */
public class LogFile {
    private final Writer out;

    public LogFile(File f) throws IOException {
        FileWriter fw = new FileWriter(f);
        this.out = new BufferedWriter(fw);
    }
    public synchronized void writeEntry(String message) throws IOException {
        Date d = new Date();
        out.write("[" + d.toString()+"]");
        out.write('\t');
        out.write(message);
        out.write("\r\n");
    }
    public void close() throws IOException {
        out.flush();
        out.close();
    }
}
