package modelo.dominio.persistencia;

import modelo.dominio.VideoList;
import modelo.dominio.Usuario;
import beans.Entidad;
import beans.Propiedad;
import modelo.dominio.Video;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Esta clase implementa los metodos de la Interfaz de abajo
 * Se encarga de la persistencia de los objetos de la clase Uusuario
 */
public class AdaptadorUsuarioDAO implements IAdaptadorUsuarioDAO {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String NOMBRE = "nombre";
    private static final String APELLIDOS = "apellidos";
    private static final String FECHA = "fecha";
    private static final String EMAIL = "email";
    private static final String PREMIUM = "premium";
    private static final String LISTA_VIDEOS = "listaVideos";
    private static final String VIDEOS_RECIENTES = "videosRecientes";
    private static final String DESCUENTO = "descuento";

    private static ServicioPersistencia servPersistencia;
    public static AdaptadorUsuarioDAO unicaInstancia;           //Atributo de la misma clase
    public static PoolDAO pool = PoolDAO.getUnicaInstancia();
    private static AdaptadorVideoListDAO adaptadorVideoList = AdaptadorVideoListDAO.getUnicaInstancia();
    private static AdaptadorVideoDAO adaptadorVideo = AdaptadorVideoDAO.getUnicaInstancia();


    /**
     *
     * @return
     */
    public static  AdaptadorUsuarioDAO getUnicaInstancia() {
        if (unicaInstancia == null) {
            unicaInstancia = new AdaptadorUsuarioDAO();
        }
        return unicaInstancia;
    }

    private AdaptadorUsuarioDAO() {
        servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
    }

    public void registrarUsuario(Usuario usuario) {
        Entidad eUsuario;
        boolean existe = true;
        try {
            eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
        } catch (NullPointerException e) {
            existe = false;
        }
        if (existe) return;

        AdaptadorVideoListDAO adaptadorVideoListDAO = AdaptadorVideoListDAO.getUnicaInstancia();
        if (adaptadorVideoListDAO != null) {
            if (!usuario.getMyVideoLists().isEmpty()) {
                for (VideoList l : usuario.getMyVideoLists()) {         // TODO: Aqui hay un nullpointerException
                    adaptadorVideoListDAO.registrarVideoList(l);
                }
            }
        }

        // TODO:             IMPORTANTE APLICAR LOS DESCUENTOS

        // crear entidad
        eUsuario = new Entidad();
        eUsuario.setNombre("Usuario");
        eUsuario.setPropiedades(new ArrayList<Propiedad>(
                Arrays.asList(
                        new Propiedad(USERNAME, usuario.getUsername()),
                        new Propiedad(PASSWORD, usuario.getPassword()),
                        new Propiedad(NOMBRE, usuario.getNombre()),
                        new Propiedad(APELLIDOS, usuario.getApellidos()),
                        new Propiedad(FECHA, usuario.getFechaNacString()),
                        new Propiedad(EMAIL, usuario.getEmail()),
                        new Propiedad(PREMIUM, String.valueOf(usuario.isPremium())),
                        new Propiedad(LISTA_VIDEOS, usuario.getStringCodigosVideoList()),
                        new Propiedad(VIDEOS_RECIENTES, usuario.getStringCodigosVideoesRecientesString()),
                        new Propiedad(DESCUENTO, String.valueOf(usuario.getDescuento()))
                )
        ));
        eUsuario = servPersistencia.registrarEntidad(eUsuario);
        int id = eUsuario.getId();
        usuario.setCodigo(id);
    }

    public boolean borrarUsuario(Usuario usuario) {
        try {
            Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
            return servPersistencia.borrarEntidad(eUsuario);
        } catch (NullPointerException n) {
            return false;
        }


    }

