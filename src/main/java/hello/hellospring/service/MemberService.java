package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;

import java.util.List;
import java.util.Optional;

public class MemberService {

    //1 회원 리포지토리 객체 생성
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
        /*아래와 같은 형태는 모양이 안예쁨...
        Optional<Member> result = memberRepository.findByName(member.getName());
        result.ifPresent(m -> {
            throw new IllegalStateException("이미 존재하는 값입니다")
        });*/
        //6 Optional 메소드인 memberRepository 바로 뒤에 .ifPresent 메소드 넣기 가능
        validateDuplicateMember(member); //7 중복회원 검증 메소드 Extract (ctrl + alt + shift + t)
        memberRepository.save(member);   //2 member 정보 저장
        return member.getId();           //3 저장 후 memberID 반환
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())  //4 name으로 member 조회
                .ifPresent(m -> {           //5 ifPresent() : null이 아닌 어떤 값이 있으면 내부 로직 동작(Optional이라 가능한 메소드)
                    throw new IllegalStateException("이미 존재하는 회원입니다");
                });
    }

    /**
     * 전체 회원 조회
     * @return
     */
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
