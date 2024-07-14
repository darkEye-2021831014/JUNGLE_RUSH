package com.junglerush;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class NetworkUtils {

    public static boolean hasInternetConnection() {
        // Host and port to check internet connectivity
        String host = "8.8.8.8"; // Google DNS server
        int port = 53; // DNS uses port 53
        int timeout = 1500; // Timeout in milliseconds

        try (Socket socket = new Socket()) {
            SocketAddress socketAddress = new InetSocketAddress(host, port);
            socket.connect(socketAddress, timeout);
            System.out.println("Internet Connection Available");
            return true;
        } catch (IOException e) {
            System.out.println("No Internet Connection");
            return false;
        }
    }
}