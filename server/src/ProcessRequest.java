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
	private final String SITE_ROOT = "../site";
    private String request;
    private Socket s;

    public ProcessRequest(Socket s) throws IOException {
        // Read message from socket connection
        InputStreamReader isr = new InputStreamReader(s.getInputStream());
        BufferedReader br = new BufferedReader(isr);
        StringBuffer requestStringBuffer = new StringBuffer();
        int c;
        while ((c = br.read()) != -1) {
            requestStringBuffer.append((char) c);
        }
        
        // Convert to a string for easier processing later
        request = requestStringBuffer.toString();
        this.s = s;
    }
	
    public void run()
    {
        String filename = parseRequest();
    	BufferedReader fileReader = getFileReader(filename);
    	if (fileReader != null)
    	{
    		writeToSocket(fileReader, s);
    	}
    }
    
    private String parseRequest() {
        String filename;
        // Setup regex pattern for matching the request to the required format
        Pattern valid_request_pattern = Pattern.compile("(GET) ([\\S]+) (HTTP\\/1\\.[0-1])");
        Matcher valid_request_matcher = valid_request_pattern.matcher(request);
        // Make sure request matches required format
        if (!valid_request_matcher.matches()) {
            System.out.println("Request was not formatted properly");
            filename = null;
        } else {
            // Find the filename that was requested
            filename = valid_request_matcher.group(2);
        }
        return filename;
    }
    
    private BufferedReader getFileReader(String filename)
    {
    	File file = new File(SITE_ROOT + filename);
    	try
    	{
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
	    	return bufferedReader;
		}
    	catch (FileNotFoundException e)
    	{
			System.err.println("File: " + file.getAbsolutePath() + " not found. :(");
			e.printStackTrace();
		} 
    	
    	return null;
    }
    
    private Boolean writeToSocket(BufferedReader br, Socket s)
    {
    	try
    	{
			PrintWriter out = new PrintWriter(s.getOutputStream(), false);
			String st;
			while ((st = br.readLine()) != null)
			{
				out.println(st);
			}
		}
    	catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return true;
    }
}
