package modelo;

public class NombresLargosFiltro implements Filtro {

    public static final  int MAX_CARACTERES = 16;
    private final String nombre;
    private static NombresLargosFiltro unicaInstancia;

    public static NombresLargosFiltro getUnicaInstancia() {
        if (unicaInstancia == null)
                unicaInstancia = new NombresLargosFiltro();
        return unicaInstancia;
    }

    private NombresLargosFiltro() {
        this.nombre = "Nombres Largos";
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public Filtro getFiltro(String nombre) {
        if (nombre.equals(this.nombre)) return getUnicaInstancia();
        return null;
    }

    @Override
    public boolean filtrarVideo(Video video) {
        return !(video.getTitulo().length() > 16);
    }

}
