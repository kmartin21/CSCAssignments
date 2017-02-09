package com.company;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Created by keithmartin on 2/3/16.
 */
public class TCPClient {

    static double throughputAckAvgTime = 0;

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        String hostName = "wolf.cs.oswego.edu";
        int portNumber = 2862;
        int sentMessageCount = 0;
        int sampleSize = 3000;
        int sampleSizeTaskThree = 256;
        double finalRtt = 0;
        double throughput = 0;
        double serverThroughput = 0;


        ClientListener clientListener = new ClientListener();
        byte[] message = clientListener.createTaskOneMessage();

        try (
                Socket socket = new Socket(hostName, portNumber);
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                DataInputStream in = new DataInputStream(socket.getInputStream());
        ) {


            //Calculate time to subtract for ack when calculating throughput for both server and client
              while (sentMessageCount <= 1000) {
                long start = System.nanoTime();
                out.writeInt(message.length); // write length of the message
                out.write(message);           // write the message
                int length = in.readInt();                    // read length of incoming message
                if (length > 0) {
                    byte[] recievedMessage = new byte[length];
                    in.readFully(recievedMessage, 0, length); // read the message
                }
                long end = System.nanoTime();
                long rtt = (end - start);
                finalRtt += ((double) rtt/2.0);
                if (sentMessageCount == 1000) {
                    throughputAckAvgTime = (finalRtt/1000);
                    finalRtt = 0;
                }
                sentMessageCount++;
            }
            sentMessageCount = 0;

            /******TASK ONE******/
            while (sentMessageCount <= sampleSize) {
                long start = System.nanoTime();
                out.writeInt(message.length); // write length of the message
                out.write(message);           // write the message
                int length = in.readInt();                    // read length of incoming message
                if (length > 0) {
                    byte[] recievedMessage = new byte[length];
                    in.readFully(recievedMessage, 0, length); // read the message
                }
                long end = System.nanoTime();
                double rtt = TimeUnit.MILLISECONDS.convert((end - start), TimeUnit.NANOSECONDS);
                finalRtt += rtt;
                if (sentMessageCount % 1000 == 0 && sentMessageCount != 3000 && sentMessageCount != 0 ) {
                    System.out.println("RTT for client on size " + length + ": " + (finalRtt/1000));
                    byte[] tempArray = clientListener.createTaskOneMessage();
                    message = Arrays.copyOf(tempArray, tempArray.length);
                    finalRtt = 0;
                }
                sentMessageCount++;
            }
            System.out.println("RTT for client on size " + message.length + ": " + (finalRtt/1000));
            byte[] tmpArray = clientListener.createTaskTwoMessage();
            message = Arrays.copyOf(tmpArray, tmpArray.length);
            sentMessageCount = 0;
            sampleSize = 5000;

            /******TASK ONE******/



            /******TASK TWO******/
            while(sentMessageCount <= sampleSize) {
                long serverStart = 0;
                long serverEnd = 0;
                long end = 0;
                long start = System.nanoTime();
                out.writeInt(message.length); // write length of the message
                out.write(message);           // write the message
                int length = in.readInt();                    // read length of incoming message
                if (length == 1) {
                    byte[] recievedMessage = new byte[length];
                    in.readFully(recievedMessage, 0, length); // read the message
                    end = System.nanoTime();
                    serverStart = System.nanoTime();
                    int serverLength = in.readInt();
                    if (serverLength == message.length) {
                        byte[] recievedThruMessage = new byte[serverLength];
                        in.readFully(recievedThruMessage, 0, serverLength);
                        serverEnd = System.nanoTime();
                    }
                }

                double timeTakenToReachServer = (end - start);
                double timeTakenToReachClient = (serverEnd - serverStart);
                throughput += (((double)message.length/1000000)/(timeTakenToReachServer/1000000000));
                serverThroughput += (((double)message.length/1000000)/(timeTakenToReachClient/1000000000));
                if (sentMessageCount % 1000 == 0 && sentMessageCount != 5000 && sentMessageCount != 0 ) {
                    System.out.println("Throughput for client on size " + message.length + " bytes: " + (throughput/1000));
                    System.out.println("Throughput for server on size " + message.length + " bytes: " + (serverThroughput/1000));
                    byte[] tempArray = clientListener.createTaskTwoMessage();
                    message = Arrays.copyOf(tempArray, tempArray.length);
                    throughput = 0;
                    serverThroughput = 0;
                }
                sentMessageCount++;
            }
            System.out.println("Throughput for client on size " + message.length + " bytes: " + (throughput / 1000));
            System.out.println("Throughput for server on size " + message.length + " bytes: " + (serverThroughput / 1000));
            byte[] tempArray = clientListener.createTaskThreeMessage();
            message = Arrays.copyOf(tempArray, tempArray.length);
            sentMessageCount = 0;
            /******TASK TWO******/


            /******TASK THREE******/
            while (sentMessageCount <= sampleSizeTaskThree) {
                long start = System.nanoTime();
                out.writeInt(message.length);
                out.write(message);
                int length = in.readInt();
                if (length > 0) {
                    byte[] recievedMessage = new byte[length];
                    in.readFully(recievedMessage, 0, length);
                }
                long end = System.nanoTime();
                long rtt = TimeUnit.MILLISECONDS.convert((end - start), TimeUnit.NANOSECONDS);
                finalRtt += rtt;
                if (sentMessageCount == sampleSizeTaskThree && message.length != 1000) {
                    System.out.println("Interaction for " + message.length + " bytes: " + finalRtt/1000);
                    byte[] tempByteArray = clientListener.createTaskThreeMessage();
                    message = Arrays.copyOf(tempByteArray, tempByteArray.length);
                    switch (sentMessageCount) {
                        case 256:
                            sampleSizeTaskThree = 512;
                            break;
                        case 512:
                            sampleSizeTaskThree = 1024;
                            break;
                        default:
                            return;
                    }
                    sentMessageCount = 0;
                }
                sentMessageCount++;
            }
            System.out.println("Interaction for " + message.length + " bytes: " + finalRtt/1000);
            /******TASK THREE******/

        } catch (IOException ex) {

        }

    }
}
