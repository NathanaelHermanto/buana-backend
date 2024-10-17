package com.buana.backend.service;

import com.buana.backend.exception.BadRequestException;
import com.buana.backend.model.Member;
import com.buana.backend.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    public Page<Member> getAllMembers(int page, int size) {
        if (page < 0 || size <= 0) {
            throw new BadRequestException("Page must be non-negative and size must be positive");
        }

        Pageable pageable = PageRequest.of(page, size);
        return memberRepository.findAll(pageable);
    }

    public Page<Member> getMemberByName(int page, String name, int size) {
        if (name == null || name.trim().isEmpty()) {
            throw new BadRequestException("Search name must not be null or empty");
        }

        if (page < 0 || size <= 0) {
            throw new BadRequestException("Page must be non-negative and size must be positive");
        }

        Pageable pageable = PageRequest.of(page, size);
        return memberRepository.findByNameContaining(name, pageable);
    }

    public Member addMember(Member member) {
        if (member == null || member.getName() == null || member.getName().trim().isEmpty()) {
            throw new BadRequestException("Member name cannot be null or empty");
        }

        return memberRepository.save(member);
    }

    public Member findMemberById(Long id) {
        if (id == null) {
            throw new BadRequestException("Member id cannot be null");
        }

        return memberRepository.findById(id).orElse(null);
    }
}
