package com.parent_child.demo.controllers;

import com.parent_child.demo.dtos.AuthorWithBooksDto;
import com.parent_child.demo.services.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/n-plus-one")
    public ResponseEntity<List<AuthorWithBooksDto>> getAuthorsWithBooksNPlusOne() {
        List<AuthorWithBooksDto> authors = authorService.getAllAuthorsWithBooksNplus1();
        return ok(authors);
    }

    @GetMapping("/join-fetch")
    public ResponseEntity<List<AuthorWithBooksDto>> getAuthorsWithBooksJoinFetch() {
        List<AuthorWithBooksDto> authors = authorService.getAllAuthorsWithBooksJoinFetch();
        return ok(authors);
    }

    @GetMapping("/jsonb-agg")
    public ResponseEntity<List<AuthorWithBooksDto>> getAuthorsWithBooksJsonbAgg() {
        List<AuthorWithBooksDto> authors = authorService.getAllAuthorsWithBooksJson();
        return ok(authors);
    }
}
