package com.example.library.service;

import com.example.library.model.Book;
import com.example.library.model.BorrowRecord;
import com.example.library.model.Member;
import com.example.library.repository.BookRepository;
import com.example.library.repository.BorrowRecordRepository;
import com.example.library.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BorrowService {

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    private static final int LOAN_PERIOD_DAYS = 14;
    private static final double FINE_PER_DAY = 5.0;

    public BorrowRecord borrowBook(Long bookId, Long memberId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException("No copies available for this book right now");
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        LocalDate today = LocalDate.now();
        LocalDate dueDate = today.plusDays(LOAN_PERIOD_DAYS);

        BorrowRecord record = new BorrowRecord(book, member, today, dueDate);
        return borrowRecordRepository.save(record);
    }

    public BorrowRecord returnBook(Long borrowRecordId) {
        BorrowRecord record = borrowRecordRepository.findById(borrowRecordId)
                .orElseThrow(() -> new RuntimeException("Borrow record not found"));

        LocalDate today = LocalDate.now();
        record.setReturnDate(today);

        if (today.isAfter(record.getDueDate())) {
            long daysLate = ChronoUnit.DAYS.between(record.getDueDate(), today);
            record.setFineAmount(daysLate * FINE_PER_DAY);
        }

        Book book = record.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return borrowRecordRepository.save(record);
    }

    public List<BorrowRecord> getHistoryForMember(Long memberId) {
        return borrowRecordRepository.findByMemberId(memberId);
    }

    public List<BorrowRecord> getOverdueBooks() {
        LocalDate today = LocalDate.now();
        return borrowRecordRepository.findByReturnDateIsNull().stream()
                .filter(r -> r.getDueDate().isBefore(today))
                .collect(java.util.stream.Collectors.toList());
    }
}