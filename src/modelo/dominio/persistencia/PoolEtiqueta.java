package modelo.dominio.persistencia;

import modelo.dominio.Etiqueta;
import modelo.dominio.Video;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.stream.Collectors;

public class PoolEtiqueta {

    private static PoolEtiqueta unicaInstancia;
    private Hashtable<String, Etiqueta> pool;

    private PoolEtiqueta() {
        pool = new Hashtable<String, Etiqueta>();
    }

    public static PoolEtiqueta getUnicaInstancia() {
        if (unicaInstancia == null) unicaInstancia = new PoolEtiqueta();
        return unicaInstancia;
    }

    public Etiqueta getEtiqueta(String nombre) {
        return pool.get(nombre);
    }

    public void addEtiqueta(String nombre, Etiqueta etiqueta) {
        if (!pool.contains(nombre)) {
            pool.put(nombre, etiqueta);
        }
    }

    public void addEtiqueta(String nombre) {
        addEtiqueta(nombre, new Etiqueta(nombre));
    }

    public void addEtiquetas(Video video) {
        for (Etiqueta etiqueta : video.getEtiquetas()) {
            addEtiqueta(etiqueta.getEtiqueta(), etiqueta);
        }
    }

    public void addEtiqueta(Etiqueta etiqueta) {
        this.addEtiqueta(etiqueta.getEtiqueta(), etiqueta);
    }

    public boolean contiene(String nombre) {
        return pool.containsKey(nombre);
    }

    public HashSet<Etiqueta> getEtiquetas() {
        return new HashSet<>(pool.values());
    }

}
