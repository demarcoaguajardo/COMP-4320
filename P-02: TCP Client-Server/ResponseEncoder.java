import java.io.*;

/*
 * ResponseEncoder.java
 * 
 * Class to encode a Response object into a byte array to be
 * sent over a network.
 * 
 */
public class ResponseEncoder {
    public byte[] encode(Response response) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(buffer);

        out.writeByte(response.tml); // Write the TML in bytes
        out.writeInt(response.result); // Write the result in bytes
        out.writeByte(response.errorCode); // Write the error code in bytes
        out.writeShort(response.id); // Write the Request ID in bytes
        
        // Flush the buffer and return the byte array 
        out.flush();
        return buffer.toByteArray();
    }
}