package persistencia;

import modelo.Video;
import modelo.VideoList;

import beans.Entidad;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

import java.util.*;

public class AdaptadorVideoListDAO implements IAdaptadorVideoListDAO {

    // TODO: Aplicar patron GRASP en Usuario para acceder a las Videoes de las listas de Videoes

    private static final String VIDEOS= "Videoes";
    private static final String NOMBRE = "nombre";

    private static ServicioPersistencia servPersistencia;
    private static AdaptadorVideoListDAO unicaInstancia;
    private static AdaptadorVideoDAO adaptadorVideoTDS = AdaptadorVideoDAO.getUnicaInstancia();

    private PoolDAO pool = PoolDAO.getUnicaInstancia();

    public static AdaptadorVideoListDAO getUnicaInstancia() {

        if (unicaInstancia == null) {
            return new AdaptadorVideoListDAO();
        } else {
            return unicaInstancia;
        }
    }

    private AdaptadorVideoListDAO() {
        servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
    }

    /**
     * Cuando se registr una linea de vente se le asigna un identificador unico
     *
     * @param VideoList
     */
    @Override
    public void registrarVideoList(VideoList VideoList) {
        Entidad eVideoList;
        boolean existe = true;
        try {
            eVideoList = servPersistencia.recuperarEntidad(VideoList.getCodigo());
        } catch (NullPointerException e) {
            existe = false;
        }
        if (existe) return;


        // crear entidad linea de venta
        eVideoList = new Entidad();
        eVideoList.setNombre("VideoList");
        eVideoList.setPropiedades(
                new ArrayList<Propiedad>(
                        Arrays.asList(
                                new Propiedad(VIDEOS, VideoList.getIDVideosString()),
                                new Propiedad(NOMBRE, VideoList.getNombre())
                        )
                ));

        // registrar entidad lista
        eVideoList = servPersistencia.registrarEntidad(eVideoList);
        // asignar identificador unico.
        //Se aprovecha el que genera el servicio de persistencia
        VideoList.setCodigo(eVideoList.getId());


    }

    @Override
    public void borrarVideoList(VideoList VideoList) {
        // No se comprueba integridad con venta
        Entidad eVideoList = servPersistencia.recuperarEntidad(VideoList.getCodigo());
        servPersistencia.borrarEntidad(eVideoList);
    }

    @Override
    public void modificarVideoList(VideoList VideoList) {
        Entidad eVideoList = null;
        eVideoList = servPersistencia.recuperarEntidad(VideoList.getCodigo());
        if (eVideoList != null) {
            servPersistencia.eliminarPropiedadEntidad(eVideoList, VIDEOS);
            servPersistencia.anadirPropiedadEntidad(eVideoList, VIDEOS, VideoList.getIDVideosString());
            servPersistencia.eliminarPropiedadEntidad(eVideoList, NOMBRE);
            servPersistencia.anadirPropiedadEntidad(eVideoList, NOMBRE, VideoList.getNombre());
        }
    }

    // registrar primero los atributos que son objetos

    @Override
    public VideoList recuperarVideoList(int codigo) {
        if (PoolDAO.getUnicaInstancia().contiene(codigo)) {
            return (VideoList) pool.getObjeto(codigo);
        }

        Entidad eVideoList;
        LinkedList<Video> lista = new LinkedList<Video>();
        eVideoList = servPersistencia.recuperarEntidad(codigo);
        String nombre;
        String lineaVideoes;

        nombre = servPersistencia.recuperarPropiedadEntidad(eVideoList, NOMBRE);
        lineaVideoes = servPersistencia.recuperarPropiedadEntidad(eVideoList, VIDEOS);	// revisar
        VideoList LC = new VideoList(nombre);
        LC.setCodigo(codigo);

        pool.addObjeto(codigo, LC);

        List<Video> Videoes = getVideoesByIds(lineaVideoes);

        for (Video Video : Videoes) {
            LC.addVideo(Video);
        }

        return LC;
    }

    @Override
    public List<VideoList> recuperarTodasVideoList() {
        List<VideoList> listas = new LinkedList<VideoList>();
        List<Entidad> entidades = servPersistencia.recuperarEntidades("VideoList");
        if (entidades != null) {
            for (Entidad entidad : entidades) {
                listas.add(recuperarVideoList(entidad.getId())); // Revisar
            }
        }
        return listas;
    }

    private List<Video> getVideoesByIds(String lineasVideoes) {
        List<Video> list = new LinkedList<Video>();
        StringTokenizer strTok = new StringTokenizer(lineasVideoes, ":");
        while (strTok.hasMoreTokens()) {
            try {
                String videoId = strTok.nextToken();
                System.out.println(videoId);
                list.add(adaptadorVideoTDS.recuperarVideo(Integer.valueOf(videoId)));
            } catch (NoSuchElementException e) {

            }

        }
        return list;
    }
}
