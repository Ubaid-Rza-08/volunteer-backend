package com.ubaid.volunteer_management.volunteer;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/volunteers")
@CrossOrigin(origins = "*")
public class VolunteerController {

    @Autowired
    private VolunteerService volunteerService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Volunteer>> getAllVolunteers() {
        List<Volunteer> volunteers = volunteerService.getAllVolunteers();
        return ResponseEntity.ok(volunteers);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Volunteer> getVolunteerById(@PathVariable Long id) {
        Optional<Volunteer> volunteer = volunteerService.getVolunteerById(id);
        return volunteer.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Volunteer> getVolunteerByEmail(@PathVariable String email) {
        Optional<Volunteer> volunteer = volunteerService.getVolunteerByEmail(email);
        return volunteer.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/name")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Volunteer>> searchByName(@RequestParam String name) {
        List<Volunteer> volunteers = volunteerService.searchByName(name);
        return ResponseEntity.ok(volunteers);
    }

    @GetMapping("/search/skill")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Volunteer>> searchBySkill(@RequestParam String skill) {
        List<Volunteer> volunteers = volunteerService.searchBySkill(skill);
        return ResponseEntity.ok(volunteers);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Volunteer> createVolunteer(@Valid @RequestBody Volunteer volunteer) {
        if (volunteerService.existsByEmail(volunteer.getEmail())) {
            return ResponseEntity.badRequest().build();
        }

        Volunteer savedVolunteer = volunteerService.saveVolunteer(volunteer);
        return ResponseEntity.ok(savedVolunteer);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Volunteer> updateVolunteer(@PathVariable Long id,
                                                     @Valid @RequestBody Volunteer volunteerDetails) {
        Volunteer updatedVolunteer = volunteerService.updateVolunteer(id, volunteerDetails);

        if (updatedVolunteer != null) {
            return ResponseEntity.ok(updatedVolunteer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteVolunteer(@PathVariable Long id) {
        if (volunteerService.deleteVolunteer(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
