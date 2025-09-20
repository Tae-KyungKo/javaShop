package jpabook.jpashop.service;


import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    //final이 뭐고?
    private final MemberRepository memberRepository;

    //@Autowired  생성자 붙여놓으면 스프링이 자동으로 인젝션 해줌. 인젝션?
    /*  RequireArgsConstructor 어노테이션 방법으로 대체 ???
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    */

    //회원 가입
    @Transactional(readOnly = false)    //기본적으로 false
    public Long join(Member member) {

        validateDuplicateMember(member);    //중복 회원 검증 메서드
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        //EXCEPTION
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    @Transactional(readOnly = true) //읽기만 할 때는 readOnly = true를 넣으면 성능이 좋아짐(쓰기에는 절대 안됨 update 불가)
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
