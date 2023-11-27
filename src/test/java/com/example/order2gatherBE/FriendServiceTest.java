package com.example.order2gatherBE;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;

import com.example.order2gatherBE.models.GroupModel;
import com.example.order2gatherBE.models.UserModel;
import com.example.order2gatherBE.repository.AuthenticationRepository;
import com.example.order2gatherBE.repository.FriendRepository;
import com.example.order2gatherBE.services.FriendService;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class FriendServiceTest {
    @Autowired
    private FriendService friendService;

    @MockBean
    private AuthenticationRepository authenticationRepository;

    @MockBean
    private FriendRepository friendRepository;

    UserModel createUser(int id, String email, String username) {
        UserModel user = new UserModel();
        user.setId(id);
        user.setGmail(email);
        user.setUsername(username);
        user.setLastLogin(new Timestamp(new Date().getTime()));
        return user;
    }

    GroupModel createGroup(int id, String name) {
        GroupModel group = new GroupModel();
        group.setGid(id);
        group.setName(name);
        return group;
    }

    @Test
    void addFriend() {
        UserModel user1 = createUser(1, "test1@gmail.com", "user1");
        UserModel user2 = createUser(2, "test2@gmail.com", "user2");
        UserModel user3 = createUser(2, "test2@gmail.com", "user3");
        Mockito
            .when(authenticationRepository.findUserbyGmail(user1.getGmail()))
            .thenReturn(user1);
        Mockito
            .when(authenticationRepository.findUserbyGmail(user2.getGmail()))
            .thenReturn(user2);
        Mockito
            .when(authenticationRepository.findUserbyGmail(user3.getGmail()))
            .thenReturn(user3);
        // Assume user1 and user3 are friends
        Mockito
            .when(friendRepository.isFriend(user1.getId(), user3.getId()))
            .thenReturn(true);
        // Assume the db always work
        Mockito
            .when(friendRepository.add(anyInt(), anyInt(), anyString()))
            .thenReturn(true);

        // Test add friends
        Assertions.assertTrue(
            friendService.add(user1.getId(), user2.getGmail(), null)
        );

        // Should return true even if they are already friend
        Assertions.assertTrue(
            friendService.add(user1.getId(), user3.getGmail(), null)
        );
    }

    @Test
    void TestGroup() {
        UserModel user1 = createUser(1, "test1@gmail.com", "user1");
        UserModel user2 = createUser(2, "test2@gmail.com", "user2");
        UserModel user3 = createUser(2, "test2@gmail.com", "user3");
        GroupModel group1 = createGroup(1, "g1");
        GroupModel group2 = createGroup(2, "g2");
        Mockito
            .when(authenticationRepository.findUserbyGmail(user1.getGmail()))
            .thenReturn(user1);
        Mockito
            .when(authenticationRepository.findUserbyGmail(user2.getGmail()))
            .thenReturn(user2);
        Mockito
            .when(authenticationRepository.findUserbyGmail(user3.getGmail()))
            .thenReturn(user3);
        // Assume user1 and user2 are not friends
        Mockito
            .when(friendRepository.isFriend(user1.getId(), user2.getId()))
            .thenReturn(false);
        // Assume user1 and user3 are friends
        Mockito
            .when(friendRepository.isFriend(user1.getId(), user3.getId()))
            .thenReturn(true);
        // Assume user1 are group g1 member, but not group 2 member
        Mockito
            .when(
                friendRepository.isGroupMember(user1.getId(), group1.getGid())
            )
            .thenReturn(true);
        Mockito
            .when(
                friendRepository.isGroupMember(user1.getId(), group2.getGid())
            )
            .thenReturn(false);
        // Assume the db always work
        Mockito
            .when(friendRepository.addUserToGroup(anyInt(), anyInt(), anyInt()))
            .thenReturn(true);
        Mockito
            .when(friendRepository.createGroup(group1.getName()))
            .thenReturn(group1.getGid());
        Mockito
            .when(friendRepository.createGroup(group2.getName()))
            .thenReturn(group2.getGid());
        Mockito
            .when(
                friendRepository.addUsersToGroup(anyList(), anyInt(), anyInt())
            )
            .thenReturn(true);
        Assertions.assertEquals(
            friendService.createGroup(user1.getId(), group1.getName()),
            group1.getGid()
        );

        List<Integer> userlist = new ArrayList<Integer>();
        userlist.add(user2.getId());
        Mockito
            .when(friendRepository.isAllFriend(user1.getId(), userlist))
            .thenReturn(true);
        // user1 add friend to group1
        Assertions.assertTrue(
            friendService.addUsersToGroup(
                user1.getId(),
                userlist,
                group1.getGid()
            )
        );
        Assertions.assertFalse(
            friendService.addUsersToGroup(
                user1.getId(),
                userlist,
                group2.getGid()
            )
        );

        userlist.add(user3.getId());
        Mockito
            .when(friendRepository.isAllFriend(user1.getId(), userlist))
            .thenReturn(false);
        // Add stranger to group
        Assertions.assertFalse(
            friendService.addUsersToGroup(
                user1.getId(),
                userlist,
                group1.getGid()
            )
        );
        Assertions.assertFalse(
            friendService.addUsersToGroup(
                user1.getId(),
                userlist,
                group2.getGid()
            )
        );
        // TODO: Test get friend and group info
    }
}
