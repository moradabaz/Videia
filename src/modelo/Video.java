package modelo;

import persistencia.PoolEtiqueta;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;



public class Video implements Comparable<Video> {

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

    @Override
    public int compareTo(Video video) {
        if(this.numReproducciones == video.getNumReproducciones()) return 0;
        if(this.numReproducciones > video.getNumReproducciones()) return -1;
        return 1;
    }

}
