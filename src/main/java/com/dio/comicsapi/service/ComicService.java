package com.dio.comicsapi.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dio.comicsapi.dto.ComicDTO;
import com.dio.comicsapi.entity.Comic;
import com.dio.comicsapi.exceptions.ComicAlreadyRegisteredException;
import com.dio.comicsapi.exceptions.ComicNotFoundException;
import com.dio.comicsapi.exceptions.ComicStockExceededException;
import com.dio.comicsapi.exceptions.ComicWithInsufficientStockException;
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
	
	public ComicDTO increment(Long id, int quantityToIncrement) throws ComicNotFoundException, ComicStockExceededException {
		Comic comicToIncrementStock = verifyIfExist(id);
		int quantityAfterIncrement = quantityToIncrement + comicToIncrementStock.getQuantity();
		if (quantityAfterIncrement <= comicToIncrementStock.getMax()) {
			comicToIncrementStock.setQuantity(comicToIncrementStock.getQuantity() + quantityToIncrement);
			Comic incrementedComicStock = comicRepository.save(comicToIncrementStock);
			return comicMapper.toDTO(incrementedComicStock);
		}
		throw new ComicStockExceededException(id, quantityToIncrement);
	}
	
	public ComicDTO decrement(Long id, int quantityToDecrement) throws ComicNotFoundException, ComicWithInsufficientStockException {
		Comic comicToDecrementStock = verifyIfExist(id);
		int quantityInStock = comicToDecrementStock.getQuantity();
		int quantityAfterDecrement =  comicToDecrementStock.getQuantity() - quantityToDecrement;
		if (quantityAfterDecrement <= comicToDecrementStock.getMax()) {
			comicToDecrementStock.setQuantity(comicToDecrementStock.getQuantity() - quantityToDecrement);
			Comic decrementedComicStock = comicRepository.save(comicToDecrementStock);
			return comicMapper.toDTO(decrementedComicStock);
		}
		throw new ComicWithInsufficientStockException(id,quantityInStock,quantityToDecrement);
	}
	
	private Comic verifyIfExist(Long id) throws ComicNotFoundException {
		return comicRepository.findById(id).orElseThrow(() -> new ComicNotFoundException(id));	
	}

	private void verifyIfIsAlreadyRegistered(String name) throws ComicAlreadyRegisteredException {
		Optional<Comic> optSavedComic = comicRepository.findByName(name);
		if (optSavedComic.isPresent()) {
			throw new ComicAlreadyRegisteredException(name);
		}		
	}
	
	
}
