package com.dio.comicsapi.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dio.comicsapi.dto.ComicDTO;
import com.dio.comicsapi.entity.Comic;
import com.dio.comicsapi.exceptions.ComicAlreadyRegisteredException;
import com.dio.comicsapi.exceptions.ComicNotFoundException;
import com.dio.comicsapi.mapper.ComicMapper;
import com.dio.comicsapi.repository.ComicRepository;



@Service
public class ComicService {

	private ComicRepository comicRepository;
	
	private final ComicMapper comicMapper = ComicMapper.INSTANCE;
	
	public ComicService(ComicRepository comicRepository) {
		this.comicRepository = comicRepository;
	}
	
	public ComicDTO createComic(ComicDTO comicDTO) throws ComicAlreadyRegisteredException {
		verifyIfIsAlreadyRegistered(comicDTO.getName());
		Comic comic = comicMapper.toModel(comicDTO);
		Comic savedComic = comicRepository.save(comic);
		return comicMapper.toDTO(savedComic);
	}
	
	public ComicDTO findByName(String name) throws ComicNotFoundException {
		Comic foundComic = comicRepository.findByName(name).orElseThrow(() -> new ComicNotFoundException(name));
		return comicMapper.toDTO(foundComic);
	}
	
	public List<ComicDTO> listAll(){
		return comicRepository.findAll().stream().map(comicMapper::toDTO).collect(Collectors.toList());
	}
	
	public void deleteById(Long id) throws ComicNotFoundException {
		verifyIfExist(id);
		comicRepository.deleteById(id);
	}

	private void verifyIfExist(Long id) throws ComicNotFoundException {
		comicRepository.findById(id).orElseThrow(() -> new ComicNotFoundException(id));
		
	}

	private void verifyIfIsAlreadyRegistered(String name) throws ComicAlreadyRegisteredException {
		Optional<Comic> optSavedComic = comicRepository.findByName(name);
		if (optSavedComic.isPresent()) {
			throw new ComicAlreadyRegisteredException(name);
		}		
	}
}
