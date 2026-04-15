package com.edigest.journalApp.repository;

import com.edigest.journalApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class UserRepositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> getUsersForSA() {
        Query query = new Query();

//        query.addCriteria(Criteria.where("email").exists(true));
//        query.addCriteria(Criteria.where("email").ne(null).ne(""));


        query.addCriteria(Criteria.where("email")
                .regex("^[A-Za-z0-9+_.-]+@(.+)$"));
        query.addCriteria(Criteria.where("sentimentAnalysis").is(true));
        final List<User> users = mongoTemplate.find(query, User.class);
        return users;
    }
}
