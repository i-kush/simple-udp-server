package com.kush.udp;

import java.io.IOException;
import java.util.logging.Level;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            Log.getLogger().severe("You should specify port number");
            return;
        }
        int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            Log.getLogger().log(Level.SEVERE, "Wrong number", e);
            return;
        }
        Log.getLogger().info(String.format("Starting UDP server on port %d", port));
        try (UDPServer server = new UDPServer(port)) {
            server.startWorking(args.length == 2, 1);
        } catch (IOException e) {
            Log.getLogger().log(Level.SEVERE, e.getMessage(), e);
        }
    }
}