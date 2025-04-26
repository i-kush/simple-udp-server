package com.kush.udp;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

class UDPServer implements AutoCloseable {

    private DatagramSocket serverSocket;

    UDPServer(int port) {
        try {
            this.serverSocket = new DatagramSocket(port);
        } catch (SocketException e) {
            Log.getLogger().log(Level.SEVERE, e.getMessage(), e);
            throw new UncheckedIOException(e);
        }
    }

    void startWorking(boolean validate, int sleepSeconds) throws IOException {
        byte[] receivedData = new byte[1024];
        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(receivedData, receivedData.length);
            serverSocket.receive(receivePacket);
            String receivedDataString = new String(receivePacket.getData(), StandardCharsets.UTF_8).trim();
            if (!receivedDataString.isEmpty()) {
                String message = "Received data:" + System.lineSeparator() + receivedDataString;
                Log.getLogger().info(message);
                if (validate) {
                    validateMessage(receivedDataString);
                }
            }
            sleep(sleepSeconds);
        }
    }

    void validateMessage(String message) {
        Log.getLogger().info("Performing validation of the received message");
        CefMessage cefMessage;
        try {
            cefMessage = new CefMessage(message);
        } catch (Exception e) {
            Log.getLogger().log(Level.WARNING, "Message is completely invalid", e);
            return;
        }
        int errors = cefMessage.getIncorrect().size() +
                cefMessage.getMissing().size() +
                cefMessage.getOtherErrors().size();
        if (errors == 0) {
            Log.getLogger().info("Message passed validation. Everything is ok.");
            return;
        }
        if (!cefMessage.getIncorrect().isEmpty()) {
            Log.getLogger().warning("Incorrect parts: " + cefMessage.getIncorrect());
        }
        if (!cefMessage.getMissing().isEmpty()) {
            Log.getLogger().warning("Missing parts: " + cefMessage.getMissing());
        }
        if (!cefMessage.getOtherErrors().isEmpty()) {
            Log.getLogger().warning("Other errors: " + cefMessage.getOtherErrors());
        }
    }

    private void sleep(int amount) {
        try {
            Log.getLogger().info(String.format("Waiting for %d second before take next one", amount));
            TimeUnit.SECONDS.sleep(amount);
        } catch (InterruptedException e) {
            Log.getLogger().log(Level.SEVERE, "Error occurred while sleeping", e);
        }
    }

    @Override
    public void close() {
        if (serverSocket != null) {
            Log.getLogger().info("Server closed");
            serverSocket.close();
        }
    }
}
