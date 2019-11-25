import java.io.*;
import java.net.*;

public class Server {

	public static void main(String[] args) throws IOException {
		ServerSocket waitSocket = new ServerSocket(80);

		while (true)
		{
			Socket connection = waitSocket.accept();
		    ProcessRequest process = new ProcessRequest();
		    process.start();
		}
	}
}
