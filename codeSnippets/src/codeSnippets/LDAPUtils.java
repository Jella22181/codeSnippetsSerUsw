package codeSnippets;
//package de.ser.lbbw.agents.ldapAuthenticator;

import java.io.ByteArrayInputStream;

import java.io.FileInputStream;

import java.io.FileNotFoundException;

import java.io.FileReader;

import java.io.IOException;

import java.io.ObjectInputStream;

import java.io.UnsupportedEncodingException;

import java.math.BigInteger;

import java.security.InvalidKeyException;

import java.security.NoSuchAlgorithmException;

import java.util.ArrayList;

import java.util.Enumeration;

import java.util.HashMap;

import java.util.Hashtable;

import java.util.List;

import java.util.Properties;

import java.util.StringTokenizer;

import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;

import javax.crypto.Cipher;

import javax.crypto.IllegalBlockSizeException;

import javax.crypto.NoSuchPaddingException;

import javax.crypto.spec.SecretKeySpec;

import javax.naming.AuthenticationException;

import javax.naming.Context;

import javax.naming.InitialContext;

import javax.naming.NamingEnumeration;

import javax.naming.NamingException;

import javax.naming.directory.Attribute;

import javax.naming.directory.Attributes;

import javax.naming.directory.DirContext;

import javax.naming.directory.InitialDirContext;

import javax.naming.directory.SearchControls;

import javax.naming.directory.SearchResult;

public class LDAPUtils {

	// IST: maiks + Kühns code zusammen

	private String _strLDAPHost = null; // LDAP-Server

	private Hashtable _htGroupCache = null; // Cache für die Gruppe

	/**
	 * 
	 * Creates a hash-table with default LDAP-parameters. The LDAP v2 defines three
	 * types of authentication: anonymous, simple (clear-text password), and
	 * 
	 * Kerberos v4. The LDAP v3 supports anonymous, simple, and SASL authentication.
	 * SASL is the Simple Authentication and Security Layer (RFC 2222).
	 *
	 * 
	 * 
	 * @param sSecurityPrincipal
	 * 
	 *            Security principal for this LDAP-connection.
	 * 
	 * @param sPassword
	 * 
	 *            LDAP password.
	 * 
	 * @param sProviderURL
	 * 
	 *            LDAP-provider URL.
	 * 
	 * @return Hash-table with default LDAP-parameters.
	 * 
	 */

	public static final Hashtable<String, String> getDefaultParametersLDAP(String sSecurityPrincipal, String sPassword,

			String sProviderURL) {

		Hashtable<String, String> env = new Hashtable<>();

		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");

		env.put(Context.PROVIDER_URL, sProviderURL);

		env.put(Context.SECURITY_AUTHENTICATION, "simple");

		env.put(Context.SECURITY_PRINCIPAL, sSecurityPrincipal);

		env.put(Context.SECURITY_CREDENTIALS, sPassword);

		return env;

	}

	private DirContext initialDirContext(Hashtable htEnv) throws NamingException {

		// Set up the environment for creating the initial context

		htEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");

		htEnv.put(Context.PROVIDER_URL, _strLDAPHost);

		htEnv.put(Context.SECURITY_AUTHENTICATION, "simple");

		// Create the initial context

		DirContext dirCtx = new InitialDirContext(htEnv);

		return dirCtx;

	}

	/**
	 * 
	 * User an LDAP-Server anmelden
	 *
	 * 
	 * 
	 * @param strUserId
	 * 
	 *            - User-ID des anzumeldenden Benutzers
	 * 
	 * @param strPassword
	 * 
	 *            - Kennwort zur User-ID
	 * 
	 * @param strContext
	 * 
	 *            - Context in dem die User-ID gefunden wird. Format: "ou=xx, o=xx"
	 *            Beispiel: "ou=LOGIN,o=LBSBW"
	 * 
	 * @return boolean - True = Anmeldung hat geklappt, False = Anmeldung ist
	 *         fehlgschlagen
	 * 
	 */

