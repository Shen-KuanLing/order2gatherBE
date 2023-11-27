package com.example.order2gatherBE.services;

import com.example.order2gatherBE.models.GroupModel;
import com.example.order2gatherBE.models.UserModel;
import com.example.order2gatherBE.repository.AuthenticationRepository;
import com.example.order2gatherBE.repository.FriendRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendService {
    @Autowired
    FriendRepository friendRepository;

    @Autowired
    AuthenticationRepository authenticationRepository;

    public boolean add(int uid1, String user2Email, String user2Nickname) {
        UserModel user2 = authenticationRepository.findUserbyGmail(user2Email);
        if (user2 == null) {
            return false;
        }
        if (friendRepository.isFriend(uid1, user2.getId())) {
            return true;
        }
        return friendRepository.add(uid1, user2.getId(), user2Nickname);
    }

    public List<Map<String, Object>> get(int uid) {
        return friendRepository.getUserFriend(uid);
    }

    public HashMap<String, Object> getGroupInfo(int uid, int gid) {
        if (!friendRepository.isGroupMember(uid, gid)) {
            return null;
        }
        HashMap<String, Object> res = new HashMap<String, Object>();
        GroupModel group = friendRepository.getGroup(gid);
        res.put("name", group.getName());
        res.put("gid", group.getGid());
        res.put("members", friendRepository.getGroupMember(gid));
        return res;
    }

    public int createGroup(int uid, String gname) {
        int gid = friendRepository.createGroup(gname);
        if (gid < 0) {
            return gid;
        }
        // role = 0 means group creator
        if (!friendRepository.addUserToGroup(uid, gid, 0)) {
            return -1;
        }
        return gid;
    }

    public boolean addUsersToGroup(int uid, List<Integer> fids, int gid) {
        if (
            !friendRepository.isGroupMember(uid, gid) ||
            !friendRepository.isAllFriend(uid, fids)
        ) {
            return false;
        }
        // role = 0 means normal member
        if (!friendRepository.addUsersToGroup(fids, gid, 1)) {
            return false;
        }
        return true;
    }
}
