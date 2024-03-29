package com.dio.comicsapi.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.dio.comicsapi.enums.Publisher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComicDTO {

    private Long id;
	
	@NotNull
    @Size(min = 1, max = 200)
    private String name;
	
	@NotNull
	@Size(min = 1, max = 200)
	private String authors;
	
	@NotNull
    @Max(500)
    private Integer max;

    @NotNull
    @Max(100)
    private Integer quantity;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    private Publisher publisher;
}
