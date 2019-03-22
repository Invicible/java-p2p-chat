// Java implementation of Server side
// It contains two classes : Server and ClientHandler
// Save file as Server.java
package com.company;

import java.io.*;
import java.util.*;
import java.net.*;

// Server class
public class Server implements Runnable {

    // Vector to store active clients
    static Vector<ClientHandler> ar = new Vector<>();
    private int port;
    // counter for clients
    static int i = 0;

    public Server(int port) {
        this.port = port;
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

                System.out.println("New client request received : " + s);

                // obtain input and output streams
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                System.out.println("Creating a new handler for this client...");

                // Create a new handler object for handling this request.
                ClientHandler mtch = new ClientHandler(s, i, dis, dos);

                // Create a new Thread with this object.
                Thread t = new Thread(mtch);

                System.out.println("Adding this client to active client list");

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
