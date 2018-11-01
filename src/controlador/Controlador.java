package controlador;

import javafx.scene.control.Alert;
import modelo.dominio.Video;
import modelo.dominio.VideoList;
import modelo.dominio.persistencia.*;
import catalogos.CatalogoVideos;
import catalogos.CatalogoUsuarios;
import modelo.dominio.Usuario;
import javafx.scene.media.MediaPlayer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class Controlador {

    private static Controlador instanciaUnica;


    private FactoriaDAO factoriaDAO;
    private CatalogoUsuarios catalogoUsuarios;
    private CatalogoVideos catalogoVideos;
    private AdaptadorUsuarioDAO adaptadorUsuario;
    private AdaptadorVideoListDAO adaptadorVideoList;
    private AdaptadorVideoDAO adaptadorVideo;
    private Usuario usuarioActual;
    private boolean logeado;


    // atributos de reproducci?n
    private boolean playing;
    private int codVideoActual;
    private javafx.scene.media.Media media;
    private MediaPlayer mediaPlayer;

    /**
     * Constructor del Controlador
     * - Inicializa la factoria
     * - Inicializa los adaptadores
     * - Inicializa y carga los catalogos
     */
    private Controlador(){
        usuarioActual = null;
        playing = false;
        try {
            factoriaDAO = FactoriaDAO.getUnicaInstancia();
            adaptadorVideoList = (AdaptadorVideoListDAO) factoriaDAO.getVideoListDAO();
            adaptadorUsuario = (AdaptadorUsuarioDAO) factoriaDAO.getUsuarioDAO();
            adaptadorVideo = (AdaptadorVideoDAO) factoriaDAO.getVideoDAO();
            inicializarCatalogos();
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
            return new Controlador();
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
    public void inicializarCatalogos() {
        catalogoVideos = CatalogoVideos.getUnicaInstancia();
        catalogoUsuarios = CatalogoUsuarios.getUnicaInstancia();
    }

    /**
     * Login del Usuario
     * Si el usuario esta registrado,
     * - Se carga el usuario registrado
     * - Se cargan las Videoes
     * - Se cargan las listas de Videoes
     * @param username
     * @param password
     */
    public boolean login(String username, String password) {
        Usuario usuario = catalogoUsuarios.getUsuario(username);
        if (usuario == null) {
            System.err.println("El usuario " + username + " no esta registrador");
        } else {
            if (usuario.getUsername().equals(username)){
                if (usuario.getPassword().equals(password)) {
                    logeado = true;
                    int id = catalogoUsuarios.getCodigoUsuario(usuario);
                    usuarioActual = usuario;
                    if (id >= 0 ){
                        usuarioActual.setCodigo(id);
                    }
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
     * @param username
     * @return
     */
    public boolean usuarioRegistrado(String username) {
        return catalogoUsuarios.getUsuario(username) != null;
    }

    /**
     * Comprueba que existe una Video
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
            if (nombre.equals(nombreLista)){
                return true;
            }
        }
        return false;
    }

    public LinkedList<Video> recuperarVideoesDeLista(String nombre) {
        List<VideoList> lista = adaptadorVideoList.recuperarTodasVideoList();
        for (VideoList l : lista) {
            String nombreLista = l.getNombre();
            if (nombre.equals(nombreLista)){
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
        return  false;
    }

    /**
     * Registra un ususario - opcion 2
     * @param username
     * @param password
     * @param nombre
     * @param apellidos
     * @param fechaNac
     * @param email
     */
    public boolean registrarUsuario(String username, String password, String nombre, String apellidos, String fechaNac, String email) {
        boolean registrado = false;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
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


    public void eliminarUsuario() {
        if (logeado) {
            for (VideoList l : usuarioActual.getMyVideoLists()) {
                adaptadorVideoList.borrarVideoList(l);
            }
            adaptadorUsuario.borrarUsuario(usuarioActual);
            catalogoUsuarios.removeUsuario(usuarioActual);
        }
    }

    /**
     * Modificar los datos de usuario actual
     * @param email
     * @param password
     */
    public void modificarDatosUsuario(String email, String password) {
        if (logeado) {
            usuarioActual.setEmail(email);
            usuarioActual.setPassword(password);
            adaptadorUsuario.modificarUsuario(usuarioActual);
            catalogoUsuarios.addUsuario(usuarioActual);
        }
    }



    // TODO PARTE RELACIONADA CON REGISTRO DE VIDEOS

 /*   public void registrarVideo(String titulo, String rutaFichero, Interprete interprete, EstiloMusical estilo) {
        // No se controla que existan dnis duplicados
        Video Video = new Video(titulo,rutaFichero,interprete,estilo);
        if (!existeVideo(Video)) {
            adaptadorVideo.registrarVideo(Video);
            catalogoVideos.addVideo(Video);
        } else {
            System.err.println("La Video (" + Video.getTitulo() + ") ya existe");
        }
    }

    public void registrarVideo(Video ... Videos) {
        for (Video c : Videos) {
            if (!existeVideo(c)) {
                adaptadorVideo.registrarVideo(c);
                catalogoVideos.addVideo(c);
                System.out.println("Video " + c.getTitulo() + " Insertada con Exito");
            }else {
                System.err.println("la Video (" + c.getTitulo() + ") ya existe");
            }
        }
    }


    // TODO: Aparatado lista de Videoes

    /**
     * Se comprueba que el usuario tiene una lista a?adida pasando su nombre
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
     *
     * @return
     */
 /*   public void registrarVideoList(String nombre, List<String> listaTitulos) {
        if (logeado) {
            if (usuarioActual.contieneVideoList(nombre)) {                     // TODO: Fallo El usuario actual es nulo
                System.err.println("Mensaje Controlador: El nombre no puede coincidir con el de una lista ya creada");
            } else {
                List<Video> Videoes = new LinkedList<Video>();
                for (String titulo : listaTitulos) {
                    Video c = buscarVideo(titulo);
                    if (c != null) {
                        Videoes.add(c);
                    } else {
                        System.err.println("Mensaje Controlador: La Video no existe");
                    }
                }
                VideoList l = new VideoList(nombre, Videoes);
                usuarioActual.anadirListasVideoes(l);
                adaptadorVideoList.registrarListaVideo(l);
                adaptadorUsuario.modificarUsuario(usuarioActual);
                catalogoUsuarios.actualizarUsuario(usuarioActual);
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


    public List<Video> getVideoes() {
        return catalogoVideos.getVideoes();
    }

    public LinkedList<Video> busqueda(String nombre, String nombreEstilo, String nombreInterprete) {
        return catalogoVideos.buscarPorFiltros(nombre, nombreEstilo, nombreInterprete);
    }

    public Video buscarVideo(String nombre) {
        return catalogoVideos.buscarVideo(nombre);
    }

    /**
     *
     * @param nombre
     * @return
     */
 /*   public VideoList obtenerVideoList(String nombre) {
        return usuarioActual.getListaVideo(nombre);
    }

    public LinkedList<VideoList> obtenerTodasListasVideoes() {
        return (LinkedList<VideoList>) usuarioActual.getListasVideoes();
    }

    public void actualizarVideoesEnLista(String nombre, LinkedList<String> titulosVideo) {
        if (usuarioActual.contieneVideoList(nombre)) {
            VideoList lc = usuarioActual.getListaVideo(nombre);
            if (lc != null) {
                for (String titulo: titulosVideo) {
                    Video c = catalogoVideos.getVideo(titulo);
                    usuarioActual.anadirVideoALista(c, lc);
                    adaptadorVideoList.modificarVideoList(lc);
                    adaptadorUsuario.modificarUsuario(this.usuarioActual);
                }
            }
        }
    }

    public void anadirVideoAReciente(Video Video) {
        usuarioActual.anadirVideoReciente(Video);
    }

    public LinkedList<Video> getVideoesRecientesUser() {
        int tamRecientes = usuarioActual.TAM_RECIENTES;
        LinkedList<Video> recientes = usuarioActual.getVideoesRecientes(); // TODO: PROVISIONAL
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


    public void ContratarPremium() {
        if (!usuarioActual.isPremium()) {
            usuarioActual.setPremium(true);
            adaptadorUsuario.modificarUsuario(usuarioActual);
        }
    }

    public void incrementarReproduccion(Video Video) {
        Video.incrementarReps();
        adaptadorVideo.modificarVideo(Video);
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
        List<Video> lista = catalogoVideos.getVideoes();
        if (lista == null) {
            System.err.println("La lista esta vacía");
        } else {
            System.out.println("Num de Videoes: " + lista.size());
            for (Video u : lista) {
                System.out.println(u.getCodigo());
                System.out.println(u.getTitulo());
                System.out.println(u.getEstiloMusicalNombre());
                System.out.println(u.getNombreInterprete());
                System.out.println(u.getNumReproducciones());
                System.out.println(u.getRutaFichero());
            }
        }
    }

    public void mostrarVideoesUser() {
        List<VideoList> lista = usuarioActual.getListasVideoes();
        for (VideoList l : lista) {
            for (Video c	 : l.getVideoes()) {
                System.out.println("Titulo " + c.getTitulo() +  "-Interprete " + c.getInterprete().getNombre());
            }
        }
    }

    public void mostrarListasUser() {
        usuarioActual.mostrarListas();
    }

    public void mostrarListasRegistradas() {
        List<VideoList> listas = adaptadorVideoList.recuperarTodasVideoList();
        for (VideoList l : listas) {
            System.out.println(l.getNombre() + " - " + l.getVideoes().size());
        }
    }



    // APARTADO PARA REPRODUCCION DE VideoES

    // REPRODUCIR Video
    public void play(int idVideo) {
        Video Video = null;

        if (!playing) {
            playing = true;
            codVideoActual = idVideo;
            try {
                Video = catalogoVideos.getVideo(codVideoActual);
                String rutaVideo = Video.getRutaFichero();
                File f = new File(rutaVideo);
                if (f.isFile()) {
                    media = new javafx.scene.media.Media(f.toURI().toString());
                    mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.play();
                    incrementarReproduccion(Video);
                    anadirVideoAReciente(Video);
                } else {
                    URL url = new URL(rutaVideo);
                    mediaPlayer = new MediaPlayer(new Media(url.toString()));
                    mediaPlayer.play();
                }
            } catch (MediaException e) {
                System.err.println("Esta Video no funsionaaa");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } else if (codVideoActual != idVideo) {
            mediaPlayer.stop();
            codVideoActual = idVideo;
            try {
                Video = catalogoVideos.getVideo(codVideoActual);
                String ruta = Video.getRutaFichero();
                File f = new File(ruta);
                media = new Media(f.toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();
                incrementarReproduccion(Video);
                anadirVideoAReciente(Video);
            } catch (MediaException e2) {
                System.err.println("No FUNSIONA 2.0");
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        } else {
            mediaPlayer.play();
        }
    }

    public void playReciente(int idVideo) {
        Video Video = null;

        if (!playing) {
            playing = true;
            codVideoActual = idVideo;
            try {
                Video = catalogoVideos.getVideo(codVideoActual);
                String rutaVideo = Video.getRutaFichero();
                File f = new File(rutaVideo);
                if (f.isFile()) {
                    media = new javafx.scene.media.Media(f.toURI().toString());
                    mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.play();
                } else {
                    URL url = new URL(rutaVideo);
                    mediaPlayer = new MediaPlayer(new Media(url.toString()));
                    mediaPlayer.play();
                }
            } catch (MediaException e) {
                System.err.println("Esta Video no funsionaaa");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } else if (codVideoActual != idVideo) {
            mediaPlayer.stop();
            codVideoActual = idVideo;
            try {
                Video = catalogoVideos.getVideo(codVideoActual);
                String ruta = Video.getRutaFichero();
                File f = new File(ruta);
                media = new Media(f.toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();
            } catch (MediaException e2) {
                System.err.println("No FUNSIONA 2.0");
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        } else {
            mediaPlayer.play();
        }
    }

    public void pause() {
        mediaPlayer.stop();
    }

    public void stop() {
        mediaPlayer.stop();
        playing = false;
    }
*/
}


