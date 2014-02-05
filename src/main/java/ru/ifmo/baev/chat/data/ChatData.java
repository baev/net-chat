package ru.ifmo.baev.chat.data;

import ru.ifmo.baev.chat.message.TCPMessage;
import ru.ifmo.baev.chat.utils.Common;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.02.14
 */
public class ChatData {

    private CopyOnWriteArrayList<TCPMessage> tcpMessages;
    private ConcurrentSkipListSet<TCPMessage> chatHistory;
    private Map<ByteArrayWrapper, Integer> countMessagesMap;
    private byte[] mac;

    public ChatData() throws SocketException, UnknownHostException {
        tcpMessages = new CopyOnWriteArrayList<>();
        chatHistory = new ConcurrentSkipListSet<>();
        countMessagesMap = new HashMap<>();
        mac = Common.getMac();
    }

    public CopyOnWriteArrayList<TCPMessage> getTcpMessages() {
        return tcpMessages;
    }

    public ConcurrentSkipListSet<TCPMessage> getChatHistory() {
        return chatHistory;
    }

    public Map<ByteArrayWrapper, Integer> getCountMessagesMap() {
        return countMessagesMap;
    }

    public byte[] getMac() {
        return mac;
    }
}
