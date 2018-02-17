package br.com.api.util;

import java.util.InputMismatchException;

/**
 * Created by caique on 12/02/2015.
 */
public class UtilValidationField {
	
	public static boolean isCNPJ(String CNPJ) {
		if (CNPJ == null || CNPJ.equals("")) {
			return false;
		}
		
		CNPJ = CNPJ.replaceAll("\\D", "");
				
		// verifica números repetidos, ex: sequencias de 1 ou 2: 1111111111111, 2222222222
		if (CNPJ.matches("^(\\d)\\1+$")) {
			return false;
		}
		
		char dig13, dig14;
		int sm, i, r, num, peso;

		try {
			sm = 0;
			peso = 2;
			for (i = 11; i >= 0; i--) {

				num = (int) (CNPJ.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10)
					peso = 2;
			}

			r = sm % 11;
			if ((r == 0) || (r == 1))
				dig13 = '0';
			else
				dig13 = (char) ((11 - r) + 48);

			sm = 0;
			peso = 2;
			for (i = 12; i >= 0; i--) {
				num = (int) (CNPJ.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10)
					peso = 2;
			}
			r = sm % 11;
			if ((r == 0) || (r == 1))
				dig14 = '0';
			else
				dig14 = (char) ((11 - r) + 48);

			if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13)))
				return (true);
			else
				return (false);
		} catch (Exception erro) {
			return (false);
		}
	}

	public static boolean isCPF(String CPF) {
		if (CPF==null || CPF.equals("")) {
			return false;
		}
		
		CPF = CPF.replaceAll("\\D", "");
		
		// considera-se erro CPF's formados por uma sequencia de numeros iguais
		if (CPF.equals("00000000000") || CPF.equals("11111111111") || CPF.equals("22222222222") ||
				CPF.equals("33333333333") || CPF.equals("44444444444") || CPF.equals("55555555555")
				|| CPF.equals("66666666666") || CPF.equals("77777777777") || CPF.equals("88888888888")
				|| CPF.equals("99999999999") || (CPF.length() != 11))
			return (false);
		char dig10, dig11;
		int sm, i, r, num, peso; // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
		try { // Calculo do 1o. Digito Verificador
			sm = 0;
			peso = 10;
			for (i = 0; i < 9; i++) {
				// converte o i-esimo caractere do CPF em um numero: // por exemplo, transforma o caractere '0' no inteiro 0 // (48 eh a posicao de '0' na tabela ASCII)
				num = (int) (CPF.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso - 1;
			}
			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11)) dig10 = '0';
			else dig10 = (char) (r + 48);
			// converte no respectivo caractere numerico // Calculo do 2o. Digito Verificador
			sm = 0;
			peso = 11;
			for (i = 0; i < 10; i++) {
				num = (int) (CPF.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso - 1;
			}
			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11)) dig11 = '0';
			else
				dig11 = (char) (r + 48); // Verifica se os digitos calculados conferem com os digitos informados.
			if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10))) return (true);
			else return (false);
		} catch (InputMismatchException erro) {
			return (false);
		}
	}

	public static boolean isEmail(String email){
		if (email==null || email.equals("")) {
			return false;
		}
		
		// regex de validação de email
		String pattern = "^.+@\\w+([.-]?\\w+)(\\.\\w{2,3})+$";
		return email.matches(pattern);
	}
	
}