package ru.ifmo.baev.chat.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ifmo.baev.chat.data.ChatData;
import ru.ifmo.baev.chat.message.TCPMessage;

import java.util.Iterator;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.02.14
 */
public class ChatGUIProcessor implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatGUIProcessor.class);

    private static final int MESSAGES_COUNT = 50;

    private final ChatData chatData;

    public ChatGUIProcessor(ChatData chatData) {
        this.chatData = chatData;
    }

    @Override
    public void run() {
        ChatGUI chatGUI = ChatGUI.createChatGUI(chatData);

        while (true) {
            synchronized (chatData.getChatHistory()) {
                chatGUI.getChatArea().setText(getLastMessages(MESSAGES_COUNT));

                try {
                    chatData.getChatHistory().wait(2000);
                } catch (InterruptedException e) {
                    LOGGER.error("Wait error", e);
                }
            }
        }
    }

    private String getLastMessages(int count) {
        StringBuilder sb = new StringBuilder();
        Iterator<TCPMessage> iterator = chatData.getChatHistory().descendingIterator();

        for (int i = 0; i < 50 && iterator.hasNext(); i++) {
            sb.append(iterator.next().toString()).append('\n');
        }
        return sb.toString();
    }
}
