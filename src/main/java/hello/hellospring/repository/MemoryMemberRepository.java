package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.*;

/* 동시성 문제고려하지 않음. 실무에서는 HashMap 대신 ConcurrentHashMap 사용 권고
 * sequence는 숫자 키값 자동 생성해주는 역할. 실무에서는 AtomicLong 권고 */
public class MemoryMemberRepository implements MemberRepository{

    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(),member);
        return member;
    }
    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }
    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny();
    }
    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }
    public void clearStore(){
        store.clear();
    }
}
