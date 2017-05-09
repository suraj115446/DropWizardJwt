package resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ldap.LdapServiceImpl;

@Path("/secured")
@Produces(MediaType.APPLICATION_JSON)
public class DummyResource {
	
	LdapServiceImpl ldapService = new LdapServiceImpl();

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String authenticateUserCredentials(@Context ContainerRequestContext  ctx, 
    		@FormParam("username") String username ,
    		@FormParam("password") String password) {
    	
    	boolean isUserAuthentic = ldapService.authenticateUser(username,password);
    	
    	if(isUserAuthentic){
        	ctx.setProperty("username", username);
    	} else {
    		ctx.setProperty("auth-failed", true);
    	}
    	
        return username + "test Success" + username;
    }


	
	

}
