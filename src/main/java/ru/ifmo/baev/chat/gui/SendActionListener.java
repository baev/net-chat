package ru.ifmo.baev.chat.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ifmo.baev.chat.data.ChatData;
import ru.ifmo.baev.chat.message.TCPMessage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.02.14
 */
public class SendActionListener implements ActionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendActionListener.class);

    private ChatData chatData;

    private ChatGUI chatGUI;

    public SendActionListener(ChatData chatData, ChatGUI chatGUI) {
        this.chatData = chatData;
        this.chatGUI = chatGUI;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String text = chatGUI.getMessageArea().getText().trim();

        if (text.isEmpty()) {
            return;
        }

        long time = System.currentTimeMillis();
        TCPMessage msg = new TCPMessage(chatData.getMac(), time, text);
        chatData.getTcpMessages().add(msg);
        synchronized (chatData.getChatHistory()) {
            chatData.getChatHistory().add(msg);
            LOGGER.info("New message: " + msg.toString());
            chatData.getChatHistory().notifyAll();
        }
        chatGUI.getMessageArea().setText("");
    }
}