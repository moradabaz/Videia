package controlador;

import modelo.*;
import modelo.Etiqueta;
import modelo.Video;
import persistencia.*;
import catalogos.CatalogoVideos;
import catalogos.CatalogoUsuarios;
import umu.tds.videos.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


public class Controlador implements VideosListener, IBuscadorVideos {

    private static Controlador instanciaUnica;


    private FactoriaDAO factoriaDAO;
    private CatalogoUsuarios catalogoUsuarios;
    private CatalogoVideos catalogoVideos;
    private AdaptadorUsuarioDAO adaptadorUsuario;
    private AdaptadorVideoListDAO adaptadorVideoList;
    private AdaptadorVideoDAO adaptadorVideo;
    private Usuario usuarioActual;
    private boolean logeado;
    private BuscadorVideos buscadorVideos;
    PoolEtiqueta poolEtiqueta;

    // atributos de reproducci?n
    private boolean playing;

    /**
     * Constructor del Controlador
     * - Inicializa la factoria
     * - Inicializa los adaptadores
     * - Inicializa y carga los catalogos
     */
    private Controlador() {
        usuarioActual = null;
        playing = false;
        try {
            factoriaDAO = FactoriaDAO.getUnicaInstancia();
            adaptadorVideoList = (AdaptadorVideoListDAO) factoriaDAO.getVideoListDAO();
            adaptadorUsuario = (AdaptadorUsuarioDAO) factoriaDAO.getUsuarioDAO();
            adaptadorVideo = (AdaptadorVideoDAO) factoriaDAO.getVideoDAO();
            this.buscadorVideos = new BuscadorVideos();
            this.buscadorVideos.anadirVideoListener(this);
            inicializarCatalogos();
            poolEtiqueta = PoolEtiqueta.getUnicaInstancia();
            cargarEtiquetas();
            catalogoVideos.setFiltro(NoFiltro.getUnicaInstancia());
        } catch (DAOException e) {
            e.printStackTrace();
        }
        //inicializarAdaptadores();
        //inicializarCatalogos();
        logeado = false;
    }


    /**
     * @return Devuelve la unica instancia del controloador
     */
    public static Controlador getInstanciaUnica() {
        if (instanciaUnica == null) {
            return instanciaUnica = new Controlador(); //TODO: Arreglar
        }
        return instanciaUnica;
    }

    /**
     * @return Devuelve el usuario actual
     */
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void inicializarAdaptadores() {
    }


    /**
     * Inicializa los cat?logos
     */
    private void inicializarCatalogos() {
        catalogoVideos = CatalogoVideos.getUnicaInstancia();
        catalogoUsuarios = CatalogoUsuarios.getUnicaInstancia();
    }

    /**
     * Login del Usuario
     * Si el usuario esta registrado,
     * - Se carga el usuario registrado
     * - Se cargan las Videoes
     * - Se cargan las listas de Videoes
     *
     * @param username
     * @param password
     */
    public boolean login(String username, String password) {
        Usuario usuario = catalogoUsuarios.getUsuario(username);
        if (usuario == null) {
            System.err.println("El usuario " + username + " no esta registrador");
        } else {
            if (usuario.getUsername().equals(username)) {
                if (usuario.getPassword().equals(password)) {
                    logeado = true;
                    int id = catalogoUsuarios.getCodigoUsuario(usuario);
                    usuarioActual = usuario;
                    if (id >= 0) {
                        usuarioActual.setCodigo(id);
                    }
                    catalogoVideos.setFiltro(usuarioActual.getFiltro());
                    System.out.println("Usuario logeado con exito");
                    return true;
                } else {
                    System.err.println("Las contraseñas NO coinciden");
                }
            }
        }
        return false;
    }

    public void logout() {
        logeado = false;
        usuarioActual = null;
    }

    /**
     * Compureba que un usuario esta registrado
     *
     * @param username
     * @return
     */
    public boolean usuarioRegistrado(String username) {
        return catalogoUsuarios.getUsuario(username) != null;
    }

    /**
     * Comprueba que existe una Video
     *
     * @param Video
     * @return
     */
    public boolean existeVideo(Video Video) {
        // return  (!catalogoVideos.buscarPorFiltros(Video.getTitulo(), "", Video.getNombreInterprete()).isEmpty());
        return catalogoVideos.existeVideo(Video);
    }

    public boolean existeVideoList(String nombre) {
        List<VideoList> lista = adaptadorVideoList.recuperarTodasVideoList();
        for (VideoList l : lista) {
            String nombreLista = l.getNombre();
            if (nombre.equals(nombreLista)) {
                return true;
            }
        }
        return false;
    }

