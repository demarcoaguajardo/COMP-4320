import java.net.*;
import java.io.*;
import java.util.Scanner;

/*
 * ClientTCP.java
 * 
 * Class to send a request to a server and receive a response
 * 
 */
public class ClientTCP {

    public static void main(String args[]) throws Exception {

         // Check for correct number of arguments
         if (args.length != 2) {
            throw new IllegalArgumentException("Perform the "
            + "following: java ClientUDP <server name> <portnum>");
        }

        // Creates a new scanner
        Scanner input = new Scanner(System.in);
        
        // Hostname and port number arguments
        InetAddress hostname = InetAddress.getByName(args[0]);
        int portnum = Integer.parseInt(args[1]);
        
        // Create a new request encoder
        RequestEncoder encoder = new RequestEncoder();
        // Create a new response decoder
        ResponseDecoder decoder = new ResponseDecoder();

        // Create a new socket that connects to the server
        Socket socket = new Socket(hostname, portnum);

        // Create the input and output streams
        DataInputStream streamInput = 
            new DataInputStream(socket.getInputStream());
        DataOutputStream streamOutput =
            new DataOutputStream(socket.getOutputStream());
        
        int requestId = 0; // Request ID
        String proceed = ""; // User input to continue or not

        // While user selects to continue requesting (yes),
        // the client will continue to send requests to the server
        do {

            // Request the user to enter an operator code
            System.out.print("Enter operator code [0-5]: ");
            int opCode = Integer.parseInt(input.next().trim());
            // Request the user to enter the first operand
            System.out.print("Enter the first operand: ");
            int operandOne = Integer.parseInt(input.next().trim());
            // Request the user to enter the second operand
            System.out.print("Enter the second operand: ");
            int operandTwo = Integer.parseInt(input.next().trim());

            // Create a new request object
            Request request = new Request(opCode, operandOne, operandTwo, 
            (short) requestId, "");

            // Encode the request
            byte[] requestEncoded = encoder.encode(request);

            // Print the request
            String requestHex = "";
            for (int i = 0; i < requestEncoded.length; i++) {
                String data;
                if (i == 0) {
                    data = String.format("%02d", requestEncoded[i] & 0xFF);
                } else {
                    data = String.format(" %02X", requestEncoded[i] & 0xFF);
                }
                requestHex += data + " ";
            }
            System.out.println("Request: " + requestHex.trim());

            // Send the request to the server
            streamOutput.write(requestEncoded);

            // Flush the output stream
            streamOutput.flush();

            // Start the timer
            double start = System.nanoTime();

            // Create a buffer to store the response
            byte[] buffer = new byte[1024];
            int bytesRead = 0;

            /*
             * While the server has not answered, keep waiting for a response.
             */
            do {
                try {
                    bytesRead = streamInput.read(buffer); // Read the response
                    if (bytesRead == -1) {
                        // There was no response
                        System.out.println("No response received.");
                        break;
                    }
                } catch (SocketTimeoutException e) {
                    // Timeout Error
                     System.out.println("Timeout: " + e.getMessage());
                     break;
                }
            } while (bytesRead == 0);
            
            /*
             * If the server answered, decode the response and print it.
             */
            if (bytesRead > 0) {
               
                double end = System.nanoTime(); // End timer

                // Decode response
                Response receivedResponse = decoder.decode(buffer); 

                // Print the response in hexadecimal
                String responseHex = "";
                for (int j = 0; j < bytesRead; j++) {
                  String data;
                  if (j == 0) {
                      data = String.format("%02d", buffer[j] & 0xFF);
                  } else {
                      data = String.format(" %02X", buffer[j] & 0xFF); 
                  }
                  responseHex += data + " ";
                }
                System.out.println("Response: " + responseHex.trim());
                System.out.println(receivedResponse + 
                    " (time elapsed: " + ((end - start) / 1000000) + "ms)");

                // Ask to continue
                System.out.print("Continue? (y/n): ");
                proceed = input.next().trim();

                // If user does not want to continue, close the socket
                if (proceed.equalsIgnoreCase("n")) {
                    socket.close();
                    break;
                }

                requestId += 1;

            } else {
                System.out.println("Exiting program.");
            }
        } while (proceed.equals("y"));
        input.close();
        socket.close();
    }
}