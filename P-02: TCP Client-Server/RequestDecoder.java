import java.io.*;

/*
 * RequestDecoder.java
 * 
 * Class to decode a byte array sent over the network
 * into a Request object
 * 
 */
public class RequestDecoder {
    public Request decode(InputStream s) throws IOException {

      // Create a new DataInputStream
      DataInputStream source = new DataInputStream(s);

      // Reads the TML, operation code, operands, Request ID, 
      // and operation name length
      byte tml = source.readByte();  
      byte opCode = source.readByte(); 
      int operandOne = source.readInt(); 
      int operandTwo = source.readInt();
      short id = source.readShort();
      byte opNameLength = source.readByte();
      
      // Reads the operation name
      byte[] opName = new byte[opNameLength];
      source.readFully(opName);
      String opNameString = new String(opName, "UTF-16BE");

      return new Request(opCode, operandOne, operandTwo, id, opNameString);
    }

    // Decode a byte array into a Request object
    public Request decode(byte[] bytes) throws IOException {
        return decode(new ByteArrayInputStream(bytes, 0, bytes.length));
    }
}