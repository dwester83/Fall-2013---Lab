// Simple echo server.  Modified from some example on the Internet.

import java.io.*;
import java.net.*;

public class EchoServer {

	static DatagramSocket serverSocket;

	//	private static final int PORT = 3156;

	public static void main(String args[]) throws Exception
	{
		try {
			// Open a UDP datagram socket with a specified port number

			BufferedReader keyboardInput = new BufferedReader(new InputStreamReader(System.in));

			System.out.print("Please input port number: ");
			int port_number = Integer.parseInt(keyboardInput.readLine());

			//	int port_number = 3156;
			serverSocket = new DatagramSocket(port_number);

			System.out.println("Echo server starts ...");

			// Create a buffer for receiving
			byte[] receiveData = new byte[4096];
			// Run forever
			while (true) {
				// Create a packet
				DatagramPacket receivePacket =
						new DatagramPacket(receiveData, receiveData.length);

				// Receive a message			
				serverSocket.receive(receivePacket);

				// Prepare for sending an echo message
				InetAddress destination = receivePacket.getAddress();			
				int dest_port_number = receivePacket.getPort();
				int length_of_message = receivePacket.getLength();			
				String message = new String(receiveData, 0, receivePacket.getLength());

				// Display received message and client address		 
				System.out.println("The received message is: " + message);
				System.out.println("The internal address is: " + destination);
				System.out.println("The internal port number is: " + dest_port_number);

				// Create a buffer for sending
				byte[] sendData = new byte[length_of_message];
				sendData = message.getBytes();

				// Create a packet
				DatagramPacket sendPacket = 
						new DatagramPacket(sendData, length_of_message, destination, dest_port_number);

				// Send a message			
				serverSocket.send(sendPacket);
			}
		} 
		catch (IOException ioEx) {
			ioEx.printStackTrace();
		} 
		finally {
			// Close the socket 
			serverSocket.close();
		}
	}
}
