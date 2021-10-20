package client.views.listmaganer;

import client.views.clientGUI.ListItemRenderer;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * client.views.listmaganer
 *
 * @created by ASUS - StudentID : 18120449
 * @Date 6/16/2021 - 3:04 PM
 * @Description
 */
public class UserListManager {
    private JList mList;
    private List<String> userList = new ArrayList<>();
    DefaultListModel<String> mModel;

    public UserListManager(JList uList) {
        this.mList = uList;
        this.mModel = new DefaultListModel<>();
        this.mList.setModel(mModel);
        mList.setCellRenderer(new ListItemRenderer());
        mList.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                mList.repaint();
            }
            public void mouseExited(MouseEvent e) {
                mList.repaint();
            }
        });
    }

    public void addElement(String username) {
        mModel.addElement(username);
    }
    public void removeElement(String username){
        mModel.removeElement(username);
    }
}
