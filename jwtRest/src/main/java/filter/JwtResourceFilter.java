package filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Form;
import javax.ws.rs.ext.Provider;

import jwtutility.JwtUtility;

@Provider
public class JwtResourceFilter implements ContainerResponseFilter{

	  public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
	        System.out.println("response filter invoked...");
	        Form form = null ;
	        if (requestContext.getProperty("auth-failed") != null) {
	            Boolean failed = (Boolean) requestContext.getProperty("auth-failed");
	            if (failed) {
	                System.out.println("JWT auth failed. No need to return JWT token");
	                return;
	            }
	        }

	        List<Object> jwt = new ArrayList<Object>();
	        if(requestContext.getProperty("username")!=null)
	        	jwt.add(JwtUtility.buildJWT(requestContext.getProperty("username").toString()));
	        else{
		        jwt.add(JwtUtility.buildJWT(requestContext.getSecurityContext().getUserPrincipal().toString()));
	
	        }
	        // jwt.add(requestContext.getHeaderString("Authorization").split(" ")[1]);
	        responseContext.getHeaders().put("jwt", jwt);
	        System.out.println("Added JWT to response header 'jwt'");

	    }

}
