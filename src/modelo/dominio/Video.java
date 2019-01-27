package modelo.dominio;

import modelo.dominio.persistencia.PoolEtiqueta;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class Video {

    private static final int MAX_CANCIONES_RECIENTES = 5;
    private PoolEtiqueta poolEtiqueta = PoolEtiqueta.getUnicaInstancia();


    private int codigo;
    private String titulo;
    private List<Etiqueta> etiquetas;
    private String rutaFichero;
    private int numReproducciones;


    public Video(String titulo, String rutaFichero) {
        if (titulo == null || titulo.equals(""))
            throw new IllegalArgumentException("El titulo del video no esta inicializado");
        if (rutaFichero == null || rutaFichero.equals(""))
            throw new IllegalArgumentException("La ruta del video no esta inicializada");

        this.codigo = -1111;
        this.titulo = titulo;
        this.rutaFichero = rutaFichero;
        this.numReproducciones = 0;
        this.etiquetas = new LinkedList<Etiqueta>();

    }


    public int getCodigo() {
        return codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public List<Etiqueta> getEtiquetas() {
        return etiquetas;
    }

    public String getRutaFichero() {
        return rutaFichero;
    }

    public int getNumReproducciones() {
        return numReproducciones;
    }

    public int getReproducciones() {
        return numReproducciones;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setEtiquetas(List<Etiqueta> etiquetas) {
        this.etiquetas = etiquetas;
    }

    public void addEtiqueta(Etiqueta etiqueta) {
        if (!etiquetas.contains(etiqueta)) {
            etiquetas.add(etiqueta);
            poolEtiqueta.addEtiqueta(etiqueta);
        }
    }

    public void setRutaFichero(String rutaFichero) {
        this.rutaFichero = rutaFichero;
    }

    public void setNumReproducciones(int numReproducciones) {
        this.numReproducciones = numReproducciones;
    }

    public void incrementarReproducciones() {
        this.numReproducciones++;
    }

    public String getEtiquetasString() {
        String linea = "";
        for (Etiqueta etiqueta : etiquetas) {
            linea += etiqueta.getEtiqueta() + ":";
        }
        return linea.trim();
    }

    public boolean contieneTodasEtiquetas(String ... labels) {
        for (String l : labels) {
            if (!etiquetas.contains(l)) {
                return false;
            }
        }
        return true;
     // Predicate<Etiqueta> predicate = l -> etiquetas.contains(l);
     // return etiquetas.stream().allMatch(predicate);

    }

    public boolean contieneTodasEtiquetas(LinkedList<String> labels) {
        //Predicate<Etiqueta> predicate = labels::contains;
        //return etiquetas.stream().allMatch(predicate);
        for (String l : labels) {
            if (!etiquetas.contains(l)) {
                return false;
            }
        }
        return true;
    }

    public boolean contieneAlgunaEtiqueta(String ... labels) {
        Predicate<Etiqueta> predicate = l -> etiquetas.contains(l);
        return etiquetas.stream().anyMatch(predicate);
    }


    public boolean contieneAlgunaEtiqueta(LinkedList<String> labels) {
        Iterator<Etiqueta> it = etiquetas.iterator();
        while(it.hasNext()) {
            Etiqueta etiq = it.next();
            for (String l : labels) {
                if (etiq.getEtiqueta().equals(l))   return true;
            }
        }

        return false;
    }




}
