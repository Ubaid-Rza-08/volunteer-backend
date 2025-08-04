package com.ubaid.volunteer_management.intern;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InternRepository extends JpaRepository<Intern, Long> {

    Optional<Intern> findByEmail(String email);

    List<Intern> findByNameContainingIgnoreCase(String name);

    List<Intern> findByUniversityContainingIgnoreCase(String university);

    List<Intern> findByDepartmentContainingIgnoreCase(String department);

    List<Intern> findByStartDateBetween(LocalDate startDate, LocalDate endDate);

    boolean existsByEmail(String email);
}
