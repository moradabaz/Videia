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
                        new Propiedad("username", usuario.getUsername()),
                        new Propiedad("password", usuario.getPassword()),
                        new Propiedad("nombre", usuario.getNombre()),
                        new Propiedad("apellidos", usuario.getApellidos()),
                        new Propiedad("fecha", usuario.getStringFecha()),
                        new Propiedad("email", usuario.getEmail()),
                        new Propiedad("premium", String.valueOf(usuario.isPremium())),
                        new Propiedad("listasCanciones", usuario.getStringCodigosVideoList()),
                        new Propiedad("cancionesRecientes", usuario.getStringCodigosVideoesRecientesString()),
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
            servPersistencia.eliminarPropiedadEntidad(eUsuario, "VideoList");
            servPersistencia.eliminarPropiedadEntidad(eUsuario, "cancionesRecientes");
            servPersistencia.eliminarPropiedadEntidad(eUsuario, "descuento");

            // TODO: APLICAR DESCUENTOS

            servPersistencia.anadirPropiedadEntidad(eUsuario, "username", usuario.getUsername());
            servPersistencia.anadirPropiedadEntidad(eUsuario, "password", usuario.getPassword());
            servPersistencia.anadirPropiedadEntidad(eUsuario, "nombre", usuario.getNombre());
            servPersistencia.anadirPropiedadEntidad(eUsuario, "apellidos", usuario.getApellidos());
            servPersistencia.anadirPropiedadEntidad(eUsuario, "fecha", usuario.getStringFecha());
            servPersistencia.anadirPropiedadEntidad(eUsuario, "email", usuario.getEmail());
            servPersistencia.anadirPropiedadEntidad(eUsuario, "premium", String.valueOf(usuario.isPremium()));
            servPersistencia.anadirPropiedadEntidad(eUsuario, "VideoList", usuario.getStringCodigosVideoList());
            servPersistencia.anadirPropiedadEntidad(eUsuario, "cancionesRecientes", usuario.getStringCodigosVideoesRecientesString());
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
        String listasCancionesString;
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
        listasCancionesString = servPersistencia.recuperarPropiedadEntidad(eUsuario, "listasCanciones");
        LinkedList<VideoList> listasCancionesRecuperada = (LinkedList<VideoList>) getListasByIds(listasCancionesString); // ERROR


        LinkedList<VideoList> VideoList = new LinkedList<VideoList>();
        LinkedList<Video> recientes = new LinkedList<Video>();

        recientesString = servPersistencia.recuperarPropiedadEntidad(eUsuario, "cancionesRecientes");
        recientes = (LinkedList<Video>) getCancionesByIds(recientesString);

        premium = Boolean.parseBoolean(servPersistencia.recuperarPropiedadEntidad(eUsuario, "premium"));
        Usuario usuario = new Usuario(username, password, nombre, apellidos, fecha, email);
        usuario.setCodigo(codigo);
        PoolDAO.getUnicaInstancia().addObjeto(codigo, usuario);
        usuario.anadirVideoList(listasCancionesRecuperada);
        usuario.setVideoesRecientes(recientes);
        return usuario;

    }

    // TODO: PRUEBAS


    // ###################################################

    private String getCodidosDeListas(List<VideoList> listasCanciones) {
        String lineas = "";
        for (VideoList l: listasCanciones) {
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