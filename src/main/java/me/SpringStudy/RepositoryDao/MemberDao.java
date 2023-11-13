package me.SpringStudy.RepositoryDao;

import me.SpringStudy.Entitiy.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberDao extends JpaRepository<Member,Long> {
//TODO :DAO클래스 작성..


}
