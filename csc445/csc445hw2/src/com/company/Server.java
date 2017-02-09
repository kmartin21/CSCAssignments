package com.company;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Server {

    public static void main(String[] args) throws Exception {
        new ServerThread().run();
    }
}

