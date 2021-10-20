package client.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

/**
 * client.utils
 *
 * @created by ASUS - StudentID : 18120449
 * @Date 6/19/2021 - 5:20 PM
 * @Description
 */
public class EmojiUtil {
    public static  ArrayList<String> getAllEmojiNames(){
        ArrayList<String> emojiNames = new ArrayList<>();
        emojiNames.add("emo_love");
        emojiNames.add("emo_sad");
        emojiNames.add("emo_shocked");
        emojiNames.add("emo_smile");
        emojiNames.add("emo_smiling");
        emojiNames.add("emo_wink");
        return emojiNames;
    }
    public static ImageIcon getIcon(String name) {
        ImageIcon icon = null;
        String ext = ".png";
        try {
//            URL url =  EmojiUtil.class.getResource( name +".png");
            File file = new File("resources/" + name + ext);
            if(file == null) return null;
            BufferedImage image = ImageIO.read(file);
            icon = new ImageIcon(image.getScaledInstance(24, 24, image.SCALE_SMOOTH));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return icon;
    }

}
