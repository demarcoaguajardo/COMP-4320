
/*
 * Request.java
 * 
 * Class to represent a request object
 * 
 */

 import java.io.UnsupportedEncodingException;

 public class Request {
   public Integer tml; // Total Message Length (in bytes, including TML)
   public Integer opCode; // Operation Code
   public Integer operandOne; // First operand
   public Integer operandTwo; // Second operand
   public Short id; // Request ID
   public Integer opNameLength; // Length of the operation name (in bytes)
   public String opName; // Operation name (use "UTF-16BE" encoding)
 
   public Request(int opCode, int operandOne, int operandTwo, Short id, String opName) {
 
     this.opCode = opCode;
     this.operandOne = operandOne;
     this.operandTwo = operandTwo;
     this.id = id;
 
     switch (opCode) {
         case 0:
             this.opName = "multiplication";
             break;
         case 1:
             this.opName = "division";
             break;
         case 2:
             this.opName = "or";
             break;
         case 3:
             this.opName = "and";
             break;
         case 4:
             this.opName = "subtraction";
             break;
         case 5:
             this.opName = "addition";
             break;
         default:
             this.opName = "";
             break;
     }
 
     try {
         byte[] opNameBytes = this.opName.getBytes("UTF-16BE");
         this.tml = 7 + opNameBytes.length;
         this.opNameLength = opNameBytes.length;
 
         // // Print the operation name (for testing purposes)
         // System.out.println("Operation name: " + opNameBytes);
         // // Print the operation name length (for testing purposes)
         // System.out.println("Operation name length in bytes: " + this.opNameLength);
 
     } catch (UnsupportedEncodingException e) {
         e.printStackTrace();
     }
     
   }
 
   /* Computes operation codes.
    *
    * 0 = Multiplication
    * 1 = Division
    * 2 = OR Bitwise
    * 3 = AND Bitwise
    * 4 = Subtraction
    * 5 = Addition
    * 
    */
   public int compute() {
 
     switch (this.opCode) {
       case 0: // Multiplication
         return this.operandOne * this.operandTwo;
       case 1: // Division
         return this.operandOne / this.operandTwo;
       case 2: // OR Bitwise
         return this.operandOne | this.operandTwo;
       case 3: // AND Bitwise
         return this.operandOne & this.operandTwo;
       case 4: // Subtraction
         return this.operandOne - this.operandTwo;
       case 5: // Addition
         return this.operandOne + this.operandTwo;
       default:
         return -1;
     }
     
   }
 
 
   /*
    * Returns a string representation of the request.
    */
   public String toString() {
     String operator = "";
     switch (this.opCode) {
       case 0:
         operator = "*";
         break;
       case 1:
         operator = "/";
         break;
       case 2:
         operator = "|";
         break;
       case 3:
         operator = "&";
         break;
       case 4:
         operator = "-";
         break;
       case 5:
         operator = "+";
         break;
       default:
         operator = "Invalid Operation";
         break;
     }
 
     return "Request #" + this.id + ": " + this.operandOne + " "
     + operator + " " + this.operandTwo;
 
   }
   
 }
 