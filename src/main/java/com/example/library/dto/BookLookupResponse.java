package com.example.library.dto;

public class BookLookupResponse {
    private String title;
    private String author;
    private String genre;
    private String isbn;
    private String coverImageUrl;
    private String description;

    public BookLookupResponse(String title, String author, String genre, String isbn, String coverImageUrl, String description) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isbn = isbn;
        this.coverImageUrl = coverImageUrl;
        this.description = description;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getGenre() { return genre; }
    public String getIsbn() { return isbn; }
    public String getCoverImageUrl() { return coverImageUrl; }
    public String getDescription() { return description; }
}