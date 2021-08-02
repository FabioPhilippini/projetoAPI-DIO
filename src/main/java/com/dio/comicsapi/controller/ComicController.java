package com.dio.comicsapi.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dio.comicsapi.dto.ComicDTO;
import com.dio.comicsapi.exceptions.ComicAlreadyRegisteredException;
import com.dio.comicsapi.exceptions.ComicNotFoundException;
import com.dio.comicsapi.service.ComicService;

@RestController
@RequestMapping("api/v1/comic")
public class ComicController {

	private ComicService comicService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ComicDTO createComic(@RequestBody @Valid ComicDTO comicDTO) throws ComicAlreadyRegisteredException{
		return comicService.createComic(comicDTO);
	}
	
	@GetMapping("/{name}")
	public ComicDTO findByName(@PathVariable String name) throws ComicNotFoundException {
		return comicService.findByName(name);
	}
	
	@GetMapping
	public List<ComicDTO> listComic() {
		return comicService.listAll();
	}
	
	@DeleteMapping("/{id{")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteById(@PathVariable Long id) throws ComicNotFoundException {
		comicService.deleteById(id);
	}
	
}