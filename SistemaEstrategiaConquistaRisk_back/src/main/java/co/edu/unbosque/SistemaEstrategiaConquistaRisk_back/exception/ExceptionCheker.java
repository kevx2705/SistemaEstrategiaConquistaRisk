package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.exception;

import java.util.InputMismatchException;

/**
 * Clase ExceptionCheker.
 * Proporciona métodos para validar contraseñas, correos y texto
 * según diferentes criterios.
 */
public class ExceptionCheker {

    /**
     * Verifica que una contraseña cumpla con todos los criterios de validación.
     *
     * @param a La contraseña a validar.
     * @throws CharacterException Si no tiene al menos 8 caracteres.
     * @throws NumberException    Si no contiene al menos un número.
     * @throws SymbolException    Si no contiene al menos un símbolo especial.
     */
    public static void checkerPasword(String a)
            throws CharacterException, NumberException, SymbolException {

        checkerCharacter(a);
        checkerNumber(a);
        checkerSymbol(a);
    }

    /**
     * Verifica que el correo tenga un formato válido.
     *
     * @param a El correo a validar.
     * @throws MailException Si el formato del correo no es válido.
     */
    public static void checkerMail(String a) throws InputMismatchException, MailException {
        if (!a.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new MailException();
        }
    }

    /**
     * Verifica que la contraseña tenga al menos 8 caracteres.
     *
     * @param a La contraseña a validar.
     * @throws CharacterException Si la longitud es menor a 8 caracteres.
     */
    public static void checkerCharacter(String a) throws CharacterException {
        if (a.length() < 8) {
            throw new CharacterException();
        }
    }

    /**
     * Verifica que dos contraseñas sean iguales.
     *
     * @param a Contraseña original.
     * @param b Confirmación.
     * @throws EqualPasswordException Si no son iguales.
     */
    public static void checkerEqualPassword(String a, String b) throws EqualPasswordException {
        if (!a.equals(b)) {
            throw new EqualPasswordException();
        }
    }

    /**
     * Verifica que la contraseña contenga al menos un número.
     *
     * @param a La contraseña a validar.
     * @throws NumberException Si no contiene números.
     */
    public static void checkerNumber(String a) throws NumberException {
        if (!a.matches(".*\\d.*")) {
            throw new NumberException();
        }
    }

    /**
     * Verifica que la contraseña contenga al menos un símbolo especial.
     *
     * @param a La contraseña a validar.
     * @throws SymbolException Si no contiene símbolos especiales.
     */
    public static void checkerSymbol(String a) throws SymbolException {
        if (!a.matches(".*[^A-Za-z0-9].*")) {
            throw new SymbolException();
        }
    }

    /**
     * Verifica que el valor no sea negativo.
     *
     * @param a Número a validar.
     * @throws NegativeNumberException Si el número es menor a 0.
     */
    public static void checkerNegativeNumber(int a) throws NegativeNumberException {
        if (a < 0) {
            throw new NegativeNumberException();
        }
    }

    /**
     * Verifica que el texto solo contenga letras y espacios.
     *
     * @param a Texto a validar.
     * @throws TextException Si contiene números o símbolos.
     */
    public static void checkerText(String a) throws TextException {
        if (!a.matches("[A-Za-záéíóúÁÉÍÓÚñÑ ]+")) {
            throw new TextException();
        }
    }

    /**
     * Verifica que lo ingresado sea solo un número.
     *
     * @param a Valor a validar.
     * @throws InputMismatchException Si no es un número válido.
     */
    public static void checkerNumber(int a) throws InputMismatchException {
        throw new InputMismatchException();
    }
}