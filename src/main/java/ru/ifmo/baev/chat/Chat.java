package ru.ifmo.baev.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ifmo.baev.chat.data.ChatData;
import ru.ifmo.baev.chat.gui.ChatGUIProcessor;
import ru.ifmo.baev.chat.receiver.TCPReceiver;
import ru.ifmo.baev.chat.receiver.UPDReceiver;
import ru.ifmo.baev.chat.sender.UDPSender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 07.11.13
 */
public class Chat {

    private static final Logger LOGGER = LoggerFactory.getLogger(Chat.class);

    private final ChatData chatData;

    private List<Thread> children;

    public Chat() throws IOException {
        this.chatData = new ChatData();
        this.children = new ArrayList<>();

        this.children.add(new Thread(new UDPSender()));
        this.children.add(new Thread(new UPDReceiver(chatData)));
        this.children.add(new Thread(new TCPReceiver(chatData)));
        this.children.add(new Thread(new ChatGUIProcessor(chatData)));
    }

    public void run() {
        LOGGER.info("Starting...");

        for (Thread thread : children) {
            thread.start();
        }

        LOGGER.info("finished...");
    }


    public static void main(String[] args) throws IOException {
        new Chat().run();
    }

}
