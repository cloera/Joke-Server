/*--------------------------------------------------------
1. Name / Date: Christian Loera / 1-24-2016

2. Java version: build 1.8.0_65

3. Precise command-line compilation examples / instructions:
> javac Cookie.java
Should compile Cookie.class

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

Cookie object is created by JokeClient to be sent to JokeServer and back
to JokeClient packaged in a Response object. For loop in constructor was
originally created to try to add variety to joke and proverb outputs. I could
not figure out how to properly randomize stored indices so I reversed proverbKeys.

References used for implementation:
http://condor.depaul.edu/elliott/435/hw/programs/joke/joke-state.html
http://docs.oracle.com/javase/1.5.0/docs/api/java/util/UUID.html
https://docs.oracle.com/javase/7/docs/api/java/io/Serializable.html
--------------------------------------------------------*/

import java.io.Serializable;
import java.util.ArrayList;

/***
 * Serializable is implemented to serialize and deserialize Cookie class. This allows 
 * Cookie object to be properly handled by client and server by converting object to
 * stream of bytes. Cookie keeps track of what jokes and proverbs have been sent to client.
 */
public class Cookie implements Serializable{
	/**
	 * serialVersionUID is used to verify that Cookie object on client
	 * 		is the same object received by the server.
	 * jokeKeys stores indices of jokeList in Database.java as ArrayList of Integers
	 * proverbKeys stores indices of proverbList in Database.java as ArrayList of Integers
	 * jokePosition stores the current joke index of jokeList in Database.java as an Integer
	 * proverbPosition stores the current proverb index of proverbList in Database.java as an Integer
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Integer> jokeKeys;
	private ArrayList<Integer> proverbKeys;
	private Integer jokePosition;
	private Integer proverbPosition;
	
	/***
	 * Constructor initializes variables and adds indices to
	 * jokeKeys and proverbKeys
	 */
	public Cookie() {
		jokeKeys = new ArrayList<Integer>();
		proverbKeys = new ArrayList<Integer>();
		jokePosition = 0;
		proverbPosition = 0;
		
		/** indices add to jokeKeys and proverbKeys **/
		for(int i = 0; i < 5; i++) {
			jokeKeys.add(i);
			proverbKeys.add(4-i);
		}
	}
	
	/***
	 * getJokeKey returns current jokeKeys index
	 * @return Integer in jokeKeys
	 */
	public Integer getJokeKey() {
		if(jokePosition > 4) {
			jokePosition = 0;
		}
		return jokeKeys.get(jokePosition);
	}

	/***
	 * getProverbKey returns current proverbKeys index
	 * @return Integer in proverbKeys
	 */
	public Integer getProverbKey() {
		if(proverbPosition > 4) {
			proverbPosition = 0;
		}
		return proverbKeys.get(proverbPosition);
	}
	
	/***
	 * nextJoke increments jokePosition to move to next
	 * index of jokeKeys
	 */
	public void nextJoke() {
		jokePosition++;
	}

	/***
	 * nextProverb increments proverbPosition to move
	 * to next index of jokeKeys
	 */
	public void nextProverb() {
		proverbPosition++;
	}
}
