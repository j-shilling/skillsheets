/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shilling.skillsheets.services.impl;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.shilling.skillsheets.services.User;
import java.io.IOException;
import java.util.Optional;

/**
 *
 * @author Jake
 */
public class UserSerializer extends StdSerializer<User> {
    public UserSerializer () {
        this (null);
    }
    
    public UserSerializer (Class<User> type) {
        super (type);
    }

    @Override
    public void serialize(User t, JsonGenerator jg, SerializerProvider sp) throws IOException {
        jg.writeStartObject();
        
        jg.writeStringField ("uuid", t.getUuid().toString());
        Optional<String> name = t.getDisplayName();
        if (name.isPresent())
            jg.writeStringField ("name", name.get());
        else
            jg.writeNullField ("name");
        Optional<String> email = t.getEmail();
        if (email.isPresent())
            jg.writeStringField ("email", email.get());
        else
            jg.writeNullField ("email");
        
        jg.writeEndObject();
    }
}
