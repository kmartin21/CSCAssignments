package com.company;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import com.sun.security.ntlm.Client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by keithmartin on 2/3/16.
 */
public class TCPServer {

    Socket clientSocket;

    TCPServer(Socket csocket) {
        this.clientSocket = csocket;
    }

    public static void main(String[] args) throws Exception {

        int length = 0;
        byte[] ack = new byte[1];
        int throughputIsDone = 0;

        try (
                ServerSocket serverSocket =
                        new ServerSocket(2862);
                Socket clientSocket = serverSocket.accept();
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
        ) {
            while (true) {
                length = in.readInt();                    // read length of incoming message
                if ((length == 1000 || length == 16000 || length == 64000 || length == 256000 || length == 1000000) && throughputIsDone <= 5000) {
                    System.out.println(throughputIsDone);
                    byte[] message = new byte[length];
                    in.readFully(message, 0, message.length); // read the message
                    out.writeInt(ack.length); // write length of the message
                    out.write(ack);           // write the message
                    out.writeInt(message.length);
                    out.write(message);
                    throughputIsDone++;
                } else if (length == 4000 || length == 2000 || length == 1000) {
                    byte[] message = new byte[length];
                    in.readFully(message, 0, message.length); // read the message
                    out.writeInt(ack.length); // write length of the message
                    out.write(ack);           // write the message
                } else {
                    byte[] message = new byte[length];
                    in.readFully(message, 0, message.length); // read the message
                    out.writeInt(message.length); // write length of the message
                    out.write(message);           // write the message
                }
            }
        }
    }
}

