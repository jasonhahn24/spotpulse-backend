package com.spotpulse.backend.controller;

import com.spotpulse.backend.domain.Contribution;
import com.spotpulse.backend.repository.ContributionRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/contributions")
public class ContributionController {

    private final ContributionRepository contributionRepository;

    public ContributionController(ContributionRepository contributionRepository) {
        this.contributionRepository = contributionRepository;
    }

    @PostMapping("/test")
    public Contribution createTestContribution() {
        Contribution c = new Contribution("test-spot-id", "test-uid-001", "여기 주차 무료예요");
        return contributionRepository.save(c);
    }

    @GetMapping
    public List<Contribution> getAllContributions() {
        return contributionRepository.findAll();
    }
}