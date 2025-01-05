package com.zonesoft.addressbook.exceptions;

public class AddressbookException extends RuntimeException {
	private static final long serialVersionUID = 3365111151656618159L;
	
	public AddressbookException() {
		    super();
	}

	public AddressbookException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		    super(message, cause, enableSuppression, writableStackTrace);
	}

	public AddressbookException(String message, Throwable cause) {
		    super(message, cause);
	}

	public AddressbookException(String message) {
		    super(message);
	}

	public AddressbookException(Throwable cause) {
		    super(cause);
	}

}