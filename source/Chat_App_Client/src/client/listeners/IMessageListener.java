package client.listeners;

import messages.MessageData;

/**
 * client.listeners
 *
 * @created by ASUS - StudentID : 18120449
 * @Date 6/18/2021 - 12:25 AM
 * @Description
 */
public interface IMessageListener {
    void onReceivedMessage(MessageData messageData);
}
