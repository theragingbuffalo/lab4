import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ProcessRequest extends Thread {
	private final String SITE_ROOT = "../site";
	
    public void run()
    {
    	System.out.println("MyThread running");
    	BufferedReader fileReader = getFileReader("file");
    	Socket s;
    	writeToSocket(fileReader, s);
    }
    
    private BufferedReader getFileReader(String filename)
    {
//    	File file = new File("C:\\Users\\pankaj\\Desktop\\test.txt"); 
//    	  
//    	  BufferedReader br = new BufferedReader(new FileReader(file)); 
//    	  
//    	  String st; 
//    	  while ((st = br.readLine()) != null) 
//    	    System.out.println(st); 
//    	  } 
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
		}
    	catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return true;
    }
}
