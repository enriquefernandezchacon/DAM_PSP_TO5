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
    private static final Permiso[] permisosAdmin = {Permiso.AGREGAR_LIBRO, Permiso.EDITAR_LIBRO, Permiso.ELIMINAR_LIBRO, Permiso.AGREGAR_USUARIO, Permiso.EDITAR_USUARIO, Permiso.ELIMINAR_USUARIO};
    private static final Permiso[] permisosBibliotecario = {Permiso.AGREGAR_LIBRO, Permiso.EDITAR_LIBRO, Permiso.ELIMINAR_LIBRO};
    private static final Permiso[] permisosUsuario = {Permiso.VER_LIBROS};
    private static Rol admin;
    private static Rol bibliotecario;
    private static Rol usuario;
    private static List<Usuario> usuarios;
    private static boolean funcionando = true;
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

    public static void main(String[] args) {
        Consola.salidaNormal("Iniciando programa...");
        pararPrograma();
        Consola.salidaNormal("Creando roles...");
        crearRoles();
        pararPrograma();
        Consola.salidaNormal("Creando usuarios...");
        crearUsuarios();
        do {
            try {
                Consola.saltoDeLinea();
                inicioSesion();
                do {
                    mostrarMenu();
                    int opcion = elegirOpcion();
                    Consola.saltoDeLinea();
                    try {
                        comprobarOpcion(opcion);
                    } catch (Exception e) {
                        Consola.salidaError("Error: " + e.getMessage());
                    }
                } while (funcionando);
            } catch (Exception e) {
                Consola.salidaError("Error: " + e.getMessage());
                pararPrograma();
            }
        } while (funcionando);

        cerrarPrograma();
    }

    private static void crearRoles() {
        admin = new Rol("Administrador", permisosAdmin);
        bibliotecario = new Rol("Bibliotecario", permisosBibliotecario);
        usuario = new Rol("modelo.Usuario", permisosUsuario);
    }

    private static void crearUsuarios() {
        usuarios = new ArrayList<>();
        usuarios.add(new Usuario("admin", "admin", admin));
        usuarios.add(new Usuario("bibliotecario", "bibliotecario", bibliotecario));
        usuarios.add(new Usuario("usuario", "usuario", usuario));
    }

    private static void inicioSesion() {
        Consola.salidaNormal("Bienvenido a la Biblioteca Segura. Por favor, ingrese su nombre de usuario y contraseña. Para salir, ingrese 'salir' en el nombre de usuario.");
        Consola.salidaNormalSinSalto("Nombre de usuario: ");
        usuarioActual = Entrada.cadena();
        Consola.salidaNormalSinSalto("Contraseña: ");
        contrasenaActual = Entrada.cadena();
        comprobarUsuario();
    }

    private static void comprobarUsuario() {
        boolean inicio = false;
        if (usuarioActual.equals("salir")) {
            funcionando = false;
        } else {
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
        pararPrograma();
        Consola.salidaNormal("Programa cerrado.");
        System.exit(0);
    }

    private static void cerrarSesion() {
        Consola.salidaNormal("Cerrando sesión...");
        pararPrograma();
        Consola.salidaNormal("Sesión cerrada.");
        inicioSesion();
    }

    private static void pararPrograma() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Metodo mostrarMenu() que hace uso de la clase consola y los permisos del usuario para las distintras opciones
    private static void mostrarMenu() {
        Consola.saltoDeLinea();
        Consola.salidaNormal("Bienvenido, " + user.nombre() + ". ¿Qué desea hacer?");
        Consola.saltoDeLinea();
        for (int i = 0; i < MENU_OPTIONS.size(); i++) {
            MenuOption option = MENU_OPTIONS.get(i);
            if (option.permiso() == null || user.rol().tienePermiso(option.permiso())) {
                Consola.salidaNormal((i + 1) + ".- " + option.descripcion());
            }
        }
        Consola.saltoDeLinea();
    }

    private static int elegirOpcion() {
        Consola.salidaNormalSinSalto("Ingrese el número de la opción que desea realizar: ");
        return Entrada.entero();
    }

    private static void comprobarOpcion(int opcion) {
        String mensaje;
        Permiso permiso;
        switch (opcion) {
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
            default -> throw new RuntimeException("Opción inválida.");
        }

        if (user.rol().tienePermiso(permiso)) {
            Consola.salidaNormal(mensaje);
        } else {
            throw new RuntimeException("No tiene permiso para realizar esta acción.");
        }
    }
}



