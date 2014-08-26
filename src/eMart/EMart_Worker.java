package eMart;

import java.io.*;
import java.net.Socket;

public class EMart_Worker implements Runnable {
	private Socket client;

	//Constructor
	EMart_Worker(Socket client) {
		this.client = client;
	}

	  public void run(){
	    String line;
	    BufferedReader in = null;
	    PrintWriter out = null;
	    try{
	      in = new BufferedReader(new 
	        InputStreamReader(client.getInputStream()));
	      out = new 
	        PrintWriter(client.getOutputStream(), true);
	    } catch (IOException e) {
	      System.out.println("in or out failed");
	      System.exit(-1);
	    }

	    while(true){
	      try{
	        line = in.readLine();
	//Send data back to client
	        out.println(line);
	//Append data to text are
	       }catch (IOException e) {
	        System.out.println("Read failed");
	        System.exit(-1);
	       }
	    }
	  }
	}
