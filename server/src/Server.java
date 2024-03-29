import java.io.*;
import java.net.*;

public class Server {
	public static void main(String[] args) throws IOException {
		int port;
		try {
			port = Integer.parseInt(args[0]);
		} catch (Exception e) {
			port = 8080;
		}

		@SuppressWarnings("resource")
		ServerSocket waitSocket = new ServerSocket(port);

		while (true)
		{
			Socket connection = waitSocket.accept();
		    ProcessRequest process = new ProcessRequest(connection);
		    new Thread(process).start();
		}
	}
}
