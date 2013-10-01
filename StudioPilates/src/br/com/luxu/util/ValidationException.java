package br.com.luxu.util;

import java.util.ArrayList;
import java.util.Collection;

public class ValidationException extends RuntimeException {
    /** 
     * 
     */
    private static final long serialVersionUID = -4111097815415667821L;
    
    private Collection<ErrorInfo> errors = new ArrayList<ErrorInfo>();

    public ValidationException() {
	super();
    }

    public ValidationException(String message) {
	super(message);
	addError(message, null);
    }

    public ValidationException(String fieldName, String message, Object[] args) {
	super(message);
	addError(fieldName, message, args);
    }

    public void addError(String name, String message, Object[] args) {
	errors.add(new ErrorInfo(name, message, args));
    }

    public void addError(String message, Object[] args) {
	errors.add(new ErrorInfo(null, message, args));
    }

    public Collection<ErrorInfo> getFieldErrors() {
	return errors;
    }

    public boolean hasError() {
	return !errors.isEmpty();
    }
}
