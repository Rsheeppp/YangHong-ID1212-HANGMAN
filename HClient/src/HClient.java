
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.IOException;
import java.util.Scanner;


public class HClient {
	private int TIMEOUT_HALF_HOUR = 1800000;
    private int TIMEOUT_HALF_MINUTE = 30000;
    private int PORT = 8888;
    private Socket socket ;
    private PrintWriter toServer ;
    private BufferedReader fromServer ;
    private boolean status;
    
    /**
     * Socket¿Í»§¶Ë
     */

    public static void main(String[] args) throws IOException {
        HClient client = new HClient();
        client.getConnected();
        client.send();
        client.receive();
    }

    private void getConnected() throws IOException {
        try {
        	//ask user for the server's ip address
        	Scanner sc = new Scanner(System.in); 
        	System.out.println("Input Server's IP Address"); 
            String ipAdd = sc.nextLine(); 
            socket = new Socket(ipAdd, PORT);
            socket = new Socket();
            //create a new connect to connect the server
            socket.connect(new InetSocketAddress(ipAdd, PORT), TIMEOUT_HALF_MINUTE);
            socket.setSoTimeout(TIMEOUT_HALF_HOUR);
            toServer = new PrintWriter(socket.getOutputStream(), true);
            fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            }finally{
                System.out.println("Welcome to hangman");
                System.out.println("Type '**' to quit ");
                status = true;
                System.out.println();
            }
        }
    
    // method for receive message from server
    private void receive() throws IOException {
        try {
        	socket.setSoTimeout(TIMEOUT_HALF_MINUTE);
            while (socket.isConnected() && status) {
                String fServer = fromServer.readLine();
                if (fServer == null) { 
                    status = false;
                    System.out.println("Lost Server");
                } else{
                	System.out.println(fServer);
                }
            }
        } finally {
            disconnect();
        }
    }
    //method for send message to server
    private void send() {
        Thread clientThread = new Thread(new Runnable() {
            public void run() {
                try {

                    while (status) {
                        Scanner sc1 = new Scanner(System.in);
                        String input = sc1.nextLine();
                        //quit the game by typing "**" whenever the client wants
                        if (input.equals("**")) {
                            status = false;
                        } else {
                        	//int send(socket,input,)
                            toServer.println(input);
                            toServer.flush();
                        }
                    }
                }
                finally {
                    try {
						disconnect();
					} catch (IOException e) {
						e.printStackTrace();
					}
                }
            }
        });
        clientThread.start();
    }
    // method for disconnecting and closing the resources
    private void disconnect() throws IOException {
        try {
            toServer.close();
            fromServer.close();
            socket.close();
        } finally{}
        System.exit(0);
    }
}
