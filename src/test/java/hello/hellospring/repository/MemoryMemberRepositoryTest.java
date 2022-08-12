package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.assertj.core.api.Assertions;  //static Import
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

public class MemoryMemberRepositoryTest {
    MemoryMemberRepository repository = new MemoryMemberRepository();

    @AfterEach
    public void afterEach(){
        repository.clearStore();
    }

    @Test
    public void save(){
        Member member = new Member();
        member.setName("spring");   //member 이름 세팅

        repository.save(member);    //리포지토리에 이름 저장

        //리포지토리에 저장된 member 조회. optional에서 값을 꺼낼땐 get으로 꺼냄
        Member result = repository.findById(member.getId()).get();
        //member가 result랑 같다!
        assertThat(result).isEqualTo(member);

        //실무에서는 빌드 시 오류테스트 통과가 안되면 다음단계로 넘어갈 수 없게 만듬
    }

    @Test
    public void findByName(){
        Member member1 = new Member();
        member1.setName(("spring1"));
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        Member result = repository.findByName("spring1").get();

        assertThat(result).isEqualTo(member1);
    }

    @Test
    public void findAll(){
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        List<Member> result = repository.findAll();

        assertThat(result.size()).isEqualTo(2);
    }
}
