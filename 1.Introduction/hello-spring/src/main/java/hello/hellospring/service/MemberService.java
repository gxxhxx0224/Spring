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

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {//멤버리포지토리를 외부에서 넣어준다 : DI(디펜더지 인젝션)
        this.memberRepository = memberRepository;
    }

    //회원가입
    public Long join(Member member){

        long start = System.currentTimeMillis();

        try{
            //같은 이름의 중복회원은 안된다(여기가 핵심)
            validateDuplicateMember(member);    //중복 회원 검증
            memberRepository.save(member);
            return member.getId();
        /*
        //어차피 result는 반환되기 때문에
        memberRepository.findByName(member.getName()); //결과가 Optional<Member>니까
            . ifPresent(m->{ //ifPresent 값이 존재하면
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        });*/
        }finally {
            long finish = System.currentTimeMillis();
            long timeMs=finish-start;
            System.out.println("join = " + timeMs+"ms");
        }

    }

    private void validateDuplicateMember(Member member) {
        Optional<Member> result = memberRepository.findByName(member.getName());
        result.ifPresent(m->{ //ifPresent 값이 존재하면
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        });
    }

    //전체 회원 조회
    public List<Member> findMembers(){
        long start = System.currentTimeMillis();
        try{
            return memberRepository.findAll();
        }finally {
            long finish = System.currentTimeMillis();
            long timeMs=finish-start;
            System.out.println("findMembers = " + timeMs + "ms");
        }
    }

    public Optional<Member> findOne(Long memberId){
        return memberRepository.findById(memberId);
    }


}