	public boolean login(String strUserId, String strPassword, String strShortContext, String strApp) {

		if (strUserId.length() == 0) {

			return false;

		}

		if (strPassword.length() == 0) {

			return false;

		}

		String strContext = longContext(strShortContext);

		String strDummy = strUserId.substring(0, 1);

		String strUser = strDummy.toUpperCase() + strUserId.substring(1);

		String strSecurityPrincipal = longName(strUser, strContext);

		boolean bRc = false;

		try {

			Hashtable htEnvLookup = new Hashtable();

			htEnvLookup.put(Context.SECURITY_PRINCIPAL, strSecurityPrincipal);

			DirContext dirCtxLookup = initialDirContext(htEnvLookup);

			String strObjectToSearch = "cn=" + strUser + "," + strContext;

			String strFilter = "(cn=*)";

			SearchControls sc = new SearchControls();

			sc.setSearchScope(SearchControls.OBJECT_SCOPE);

			NamingEnumeration ne = dirCtxLookup.search(strObjectToSearch, strFilter, sc);

			if (ne.hasMore()) {

				SearchResult sr = (SearchResult) ne.next();

				String strUserName = sr.getName();

				int iPos = strUserName.lastIndexOf("/") + 1;

				strUserName = strUserName.substring(iPos);

				Hashtable htEnvAuth = new Hashtable();

				htEnvAuth.put(Context.SECURITY_PRINCIPAL, strUserName);

				htEnvAuth.put(Context.SECURITY_CREDENTIALS, strPassword);

				DirContext dirCtxAuth = initialDirContext(htEnvAuth);

				bRc = checkAppPermission(dirCtxAuth, strUserId, strApp);

			} else {

				// Benutzer nicht gefunden

				bRc = false;

			}

		} catch (AuthenticationException exc) {

			/* exc.printStackTrace (); */ bRc = false;

		} catch (NamingException exc) {

			/* exc.printStackTrace (); */ bRc = false;

		}

		return bRc;

	}

	/**
	 * 
	 * Prüfen, ob User berechtigt ist die Applikation zu starten.
	 *
	 * 
	 * 
	 * @param strUserId
	 * 
	 *            - Benutzerkennung
	 * 
	 * @param strApp
	 * 
	 *            - Anwendung, für die die Berechtigung geprüft werden soll, for
	 *            future use
	 * 
	 * @return boolean - True = Benutzer berechtigt, False = Benutzer nicht
	 *         berechtigt
	 * 
	 */

	private boolean checkAppPermission(DirContext dirCtx, String strUserId, String strApp) {

		boolean bRc = false;

		String strUser = strUserId.toUpperCase();

		if (_htGroupCache != null) {

			// Zuerst im cache nachschauen:

			Object o = _htGroupCache.get(strUser);

			if (o != null) {

				return true;

			}

		}

		// Benutzer nicht in Hash-Tabelle gefunden

		// Gruppen noch nicht gecacht oder

		// vielleicht wurde er der Gruppe nachträglich zugeorndet

		try {

			Hashtable htNewGroupCache = new Hashtable();

			String strShortGroup = getGroupNameForApplication(strApp);

			String strLongGroup = longName(strShortGroup, getGroupContext());

			Attributes answer = dirCtx.getAttributes(strLongGroup);

			// BS.ASSERT(answer != null); // Gruppe nicht gefunden !;

			// Memberliste durchsuchen

			Attribute a = answer.get("member");

			boolean found = false;

			for (NamingEnumeration ne = a.getAll(); ne.hasMore();) {

				StringTokenizer strTok1 = new StringTokenizer((String) ne.next(), ",");

				StringTokenizer strTok2 = new StringTokenizer(strTok1.nextToken(), "=");

				while (strTok2.hasMoreTokens() == true) {

					String parm = strTok2.nextToken();

					String value = strTok2.nextToken().toUpperCase();

					if (parm.equals("cn") == true) {

						if (htNewGroupCache != null) {

							// Gruppencache füllen

							htNewGroupCache.put(value, value);

						}

						if (value.equals(strUser) == true) {

							// System.out.println ( aUserId + " wurde gefunden !" );

							found = true;

							bRc = true;

						}

					}

				} // end while

			} // end for

			_htGroupCache = htNewGroupCache;

		} catch (NamingException e) {

			e.printStackTrace();

			bRc = false;

		}

		return bRc;

	}

	public static String getGroupNameForApplication(String strApplication) {

		return authLdapGroupContextCN(strApplication);

	}

	/**
	 * 
	 * Liefert den Context Name des GroupContexts fuer die Berechtigungsgruppe im
	 * LDAP.
	 *
	 * 
	 * 
	 * @param strApplication
	 * 
	 *            Applikationsname, für die der Gruppenkontext ermittelt werden soll
	 *
	 * 
	 * 
	 * @return ldapGroupContexto
	 * 
	 */

