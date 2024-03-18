package org.example.owner;

public interface ManagingCatsUsecases {
    void addToCatList(long ownerId, long catId);
    void removeFromCatList(long ownerId, long catId);
}
