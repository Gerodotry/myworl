import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Server {
    private static final int TIMEOUT_SECONDS = 20; // Timeout limit for calculations

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);

        System.out.println("Server is waiting for connections...");

        ExecutorService executorService = Executors.newCachedThreadPool();

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected!");

            DataInputStream input = new DataInputStream(clientSocket.getInputStream());
            int x = input.readInt();
            System.out.println("Received x = " + x);

            Future<Integer> fxCustomer = executorService.submit(() -> calculateF(x));
            Future<Integer> gxCustomer = executorService.submit(() -> calculateG(x));

            int fx = -1;
            int gx = -1;

            try {
                fx = fxCustomer.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
                gx = gxCustomer.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                System.out.println("Timeout occurred while calculating f(x) or g(x).");
                // Handle the timeout here
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            int gcd = -1;

            if (fx != -1 && gx != -1) {
                gcd = calculateGCD(fx, gx);
            }

            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
            output.writeInt(fx);
            output.writeInt(gx);
            output.writeInt(gcd);

            System.out.println("Results sent to the client:");
            System.out.println("Result f(x): " + (fx == -1 ? "Error: Result for f(x) is too large" : String.valueOf(fx)));
            System.out.println("Result g(x): " + (gx == -1 ? "Error: Result for g(x) is too large" : String.valueOf(gx)));
            System.out.println("GCD(f(x), g(x)): " + gcd);

            clientSocket.close();
        }
    }

    public static int calculateF(int x) {
        long result = (long) x + 5;
        if (result > Integer.MAX_VALUE) {
            System.out.println("Error: Result for f(x) is too large.");
            return -1; // Indicate that the result is too large
        }
        return (int) result;
    }

    public static int calculateG(int x) {
        long result = (long) x * x * x * x;
        if (result > Integer.MAX_VALUE) {
            System.out.println("Error: Result for g(x) is too large.");
            return -1; // Indicate that the result is too large
        }
        return (int) result;
    }

    public static int calculateGCD(int a, int b) {
        if (b == 0) {
            return a;
        }
        return calculateGCD(b, a % b);
    }
}
