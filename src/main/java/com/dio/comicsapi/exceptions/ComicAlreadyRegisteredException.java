package com.dio.comicsapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ComicAlreadyRegisteredException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public ComicAlreadyRegisteredException(String comicName) {
        super(String.format("Comic with name %s already registered in the system.", comicName));
    }

}
