package ldap;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.ldap.InitialLdapContext;

public class LdapServiceImpl {
	

	public boolean authenticateUser(String username, String password) {
		// TODO Auto-generated method stub
		
		boolean isContextCreated = false;
		
		Hashtable<String, String> env = new Hashtable<String, String>();

		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://dluads1/dc=sapient,dc=com?sAMAccountName?sub?(objectClass=*) ldap://dluads2/dc=sapient,dc=com?sAMAccountName?sub?(objectClass=*)");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, "Sapient\\" + username);
		env.put(Context.SECURITY_CREDENTIALS, password);

		try {
			new InitialDirContext(env);
			new InitialLdapContext(env, null);
			System.out.println("Validated");
			isContextCreated = true;


		} catch (Exception e) {
		
			System.out.println("not Validated");
			isContextCreated = false;
			//addActionError("Invalid username or password");
		}
		
		return isContextCreated;
	} 
	
	public Hashtable<String, String> getEnvironment(String username, String password) {
		Hashtable<String, String> env = new Hashtable<String, String>();

		env.put(Context.SECURITY_AUTHENTICATION, "simple");	
		env.put(Context.SECURITY_PRINCIPAL,username);
		env.put(Context.SECURITY_CREDENTIALS,password);
		env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL,"ldap://dluads1/dc=sapient,dc=com?sAMAccountName?sub?(objectClass=*) ldap://dluads2/dc=sapient,dc=com?sAMAccountName?sub?(objectClass=*)" );
		return env;
	}

}
