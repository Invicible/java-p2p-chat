package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;

class ClientHandler implements Runnable {


    private BlockingQueue<Message> queue;

    private int id;
    private final DataInputStream dis;
    private final DataOutputStream dos;
    private Socket s;
    private String received;
    Vector<ConnectionObject> connectionList;
    ConnectionObject connectionObject;

    // constructor
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos, BlockingQueue<Message> queue,    Vector<ConnectionObject> connectionList,ConnectionObject connectionObject
    ) {
        this.dis = dis;
        this.dos = dos;
        this.s = s;
        this.queue = queue;
        this.connectionList = connectionList;
        this.connectionObject = connectionObject;
    }

    @Override
    public void run() {
        connectionList.add(connectionObject);

        // readMessage thread
        Thread readMessage = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        // read the message sent to this client
                        String msg = dis.readUTF();
                        System.out.println("New message form " + s.getInetAddress() + " '" + msg + "'");
                    } catch (IOException e) {
                        break;
                    }
                }
                try {
                    // closing resources
                    dis.close();
                    dos.close();
                    connectionList.remove(connectionObject);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        readMessage.start();

        while (true) {
            Message messageObject = queue.peek();

            try {

                if (messageObject != null && messageObject.getId() == connectionObject.getId()) {


                    if (messageObject.getMessage().equals("terminate")) {
                        this.s.close();
                        break;
                    } else {

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
