package modelo.dominio;

import java.util.*;

public class VideoList {

    private int codigo;
    private String nombre;
    private LinkedList<Video> videos;


    public VideoList(String nombre) {
        this.nombre = nombre;
        videos = new LinkedList<Video>();
    }

    public VideoList(String nombre, List<Video> lista) {
        this.nombre = nombre;
        videos = new LinkedList<Video>(lista);
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCodigo() {
        return codigo;
    }

    public LinkedList<Video> getVideos() {
        return videos;
    }

    public boolean addVideo(Video Video) {
        return videos.add(Video);
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public boolean tieneMismoNombre(String nombre) {
        return this.nombre.equals(nombre);
    }

    public String getVideosString() {
        String linea = "";
        for (Video c : videos) {
            linea += c.getTitulo() + ":";
        }
        return linea.trim();
    }

    public String getIDVideosString() {
        String linea = "";
        for (Video Video : videos) {
            linea += Video.getCodigo() + ":";
        }
        return linea.trim();
    }

    public void addVideosFromList(LinkedList<Video> lista) {

        lista.stream().filter(v -> !videos.contains(v))
                      .forEach(v -> videos.add(v));

      //  for (Video v : lista) {
      //      if (!videos.contains(v)) {
      //          videos.add(v);
      //      }
      //  }
    }

    public void addVideos(Video ... lista) {

        Arrays.stream(lista).filter(v -> !videos.contains(v))
                            .forEach(v -> videos.add(v));
        //  Collections.addAll(Arrays.asList(lista));
    }

    public boolean contains(Video video) {
        boolean existe = false;
        for (Video vd : videos) {
            if (vd.getRutaFichero().equals(video.getRutaFichero())) existe = true;
        }
        return existe;
    }

    public void removeVideo(Video video) {
        if (videos.contains(video))
            this.videos.remove(video);
    }

}
