package com.spotpulse.backend.controller;

import com.spotpulse.backend.domain.User;
import com.spotpulse.backend.exception.NotFoundException;
import com.spotpulse.backend.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 테스트용 사용자 하나 저장 (개발 초기 단계에서 사용)
    @PostMapping("/test")
    public User createTestUser() {
        User user = new User("test-uid-001", "테스트유저");
        return userRepository.save(user);
    }

    // 저장된 모든 사용자 조회
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 로그인/회원가입 시 호출 - FirebaseAuthFilter가 검증해서 넣어준 uid를 기준으로
    // 이미 가입된 사용자면 그대로 반환, 처음 로그인하는 사용자면 새로 생성해서 반환
    @PostMapping("/login")
    public User loginOrRegister(HttpServletRequest request, @RequestParam(required = false) String nickname) {
        String uid = (String) request.getAttribute("uid");
        if (uid == null) {
            throw new NotFoundException("인증 토큰이 없습니다.");
        }

        Optional<User> existing = userRepository.findById(uid);
        if (existing.isPresent()) {
            return existing.get();
        }

        // 닉네임을 안 보내면 "여행자" + uid 앞 6자리로 기본 닉네임 생성
        String defaultNickname = (nickname != null) ? nickname : "여행자" + uid.substring(0, 6);
        User newUser = new User(uid, defaultNickname);
        return userRepository.save(newUser);
    }

    // 내 정보 조회 - 토큰에서 추출한 uid로 본인의 사용자 정보(닉네임, 크레딧, 등급)를 반환
    @GetMapping("/me")
    public User getMyInfo(HttpServletRequest request) {
        String uid = (String) request.getAttribute("uid");
        if (uid == null) {
            throw new NotFoundException("인증 토큰이 없습니다.");
        }

        return userRepository.findById(uid)
            .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
    }
}