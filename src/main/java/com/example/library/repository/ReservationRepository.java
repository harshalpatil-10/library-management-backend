package com.example.library.repository;

import com.example.library.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByBookIdAndStatusOrderByRequestedAtAsc(Long bookId, Reservation.Status status);
    List<Reservation> findByMemberIdOrderByRequestedAtDesc(Long memberId);
    Optional<Reservation> findFirstByBookIdAndStatusOrderByRequestedAtAsc(Long bookId, Reservation.Status status);
    boolean existsByBookIdAndMemberIdAndStatus(Long bookId, Long memberId, Reservation.Status status);
}