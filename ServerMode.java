

/*--------------------------------------------------------
1. Name / Date: Christian Loera / 1-24-2016

2. Java version: build 1.8.0_65

3. Precise command-line compilation examples / instructions:
> javac ServerMode.java

This should compile to ServerMode.class

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

References used for implementation:
https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html
----------------------------------------------------------*/
/***
 * ServerMode stores the state of the server's mode as an enum.
 * Once the ServerAdmin has set the mode the JokeServer gets the mode,
 * determines what the current mode of server has been set to,
 * and sends the appropriate message.
 */
public enum ServerMode { 
	MAINTENANCE, JOKE, PROVERB; //enum constants

	/** mode stores current state of server as MAINTENANCE, JOKE, PROVERB. */
	private ServerMode mode;
	
	/***
	 * getMode gets the current state of server.
	 * @return ServerMode
	 */
	public ServerMode getMode() {
		return mode;
	}
	
	/***
	 * setMode sets the current state of the server and 
	 * stores it in mode.
	 * @param m - enum of type ServerMode
	 */
	public void setMode(ServerMode m) {
		mode = m;
	}
}