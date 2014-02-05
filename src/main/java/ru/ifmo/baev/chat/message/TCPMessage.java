package ru.ifmo.baev.chat.message;

import ru.ifmo.baev.chat.utils.Common;
import ru.ifmo.baev.chat.utils.Config;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static ru.ifmo.baev.chat.utils.Common.getBytes;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.02.14
 */
public class TCPMessage implements Comparable<TCPMessage> {
    private final byte[] mac;
    private final long time;
    private final String text;

    public TCPMessage(byte[] mac, long time, String text) {
        this.mac = mac;
        this.time = time;
        this.text = text;
    }

    public static TCPMessage fromBytes(byte[] bytes)
            throws IllegalArgumentException {

        int minMessageLength = Config.TIME_LENGTH + Config.MAC_ADDRESS_LENGTH + Config.TEXT_SIZE_LENGTH;
        checkMessageLength(bytes, minMessageLength);

        int textSize = getTextSize(bytes);
        checkMessageLength(bytes, minMessageLength + textSize);

        return new TCPMessage(getMacBytes(bytes), getTime(bytes), getText(bytes, textSize));
    }

    private static void checkMessageLength(byte[] bytes, int minLength) {
        if (bytes.length < minLength) {
            throw new IllegalArgumentException();
        }
    }

    private static byte[] getTimeBytes(byte[] bytes) {
        return getBytes(bytes, 0, Config.TIME_LENGTH);
    }

    private static long getTime(byte[] bytes) {
        byte[] timeBytes = getTimeBytes(bytes);
        return Common.unpackLong(timeBytes);
    }

    private static byte[] getMacBytes(byte[] bytes) {
        int from = Config.TIME_LENGTH;
        int to = from + Config.MAC_ADDRESS_LENGTH;
        return getBytes(bytes, from, to);

    }

    private static int getTextSize(byte[] bytes) {
        int from = Config.TIME_LENGTH + Config.MAC_ADDRESS_LENGTH;
        int to = from + Config.TEXT_SIZE_LENGTH;

        byte[] textSizeBytes = getBytes(bytes, from, to);
        return ByteBuffer.wrap(textSizeBytes).getInt();
    }

    private static byte[] getTextBytes(byte[] bytes, int textSize) {
        int from = Config.TIME_LENGTH + Config.MAC_ADDRESS_LENGTH + Config.TEXT_SIZE_LENGTH;
        int to = from + textSize;
        return getBytes(bytes, from, to);
    }

    private static String getText(byte[] bytes, int textSize) {
        return new String(getTextBytes(bytes, textSize));
    }

    public byte[] toBytes(long timeOffset) {
        byte[] time = Common.packLong(this.time - timeOffset);
        byte[] textBytes = text.getBytes(Common.CHARSET);
        byte[] textBytesSize = ByteBuffer.allocate(4).putInt(textBytes.length).array();

        ByteBuffer byteBuffer = ByteBuffer.allocate(time.length + mac.length + textBytesSize.length + textBytes.length);
        byteBuffer.put(Common.orderBytes(time));
        byteBuffer.put(Common.orderBytes(mac));
        byteBuffer.put(Common.orderBytes(textBytesSize));
        byteBuffer.put(Common.orderBytes(textBytes));
        byteBuffer.compact();

        return byteBuffer.array();
    }

    @Override
    public String toString() {
        return String.format("MAC: %s, Time: %s, Text: %s", Common.macToString(mac), time, text);
    }

    public int compareTo(TCPMessage o) {
        if (time == o.time && Arrays.equals(mac, o.mac) && text.equals(o.text)) {
            return 0;
        }
        if (time > o.time) {
            return 1;
        } else {
            return -1;
        }
    }
}
