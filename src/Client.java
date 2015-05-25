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
import java.util.*;

public class Client {
	// Static variable to store the server socket port, server ip address, and
	// hard coded file size
	public final static int SOCKET_PORT = 54327;
	public final static String SERVER = "192.168.0.12";
	public final static int FILE_SIZE = 6022386;

	// Main method
	public static void main(String[] args) throws IOException {
		// String variable to store scanner string input
		String s;
		// Create scanner object
		Scanner scan = new Scanner(System.in);
		// Output message which requires an input
		System.out
				.println("Client: Would you like to send or recieve a file? **SENDING A FILE TO SERVER IS COMMENTED OUT IN SERVER CLASS** (S/R): ");
		// Stores scanner input into variable
		s = scan.nextLine();

		// CLIENT TO RECIEVE A FILE
		// if the contents of the scanner variable are "R" (receive)
		if (s.equals("R")) {
			// Create another scanner object
			Scanner scan1 = new Scanner(System.in);
			// Output message which requires a correct input
			System.out
					.println("Please enter the file you wish to recieve, with file type (eg .doc, .jpg): ");
			// Store input into variable
			String FileNameR = scan1.nextLine();
			// Check to see if file is too large
			long length = FileNameR.length();
			if (length > Integer.MAX_VALUE) {
				System.out.println("The file is too large");
			}
			// contains stats of the bytes read from inputstream
			int bytesRead;
			// current total number of bytes read
			int current = 0;
			// File output stream variable initiated with the value of null
			FileOutputStream fos = null;
			// Buffered output stream variable initiated with the value of null
			BufferedOutputStream bos = null;
			// Socket variable initiated with the value of null
			Socket sock = null;
			try {
				// Attempt to connect to server using server IP address and
				// socket port number
				sock = new Socket(SERVER, SOCKET_PORT);
				// Output stating that you are connecting
				System.out.println("Connecting...");
				// Defined bytearray which will contain all the temporary data,
				// given hard coded file size by FILE_SIZE
				byte[] mybytearray = new byte[FILE_SIZE];
				// Defined InputStream object to aid in collecting information
				// passed to input channel
				InputStream is = sock.getInputStream();
				// Defined FileOutput object which holds the file which will be
				// transfered.
				// FileNameR is the scanner file name, with the directory
				fos = new FileOutputStream("C:/TempClient/" + FileNameR);
				// Defined BufferOutputStream object to aid write data to output
				// file
				bos = new BufferedOutputStream(fos);
				// Using the read method to read data from InputStream, data
				// read is
				// stored in bytearray
				bytesRead = is.read(mybytearray, 0, mybytearray.length);
				// Setting the variable currentTotal to the number of bytes read
				current = bytesRead;
				// Do-while loop
				do {
					// InputStream is read again, if the contents of bytes read
					// are
					// less than or equal to 0, then the currentTotal is updated
					bytesRead = is.read(mybytearray, current,
							(mybytearray.length - current));
					if (bytesRead >= 0)
						current += bytesRead;
					// When bytesRead is -1 (no data left) the loop ends
				} while (bytesRead > -1);
				// Write bytes read
				bos.write(mybytearray, 0, current);
				// Flush, forces any buffered output bytes to be written out to
				// the
				// output stream.
				bos.flush();
				// Flushes this output stream and forces any buffered output
				// bytes
				// to be written out
				fos.flush();
				// Output stating the file sent and it's length(bytes)
				System.out.println("File " + FileNameR + " downloaded ("
						+ current + " bytes read)");
			} finally {
				// If the file output stream has been flushed correctly, it will
				// be closed
				if (fos != null)
					fos.close();
				// If the buffered output stream has been flushed correctly, it
				// will be closed
				if (bos != null)
					bos.close();
				// If the socket is also null, it will be closed
				if (sock != null)
					sock.close();
			}

		}

		// CLIENT TO SEND A FILE
		// if the contents of the scanner variable are "S" (send)
		else if (s.equals("S")) {
			// Variable to store scanner input
			String FileNameS;
			// Scanner object
			Scanner scan2 = new Scanner(System.in);
			// Output message which requires a correct input
			System.out
					.println("Please enter the file you wish to send, with file type (eg .doc, .jpg): ");
			// Store input into variable
			FileNameS = scan2.nextLine();
			// close scanner
			scan2.close();
			// Check to see if file is too large
			long length = FileNameS.length();
			if (length > Integer.MAX_VALUE) {
				System.out.println("The file is too large");
			}
			// File input stream variable initiated with the value of null
			FileInputStream fis = null;
			// Buffered output stream variable initiated with the value of null
			BufferedInputStream bis = null;
			// Output stream variable initiated with the value of null
			OutputStream os = null;
			// Socket variable initiated with the value of null
			Socket sock = null;

			try {
				// Attempt to connect to server using server IP address and
				// socket port number
				sock = new Socket(SERVER, SOCKET_PORT);
				// // Creating variable myFile to store the file stated scanner
				// file name, with the directory
				File myFile = new File("c:/Recieve/" + FileNameS);
				// Defined bytearray which will contain all the temporary data,
				// calculating the file size
				byte[] mybytearray = new byte[(int) myFile.length()];
				// Define fis and bis objects to read from myFile, data read
				// would
				// be filled in bytearray object
				fis = new FileInputStream(myFile);
				bis = new BufferedInputStream(fis);
				// Method used to read the file and store data in bytearray
				bis.read(mybytearray, 0, mybytearray.length);
				// OutStream os provides a channel to communicate with client
				os = sock.getOutputStream();
				// Output stating the file sending and it's length(bytes)
				System.out.println("Sending " + FileNameS + "("
						+ mybytearray.length + " bytes)");
				// Outputstream 'os' writes the data read from bytearray
				os.write(mybytearray, 0, mybytearray.length);
				// Output stating that the data has been written
				System.out.println("Done.");
				// Flushes this output stream and forces any buffered output
				// bytes
				// to be written out
				os.flush();
				// Closing buffered output stream, output stream and socket
				bis.close();
				os.close();
				sock.close();
				// Catches any I/O exception that may occur
			} catch (IOException e) {
				System.out.println(e);
			}

		}

	}

}