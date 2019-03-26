package com.company;

public class ConnectionObject {

    public static final int SERVER_TYPE = 0;
    public static final int CLIENT_TYPE = 1;

    private static int count = 0;

    private  int id = 0;
    private String ip;
    private int port;
    private int type;

    public ConnectionObject(String ip, int port, int type) {
        this.ip = ip;
        this.port = port;
        this.type = type;
        this.id = id;
        id=count++;
    }

    public int getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public int getType() {
        return type;
    }




}
