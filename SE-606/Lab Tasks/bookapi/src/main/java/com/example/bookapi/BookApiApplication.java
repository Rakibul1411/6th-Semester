package com.example.bookapi;

import com.example.bookapi.application.interactor.BookInteractor;
import com.example.bookapi.domain.repository.BookRepository;
import com.example.bookapi.domain.usecase.BookUseCase;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(BookApiApplication.class, args);
	}

	@Bean
	public BookUseCase bookUseCase(BookRepository repo) {
		return new BookInteractor(repo);
	}
}

