package modelo.dominio;



public interface Filtro {


    public String getNombre();
    public Filtro getFiltro(String nombre);
    public boolean filtrarVideo(Video video);

}
