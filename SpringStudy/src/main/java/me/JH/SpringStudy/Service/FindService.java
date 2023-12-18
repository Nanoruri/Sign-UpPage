package me.JH.SpringStudy.Service;

import me.JH.SpringStudy.Entitiy.User;
import me.JH.SpringStudy.RepositoryDao.MemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FindService {
	private final MemberDao memberDao;


	@Autowired
	public FindService(MemberDao memberDao) {
		this.memberDao = memberDao;
	}


	public String findId(String name, String email){
		User user = memberDao.findByNameAndEmail(name, email);
		return(user !=null) ? user.getUserId() : null;
	}
}
