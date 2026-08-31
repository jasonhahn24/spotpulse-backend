package com.spotpulse.backend.controller;

import com.spotpulse.backend.domain.Contribution;
import com.spotpulse.backend.exception.NotFoundException;
import com.spotpulse.backend.repository.ContributionRepository;
import com.spotpulse.backend.service.CreditService;
import jakarta.servlet.http.HttpServletRequest;
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

    // 실전용 - 로그인한 사용자가 실제 관광지에 팁을 등록
    @PostMapping
    public Contribution createContribution(@RequestBody ContributionRequest request,
                                             HttpServletRequest httpRequest) {
        String uid = (String) httpRequest.getAttribute("uid");
        if (uid == null) {
            throw new NotFoundException("인증 토큰이 없습니다.");
        }

        Contribution c = new Contribution(request.getSpotId(), uid, request.getContent());
        Contribution saved = contributionRepository.save(c);

        // 팁 등록 시 작성자에게 크레딧 +5 지급
        creditService.giveCredit(uid, 5);

        return saved;
    }

    @GetMapping
    public List<Contribution> getAllContributions() {
        return contributionRepository.findAll();
    }

    // 요청 body를 담을 내부 클래스
    public static class ContributionRequest {
        private String spotId;
        private String content;

        public String getSpotId() { return spotId; }
        public void setSpotId(String spotId) { this.spotId = spotId; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }
}