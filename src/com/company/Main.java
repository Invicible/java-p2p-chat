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
    private static BlockingQueue<Message> clientMessageQueue;
    private static Vector<ConnectionObject> connectionList = new Vector<>();
    static InetAddress myIP;
    private static int port;

    public static void main(String args[]) throws IOException {
        serverMessageQueue = new LinkedBlockingQueue<Message>();
        clientMessageQueue = new LinkedBlockingQueue<Message>();

        port = 6666;
        Runnable server = new Server(port, serverMessageQueue, connectionList);
        new Thread(server).start();


        myIP = InetAddress.getLocalHost();


        Scanner scn = new Scanner(System.in);
        String msg;

            do {
                // read the message to deliver.
                msg = scn.nextLine();
                String[] words = msg.split(" ", 3);
                try {
                switch (words[0]) {
                    case HELP:
                        System.out.println("myip - Display IP address");
                        System.out.println("myport - Display port number that the program runs on");
                        System.out.println("connect $ip $port - Connect to another peer where $ip is the IP address and $port " +
                                "is the Port number of that peer");
                        System.out.println("list - List all active connections");
                        System.out.println("send $id $message - Send a message to the peer with id=$id (can be found in connection list) " +
                                "and a message=$message. Example 'send 2 Hello'");
                        System.out.println("terminate $id - Terminate the connection with id=$id");


                        break;
                    case MY_IP:
                        System.out.println("The IP address is " + InetAddress.getLocalHost().getHostAddress());
                        break;
                    case MY_PORT:
                        System.out.println("The program runs on port number " + port);
                        break;
                    case CONNECT:
                        Runnable client = new Client(words[1], Integer.parseInt(words[2]), clientMessageQueue, connectionList);
                        new Thread(client).start();
                        break;
                    case LIST:
                        System.out.format("%5s%19s%10s\n", "id:", "IP Address", "Port No");
                        for (ConnectionObject obj : connectionList) {
                            System.out.format("%5d%19s%10d\n", obj.getId(), obj.getIp(), obj.getPort());
                        }
                        break;
                    case TERMINATE:
                        handleSend(Integer.parseInt(words[1]),"terminate");
                        break;
                    case SEND:
                        handleSend(Integer.parseInt(words[1]), words[2]);
                        break;
                    default:
                        System.out.println("Command is not supported!!");
                }
                }
                catch (ArrayIndexOutOfBoundsException e){
                    System.out.println("Invalid arguments. Use 'help' command for more information");
                }

            } while (!msg.equals("exit"));
    }

    private static void handleSend(int id, String message) {
        for (ConnectionObject obj : connectionList) {
            if (obj.getId() == id) {
                if (obj.getType() == ConnectionObject.CLIENT_TYPE) {
                    clientMessageQueue.offer(new Message(id, message));
                } else {
                    serverMessageQueue.offer(new Message(id, message));
                }
                return;
            }
        }
        System.out.println("No connection with provided ID");

    }

}
