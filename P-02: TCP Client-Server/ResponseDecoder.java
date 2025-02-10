import java.io.*;

/*
 * ResponseDecoder.java
 * 
 * Class to decode a byte array sent over the network
 * into a Response object
 * 
 */
public class ResponseDecoder {
    public Response decode(InputStream s) throws IOException {

        // Create a new DataInputStream
        DataInputStream source = new DataInputStream(s);

        // Reads the TML, result, error code, and Request ID
        byte tml = source.readByte();
        int result = source.readInt();
        byte errorCode = source.readByte();
        short id = source.readShort();

        return new Response(result, errorCode, id);
    }

    // Decode a byte array into a Response object
    public Response decode(byte[] bytes) throws IOException {
        return decode(new ByteArrayInputStream(bytes, 0, bytes.length));
    }

    
}