package com.dio.comicsapi.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dio.comicsapi.builder.ComicDTOBuilder;
import com.dio.comicsapi.dto.ComicDTO;
import com.dio.comicsapi.entity.Comic;
import com.dio.comicsapi.exceptions.ComicAlreadyRegisteredException;
import com.dio.comicsapi.exceptions.ComicNotFoundException;
import com.dio.comicsapi.mapper.ComicMapper;
import com.dio.comicsapi.repository.ComicRepository;


@ExtendWith(MockitoExtension.class)
public class ComicServiceTest {
	
	private static final long INVALID_COMIC_ID = 1L;
	
	@Mock
	private ComicRepository comicRepository;
	
	private ComicMapper comicMapper = ComicMapper.INSTANCE;
	
	@InjectMocks
	private ComicService comicService;
	
	void whenComicInformedThenItShouldBeCreated() throws ComicAlreadyRegisteredException {
		//given
		ComicDTO expectedComicDTO = ComicDTOBuilder.builder().build().toComicsDTO();	
		Comic expectedSavedComic = comicMapper.toModel(expectedComicDTO);
		
		// when
        Mockito.when(comicRepository.findByName(expectedComicDTO.getName())).thenReturn(Optional.empty());
        Mockito.when(comicRepository.save(expectedSavedComic)).thenReturn(expectedSavedComic);
        
      //then
        ComicDTO createdComicsDTO = comicService.createComic(expectedComicDTO);

        MatcherAssert.assertThat(createdComicsDTO.getId(), Matchers.is(Matchers.equalTo(expectedComicDTO.getId())));
        MatcherAssert.assertThat(createdComicsDTO.getName(),  Matchers.is( Matchers.equalTo(expectedComicDTO.getName())));
        MatcherAssert.assertThat(createdComicsDTO.getQuantity(),  Matchers.is( Matchers.equalTo(expectedComicDTO.getQuantity())));
	}
	
	@Test
    void whenAlreadyRegisteredComicInformedThenAnExceptionShouldBeThrown() {
        // given
        ComicDTO expectedComicDTO = ComicDTOBuilder.builder().build().toComicsDTO();
        Comic duplicatedComic = comicMapper.toModel(expectedComicDTO);

        // when
        Mockito.when(comicRepository.findByName(expectedComicDTO.getName())).thenReturn(Optional.of(duplicatedComic));

        // then
        assertThrows(ComicAlreadyRegisteredException.class, () -> comicService.createComic(expectedComicDTO));
    }
	
	@Test
    void whenValidComicNameIsGivenThenReturnAComic() throws ComicNotFoundException {
        // given
        ComicDTO expectedFoundComicDTO = ComicDTOBuilder.builder().build().toComicsDTO();
        Comic expectedFoundComic = comicMapper.toModel(expectedFoundComicDTO);

        // when
        when(comicRepository.findByName(expectedFoundComic.getName())).thenReturn(Optional.of(expectedFoundComic));

        // then
        ComicDTO foundComicDTO = comicService.findByName(expectedFoundComicDTO.getName());

        assertThat(foundComicDTO, is(equalTo(expectedFoundComicDTO)));
    }
	
	 @Test
	 void whenNotRegisteredComicNameIsGivenThenThrowAnException() {
	    // given
	    ComicDTO expectedFoundComicDTO = ComicDTOBuilder.builder().build().toComicsDTO();

	    // when
	    when(comicRepository.findByName(expectedFoundComicDTO.getName())).thenReturn(Optional.empty());

	    // then
	    assertThrows(ComicNotFoundException.class, () -> comicService.findByName(expectedFoundComicDTO.getName()));
	 }

}
