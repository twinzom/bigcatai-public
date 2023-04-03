package com.bigcatai.matchingengine.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;

public class BytesConvertor {

    public static byte[] doubleArrayToBytesLittleEndianOrder(List<Double> vector) {
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bas);
        for (Double f : vector) {
            byte[] bytes = getBytesLittleEndianOrder(f);
            try {
                dos.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return bas.toByteArray();
    }
    
    public static byte[] doubleArrayToBytesLittleEndianOrder(Double[] vector) {
        return doubleArrayToBytesLittleEndianOrder(Arrays.asList(vector));
    }

    public static byte[] getBytesLittleEndianOrder(Double value) {
        byte[] bytes = new byte[8];
        long lng = Double.doubleToLongBits(value);
        for (int i = 0; i < 8; i++) {
            bytes[i] = (byte) (lng >>> (i * 8));
        }
        return bytes;
    }
    
    public static byte[] getBytesLittleEndianOrder (Long value) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putLong(value);
        return buffer.array();
    }
    
}
