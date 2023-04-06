package utilidades;

public class Consola {
    /**
     * Constructor privado para evitar que java genere el suyo
     * por defecto y así evitar que se puedan instanciar objetos
     */
    private Consola() {}

    public static void salidaNormal(String mensaje) {
        System.out.println(mensaje);
    }

    public static void salidaError(String mensaje) {
        System.err.println(mensaje);
    }

    public static void salidaNormalSinSalto(String mensaje) {
        System.out.print(mensaje);
    }

    public static void saltoDeLinea() {
        System.out.println();
    }
}
