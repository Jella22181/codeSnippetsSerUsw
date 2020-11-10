package codeSnippets;

// de.ser.authentication.AuthenticationAgent

import java.io.UnsupportedEncodingException;

import java.security.InvalidKeyException;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import java.util.Map;

import java.util.Properties;

import javax.crypto.BadPaddingException;

import javax.crypto.IllegalBlockSizeException;

import javax.crypto.NoSuchPaddingException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.ldap.InitialLdapContext;

public class AuthenticationAgent {
	public static void main(String... args) {
		AuthenticationAgent a = new AuthenticationAgent();
		a.execute();
	}

	
	
//	The following example shows how, by using a simple clear-text password, a client authenticates to an LDAP server.
//
//    // Set up the environment for creating the initial context
//    Hashtable env = new Hashtable();
//    env.put(Context.INITIAL_CONTEXT_FACTORY, 
//        "com.sun.jndi.ldap.LdapCtxFactory");
//    env.put(Context.PROVIDER_URL, "ldap://localhost:389/o=JNDITutorial");
//
//    // Authenticate as S. User and password "mysecret"
//    env.put(Context.SECURITY_AUTHENTICATION, "simple");
//    env.put(Context.SECURITY_PRINCIPAL, "cn=S. User, ou=NewHires, o=JNDITutorial");
//    env.put(Context.SECURITY_CREDENTIALS, "mysecret");
//
//    // Create the initial context
//    DirContext ctx = new InitialDirContext(env);
//
//    // ... do something useful with ctx

	
	
	public void execute() {

		//The following example shows how, by using a simple clear-text password, a client authenticates to an LDAP server.

	    // Set up the environment for creating the initial context
	    Hashtable env = new Hashtable();
	    env.put(Context.INITIAL_CONTEXT_FACTORY, 
	        "com.sun.jndi.ldap.LdapCtxFactory");
	    env.put(Context.PROVIDER_URL, "ldap://localhost:20389/o=sevenSeas");

	    // Authenticate as S. User and password "mysecret"
	    env.put(Context.SECURITY_AUTHENTICATION, "simple");
	    env.put(Context.SECURITY_PRINCIPAL, "cn=humpert, ou=people, o=sevenSeas");
	    env.put(Context.SECURITY_CREDENTIALS, "humpert");

	    // Create the initial context
	    try {
			DirContext ctx = new InitialDirContext(env);
			System.out.println("new InitialDirContext");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    // ... do something useful with ctx
	    
	    
	    
	    Hashtable env1 = new Hashtable();
	    env1.put(Context.INITIAL_CONTEXT_FACTORY, 
	        "com.sun.jndi.ldap.LdapCtxFactory");
	    env1.put(Context.PROVIDER_URL, "ldap://localhost:20389/o=serPartition");

	    // Authenticate as S. User and password "mysecret"
	    env1.put(Context.SECURITY_AUTHENTICATION, "simple");
	    env1.put(Context.SECURITY_PRINCIPAL, "cn=humpert, ou=serUser, o=serPartition");
	    env1.put(Context.SECURITY_CREDENTIALS, "humpert");

	    // Create the initial context
	    try {
			DirContext ctx = new InitialDirContext(env1);
			System.out.println("new InitialDirContext serPartition");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    
	    
	    
	    Hashtable env2 = new Hashtable();
	    env2.put(Context.INITIAL_CONTEXT_FACTORY, 
	        "com.sun.jndi.ldap.LdapCtxFactory");
	    env2.put(Context.PROVIDER_URL, "ldap://localhost:20389/o=serPartition");

	    // Authenticate as S. User and password "mysecret"
	    env2.put(Context.SECURITY_AUTHENTICATION, "simple");
	    env2.put(Context.SECURITY_PRINCIPAL, "cn=Mike Schulze, ou=serUser, o=serPartition");
	    env2.put(Context.SECURITY_CREDENTIALS, "schulzem");

	    // Create the initial context
	    try {
			DirContext ctx = new InitialLdapContext(env2,null);
			System.out.println(" Schulzenew InitialLdapContext serPartition");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	// TODO mit mail gehts nicht    
//	    Hashtable env3 = new Hashtable();
//	    env3.put(Context.INITIAL_CONTEXT_FACTORY, 
//	        "com.sun.jndi.ldap.LdapCtxFactory");
//	    env3.put(Context.PROVIDER_URL, "ldap://localhost:20389/o=serPartition");
//
//	    // Authenticate as S. User and password "mysecret"
//	    env3.put(Context.SECURITY_AUTHENTICATION, "simple");
//	    env3.put(Context.SECURITY_PRINCIPAL, "miller@ser.de");
//	    env3.put(Context.SECURITY_CREDENTIALS, "miller");
//
//	    // Create the initial context
//	    try {
//			DirContext ctx = new InitialDirContext(env3);
//			System.out.println(" miller new InitialLdapContext serPartition");
//		} catch (NamingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	    
	    


	}

}
