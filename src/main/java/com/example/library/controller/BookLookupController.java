package com.example.library.controller;

import com.example.library.dto.BookLookupResponse;
import com.example.library.service.BookLookupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/books")
public class BookLookupController {

    @Autowired
    private BookLookupService bookLookupService;

    @GetMapping("/lookup/{isbn}")
    public ResponseEntity<?> lookup(@PathVariable String isbn) {
        try {
            return ResponseEntity.ok(bookLookupService.lookupByIsbn(isbn));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}