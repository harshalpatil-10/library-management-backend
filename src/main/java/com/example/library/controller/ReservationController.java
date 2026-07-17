package com.example.library.controller;

import com.example.library.dto.ReservationRequest;
import com.example.library.model.Reservation;
import com.example.library.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public Reservation reserve(@RequestBody ReservationRequest request) {
        return reservationService.reserveBook(request.getBookId(), request.getMemberId());
    }

    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return "Reservation cancelled";
    }

    @GetMapping("/book/{bookId}")
    public List<Reservation> getQueueForBook(@PathVariable Long bookId) {
        return reservationService.getQueueForBook(bookId);
    }

    @GetMapping("/member/{memberId}")
    public List<Reservation> getMyReservations(@PathVariable Long memberId) {
        return reservationService.getMyReservations(memberId);
    }
}