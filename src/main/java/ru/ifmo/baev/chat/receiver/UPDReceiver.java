package ru.ifmo.baev.chat.receiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ifmo.baev.chat.data.ByteArrayWrapper;
import ru.ifmo.baev.chat.data.ChatData;
import ru.ifmo.baev.chat.utils.Common;
import ru.ifmo.baev.chat.message.UDPMessage;
import ru.ifmo.baev.chat.sender.TCPSender;
import ru.ifmo.baev.chat.utils.Config;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.Arrays;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.02.14
 */
public class UPDReceiver implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(UPDReceiver.class);

    private final ChatData chatData;

    public UPDReceiver(ChatData chatData) {
        this.chatData = chatData;
    }

    public void run() {
        LOGGER.info("UDPReceiver started");
        byte[] receivedMessage = new byte[Config.UDP_MESSAGE_LENGTH];

        MulticastSocket receiverSocket;
        try {
            receiverSocket = new MulticastSocket(Config.UDP_PORT);
        } catch (IOException e) {
            LOGGER.error("MulticastSocket initialization failed", e);
            return;
        }

        while (true) {
            try {
                DatagramPacket receivedPacket = new DatagramPacket(receivedMessage, receivedMessage.length);
                receiverSocket.receive(receivedPacket);

                UDPMessage udpMessage = UDPMessage.fromBytes(receivedMessage);
                if (Arrays.equals(Common.orderBytes(Common.getMac()), udpMessage.getMac())) {
                    continue;
                }

                LOGGER.info("UDPReceived: " + udpMessage.toString());

                long receivedTimeOffset = System.currentTimeMillis() - udpMessage.getTime();

                ByteArrayWrapper mac = new ByteArrayWrapper(udpMessage.getMac());

                if (shouldSendSomethingTo(mac)) {
                    new Thread(new TCPSender(
                            udpMessage.getMac(),
                            receivedPacket.getAddress(),
                            receivedTimeOffset,
                            chatData)
                    ).start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean shouldSendSomethingTo(ByteArrayWrapper mac) {
        return !chatData.getCountMessagesMap().containsKey(mac) ||
                chatData.getCountMessagesMap().get(mac) < chatData.getTcpMessages().size();
    }
}
