package com.example.order2gatherBE.models;

import jakarta.validation.constraints.*;
import java.util.List;

public class FriendRequest {

    public static class Add {
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

    public static class GetGroupInfo {
        @NotNull(message = "The gid is required")
        private int gid;

        public int getGid() {
            return gid;
        }
    }

    public static class CreateGroup {
        @NotEmpty(message = "The name is required.")
        private String name;

        public String getName() {
            return name;
        }
    }

    public static class AddFriendToGroup {
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