	private static String authLdapGroupContextCN(String strApplication) {

		// Default-Wert (Applikations-unabhängig)

		// String strGroupContextCN =
		// BSOptions.instance().getPropertyString(PROPKEY_AUTH_LDAP_GROUPCONTEXT_CN,

		// "INTRANET_AUSKUNFT");

		String strGroupContextCN = "";

		// Wenn strApplication gegeben ist, dann nach spezifischem Wert suchen.

		if (strApplication != null) {

			// strGroupContextCN = BSOptions.instance()

			// .getPropertyString(PROPKEY_AUTH_LDAP_GROUPCONTEXT_CN + "." + strApplication,
			// strGroupContextCN);

			strGroupContextCN = strGroupContextCN;

		}

		// Trace.printI("auth_ldap_groupcontext_cn: " + strGroupContextCN);

		return strGroupContextCN;

	}

	public static String getGroupContext() {

		// String[] astr = BSTokenString.tokenize(authLdapGroupContextOU(), '.', true);

		String[] astr = null;

		StringBuffer sbLong = new StringBuffer();

		for (int idx = 0; idx < astr.length; idx++) {

			if (idx == 0) {

				sbLong.append("ou=");

			} else {

				sbLong.append(",ou=");

			}

			sbLong.append(astr[idx]);

		}

		sbLong.append(",o=");

		sbLong.append(authLdapGroupContextO());

		String strLong = sbLong.toString();

		// Trace.printI("getGroupContext ->" + strLong);

		return strLong;

	}

	/**
	 * 
	 * Liefert die Organistation des GroupContexts fuer die Berechtigungsgruppe im
	 * LDAP.
	 *
	 * 
	 * 
	 * @return ldapGroupContext
	 * 
	 */

	private static String authLdapGroupContextO() {

		// String strGroupContext =
		// BSOptions.instance().getPropertyString(PROPKEY_AUTH_LDAP_GROUPCONTEXT_O,
		// "lbsbw");

		String strGroupContext = "";

		// Trace.printI("auth_ldap_groupcontext_o: " + strGroupContext);

		return strGroupContext;

	}

	/**
	 * 
	 * Liefert die Organistational Unit des GroupContexts fuer die
	 * Berechtigungsgruppe im LDAP.
	 *
	 * 
	 * 
	 * @return ldapGroupContexto
	 * 
	 */

	private static String authLdapGroupContextOU() {

		// String strGroupContext =
		// BSOptions.instance().getPropertyString(PROPKEY_AUTH_LDAP_GROUPCONTEXT_OU,

		// "group.apps.zen");

		String strGroupContext = "";

		// Trace.printI("auth_ldap_groupcontext_ou: " + strGroupContext);

		return strGroupContext;

	}

	// /**

	// * Konstruktor mit Connection-String für LDAP-Server

	// *

	// * @param strLDAPServer

	// * - Adresse des LDAP-Servers. Format: "ldap://ip-address:ldap-port" Beispiel:
	// "ldap://FS11.wuertt.lbs.de:389"

	// */

	// public LDAPUtility(String strLDAPServer) {

	// _strLDAPHost = strLDAPServer;

	// }

	// private DirContext initialDirContext(Hashtable htEnv) throws NamingException
	// {

	// // Set up the environment for creating the initial context

	// htEnv.put(Context.INITIAL_CONTEXT_FACTORY,
	// "com.sun.jndi.ldap.LdapCtxFactory");

	// htEnv.put(Context.PROVIDER_URL, _strLDAPHost);

	// htEnv.put(Context.SECURITY_AUTHENTICATION, "simple");

	//

	// // Create the initial context

	// DirContext dirCtx = new InitialDirContext(htEnv);

	// return dirCtx;

	// }

	public static String longName(String strName, String strContext) {

		StringBuffer sbLong = new StringBuffer();

		sbLong.append("cn=");

		sbLong.append(strName);

		sbLong.append(",");

		sbLong.append(strContext);

		String strLong = sbLong.toString();

		return strLong;

	}

	public static String longContext(String strShortContext) {

		// String[] astr = BSTokenString.tokenize(strShortContext, '.', true);TODO
		// wieder rein

		String[] astr = null;// TODO wieder raus

		StringBuffer sbLong = new StringBuffer();

		for (int idx = 0; idx < astr.length; idx++) {

			if (idx != 0) {

				sbLong.append(",");

			}

			if (idx != astr.length - 1) {

				sbLong.append("ou=");

			} else {

				sbLong.append("o=");

			}

			sbLong.append(astr[idx]);

		}

		String strLong = sbLong.toString();

		return strLong;

	}