    public void modificarUsuario(Usuario usuario) {
        Entidad eUsuario;
        eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
        if (eUsuario != null) {
            servPersistencia.eliminarPropiedadEntidad(eUsuario, USERNAME);
            servPersistencia.eliminarPropiedadEntidad(eUsuario, PASSWORD);
            servPersistencia.eliminarPropiedadEntidad(eUsuario, NOMBRE);
            servPersistencia.eliminarPropiedadEntidad(eUsuario, APELLIDOS);
            servPersistencia.eliminarPropiedadEntidad(eUsuario, FECHA);
            servPersistencia.eliminarPropiedadEntidad(eUsuario, EMAIL);
            servPersistencia.eliminarPropiedadEntidad(eUsuario, PREMIUM);
            servPersistencia.eliminarPropiedadEntidad(eUsuario, LISTA_VIDEOS);
            servPersistencia.eliminarPropiedadEntidad(eUsuario, VIDEOS_RECIENTES);
            servPersistencia.eliminarPropiedadEntidad(eUsuario, DESCUENTO);

            // TODO: APLICAR DESCUENTOS

            servPersistencia.anadirPropiedadEntidad(eUsuario, USERNAME, usuario.getUsername());
            servPersistencia.anadirPropiedadEntidad(eUsuario, PASSWORD, usuario.getPassword());
            servPersistencia.anadirPropiedadEntidad(eUsuario, NOMBRE, usuario.getNombre());
            servPersistencia.anadirPropiedadEntidad(eUsuario, APELLIDOS, usuario.getApellidos());
            servPersistencia.anadirPropiedadEntidad(eUsuario, FECHA, usuario.getFechaNacString());
            servPersistencia.anadirPropiedadEntidad(eUsuario, EMAIL, usuario.getEmail());
            servPersistencia.anadirPropiedadEntidad(eUsuario, PREMIUM, String.valueOf(usuario.isPremium()));
            servPersistencia.anadirPropiedadEntidad(eUsuario, LISTA_VIDEOS, usuario.getStringCodigosVideoList());
            servPersistencia.anadirPropiedadEntidad(eUsuario, VIDEOS_RECIENTES, usuario.getStringCodigosVideoesRecientesString());
            servPersistencia.anadirPropiedadEntidad(eUsuario, DESCUENTO, String.valueOf(usuario.getDescuento()));
        }

    }

    @Override
    public List<Usuario> recuperarTodosUsuarios() {
        List<Usuario> usuarios = new LinkedList<Usuario>();
        List<Entidad> entidades = servPersistencia.recuperarEntidades("Usuario");

        for (Entidad usuaio : entidades) {
            usuarios.add(recuperarUsuario(usuaio.getId()));
        }
        return usuarios;
    }

