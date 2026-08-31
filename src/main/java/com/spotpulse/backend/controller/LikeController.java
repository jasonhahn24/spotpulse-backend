package com.spotpulse.backend.controller;

import com.spotpulse.backend.domain.Contribution;
import com.spotpulse.backend.domain.Like;
import com.spotpulse.backend.exception.NotFoundException;
import com.spotpulse.backend.repository.ContributionRepository;
import com.spotpulse.backend.repository.LikeRepository;
import com.spotpulse.backend.service.CreditService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/contributions")
public class LikeController {

    private final LikeRepository likeRepository;
    private final ContributionRepository contributionRepository;
    private final CreditService creditService;

    public LikeController(LikeRepository likeRepository,
                           ContributionRepository contributionRepository,
                           CreditService creditService) {
        this.likeRepository = likeRepository;
        this.contributionRepository = contributionRepository;
        this.creditService = creditService;
    }

    // 좋아요 등록 - 토큰에서 uid 자동 추출
    @PostMapping("/{contributionId}/like")
    public Map<String, Object> likeContribution(@PathVariable String contributionId,
                                                  HttpServletRequest httpRequest) {
        String userId = (String) httpRequest.getAttribute("uid");
        if (userId == null) {
            throw new NotFoundException("인증 토큰이 없습니다.");
        }

        if (likeRepository.existsByContributionIdAndUserId(contributionId, userId)) {
            return Map.of("success", false, "message", "이미 좋아요를 눌렀습니다.");
        }

        Like like = new Like(contributionId, userId);
        likeRepository.save(like);

        Optional<Contribution> optionalContribution = contributionRepository.findById(contributionId);
        if (optionalContribution.isEmpty()) {
            return Map.of("success", false, "message", "해당 기여를 찾을 수 없습니다.");
        }
        Contribution contribution = optionalContribution.get();
        contribution.setLikesCount(contribution.getLikesCount() + 1);
        contributionRepository.save(contribution);

        if (contribution.getLikesCount() % 5 == 0) {
            creditService.giveCredit(contribution.getUserId(), 10);
        }

        return Map.of(
            "success", true,
            "likesCount", contribution.getLikesCount()
        );
    }

    // 좋아요 취소 - 토큰에서 uid 자동 추출
    @DeleteMapping("/{contributionId}/like")
    public Map<String, Object> unlikeContribution(@PathVariable String contributionId,
                                                    HttpServletRequest httpRequest) {
        String userId = (String) httpRequest.getAttribute("uid");
        if (userId == null) {
            throw new NotFoundException("인증 토큰이 없습니다.");
        }

        if (!likeRepository.existsByContributionIdAndUserId(contributionId, userId)) {
            return Map.of("success", false, "message", "좋아요를 누른 적이 없습니다.");
        }

        likeRepository.deleteByContributionIdAndUserId(contributionId, userId);

        Optional<Contribution> optionalContribution = contributionRepository.findById(contributionId);
        if (optionalContribution.isEmpty()) {
            return Map.of("success", false, "message", "해당 기여를 찾을 수 없습니다.");
        }
        Contribution contribution = optionalContribution.get();
        contribution.setLikesCount(Math.max(0, contribution.getLikesCount() - 1));
        contributionRepository.save(contribution);

        return Map.of(
            "success", true,
            "likesCount", contribution.getLikesCount()
        );
    }
}