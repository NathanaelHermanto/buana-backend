package com.buana.backend.controller;

import com.buana.backend.model.Member;
import com.buana.backend.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @GetMapping("/")
    public Page<Member> getAllMembers(@RequestParam int page, @RequestParam int size) {
        return memberService.getAllMembers(page, size);
    }

    @GetMapping("/search")
    public Page<Member> getAllMembers(@RequestParam String name, @RequestParam int page, @RequestParam int size) {
        return memberService.getMemberByName(page, name, size);
    }

    @PostMapping("/")
    public Member addMember(@RequestBody Member member) {
        return memberService.addMember(member);
    }
}
