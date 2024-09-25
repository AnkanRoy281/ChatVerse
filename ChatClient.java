import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    private static final String SERVER_IP =  "172.22.123.7"; // Replace with the server's IP address
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.print("Enter your name: ");
            String name = scanner.nextLine();
            out.println(name);

            new Thread(() -> {
                while (true) {
                    try {
                        String message = in.readLine();
                        if (message == null) {
                            break;
                        }
                        System.out.println(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }).start();

            while (true) {
                String message = scanner.nextLine();
                out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
