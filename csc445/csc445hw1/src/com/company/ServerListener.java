package com.company;

/**
 * Created by keithmartin on 2/17/16.
 */
public class ServerListener {
    private static final int START = 1;
    private static final int SIXTEEN_K_BYTES = 16000;
    private static final int SIXTY_FOUR_K_BYTES = 64000;
    private static final int TWO_FIFTY_SIX_K_BYTES = 256000;
    private static final int ONE_MILL_BYTES = 1000000;
    private static final int TASK_TWO_FINISHED = 2;

    public static int state = START;

    public byte[] createTaskTwoMessage() {
        if (state == START) {
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
}
