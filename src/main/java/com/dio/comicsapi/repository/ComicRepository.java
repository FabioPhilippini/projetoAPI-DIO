package com.dio.comicsapi.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dio.comicsapi.entity.Comic;

public interface ComicRepository extends JpaRepository<Comic,Long> {

	Optional<Comic>findByName(String name);
}
