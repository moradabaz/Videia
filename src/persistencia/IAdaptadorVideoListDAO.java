package persistencia;

import modelo.VideoList;

import java.util.List;

public interface IAdaptadorVideoListDAO {


    /**
     *
     *
     */
    public void registrarVideoList(VideoList videoList);

    /**
     *
     *
     */
    public void borrarVideoList(VideoList videoList);

    /**
     *
     *
     */
    public void modificarVideoList(VideoList videoList);


    /**
     *
     * @param codigo
     * @return
     */
    public VideoList recuperarVideoList(int codigo);

    /**
     *
     * @param codigo
     * @return
     */
    public List<VideoList> recuperarTodasVideoList();


}
