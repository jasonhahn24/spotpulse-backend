package com.spotpulse.backend.controller;

import com.spotpulse.backend.domain.Contribution;
import com.spotpulse.backend.repository.ContributionRepository;
import com.spotpulse.backend.service.CreditService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contributions")
public class ContributionController {

    private final ContributionRepository contributionRepository;
    private final CreditService creditService;

    public ContributionController(ContributionRepository contributionRepository,
                                    CreditService creditService) {
        this.contributionRepository = contributionRepository;
        this.creditService = creditService;
    }

    @PostMapping("/test")
    public Contribution createTestContribution() {
        Contribution c = new Contribution("test-spot-id", "test-uid-001", "여기 주차 무료예요");
        Contribution saved = contributionRepository.save(c);

        creditService.giveCredit("test-uid-001", 5);

        return saved;
    }

    @GetMapping
    public List<Contribution> getAllContributions() {
        return contributionRepository.findAll();
    }
}