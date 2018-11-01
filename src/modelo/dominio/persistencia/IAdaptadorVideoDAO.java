package modelo.dominio.persistencia;

import modelo.dominio.Video;

import java.util.List;

public interface IAdaptadorVideoDAO {

    /**
     *
     * @param video
     */
    public void registrarVideo(Video video);

    /**
     *
     * @param video
     */
    public void borrarVideo(Video video);

    /**
     *
     * @param video
     */
    public void modificarVideo(Video video);

    /**
     *
     * @param codigo
     * @return
     */
    public Video recuperarVideo(int codigo);

    /**
     *
     * @return
     */
    public List<Video> recuperarTodosVideos();
}
