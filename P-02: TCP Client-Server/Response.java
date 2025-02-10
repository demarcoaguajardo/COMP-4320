
/*
 * Response.java
 * 
 * Class to represent a Response object
 * 
 */
public class Response {

    public Integer tml; // Total Message Length (in bytes)
    public Integer result; // Result
    public Integer errorCode; // Error code (0 = no error, 127 = error)
    public Short id; // Request ID
    
    public Response(int result, int errorCode, short id) {
        this.tml = 8; //
        this.result = result;
        this.errorCode = errorCode;
        this.id = id;
    }


    /*
     * Returns a string representation of the response.
     */
    public String toString() {
        if (this.errorCode == 0) {
            return "Request #" + this.id + ": " + "Result: " + 
            this.result + " (Ok)";
        }
        else { 
            return "Request #" + this.id + ": " + "Result: " +
            this.result + " (error: " + this.errorCode + ")";
        }
    }
}