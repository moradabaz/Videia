package modelo.dominio.persistencia;

//import tds.driver.FactoriaServicioPersistencia;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

public class FactoriaDAO extends IFactoriaDAO {

    private AdaptadorVideoDAO adaptadorVideoDAO;
    private AdaptadorUsuarioDAO adaptadorUsuarioDAO;
    private AdaptadorVideoListDAO adaptadorPlayListDAO;
    private ServicioPersistencia servPersistencia;

    public FactoriaDAO() {
        servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
        adaptadorVideoDAO = AdaptadorVideoDAO.getUnicaInstancia();
        adaptadorUsuarioDAO = AdaptadorUsuarioDAO.getUnicaInstancia();
        adaptadorPlayListDAO = AdaptadorVideoListDAO.getUnicaInstancia();
    }

    @Override
    public IAdaptadorUsuarioDAO getUsuarioDAO() {
        return (IAdaptadorUsuarioDAO) AdaptadorUsuarioDAO.getUnicaInstancia();
    }

    @Override
    public IAdaptadorVideoListDAO getVideoListDAO() {
        return (IAdaptadorVideoListDAO) adaptadorPlayListDAO;
    }


    @Override
    public IAdaptadorVideoDAO getVideoDAO() {
        return adaptadorVideoDAO;
    }


}
