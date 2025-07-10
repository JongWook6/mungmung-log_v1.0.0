package com.grepp.teamnotfound.infra.util.mail;

import java.security.SecureRandom;

public class VerifyCodeGenerator {

    private static final String CHARACTERS = "0123456789";
    private static final int CODE_LENGTH = 6;

    public static String generateCode(){
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for(int i = 0; i<CODE_LENGTH; i++){
            code.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }

        return code.toString();
    }
}
