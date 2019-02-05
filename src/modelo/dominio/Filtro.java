package modelo.dominio;



public interface Filtro {


     String getNombre();
     Filtro getFiltro(String nombre);
     boolean filtrarVideo(Video video);

}
