package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;

public class Client implements Runnable {
    private BlockingQueue<Message> queue;
    private Socket s;
    private DataInputStream dis;
    private DataOutputStream dos;
    private int port;
    private String ip;
    private Vector<ConnectionObject> connectionList;


    public Client(String ip, int port, BlockingQueue<Message> queue, Vector<ConnectionObject> connectionList) {
        this.ip = ip;
        this.port = port;
        this.queue = queue;
        this.connectionList = connectionList;


    }

    @Override
    public void run() {
        ConnectionObject connectionObject = new ConnectionObject(ip,port,ConnectionObject.CLIENT_TYPE);

        connectionList.add(connectionObject);

        try {
            s = new Socket(ip, port);
            // obtaining input and out streams
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
            System.out.println("The connection to peer " + s.getInetAddress() + "  is successfully established");

        } catch (IOException e) {
            e.printStackTrace();
        }

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

        readMessage.start();


        while (true) {
            Message messageObject = queue.peek();
            try {

                if (messageObject != null) {

                    if (messageObject.getMessage().equals("terminate")) {
                        this.s.close();
                        break;
                    } else {
                        System.out.println("sending message from client");

                        dos.writeUTF(messageObject.getMessage());
                        queue.remove(messageObject);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            // closing resources
            this.dis.close();
            this.dos.close();
            connectionList.remove(connectionObject);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
