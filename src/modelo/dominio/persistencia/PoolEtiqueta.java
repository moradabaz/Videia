package modelo.dominio.persistencia;

import modelo.dominio.Etiqueta;

import java.util.Hashtable;

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
        if (!pool.contains(etiqueta)) {
            pool.put(nombre, etiqueta);
        }
    }

    public void addEtiqueta(Etiqueta etiqueta) {
        this.addEtiqueta(etiqueta.getEtiqueta(), etiqueta);
    }

    public boolean contiene(String nombre) {
        return pool.containsKey(nombre);
    }

}
