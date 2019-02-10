package catalogos;

import modelo.Filtro;
import modelo.NoFiltro;
import modelo.Video;
import persistencia.DAOException;
import persistencia.FactoriaDAO;
import persistencia.IAdaptadorVideoDAO;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CatalogoVideos {

    /**
     * Este catalogo se compone por solo un mapa <ID, VIDEO>
     */

    // FILTRO DE BUSQUEDA - FLAGS
    public static final int NO_FILTER_SEARCH = 0;                 // Sin filtros
    public static final int TITLE_SEARCH = 1;                     // Filtro de buscarPorFiltros: interprete
    public static final int LABEL_SEARCH = 2;                     // Busqueda por etiqueta
    public static final int TITLE_AND_LABEL_SEARCH = 3;           // Busqueda por etiqueta y titulo

    private Map<Integer, Video> videos;                           // Mapa de codigo y videos
    private static CatalogoVideos unicaInstancia;                 // SingleTon

    private FactoriaDAO dao;                                      // Factoria DAO para la persistencia de datos
    private IAdaptadorVideoDAO adaptadorVideo;                    // Interfaz del Adaptador DAO para persistencia de Video
    private Filtro filtro;


    /**
     * Metodo para devolver la unica instancia del catalgo
     * @return
     */
    public static CatalogoVideos getUnicaInstancia() {
        if (unicaInstancia == null) {
            unicaInstancia = new CatalogoVideos();
        }
        return unicaInstancia;
    }

    /**
     * Constructor privado
     * Incializa los dao
     * Inicializa los mapas
     * Carga los catalogos
     */
    private  CatalogoVideos() {
        try {
            dao = FactoriaDAO.getUnicaInstancia(FactoriaDAO.DAO_TDS);
            adaptadorVideo = dao.getVideoDAO(); //AdaptadorVideoTDS.getUnicaInstancia();
            videos = new HashMap<Integer, Video>();
            cargarCatalogo();
            filtro = NoFiltro.getUnicaInstancia();
        } catch (DAOException eDAO) {
            eDAO.printStackTrace();
        }
    }

    /**
     * Esta funcion carga los objetos Videos en los mapas
     * Recupera todos los objetos de la BBDD con el adaptador
     * Los inserta en el Mapa
     */
    private void cargarCatalogo() {
        List<Video> VideoesBD = adaptadorVideo.recuperarTodosVideos(); // TODO: Fallo en el adaptador
        if (!VideoesBD.isEmpty()) {
            for (Video Video : VideoesBD)
                videos.put(Video.getCodigo(), Video);
        }

    }

    /**
     *
     * @return Devuelve una lista con todos los videos en el mapa
     */
    public LinkedList<Video> getVideos() {
        LinkedList<Video> lista = new LinkedList<>();
        for (Video v : videos.values()) {
            lista.addLast(v);
        }
        return lista;
    }

    /**
     * Devuelve un video, pasando su titulo como parametro.
     * Si no existe devuelve null
     * @param titulo
     * @return
     */
    public Video getVideo(String titulo) {
        for (Video v : videos.values()) {
            if (v.getTitulo().equals(titulo)) return v;
        }
        return null;
    }

    /**
     * Devuleve un video pasandole el codigo o Id de la BBDD como parametro
     * @param codigo
     * @return
     */
    public Video getVideo(int codigo) {
        return videos.get(codigo);
    }

    /**
     * Añade un video al catalogo
     * @param video
     */
    public void addVideo(Video video) {
        if (video != null)
            videos.put(video.getCodigo(), video);
    }

    /**
     * Elimina un video del catalogo, en caso de estar contenido en este
     * @param video
     */
    public void removeVideo(Video video) {
        if (video != null)
            if (videos.containsKey(video.getTitulo()))
                   videos.remove(video.getTitulo());
    }

    /**
     * LAMBDA FUNCION JAVA 8
     * Hace una busqueda por etiqueta de los videos y devuelve una sita
     * @param etiquetas
     * @return
     */
    private LinkedList<Video> searchForLabel(LinkedList<String> etiquetas) {
        Predicate<Video> predicate = v -> v.contieneAlgunaEtiqueta(etiquetas);
        return videos.values().stream().filter(predicate).collect(Collectors.toCollection(LinkedList::new));
    }

    private LinkedList<Video> searchForLabel(String ... etiquetas) {
        Predicate<Video> predicate = v -> v.contieneAlgunaEtiqueta(new LinkedList<>(Arrays.asList(etiquetas)));
        return videos.values().stream().filter(predicate).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * LAMBDA-FUNCION JAVA 8
     * Busqueda por titulo
     * @param titulo
     * @return
     */
    private LinkedList<Video> searchForTitle(String titulo) {
        Predicate<Video> predicate = v -> v.getTitulo().contains(titulo);
        return videos.values().stream().filter(predicate).collect(Collectors.toCollection(LinkedList::new));
    }


    // Determina el tipo de busqueda

    /**
     * Determina el tipo de busqueda que se tiene que hacer
     * @param titulo
     * @param etiquetas
     * @return
     */
    private int tipoBusqueda(String titulo, String ... etiquetas) {
        if (!esCadenaVacia(titulo) && etiquetas.length > 0) {
            return TITLE_AND_LABEL_SEARCH;
        } else if (!esCadenaVacia(titulo) && (etiquetas.length == 0 || etiquetas == null)) {
            return TITLE_SEARCH;
        } else if (esCadenaVacia(titulo) && etiquetas.length > 0) {
            return LABEL_SEARCH;
        }
        return NO_FILTER_SEARCH;
    }

    private List<Video> busquedaMultiple(LinkedList<Video> lista1, LinkedList<Video> lista2) {
        Set<Video> conjunto = new HashSet<Video>();
        for (Video video : lista2) {
            if (lista1.contains(video))
                conjunto.add(video);
        }
        return new LinkedList<>(conjunto);
    }

    public LinkedList<Video> buscarVideoPorFiltros(String titulo, String ... etiqueta) {
        LinkedList<Video> lista = new LinkedList<Video>();
        int tipoBusqueda = tipoBusqueda(titulo, etiqueta);
        switch (tipoBusqueda) {
            case TITLE_SEARCH:
                lista.addAll(searchForTitle(titulo));
                break;
            case LABEL_SEARCH:
                lista.addAll(searchForLabel(etiqueta));
                break;
            case TITLE_AND_LABEL_SEARCH:
                lista.addAll(busquedaMultiple(searchForTitle(titulo), searchForLabel(etiqueta)));
                break;
        }
      //  return lista;
        return (LinkedList<Video>) lista.stream().filter(filtro::filtrarVideo).collect(Collectors.toCollection(LinkedList::new));
    }



    /**
     * Comprueba que un String es cadena vacía
     * @param cadena
     * @return
     */
    private boolean esCadenaVacia(String cadena) {
        return cadena.equals("");
    }



    /**
     * Funcion EQUALS pero refinada
     * @param Video
     * @return
     */
    public boolean existeVideo(Video Video) {
        if (videos == null) return false;
        if (videos.isEmpty()) return false;
        for(Video c : videos.values()) {
            if (c.equals(Video)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Dado el titulo, busca en el catalogo si existe
     * y lo retorna.
     * @param titulo
     * @return Si no encuentra el video, retorna valor NULL
     */
    public Video buscarVideo(String titulo) {
        for (Video v : videos.values()) {
            if (v.getTitulo().equals(titulo))
                return v;
        }
        return null;
    }

    public void replaceVideo(Video video){
        videos.put(video.getCodigo(), video);
    }

    public void setFiltro(Filtro filtroActual) {
        this.filtro = filtroActual;
    }
}
