package co.edu.unbosque.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Utilidad para cifrar y descifrar texto empleando el algoritmo AES en modo CBC con padding PKCS5.
 * 
 * NOTAS DE SEGURIDAD:
 * - El uso de una llave (key) y vector de inicialización (IV) fijos únicamente debe hacerse en entornos de pruebas.
 * - Para producción se recomienda generar llaves seguras y IV aleatorios por mensaje, gestionados mediante un KMS o almacén seguro.
 * - Las llaves y el IV aquí se convierten directamente con {@code String#getBytes()} (charset por defecto); para mayor control usar UTF-8 explícito.
 * - El algoritmo utilizado es AES de 128 bits siempre que la llave tenga longitud de 16 bytes. Si se requiere 192/256 bits se debe ajustar la longitud y políticas de cifrado del JRE.
 * - Este utilitario no implementa autenticación (no provee integridad). Para escenarios sensibles considere AES en modo GCM o añadir HMAC.
 *
 */
public class AESUtil {

	/** Nombre del algoritmo simétrico. */
	private static final String ALGORITMO = "AES";

	/** Transformación usada: modo CBC con padding PKCS5. */
	private static final String TIPO_CIFRADO = "AES/CBC/PKCS5Padding";

	/**
	 * Constructor privado para evitar instanciación de la clase utilitaria.
	 */
	private AESUtil() {
		// Intencionalmente vacío.
	}

	/**
	 * Cifra un texto plano usando AES CBC.
	 *
	 * @param llave llave simétrica (debe tener 16 bytes para AES-128)
	 * @param iv vector de inicialización (16 bytes)
	 * @param texto texto plano a cifrar
	 * @return texto cifrado en Base64, o null si ocurre un error
	 */
	public static String encrypt(String llave, String iv, String texto) {
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(TIPO_CIFRADO);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		}

		SecretKeySpec secretKeySpec = new SecretKeySpec(llave.getBytes(), ALGORITMO);
		IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
		try {
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
		} catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
			e.printStackTrace();
			return null;
		}

		byte[] encrypted;
		try {
			encrypted = cipher.doFinal(texto.getBytes());
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			return null;
		}

		return new String(encodeBase64(encrypted));
	}

	/**
	 * Descifra un texto en Base64 cifrado con AES CBC.
	 *
	 * @param llave llave simétrica usada en el cifrado
	 * @param iv vector de inicialización empleado en el cifrado
	 * @param encrypted texto cifrado en Base64
	 * @return texto plano resultante o null si ocurre un error
	 */
	public static String decrypt(String llave, String iv, String encrypted) {
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(TIPO_CIFRADO);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		}

		SecretKeySpec secretKeySpec = new SecretKeySpec(llave.getBytes(), ALGORITMO);
		IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
		byte[] enc = decodeBase64(encrypted);
		try {
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
		} catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
			e.printStackTrace();
			return null;
		}

		byte[] decrypted;
		try {
			decrypted = cipher.doFinal(enc);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			return null;
		}

		return new String(decrypted);
	}

	/**
	 * Descifra un texto usando la llave e IV por defecto definidos en la clase.
	 * Sólo para escenarios simplificados o de pruebas.
	 *
	 * @param encrypted texto cifrado en Base64
	 * @return texto plano o null si ocurre un error
	 */
	public static String decrypt(String encrypted) {
		String iv = "programacioncomp";
		String key = "llavede16carater";
		return decrypt(key, iv, encrypted);
	}

	/**
	 * Cifra un texto usando la llave e IV por defecto definidos en la clase.
	 * Sólo para escenarios simplificados o de pruebas.
	 *
	 * @param plainText texto plano a cifrar
	 * @return texto cifrado en Base64 o null si ocurre un error
	 */
	public static String encrypt(String plainText) {
		String iv = "programacioncomp";
		String key = "llavede16carater";
		return encrypt(key, iv, plainText);
	}
}
