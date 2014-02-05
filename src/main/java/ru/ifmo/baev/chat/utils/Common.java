package ru.ifmo.baev.chat.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Date: 07.11.13
 */
public final class Common {

    public static final Charset CHARSET = Charset.forName("UTF-8");

    private static final ByteOrder BYTE_ORDER = ByteOrder.BIG_ENDIAN;

    private Common() {
    }

    public static byte[] orderBytes(byte[] bytes) {
        return ByteBuffer.wrap(bytes).order(BYTE_ORDER).array();
    }

    public static byte[] getBytes(byte[] bytes, int from, int to) {
        return Common.orderBytes(Arrays.copyOfRange(bytes, from, to));
    }

    public static String macToString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(18);

        for (byte b : bytes) {
            if (sb.length() > 0)
                sb.append(':');
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static long unpackLong(byte[] bytes) {
        long value = 0;
        for (byte b : bytes) {
            value = (value << 8) + (b & 0xff);
        }
        return value;
    }

    public static byte[] packLong(long l) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(l);
        return buffer.array();
    }

    public static byte[] getMac()
            throws SocketException, UnknownHostException {
        return NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
    }
}
