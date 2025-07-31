package com.orenda.charging_session.service;

import com.orenda.charging_session.model.SessionRequest;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    public String authorize(SessionRequest sessionRequest){
        try {
            if(sessionRequest.getDriverTokenId().startsWith("valid")) return "allowed";
            if (sessionRequest.getDriverTokenId().startsWith("block")) return "not_allowed";
            return "invalid";
        } catch (Exception e) {
            return "unknown";
        }
    }
}
