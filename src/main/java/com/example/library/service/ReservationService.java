package com.example.library.service;

import com.example.library.model.*;
import com.example.library.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReservationService {

    @Autowired private ReservationRepository reservationRepository;
    @Autowired private BookRepository bookRepository;
    @Autowired private MemberRepository memberRepository;

    public Reservation reserveBook(Long bookId, Long memberId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        if (book.getAvailableCopies() > 0) {
            throw new RuntimeException("This book has copies available right now — no need to reserve, just borrow it.");
        }

        if (reservationRepository.existsByBookIdAndMemberIdAndStatus(bookId, memberId, Reservation.Status.WAITING)) {
            throw new RuntimeException("You're already in the queue for this book.");
        }

        Reservation reservation = new Reservation(book, member);
        return reservationRepository.save(reservation);
    }

    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        reservation.setStatus(Reservation.Status.CANCELLED);
        reservationRepository.save(reservation);
    }

    public List<Reservation> getQueueForBook(Long bookId) {
        return reservationRepository.findByBookIdAndStatusOrderByRequestedAtAsc(bookId, Reservation.Status.WAITING);
    }

    public List<Reservation> getMyReservations(Long memberId) {
        return reservationRepository.findByMemberIdOrderByRequestedAtDesc(memberId);
    }

    // Called by BorrowService whenever a book is returned - fairly offers it to
    // the longest-waiting person in the queue instead of leaving it as free-for-all.
    public Reservation fulfillNextInQueue(Long bookId) {
        return reservationRepository
                .findFirstByBookIdAndStatusOrderByRequestedAtAsc(bookId, Reservation.Status.WAITING)
                .orElse(null);
    }

    public void markFulfilled(Reservation reservation) {
        reservation.setStatus(Reservation.Status.FULFILLED);
        reservation.setFulfilledAt(java.time.LocalDateTime.now());
        reservationRepository.save(reservation);
    }
}