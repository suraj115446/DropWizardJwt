package filter;

import java.io.IOException;
import java.net.Authenticator.RequestorType;
import java.security.Principal;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import org.eclipse.jetty.http.HttpMethod;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;
import org.w3c.dom.views.AbstractView;

import configuration.JwtRestConfiguration;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import jwtutility.RsaKeyProducer;

@Provider
public class JwtRequestFilter implements ContainerRequestFilter {

	public void filter(ContainerRequestContext requestContext) throws IOException {
		
		String authHeaderVal = null;

		if (requestContext.getUriInfo().getPath().contains("secured") && requestContext.getMethod().equals(HttpMethod.POST.toString())) {

		} else {
			authHeaderVal = requestContext.getHeaderString("Authorization");

	        if (authHeaderVal.startsWith("Bearer")) {
	            try {
	                System.out.println("JWT based Auth in action... time to verify th signature");
	                System.out.println("JWT being tested:\n" + authHeaderVal.split(" ")[1]);
	                final String subject = validate(authHeaderVal.split(" ")[1]);
	                final SecurityContext securityContext = requestContext.getSecurityContext();
	                if (subject != null) {
	                    requestContext.setSecurityContext(new SecurityContext() {
	                        
	                        public Principal getUserPrincipal() {
	                            return new Principal() {
	                                
	                                public String getName() {
	                                    System.out.println("Returning custom Principal - " + subject);
	                                    return subject;
	                                }
	                            };
	                        }

	                        
	                        public boolean isUserInRole(String role) {
	                            return securityContext.isUserInRole(role);
	                        }

	                        
	                        public boolean isSecure() {
	                            return securityContext.isSecure();
	                        }

	                        
	                        public String getAuthenticationScheme() {
	                            return securityContext.getAuthenticationScheme();
	                        }
	                    });
	                }
	            } catch (InvalidJwtException ex) {
	                System.out.println("JWT validation failed");

	                requestContext.setProperty("auth-failed", true);
	                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());

	            }

	        } else {
	            System.out.println("No JWT token !");
	            requestContext.setProperty("auth-failed", true);
	            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
	        }
		}
	}

	private String validate(String jwt) throws InvalidJwtException {
	       String subject = null;
	        RsaJsonWebKey rsaJsonWebKey = RsaKeyProducer.produce();

	        System.out.println("RSA hash code... " + rsaJsonWebKey.hashCode());

	        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
	                .setRequireSubject() // the JWT must have a subject claim
	                .setVerificationKey(rsaJsonWebKey.getKey()) // verify the signature with the public key
	                .build(); // create the JwtConsumer instance

	        try {
	            //  Validate the JWT and process it to the Claims
	            JwtClaims jwtClaims = jwtConsumer.processToClaims(jwt);
	            subject = (String) jwtClaims.getClaimValue("sub");
	            System.out.println("JWT validation succeeded! " + jwtClaims);
	        } catch (InvalidJwtException e) {
	            e.printStackTrace(); //on purpose
	            throw e;
	        }

	        return subject;
	}


}
