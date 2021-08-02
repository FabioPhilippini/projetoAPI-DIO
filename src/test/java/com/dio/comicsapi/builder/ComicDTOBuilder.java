package com.dio.comicsapi.builder;


import com.dio.comicsapi.dto.ComicDTO;
import com.dio.comicsapi.enums.Publisher;
import lombok.Builder;

@Builder
public class ComicDTOBuilder {

	@Builder.Default
	private Long id = 1L;
	
	@Builder.Default
	private String name = "Spider-Man: Kraven's Last Hunt";
	
	@Builder.Default
	private String authors = "J.M. DeMatteis, Mike Zeck";
	
	@Builder.Default
	private int max = 20;
	
	@Builder.Default
	private int quantity = 10;
	
	@Builder.Default
	private Publisher publisher = Publisher.MARVEL;
	
	public ComicDTO toComicsDTO() {
        return new ComicDTO(id,
                name,
                authors,
                max,
                quantity,
                publisher);
    }
}
