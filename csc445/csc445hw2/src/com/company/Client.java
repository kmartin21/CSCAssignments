package com.company;

import java.awt.image.AreaAveragingScaleFilter;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.concurrent.TimeUnit;


public class Client {

    static DatagramSocket sock = null;
    volatile int loWindow = 0;
    volatile int hiWindow = 500;
    static ArrayList<byte[]> messageList;
    static List<byte[]> messagesLost;
    static int portNumber = 2869;
    int onePercent = 0;
    static String tftpMethod = "";
    static int ip = 0;
    static String dropped = "";
    static byte dropPackets = 0;
    static long start = 0;
    static long finish = 0;
    static long startRtt = 0;
    static long finishRtt = 0;
    static double messageSize = 0;
    static InetAddress a = null;

    public static void main(String[] args) throws Exception {

        //OP Codes
        final byte RRQ = 1;
        final byte ACK = 4;

        TFTPPacket tftpPacket = new TFTPPacket();
        byte[] buffer = new byte[1508];
        File file = new File("Desktop/randomText.txt");

        //User input
        Scanner in = new Scanner(System.in);
        System.out.println("Welcome to Keith Martin's TFTP! Make your selections.");
        System.out.println("Type '4' for IPv4 datagrams or type '6' for IPv6 datagrams:");
        ip = in.nextInt();
        System.out.println("Type 'sliding' to use sliding windows or type 'stop' to use stop and wait:");
        tftpMethod = in.next();
        System.out.println("Type 'y' to drop 1% of packets or 'n' to not drop 1% of packets:");
        dropped = in.next();
        if (dropped.equalsIgnoreCase("y")) {
            dropPackets = 1;
        }

        try {

            if (ip == 4) {
                a = InetAddress.getByName("pi.cs.oswego.edu");
            } else {
                a = Inet6Address.getByName("fe80::225:90ff:fe4d:f030");
            }
            sock = new DatagramSocket();
            //RRQ for file
            messageList = tftpPacket.createSlidingWindow(file);
            byte[] rrq = tftpPacket.createRRQPacket(RRQ, "randomText.txt", "octet", dropPackets);
            DatagramPacket rrqDP = new DatagramPacket(rrq, rrq.length, a, portNumber);
            sock.send(rrqDP);
            //now receive ack to send file
            DatagramPacket replyDP = new DatagramPacket(buffer, buffer.length, a, portNumber);
            sock.receive(replyDP);
            byte[] receivedMessage = replyDP.getData();
            if (receivedMessage[1] == ACK) {
                System.out.println("IP: " + a);
                Client client = new Client();
            }
        } catch (SocketException e) {
            System.err.println("SocketException " + e);
        } catch (UnknownHostException e) {
            System.err.println("UnknownHostException " + e);
        }
    }

    //Constructor
    public Client() {
        if (tftpMethod.equalsIgnoreCase("sliding")) {
            SlidingWindowWorker slidingWindowWorker = new SlidingWindowWorker();
            slidingWindowWorker.start();
        } else if (tftpMethod.equalsIgnoreCase("stop")) {
            StopAndWaitWorker stopAndWaitWorker = new StopAndWaitWorker();
            stopAndWaitWorker.start();
        }
    }


    public void sendPacket() throws UnknownHostException {
        DatagramPacket messageDP = new DatagramPacket(messageList.get(hiWindow), messageList.get(hiWindow).length, a, portNumber);
        try {
            messageSize += messageDP.getLength();
            sock.send(messageDP);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendPackets(int lo, int hi) throws IOException{
        for (int i = lo; hi < messageList.size() && i <= hi; i++) {
            DatagramPacket messageDP = new DatagramPacket(messageList.get(i), messageList.get(i).length, a, portNumber);
            try {
                messageSize += messageDP.getLength();
                sock.send(messageDP);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Thread used for sliding windows
     * Sends and receives messages
     */
    class SlidingWindowWorker extends Thread {
        byte[] ack = new byte[7];
        byte[] sequenceNum = new byte[4];
        @Override
        public void run() {
            super.run();
            DatagramPacket ackDP = new DatagramPacket(ack, ack.length);
            try {
                sendPackets(loWindow, hiWindow);
            } catch (IOException e) {
                e.printStackTrace();
            }
            start = System.nanoTime();
            while (loWindow < messageList.size()) {
                try {
                    sock.setSoTimeout(1);
                    sock.receive(ackDP);
                    System.arraycopy(ackDP.getData(), 3, sequenceNum, 0, 4);
                    if (ByteBuffer.wrap(sequenceNum).getInt() >= loWindow) {
                        if ((hiWindow + (ByteBuffer.wrap(sequenceNum).getInt() - loWindow)) < messageList.size() - 1) {
                            hiWindow += (ByteBuffer.wrap(sequenceNum).getInt() - loWindow);
                        }
                        loWindow = ByteBuffer.wrap(sequenceNum).getInt();
                    }
                    sendPacket();
                } catch (SocketTimeoutException ex) {
                    try {
                        sendPackets(loWindow, hiWindow);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            finish = System.nanoTime();
            double time = (finish - start);
            System.out.println(messageSize);
            System.out.println(messageSize/1000000);
            System.out.println(time/1000000000);
            System.out.println("Throughput: " + (messageSize/1000000)/(time/1000000000));
        }
    }



    /**
     * Thread used for stop and wait
     * Sends and receives messages
     */
    class StopAndWaitWorker extends Thread  {
        byte[] ack = new byte[7];
        int position = 0;
        byte[] sequenceNum = new byte[4];
        @Override
        public void run() {
            super.run();
            DatagramPacket ackDP = new DatagramPacket(ack, ack.length);
            start = System.nanoTime();
            while (position < messageList.size()) {
                try {
                    DatagramPacket messageDP = new DatagramPacket(messageList.get(position), messageList.get(position).length, a, portNumber);
                    sock.send(messageDP);
                    messageSize += messageList.get(position).length;
                    sock.setSoTimeout(1);
                    sock.receive(ackDP);
                    System.arraycopy(ackDP.getData(), 3, sequenceNum, 0, 4);
                    if (ByteBuffer.wrap(sequenceNum).getInt() >= position) {
                        position = ByteBuffer.wrap(sequenceNum).getInt();
                    }
                } catch (SocketTimeoutException ex) {
                    try {
                        DatagramPacket messageDP = new DatagramPacket(messageList.get(position), messageList.get(position).length, a, portNumber);
                        sock.send(messageDP);
                        messageSize += messageList.get(position).length;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            finish = System.nanoTime();
            double time = (finish - start);
            System.out.println(messageSize);
            System.out.println(messageSize/1000000);
            System.out.println(time / 1000000000);
            System.out.println("Throughput: " + (messageSize/1000000)/(time/1000000000));
        }
    }
}
