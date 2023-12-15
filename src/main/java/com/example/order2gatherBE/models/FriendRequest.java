package com.example.order2gatherBE.models;

import java.util.List;
import jakarta.validation.constraints.*;

public class FriendRequest {

    static public class Add {
        @NotEmpty(message = "The email is required.")
        @Email(message = "The email is not a valid email.")
        private String email;
        private String nickname;

        public String getEmail() {
            return email;
        }

        public String getNickname() {
            return nickname;
        }
    }

    static public class Delete {
        @NotNull(message = "The fid is required.")
        private int fid;

        public int getFid() {
            return fid;
        }
    }

    static public class RemoveUserFromGroup {
        @NotNull(message = "The fid is required.")
        private int fid;
        @NotNull(message = "The gid is required.")
        private int gid;

        public int getFid() {
            return fid;
        }

        public int getGid() {
            return gid;
        }
    }

    static public class GetGroupInfo {
        @NotNull(message = "The gid is required")
        private int gid;

        public int getGid() {
            return gid;
        }
    }

    static public class CreateGroup {
        @NotEmpty(message = "The name is required.")
        private String name;

        public String getName() {
            return name;
        }
    }

    static public class AddFriendToGroup {
        @NotEmpty(message = "The fids is required.")
        private List<Integer> fids;
        @NotNull(message = "The gid is required.")
        private int gid;

        public List<Integer> getFids() {
            return fids;
        }

        public int getGid() {
            return gid;
        }
    }
}
