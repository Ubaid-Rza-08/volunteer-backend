package com.ubaid.volunteer_management;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VolunteerService {

    @Autowired
    private VolunteerRepository volunteerRepository;

    public List<Volunteer> getAllVolunteers() {
        return volunteerRepository.findAll();
    }

    public Optional<Volunteer> getVolunteerById(Long id) {
        return volunteerRepository.findById(id);
    }

    public Optional<Volunteer> getVolunteerByEmail(String email) {
        return volunteerRepository.findByEmail(email);
    }

    public List<Volunteer> searchByName(String name) {
        return volunteerRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Volunteer> searchBySkill(String skill) {
        return volunteerRepository.findBySkillsContainingIgnoreCase(skill);
    }

    public Volunteer saveVolunteer(Volunteer volunteer) {
        // Set the current user as the creator
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            User currentUser = (User) authentication.getPrincipal();
            volunteer.setCreatedBy(currentUser);
        }

        return volunteerRepository.save(volunteer);
    }

    public Volunteer updateVolunteer(Long id, Volunteer volunteerDetails) {
        Optional<Volunteer> optionalVolunteer = volunteerRepository.findById(id);

        if (optionalVolunteer.isPresent()) {
            Volunteer volunteer = optionalVolunteer.get();
            volunteer.setName(volunteerDetails.getName());
            volunteer.setEmail(volunteerDetails.getEmail());
            volunteer.setPhoneNumber(volunteerDetails.getPhoneNumber());
            volunteer.setSkills(volunteerDetails.getSkills());
            volunteer.setAvailability(volunteerDetails.getAvailability());

            return volunteerRepository.save(volunteer);
        }

        return null;
    }

    public boolean deleteVolunteer(Long id) {
        if (volunteerRepository.existsById(id)) {
            volunteerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean existsByEmail(String email) {
        return volunteerRepository.existsByEmail(email);
    }
}
