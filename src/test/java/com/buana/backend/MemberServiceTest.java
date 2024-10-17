package com.buana.backend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.buana.backend.exception.BadRequestException;
import com.buana.backend.model.Member;
import com.buana.backend.repository.MemberRepository;
import com.buana.backend.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetMembers_ReturnsMemberPage() {
        // Arrange
        Member member1 = new Member(1L, "Peter Parker", "Developer");
        Member member2 = new Member(2L, "Tony Stark", "Manager");
        List<Member> members = Arrays.asList(member1, member2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Member> memberPage = new PageImpl<>(members, pageable, members.size());

        when(memberRepository.findAll(pageable)).thenReturn(memberPage);

        // Act
        Page<Member> result = memberService.getAllMembers(0,10);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals("Peter Parker", result.getContent().get(0).getName());
        assertEquals("Tony Stark", result.getContent().get(1).getName());
        verify(memberRepository, times(1)).findAll(pageable);
    }

    @Test
    public void testGetMembers_EmptyPage() {
        // Arrange
        List<Member> members = List.of(); // No members
        Pageable pageable = PageRequest.of(0, 10);
        Page<Member> memberPage = new PageImpl<>(members, pageable, 0);

        when(memberRepository.findAll(pageable)).thenReturn(memberPage);

        // Act
        Page<Member> result = memberService.getAllMembers(0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getContent().size());
        verify(memberRepository, times(1)).findAll(pageable);
    }

    @Test
    public void testAddMember_Success() {
        // Arrange
        Member member = new Member(1L, "Peter Parker", "Developer");

        when(memberRepository.save(member)).thenReturn(member);

        // Act
        Member result = memberService.addMember(member);

        // Assert
        assertNotNull(result);
        assertEquals("Peter Parker", result.getName());
        verify(memberRepository, times(1)).save(member);
    }

    @Test
    public void testAddMember_InvalidMember_ThrowsException() {
        // Arrange
        Member invalidMember = new Member(1L, null, "Developer"); // Name is null

        // Act & Assert
        BadRequestException thrown = assertThrows(BadRequestException.class, () -> memberService.addMember(invalidMember));
        assertEquals("Member name cannot be null or empty", thrown.getMessage());

        // Check if save was never called
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    public void testAddMember_EmptyName_ThrowsException() {
        // Arrange
        Member invalidMember = new Member(1L, "", "Developer"); // Name is empty

        // Act & Assert
        BadRequestException thrown = assertThrows(BadRequestException.class, () -> memberService.addMember(invalidMember));
        assertEquals("Member name cannot be null or empty", thrown.getMessage());

        // Check if save was never called
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    public void testFindById_ExistingMember() {
        // Arrange
        Member member = new Member(1L, "Peter Parker", "Developer");

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        // Act
        Member foundMember = memberService.findMemberById(1L);

        // Assert
        assertNotNull(foundMember);
        assertEquals("Peter Parker", foundMember.getName());
        assertEquals("Developer", foundMember.getPosition());
        verify(memberRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindById_NonExistingMember() {
        // Arrange
        when(memberRepository.findById(2L)).thenReturn(Optional.empty());

        // Act
        Member foundMember = memberService.findMemberById(2L);

        // Assert
        assertNull(foundMember);
        verify(memberRepository, times(1)).findById(2L);
    }
}

