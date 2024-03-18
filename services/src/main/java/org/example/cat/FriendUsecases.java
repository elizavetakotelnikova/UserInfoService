package org.example.cat;

public interface FriendUsecases {
    void friendCats(long firstId, long secondId);
    void unfriendCats(long firstId, long secondId);
    boolean checkIfCatsAreFriends(long firstId, long secondId);
}
