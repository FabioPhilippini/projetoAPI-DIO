package com.dio.comicsapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Publisher {
	
	DC("DC"),
	MARVEL("MARVEL");
	
	private final String description;

}
