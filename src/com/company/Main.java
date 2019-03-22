package com.company;
// Java implementation for multithreaded chat client
// Save file as Main.java

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Main {
    private final static String HELP = "help";
    private final static String MY_IP = "myip";
    private final static String MY_PORT = "myport";
    private final static String CONNECT = "connect";
    private final static String LIST = "list";
    private final static String TERMINATE = "terminate";
    private final static String SEND = "send";


    static String ip;

    public static void main(String args[]) throws UnknownHostException, IOException {
        System.out.println("aassaa");

        Runnable server = new Server(Integer.parseInt(args[0]));
        new Thread(server).start();
        Scanner scn = new Scanner(System.in);

        do {

            // read the message to deliver.
            String msg = scn.nextLine();
            String[] words = msg.split(" ");

            switch (words[0]) {
                case HELP:
                    break;
                case MY_IP:
                    break;
                case MY_PORT:
                    break;
                case CONNECT:
                    initClientConnection("localhost",6666);
                    break;
                case LIST:
                    break;
                case TERMINATE:
                    break;
                case SEND:
                    break;
            }

        } while (scn.nextLine().equals("exit"));


    }

    public static void initClientConnection(String ip, int port) throws UnknownHostException, IOException {
        // getting localhost ip

        // establish the connection
        Socket s = new Socket(ip, port);
        Scanner scn = new Scanner(System.in);

        // obtaining input and out streams
        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());

        // sendMessage thread
        Thread sendMessage = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    // read the message to deliver.
                    String msg = scn.nextLine();

                    try {
                        // write on the output stream
                        dos.writeUTF(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // readMessage thread
        Thread readMessage = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    try {
                        // read the message sent to this client
                        String msg = dis.readUTF();
                        System.out.println(msg);
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
            }
        });

        sendMessage.start();
        readMessage.start();


    }

}
