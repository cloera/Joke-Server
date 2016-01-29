/*--------------------------------------------------------
1. Name / Date: Christian Loera / 1-24-2016

2. Java version: build 1.8.0_65

3. Precise command-line compilation examples / instructions:
Make sure JokeServer.java, Cookie.java, ServerAdmin.java, ServerMode.java,
Response.java, Database.java are in the same folder.

> javac JokeServer.java

This should compile to Database.class, JokeDatabase.class, ProverbDatabase.class,
AdminWorker.class, Cookie.class, JokeServer.class, Response.class, ServerAdmin.class,
ServerMode.class, Worker.class


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

This has not been tested on multiple computers. I have tested this with
3 JokeClients, 1 JokeClientAdmin, and 1 JokeServer running at the same time.

References used for implementation:
http://condor.depaul.edu/elliott/435/hw/programs/joke/joke-pseudo.html
https://docs.oracle.com/javase/7/docs/api/java/io/ObjectInputStream.html
https://docs.oracle.com/javase/7/docs/api/java/io/ObjectOutputStream.html
http://condor.depaul.edu/elliott/435/hw/programs/joke/joke-threads.html
http://condor.depaul.edu/elliott/435/hw/programs/joke/joke-state.html
--------------------------------------------------------*/

import java.io.*;
import java.net.*;

/***
 * Worker does all the processing for JokeClient receiving Cookie objects, searching for jokes in the 
 * Database, and sending Response objects. It checks the state of ServerMode to determine 
 * what message to send to the client. When the processing is done it closes the socket to the client.
 */
class Worker extends Thread {
	Socket socket;
	static Database joke;
	static Database proverb;
	static ServerMode mode;

	/***
	 * Worker constructor initializes socket, joke, proverb, and mode
	 * @param sock - Socket that connects to client
	 * @param m - Current state of server mode
	 * @param proverbs - List of proverbs initialized in JokeServer class
	 * @param jokes - List of jokes initialized in JokeServer class
	 */
	Worker(Socket sock, Database jokes, Database proverbs, ServerMode m) {
		socket = sock;
		joke = jokes;
		proverb = proverbs;
		mode = m;
		if(mode.getMode() == null) {  //If mode is null then set mode to JOKE
			mode.setMode(ServerMode.JOKE); //mode can be null if JokeClientAdmin has not set mode of server before client request
		}
	}
	
	/***
	 * run takes in stream of bytes from client, deserializes it as Cookie, and
	 * calls sendResponse to output back to client.  If received client object is not
	 * an expected class then a ClassNotFoundException is thrown. Socket is then closed
	 * when work is done.
	 */
	public void run() {
		ObjectInputStream inputObject = null;
		ObjectOutputStream outputObject = null;

		try {
			inputObject = new ObjectInputStream(socket.getInputStream()); //inputObject used to store stream of bytes from client
			outputObject = new ObjectOutputStream(socket.getOutputStream());//outputObject used to send bytes back to client
			
			try {
				Response responseToClient = new Response(); //Response object to be sent back to client
				Cookie clientCookie;
				clientCookie = (Cookie) inputObject.readObject();  //deserializes object, casts it as Cookie, and stores it in clientCookie
				sendResponse(clientCookie, responseToClient,outputObject);  //sendResponse is called to send output to client
			} catch (ClassNotFoundException exp) { //throws ClassNotFoundException if inputObject cannot be read as Cookie object
				System.out.println("Cannot find class object");
				exp.printStackTrace();
			}
			socket.close(); //close socket to requested server
		} catch (IOException exp) { //throw IOException if inputObject is not of type ObjectInputStream
			System.out.println(exp); //or outputObject is not of type ObjectOutputStream
		}
	}

	/***
	 * sendResponse checks for the mode of the server and sends a Response object to the client
	 * with the appropriate string stored and the client's Cookie.
	 * @param clientCookie - Cookie object received by client
	 * @param responseToClient - Response object that will be sent to client
	 * @param outputObject - Used to write serialized version of object back to client
	 * @throws IOException - Thrown if outputObject is interrupted while processing or fails to process
	 */
	static void sendResponse(Cookie clientCookie, Response responseToClient, ObjectOutputStream outputObject) throws IOException {
		if(mode.getMode() == ServerMode.JOKE) { //If the mode of the server is set to JOKE, send joke
			responseToClient.addResponse(joke.say(clientCookie.getJokeKey())); //gets joke from Database and stores string in responseToClient
			clientCookie.nextJoke(); //clientCookie increments the index of the joke to be accessed later
			responseToClient.setCookie(clientCookie); //stores clientCookie in responseToClient
			System.out.println("Sending joke response..."); //notify server joke is being sent
			outputObject.writeObject(responseToClient); //send a serialized version of Response object to client
		} else if(mode.getMode() == ServerMode.PROVERB) { //If the mode of the server is set to PROVERB, send proverb
			responseToClient.addResponse(proverb.say(clientCookie.getProverbKey()));
			clientCookie.nextProverb();
			responseToClient.setCookie(clientCookie);
			System.out.println("Sending proverb response...");
			outputObject.writeObject(responseToClient); //send Response object with proverb and client's Cookie to the client
		} else if(mode.getMode() == ServerMode.MAINTENANCE) { //If the mode of the server is set to MAINTENANCE, notify clients server is down for maintenance
			responseToClient.addResponse("Joke server temporarily down for maintenance.\n");
			responseToClient.setCookie(clientCookie);
			System.out.println("Sending maintenance response...");
			outputObject.writeObject(responseToClient); //send Response object with maintenance message and client's Cookie to the client
		}
	}
}

/***
 * JokeServer asynchronously deals with the JokeClient and JokeClientAdmin connection.
 * It creates a thread of Worker to handle JokeClient and a thread of ServerAdmin to 
 * handle JokeClientAdmin.
 */
public class JokeServer {
	static ServerMode mode = ServerMode.JOKE;

	public static void main(String[] args) throws IOException {
		int queueLenth = 10; //number of clients that can connect at the same time
		int clientPort = 4500; //port that JokeClient and JokeServer communicate on
		Socket socketClient;
		
		//initialize joke and proverb databases
		Database joke = new JokeDatabase(); 
		Database proverb = new ProverbDatabase();

		System.out.println("Christian Loera's Joke server, listening at port 4500 and 4891.\n");
		
		ServerAdmin servAdmin = new ServerAdmin(); //initialize ServerAdmin class
		Thread threadAdmin = new Thread(servAdmin); //create new thread of ServerAdmin class
		threadAdmin.start();//start ServerAdmin thread to handle JokeClientAdmin connection asynchronously
		
		@SuppressWarnings("resource")
		ServerSocket clientServerSocket = new ServerSocket(clientPort, queueLenth); //creates socket that binds at port JokeClient connects to

		System.out.println("Client server is running");
		while(true) {
			socketClient = clientServerSocket.accept(); //listening for JokeClient
			if(socketClient.isConnected()) //if socketClient connects to JokeClient execute new thread of Worker
				new Worker(socketClient, joke, proverb, mode).start();
		}
	}
}
