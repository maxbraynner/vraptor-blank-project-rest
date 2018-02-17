package br.com.api.util;

import org.mindrot.jbcrypt.BCrypt;

public class UtilPassword {

	public static String crypt(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt(12));
	}
	
	public static boolean check(String candidate, String crypted) {
		return BCrypt.checkpw(candidate, crypted);
	}
	
	public static void main(String[] args) {
		String password = "123";
		
		String crypt = crypt(password);
		
		System.out.println(crypt);
		
		boolean check = check(password, crypt);
		
		System.out.println(check);
	}
	
}
