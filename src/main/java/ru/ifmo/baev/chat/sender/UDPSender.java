package ru.ifmo.baev.chat.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ifmo.baev.chat.utils.Common;
import ru.ifmo.baev.chat.message.UDPMessage;
import ru.ifmo.baev.chat.utils.Config;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Date: 08.11.13
 */
public class UDPSender implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(UDPSender.class);

    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress receivers = InetAddress.getByName(Config.UDP_IP);
            LOGGER.info("UDPSender started");
            while (true) {
                UDPMessage udpMessage = new UDPMessage(Common.getMac(), System.currentTimeMillis());
                byte[] message = udpMessage.toBytes();

                DatagramPacket packet = new DatagramPacket(message, message.length, receivers, Config.UDP_PORT);
                socket.send(packet);
                LOGGER.info("UDPSend: " + udpMessage.toString());

                Thread.sleep(Config.UDP_TIMEOUT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
