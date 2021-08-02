package com.dio.comicsapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ComicWithInsufficientStockException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public ComicWithInsufficientStockException(Long id, int quantityInStock, int quantityToDecrement) {
		super(String.format("Comic with ID:%s has insufficient stock. Stock: %d. Quantity requested to decrement: %d ", id,quantityInStock, quantityToDecrement ));
	    }

}
