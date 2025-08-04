package com.ubaid.volunteer_management;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class InternService {

    @Autowired
    private InternRepository internRepository;

    public List<Intern> getAllInterns() {
        return internRepository.findAll();
    }

    public Optional<Intern> getInternById(Long id) {
        return internRepository.findById(id);
    }

    public Optional<Intern> getInternByEmail(String email) {
        return internRepository.findByEmail(email);
    }

    public List<Intern> searchByName(String name) {
        return internRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Intern> searchByUniversity(String university) {
        return internRepository.findByUniversityContainingIgnoreCase(university);
    }

    public List<Intern> searchByDepartment(String department) {
        return internRepository.findByDepartmentContainingIgnoreCase(department);
    }

    public List<Intern> getInternsByDateRange(LocalDate startDate, LocalDate endDate) {
        return internRepository.findByStartDateBetween(startDate, endDate);
    }

    public Intern saveIntern(Intern intern) {
        // Set the current user as the creator
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            User currentUser = (User) authentication.getPrincipal();
            intern.setCreatedBy(currentUser);
        }

        return internRepository.save(intern);
    }

    public Intern updateIntern(Long id, Intern internDetails) {
        Optional<Intern> optionalIntern = internRepository.findById(id);

        if (optionalIntern.isPresent()) {
            Intern intern = optionalIntern.get();
            intern.setName(internDetails.getName());
            intern.setEmail(internDetails.getEmail());
            intern.setPhoneNumber(internDetails.getPhoneNumber());
            intern.setUniversity(internDetails.getUniversity());
            intern.setMajor(internDetails.getMajor());
            intern.setStartDate(internDetails.getStartDate());
            intern.setEndDate(internDetails.getEndDate());
            intern.setDepartment(internDetails.getDepartment());

            return internRepository.save(intern);
        }

        return null;
    }

    public boolean deleteIntern(Long id) {
        if (internRepository.existsById(id)) {
            internRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean existsByEmail(String email) {
        return internRepository.existsByEmail(email);
    }
}
