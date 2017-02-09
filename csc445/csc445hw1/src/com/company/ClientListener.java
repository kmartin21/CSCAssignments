package com.company;

import sun.jvm.hotspot.runtime.Bytes;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by keithmartin on 2/14/16.
 */
public class ClientListener {

    private static final int ONE_BYTE = 1;
    private static final int THIRTY_TWO_BYTES = 32;
    private static final int K_24_BYTES = 1024;
    private static final int SIXTEEN_K_BYTES = 16000;
    private static final int SIXTY_FOUR_K_BYTES = 64000;
    private static final int TWO_FIFTY_SIX_K_BYTES = 256000;
    private static final int ONE_MILL_BYTES = 1000000;
    private static final int TASK_ONE_FINISHED = 1;
    private static final int TASK_TWO_FINISHED = 2;
    private static final int TASK_THREE_FINISHED = 3;
    private static final int TWO_KB = 2000;
    private static final int ONE_KB = 1000;

    public int state = ONE_BYTE;

    public byte[] createTaskOneMessage() {
        if (state == ONE_BYTE) {
            byte[] byteMessage = new byte[1];
            state = THIRTY_TWO_BYTES;
            return byteMessage;
        } else if (state == THIRTY_TWO_BYTES) {
            byte[] byteMessage = new byte[32];
            state = K_24_BYTES;
            return byteMessage;
        } else if (state == K_24_BYTES) {
            byte[] byteMessage = new byte[1024];
            state = TASK_ONE_FINISHED;
            return byteMessage;
        }
        return null;
    }

    public byte[] createTaskTwoMessage() {
        if (state == TASK_ONE_FINISHED) {
            byte[] byteMessage = new byte[1000];
            state = SIXTEEN_K_BYTES;
            return byteMessage;
        } else if (state == SIXTEEN_K_BYTES) {
            byte[] byteMessage = new byte[16000];
            state = SIXTY_FOUR_K_BYTES;
            return byteMessage;
        } else if (state == SIXTY_FOUR_K_BYTES) {
            byte[] byteMessage = new byte[64000];
            state = TWO_FIFTY_SIX_K_BYTES;
            return byteMessage;
        } else if (state == TWO_FIFTY_SIX_K_BYTES) {
            byte[] byteMessage = new byte[256000];
            state = ONE_MILL_BYTES;
            return byteMessage;
        } else if (state == ONE_MILL_BYTES) {
            byte[] byteMessage = new byte[1000000];
            state = TASK_TWO_FINISHED;
            return byteMessage;
        }
        return null;
    }

    public byte[] createTaskThreeMessage() {
        if (state == TASK_TWO_FINISHED) {
            byte[] byteMessage = new byte[4000];
            state = TWO_KB;
            return byteMessage;
        } else if (state == TWO_KB) {
            byte[] byteMessage = new byte[2000];
            state = ONE_KB;
            return byteMessage;
        } else if (state == ONE_KB) {
            byte[] byteMessage = new byte[1000];
            state = TASK_THREE_FINISHED;
            return byteMessage;
        }
        return null;
    }

    public void setState(int state) {
        this.state = state;
    }

}
