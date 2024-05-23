package com.example.catmicroservice.services;

import org.springframework.stereotype.Service;

@Service
public interface FriendUsecases {
    void friendCats(long firstId, long secondId);
    void unfriendCats(long firstId, long secondId);
    boolean checkIfCatsAreFriends(long firstId, long secondId);
}
