import java.io.*;

/*
 * RequestEncoder.java
 * 
 * Class to encode a Request object into a byte array to be
 * sent over a network.
 * 
 */
public class RequestEncoder {
    public byte[] encode(Request request) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(buffer);
        
        out.writeByte(request.tml); // Write the TML in bytes
        out.writeByte(request.opCode); // Write the OpCode in bytes
        out.writeInt(request.operandOne); // Write the first operand in bytes
        out.writeInt(request.operandTwo); // Write the second operand in bytes
        out.writeShort(request.id); // Write the Request ID in bytes
        
        out.writeByte(request.opNameLength); // Write OP name length in bytes

        // Write the string as a UTF-16BE byte array
        out.write(request.opName.getBytes("UTF-16BE"));

       

        // Flush the buffer and return the byte array 
        out.flush();
        return buffer.toByteArray();
    }
}