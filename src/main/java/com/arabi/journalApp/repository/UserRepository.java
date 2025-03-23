package com.arabi.journalApp.repository;

import com.arabi.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByUserName(String userName);
}
