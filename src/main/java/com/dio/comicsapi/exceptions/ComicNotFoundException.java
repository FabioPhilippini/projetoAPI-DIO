package com.dio.comicsapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ComicNotFoundException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public ComicNotFoundException(String comicName) {
	        super(String.format("Comic with name %s not found in the system.", comicName));
	    }

	    public ComicNotFoundException(Long id) {
	        super(String.format("Comic with id %s not found in the system.", id));
	    }
	
	

}
