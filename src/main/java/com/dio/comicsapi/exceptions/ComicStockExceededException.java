package com.dio.comicsapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ComicStockExceededException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public ComicStockExceededException(Long id, int quantityToIncrement) {
	        super(String.format("Comic with ID:%s to increment exceeds the max stock capacity. Quantity requested to increment: %s", id, quantityToIncrement));
	    }

}
