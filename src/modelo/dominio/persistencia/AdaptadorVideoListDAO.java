package modelo.dominio.persistencia;

import modelo.dominio.*;
import beans.Entidad;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class AdaptadorVideoListDAO implements IAdaptadorVideoListDAO {

    // TODO: Aplicar patron GRASP en Usuario para acceder a las Videoes de las listas de Videoes

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
                                new Propiedad("Videoes", VideoList.getIDVideosString()),
                                new Propiedad("nombre", VideoList.getNombre())
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
        Entidad eVideoList;

        eVideoList = servPersistencia.recuperarEntidad(VideoList.getCodigo());

        servPersistencia.eliminarPropiedadEntidad(eVideoList, "Videoes");
        servPersistencia.anadirPropiedadEntidad(eVideoList, "Videoes", VideoList.getIDVideosString());
        servPersistencia.eliminarPropiedadEntidad(eVideoList, "nombre");
        servPersistencia.anadirPropiedadEntidad(eVideoList, "nombre", VideoList.getNombre());
    }

    // registrar primero los atributos que son objetos

    @Override
    public VideoList recuperarVideoList(int codigo) {
       /* Entidad eVideoList;
        LinkedList<Video> lista = new LinkedList<Video>();
        eVideoList = servPersistencia.recuperarEntidad(codigo);
        String nombre;
        String lineaVideoes;
        nombre = servPersistencia.recuperarPropiedadEntidad(eVideoList, "nombre");
        lineaVideoes = servPersistencia.recuperarPropiedadEntidad(eVideoList, "Videoes");
        lista = new LinkedList<>(adaptadorVideoTDS.getVideos(lineaVideoes));
        VideoList VideoList = new VideoList(nombre);
        VideoList.addVideos(lista);
        return VideoList;*/

        if (PoolDAO.getUnicaInstancia().contiene(codigo)) {
            return (VideoList) pool.getObjeto(codigo);
        }

        Entidad eVideoList;
        LinkedList<Video> lista = new LinkedList<Video>();
        eVideoList = servPersistencia.recuperarEntidad(codigo);
        String nombre;
        String lineaVideoes;

        nombre = servPersistencia.recuperarPropiedadEntidad(eVideoList, "nombre");
        lineaVideoes = servPersistencia.recuperarPropiedadEntidad(eVideoList, "Videoes");	// revisar
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
            String nombreVideo = strTok.nextToken();
            System.out.println(nombreVideo);
            list.add(adaptadorVideoTDS.recuperarVideo(Integer.valueOf((String) strTok.nextElement())));
        }
        return list;
    }
}
