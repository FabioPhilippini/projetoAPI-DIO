package com.dio.comicsapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.dio.comicsapi.dto.ComicDTO;
import com.dio.comicsapi.entity.Comic;

@Mapper
public interface ComicMapper {
	
	ComicMapper INSTANCE = Mappers.getMapper(ComicMapper.class);
	
	Comic toModel(ComicDTO comicDTO);
	
	ComicDTO toDTO(Comic comic);

}
