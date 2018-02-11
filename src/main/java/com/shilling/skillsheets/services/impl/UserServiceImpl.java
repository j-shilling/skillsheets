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
    private final GoogleIdTokenVerifier verifier;
    
    private final UserFactory users;

    @Autowired
    private UserServiceImpl(
            GoogleIdTokenVerifier verifier,
            UserFactory users) {
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
        
        Optional<User> result = this.users.fromId(payload.getSubject());
        if (!result.isPresent())
            result = this.users.fromEmail(payload.getEmail());
        
        if (result.isPresent()) {
            result.get().setId (payload.getSubject())
                    .setEmail (payload.getEmail())
                    .setDisplayName ((String) payload.get("name"));
        } else {
            User user = this.users.newUserWithId(payload.getSubject())
                    .setEmail (payload.getEmail())
                    .setDisplayName ((String) payload.get("name"));
            result = Optional.of(user);
        }
        
        return result;
                
    }

}
