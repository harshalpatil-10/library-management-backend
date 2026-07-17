package com.example.library.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime requestedAt;
    private LocalDateTime fulfilledAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status { WAITING, FULFILLED, CANCELLED }

    public Reservation() {}

    public Reservation(Book book, Member member) {
        this.book = book;
        this.member = member;
        this.requestedAt = LocalDateTime.now();
        this.status = Status.WAITING;
    }

    public Long getId() { return id; }
    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }
    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }
    public LocalDateTime getRequestedAt() { return requestedAt; }
    public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }
    public LocalDateTime getFulfilledAt() { return fulfilledAt; }
    public void setFulfilledAt(LocalDateTime fulfilledAt) { this.fulfilledAt = fulfilledAt; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}