    public LinkedList<Video> recuperarVideoesDeLista(String listName) {
        List<VideoList> lista = adaptadorVideoList.recuperarTodasVideoList();
        for (VideoList l : lista) {
            String nombreLista = l.getNombre();
            if (nombreLista.equals(nombreLista)) {
                return l.getVideos();
            }
        }
        return new LinkedList<>();
    }

    public boolean isPlaying() {
        return playing;
    }


    /**
     * Registra un Usuario en la base de datos y en los catalogos
     *
     * @param username
     * @param password
     * @param nombre
     * @param apellidos
     * @param fechaNac
     * @param email
     */
    public boolean registrarUsuario(String username, String password, String nombre, String apellidos, Date fechaNac, String email) {
        if (!usuarioRegistrado(username)) {
            Usuario usuario = new Usuario(username, password, nombre, apellidos, fechaNac, email);
            adaptadorUsuario.registrarUsuario(usuario);
            catalogoUsuarios.addUsuario(usuario);
            System.out.println("Usuario registrado con éxito");
            return true;
        } else {
            System.err.println("Mensaje Controlador: Ya existe un usuario con ese nombre");
        }
        return false;
    }

    /**
     * Registra un ususario - opcion 2
     *
     * @param username
     * @param password
     * @param nombre
     * @param apellidos
     * @param fechaNac
     * @param email
     */
    public boolean registrarUsuario(String username, String password, String nombre, String apellidos, String fechaNac, String email) {
        boolean registrado = false;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date fecha = null;
        try {
            fecha = formatter.parse(fechaNac);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        registrado = registrarUsuario(username, password, nombre, apellidos, fecha, email);
        if (registrado) {
            System.out.println("Usuario registrado con EXITO");
        }
        return registrado;
    }


    public boolean eliminarUsuario() {
        if (logeado) {
            for (VideoList l : usuarioActual.getMyVideoLists()) {
                adaptadorVideoList.borrarVideoList(l);
            }
            this.usuarioActual.getMyVideoLists().clear();
            this.usuarioActual.getVideosRecientes().clear();
            catalogoUsuarios.removeUsuario(usuarioActual);
            boolean borradoDeLaBBDD = adaptadorUsuario.borrarUsuario(usuarioActual);
            if (borradoDeLaBBDD)
                this.logout();
            return borradoDeLaBBDD;
        }
        return false;
    }

    /**
     * Modificar los datos de usuario actual
     *
     * @param email
     * @param
     */
    public void modificarDatosUsuario(String nombre, String apellidos, String fecha, String email) {
        if (logeado) {
            usuarioActual.setNombre(nombre);
            usuarioActual.setApellidos(apellidos);
            usuarioActual.setFechaNac(fecha);
            usuarioActual.setEmail(email);
            adaptadorUsuario.modificarUsuario(usuarioActual);
            catalogoUsuarios.addUsuario(usuarioActual);
        }
    }


    // TODO PARTE RELACIONADA CON REGISTRO DE VIDEOS

    public void registrarVideo(String titulo, String rutaFichero) {
        // No se controla que existan dnis duplicados
        Video video = new Video(titulo, rutaFichero);
        if (!existeVideo(video)) {
            adaptadorVideo.registrarVideo(video);
            catalogoVideos.addVideo(video);
            poolEtiqueta.addEtiquetas(video);
        } else {
            System.err.println("La Video (" + video.getTitulo() + ") ya existe");
        }
    }

    public void registrarVideo(Video video1) {
        if (!existeVideo(video1)) {
            adaptadorVideo.registrarVideo(video1);
            catalogoVideos.addVideo(video1);
            poolEtiqueta.addEtiquetas(video1);
        } else {
            System.err.println("La Video (" + video1.getTitulo() + ") ya existe");
        }
    }

    public void registrarVideo(Video... Videos) {
        for (Video c : Videos) {
            if (!existeVideo(c)) {
                registrarVideo(c);
                if (catalogoVideos.existeVideo(c))
                    System.out.println("Video " + c.getTitulo() + " Insertada con Exito");
            } else {
                System.err.println("la Video (" + c.getTitulo() + ") ya existe");
            }
        }
    }


    // TODO: Aparatado lista de Videoes

    // TODO: NO HE COMPROBADO QUE FUNCIONEN AL 100%

    /**
     * Se comprueba que el usuario tiene una lista a?adida pasando su nombre
     *
     * @param nombre
     * @return
     */
    public boolean UsuarioActualTieneLista(String nombre) {
        if (!logeado) return false;
        for (VideoList l : usuarioActual.getMyVideoLists()) {
            if (l.getNombre().equals(nombre)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Este metodo se encarga de crear una lista aportando el nombre de la lista y una lista de
     * títulos de Videoes para el usuario actual
     *
     * @return
     */
    public void registrarVideoList(String nombre, List<String> listaTitulos) {
        if (logeado) {
            if (usuarioActual.contieneVideoList(nombre)) {                     // TODO: Fallo El usuario actual es nulo
                System.err.println("Mensaje Controlador: El nombre no puede coincidir con el de una lista ya creada");
            } else {
                List<Video> Videoes = new LinkedList<Video>();
                for (String titulo : listaTitulos) {
                    Video video = catalogoVideos.getVideo(titulo);
                    if (video != null) {
                        Videoes.add(video);
                    } else {
                        System.err.println("Mensaje Controlador: La Video no existe");
                    }
                  /*  Video c = new Video("video", "");
                    if (c != null) {
                        Videoes.add(c);
                    } else {
                        System.err.println("Mensaje Controlador: La Video no existe");
                    }*/
                }
                VideoList videoList = new VideoList(nombre, Videoes);
                usuarioActual.addVideoList(videoList);
               // usuarioActual.anadirVideoList(videoList);
                adaptadorVideoList.registrarVideoList(videoList);
                adaptadorUsuario.modificarUsuario(usuarioActual);
                catalogoUsuarios.actualizarUsuario(usuarioActual);
                System.out.println("Tamano de lista Adaptador: " + adaptadorVideoList.recuperarTodasVideoList().size());

            }
        } else {
            System.err.println("El usuario debe estar logeado");

        }
    }

    public void eliminarVideoList(String nombre) {
        if (UsuarioActualTieneLista(nombre)) {
            VideoList l = usuarioActual.getListaVideo(nombre);
            adaptadorVideoList.borrarVideoList(l);
            usuarioActual.eliminarVideoList(l);
            adaptadorUsuario.modificarUsuario(usuarioActual);
            catalogoUsuarios.actualizarUsuario(usuarioActual);
        }
    }

    /**
     * @return Retorna una lista con todos los videos del catalogo de videos
     */
    public LinkedList<Video> getVideoes() {
        return catalogoVideos.getVideos();
    }

    /**
     * @param nombre    titulo del video
     * @param etiquetas Etiquetas
     * @return Retorna una lista de videos dado el nombre y las etiquetas
     */
    public LinkedList<Video> busqueda(String nombre, String... etiquetas) {
        return catalogoVideos.buscarVideoPorFiltros(nombre, etiquetas);
    }

    /**
     * @param nombre
     * @return Retorna un videos dado el nombre del video
     */
    public Video buscarVideo(String nombre) {
        return catalogoVideos.buscarVideo(nombre);
    }

    /**
     * @param nombre
     * @return
     */
    public VideoList obtenerVideoList(String nombre) {
        return usuarioActual.getListaVideo(nombre);
    }

    public LinkedList<VideoList> obtenerTodasListasVideoes() {
        return (LinkedList<VideoList>) usuarioActual.getMyVideoLists();
    }


    public void anadirVideoAReciente(Video Video) {
        usuarioActual.anadirVideoReciente(Video);
        adaptadorUsuario.modificarUsuario(usuarioActual);
    }

    public LinkedList<Video> getVideoesRecientesUser() {
        int tamRecientes = usuarioActual.TAM_RECIENTES;
        LinkedList<Video> recientes = usuarioActual.getVideosRecientes(); // TODO: PROVISIONAL
        if (recientes == null) return new LinkedList<Video>();
        if (recientes.size() > tamRecientes) {
            LinkedList<Video> resultado = new LinkedList<Video>();
            for (int i = 0; i < tamRecientes; i++) {
                resultado.add(i, recientes.get(i));
            }
            return resultado;
        }
        return (LinkedList<Video>) recientes;
    }


    public LinkedList<Video> getVideosMasVistos() {
        return catalogoVideos.getVideos().stream().sorted((o1, o2) -> {
            int n1 = o1.getNumReproducciones();
            int n2 = o2.getNumReproducciones();
            if (n1 > n2)
                return -1;
            else if (n1 < n2)
                return 1;
            return 0;
        }).limit(10).collect(Collectors.toCollection(LinkedList::new));
    }

    public void contratarPremium() {
        if (logeado) {
            if (!usuarioActual.isPremium()) {
                usuarioActual.setPremium(true);
                Usuario us = adaptadorUsuario.recuperarUsuario(usuarioActual.getCodigo());
                System.out.println(us.getUsername() + " + Premium: " + us.isPremium());
                adaptadorUsuario.modificarUsuario(usuarioActual);
                catalogoUsuarios.actualizarUsuario(usuarioActual);
            }
        }
    }

    public void cancelarPremium() {
        if (usuarioActual.isPremium()) {
            usuarioActual.setPremium(false);
            adaptadorUsuario.modificarUsuario(usuarioActual);
            catalogoUsuarios.actualizarUsuario(usuarioActual);
        }
    }

    public void incrementarReproduccion(Video video) {
        video.incrementarReproducciones();
        adaptadorVideo.modificarVideo(video);
        catalogoVideos.replaceVideo(video);
    }


    //TODO: PRUEBAS

    public void mostrarUsuariosRegistrados() {
        List<Usuario> lista = catalogoUsuarios.getAllUsuarios();
        if (lista == null) {
            System.err.println("La lista esta vacía");
        } else {
            System.out.println("Num de usuarios: " + lista.size());
            for (Usuario u : lista) {
                System.out.println(u.getCodigo());
                System.out.println(u.getUsername());
                System.out.println(u.getPassword());
                System.out.println(u.getNombre());
                System.out.println(u.getFechaNac().toString());
                System.out.println(u.getEmail());
            }
        }
    }

    public void mostrarVideoesRegistradas() {
        List<Video> lista = catalogoVideos.getVideos();
        if (lista == null) {
            System.err.println("La lista esta vacía");
        } else {
            System.out.println("Num de Videoes: " + lista.size());
            for (Video u : lista) {
                System.out.println(u.getCodigo());
                System.out.println(u.getTitulo());
                System.out.println(u.getEtiquetasString());
                System.out.println(u.getNumReproducciones());
                System.out.println(u.getRutaFichero());
            }
        }
    }

    public void mostrarVideoesUser() {
        List<VideoList> lista = usuarioActual.getMyVideoLists();
        for (VideoList l : lista) {
            for (Video c : l.getVideos()) {
                System.out.println("Titulo " + c.getTitulo());
            }
        }
    }

    public void mostrarListasUser() {
        usuarioActual.mostrarListas();
    }

    public void mostrarListasRegistradas() {
        List<VideoList> listas = adaptadorVideoList.recuperarTodasVideoList();
        for (VideoList l : listas) {
            System.out.println(l.getNombre() + " - " + l.getVideos().size());
        }
    }

    /**
     * Funcion utilizada para cambiar la contraseña del usuairo actual
     *
     * @param password Contraseña nueva
     */
    public void changePassword(String password) {
        this.usuarioActual.setPassword(password);
        this.adaptadorUsuario.modificarUsuario(usuarioActual);
        this.catalogoUsuarios.actualizarUsuario(usuarioActual);
    }


    @Override
    public void buscarVideos(String rutaxml) {
        //buscarVideos(rutaxml);
        this.buscadorVideos.setArchivoVideo(rutaxml);
    }


    @Override
    public void nuevosVideos(ArchivoVideosEvent event) {
        String titulo = "", url = "";
        List<String> nombreEtiquetas = new LinkedList<String>();
        Videos video = event.getNuevoVideo();
        if (video != null) {
            for (umu.tds.videos.Video vid : video.getVideo()) {
                url = vid.getUrl();
                titulo = vid.getTitulo();
                for (umu.tds.videos.Etiqueta label : vid.getEtiqueta()) {
                    nombreEtiquetas.add(label.getNombre());
                }
                if (!existeUrl(url)) {
                    Video video1 = new Video(titulo, url);
                    List<Etiqueta> listaEtiquetas = new LinkedList<>();
                    for (String label : nombreEtiquetas) {
                        Etiqueta etiqueta = new Etiqueta(label);
                        video1.addEtiqueta(etiqueta);
                        addEtiqueta(etiqueta);
                    }
                    // 2 - Almacenamos en la base de datos
                    registrarVideo(video1);
                }
            }
            cargarEtiquetas();
        }

        // 1º Creamos Video

    }

    public LinkedList<String> getVideoUrls() {
        LinkedList<Video> listaVideos = new LinkedList<Video>(this.catalogoVideos.getVideos());
        return new LinkedList<>(listaVideos.stream().map(v -> v.getRutaFichero())
                .collect(Collectors.toList()));
    }

    public LinkedList<String> getVideoUrls(VideoList videoList) {
        return new LinkedList<>(videoList.getVideos().stream().map(v -> v.getRutaFichero()).collect(Collectors.toList()));
    }

    public LinkedList<String> getVideosRecientesUrls() {
        return new LinkedList<>(usuarioActual.getVideosRecientes().stream().map(v -> v.getRutaFichero()).collect(Collectors.toList()));
    }

    private boolean existeUrl(String url) {
        return catalogoVideos.getVideos().stream().map(v -> v.getRutaFichero()).collect(Collectors.toList()).contains(url);
    }

    public void eliminarTodoslosVideos() {
        LinkedList<Video> lista = (LinkedList<Video>) adaptadorVideo.recuperarTodosVideos();
        Iterator<Video> it = lista.iterator();
        while (it.hasNext()) {
            Video video = it.next();
            adaptadorVideo.borrarVideo(video);
        }
    }

    public void addEtiqueta(Etiqueta etiqueta) {
        poolEtiqueta.addEtiqueta(etiqueta);
    }

    public void actualizarVideo(Video video) {
        adaptadorVideo.modificarVideo(video);
    }

    public void actualizarVideoList(VideoList videoList) {
        adaptadorVideoList.modificarVideoList(videoList);
    }

    public HashSet<Etiqueta> getEtiquetas() {
        return poolEtiqueta.getEtiquetas();
    }

    private void cargarEtiquetas() {
        for (Video video : adaptadorVideo.recuperarTodosVideos()) {
            poolEtiqueta.addEtiquetas(video);
        }
    }

    public LinkedList<VideoList> getUserVideoLists() {
        return usuarioActual.getMyVideoLists();
    }

    public void addVideoToVideoList(Video video, VideoList vdlist) {
        usuarioActual.anadirVideoALista(video, vdlist);
        adaptadorUsuario.modificarUsuario(usuarioActual);
        adaptadorVideoList.modificarVideoList(vdlist);
    }

    public void actualizarVideoesEnLista(String tituloVideo, String videoListNombre) {
        if (usuarioActual.contieneVideoList(videoListNombre)) {
            VideoList videoList = usuarioActual.getListaVideo(videoListNombre);
            if (videoList != null) {
                Video video = catalogoVideos.getVideo(tituloVideo);
                usuarioActual.anadirVideoALista(video, videoList);
                adaptadorUsuario.modificarUsuario(usuarioActual);
                adaptadorVideoList.modificarVideoList(videoList);
            }
        }
    }


    public LinkedList<Video> buscarPorTitulo(String nombre) {
       // return catalogoVideos.buscarVideoPorFiltros(nombre);
        return catalogoVideos.buscarVideoPorFiltros(nombre);
    }

    public LinkedList<Video> busquedaMultiple(String nombre, LinkedList<String> etiquetas) {
        String[] labels = new String[etiquetas.size()];
        etiquetas.toArray(labels);
        return catalogoVideos.buscarVideoPorFiltros(nombre, labels);
    }


    public LinkedList<Video> buscarPorEtiquetas(LinkedList<String> listaEtiquetas) {

        return busquedaMultiple("", listaEtiquetas);
    }

    public void play(Video video) {
        playing = true;
        incrementarReproduccion(video);
        usuarioActual.anadirVideoReciente(video);
    }

    public LinkedList<Video> getVideosFromVideoList(String videoListNombre) {
        if (usuarioActual.contieneVideoList(videoListNombre))
            return usuarioActual.getListaVideo(videoListNombre).getVideos();
        return null;
    }

    public VideoList getVideoList(String nombreVideoLista) {
        if (usuarioActual.contieneVideoList(nombreVideoLista))
            return usuarioActual.getListaVideo(nombreVideoLista);
        return null;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public String getFiltroActual() {
        return usuarioActual.getFiltroNombre();
    }

    public Video getVideo(String nombre) {
        Video video = catalogoVideos.getVideo(nombre);
        return video;
    }

    public void crearVideoList(String text) {
        VideoList videoList = new VideoList(text);
        usuarioActual.addVideoList(videoList);
       // registrarVideoList(text, );
    }


    public void addVideoToList(String videoNombre, String vdListNombre) {
        Video video = catalogoVideos.getVideo(videoNombre);
        VideoList vdList = usuarioActual.getListaVideo(vdListNombre);
        usuarioActual.anadirVideoALista(video, vdList);
    }

    public void setFiltroEnCatalogoVideos() {
        catalogoVideos.setFiltro(usuarioActual.getFiltro());
    }
}


