package com.holdings.server.service.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValueValidate {
	/*
	 * Validate user name.
	 * 
	 * 4-20 digits.
	 * 
	 * Can only be letters (case sensitive), numbers, and underscores.
	 * 
	 * Cannot start and end with underscores.
	 */
	public static boolean validateUserName(String target) {
		Pattern pattern = Pattern.compile("^[a-zA-Z\\d]\\w{2,18}[a-zA-Z\\d]$");
		Matcher matcher = pattern.matcher(target);
		return matcher.find();
	}

	/*
	 * Validate email.
	 * 
	 * Couldn't start or finish with a dot
	 * 
	 * Shouldn't contain spaces into the string
	 * 
	 * Shouldn't contain special chars (<:, *,ecc)
	 * 
	 * Could contain dots in the middle of mail address before the @
	 * 
	 * Could contain a double doman ( '.de.org' or similar rarity)
	 */
	public static boolean validateEmail(String target) {
		Pattern pattern = Pattern.compile("^((?!\\.)[\\w-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$");
		Matcher matcher = pattern.matcher(target);
		return matcher.find();
	}

	/*
	 * Validate password.
	 * 
	 * Must 8-16 characters with no space.
	 */
	public static boolean validatePassword(String target) {
		Pattern pattern = Pattern.compile("^([^\\s]){8,16}$");
		Matcher matcher = pattern.matcher(target);
		return matcher.find();
	}
}
