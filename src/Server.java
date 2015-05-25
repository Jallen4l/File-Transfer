import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server implements Runnable {
	// Static variables to store where the file is located and where the file
	// will be located
	public final static String FILE_TO_SEND = "C:/TempServer/test.doc";
	public final static String FILE_TO_RECIEVE = "C:/TempServer/test.doc";
	// Static variable giving a hard coded file size
	public final static int FILE_SIZE = 6022386;

	// Client socket variable initiated
	Socket csocket;

	Server(Socket csocket) {
		this.csocket = csocket;
	}

	// Main method
	public static void main(String[] args) throws IOException {
		//For loop getting a list of files from directory
		File folder = new File("C:/TempServer");
		File[] listOfFiles = folder.listFiles();
		System.out.println("List of Files Currently on Server:");
		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		        System.out.println(listOfFiles[i].getName());
		      } else if (listOfFiles[i].isDirectory()) {
		        System.out.println("Directory " + listOfFiles[i].getName());
		      }
		    }
		// Create a server socket object, giving a port number (port number can
		// be changed)
		ServerSocket servsock = new ServerSocket(54327);
		// Output stating that the server is listening for clients
		System.out.println("Listening...");
		// Creating a threaded server
		while (true) {
			// Server accepts the socket
			Socket sock = servsock.accept();
			// Output stating that client is connected
			System.out.println("Connected");
			// New thread for each socket, so that multiple clients can connect
			// to the host without throwing an exception
			// start() runs the rest of the code
			new Thread(new Server(sock)).start();
		}
	}

	public void run() {

		// File input stream variable initiated with the value of null
		FileInputStream fis = null;
		// Buffered output stream variable initiated with the value of null
		BufferedInputStream bis = null;
		// Output stream variable initiated with the value of null
		OutputStream os = null;
		try {
			// Creating variable myFile to store the file stated by FILE_TO_SEND
			File myFile = new File(FILE_TO_SEND);
			// Defined bytearray which will contain all the temporary data,
			// calculating the file size
			byte[] mybytearray = new byte[(int) myFile.length()];
			// Define fis and bis objects to read from myFile, data read would
			// be filled in bytearray object
			fis = new FileInputStream(myFile);
			bis = new BufferedInputStream(fis);
			// Method used to read the file and store data in bytearray
			bis.read(mybytearray, 0, mybytearray.length);
			// OutStream os provides a channel to communicate with client
			os = csocket.getOutputStream();
			// Output stating the file sending and it's length(bytes)
			System.out.println("Sending " + FILE_TO_SEND + "("
					+ mybytearray.length + " bytes)");
			// Outputstream 'os' writes the data read from bytearray
			os.write(mybytearray, 0, mybytearray.length);
			// Output stating that the data has been written
			System.out.println("Done.");
			// Flushes this output stream and forces any buffered output bytes
			// to be written out
			os.flush();
			// Closing buffered output stream, output stream and client socket
			bis.close();
			os.close();
			csocket.close();
			// Catches any I/O exception that may occur
		} catch (IOException e) {
			System.out.println(e);
		}

		// contains stats of the bytes read from inputstream
		int bytesRead;
		// current total number of bytes read
		int current = 0;
		// File output stream variable initiated with the value of null
		FileOutputStream fos = null;
		// Buffered output stream variable initiated with the value of null
		BufferedOutputStream bos = null;
		try {
			// Creating variable myFile to store the file stated by
			// FILE_TO_RECIEVE
			File myFile = new File(FILE_TO_RECIEVE);
			// Defined bytearray which will contain all the temporary data,
			// given hard coded file size by FILE_SIZE
			byte[] mybytearray = new byte[FILE_SIZE];
			// Defined InputStream object to aid in collecting information
			// passed to input channel
			InputStream is = csocket.getInputStream();
			// Defined FileOutput object which holds the file which will be
			// transfered
			fos = new FileOutputStream(myFile);
			// Defined BufferOutputStream object to aid write data to output
			// file
			bos = new BufferedOutputStream(fos);
			// Using the read method to read data from InputStream, data read is
			// stored in bytearray
			bytesRead = is.read(mybytearray, 0, mybytearray.length);
			// Setting the variable currentTotal to the number of bytes read
			current = bytesRead;
			// Do-while loop
			do {
				// InputStream is read again, if the contents of bytes read are
				// less than or equal to 0, then the currentTotal is updated
				bytesRead = is.read(mybytearray, current,
						(mybytearray.length - current));
				if (bytesRead >= 0)
					current += bytesRead;
				// When bytesRead is -1 (no data left) the loop ends
			} while (bytesRead > -1);
			// Write bytes read
			bos.write(mybytearray, 0, current);
			// Flush, forces any buffered output bytes to be written out to the
			// output stream.
			bos.flush();
			// Flushes this output stream and forces any buffered output bytes
			// to be written out
			fos.flush();
			// Close client socket
			csocket.close();
			// Output stating the file sent and it's length(bytes)
			System.out.println("File " + myFile + " downloaded (" + current
					+ " bytes read)");
			// Catches any I/O exception that may occur
		} catch (IOException e) {
			System.out.println(e);
		}

	}

}
