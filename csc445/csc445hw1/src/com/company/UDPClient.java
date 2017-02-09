package com.company;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Created by keithmartin on 2/15/16.
 */
public class UDPClient {

    static double throughputAckAvgTime = 0;
    public static void main(String args[]) {
        DatagramSocket sock = null;
        ClientListener clientListener = new ClientListener();
        ClientHandler clientHandler = new ClientHandler();
        int port = 2865;
        int sentMessageCount = 0;
        int sampleSize = 3000;
        byte[] message = clientListener.createTaskOneMessage();
        byte[] ack = new byte[1];
        boolean receivedServerMessage = true;
        double rtt = 0;
        double finalRtt = 0;


        try {
            sock = new DatagramSocket();

            InetAddress host = InetAddress.getByName("wolf.cs.oswego.edu");


            //Get average ack time to subtract
            clientHandler.setAvgAckTime(sock, ack, host, port);

            /******TASK ONE******/
            while (sentMessageCount <= sampleSize) {
                try {
                    //take input and send the packet
                    DatagramPacket messageDP = new DatagramPacket(message, message.length, host, port);
                    long start = System.nanoTime();
                    sock.send(messageDP);
                    sock.setSoTimeout(2000);
                    //now receive reply
                    byte[] receivedMessage = new byte[2000];
                    DatagramPacket reply = new DatagramPacket(receivedMessage, receivedMessage.length);
                    sock.receive(reply);
                    receivedServerMessage = true;
                    long end = System.nanoTime();

                    rtt = TimeUnit.MILLISECONDS.convert((end - start), TimeUnit.NANOSECONDS);
                    //echo the details of incoming data - client ip : client port - client message
                    if (reply.getLength() != message.length) {
                        System.out.println("WRONG MESSAGE SIZE");
                        receivedServerMessage = false;
                    }
                    if (sentMessageCount % 1000 == 0 && sentMessageCount != 3000 && sentMessageCount != 0 && receivedServerMessage) {
                        System.out.println("RTT for client on size " + reply.getLength() + ": " + (finalRtt / 1000));
                        byte[] tempArray = clientListener.createTaskOneMessage();
                        message = Arrays.copyOf(tempArray, tempArray.length);
                        finalRtt = 0;
                    }
                } catch (SocketTimeoutException e) {
                    System.out.println("Socket timeout exception: " + sentMessageCount);
                    receivedServerMessage = false;
                } catch (IOException e) {
                    System.err.println("IOException " + e);
                }
                if (receivedServerMessage) {
                    finalRtt += rtt;
                    sentMessageCount++;
                }
            }
            System.out.println("RTT for client on size " + message.length + ": " + (finalRtt / 1000));
            /******TASK ONE******/



            /******TASK TWO******/
            clientHandler.taskTwo1K(sock, host, port);
            clientHandler.taskTwo16K(sock, host, port);
            clientHandler.taskTwo64K(sock, host, port);
            clientHandler.taskTwo256K(sock, host, port);
            clientHandler.taskTwo1M(sock, host, port);
            /******TASK TWO******/


        } catch (SocketException e) {
            System.err.println("SocketException " + e);
        } catch (UnknownHostException e) {
            System.err.println("UnknownHostException " + e);
        }



    }
}
