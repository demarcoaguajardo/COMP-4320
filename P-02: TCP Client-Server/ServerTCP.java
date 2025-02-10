import java.net.*;
import java.io.*;

/*
 * ServerTCP.java
 * 
 * Class to represent a TCP server
 * 
 */
public class ServerTCP {
    public static void main(String[] args) throws Exception {

        // Check for correct number of arguments
        if (args.length > 1) {
            throw new IllegalArgumentException("Perform the "
            + "following: java ServerUDP.java <port number>");
        }
        
        // Port Number (either the first argument or 10019 (our group #))
        int portnum = args.length == 1 ? Integer.parseInt(args[0]) : 10019;
        
        // Create a new decoder for the request
        RequestDecoder decoder = new RequestDecoder();
        // Create a new encoder for the response
        ResponseEncoder encoder = new ResponseEncoder();
        
        // Create a new socket
        try (ServerSocket serverSocket = new ServerSocket(portnum)) {

            // Accept the connection
            Socket socket = serverSocket.accept(); 

            // Loop to receive and send packets
            try {
                while (true) {

                    // Create the input and output streams
                    DataInputStream streamInput = 
                        new DataInputStream(socket.getInputStream());
                    DataOutputStream streamOutput =
                        new DataOutputStream(socket.getOutputStream());

                    // Create a buffer to store the request
                    byte[] buffer = new byte[1024]; // Buffer to store the request
                    int bytesRead = streamInput.read(buffer); // Reads the request

                    if (bytesRead == -1) {
                        System.out.println("User done. Exiting.");
                        break;
                    }

                    Request receivedRequest = decoder.decode(buffer);
                    String requestHex = "";

                    // Print the request in hexadecimal
                    for (int i = 0; i < bytesRead; i++) {
                        String data;
                        if (i == 0) {
                            data = String.format("%02d", buffer[i] & 0xFF);
                        } else {
                            data = String.format(" %02X", buffer[i] & 0xFF); 
                        }
                        requestHex += data + " ";
                    }
                    System.out.println(requestHex.trim());
                    System.out.println(receivedRequest);

                    // Generates the error code. 0 if TML matches; 127 otherwise. 
                    byte errorCode = (receivedRequest.tml) == (bytesRead - 6) ? 
                        (byte) 0 : (byte) 127;

                    // Creates the response
                    Response response = new Response(receivedRequest.compute(), errorCode, 
                    receivedRequest.id);
                    byte[] completeResponse = encoder.encode(response);

                    // Writes response to client
                    streamOutput.write(completeResponse);
                    
                    // Flushes the output stream
                    streamOutput.flush();
                }

            // When the user submits "n"
            } catch (SocketException e) {
                System.out.println("User done. Exiting.");
                return;
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}