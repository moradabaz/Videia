package modelo.dominio;

public class MenoresFiltro implements Filtro{

    private final String nombre;
    private static MenoresFiltro unicaInstancia;

    public static MenoresFiltro getUnicaInstancia() {
        if (unicaInstancia == null)
            unicaInstancia = new MenoresFiltro();
        return unicaInstancia;
    }

    private MenoresFiltro() {
        nombre = "Menores";
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

        for (Etiqueta etiqueta : video.getEtiquetas()) {
            if (etiqueta.getEtiqueta().equals("Adultos"))   return false;
        }

        return true;
    }
}
