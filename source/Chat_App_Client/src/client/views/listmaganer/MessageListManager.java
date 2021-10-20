package client.views.listmaganer;

import client.utils.EmojiUtil;
import client.views.ChatBoxGUI;
import messages.MessageData;
import messages.MessageType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * client.views.listmaganer
 *
 * @created by ASUS - StudentID : 18120449
 * @Date 6/18/2021 - 3:37 PM
 * @Description
 */
class MessageListItemRenderer extends JLabel implements ListCellRenderer<MessageData> {
    private String currentUser;

    public MessageListItemRenderer(String currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends MessageData> list, MessageData msg, int index, boolean isSelected, boolean cellHasFocus) {
        String sender = msg.getSender();
        String type = msg.getType();
        String message = msg.getData();

        String backgroundColor;
        String textColor;
        String textColor2;
        String displayName;

        // Config color of sender and receiver
        if (sender.equals(this.currentUser)) {
            backgroundColor = "#2B5278";
            textColor = "#FFFFFF";
            textColor2 = "#9b9b9b";
            displayName = "<b>You</b>";
        } else {
            backgroundColor = "#182533";
            textColor = "#FFFFFF";
            textColor2 = "#B5C7FF";
            displayName = "<b>" + sender + "</b>";
        }

        if (type.equals(MessageType.ON_MESSAGE)) {
            setIcon(null);
            message = message.replaceAll("(\r\n|\n)", "<br/>");
            setText("<html>" +
                    "<div style='background-color: " + backgroundColor + "; color: " + textColor + "; padding: 8px 16px; border-radius: 50%'>" +
                    "<div>" + displayName + "</div>" + message +
                    "</div>" +
                    "</html>");
            setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        } else if (type.equals(MessageType.ON_FILE)) {
            setIcon(null);
            setText("<html>" +
                    "<div style='background-color: " + backgroundColor + "; color: " + textColor + "; padding: 8px 16px;'>" +
                    "<div style='color: " + textColor2 + "; margin-bottom: 4px'>Nháy đúp để tải về</div>" +
                    "<div>" + message + "</div>" +
                    "</div>" +
                    "</html>");
            setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        } else if (type.equals((MessageType.ON_EMOJI))){
            setIcon(EmojiUtil.getIcon(message));
            setText(null);
            setBorder(BorderFactory.createEmptyBorder(12, 16, 20, 16));
        }

        return this;
    }
}

public class MessageListManager {
    private JList mList;
    private ChatBoxGUI mChatBox;
    DefaultListModel<MessageData> mModel;

    public MessageListManager(ChatBoxGUI chatBoxGUI, JList uList, String currentUser) {
        this.mList = uList;
        this.mChatBox = chatBoxGUI;
        this.mModel = new DefaultListModel<>();
        this.mList.setModel(mModel);
        mList.setCellRenderer(new MessageListItemRenderer(currentUser));
        this.addEventListener();

    }

    private void addEventListener() {
        mList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    MessageData msgItem = mModel.getElementAt(mList.getSelectedIndex());
                    if (!msgItem.getType().equals(MessageType.ON_FILE)) return;

                    String fileName = msgItem.getData();
                    mChatBox.onDownload(fileName);

                }
            }
        });
    }

    public void addElement(MessageData message) {
        mModel.addElement(message);
    }

    public void removeElement(MessageData message) {
        mModel.removeElement(message);
    }

}
