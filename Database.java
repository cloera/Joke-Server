/*--------------------------------------------------------
1. Name / Date: Christian Loera / 1-24-2016

2. Java version: build 1.8.0_65

3. Precise command-line compilation examples / instructions:
> javac Database.java
This should compile Database.class, JokeDatabase.class, ProverbDatabase.class

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

Database is initialized in JokeServer.java and passed to thread of Worker class.
Strings from jokeList and proverbList are stored in Response object before being sent
to client.

 Jokes courtesy of the users from reddit.com
 https://www.reddit.com/r/AskReddit/comments/m32f7/gimme_your_bestworst_computer_science_jokes_oh/
  
 Proverbs courtesy of quotegarden.com
 http://www.quotegarden.com/computers.html
--------------------------------------------------------*/

import java.util.ArrayList;

/***
 * Database is an interface that defines say(Integer) method.
 */
public interface Database {
	/***
	 * say takes in an Integer corresponding to the index of an ArrayList of Strings
	 * and returns a joke or proverb String
	 * @param key - Integer corresponding to ArrayList index
	 * @return joke or proverb String
	 */
	String say(Integer key);
}

/***
 * JokeDatabase implements Database interface and stores list of jokes
 * to be sent to client
 */
class JokeDatabase implements Database {
	/**
	 * jokeList stores jokes as an ArrayList of Strings
	 */
	private ArrayList<String> jokeList;

	/***
	 * Constructor initializes jokeList and adds string of jokes to said list
	 */
	public JokeDatabase() {
		jokeList = new ArrayList<String>(); //jokeList initialized
		
		//adding jokes to list
		jokeList.add("How many computer programmers does it take to change a light bulb?\n"
							+ "None, that's a hardware problem.\n");
		jokeList.add("What do you call 8 Hobbits?\n "
							+ "A Hobbyte.\n");
		jokeList.add("Why did C++ decide to not go out with C?\n"
							+ "Because quite frankly, C just has no class.\n");
		jokeList.add("I would tell you a UDP joke, but you might not get it.\n");
		jokeList.add("Why do mathematicians always confuse Halloween and Christmas?\n"
							+ "Because 31 Oct = 25 Dec.\n");
	}
	
	/***
	 * say returns joke from jokeList at key index.
	 */
	public String say(Integer key) {
		return jokeList.get(key);
	}
}

class ProverbDatabase implements Database {
	/**
	 * proverbList stores proverbs as an ArrayList of Strings
	 */
	private ArrayList<String> proverbList;

	/***
	 * Constructor initializes proverbList and adds string of proverbs
	 */
	public ProverbDatabase() {
		proverbList = new ArrayList<String>();
		
		proverbList.add("Man is still the most extraordinary computer of all.\n"
							+ "-John F. Kennedy\n");
		proverbList.add("To err is human, to really foul things up requires a computer.\n"
							+ "-Bill Vaughan\n");
		proverbList.add("Computing is not about computers anymore. It is about living.\n"
							+ "-Nicholas Negroponte\n");
		proverbList.add("Computers have lots of memory, but no imagination.\n"
							+ "-Author Unknown\n");
		proverbList.add("Hardware: the parts of a computer that can be kicked.\n"
							+ "-Jeff Pesis\n");
	}

	/***
	 * say returns proverb from proverbList at key index.
	 */
	public String say(Integer key) {
		return proverbList.get(key);
	}
}
