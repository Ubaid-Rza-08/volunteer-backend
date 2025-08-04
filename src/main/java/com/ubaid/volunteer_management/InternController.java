package com.ubaid.volunteer_management;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/interns")
@CrossOrigin(origins = "*")
public class InternController {

    @Autowired
    private InternService internService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<Intern>> getAllInterns() {
        List<Intern> interns = internService.getAllInterns();
        return ResponseEntity.ok(interns);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Intern> getInternById(@PathVariable Long id) {
        Optional<Intern> intern = internService.getInternById(id);
        return intern.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Intern> getInternByEmail(@PathVariable String email) {
        Optional<Intern> intern = internService.getInternByEmail(email);
        return intern.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/name")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<Intern>> searchByName(@RequestParam String name) {
        List<Intern> interns = internService.searchByName(name);
        return ResponseEntity.ok(interns);
    }

    @GetMapping("/search/university")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<Intern>> searchByUniversity(@RequestParam String university) {
        List<Intern> interns = internService.searchByUniversity(university);
        return ResponseEntity.ok(interns);
    }

    @GetMapping("/search/department")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<Intern>> searchByDepartment(@RequestParam String department) {
        List<Intern> interns = internService.searchByDepartment(department);
        return ResponseEntity.ok(interns);
    }

    @GetMapping("/search/date-range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<Intern>> getInternsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Intern> interns = internService.getInternsByDateRange(startDate, endDate);
        return ResponseEntity.ok(interns);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Intern> createIntern(@Valid @RequestBody Intern intern) {
        if (internService.existsByEmail(intern.getEmail())) {
            return ResponseEntity.badRequest().build();
        }

        Intern savedIntern = internService.saveIntern(intern);
        return ResponseEntity.ok(savedIntern);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Intern> updateIntern(@PathVariable Long id,
                                               @Valid @RequestBody Intern internDetails) {
        Intern updatedIntern = internService.updateIntern(id, internDetails);

        if (updatedIntern != null) {
            return ResponseEntity.ok(updatedIntern);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteIntern(@PathVariable Long id) {
        if (internService.deleteIntern(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}