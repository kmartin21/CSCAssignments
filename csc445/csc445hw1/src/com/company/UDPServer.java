package com.company;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Created by keithmartin on 2/15/16.
 */
public class UDPServer {

    public static void main(String args[]) {
        DatagramSocket sock = null;
        byte[] buffer = new byte[1024];
        byte[] sendToClient = new byte[1000];
        byte[] ack = new byte[1];


        try {
            sock = new DatagramSocket(2865);

            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
            while(true) {
                    sock.receive(incoming);
                    byte[] data = new byte[incoming.getLength()];
                    DatagramPacket dp = new DatagramPacket(data, data.length, incoming.getAddress(), incoming.getPort());
                //If length of message is 1000, send ack and message for server throughput calc
                if (dp.getLength() == 1000) {
                        DatagramPacket ackDP = new DatagramPacket(ack, ack.length, incoming.getAddress(), incoming.getPort());
                        sock.send(ackDP);
                        sock.send(dp);
                    //If handleThroughPutServer method didn't receive the message, resend it
                } else if (dp.getLength() == 4) {
                    DatagramPacket clientDP = new DatagramPacket(sendToClient, sendToClient.length, incoming.getAddress(), incoming.getPort());
                    sock.send(clientDP);
                } else {
                        sock.send(dp);
                    }
            }
        }
        catch(IOException e) {
            System.err.println("IOException " + e);
        }
    }
}



