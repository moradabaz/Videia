package modelo.dominio;

public class ImpopularesFiltro implements Filtro {

    private static final int NUM_REP = 5;
    private final String nombre;
    public static ImpopularesFiltro unicaInstancia;


    public static ImpopularesFiltro getUnicaInstancia() {
        if (unicaInstancia == null)
                unicaInstancia = new ImpopularesFiltro();
        return unicaInstancia;
    }

    private ImpopularesFiltro() {
        this.nombre = "Impopulares";
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
        return !(video.getReproducciones() < NUM_REP);
    }

}
