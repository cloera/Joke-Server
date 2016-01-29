/*--------------------------------------------------------
1. Name / Date: Christian Loera / 1-24-2016

2. Java version: build 1.8.0_65

3. Precise command-line compilation examples / instructions:

> javac JokeClientAdmin.java

This should compile to JokeClientAdmin.class

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
http://condor.depaul.edu/elliott/435/hw/programs/joke/joke-state.html
--------------------------------------------------------*/

import java.io.*;
import java.net.*;

/***
 * JokeClientAdmin waits for user to input a mode to change state of JokeServer.
 * If input equals exit then client admin is closed, else sendMode will be
 * called.
 */
public class JokeClientAdmin {

	public static void main(String[] args) {
		String servName;
		if (args.length < 1)
			servName = "localhost";
		else
			servName = args[0];

		System.out.println("Christian Loera's Admin Client.\n");
		System.out.println("Using Server: " + servName + ", Port: 4891");
		
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));//stores command from user to input
		try {
			String modeName;
			do {
				System.out.print("Type 'joke', 'proverb', or 'maintenance' to switch server mode, 'exit' to end session: ");
				System.out.flush();
				modeName = input.readLine(); //stores command from user into modeName
				if (modeName.indexOf("exit") < 0)  //if the string exit is in modeName then sendMode is called
					sendMode(modeName, servName);
			} while (modeName.indexOf("exit") < 0); //continue loop while string exit is not in modeName
			System.out.println("Cancelled by user request.");
		} catch (IOException exp) { //throw IOException input is interrupted or fails
			exp.printStackTrace();
		}
	}


	/***
	 * sendMode sends a request as a String to ServerAdmin to change the mode of JokeServer.
	 * ServerAdmin will reply with a String to validate that mode has been changed.
	 * @param modeName - Requested mode that JokeServer will change to
	 * @param servName - Stores the server address of the client
	 */
	static void sendMode(String modeName, String servName) {
		Socket socket;
		BufferedReader fromServ;
		PrintStream toServ;
		String txtFromServ;

		try {
			socket = new Socket(servName, 4891); //connects to ServerAdmin at port 4891

			fromServ = new BufferedReader(new InputStreamReader(socket.getInputStream())); //used to read String response from ServerAdmin
			toServ = new PrintStream(socket.getOutputStream()); //used to send String of requested server mode to ServerAdmin

			toServ.println(modeName); //sends server mode requested by user
			toServ.flush();

			//reads and prints at most 3 lines of text from ServerAdmin to user
			for (int i = 1; i <= 3; i++) {
				txtFromServ = fromServ.readLine();
				if (txtFromServ != null)
					System.out.println(txtFromServ);
			}
			socket.close(); //close socket to ServerAdmin
		} catch (IOException exp) { //Throws IOException if requested socket fails or is interrupted
			System.out.println("Socket error.");
			exp.printStackTrace();
		}
	}
}
