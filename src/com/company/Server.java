// Java implementation of Server side
// It contains two classes : Server and ClientHandler
// Save file as Server.java
package com.company;

import java.io.*;
import java.util.*;
import java.net.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// Server class
public class Server implements Runnable {

    // Vector to store active clients
    static Vector<ClientHandler> ar = new Vector<>();
    BlockingQueue<Message> queue;
    Vector<ConnectionObject> connectionList;
    private int port;
    // counter for clients
    static int i = 0;

    public Server(int port, BlockingQueue<Message> queue, Vector<ConnectionObject> connectionList) {
        this.port = port;
        this.queue = queue;
        this.connectionList = connectionList;
    }

    @Override
    public void run() {
        try {
            // server is listening on port 1234
            ServerSocket ss = new ServerSocket(port);

            Socket s;

            // running infinite loop for getting
            // client request
            while (true) {
                // Accept the incoming request
                s = ss.accept();

                System.out.println("The connection to peer " + s.getInetAddress() + "  is successfully established");

                // obtain input and output streams
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                ConnectionObject connectionObject = new ConnectionObject(s.getInetAddress().toString(),s.getPort(),ConnectionObject.SERVER_TYPE);
                // Create a new handler object for handling this request.

                ClientHandler mtch = new ClientHandler(s,  dis, dos,queue,connectionList,connectionObject);

                // Create a new Thread with this object.
                Thread t = new Thread(mtch);


                // add this client to active clients list
                ar.add(mtch);

                // start the thread.
                t.start();

                // increment i for new client.
                // i is used for naming only, and can be replaced
                // by any naming scheme
                i++;

            }

        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }
}

// ClientHandler class
