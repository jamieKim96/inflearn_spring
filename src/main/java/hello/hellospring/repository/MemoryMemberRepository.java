package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.*;

/* 동시성 문제고려하지 않음. 실무에서는 HashMap 대신 ConcurrentHashMap 사용 권고
 * sequence는 숫자 키값 자동 생성해주는 역할. 실무에서는 AtomicLong 권고 */
public class MemoryMemberRepository implements MemberRepository{

    //Map<key:MemberId, value: Member>
    private static Map<Long, Member> store = new HashMap<>();   //Member 저장 공간
    private static long sequence = 0L;   //store에 시스템이 설정하는 Member ID 시퀀스 값 자동 저장

    //(id, member) 메모리에 저장하고 member 반환
    @Override
    public Member save(Member member) {

        member.setId(++sequence);           //id 자동 1 증가
        store.put(member.getId(),member);   //(id, member) Map에 저장
        return member;
    }
    //key가 id인 member 반환
    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id)); //member가 Null 반환될 경우를 대비하여 optional로 wrap 함
    }

    // 메모리에 저장 member 중 파라미터(name)와 같은 member의 name을 가진 member 반환
    @Override
    public Optional<Member> findByName(String name) {
        //java의 람다함수로 Map에서 루프를 돌면서 member에서 같은 이름을 찾으면 반환
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny();
    }

    // 메모리에 저장된 모든 member 반환
    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore(){
        store.clear();
    }
}
