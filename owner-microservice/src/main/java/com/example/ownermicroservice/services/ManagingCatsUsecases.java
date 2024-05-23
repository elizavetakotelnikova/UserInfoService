package com.example.ownermicroservice.services;

import com.example.jpa.Cat;
import com.example.jpa.Owner;

public interface ManagingCatsUsecases {
    void addToCatList(Owner owner, Cat cat);
    void removeFromCatList(Owner owner, Cat cat);
}
