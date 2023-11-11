import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class SideG {
    private static Map<Double, Double> memoizationCache = new HashMap<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(54321);
            System.out.println("Server G is running...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected to Server G.");

                // Use CompletableFuture for asynchronous request processing
                CompletableFuture.runAsync(() -> {
                    try {
                        ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                        Double x = (Double) in.readObject();

                        Optional<Double> resultG = calculateG(x);

                        ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                        out.writeObject(resultG.orElse(null));

                        clientSocket.close();
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Example implementation of the function g(x)
    private static Optional<Double> calculateG(Double x) {
        if (memoizationCache.containsKey(x)) {
            return Optional.of(memoizationCache.get(x));
        } else {
            // Calculation of g(x) and storing the result in the cache
            Optional<Double> resultG = tryG(x);
            resultG.ifPresent(value -> memoizationCache.put(x, value));
            return resultG;
        }
    }

    // Example implementation of the function g(x) (your implementation may vary)
    private static Optional<Double> tryG(Double x) {
        try {
            return Optional.of(x * 3);
        } catch (Exception e) {
            // Handle any exceptions
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
