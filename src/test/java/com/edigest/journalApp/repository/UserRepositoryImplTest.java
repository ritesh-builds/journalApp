package com.edigest.journalApp.repository;

import com.edigest.journalApp.entity.User;
import com.edigest.journalApp.service.UserArgumentsProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserRepositoryImplTest {

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Test
    public void testSaveNewUser() {
        Assertions.assertNotNull(userRepositoryImpl.getUsersForSA());
    }
}
