package messages;

import java.io.Serializable;

/**
 * messages
 *
 * @created by ASUS - StudentID : 18120449
 * @Date 6/18/2021 - 10:28 PM
 * @Description
 */
public class CommonFile implements Serializable {
    private int id;
    private String name;
    private byte[] data;
    private String fileExtension;


    public CommonFile(int id, String name, byte[] data, String fileExtension) {
        this.id = id;
        this.name = name;
        this.data = data;
        this.fileExtension = fileExtension;
    }


}