	public static HashMap getHashMapProperties(String path) {

		HashMap hashMap = new HashMap<String, String>();

		FileReader reader = null;

		try {

			reader = new FileReader(path);

			/*
			 * 
			 * The Properties class represents a persistent set of properties. The
			 * Properties can be saved to a stream or loaded from a stream. Each key and its
			 * 
			 * corresponding value in the property list is a string.
			 *
			 *
			 * 
			 * 
			 * 
			 * Because Properties inherits from Hashtable, the put and putAll methods can be
			 * applied to a Properties object. Their use is strongly discouraged as
			 * 
			 * they allow the caller to insert entries whose keys or values are not Strings.
			 * The setProperty method should be used instead. If the store or save
			 * 
			 * method is called on a "compromised" Properties object that contains a
			 * non-String key or value, the call will fail. Similarly, the call to the
			 * 
			 * propertyNames or list method will fail if it is called on a "compromised"
			 * Properties object that contains a non-String key.
			 * 
			 */

			Properties p = new Properties();

			Properties properties = new Properties();

			properties.load(new FileInputStream(path));

			Enumeration<?> keys = properties.propertyNames();

			while (keys.hasMoreElements()) {

				String key = (String) keys.nextElement();

				hashMap.put(key, properties.getProperty(key));

			}

			reader.close();

		} catch (FileNotFoundException e1) {

			e1.printStackTrace();

		}

		catch (IOException e) {

			e.printStackTrace();

		}

		return hashMap;

	}

	/**
	 * 
	 * Decodes the common input parameters, which are CSV encoded.
	 *
	 * 
	 * 
	 * @param csvString
	 * 
	 *            CSV-encoded String.
	 * 
	 * @return List of String values of the decoded properties.
	 * 
	 */

	public static String decode(String secret) throws NoSuchPaddingException, NoSuchAlgorithmException,

			InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

		byte[] kbytes = "jaas is the way".getBytes();

		SecretKeySpec key = new SecretKeySpec(kbytes, "Blowfish");

		BigInteger n = new BigInteger(secret, 16);

		byte[] encoding = n.toByteArray();

		// SECURITY-344: fix leading zeros

		if (encoding.length % 8 != 0) {

			int length = encoding.length;

			int newLength = ((length / 8) + 1) * 8;

			int pad = newLength - length; // number of leading zeros

			byte[] old = encoding;

			encoding = new byte[newLength];

			for (int i = old.length - 1; i >= 0; i--)

				encoding[i + pad] = old[i];

			// SECURITY-563: handle negative numbers

			if (n.signum() == -1) {

				for (int i = 0; i < newLength - length; i++)

					encoding[i] = (byte) -1;

			}

		}

		Cipher cipher = Cipher.getInstance("Blowfish");

		cipher.init(Cipher.DECRYPT_MODE, key);

		byte[] decode = cipher.doFinal(encoding);

