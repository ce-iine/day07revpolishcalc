package sdf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class PolishClient {

    // public static final int PORT = 3000;
    public static void main(String[] args) throws UnknownHostException, IOException {

        int PORT = 0;

        if (args.length > 0) {
            PORT = Integer.parseInt(args[0]);
        }

        Socket socket = new Socket("localhost", PORT);
        System.out.println("connected to server");

        OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
        BufferedWriter bw = new BufferedWriter(osw);
        InputStreamReader isr = new InputStreamReader(socket.getInputStream());
        BufferedReader br = new BufferedReader(isr);

        Console cons = System.console();

        boolean stop = false;

        while (!stop) {
            String line = cons.readLine("Please enter your calculation request >>>");
            line = line.trim();
            stop = "exit".equals(line);

            bw.write(line + "\n");
            bw.flush();

            if (stop)
                continue;

            line = br.readLine();
            System.out.printf(">> result: %s\n", line);

        }

        // Close the stream
        bw.flush();
        osw.flush();
        osw.close();
        isr.close();
        socket.close();

    }
}
