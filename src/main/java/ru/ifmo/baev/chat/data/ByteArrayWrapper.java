package ru.ifmo.baev.chat.data;

import java.util.Arrays;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.02.14
 */
public class ByteArrayWrapper {

    private final byte[] bytes;

    public ByteArrayWrapper(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ByteArrayWrapper
                && Arrays.equals(bytes, ((ByteArrayWrapper) other).bytes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }
}
