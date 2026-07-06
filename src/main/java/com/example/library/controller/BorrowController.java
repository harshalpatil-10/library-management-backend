package com.example.library.controller;

import com.example.library.model.BorrowRecord;
import com.example.library.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class BorrowController {

    @Autowired
    private BorrowService borrowService;

    @PostMapping("/borrow")
    public BorrowRecord borrowBook(@RequestParam Long bookId, @RequestParam Long memberId) {
        return borrowService.borrowBook(bookId, memberId);
    }

    @PostMapping("/return/{borrowRecordId}")
    public BorrowRecord returnBook(@PathVariable Long borrowRecordId) {
        return borrowService.returnBook(borrowRecordId);
    }

    @GetMapping("/borrow/history/{memberId}")
    public List<BorrowRecord> getHistory(@PathVariable Long memberId) {
        return borrowService.getHistoryForMember(memberId);
    }

    @GetMapping("/borrow/overdue")
    public List<BorrowRecord> getOverdueBooks() {
        return borrowService.getOverdueBooks();
    }
}