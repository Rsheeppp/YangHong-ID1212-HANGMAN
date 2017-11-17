package HServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import Hangman.Hangman;


/**
 * Socket多线程处理类 用来处理服务端接收到的客户端请求（处理Socket对象）
 */
public class SocketThread extends Thread {

	private Socket socket;

    public SocketThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
    	//communicate to the client by input stream and output stream
        try {
        	PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            InputStream inputStream = socket.getInputStream();
            Controller communication = new Controller(printWriter, inputStream);
            //Start a new game for this thread
            start(communication);

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
        	try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
        }

    }
    }
    public void start(Controller getData) throws IOException {
		/*PrintWriter printWriter;
		InputStream in;
		printWriter = getData.out ;
		in = getData.in ;*/
		int points = 0 ;
		Controller communication = getData;
        while (true) {
            Hangman hangman = new Hangman(communication);
            int result = hangman.start();
            if (result==1)
            {
            points+=1;
            }
            else if(result==2)
            {
            points-=1;
            }
            //Prints the current points
            communication.printToClient("points: " + points);
            //Ask the client if he wants to play again
            communication.printToClient("Do you want to play again? Press '1' - Play, Press '!' - Quit");
            communication.printToClient("'++'- Yes");
            communication.printToClient("'**'- Quit");
            String clientInput = communication.readClientInput();

            if (clientInput.equals("**")) {
                System.out.println("The client " + Thread.currentThread().getName() + " left ");
                return;
            }
        }
    }
    }
               
               
               
               
               
               
               