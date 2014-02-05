package ru.ifmo.baev.chat.receiver;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ifmo.baev.chat.data.ChatData;
import ru.ifmo.baev.chat.message.TCPMessage;
import ru.ifmo.baev.chat.utils.Config;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.02.14
 */
public class TCPReceiver implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(TCPReceiver.class);

    private final ServerSocket mySocket;

    private final ChatData chatData;

    public TCPReceiver(ChatData chatData)
            throws IOException {

        this.chatData = chatData;
        this.mySocket = new ServerSocket(Config.TCP_PORT);
    }

    public void run() {
        LOGGER.info("TCPReceiver started");
        while (true) {
            try {
                Socket socket = mySocket.accept();
                byte[] bytes = IOUtils.toByteArray(socket.getInputStream());

                TCPMessage tcpMessage = TCPMessage.fromBytes(bytes);
                LOGGER.info("TCPReceived: " + tcpMessage.toString());

                synchronized (chatData.getChatHistory()) {
                    chatData.getChatHistory().add(tcpMessage);
                    chatData.getChatHistory().notifyAll();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
