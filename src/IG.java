import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class IG {

	// Simple echo server.  Modified from some example on the Internet.

	static DatagramSocket serverSocket;

	//	private static final int PORT = 3156;

	public static void main(String args[]) throws Exception
	{
		try {
			// Open a UDP datagram socket with a specified port number

			BufferedReader keyboardInput = new BufferedReader(new InputStreamReader(System.in));

			System.out.print("Please input Internal Gate port number: ");
			int port_number = Integer.parseInt(keyboardInput.readLine());

			//int port_number = 3156;
			serverSocket = new DatagramSocket(port_number);

			System.out.println("Internal Gateway Client server starts ...");

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
				InetAddress sourceAddress = receivePacket.getAddress();
				int source_port_number = receivePacket.getPort();

				byte[] recieved = receivePacket.getData();

				// Get the server address
				byte[] serverAddress = Arrays.copyOfRange(recieved, 0, 4);
				InetAddress addr = null;
				addr = InetAddress.getByAddress(serverAddress);
				InetAddress destination = addr;

				// Get the server port number
				byte[] test = Arrays.copyOfRange(recieved, 4, 6);
				ByteBuffer wrapped = ByteBuffer.wrap(test);
				int dest_port_number = (int) wrapped.getShort();

				// The message that was recieved
				String message = new String(receiveData, 6, receivePacket.getLength());

				// Display received message and client address		 
				System.out.println("The received message is: " + message);
				System.out.println("The client address is: " + sourceAddress);
				System.out.println("The client port number is: " + source_port_number);
				System.out.println("The server address is: " + destination);
				System.out.println("The server port number is: " + dest_port_number);

				// Create a buffer for sending
				byte[] sendData = new byte[message.length()];
				sendData = message.getBytes();

				// Create a packet
				DatagramPacket sendPacket = 
						new DatagramPacket(sendData, message.length(), destination, dest_port_number);

				// Send a message			
				serverSocket.send(sendPacket);



				// Receive a message			
				serverSocket.receive(receivePacket);

				// Prepare for sending an echo message
				int length_of_message = receivePacket.getLength();			
				message = new String(receiveData, 0, receivePacket.getLength());

				// Display received message and client address		 
				System.out.println("The received message is: " + message);
				System.out.println("The client address is: " + destination);
				System.out.println("The client port number is: " + dest_port_number);

				// Create a buffer for sending
				sendData = new byte[length_of_message];
				sendData = message.getBytes();

				// Create a packet
				sendPacket = 
						new DatagramPacket(sendData, length_of_message, sourceAddress, source_port_number);

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
