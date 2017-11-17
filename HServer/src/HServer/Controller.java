package HServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;


// class for the server to communicate to the client
public class Controller {
	 public PrintWriter out;
	 public InputStream in;
     private String input ;
	    //Constructor
	    public Controller(PrintWriter out,InputStream in) {
	        this.out = out;
	        this.in = in;
	    }
	    // A method that prints to the client
	    public String readClientInput() throws IOException
	    {
	    	InputStreamReader inputStreamReader = new InputStreamReader(in);
	    	// boost the efficiency£¬turn byte into stream
	        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);// put it into buffer zone
	        input = bufferedReader.readLine();
	        return input;
	    }
	    //
	   public void printToClient(char[] message) {
	        for (int i = 0; i < message.length; i++) {
	            out.print(message[i] + " ");
	            out.flush();
	        }
	    }
	    public void printToClient(String message) {
	        out.println(message);
	        out.flush();
	    }
	    //Method that reads and input from the client
	    public void isDisconnected() {
	        String chk = input;
	        if (chk == null) {
	            //Message in the server if one of the clients is disconnected
	            System.out.println("Client " + Thread.currentThread().getName() + " has disconnected");
	        };
	    }
	}