    public Usuario recuperarUsuario(int codigo) {

        if (PoolDAO.getUnicaInstancia().contiene(codigo)){
            return (Usuario) pool.getObjeto(codigo);
        }


        Entidad eUsuario;
        LinkedList<VideoList> lista = new LinkedList<>();
        eUsuario = servPersistencia.recuperarEntidad(codigo);
        String username;
        String password;
        String nombre;
        String apellidos;
        String fechaString;
        String email;
        boolean premium;
        String listasCancionesString;
        String recientesString;


        username = servPersistencia.recuperarPropiedadEntidad(eUsuario, USERNAME);
        password = servPersistencia.recuperarPropiedadEntidad(eUsuario, PASSWORD);
        nombre = servPersistencia.recuperarPropiedadEntidad(eUsuario, NOMBRE);
        apellidos = servPersistencia.recuperarPropiedadEntidad(eUsuario, APELLIDOS);
        fechaString = servPersistencia.recuperarPropiedadEntidad(eUsuario, FECHA);


        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date fecha = new Date();
        try {
            fecha = formatter.parse(fechaString);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        email = servPersistencia.recuperarPropiedadEntidad(eUsuario, EMAIL);
        listasCancionesString = servPersistencia.recuperarPropiedadEntidad(eUsuario, LISTA_VIDEOS);
        LinkedList<VideoList> listasCancionesRecuperada = (LinkedList<VideoList>) getListasByIds(listasCancionesString); // ERROR


        LinkedList<VideoList> VideoList = new LinkedList<VideoList>();
        LinkedList<Video> recientes = new LinkedList<Video>();

        recientesString = servPersistencia.recuperarPropiedadEntidad(eUsuario, VIDEOS_RECIENTES);
        recientes = (LinkedList<Video>) getVideosByIds(recientesString);

        premium = Boolean.parseBoolean(servPersistencia.recuperarPropiedadEntidad(eUsuario, PREMIUM));
        Usuario usuario = new Usuario(username, password, nombre, apellidos, fecha, email);
        usuario.setCodigo(codigo);
        usuario.setPremium(premium);
        PoolDAO.getUnicaInstancia().addObjeto(codigo, usuario);
        usuario.anadirVideoList(listasCancionesRecuperada); // TODO -> AQUI SE ROMPE
        usuario.setVideoesRecientes(recientes);
        return usuario;

    }

    // TODO: PRUEBAS


    // ###################################################

    private String getCodidosDeListas(List<VideoList> listasCanciones) {
        String lineas = "";
        for (VideoList l: listasCanciones) {
            lineas += l.getCodigo() + ":";
        }
        return lineas.trim();

    }

    private List<VideoList> getListasByIds(String lineas) {
        List<VideoList> playlists = new LinkedList<VideoList>();
        StringTokenizer strTok = new StringTokenizer(lineas, ":");
        while (strTok.hasMoreTokens()) {
            String element =  strTok.nextToken();
            if (element != null && !element.equals(""))
                playlists.add(adaptadorVideoList.recuperarVideoList(Integer.valueOf(element)));
        }
        return playlists;
    }

    private String getCodigoVideosRecientes(List<Video> videosRecientes) {
        String lineas = "";
        for (Video v: videosRecientes) {
            lineas += v.getCodigo() + ":";
        }
        return lineas.trim();

    }

    private List<Video> getVideosByIds(String lineas) {     // 1212213:312321:12312312:
        List<Video> videosRecientes = new LinkedList<Video>();
        StringTokenizer strTok = new StringTokenizer(lineas, ":");
        while (strTok.hasMoreTokens()) {
            Video video = adaptadorVideo.recuperarVideo(Integer.valueOf((String) strTok.nextElement()));
            videosRecientes.add(video);
        }
        return videosRecientes;
    }
}


/*
package modelo.dominio.persistencia;

import modelo.dominio.VideoList;
import modelo.dominio.Usuario;
import beans.Entidad;
import beans.Propiedad;
import modelo.dominio.Video;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AdaptadorUsuarioDAO implements IAdaptadorUsuarioDAO {


    private static ServicioPersistencia servPersistencia;
    public static AdaptadorUsuarioDAO unicaInstancia;
    public static PoolDAO pool = PoolDAO.getUnicaInstancia();
    private static AdaptadorVideoListDAO adaptadorVideoList = AdaptadorVideoListDAO.getUnicaInstancia();
    private static AdaptadorVideoDAO adaptadorVideo = AdaptadorVideoDAO.getUnicaInstancia();


    public static  AdaptadorUsuarioDAO getUnicaInstancia() {
        if (unicaInstancia == null) {
            return new AdaptadorUsuarioDAO();
        } else {
            return unicaInstancia;
        }
    }

    private AdaptadorUsuarioDAO() {
        servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
    }

    public void registrarUsuario(Usuario usuario) {
        Entidad eUsuario;
        boolean existe = true;
        try {
            eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
        } catch (NullPointerException e) {
            existe = false;
        }
        if (existe) return;

        AdaptadorVideoListDAO adaptadorVideoListDAO = AdaptadorVideoListDAO.getUnicaInstancia();
        for (VideoList l : usuario.getMyVideoLists()) {
            adaptadorVideoListDAO.registrarVideoList(l);
        }

        // TODO:             IMPORTANTE APLICAR LOS DESCUENTOS

        // crear entidad
        eUsuario = new Entidad();
        eUsuario.setNombre("Usuario");
        eUsuario.setPropiedades(new ArrayList<Propiedad>(
                Arrays.asList(
                        new Propiedad("username", usuario.getUsername()),
                        new Propiedad("password", usuario.getPassword()),
                        new Propiedad("nombre", usuario.getNombre()),
                        new Propiedad("apellidos", usuario.getApellidos()),
                        new Propiedad("fecha", usuario.getStringFecha()),
                        new Propiedad("email", usuario.getEmail()),
                        new Propiedad("premium", String.valueOf(usuario.isPremium())),
                        new Propiedad("myVideoLists", usuario.getStringCodigosVideoList()),
                        new Propiedad("videosRecientes", usuario.getStringCodigosVideoesRecientesString()),
                        new Propiedad("descuento", String.valueOf(usuario.getDescuento()))
                )
        ));
        eUsuario = servPersistencia.registrarEntidad(eUsuario);
        int id = eUsuario.getId();
        usuario.setCodigo(id);
    }

    public void borrarUsuario(Usuario usuario) {
        Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
        servPersistencia.borrarEntidad(eUsuario);
    }

    public void modificarUsuario(Usuario usuario) {
        Entidad eUsuario;
        eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
        if (eUsuario != null) {
            servPersistencia.eliminarPropiedadEntidad(eUsuario, "username");
            servPersistencia.eliminarPropiedadEntidad(eUsuario, "password");
            servPersistencia.eliminarPropiedadEntidad(eUsuario, "nombre");
            servPersistencia.eliminarPropiedadEntidad(eUsuario, "apellidos");
            servPersistencia.eliminarPropiedadEntidad(eUsuario, "fecha");
            servPersistencia.eliminarPropiedadEntidad(eUsuario, "email");
            servPersistencia.eliminarPropiedadEntidad(eUsuario, "premium");
            servPersistencia.eliminarPropiedadEntidad(eUsuario, "myVideoLists");
            servPersistencia.eliminarPropiedadEntidad(eUsuario, "videosRecientes");
            servPersistencia.eliminarPropiedadEntidad(eUsuario, "descuento");

            // TODO: APLICAR DESCUENTOS

            servPersistencia.anadirPropiedadEntidad(eUsuario, "username", usuario.getUsername());
            servPersistencia.anadirPropiedadEntidad(eUsuario, "password", usuario.getPassword());
            servPersistencia.anadirPropiedadEntidad(eUsuario, "nombre", usuario.getNombre());
            servPersistencia.anadirPropiedadEntidad(eUsuario, "apellidos", usuario.getApellidos());
            servPersistencia.anadirPropiedadEntidad(eUsuario, "fecha", usuario.getStringFecha());
            servPersistencia.anadirPropiedadEntidad(eUsuario, "email", usuario.getEmail());
            servPersistencia.anadirPropiedadEntidad(eUsuario, "premium", String.valueOf(usuario.isPremium()));
            servPersistencia.anadirPropiedadEntidad(eUsuario, "myVideoLists", usuario.getStringCodigosVideoList());
            servPersistencia.anadirPropiedadEntidad(eUsuario, "videosRecientes", usuario.getStringCodigosVideoesRecientesString());
            servPersistencia.anadirPropiedadEntidad(eUsuario, "descuento", String.valueOf(usuario.getDescuento()));
        }

    }

    @Override
    public List<Usuario> recuperarTodosUsuarios() {
        List<Usuario> usuarios = new LinkedList<Usuario>();
        List<Entidad> entidades = servPersistencia.recuperarEntidades("Usuario");

        for (Entidad usuaio : entidades) {
            usuarios.add(recuperarUsuario(usuaio.getId()));
        }
        return usuarios;
    }

    public Usuario recuperarUsuario(int codigo) {
        if (PoolDAO.getUnicaInstancia().contiene(codigo)){
            return (Usuario) pool.getObjeto(codigo);
        }


        Entidad eUsuario;
        LinkedList<VideoList> lista = new LinkedList<>();
        eUsuario = servPersistencia.recuperarEntidad(codigo);
        String username;
        String password;
        String nombre;
        String apellidos;
        String fechaString;
        String email;
        boolean premium;
        String myvideoListString;
        String recientesString;


        username = servPersistencia.recuperarPropiedadEntidad(eUsuario, "username");
        password = servPersistencia.recuperarPropiedadEntidad(eUsuario, "password");
        nombre = servPersistencia.recuperarPropiedadEntidad(eUsuario, "nombre");
        apellidos = servPersistencia.recuperarPropiedadEntidad(eUsuario, "apellidos");
        fechaString = servPersistencia.recuperarPropiedadEntidad(eUsuario, "fecha");


        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date fecha = new Date();
        try {
            fecha = formatter.parse(fechaString);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        email = servPersistencia.recuperarPropiedadEntidad(eUsuario, "email");
        myvideoListString = servPersistencia.recuperarPropiedadEntidad(eUsuario, "myVideoLists");
        LinkedList<VideoList> myVideoListrecuperada = (LinkedList<VideoList>) getListasByIds(myvideoListString); // ERROR


        LinkedList<VideoList> VideoList = new LinkedList<VideoList>();
        LinkedList<Video> recientes = new LinkedList<Video>();

        recientesString = servPersistencia.recuperarPropiedadEntidad(eUsuario, "cancionesRecientes");
        recientes = (LinkedList<Video>) getCancionesByIds(recientesString);

        premium = Boolean.parseBoolean(servPersistencia.recuperarPropiedadEntidad(eUsuario, "premium"));
        Usuario usuario = new Usuario(username, password, nombre, apellidos, fecha, email);
        usuario.setCodigo(codigo);
        PoolDAO.getUnicaInstancia().addObjeto(codigo, usuario);
        usuario.anadirVideoList(myVideoListrecuperada);
        usuario.setVideoesRecientes(recientes);
        return usuario;

    }

    // TODO: PRUEBAS


    // ###################################################

    private String getCodidosDeListas(List<VideoList> videoLists) {
        String lineas = "";
        for (VideoList l: videoLists) {
            lineas += l.getCodigo() + " ";
        }
        return lineas.trim();

    }

    private List<VideoList> getListasByIds(String lineas) {

        List<VideoList> playlists = new LinkedList<VideoList>();
        StringTokenizer strTok = new StringTokenizer(lineas, " ");
        while (strTok.hasMoreTokens()) {
            playlists.add(adaptadorVideoList.recuperarVideoList(Integer.valueOf((String) strTok.nextElement())));
        }
        return playlists;
    }

    private String getCodigoVideosRecientes(List<Video> videosRecientes) {
        String lineas = "";
        for (Video v: videosRecientes) {
            lineas += v.getCodigo() + " ";
        }
        return lineas.trim();

    }

    private List<Video> getCancionesByIds(String lineas) {
        List<Video> cancionesRecientes = new LinkedList<Video>();
        StringTokenizer strTok = new StringTokenizer(lineas, " ");
        while (strTok.hasMoreTokens()) {
            Video video = adaptadorVideo.recuperarVideo(Integer.valueOf((String) strTok.nextElement()));
            cancionesRecientes.add(video);
        }
        return cancionesRecientes;
    }
}





 */