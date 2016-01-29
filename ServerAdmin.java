/*--------------------------------------------------------
1. Name / Date: Christian Loera / 1-24-2016

2. Java version: build 1.8.0_65

3. Precise command-line compilation examples / instructions:
Make sure ServerAdmin.java, ServerMode.java

> javac JokeServer.java

This should compile to AdminWorker.class, ServerAdmin.class, ServerMode.class

4. Precise examples / instructions to run this program:

In separate shell windows:

> java JokeServer
> java JokeClient
> java JokeClientAdmin

All commands are displayed in window in ' ' (e.g. 'exit').
JokeServer can only exit by using CTRL + C.

5. List of files needed for running the program:
	-JokeServer.java
	-Cookie.java
	-Response.java
	-ServerMode.java
	-Database.java
	-ServerAdmin.java
	-JokeClient.java
	-JokeClientAdmin.java
	
6. Notes:

ServerAdmin will start running and listening for a connection when JokeServer
is ran on the command line.

References used for implementation:
http://condor.depaul.edu/elliott/435/hw/programs/joke/joke-pseudo.html
http://condor.depaul.edu/elliott/435/hw/programs/joke/joke-threads.html
http://condor.depaul.edu/elliott/435/hw/programs/joke/joke-state.html
--------------------------------------------------------*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/***
 * AdminWorker does all the processing for JokeClientAdmin. It compares the String received by the client
 * to determine what mode to change the JokeServer to. It'll notify the JokeClientAdmin by sending a String
 * response back. When the processing is done it closes the socket to the client.
 */
class AdminWorker extends Thread {
	Socket socket;
	static ServerMode mode;

	/***
	 * The constructor initializes the socket and mode
	 * @param sock - Socket of JokeClientAdmin
	 * @param m - Mode of server
	 */
	AdminWorker(Socket sock, ServerMode m) {
		socket = sock;
		mode = m;
	}
	
	/***
	 * run stores client requested mode as a String and calls
	 * switchMode to change server mode and notify admin client.
	 * Throws IOException if client input fails.
	 */
	public void run() {
		BufferedReader input = null;
		PrintStream output = null;

		try {
			input = new BufferedReader(new InputStreamReader(socket.getInputStream())); //stores bytes coming from admin client
			output = new PrintStream(socket.getOutputStream()); //used to send string a bytes back to client
			
			try {
				String stateInput;
				stateInput = input.readLine();  //stateInput stores lines read by input as a string
				switchMode(stateInput, output);  //switchMode is called to change server mode and notify admin client
			} catch (IOException exp) {
				System.out.println("Server read error");
				exp.printStackTrace();
			}
			socket.close(); //close socket to requested server
		} catch (IOException exp) {
			System.out.println(exp);
		}
	}

	/***
	 * switchMode compares String from admin client to see what mode to change the JokeServer to and
	 * then notifies the admin client of the change. If no valid String was received then it notifies
	 * admin client of the invalid input.
	 * @param stateInput - mode requested by JokeClientAdmin
	 * @param output - used to send reply to admin client
	 * @throws IOException - throws exception if output has failed
	 */
	static void switchMode(String stateInput, PrintStream output) throws IOException {
		if(stateInput.equals("joke")) { //if string from client equals joke
			mode.setMode(ServerMode.JOKE); //set ServerMode to JOKE
			output.println("Server mode is set JOKE."); //notify client mode is set
		} else if(stateInput.equals("proverb")) { //if string is proverb set mode to PROVERB
			mode.setMode(ServerMode.PROVERB);
			output.println("Server mode is set PROVERB.");
		} else if(stateInput.equals("maintenance")) { //if string is maintenance set mode to MAINTENANCE
			mode.setMode(ServerMode.MAINTENANCE);
			output.println("Server mode is set MAINTENANCE.");
		} else //invalid input if all conditions fail
			output.println("Invalid input.");
	}
}

/***
 * ServerAdmin is used to handle JokeClientAdmin and JokeClient asynchronously so no blocking occurs
 * between the socket connections.  When a JokeClientAdmin connects to the ServerAdmin, an
 * AdminWorker thread is created and processes the request.
 */
public class ServerAdmin implements Runnable {
	
	public void run() {
		int queueLenth = 10; //max number of clients that can be handled at one time
		int adminPort = 4891; //port that JokeClientAdmin and ServerAdmin communicate on
		Socket socketAdmin;
		
		ServerMode mode = ServerMode.JOKE; //initialized mode to send to AdminWorker
		
		try {
			@SuppressWarnings("resource")
			ServerSocket adminServerSocket = new ServerSocket(adminPort, queueLenth);//binds socket at port 4891
			System.out.println("Admin server is running");
			while(true) {
				socketAdmin = adminServerSocket.accept();//listens for JokeClientAdmin connections
				if(socketAdmin.isConnected()) //if connected to admin client then start new AdminWorker thread
					new AdminWorker(socketAdmin, mode).start();
			}
		} catch (IOException e) {
			System.out.println("Admin thread IOException.");
			e.printStackTrace();
		}
	
	}
}

