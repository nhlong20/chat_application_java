package client.views.clientGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * client.views.clientGUI
 *
 * @created by ASUS - StudentID : 18120449
 * @Date 6/16/2021 - 3:14 PM
 * @Description
 */
public class ListItemRenderer extends JLabel implements ListCellRenderer<String> {
    @Override
    public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
        String backgroundColor = cellHasFocus ? "#90b1f0" : "white";
        setText("<html><div style='padding: 2px 16px; background: " + backgroundColor +";'><h3>" + value + "</h3></div></html>");
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.lightGray));
        return this;
    }
}