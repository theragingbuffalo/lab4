import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.InputStreamReader;
import java.util.regex.*;

public class ProcessRequest implements Runnable {

	private final String SITE_ROOT = "site";
    private Socket socket;

    public ProcessRequest(Socket socket) {
        this.socket = socket;
    }
	
    public void run()
    {
        // Read message from socket connection
        try {
            InputStreamReader isr = new InputStreamReader(socket.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            String st, filename;
			while ((st = br.readLine()) != null) {
				// Parse line for properly formatted GET request
				filename = parseRequest(st);
				if (filename != null) {
		            BufferedReader fileReader = getFileReader(filename);
		            if (fileReader != null)
		            {
		            	// If file was found and retrieved successfully,
		            	// write it to the socket and close
		                writeToSocket(fileReader);
		            }
		            else
		            {
		            	// If file was not found, write a
		            	// message to the socket and close
		            	writeToSocket("404 - File: " + filename + " not found. :(");
		            }
		            socket.close();
		            break;
				}
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private String parseRequest(String request) {
        String filename;
        // Setup regex pattern for matching the request to the required format
        Pattern valid_request_pattern = Pattern.compile("GET (\\S+) HTTP\\/1\\.[01]");
        Matcher valid_request_matcher = valid_request_pattern.matcher(request);
        // Make sure request matches required format
        if (!valid_request_matcher.matches()) {
            System.out.println("Request '" + request + "' is not of format " +
            				   "'GET [file] HTTP/[1.0|1.1]'");
            filename = null;
        } else {
            // Find the filename that was requested
            filename = valid_request_matcher.group(1);
        }
        return filename;
    }
    
    private BufferedReader getFileReader(String filename)
    {
    	if (filename.endsWith("/"))
    	{
    		filename += "index.html";
    	}
    	File file = new File(SITE_ROOT + filename);
    	try
    	{
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			System.out.println("File: " + filename + " retrieved.");
			
	    	return bufferedReader;
		}
    	catch (FileNotFoundException e)
    	{
			System.err.println("File: " + file.getAbsolutePath() + " not found.");
			
			return null;
		}
    }
    
    private void writeToSocket(BufferedReader br)
    {
    	try
    	{
			PrintWriter out = new PrintWriter(socket.getOutputStream(), false);
			String st;
			while ((st = br.readLine()) != null)
			{
				out.println(st);
			}
			out.flush();
			System.out.println("Wrote file to socket output stream.");
		}
    	catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private void writeToSocket(String message)
    {
    	try
    	{
			PrintWriter out = new PrintWriter(socket.getOutputStream(), false);
			out.println(message);
			out.flush();
			System.out.println("Wrote message to socket output stream.");
		}
    	catch (IOException e) {
			e.printStackTrace();
		}
    }
}
