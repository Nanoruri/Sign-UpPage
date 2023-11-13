package me.SpringStudy.RepositoryDao;

import me.SpringStudy.Entitiy.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberDao extends JpaRepository<Member,Long> {
    List<Member> findAllById(Iterable<Long> longs);
    //TODO :DAO클래스 작성..
}
