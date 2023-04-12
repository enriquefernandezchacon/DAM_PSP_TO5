import modelo.Permiso;
import modelo.Rol;
import modelo.Usuario;
import utilidades.Consola;
import utilidades.Entrada;
import utilidades.MenuOption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    //Roles y permisos
    //Los permisos son un array con los diferentes permisos que puede tener un rol
    private static final Permiso[] permisosAdmin = {Permiso.AGREGAR_LIBRO, Permiso.EDITAR_LIBRO, Permiso.ELIMINAR_LIBRO, Permiso.AGREGAR_USUARIO, Permiso.EDITAR_USUARIO, Permiso.ELIMINAR_USUARIO};
    private static final Permiso[] permisosBibliotecario = {Permiso.AGREGAR_LIBRO, Permiso.EDITAR_LIBRO, Permiso.ELIMINAR_LIBRO};
    private static final Permiso[] permisosUsuario = {Permiso.VER_LIBROS};
    private static Rol admin;
    private static Rol bibliotecario;
    private static Rol usuario;
    private static List<Usuario> usuarios;
    private static boolean programaCorriendo = true;
    private static String usuarioActual;
    private static String contrasenaActual;
    private static Usuario user;
    private static final List<MenuOption> MENU_OPTIONS = Arrays.asList(
            new MenuOption("Ver libros disponibles", Permiso.VER_LIBROS),
            new MenuOption("Agregar un libro", Permiso.AGREGAR_LIBRO),
            new MenuOption("Editar un libro", Permiso.EDITAR_LIBRO),
            new MenuOption("Eliminar un libro", Permiso.ELIMINAR_LIBRO),
            new MenuOption("Agregar un usuario", Permiso.AGREGAR_USUARIO),
            new MenuOption("Editar un usuario", Permiso.EDITAR_USUARIO),
            new MenuOption("Eliminar un usuario", Permiso.ELIMINAR_USUARIO),
            new MenuOption("Cerrar sesión", null)
    );
    //REGEX para que la contraseña tenga al menos una letra minúscula y un número
    private static final String REGEX_CONTRASENA = "^(?=.*[a-z])(?=.*[0-9]).+$";
    //REGEX para que el nombre de usuario tenga el formato 'nombre_#', donde 'nombre' es una cadena de caracteres en minúsculas y '#' es un número
    //con un guión bajo en medio
    private static final String REGEX_USUARIO = "^[a-z]+_[0-9]+$";

    public static void main(String[] args) {
        Consola.salidaNormal("Iniciando programa...");
        esperarSegundo();
        Consola.salidaNormal("Creando roles...");
        crearRoles();
        esperarSegundo();
        Consola.salidaNormal("Creando usuarios...");
        crearUsuarios();
        ejecucionDelPrograma();
        cerrarPrograma();
    }

    /**
     * Este metodo controla el bucle principal de ejecución.
     */
    private static void ejecucionDelPrograma() {
        do {
            try {
                Consola.saltoDeLinea();
                //Se solicita el inicio de sesión, si es incorrecto, se caza la excepción y se vuelve a solicitar al
                //reiniciar el try-catch
                inicioSesion();
                do {
                    mostrarMenu();
                    int opcion = elegirOpcion();
                    Consola.saltoDeLinea();
                    try {
                        comprobarOpcion(opcion);
                    //Este catch se usa para capturar la excepción que se lanza cuando se ingresa una opción inválida
                    } catch (Exception e) {
                        Consola.salidaError("Error: " + e.getMessage());
                        esperarSegundo();
                    }
                //Flag para saber si el programa debe seguir activo
                } while (programaCorriendo);
            //Este catch se usa para capturar la excepción que se lanza cuando se ingresa un usuario o contraseña inválidos principalmente
            } catch (Exception e) {
                Consola.salidaError("Error: " + e.getMessage());
                esperarSegundo();
            }
        } while (programaCorriendo);
    }

    /**
     * Este metodo crea los roles del programa.
     */
    private static void crearRoles() {
        admin = new Rol("Administrador", permisosAdmin);
        bibliotecario = new Rol("Bibliotecario", permisosBibliotecario);
        usuario = new Rol("modelo.Usuario", permisosUsuario);
    }

    private static void crearUsuarios() {
        usuarios = new ArrayList<>();
        usuarios.add(new Usuario("admin_1", "admin1234", admin));
        usuarios.add(new Usuario("bibliotecario_1", "bibliotecario1234", bibliotecario));
        usuarios.add(new Usuario("usuario_1", "usuario1234", usuario));
    }

    private static void inicioSesion() {
        Consola.salidaNormal("Bienvenido a la Biblioteca Segura. Por favor, ingrese su nombre de usuario y contraseña. Para salir, ingrese 'salir' en el nombre de usuario.");
        Consola.salidaNormalSinSalto("Nombre de usuario: ");
        usuarioActual = Entrada.cadena();
        Consola.salidaNormalSinSalto("Contraseña: ");
        contrasenaActual = Entrada.cadena();
        //Este metodo realiza las comprobaciones de los datos ingresados
        comprobarUsuario();
    }

    private static void comprobarUsuario() {
        boolean inicio = false;
        //Si el usuario ingresa 'salir' en el nombre de usuario, se cierra el programa
        if (usuarioActual.equals("salir")) {
            programaCorriendo = false;
        } else {
            //Se comprueba que el nombre de usuario y la contraseña cumplan con los requisitos
            if (!usuarioActual.matches(REGEX_USUARIO)) {
                throw new RuntimeException("El nombre de usuario debe tener el formato 'nombre_#', donde 'nombre' es una cadena de caracteres en minúsculas y '#' es un número.");
            }
            if (!contrasenaActual.matches(REGEX_CONTRASENA)) {
                throw new RuntimeException("La contraseña debe tener al menos una letra minúscula y un número.");
            }
            //Se comprueba que el usuario y la contraseña ingresados coincidan con los de la lista de usuarios
            for (Usuario usuario : usuarios) {
                if (usuario.nombre().equals(usuarioActual) && usuario.contrasena().equals(contrasenaActual)) {
                    Consola.salidaNormal("Inicio de sesión exitoso.");
                    inicio = true;
                    user = usuario;
                    break;
                }
            }
            if (!inicio) {
                throw new RuntimeException("Usuario o contraseña incorrectos.");
            }
        }
    }

    private static void cerrarPrograma() {
        Consola.saltoDeLinea();
        Consola.salidaNormal("Cerrando programa...");
        esperarSegundo();
        Consola.salidaNormal("Programa cerrado.");
        System.exit(0);
    }

    private static void cerrarSesion() {
        Consola.salidaNormal("Cerrando sesión...");
        esperarSegundo();
        Consola.salidaNormal("Sesión cerrada.");
        inicioSesion();
    }

    /**
     * Este metodo para la ejecución del programa para dotar a la simulacion de mas realismo
     */
    private static void esperarSegundo() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Este metodo muestra el menu de opciones del programa.
     */
    private static void mostrarMenu() {
        Consola.saltoDeLinea();
        Consola.salidaNormal("Bienvenido, " + user.nombre() + ". ¿Qué desea hacer?");
        Consola.saltoDeLinea();
        //Se recorre la lista de opciones del menu y se muestran las que el usuario tiene permiso de realizar
        for (int i = 0; i < MENU_OPTIONS.size(); i++) {
            //Se obtiene la opcion del menu
            MenuOption option = MENU_OPTIONS.get(i);
            //Se comprueba que el usuario tenga permiso de realizar la opcion
            if (option.permiso() == null || user.rol().tienePermiso(option.permiso())) {
                //Si el usuario tiene permiso, se muestra la opcion
                Consola.salidaNormal((i + 1) + ".- " + option.descripcion());
            }
        }
        Consola.saltoDeLinea();
    }

    private static int elegirOpcion() {
        Consola.salidaNormalSinSalto("Ingrese el número de la opción que desea realizar: ");
        return Entrada.entero();
    }

    /**
     * Este metodo comprueba que la opcion ingresada por el usuario sea válida y en caso de serlo, realiza la accion correspondiente.
     * @param opcion
     */
    private static void comprobarOpcion(int opcion) {
        String mensaje;
        Permiso permiso;
        switch (opcion) {
            //Cada caso esta compuesto por el mensaje (simula la accion a realizar) y el permiso que se necesita para realizar la accion
            case 1 -> {
                mensaje = "Mostrando libros...";
                permiso = Permiso.VER_LIBROS;
            }
            case 2 -> {
                mensaje = "Agregando libro...";
                permiso = Permiso.AGREGAR_LIBRO;
            }
            case 3 -> {
                mensaje = "Editando libro...";
                permiso = Permiso.EDITAR_LIBRO;
            }
            case 4 -> {
                mensaje = "Eliminando libro...";
                permiso = Permiso.ELIMINAR_LIBRO;
            }
            case 5 -> {
                mensaje = "Agregando usuario...";
                permiso = Permiso.AGREGAR_USUARIO;
            }
            case 6 -> {
                mensaje = "Editando usuario...";
                permiso = Permiso.EDITAR_USUARIO;
            }
            case 7 -> {
                mensaje = "Eliminando usuario...";
                permiso = Permiso.ELIMINAR_USUARIO;
            }
            case 8 -> {
                cerrarSesion();
                return;
            }
            //Si la opcion ingresada no es valida, se lanza una excepcion
            default -> throw new RuntimeException("Opción inválida.");
        }
        //Se comprueba que el usuario tenga permiso necesario para realizar la accion
        if (user.rol().tienePermiso(permiso)) {
            //Si el usuario tiene permiso, se simula la accion
            Consola.salidaNormal(mensaje);
        } else {
            //Si el usuario no tiene permiso, se lanza una excepcion
            throw new RuntimeException("No tiene permiso para realizar esta acción.");
        }
    }
}