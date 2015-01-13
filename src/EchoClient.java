// Simple echo client.  Modified from some examples on the Internet.

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

public class EchoClient {

	static DatagramSocket clientSocket;

	public static void main (String args[]) throws Exception 
	{	
		// In case I need to write the time it took to send and receive
		// PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("times.txt", true)));

		try {
			// Open a UDP datagram socket
			clientSocket = new DatagramSocket();

			// Prepare for transmission
			// Determine server IP address		
			// Approach 1: from the host name
			//             our example is localhost
			/*
			String hostname = "localhost";		
			InetAddress destination = InetAddress.getByName(hostname);		
			 */

			// Approach 2: from the IP address
			BufferedReader keyboardInput = new BufferedReader(new InputStreamReader(System.in));

			System.out.print("Please input Internal Gateway IP in #.#.#.# format: ");
			String input = keyboardInput.readLine();
			String[] parts = input.split("\\.");
			byte [] igAddress = new byte[] {(byte)Integer.parseInt(parts[0]),(byte)Integer.parseInt(parts[1]),
					(byte)Integer.parseInt(parts[2]),(byte)Integer.parseInt(parts[3])};
			// byte [] igAddress = new byte[] {(byte)146,(byte)57,(byte)195,(byte)99};

			InetAddress addr = null;
			try {
				addr = InetAddress.getByAddress(igAddress);
			} catch (UnknownHostException impossible) {
				System.out.println("Unable to determine the host by address!");
			}
			InetAddress igDestination = addr;


			// Determine server port number
			System.out.print("Please input Internal Gateway port number: ");
			int igdest_port_number = Integer.parseInt(keyboardInput.readLine());
			// int igdest_port_number = 3000;


			System.out.print("Please input server IP in #.#.#.# format: ");
			input = keyboardInput.readLine();
			parts = input.split("\\.");
			byte [] serverAddress = new byte[] {(byte)Integer.parseInt(parts[0]),(byte)Integer.parseInt(parts[1]),
					(byte)Integer.parseInt(parts[2]),(byte)Integer.parseInt(parts[3])};
			// byte [] serverAddress = new byte[] {(byte)146,(byte)57,(byte)195,(byte)99};


			// Determine server port number
			System.out.print("Please input server port number: ");
			short serverPort = Short.parseShort(keyboardInput.readLine());
			// short serverPort = 2000;


			//Timer for iteration
			//			int size = 128;
			//			while (size <= 2048) {


			// Message and its length
			System.out.print("Message would you like to send: ");
			String message = keyboardInput.readLine();
			// String message = "Hello World!";

			// Array to put all the needed data + 6 for the IP and Port
			ByteBuffer buffer = ByteBuffer.allocate((message.length() + 6));

			// Putting everything into the message
			buffer.put(serverAddress);
			buffer.putShort(serverPort);
			buffer.put(message.getBytes());	

			// Making the size of the total message
			byte[] sendData = buffer.array();
			int length_of_message = sendData.length; 	

			// Create a packet
			DatagramPacket sendPacket = 
					new DatagramPacket(sendData, length_of_message, igDestination, igdest_port_number);

			// Start timer here
			// long startTime = System.nanoTime();

			// Send a message
			clientSocket.send(sendPacket);

			// Print out the message sent
			System.out.println("Message sent is:   [" + message + "]");

			// Prepare for receiving
			// Create a buffer for receiving
			byte[] receiveData = new byte[4096];

			// Create a packet
			DatagramPacket receivePacket = 
					new DatagramPacket(receiveData, receiveData.length);

			// Receive a message
			clientSocket.receive(receivePacket);

			// End timer here
			//		    long endTime = System.nanoTime() - startTime;

			// Display the message
			String echo_message = new String(receiveData, 0, receivePacket.getLength());
			System.out.println("Message echoed is: [" + echo_message + "]");
			/* System.out.println("The timer is at: " + endTime);
			pw.write("Size of message was: " + size + "\nTime was: " + endTime + "\n\n");
			size = size * 2;
			} */
		} 
		catch (IOException ioEx) {
			ioEx.printStackTrace();
		} 
		finally {
			// Close the socket 
			clientSocket.close();
		}
		//		pw.close();
	}

}
