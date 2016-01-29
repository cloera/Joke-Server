/*--------------------------------------------------------
1. Name / Date: Christian Loera / 1-24-2016

2. Java version: build 1.8.0_65

3. Precise command-line compilation examples / instructions:
Make sure JokeClient.java, Cookie.java, Response.java are in the same folder.

> javac JokeClient.java

This should compile to Cookie.class, JokeClient.class, Response.class

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
The JokeClient's cookie will remember what jokes/proverbs have been shown.
Information stored in the cookie will be erased once JokeClient exits but
will receive same message as other clients depending on the JokeServer's state.

References used for implementation:
https://docs.oracle.com/javase/7/docs/api/java/io/ObjectInputStream.html
https://docs.oracle.com/javase/7/docs/api/java/io/ObjectOutputStream.html
http://condor.depaul.edu/elliott/435/hw/programs/joke/joke-state.html
--------------------------------------------------------*/

import java.io.*;
import java.net.*;

/***
 * JokeClient waits for user input to begin connection to server.
 * If input equals exit then client is closed, else getJoke will be
 * called.
 */
public class JokeClient {
	static Cookie cookie = new Cookie(); //cookie is initialized
										//cookie is used to remember what jokes and proverbs have been received by client
	public static void main(String[] args) {
		String servName;
		if (args.length < 1)
			servName = "localhost";
		else
			servName = args[0];
		

		System.out.println("Christian Loera's Joke Client.\n");
		System.out.println("Using Server: " + servName + ", Port: 4500");
		
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));//stores input from user client
		
		try {
			String userInput;
			do {
				System.out.print("Press ENTER for joke or proverb, type 'exit' to end session: ");
				System.out.flush();
				userInput = input.readLine(); //stores user input as string
				if (userInput.indexOf("exit") < 0)  //if the string exit is not stored in userInput then call getJoke
					getJoke(servName);
			} while (userInput.indexOf("exit") < 0); //continue loop while string exit does not occur in userInput
			System.out.println("Cancelled by user request.");
		} catch (IOException exp) { //throw IOException  input is interrupted or fails
			exp.printStackTrace();
		}
	}
	
	/***
	 * getJoke connects to JokeServer at port 4500 and sends a Cookie object to the server.
	 * It receives a Response object that has the client's cookie and a string response 
	 * to be printed for the user.
	 * @param servName - stores the server address of the client
	 */
	static void getJoke(String servName) {
		Socket socket;
		ObjectOutputStream outputObjToServ;
		ObjectInputStream inputObjFromServ;
		Response responseFromServer;

		try {
			socket = new Socket(servName, 4500); //connects to JokeServer at 4500
			outputObjToServ = new ObjectOutputStream(socket.getOutputStream()); //used to send Cookie object to JokeServer
			inputObjFromServ = new ObjectInputStream(socket.getInputStream());  //used to receive Response object from JokeServer
			
			outputObjToServ.writeObject(cookie); //sends JokeServer Cookie object

			try {
				responseFromServer = (Response) inputObjFromServ.readObject(); //Reads object from server and stores it as Response object
				responseFromServer.printResponse(); //print response to user
				cookie = responseFromServer.getCookie(); //store updated Cookie from server to client's cookie
			} catch (ClassNotFoundException exp) { //if object from server and responseFromServer types don't match throw exception
				exp.printStackTrace();
			}
			socket.close(); //close socket to server
		} catch (IOException exp) { //Throws IOException if requested socket fails or is interrupted
			System.out.println("Socket error.");
			exp.printStackTrace();
		}
	}
}
