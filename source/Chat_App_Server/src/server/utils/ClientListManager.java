package server.utils;

import messages.MessageData;

import javax.swing.*;

/**
 * server.utils
 *
 * @created by ASUS - StudentID : 18120449
 * @Date 6/19/2021 - 11:12 AM
 * @Description
 */
public class ClientListManager {
    JList<String> usernameList;
    DefaultListModel<String> mModel;

    public ClientListManager(JList usernameList){
        this.usernameList = usernameList;
        mModel = new DefaultListModel<>();
        usernameList.setModel(mModel);
    }
    public void addElement(String username){
        mModel.addElement(username);
    }
    public void removeElement(String username){
        mModel.removeElement(username);
    }
    public void removeAll(){
        mModel.removeAllElements();
    }

}
