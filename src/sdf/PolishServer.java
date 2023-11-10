package sdf;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PolishServer {

    public static final int PORT = 3000;

    public static void main(String[] args) throws IOException {

        final ExecutorService thrPool = Executors.newFixedThreadPool(2);
        System.out.printf("listening on port %d\n", PORT);
        ServerSocket server = new ServerSocket(PORT);

        while (true) {
            System.out.printf("Waiting for client connection\n");
            //server that listens for incoming connections on specified port and accepts client connection when one is made. 
            //Once a connection is accepted, the server can communicate with the client using the Socket object (client). 
            Socket client = server.accept(); 

            Calculations calc = new Calculations(client);

            thrPool.submit(calc);
        }

    }
    
}