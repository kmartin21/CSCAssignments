package com.company;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

/**
 * Created by keithmartin on 2/22/16.
 */
public class ClientHandler {
    int sentMessageCount = 0;
    int sampleSize = 1000;
    boolean receivedServerMessage = true;
    int receivedMessageLength = 0;
    double serverTempTime = 0;
    double clientRtt = 0;
    double clientFinalRtt = 0;
    double serverRtt = 0;
    double serverFinalRtt = 0;
    double serverStartTime = 0;
    double serverEndTime = 0;
    byte[] message = new byte[1000];
    byte[] serverNak = new byte[4];

    public void setAvgAckTime(DatagramSocket sock, byte[] message, InetAddress host, int port) {
        while (sentMessageCount <= sampleSize) {
            try {
                //take input and send the packet
                DatagramPacket messageDP = new DatagramPacket(message, message.length, host, port);
                long start = System.nanoTime();
                sock.send(messageDP);
                sock.setSoTimeout(2000);
                //now receive reply
                byte[] receivedMessage = new byte[message.length];
                DatagramPacket reply = new DatagramPacket(receivedMessage, receivedMessage.length);
                sock.receive(reply);
                receivedServerMessage = true;
                long end = System.nanoTime();

                clientRtt = (end - start);

                byte[] data = reply.getData();
                receivedMessageLength = data.length;
                //echo the details of incoming data - client ip : client port - client message
                if (reply.getLength() != message.length) {
                    System.out.println("WRONG MESSAGE SIZE");
                    receivedServerMessage = false;
                }
                if (sentMessageCount == sampleSize) {
                    System.out.println("RTT for ack " + ": " + (clientFinalRtt / 1000));
                    UDPClient.throughputAckAvgTime = (clientFinalRtt/1000);
                    clientFinalRtt = 0;
                }
            } catch (SocketTimeoutException e) {
                System.out.println("Socket timeout exception: " + sentMessageCount);
                receivedServerMessage = false;
            } catch (IOException e) {
                System.err.println("IOException " + e);
            }
            if (receivedServerMessage) {
                clientFinalRtt += clientRtt/2.0;
                sentMessageCount++;
            }
        }
        sentMessageCount = 0;
        clientFinalRtt = 0;
        serverFinalRtt = 0;
        clientRtt = 0;
    }

    public void taskTwo1K(DatagramSocket sock, InetAddress host, int port) {
        while (sentMessageCount <= sampleSize) {
            handleFragmentedMessages(sock, host, port, 1);
            clientFinalRtt += clientRtt;
            serverFinalRtt += serverRtt;
            sentMessageCount++;
        }
        System.out.println("Throughput for client on size 1K " + ": " + ((clientFinalRtt /1000)));
        System.out.println("Throughput for server on size 1K " + ": " + ((serverFinalRtt /1000)));
        sentMessageCount = 0;
        clientFinalRtt = 0;
        clientRtt = 0;
        serverFinalRtt = 0;
        serverRtt = 0;
    }


    public void taskTwo16K(DatagramSocket sock, InetAddress host, int port) {
        while (sentMessageCount <= sampleSize) {
                    handleFragmentedMessages(sock, host, port, 16);
                    clientFinalRtt += clientRtt;
                    serverFinalRtt += serverRtt;
                    sentMessageCount++;
        }
        System.out.println("Throughput for client on size 16K " + ": " + ((clientFinalRtt /1000)));
        System.out.println("Throughput for server on size 16K " + ": " + ((serverFinalRtt /1000)));
        sentMessageCount = 0;
        clientFinalRtt = 0;
        serverFinalRtt = 0;
    }

    public void taskTwo64K(DatagramSocket sock, InetAddress host, int port) {
        while (sentMessageCount <= sampleSize) {
            handleFragmentedMessages(sock, host, port, 64);
            clientFinalRtt += clientRtt;
            serverFinalRtt += serverRtt;
            sentMessageCount++;
        }
        System.out.println("Throughput for client on size 64K" + ": " + ((clientFinalRtt /1000)));
        System.out.println("Throughput for server on size 64K " + ": " + ((serverFinalRtt /1000)));
        sentMessageCount = 0;
        clientFinalRtt = 0;
        serverFinalRtt = 0;
    }

