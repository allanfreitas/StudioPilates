package br.com.luxu.DAO;

public class HibernateException extends Exception {
    
    private static final long serialVersionUID = -3076694111010684064L;  
    
    public HibernateException() {  
          
    }  
  
    public HibernateException(String message) {  
        super(message);  
          
    }  
  
    public HibernateException(Throwable cause) {  
        super(cause);  
          
    }  
  
    public HibernateException(String message, Throwable cause) {  
        super(message, cause);  
          
    }
    
    
}
