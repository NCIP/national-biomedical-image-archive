package gov.nih.nci.nbia.exception;

public class DataAccessException extends RuntimeException {

  public DataAccessException() {
    super();
  }
  
  public DataAccessException(String message) {
    super(message);
  }
  
  public DataAccessException(String message, Throwable cause) {
    super(message, cause);
  }
}
