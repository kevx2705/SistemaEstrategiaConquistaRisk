package co.edu.unbosque.SistemaEstrategiaConquistaRisk_back.exception;

import java.util.InputMismatchException;

/**
 * Clase {@code ExceptionCheker}. Proporciona métodos estáticos para validar
 * contraseñas, correos electrónicos y otros criterios según reglas específicas.
 * Cada método lanza una excepción personalizada si la validación falla.
 */
public class ExceptionCheker {

    /**
     * Verifica que una contraseña cumpla con los criterios básicos de validación:
     * longitud mínima, presencia de números y símbolos.
     *
     * @param a La contraseña a validar.
     * @throws CharacterException Si la contraseña tiene menos de 8 caracteres.
     * @throws NumberException Si la contraseña no contiene al menos un número.
     * @throws SymbolException Si la contraseña no contiene al menos un símbolo especial.
     */
    public static void checkerPasword(String a)
            throws CharacterException, NumberException, SymbolException {

        checkerCharacter(a);
        checkerNumber(a);
        checkerSymbol(a);
    }

    /**
     * Valida que un correo electrónico tenga un formato válido.
     *
     * @param a El correo electrónico a validar.
     * @throws InputMismatchException Si el formato del correo no es válido.
     * @throws MailException Si el correo no cumple con el patrón esperado.
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
     * @param a Primera contraseña.
     * @param b Segunda contraseña.
     * @throws EqualPasswordException Si las contraseñas no coinciden.
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
     * Verifica que un número no sea negativo.
     *
     * @param a El número a validar.
     * @throws NegativeNumberException Si el número es negativo.
     */
    public static void checkerNegativeNumber(int a) throws NegativeNumberException {
        if (a < 0) {
            throw new NegativeNumberException();
        }
    }

    /**
     * Verifica que una cadena contenga solo letras (incluyendo tildes y ñ).
     *
     * @param a La cadena a validar.
     * @throws TextException Si la cadena contiene caracteres no permitidos.
     */
    public static void checkerText(String a) throws TextException {
        if (!a.matches("[A-Za-záéíóúÁÉÍÓÚñÑ]+")) {
            throw new TextException();
        }
    }

    /**
     * Método obsoleto. Verifica que un valor sea un número (siempre lanza excepción).
     * <b>Nota:</b> Este método siempre lanza {@link InputMismatchException}.
     *
     * @param a El valor a validar (no se usa).
     * @throws InputMismatchException Siempre se lanza esta excepción.
     * @deprecated Este método no implementa lógica de validación útil.
     */
    @Deprecated
    public static void checkerNumber(int a) throws InputMismatchException {
        throw new InputMismatchException();
    }
}
