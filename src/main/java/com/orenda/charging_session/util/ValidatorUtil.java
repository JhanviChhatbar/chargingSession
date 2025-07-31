package com.orenda.charging_session.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class ValidatorUtil {

    public static boolean isValidUUID(String uuid){
        try {
            UUID.fromString(uuid);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isValidToken(String token){
        return token != null && token.length() >= 20 && token.length() <= 80 &&
        token.matches("[A-Za-z0-9\\-._~]+");
    }

    public static boolean isValidURL(String url){
        try {
            URL updatedURL = new URL(url);
            return updatedURL.getProtocol().startsWith("http");
        }catch (MalformedURLException e){
            e.printStackTrace();
            return false;
        }
    }
}
