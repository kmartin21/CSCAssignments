package com.company;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

/**
 * Created by keithmartin on 3/19/16.
 */
public class TFTPPacket {

    //OP Codes
    private final byte RRQ = 1;
    private final byte WRQ = 2;
    private final byte DATA = 3;
    private final byte ACK = 4;
    private final byte ERROR = 5;


    public byte[] createRRQPacket(final byte opCode, final String fileName, final String mode, byte drop) {

        byte zeroByte = 0;
        int rrqByteLength = 2 + fileName.length() + 1 + mode.length() + 1 + 1;
        byte[] rrqByteArray = new byte[rrqByteLength];

        int position = 0;
        rrqByteArray[position] = zeroByte;
        position++;
        rrqByteArray[position] = opCode;
        position++;
        for (int i = 0; i < fileName.length(); i++) {
            rrqByteArray[position] = (byte) fileName.charAt(i);
            position++;
        }
        rrqByteArray[position] = zeroByte;
        position++;
        for (int i = 0; i < mode.length(); i++) {
            rrqByteArray[position] = (byte) mode.charAt(i);
            position++;
        }
        rrqByteArray[position] = zeroByte;
        position++;
        rrqByteArray[position] = drop;
        return rrqByteArray;
    }


    public byte[] createDataPacket(final byte opCode, int sequenceNum, int dataLength, InputStream in) throws IOException {
        byte zeroByte = 0;
        int dataByteLength = 8 + dataLength;
        byte[] dataByteArray = new byte[dataByteLength];
        int position = 0;
        dataByteArray[position] = zeroByte;
        position++;
        dataByteArray[position] = opCode;
        position++;
        dataByteArray[position] = zeroByte;
        position++;
        byte[] b = ByteBuffer.allocate(4).putInt(sequenceNum).array();
        for (int i = 0; i < b.length; i++) {
            dataByteArray[position] = b[i];
            position++;
        }
        in.read(dataByteArray, 8, dataLength);
        return dataByteArray;
    }


    public ArrayList<byte[]> createSlidingWindow(File file) throws IOException {
        boolean endOfFile = false;
        int length = (int) file.length();
        int sequenceNum = 1;
        ArrayList <byte[]> byteList = new ArrayList<byte[]>();
        InputStream in = new FileInputStream(file);
        int offset = 0;
        int position = 1500;
        while (!endOfFile) {
            if (position > length) {
                position = length;
                endOfFile = true;
            }
            byte[] byteArray = createDataPacket(DATA, sequenceNum, (position - offset), in);
            byteList.add(byteArray);
            offset = position;
            position += 1500;
            sequenceNum++;
        }
        in.close();
        return byteList;
    }


    public byte[] createACKPacket(final byte opCode, int sequenceNum) {
        byte zeroByte = 0;
        byte[] ackByteArray = new byte[7];
        int position = 0;

        ackByteArray[position] = zeroByte;
        position++;
        ackByteArray[position] = opCode;
        position++;
        ackByteArray[position] = zeroByte;
        position++;
        byte[] b = ByteBuffer.allocate(4).putInt(sequenceNum).array();
        for (int i = 0; i < b.length; i++) {
            ackByteArray[position] = b[i];
            position++;
        }
        return ackByteArray;
    }
}
