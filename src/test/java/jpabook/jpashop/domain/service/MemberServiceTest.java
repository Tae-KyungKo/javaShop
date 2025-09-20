package jpabook.jpashop.domain.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
    //@Rollback(false)    //Transactional 어노테이션이 테스트 케이스에 있으면 롤백 해버려서 db에 들어가는거 안보임
                            //왜? 테스트케이스는 사실 db에 남아있을 필요가 없으니까.
                            //그래서 rollback(false)하면 db에 보임.
    public void 회원가입() throws Exception{
    Member member = new Member();
    member.setName("kim");


    Long savedId = memberService.join(member);

    em.flush(); //transactional이 디비 비우기 전에 insert 쿼리 확인할 수 있도록 하는 용도?
    assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test
    public void 중복_회원_예외() throws Exception {
        //Given
        Member member1 = new Member();
        member1.setName("kim");
        Member member2 = new Member();
        member2.setName("kim");
        //When
        memberService.join(member1);
        //Then
        //IllegalStateException 예외가 발생하지 않으면 테스트 실패
        assertThrows(IllegalStateException.class, () ->
                memberService.join(member2));
    }
}