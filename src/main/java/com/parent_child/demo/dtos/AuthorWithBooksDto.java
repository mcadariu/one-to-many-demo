package com.parent_child.demo.dtos;

import java.util.List;

public class AuthorWithBooksDto {
    private Long id;
    private String name;
    private String bio;
    private List<BookDto> books;

    public AuthorWithBooksDto(Long id, String name, String bio, List<BookDto> books) {
        this.id = id;
        this.name = name;
        this.bio = bio;
        this.books = books;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<BookDto> getBooks() {
        return books;
    }

    public void setBooks(List<BookDto> books) {
        this.books = books;
    }
}
