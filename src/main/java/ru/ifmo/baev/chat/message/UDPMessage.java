package ru.ifmo.baev.chat.message;

import ru.ifmo.baev.chat.utils.Common;
import ru.ifmo.baev.chat.utils.Config;

import static ru.ifmo.baev.chat.utils.Common.getBytes;

/**
 * Date: 08.11.13
 */
public class UDPMessage {
    private final byte[] mac;
    private final long time;

    public UDPMessage(byte[] hardwareAddress, long time) {
        mac = hardwareAddress;
        this.time = time;
    }

    public static UDPMessage fromBytes(byte[] bytes) {
        int minMessageLength = Config.TIME_LENGTH + Config.MAC_ADDRESS_LENGTH;
        checkMessageLength(bytes, minMessageLength);

        byte[] time = getTimeBytes(bytes);
        byte[] mac = getMacBytes(bytes);
        return new UDPMessage(mac, Common.unpackLong(time));
    }

    private static byte[] getTimeBytes(byte[] bytes) {
        return getBytes(bytes, 0, Config.TIME_LENGTH);
    }

    private static byte[] getMacBytes(byte[] bytes) {
        int from = Config.TIME_LENGTH;
        int to = from + Config.MAC_ADDRESS_LENGTH;
        return getBytes(bytes, from, to);
    }

    private static void checkMessageLength(byte[] bytes, int minLength) {
        if (bytes.length < minLength) {
            throw new IllegalArgumentException();
        }
    }

    public byte[] toBytes() {
        byte[] message = new byte[Config.UDP_MESSAGE_LENGTH];
        byte[] mac = Common.orderBytes(this.mac);
        System.arraycopy(mac, 0, message, Config.TIME_LENGTH, Config.MAC_ADDRESS_LENGTH);
        byte[] time = Common.orderBytes(Common.packLong(this.time));
        System.arraycopy(time, 0, message, 0, Config.TIME_LENGTH);
        return message;
    }

    public byte[] getMac() {
        return mac;
    }

    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return String.format("MAC: %s, Time: %s", Common.macToString(mac), time);
    }
}
