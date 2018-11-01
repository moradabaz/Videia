package modelo.dominio.persistencia;

import beans.Entidad;
import beans.Propiedad;
import modelo.dominio.Video;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


import static modelo.dominio.persistencia.AdaptadorUsuarioDAO.pool;

public class AdaptadorVideoDAO implements IAdaptadorVideoDAO {

    private static ServicioPersistencia servPersistencia;
    private static AdaptadorVideoDAO unicaInstancia = null;

    public static AdaptadorVideoDAO getUnicaInstancia() {
        if (unicaInstancia == null) {
            return new AdaptadorVideoDAO();
        } else {
            return unicaInstancia;
        }
    }


    private AdaptadorVideoDAO() {
        servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
    }

    @Override
    public void registrarVideo(Video Video) {
        Entidad eVideo = null;
        // Si la entidad est� registrada no la registra de nuevo
        boolean existe = true;
        try {
            eVideo = servPersistencia.recuperarEntidad(Video.getCodigo());
        } catch (NullPointerException e) {
            existe = false;
        }
        if (existe) return;

        // crear entidad Video
        eVideo = new Entidad();
        eVideo.setNombre("Video"); // TODO:   O bien "Video" o Bien Video.getTitulo()
        eVideo.setPropiedades(new ArrayList<Propiedad>(Arrays.asList(
                new Propiedad("titulo", Video.getTitulo()),
                new Propiedad("etiquetas", Video.getEtiquetasString()),
                new Propiedad("rutaFichero", Video.getRutaFichero()),
                new Propiedad("numReproducciones", String.valueOf(Video.getNumReproducciones())))
        ));

        // registrar entidad Video
        eVideo = servPersistencia.registrarEntidad(eVideo);
        // asignar identificador unico
        // Se aprovecha el que genera el servicio de modelo.dominio.persistencia
        Video.setCodigo(eVideo.getId());
    }


    public void registrarVideoes(Video ... Videoes) {
        for (Video c : Videoes) {
            registrarVideo(c);
        }
    }

    @Override
    public void borrarVideo(Video Video) {
        // No se comprueba integridad con lineas de venta
        Entidad eVideo = servPersistencia.recuperarEntidad(Video.getCodigo());
        servPersistencia.borrarEntidad(eVideo);
    }

    @Override
    public void modificarVideo(Video Video) {
        Entidad eVideo = servPersistencia.recuperarEntidad(Video.getCodigo());
        servPersistencia.eliminarPropiedadEntidad(eVideo, "titulo");
        servPersistencia.anadirPropiedadEntidad(eVideo, "titulo", Video.getTitulo());
        servPersistencia.eliminarPropiedadEntidad(eVideo, "etiquetas");
        servPersistencia.anadirPropiedadEntidad(eVideo, "etiquetas", Video.getEtiquetasString());
        servPersistencia.eliminarPropiedadEntidad(eVideo, "rutaFichero");
        servPersistencia.anadirPropiedadEntidad(eVideo, "rutaFichero", Video.getRutaFichero());
        servPersistencia.eliminarPropiedadEntidad(eVideo, "numReproducciones");
        String numRep = String.valueOf(Video.getNumReproducciones());
        System.out.println("Numero de reproducciones " + numRep);
        servPersistencia.anadirPropiedadEntidad(eVideo, "numReproducciones", String.valueOf(Video.getNumReproducciones()));
    }

    @Override
    public Video recuperarVideo(int codigo) {			// Fallo al recuperar el numero de reproducciones :(
        Entidad eVideo;
        String titulo;
        String rutaFichero;
        String interpreteString;
        String estiloMusicalString;
        String reproString;
        int numReproducciones = 0;
        try {
            eVideo = servPersistencia.recuperarEntidad(codigo);
        }catch (NullPointerException e) {
            return null;
        }
        titulo = servPersistencia.recuperarPropiedadEntidad(eVideo, "nombre");
        rutaFichero = servPersistencia.recuperarPropiedadEntidad(eVideo, "rutaFichero");
        interpreteString = servPersistencia.recuperarPropiedadEntidad(eVideo, "interprete");
        estiloMusicalString = servPersistencia.recuperarPropiedadEntidad(eVideo, "estiloMusical");
        reproString = servPersistencia.recuperarPropiedadEntidad(eVideo, "numReproducciones"); // Error

        Video Video = new Video(titulo, rutaFichero);
        //System.out.println(reproString);
        numReproducciones = Integer.parseInt(reproString);
        Video.setNumReproducciones(numReproducciones);
        Video.setCodigo(codigo);

        PoolDAO.getUnicaInstancia().addObjeto(codigo, Video);
        return Video;
    }

    @Override
    public List<Video> recuperarTodosVideos() {          // TODO: FALLO NO SE CARGAN LAS VideoES EN EL CATALOGO
        List<Video> Videoes = new LinkedList<Video>();
        List<Entidad> entidades = servPersistencia.recuperarEntidades("Video");
        if (entidades != null) {
            for (Entidad eVideo : entidades) {
                Videoes.add(recuperarVideo(eVideo.getId()));
            }
        }
        return Videoes;
    }


    public LinkedList<Video> getVideoes(String lineaVideoes) {
        LinkedList<Video> lista = new LinkedList<>();
        String codigos[] = lineaVideoes.split(":");
        if (codigos.length == 0 || lineaVideoes.equals("")) return lista;

        for (String id : codigos) {
            Video Video = (modelo.dominio.Video) pool.getObjeto(Integer.parseInt(id));
            if (Video == null) {
                Video = this.recuperarVideo(Integer.parseInt(id));
            }
            lista.add(Video);
        }

        return lista;
    }
}
