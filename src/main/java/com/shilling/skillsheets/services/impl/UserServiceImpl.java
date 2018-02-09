package com.shilling.skillsheets.services.impl;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.shilling.skillsheets.dao.Account;
import com.shilling.skillsheets.dao.AccountDao;
import com.shilling.skillsheets.services.User;
import com.shilling.skillsheets.services.UserService;

/**
 * Implementation of {@link UserService}
 *
 * @author Jake Shilling
 *
 */
public class UserServiceImpl implements UserService {

    private final Logger logger = LogManager.getLogger(UserServiceImpl.class);
    private final AccountDao dao;
    private final GoogleIdTokenVerifier verifier;
    
    private final UserFactory users;

    @Autowired
    private UserServiceImpl(
            AccountDao dao,
            GoogleIdTokenVerifier verifier,
            UserFactory users) {
        this.dao = dao;
        this.verifier = verifier;
        this.users = users;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> fromToken(String id_token) {
        GoogleIdToken token;

        try {
            token = verifier.verify(id_token);
        } catch (GeneralSecurityException | IOException e) {
            this.logger.error(e.getMessage());
            this.logger.traceExit("Returning Optional.empty()");
            return Optional.empty();
        }

        if (token == null) {
            return Optional.empty(); // token could not be verified
        }
        Payload payload = token.getPayload();
        
        try {
            
            Optional<Account> result = this.dao.getById(payload.getSubject());
            if (!result.isPresent())
                result = this.dao.getByEmail(payload.getEmail());

            Account account;
            if (result.isPresent()) {

                account = result.get()
                        .setDisplayName((String) payload.get("name"))
                        .setId(payload.getSubject())
                        .setEmail(payload.getEmail());
            } else {
                account = this.dao.newWithId(payload.getSubject())
                        .setDisplayName((String) payload.get("name"))
                        .setEmail(payload.getEmail());
            }

            return Optional.of(this.users.user(account));
            
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
                
    }

}
