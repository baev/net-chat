package ru.ifmo.baev.chat.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ifmo.baev.chat.data.ByteArrayWrapper;
import ru.ifmo.baev.chat.data.ChatData;
import ru.ifmo.baev.chat.message.TCPMessage;
import ru.ifmo.baev.chat.utils.Config;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.02.14
 */
public class TCPSender implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(TCPSender.class);

    private final byte[] receiverMac;
    private final InetAddress receiverInetAddress;
    private long receiverTimeOffset;

    private final ChatData chatData;

    public TCPSender(byte[] receiverMac,
                     InetAddress receiverInetAddress,
                     long receiverTimeOffset,
                     ChatData chatData) {

        this.receiverMac = receiverMac;
        this.receiverInetAddress = receiverInetAddress;
        this.receiverTimeOffset = receiverTimeOffset;
        this.chatData = chatData;
    }

    @Override
    public void run() {
        ByteArrayWrapper receiverHardwareAddressWrapper = new ByteArrayWrapper(receiverMac);
        Integer countMessages = chatData.getCountMessagesMap().get(receiverHardwareAddressWrapper);
        int startMessagesFrom = countMessages == null ? 0 : countMessages;
        List<TCPMessage> messages = chatData.getTcpMessages().subList(startMessagesFrom, chatData.getTcpMessages().size());
        for (TCPMessage tcpMessage : messages) {
            try {
                sendMessage(tcpMessage);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            chatData.getCountMessagesMap().put(receiverHardwareAddressWrapper, ++startMessagesFrom);
        }
    }

    private void sendMessage(TCPMessage tcpMessage)
            throws IOException {
        Socket socket = new Socket(receiverInetAddress.getHostAddress(), Config.TCP_PORT);
        socket.getOutputStream().write(tcpMessage.toBytes(receiverTimeOffset));
        socket.close();
        LOGGER.info("TCPSend: " + tcpMessage.toString());
    }
}
