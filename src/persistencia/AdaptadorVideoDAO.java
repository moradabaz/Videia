package persistencia;

import beans.Entidad;
import beans.Propiedad;
import modelo.Etiqueta;
import modelo.Video;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


import static persistencia.AdaptadorUsuarioDAO.pool;

public class AdaptadorVideoDAO implements IAdaptadorVideoDAO {

    // Propiedades para el Video

    private static final String TITULO = "titulo";
    private static final String ETIQUETAS = "etiquetas";
    private static final String RUTA_FICHERO = "rutaFichero";
    private static final String NUM_REPRODUCCIONES = "numReproducciones";

    private static ServicioPersistencia servPersistencia;
    private static AdaptadorVideoDAO unicaInstancia = null;
    private static PoolEtiqueta poolEtiqueta;

    public static AdaptadorVideoDAO getUnicaInstancia() {
        if (unicaInstancia == null) {
            unicaInstancia = new AdaptadorVideoDAO();
        }
            return unicaInstancia;

    }


    private AdaptadorVideoDAO() {
        servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
        poolEtiqueta = PoolEtiqueta.getUnicaInstancia();
    }

    @Override
    public void registrarVideo(Video video) {
        Entidad eVideo = null;
        // Si la entidad esta registrada no la registra de nuevo
        boolean existe = true;
        try {
            eVideo = servPersistencia.recuperarEntidad(video.getCodigo());
        } catch (NullPointerException e) {
            existe = false;
        }
        if (existe) return;

        // crear entidad Video
        eVideo = new Entidad();
        eVideo.setNombre("Video"); // TODO:   O bien "Video" o Bien Video.getTitulo()
        eVideo.setPropiedades(new ArrayList<Propiedad>(Arrays.asList(
                new Propiedad("titulo", video.getTitulo()),
                new Propiedad("etiquetas", video.getEtiquetasString()),
                new Propiedad("rutaFichero", video.getRutaFichero()),
                new Propiedad("numReproducciones", String.valueOf(video.getNumReproducciones())))
        ));

        // registrar entidad Video
        eVideo = servPersistencia.registrarEntidad(eVideo);
        // asignar identificador unico
        // Se aprovecha el que genera el servicio de persistencia
        video.setCodigo(eVideo.getId());

    }


    public void registrarVideoes(Video... videoes) {
        for (Video c : videoes) {
            registrarVideo(c);
        }
    }

    @Override
    public void borrarVideo(Video video) {
        Entidad eVideo = servPersistencia.recuperarEntidad(video.getCodigo());
        servPersistencia.borrarEntidad(eVideo);
    }

    @Override
    public void modificarVideo(Video video) {
        Entidad eVideo = servPersistencia.recuperarEntidad(video.getCodigo());
        servPersistencia.eliminarPropiedadEntidad(eVideo, TITULO);
        servPersistencia.anadirPropiedadEntidad(eVideo, TITULO, video.getTitulo());
        servPersistencia.eliminarPropiedadEntidad(eVideo, ETIQUETAS);
        servPersistencia.anadirPropiedadEntidad(eVideo, ETIQUETAS, video.getEtiquetasString());
        servPersistencia.eliminarPropiedadEntidad(eVideo, RUTA_FICHERO);
        servPersistencia.anadirPropiedadEntidad(eVideo, RUTA_FICHERO, video.getRutaFichero());
        servPersistencia.eliminarPropiedadEntidad(eVideo, NUM_REPRODUCCIONES);
        String numRep = String.valueOf(video.getNumReproducciones());
        System.out.println("Numero de reproducciones " + numRep);
        servPersistencia.anadirPropiedadEntidad(eVideo, NUM_REPRODUCCIONES, String.valueOf(video.getNumReproducciones()));
    }

    @Override
    public Video recuperarVideo(int codigo) {            // Fallo al recuperar el numero de reproducciones :(
        Entidad eVideo;
        String titulo;
        String rutaFichero;
        String reproString;
        String etiquetasString;
        int numReproducciones = 0;
        try {
            eVideo = servPersistencia.recuperarEntidad(codigo);
        } catch (NullPointerException e) {
            return null;
        }
        titulo = servPersistencia.recuperarPropiedadEntidad(eVideo, TITULO);
        rutaFichero = servPersistencia.recuperarPropiedadEntidad(eVideo, RUTA_FICHERO);
        reproString = servPersistencia.recuperarPropiedadEntidad(eVideo, NUM_REPRODUCCIONES); // Error
        etiquetasString = servPersistencia.recuperarPropiedadEntidad(eVideo, ETIQUETAS);
        Video video = new Video(titulo, rutaFichero);
        //System.out.println(reproString);
        numReproducciones = Integer.parseInt(reproString);
        video.setNumReproducciones(numReproducciones);
        video.setCodigo(codigo);
        for (String etiqueta : etiquetasString.split(":"))  video.addEtiqueta(new Etiqueta(etiqueta));
        poolEtiqueta.addEtiquetas(video);
        PoolDAO.getUnicaInstancia().addObjeto(codigo, video);
        return video;
    }

    @Override
    public List<Video> recuperarTodosVideos() {          // TODO: FALLO NO SE CARGAN LAS VideoES EN EL CATALOGO
        List<Video> videoes = new LinkedList<Video>();
        List<Entidad> entidades = servPersistencia.recuperarEntidades("Video");
        if (entidades != null) {
            for (Entidad eVideo : entidades) {
                videoes.add(recuperarVideo(eVideo.getId()));
            }
        }
        return videoes;
    }


}
