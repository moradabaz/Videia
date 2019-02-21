package pruebas;

import modelo.Usuario;
import modelo.Video;
import modelo.VideoList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import persistencia.*;

import java.text.SimpleDateFormat;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class TestAdaptadoresDAO {
    private FactoriaDAO factoriaDAO;
    private IAdaptadorUsuarioDAO adaptadorUsuarioDAO;
    private IAdaptadorVideoDAO adaptadorVideoDAO;
    private IAdaptadorVideoListDAO adaptadorVideoListDAO;
    private Usuario usuario1, usuario2;
    private Video video1, video2;
    private VideoList videoList;


    @Before
    public void inicializarDatos () throws DAOException {
        factoriaDAO = FactoriaDAO.getUnicaInstancia(FactoriaDAO.DAO_TDS);
        adaptadorUsuarioDAO = factoriaDAO.getUsuarioDAO();
        adaptadorVideoDAO = factoriaDAO.getVideoDAO();
        adaptadorVideoListDAO = factoriaDAO.getVideoListDAO();


     /*   usuario1 = new Usuario(1,"moradisten", "wwww", "Morad", "Abbou", "1996-04-04", "moradgta@gmail.com");
        adaptadorUsuarioDAO.registrarUsuario(usuario1);
        usuario2 = new Usuario(2,"usuario2", "wwww", "Morad", "Abbou", "1996-04-04", "morad22gta@gmail.com");
        adaptadorUsuarioDAO.registrarUsuario(usuario2);

        video1 = new Video("Video 1", "https://www.youtube.com/watch?v=gd1c885zgO4");
        adaptadorVideoDAO.registrarVideo(video1);
        video2 = new Video("Video 2", "https://www.youtube.com/watch?v=WM28K5do8_k");
        adaptadorVideoDAO.registrarVideo(video2);

        videoList = new VideoList("videoList");

        videoList.addVideo(video1);
        videoList.addVideo(video2);

        adaptadorVideoListDAO.registrarVideoList(videoList);

        adaptadorUsuarioDAO.registrarUsuario(usuario1);*/

    }

    private void imprimirUsuario(Usuario usuario) {
        System.out.println("Codigo Usuario: " + usuario.getCodigo()
                + " - username: " + usuario.getUsername()
                + " - Nombre: " + usuario.getNombre()
                + " - Apellidos: " + usuario.getApellidos()
                + " - Email: " + usuario.getEmail()
                + " - Edad: " + usuario.getEmail()
                + " - fecha de Nacimiento: " + usuario.getFechaNacString());
    }

    private void imprimirVideo(Video video) {
        System.out.println("Codigo video: " + video.getCodigo()
                + " - Nombre: " + video.getTitulo()
                + " - Ruta Youtube " + video.getRutaFichero());
    }

    private void imprimirVideoList(VideoList videoList) {
        System.out.println("Codigo videolist: " + videoList.getCodigo()
                + " - Nombre: " + videoList.getNombre()
                + " - Videos: [[ " + videoList.getVideosString() + " ]]");
    }

    @After
    public void imprimirResultadosDelaBBDD() {
        List<Usuario> usuarios = adaptadorUsuarioDAO.recuperarTodosUsuarios();
        for (Usuario usuario :  usuarios) {
            imprimirUsuario(usuario);
        }

        List<Video> videos = adaptadorVideoDAO.recuperarTodosVideos();
        for (Video video: videos) {
            imprimirVideo(video);
        }

        List<VideoList> videoLists = adaptadorVideoListDAO.recuperarTodasVideoList();
        for (VideoList videoList: videoLists) {
            imprimirVideoList(videoList);
        }

    }

    @Test
    public void recuperarUsuario() {
        Usuario usuario = adaptadorUsuarioDAO.recuperarUsuario(1);
        imprimirUsuario(usuario);
        assertEquals(usuario.getUsername(),"usuario1");
    }



}
