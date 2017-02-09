package com.company;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;


public class ServerThread extends Thread {


        DatagramSocket sock = null;
        static int checkForNull = 0;

        public ServerThread() throws SocketException, IOException {
            sock = new DatagramSocket(4444);
        }

    @Override
    public void run() {
        super.run();

        TFTPPacket tftpPacket = new TFTPPacket();
        //OP Codes
        final byte RRQ = 1;
        final byte ACK = 4;

        int expectedSequenceNum = 1;
        byte[] ack = tftpPacket.createACKPacket(ACK, expectedSequenceNum);

        byte[] buffer = new byte[1508];
        boolean keepGoing = true;
        byte[] sequenceNum = new byte[4];
        boolean dropPackets = false;
        int chosenRandomNum = 5;
        ArrayList<String> messageList = new ArrayList<>();

        try {
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);

            sock.receive(incoming);
            byte[] rrq = new byte[incoming.getLength()];
            System.arraycopy(incoming.getData(), incoming.getOffset(), rrq, 0, incoming.getLength());
            if (rrq[1] == RRQ) {
                if (rrq[(rrq.length - 1)] == 1) {
                    dropPackets = true;
                }
                DatagramPacket ackDP = new DatagramPacket(ack, ack.length, incoming.getAddress(), incoming.getPort());
                sock.send(ackDP);
            }


            while (keepGoing) {
                sock.receive(incoming);
                byte[] data = new byte[incoming.getLength()];
                System.arraycopy(incoming.getData(), incoming.getOffset(), data, 0, incoming.getLength());
                System.arraycopy(incoming.getData(), 3, sequenceNum, 0, 4);
                int sequenceNumber = ByteBuffer.wrap(sequenceNum).getInt();
                if (sequenceNumber == expectedSequenceNum || sequenceNumber < expectedSequenceNum) {
                    if (!dropPackets || (dropPackets && chosenRandomNum != (int)(Math.random()*100))) {
                        String str = new String(data);
                        messageList.add(str);
                        ack = tftpPacket.createACKPacket(ACK, expectedSequenceNum);
                        DatagramPacket ackDP = new DatagramPacket(ack, ack.length, incoming.getAddress(), incoming.getPort());
                        sock.send(ackDP);
                        expectedSequenceNum++;
                        if (data.length < 1508) {
                            keepGoing = false;
                        }
                    }
                }
            }


            /*while (keepGoing) {
                sock.receive(incoming);
                byte[] data = new byte[incoming.getLength()];

                //If inside allowed window, add to array and send ack with received sequence num
                if (sequenceNumber >= loWindow && sequenceNumber <= hiWindow) {
                    receivedMessages[sequenceNumber] = data;
                    ack = tftpPacket.createACKPacket(ACK, sequenceNumber);
                    DatagramPacket ackDP = new DatagramPacket(ack, ack.length, incoming.getAddress(), incoming.getPort());
                    sock.send(ackDP);
                }
                    if (sequenceNumber == expectedSequenceNum) {
                        expectedSequenceNum++;
                        loWindow++;
                        hiWindow++;
                    }
                        if (expectedSequenceNum == fileSizeInNumPackets) {
                            keepGoing = false;
                        }
                }*/

            PrintWriter out = new PrintWriter("LargeTxtFile.txt");
            for (String message: messageList) {
                out.println(message);
            }
            out.close();
        } catch (IOException e) {
            System.err.println("IOException " + e);
        }
    }
}






