package com.example.order2gatherBE.repository;

import com.example.order2gatherBE.models.GroupModel;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FriendRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AuthenticationRepository authenticationRepository;

    public boolean add(int uid1, int uid2, String user2NickName) {
        String user2Name = user2NickName;
        String sql =
            "INSERT INTO `friend` (`user1`, `user2`, `user2NickName`) VALUES (?, ?, ?);";
        if (user2Name == null) {
            user2Name =
                authenticationRepository.findUserbyId(uid2).getUsername();
        }
        return jdbcTemplate.update(sql, uid1, uid2, user2Name) > 0;
    }

    public boolean isGroupMember(int uid, int gid) {
        String sql =
            "select count(*) from user2Group where uid = ? and gid = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, uid, gid) > 0;
    }

    public boolean isFriend(int uid1, int uid2) {
        String sql =
            "select count(*) from friend where user1 = ? and user2 = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, uid1, uid2) > 0;
    }

    public boolean isAllFriend(int uid1, List<Integer> uid2List) {
        String user2Para = uid2List
            .stream()
            .map(n -> String.valueOf(n))
            .collect(Collectors.joining(","));
        String sql =
            "SELECT COUNT(*) FROM friend WHERE user1 = ? AND user2 IN (?)";
        int count = jdbcTemplate.queryForObject(
            sql,
            Integer.class,
            uid1,
            user2Para
        );
        return count == uid2List.size();
    }

    public int createGroup(String gname) {
        String sql = "INSERT INTO `group` (`name`) VALUES (?);";
        if (jdbcTemplate.update(sql, gname) > 0) {
            int res = jdbcTemplate.queryForObject(
                "SELECT @@IDENTITY as id",
                Integer.class
            );
            return res;
        }
        return -1;
    }

    public boolean addUserToGroup(int uid, int gid, int role) {
        String sql =
            "INSERT INTO `user2group` (`gid`, `uid`, `role`) VALUES (?, ?, ?);";
        return jdbcTemplate.update(sql, gid, uid, role) > 0;
    }

    public boolean addUsersToGroup(List<Integer> uidList, int gid, int role) {
        String sql = "INSERT INTO user2group (gid, uid, role) VALUES (?, ?, ?)";
        int[] updateCounts = jdbcTemplate.batchUpdate(
            sql,
            new BatchPreparedStatementSetter() {

                @Override
                public void setValues(
                    PreparedStatement preparedStatement,
                    int i
                )
                    throws SQLException {
                    int uid = uidList.get(i);
                    preparedStatement.setInt(1, gid);
                    preparedStatement.setInt(2, uid);
                    preparedStatement.setInt(3, role);
                }

                @Override
                public int getBatchSize() {
                    return uidList.size();
                }
            }
        );
        for (int updateCount : updateCounts) {
            if (updateCount <= 0) {
                return false;
            }
        }
        return true;
    }

    public List<Map<String, Object>> getUserFriend(int uid) {
        String sql =
            "Select user.id as id, user.username as username, user.gmail as email, " +
            "friend.user2NickName as nickname from friend left join user on friend.user2=user.id where user1=?";
        List<Map<String, Object>> friends = jdbcTemplate.queryForList(sql, uid);
        return friends;
    }

    public GroupModel getGroup(int gid) {
        String sql = "Select * from `group` where gid = ?";
        List<GroupModel> groups = jdbcTemplate.query(
            sql,
            new BeanPropertyRowMapper<>(GroupModel.class),
            gid
        );
        if (groups.size() == 0) {
            return null;
        }
        return groups.get(0);
    }

    public List<Map<String, Object>> getGroupMember(int gid) {
        String sql =
            "SELECT user.id as id, user.username as username, user.gmail as email," +
            "user2group.role as role " +
            "from user2group left join user on user2group.uid=user.id where gid=?";
        List<Map<String, Object>> groupMember = jdbcTemplate.queryForList(
            sql,
            gid
        );
        return groupMember;
    }
}
