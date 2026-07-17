package com.example.library.service;

import com.example.library.dto.BookLookupResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BookLookupService {

    private static final String GOOGLE_BOOKS_API = "https://www.googleapis.com/books/v1/volumes?q=isbn:";

    public BookLookupResponse lookupByIsbn(String isbn) {
        RestTemplate restTemplate = new RestTemplate();
        String url = GOOGLE_BOOKS_API + isbn.trim();

        try {
            String response = restTemplate.getForObject(url, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);

            if (!root.has("items") || root.get("items").isEmpty()) {
                throw new RuntimeException("No book found for ISBN: " + isbn);
            }

            JsonNode volumeInfo = root.get("items").get(0).path("volumeInfo");

            String title = volumeInfo.path("title").asText("Unknown Title");

            StringBuilder authors = new StringBuilder();
            if (volumeInfo.has("authors")) {
                volumeInfo.get("authors").forEach(a -> {
                    if (authors.length() > 0) authors.append(", ");
                    authors.append(a.asText());
                });
            }
            String author = authors.length() > 0 ? authors.toString() : "Unknown Author";

            String genre = "General";
            if (volumeInfo.has("categories") && volumeInfo.get("categories").size() > 0) {
                genre = volumeInfo.get("categories").get(0).asText();
            }

            String coverUrl = volumeInfo.path("imageLinks").path("thumbnail").asText(null);
            String description = volumeInfo.path("description").asText("");
            if (description.length() > 300) {
                description = description.substring(0, 300) + "...";
            }

            return new BookLookupResponse(title, author, genre, isbn, coverUrl, description);

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to look up ISBN: " + e.getMessage());
        }
    }
}