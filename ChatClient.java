import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Connected to server at " + SERVER_ADDRESS + ":" + SERVER_PORT);

            System.out.print("Enter your name: ");
            String name = scanner.nextLine();
            out.println(name);
            System.out.println("Name sent to server. You can start chatting now.");

            // Create a final reference to 'out' for use in the lambda
            final PrintWriter outFinal = out;

            // Thread to listen for messages from the server
            Thread listenerThread = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    System.err.println("Error reading from server: " + e.getMessage());
                }
            });
            listenerThread.start();

            // Main thread to send messages to the server
            String userInput;
            while (true) {
                userInput = scanner.nextLine();
                outFinal.println(userInput);
            }

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + SERVER_ADDRESS);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + SERVER_ADDRESS);
            e.printStackTrace();
        }
    }
}