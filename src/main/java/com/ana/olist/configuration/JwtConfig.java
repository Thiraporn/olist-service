package com.ana.olist.configuration;

import com.ana.common.security.libs.jsonwebtoken.KeyUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.PrivateKey;
import java.security.PublicKey;

@Configuration
public class JwtConfig {

    @Value("${jwt.publicKeyPath}")
    private String publicKeyPath;

    @Bean
    public PublicKey publicKey() throws Exception {
        return KeyUtils.loadPublicKey(publicKeyPath);
    }

}