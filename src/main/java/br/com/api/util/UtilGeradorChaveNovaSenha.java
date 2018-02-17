package br.com.api.util;

import java.util.Random;

public class UtilGeradorChaveNovaSenha {
	private static final char[] ALL_CHARS = new char[62];
	private static final Random RANDOM = new Random();
	static {
		for (int i = 48, j = 0; i < 123; i++) {
			if (Character.isLetterOrDigit(i)) {
				ALL_CHARS[j] = (char) i;
				j++;
			}
		}
	}

	private static String getChave(final int length) {
		final char[] result = new char[length];
		for (int i = 0; i < length; i++) {
			result[i] = ALL_CHARS[RANDOM.nextInt(ALL_CHARS.length)];
		}

		String key = new String(result);
		
		return key.toLowerCase();
	}

	public static String getChave() {
		return getChave(16);
	}
}
