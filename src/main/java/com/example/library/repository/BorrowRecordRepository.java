package com.example.library.repository;

import com.example.library.model.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    // Custom query methods - Spring Data JPA auto-generates the SQL from the method name
    List<BorrowRecord> findByMemberId(Long memberId);
    List<BorrowRecord> findByReturnDateIsNull();
}