package modelo;

public class MisListasFiltro implements Filtro {

    private Usuario usuario;
    private final String nombre;
    public static MisListasFiltro unicaInstancia;

    public static MisListasFiltro getUnicaInstancia() {
        if (unicaInstancia == null)
            unicaInstancia = new MisListasFiltro();
        return unicaInstancia;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public MisListasFiltro getFiltro(String nombre) {
        if (nombre.equals(this.nombre)) return unicaInstancia;
        return null;
    }

    private MisListasFiltro(Usuario usuario) {
        this.usuario = usuario;
        this.nombre = "Mis Listas";
    }

    private MisListasFiltro() {
        this.nombre = "Mis Listas";
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean filtrarVideo(Video video) {
       if (usuario == null)    return false;
       return !usuario.contieneEsteVideo(video);
    }
}
