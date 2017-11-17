package HServer;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HServer {

    /**
     * Socket·þÎñ¶Ë
     */
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            System.out.println("Server launched, waiting for connection...");
            while (true) {
            	// Listen for socket's connection and create a new thread for the client
                Socket socket = serverSocket.accept();
                SocketThread socketThread = new SocketThread(socket);
                socketThread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}