    public void taskTwo256K(DatagramSocket sock, InetAddress host, int port) {
        while (sentMessageCount <= sampleSize) {
            handleFragmentedMessages(sock, host, port, 256);
            clientFinalRtt += clientRtt;
            serverFinalRtt += serverRtt;
            sentMessageCount++;
        }
        System.out.println("Throughput for client on size 256K" + ": " + ((clientFinalRtt /1000)));
        System.out.println("Throughput for server on size 256K " + ": " + ((serverFinalRtt /1000)));
        sentMessageCount = 0;
        clientFinalRtt = 0;
        serverFinalRtt = 0;
    }

    public void taskTwo1M(DatagramSocket sock, InetAddress host, int port) {
        while (sentMessageCount <= sampleSize) {
            handleFragmentedMessages(sock, host, port, 1000);
            clientFinalRtt += clientRtt;
            serverFinalRtt += serverRtt;
            sentMessageCount++;
        }
        System.out.println("Throughput for client on size 1M"+ ": " + ((clientFinalRtt /1000)));
        System.out.println("Throughput for server on size 1M " + ": " + ((serverFinalRtt /1000)));
        sentMessageCount = 0;
        clientFinalRtt = 0;
        serverFinalRtt = 0;
    }


    public void handleFragmentedMessages(DatagramSocket sock, InetAddress host, int port, int count) {
        int fragmentedMessageCount = 0;
        double tempTime = 0;

        while (fragmentedMessageCount < count) {
            try {
                //take input and send the packet
                DatagramPacket messageDP = new DatagramPacket(message, message.length, host, port);
                long startTime = System.nanoTime();
                sock.send(messageDP);
                sock.setSoTimeout(2000);
                //now receive reply
                byte[] receivedMessage = new byte[message.length];
                DatagramPacket reply = new DatagramPacket(receivedMessage, receivedMessage.length);
                sock.receive(reply);
                receivedServerMessage = true;

                //echo the details of incoming data - client ip : client port - client message
                if (reply.getLength() != 1) {
                    System.out.println("WRONG MESSAGE SIZE");
                    receivedServerMessage = false;
                } else {
                    long endTime = System.nanoTime();
                    serverStartTime = System.nanoTime();
                    tempTime += Math.abs((endTime - startTime));
                    handleThroughputOfServer(sock, host, port);
                }
            } catch (SocketTimeoutException e) {
                System.out.println("Socket timeout exception: " + sentMessageCount);
                receivedServerMessage = false;
            } catch (IOException e) {
                System.err.println("IOException " + e);
            }
            if (receivedServerMessage) {
                fragmentedMessageCount++;
            }
        }
        clientRtt = (((double)message.length/1000000)/(tempTime/1000000000));
        serverRtt = (((double)message.length/1000000)/(serverTempTime/1000000000));
    }


    public void handleThroughputOfServer(DatagramSocket sock, InetAddress host, int port) {
        serverTempTime = 0;
        boolean didReceiveServerMessage = false;
        while (!didReceiveServerMessage) {
            try {
                //take input and send the packet
                sock.setSoTimeout(2000);
                //now receive reply
                byte[] receivedMessage = new byte[message.length];
                DatagramPacket reply = new DatagramPacket(receivedMessage, receivedMessage.length);
                sock.receive(reply);
                didReceiveServerMessage = true;

                //echo the details of incoming data - client ip : client port - client message
                if (reply.getLength() != 1000) {
                    System.out.println("WRONG MESSAGE SIZE HANDLE SERVER THRU");
                    didReceiveServerMessage = false;
                } else {
                    serverEndTime = System.nanoTime();
                    serverTempTime += serverEndTime - serverStartTime;
                }
            } catch (SocketTimeoutException e) {
                try {
                    DatagramPacket messageDP = new DatagramPacket(serverNak, serverNak.length, host, port);
                    sock.send(messageDP);
                } catch (IOException ex) {
                    System.out.println("IOException");
                }
                System.out.println("Socket timeout exception: " + sentMessageCount);
                didReceiveServerMessage = false;
            } catch (IOException e) {
                System.err.println("IOException " + e);
            }
        }
    }
}
