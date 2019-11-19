import java.io.*;
import java.net.*;

import server.ProcessRequest;

public class Server {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ServerSocket waitSocket = new ServerSocket(80);

		while (true)
		{
			Socket connection = waitSocket.accept();
		    ProcessRequest process = new ProcessRequest();
		    process.start();
		}
	}
}
