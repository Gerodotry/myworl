public class Client {
    public static void main(String[] args) throws IOException {
        String serverAddress = "localhost";
        int serverPort = 12345;
        BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
        String inputLine;

        while (true) {
            Socket clientSocket = new Socket(serverAddress, serverPort);
            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());

            System.out.print("Enter the value of x (or 'exit' to quit): ");
            inputLine = consoleInput.readLine();

            if (inputLine.equalsIgnoreCase("exit")) {
                break;
            }

            int x = Integer.parseInt(inputLine);
            output.writeInt(x);

            DataInputStream input = new DataInputStream(clientSocket.getInputStream());

            int fx = input.readInt();
            int gx = input.readInt();
            int gcd = input.readInt();

            if (fx == -1) {
                System.out.println("Error: Result for f(x) is too large.");
            } else if (gx == -1) {
                System.out.println("Error: Result for g(x) is too large.");
            } else if (gcd == -1) {
                System.out.println("Error in calculating GCD");
            } else {
                System.out.println("Result f(x): " + fx);
                System.out.println("Result g(x): " + gx);
                System.out.println("GCD(f(x), g(x)): " + gcd);
            }

            clientSocket.close();
        }
    }
}
