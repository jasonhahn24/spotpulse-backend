package com.spotpulse.backend.service;

import com.spotpulse.backend.domain.User;
import com.spotpulse.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreditService {

    private final UserRepository userRepository;

    public CreditService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void giveCredit(String userId, int amount) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) return;

        User user = optionalUser.get();
        user.setTotalCredits(user.getTotalCredits() + amount);
        user.setGrade(calculateGrade(user.getTotalCredits()));
        userRepository.save(user);
    }

    private String calculateGrade(int credits) {
        if (credits >= 1500) return "레전드 여행자";
        if (credits >= 700) return "여행 마스터";
        if (credits >= 300) return "시니어 여행자";
        if (credits >= 100) return "열정 여행자";
        return "초보 여행자";
    }
}