		return new String(decode);

	}

	/**
	 * 
	 * The agent(technical user) should login to LDAP and retrieve the full DN of
	 * the user who tries to login via webCube-interface.
	 *
	 * 
	 * 
	 * @param strAgentLogin
	 * 
	 *            Technical user of the agent.
	 * 
	 * @param strAgentToken
	 * 
	 *            Token of the agent.
	 * 
	 * @param strProviderURL
	 * 
	 *            LDAP-provider URL.
	 * 
	 * @param strUserLogin
	 * 
	 *            User login name.
	 * 
	 * @param strBaseDN
	 * 
	 *            User base-DN.
	 * 
	 * @return Distinguished name(full DN) of the user, or "null" if no DN could be
	 *         found.
	 * 
	 */// ist: admin,[cn]=admin,ldap:...

	public final static String doRetrieveFullDN(String strAgentLogin, String strAgentToken, String strProviderURL,

			String strUserLogin, String strBaseDN) {

		String strDistinguishedName = null;

		// Paremeters to authenticate the agent(technical user) via LDAP:

		Hashtable<String, String> env = getDefaultParametersLDAP(strAgentLogin, strAgentToken, strProviderURL);

		try {

			DirContext ctx = new InitialDirContext(env); // difference 'InitialDirContext' / 'InitialContext' ?

			// log.debug(" LDAP-Context OK, for technical user...");

			// strBaseDN --> "dc=%s,dc=%s"

			// Try to find the full DN of the user 'strUserName':

			strDistinguishedName = findDistinguishedName(ctx, strBaseDN, strUserLogin);

			if (strDistinguishedName != null) {

				// log.debug(" Distinguished-Name OK");
				System.out.println("----------------");

			} else {

				// agentExecutionResult = AuthenticationAgentHelper.createResultFailed("No vaild
				// DN!");

				// String.format("There're no attributes at all, regarding BaseDN(%s) and
				// Login(%s)!", strBaseDN, strUserLogin));

				// log.debug(" Process failed: " + agentExecutionResult.getExecutionMessage());

			}

			ctx.close();

		} catch (final NamingException e) {// ist: natürlich: javax.naming.CommunicationException:
											// FS11.wuertt.lbs.de:389 [Root exception is java.net.UnknownHostException:
											// FS11.wuertt.lbs.de]

			Throwable th = e.getRootCause();

			// log.debug( String.format("NamingException: %s\n(Cause: %s)", e.getMessage(),
			// th.toString()) );

			// agentExecutionResult =
			// AuthenticationAgentHelper.createResultFailed(e.getMessage());

		}

		return strDistinguishedName;

	}

	/**
	 * 
	 * A LDAP-search is executed.
	 *
	 * 
	 * 
	 * @param ctx
	 * 
	 *            The "DirContext" of the current LDAP-connection is used.
	 * 
	 * @param ldapSearchBase
	 * 
	 *            The base DN of the user is searched for.
	 * 
	 * @param accountName
	 * 
	 *            The account name of the user is searched for.
	 * 
	 * @return A distinguished name, if a single search result exists. Null is
	 *         returned if there're more or no hits.
	 * 
	 * @throws NamingException
	 * 
	 *             If something went wrong with the search.
	 * 
	 */

	public static final String findDistinguishedName(DirContext ctx, String strBaseDN, String strUserLogin) {

		String strSearchFilter = String.format("(&(objectClass=user)(cn=%s))", strUserLogin);

		SearchControls searchControls = new SearchControls();

		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		// log.debug(String.format(" LDAP search... BaseDN: %s SearchFilter: %s",
		// strBaseDN, strSearchFilter));

		NamingEnumeration<SearchResult> results = null;

		try {

			results = ctx.search(strBaseDN, strSearchFilter, searchControls);

		} catch (NamingException ex) {

			// log.debug(" Failed: " + ex.getMessage());

		}

		if (results != null) {

			// log.debug(" Search results OK...");

			if (results.hasMoreElements()) {

				SearchResult searchResult = (SearchResult) results.nextElement();

				// log.debug(" NameInNamespace: " + searchResult.getNameInNamespace() );

				// Make sure there is not another item available, there should be only 1 match:

				if (results.hasMoreElements()) {

					// log.error(String.format(" Matched multiple users for the context <%s> and the
					// filter <%s>!", strBaseDN, strSearchFilter));

					return null;

				}

				return searchResult.getNameInNamespace();

			} else {

				// log.debug(" Failed: No elements found!");

			}

		}

		return null;

	} // findAccountByAccountName

	/**
	 * 
	 * Try to authenticate the user with it's password and full-DN.
	 *
	 * 
	 * 
	 * @param strUserLogin
	 * 
	 *            User login name.
	 * 
	 * @param strUserToken
	 * 
	 *            User login token.
	 * 
	 * @param strProviderURL
	 * 
	 *            LDAP-provider URL.
	 * 
	 * @param strUserDN
	 * 
	 *            User login full-DN.
	 * 
	 * @return Result-code of the AgentExecutionResult.
	 * 
	 */

	public final static int doAuthenticateUserWithDN(String strUserLogin, String strUserToken, String strProviderURL,

			String strUserDN) {

		Hashtable<String, String> env = getDefaultParametersLDAP(strUserDN, strUserToken, strProviderURL);

		try {

			Context ctx = new InitialContext(env);

			// log.debug(" LDAP-Context OK, for " + strUserLogin);

			ctx.close();

		} catch (final NamingException e) {

			Throwable th = e.getRootCause();

			// log.debug( String.format("NamingException: %s\n(Cause: %s)", e.getMessage(),
			// th.toString()) );

			return 0;

		}

		// !!! BE AWARE !!!

		/// ...of this tiny detail: "strUserLogin" is transferred to the CSB and used
		/// for authentication in turn !!!

		// OTHERWISE: com.ser.sedna.client.exception.ServerException: Doxis4
		// CSB-Fehlernummer: ORGA0101I: Login is not allowed for XYZ within customer ABC

		// log.debug(" User successfully authenticated.");

		return 0;

	}

}
