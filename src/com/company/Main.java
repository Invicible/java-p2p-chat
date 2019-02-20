package com.company;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
//        System.out.println("aa");
//        Scanner sc = new Scanner(System.in);
//        String i = sc.next();
//        System.out.println(i);
        String serverName = "127.0.0.1";
        int port = 6666;

        try {
            Thread t = new Server(6666);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            System.out.println("Client connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);

            System.out.println("Client just connected to " + client.getRemoteSocketAddress());
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            out.writeUTF("Hello from " + client.getLocalSocketAddress());
            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);
            Scanner sc = new Scanner(System.in);

            System.out.println("Server says " + in.readUTF());
            while (true) {

                out.writeUTF("From Client: " );



            }
            //client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
