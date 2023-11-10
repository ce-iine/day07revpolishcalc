package sdf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Stack;

public class Calculations implements Runnable {

    public static final String ADD = "+";
    public static final String SUBTRACT = "-";
    public static final String MULTIPLY = "*";
    public static final String DIVIDE = "/";
    String line = "";
    private final Socket sock;

    public Calculations(Socket sock) {
        this.sock = sock;
    }

    private final Stack<Float> stack = new Stack<>();

    @Override
    public void run() {
        // entry point of the thread
        System.out.printf("Starting thread...\n");
        try {
            evaluate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void evaluate() throws IOException {

        InputStreamReader isr = new InputStreamReader(sock.getInputStream());
        BufferedReader br = new BufferedReader(isr);
        OutputStreamWriter osw = new OutputStreamWriter(sock.getOutputStream());
        BufferedWriter bw = new BufferedWriter(osw);

        boolean stop = false;

        while (!stop) {
            line = br.readLine();
            System.out.printf("CLIENT REQUEST: %s\n", line);

            String[] terms = line.trim().split(" ");
            float ans = 0f;
            float lhs = 0f;
            float rhs = 0f;

            for (String t : terms) {

                switch (t) {
                    case ADD:
                        rhs = stack.pop();
                        lhs = stack.pop();
                        ans = lhs + rhs;
                        stack.push(ans);
                        break;

                    case SUBTRACT:
                        rhs = stack.pop();
                        lhs = stack.pop();
                        ans = lhs - rhs;
                        stack.push(ans);
                        break;

                    case MULTIPLY:
                        rhs = stack.pop();
                        lhs = stack.pop();
                        ans = lhs * rhs;
                        stack.push(ans);
                        break;

                    case DIVIDE:
                        rhs = stack.pop();
                        lhs = stack.pop();
                        ans = lhs / rhs;
                        stack.push(ans);
                        break;

                    default:
                        stack.push(Float.parseFloat(t));

                }

            }
            System.out.printf("line: %s, result: %f\n", line, ans);
            bw.write("%5.2f\n".formatted(ans));
            bw.flush();
        }
        // Close the stream
        bw.flush();
        osw.flush();
        osw.close();
        isr.close();
        sock.close();
    }
}
