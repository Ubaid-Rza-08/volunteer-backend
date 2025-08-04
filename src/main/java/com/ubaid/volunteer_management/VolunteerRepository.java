package com.ubaid.volunteer_management;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {

    Optional<Volunteer> findByEmail(String email);

    List<Volunteer> findByNameContainingIgnoreCase(String name);

    List<Volunteer> findBySkillsContainingIgnoreCase(String skill);

    boolean existsByEmail(String email);
}
