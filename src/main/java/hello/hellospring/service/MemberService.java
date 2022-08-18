package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public class MemberService {

    //1
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {  //memberRepository를 직접 생성하는 것이 아니라 외부에서 넣어주도록 함
        this.memberRepository = memberRepository;
    }

    /**
     * 회원가입
     * @param member
     * @return
     */

    public Long join(Member member) {
        //같은 이름의 중복회원 X
            validateDuplicateMember(member); //7
            memberRepository.save(member);   //2
            return member.getId();           //3
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())  //4
                .ifPresent(m -> {           //5
                    throw new IllegalStateException("이미 존재하는 회원입니다");
                });
    }

    /**
     * 전체 회원 조회
     * @return
     */
    public List<Member> findMembers() {
            return memberRepository.findAll();

    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
