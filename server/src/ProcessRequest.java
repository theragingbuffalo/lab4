import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.NumberFormat.Style;
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
            String filename = parseRequest(br.readLine());
            System.out.println(filename);
            BufferedReader fileReader = getFileReader(filename);
            if (fileReader != null)
            {
                writeToSocket(fileReader);
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
            System.out.println("Request was not formatted properly");
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
    	System.out.println("Filename is: " + filename);
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
    
    private Boolean writeToSocket(BufferedReader br)
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
		}
    	catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return true;
    }
}
