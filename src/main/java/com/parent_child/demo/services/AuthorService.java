package com.parent_child.demo.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parent_child.demo.dtos.AuthorWithBooksDto;
import com.parent_child.demo.dtos.BookDto;
import com.parent_child.demo.entities.Author;
import com.parent_child.demo.entities.Book;
import com.parent_child.demo.repositories.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<AuthorWithBooksDto> getAllAuthorsWithBooksNplus1() {
        List<Author> authors = authorRepository.findAll(); // 1 query

        return authors.stream()
                .map(author -> {
                    List<Book> books = author.getBooks(); // N queries
                    return new AuthorWithBooksDto(
                            author.getId(),
                            author.getName(),
                            author.getBio(),
                            books.stream()
                                    .map(book -> new BookDto(book.getTitle(), book.getIsbn(), book.getPublishedYear()))
                                    .collect(Collectors.toList())
                    );
                })
                .collect(Collectors.toList());


    }

    public List<AuthorWithBooksDto> getAllAuthorsWithBooksJoinFetch() {
        List<Author> authors = authorRepository.findAllWithBooks(); // Just 1 query now!

        return authors.stream()
                .map(author -> new AuthorWithBooksDto(
                        author.getId(),
                        author.getName(),
                        author.getBio(),
                        author.getBooks().stream()
                                .map(book -> new BookDto(book.getTitle(), book.getIsbn(), book.getPublishedYear()))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    public List<AuthorWithBooksDto> getAllAuthorsWithBooksJson() {
        List<Object[]> results = authorRepository.findAllWithBooksAsJson();

        return results.stream()
                .map(row -> {
                    Long id = ((Number) row[0]).longValue();
                    String name = (String) row[1];
                    String bio = (String) row[2];
                    JsonNode booksNode = parseJsonNode((String) row[3]);

                    List<BookDto> books = new ArrayList<>();
                    if (booksNode.isArray()) {
                        for (JsonNode bookNode : booksNode) {
                            BookDto bookDto = new BookDto(
                                    bookNode.get("title").asText(),
                                    bookNode.get("isbn").asText(),
                                    bookNode.get("publishedYear").asInt()
                            );
                            books.add(bookDto);
                        }
                    }

                    return new AuthorWithBooksDto(id, name, bio, books);
                })
                .collect(Collectors.toList());
    }

    private JsonNode parseJsonNode(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse JSON", e);
        }
    }
}
