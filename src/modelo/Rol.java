package modelo;

public class Rol {
    private final String nombre;
    private final Permiso[] permisos;

    public Rol(String nombre, Permiso[] permisos) {
        this.nombre = nombre;
        this.permisos = permisos;
    }

    public boolean tienePermiso(Permiso permiso) {
        for (Permiso p : permisos) {
            if (p.equals(permiso)) {
                return true;
            }
        }
        return false;
    }
}
