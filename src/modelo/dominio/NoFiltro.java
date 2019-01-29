package modelo.dominio;

import java.util.List;

public class NoFiltro implements Filtro{

    private final String nombre;
    public static NoFiltro unicaInstancia;

    public static NoFiltro getUnicaInstancia() {
        if (unicaInstancia == null)
            unicaInstancia = new NoFiltro();
        return unicaInstancia;
    }

    private NoFiltro() {
        nombre = "No Filtro";
    }

    public String getNombre() {
        return nombre;
    }


    public NoFiltro getFiltro(String nombreFiltro) {
        if (nombreFiltro.equals(nombre))   return getUnicaInstancia();
        else return null;
    }

    @Override
    public boolean filtrarVideo(Video videos) {
        return true;
    }
}
