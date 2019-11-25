package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class RequestFileTest {

	public static void main(String[] args) throws UnknownHostException, IOException {
		// establish socket connection
        Socket s = new Socket("localhost", 80);
        PrintWriter out = new PrintWriter(s.getOutputStream(), false);

        // make request to socket
        out.print("GET " + args[0] + " HTTP/1.1\r\n");
        out.print("Host: localhost");
        out.print("\r\n");
        out.flush();
        
        // read content
        InputStreamReader isr = new InputStreamReader(s.getInputStream());
        BufferedReader br = new BufferedReader(isr);
        int c;
        while ((c = br.read()) != -1) {
          System.out.print((char) c);
        }
        
        // close connection
        s.close();
	}

}
