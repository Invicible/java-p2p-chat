package com.company;
// Java implementation for multithreaded chat client
// Save file as Main.java

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    private final static String HELP = "help";
    private final static String MY_IP = "myip";
    private final static String MY_PORT = "myport";
    private final static String CONNECT = "connect";
    private final static String LIST = "list";
    private final static String TERMINATE = "terminate";
    private final static String SEND = "send";
    private static BlockingQueue<Message> serverMessageQueue;
    private  static BlockingQueue<Message> clientMessageQueue;
    private static Vector<ConnectionObject> connectionList = new Vector<>();
    static InetAddress myIP;

    public static void main(String args[]) throws UnknownHostException, IOException {
        serverMessageQueue = new LinkedBlockingQueue<Message>();
        clientMessageQueue = new LinkedBlockingQueue<Message>();

        Runnable server = new Server(6666,serverMessageQueue,connectionList);
        new Thread(server).start();



        myIP = InetAddress.getLocalHost();



        Scanner scn = new Scanner(System.in);
        String msg;
        do {

            // read the message to deliver.
             msg = scn.nextLine();
            String[] words = msg.split(" ",3);

            switch (words[0]) {
                case HELP:
                    break;
                case MY_IP:
                    System.out.println("The IP address is " + InetAddress.getLocalHost().getHostAddress());
                    break;
                case MY_PORT:
                    break;
                case CONNECT:
                    Runnable client = new Client(words[1],Integer.parseInt(words[2]),clientMessageQueue);
                    new Thread(client).start();
                    break;
                case LIST:

                    for(ConnectionObject obj: connectionList){
                        System.out.println(obj.getId());
                        System.out.println(obj.getIp());
                        System.out.println(obj.getPort());
                        System.out.println(obj.getType());
                    }
                   break;
                case TERMINATE:
                    clientMessageQueue.offer(new Message(1,"hello theasdfsadfre"));
                    break;
                case SEND:
                    serverMessageQueue.offer(new Message(Integer.parseInt(words[1]),words[2]));
                    break;
            }

        } while (!msg.equals("exit"));


    }

}
