/*--------------------------------------------------------
1. Name / Date: Christian Loera / 1-24-2016

2. Java version: build 1.8.0_65

3. Precise command-line compilation examples / instructions:

Cookie.java must be in same folder when compiling.
> javac Response.java
Should compile Cookie.class, Response.class

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

Response has a Cookie and ArrayList object that is sent
from the server to the client. This allows for multiple
objects to be sent sent through ObjectOutputStream in
one writeObject(Object) call. Server sets objects to be sent
by calling addResponse(String) and setCookie(Cookie). 
Client retrieves message and Cookie by using getCookie() 
and printResponse().

References used for implementation:
http://condor.depaul.edu/elliott/435/hw/programs/joke/joke-state.html
http://docs.oracle.com/javase/1.5.0/docs/api/java/util/UUID.html
https://docs.oracle.com/javase/7/docs/api/java/io/Serializable.html
--------------------------------------------------------*/

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Response is used to safely transfer an ArrayList and Cookie object between
 * the server and client. This is done by serializing and deserializing (converting to byte stream)
 * the object when writeObject(Object) and readObject(Object) are called.
 * Response implements Serializable to allow serializability of object.
 */
public class Response implements Serializable {

	/**
	 * serialVersionUID is used to verify that Response object on client is the same object received by the server.
	 * cookie holds Cookie object
	 * responseArr holds ArrayList of Strings
	 */
	private static final long serialVersionUID = 1L;
	private Cookie cookie;
	private ArrayList<String> responseArr;
	
	/***
	 * Constructor initializes resposeArr
	 */
	public Response() {
		responseArr = new ArrayList<String>();
	}
	
	/***
	 * addResponse adds String to responseArr
	 * @param r - a String to be added
	 */
	public void addResponse(String r) {
		responseArr.add(r);
	}
	
	/***
	 * printResponse goes through responseArr and
	 * prints every item in ArrayList
	 */
	public void printResponse() {
		for(String response : responseArr)
			System.out.println(response);
	}
	
	/***
	 * setCookie sets a Cookie to cookie object
	 * @param c - a Cookie object to be set
	 */
	public void setCookie(Cookie c) {
		cookie = c;
	}
	
	/***
	 * getCookie gets a Cookie object
	 * @return cookie
	 */
	public Cookie getCookie() {
		return cookie;
	}
	
}
