package br.com.luxu.util;

public class ErrorInfo {
    
    private String fieldName;
    private String message;
    private Object[] args;

    public ErrorInfo(String name, String message, Object[] args) {
	this.fieldName = name;
	this.message = message;
	this.args = args;
    }

    public String getFieldName() {
	return this.fieldName;
    }

    public String getMessage() {
	return this.message;
    }

    public Object[] getArgs() {
	return args;
    }
}
