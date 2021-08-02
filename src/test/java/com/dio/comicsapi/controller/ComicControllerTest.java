package com.dio.comicsapi.controller;

import static com.dio.comicsapi.utils.JsonConvertionUtils.asJsonString;
import static com.hqsapi.dio.springapi.utils.JsonConvertionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.dio.comicsapi.builder.ComicDTOBuilder;
import com.dio.comicsapi.dto.ComicDTO;
import com.dio.comicsapi.dto.QuantityDTO;
import com.dio.comicsapi.exceptions.ComicNotFoundException;
import com.dio.comicsapi.service.ComicService;



@ExtendWith(MockitoExtension.class)
public class ComicControllerTest {
	
	 private static final String COMIC_API_URL_PATH = "/api/v1/comics";
	 private static final long VALID_COMIC_ID = 1L;
	 private static final long INVALID_COMIC_ID = 2l;
	 private static final String COMIC_API_SUBPATH_INCREMENT_URL = "/increment";
	 private static final String COMIC_API_SUBPATH_DECREMENT_URL = "/decrement";

	 private MockMvc mockMvc;

	 @Mock
	 private ComicService comicService;

	 @InjectMocks
	 private ComicController comicController;
	    
	 @BeforeEach
	 void setUp() {
	     mockMvc = MockMvcBuilders.standaloneSetup(comicController)
	              .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
	              .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
	              .build();
	  }
	 
	 @Test
	    void whenPOSTIsCalledThenAComicIsCreated() throws Exception {
	        // given
	        ComicDTO comicDTO = ComicDTOBuilder.builder().build().toComicsDTO();

	        // when
	        when(comicService.createComic(comicDTO)).thenReturn(comicDTO);

	        // then
	        mockMvc.perform(post(COMIC_API_URL_PATH)
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(asJsonString(comicDTO)))
	                .andExpect(status().isCreated())
	                .andExpect(jsonPath("$.name", is(comicDTO.getName())))
	                .andExpect(jsonPath("$.authors", is(comicDTO.getAuthors())))
	                .andExpect(jsonPath("$.publisher", is(comicDTO.getPublisher().toString())));
	    }
	    
	    @Test
	    void whenPOSTIsCalledWithoutRequiredFiledThenAErrorIsReturned() throws Exception {
	        // given
	        ComicDTO comicDTO = ComicDTOBuilder.builder().build().toComicsDTO();
	        comicDTO.setAuthors(null);

	        // then
	        mockMvc.perform(post(COMIC_API_URL_PATH)
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(asJsonString(comicDTO)))
	                .andExpect(status().isBadRequest());
	    }
	    
	    @Test
	    void whenGETIsCalledWithValidNameThenOkStatusIsReturned() throws Exception {
	        // given
	        ComicDTO comicDTO = ComicDTOBuilder.builder().build().toComicsDTO();

	        //when
	        when(comicService.findByName(comicDTO.getName())).thenReturn(comicDTO);

	        // then
	        mockMvc.perform(MockMvcRequestBuilders.get(COMIC_API_URL_PATH + "/" + comicDTO.getName())
	                .contentType(MediaType.APPLICATION_JSON))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.name", is(comicDTO.getName())))
	                .andExpect(jsonPath("$.authors", is(comicDTO.getAuthors())))
	                .andExpect(jsonPath("$.publisher", is(comicDTO.getPublisher().toString())));
	    }
	    
	    @Test
	    void whenGETIsCalledWithoutRegisteredNameThenNotFoundStatusIsReturned() throws Exception {
	        // given
	        ComicDTO comicDTO = ComicDTOBuilder.builder().build().toComicsDTO();

	        //when
	        when(comicService.findByName(comicDTO.getName())).thenThrow(ComicNotFoundException.class);

	        // then
	        mockMvc.perform(MockMvcRequestBuilders.get(COMIC_API_URL_PATH + "/" + comicDTO.getName())
	                .contentType(MediaType.APPLICATION_JSON))
	                .andExpect(status().isNotFound());
	    }
	    
	    @Test
	    void whenGETListWithComicsIsCalledThenOkStatusIsReturned() throws Exception {
	        // given
	        ComicDTO comicDTO = ComicDTOBuilder.builder().build().toComicsDTO();

	        //when
	        when(comicService.listAll()).thenReturn(Collections.singletonList(comicDTO));

	        // then
	        mockMvc.perform(MockMvcRequestBuilders.get(COMIC_API_URL_PATH)
	                .contentType(MediaType.APPLICATION_JSON))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$[0].name", is(comicDTO.getName())))
	                .andExpect(jsonPath("$[0].authors", is(comicDTO.getAuthors())))
	                .andExpect(jsonPath("$[0].publisher", is(comicDTO.getPublisher().toString())));
	    }
	    
	    @Test
	    void whenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
	        // given
	        ComicDTO comicDTO = ComicDTOBuilder.builder().build().toComicsDTO();

	        //when
	        doNothing().when(comicService).deleteById(comicDTO.getId());

	        // then
	        mockMvc.perform(MockMvcRequestBuilders.delete(COMIC_API_URL_PATH + "/" + comicDTO.getId())
	                .contentType(MediaType.APPLICATION_JSON))
	                .andExpect(status().isNoContent());
	    }
	    
	    @Test
	    void whenPATCHIsCalledToIncrementDiscountThenOKstatusIsReturned() throws Exception {
	        //given
	    	QuantityDTO quantityDTO = QuantityDTO.builder()
	                .quantity(10)
	                .build();

	        ComicDTO comicDTO = ComicDTOBuilder.builder().build().toComicsDTO();
	        comicDTO.setQuantity(comicDTO.getQuantity() + quantityDTO.getQuantity());
            
	        //when
	        when(comicService.increment(VALID_COMIC_ID, quantityDTO.getQuantity())).thenReturn(comicDTO);

	        //then
	        mockMvc.perform(MockMvcRequestBuilders.patch(COMIC_API_URL_PATH + "/" + VALID_COMIC_ID + COMIC_API_SUBPATH_INCREMENT_URL)
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(asJsonString(quantityDTO))).andExpect(status().isOk())
	                .andExpect(jsonPath("$.name", is(comicDTO.getName())))
	                .andExpect(jsonPath("$.authors", is(comicDTO.getAuthors())))
	                .andExpect(jsonPath("$.publisher", is(comicDTO.getPublisher().toString())))
	                .andExpect(jsonPath("$.quantity", is(comicDTO.getQuantity())));
	    }

}
