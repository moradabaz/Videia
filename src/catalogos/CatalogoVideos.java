package catalogos;

import modelo.dominio.Video;
import modelo.dominio.persistencia.DAOException;
import modelo.dominio.persistencia.FactoriaDAO;
import modelo.dominio.persistencia.IAdaptadorVideoDAO;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CatalogoVideos {



    // FILTRO DE BUSQUEDA
    public static final int NO_FILTER_SEARCH = 0;                 // Sin filtros
    public static final int TITLE_SEARCH = 1;                    // Filtro de buscarPorFiltros: interprete
    public static final int LABEL_SEARCH = 2;
    public static final int TITLE_AND_LABEL_SEARCH = 3;

    private Map<Integer, Video> videos;
    private static CatalogoVideos unicaInstancia;

    private FactoriaDAO dao;
    private IAdaptadorVideoDAO adaptadorVideo;

    public static CatalogoVideos getUnicaInstancia() {
        if (unicaInstancia == null) {
            return new CatalogoVideos();
        }
        return unicaInstancia;
    }

    private  CatalogoVideos() {
        try {
            dao = FactoriaDAO.getUnicaInstancia(FactoriaDAO.DAO_TDS);
            adaptadorVideo = dao.getVideoDAO(); //AdaptadorVideoTDS.getUnicaInstancia();
            videos = new HashMap<Integer, Video>();
            cargarCatalogo();
        } catch (DAOException eDAO) {
            eDAO.printStackTrace();
        }
    }

    private void cargarCatalogo() {
        List<Video> VideoesBD = adaptadorVideo.recuperarTodosVideos(); // TODO: Fallo en el adaptador
        if (!VideoesBD.isEmpty()) {
            for (Video Video : VideoesBD)
                videos.put(Video.getCodigo(), Video);
        }

    }

    public List<Video> getVideos() {
        ArrayList<Video> lista = new ArrayList<Video>();
        for (Video v : videos.values()) {
            lista.add(v);
        }
        return lista;
    }

    public Video getVideo(String titulo) {
        for (Video v : videos.values()) {
            if (v.getTitulo().equals(titulo)) return v;
        }
        return null;
    }


    public Video getVideo(int codigo) {
        return videos.get(codigo);
    }

    public void addVideo(Video video) {
        videos.put(video.getCodigo(), video);
    }

    public void removeVideo(Video video) {
        if (videos.containsKey(video.getTitulo()))
                videos.remove(video.getTitulo());
    }


    private LinkedList<Video> searchForLabel(String ... etiquetas) {
        Predicate<Video> predicate = v -> v.contieneTodasEtiquetas(etiquetas);
        return videos.values().stream().filter(predicate).collect(Collectors.toCollection(LinkedList::new));
    }

    private LinkedList<Video> searchForTitle(String titulo) {
        Predicate<Video> predicate = v -> v.getTitulo().equals(titulo);
        return videos.values().stream().filter(predicate).collect(Collectors.toCollection(LinkedList::new));
    }


    // Determina el tipo de busqueda
    private int tipoBusqueda(String titulo, String ... etiquetas) {
        if (!esCadenaVacia(titulo) && etiquetas.length > 0) {
            return TITLE_AND_LABEL_SEARCH;
        } else if (esCadenaVacia(titulo) && etiquetas.length == 0) {
            return TITLE_SEARCH;
        } else if (esCadenaVacia(titulo) && etiquetas.length == 0) {
            return LABEL_SEARCH;
        }
        return NO_FILTER_SEARCH;
    }


    private boolean esCadenaVacia(String cadena) {
        return cadena.equals("");
    }

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

}
