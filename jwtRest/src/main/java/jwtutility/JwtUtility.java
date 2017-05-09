package jwtutility;

import javax.ws.rs.core.Context;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;

import configuration.JwtRestConfiguration;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class JwtUtility {
	
	@Context
	static Environment environment;
	
	public static String buildJWT(String subject) {
		
		RsaJsonWebKey rsaJsonWebKey =  RsaKeyProducer.produce();
		JwtClaims claims = new JwtClaims();
		claims.setSubject(subject); 
		claims.setExpirationTimeMinutesInTheFuture(10);
		
		JsonWebSignature jws = new JsonWebSignature();
		
		jws.setPayload(claims.toJson());
		jws.setKey(rsaJsonWebKey.getPrivateKey());
		jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

		String jwt = null;
		try {
			jwt = jws.getCompactSerialization();
		} catch (JoseException ex) {

		}

		System.out.println("Claim:\n" + claims);
		System.out.println("JWS:\n" + jws);
		System.out.println("JWT:\n" + jwt);

		return jwt;
	}

}
