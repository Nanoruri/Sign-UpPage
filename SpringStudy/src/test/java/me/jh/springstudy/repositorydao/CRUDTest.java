//import me.SpringStudy.MySpringBootApplication;
//import me.SpringStudy.RepositoryDao.MemberDao;
//import me.SpringStudy.Entitiy.Member;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//
//import java.util.List;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest(classes = MySpringBootApplication.class)
//public class CRUDTest {
//
//    @Autowired
//    private MemberDao memberDao; // Assuming you have a MemberDao class
//
//    @MockBean
//    private MemberDao memberRepository; // Assuming you have a MemberRepository class for database operations
//
//
//    // 멤버 등록 후 조회
//
////    @Test
////    public void testGetAllMembers() {
////        // Mock data
////        MeberDto member1 = new MeberDto();
////        member1.setUserNo(1);
////        member1.setUserId("user1");
////        // Set other properties as needed
////
////        MeberDto member2 = new MeberDto();
////        member2.setUserNo(2);
////        member2.setUserId("user2");
////        // Set other properties as needed
////
////        List<MeberDto> mockMembers = new ArrayList<>();
////        mockMembers.add(member1);
////        mockMembers.add(member2);
////
////        // Mock repository behavior
////        when(memberRepository.findAll()).thenReturn(mockMembers);
////
////        // Perform the test
////        List<MeberDto> actualMembers = memberDao.findAll();
////
////
////        // Print All TEST
////        for(MeberDto memberDto : actualMembers)
////            System.out.println(memberDto.toString());
////
////
////        // Verify the results
////        assertEquals(2, actualMembers.size());
////        assertEquals(member1.getUserId(), actualMembers.get(0).getUserId());
////        // Verify other properties as needed
////    }
//
//
//
//
//    private static final Logger logger = LoggerFactory.getLogger(CRUDTest.class);
//
//    @Test
//    public void testInsertMember() {
//        // Mock data for the new member to be inserted
//        Member mem1 = new Member(1, "im", "임재현", "iim", 1, 1, "test", "test", "test");
//
//            // Set other properties as needed
//
//        // Mock repository behavior for the insert operation
//        when(memberRepository.save(mem1)).thenReturn(mem1);
//
//
//
//
//        // Perform the test
//        Member savedMember1 = memberDao.save(mem1);
//
//
//        // Perform the test
//        List<Member> actualMembers = memberDao.findAll();
//
//
//
//        logger.info("####### print start #######");
//        // Print All TEST
//        for(Member memberDto : actualMembers)
//            logger.info("#######" + memberDto.toString());
//
//
//
//
//        // Verify the result
//        assertEquals(mem1.getUserId(), savedMember1.getUserId());
//        // Verify other properties as needed
//    }
//    // Add more test cases as needed for other methods in your MemberDao
//
//
//}
