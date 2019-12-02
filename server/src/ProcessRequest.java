import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.*;

public class ProcessRequest implements Runnable {

    private String request;

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
    }

    private String parseRequest() {
        String filename;
        // Setup regex pattern for matching the request to the required format
        Pattern valid_request_pattern = Pattern.compile("(GET) ([\S]+) (HTTP\/1\.[0-1])");
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

    public void run(){
        String filename = parseRequest();
    }
}
