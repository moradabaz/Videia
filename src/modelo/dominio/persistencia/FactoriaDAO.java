package modelo.dominio.persistencia;

//import tds.driver.FactoriaServicioPersistencia;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

/**
 * Esta clase es la que se encarga de cargar todos los adaptadores e iniciar el servicio de persistencia
 * de la BBDD
 * DAO -> Data Acces Object
 * Una Factoria DAO se utiliza en aplicaciones distribuidas para conseguir que una aplicacion sea
 * independiente del tipo de almacenamiento utilizado
 * Las clases Video, Usuario y VideoList cuentan con una interfaz DAO que proporciona un conjunto de metodos
 * para obetener y almacenar los datos dentro de la BBDD
 *
 * La Interfaz DAO es implementada por clases que se encargan de establecer las conexiones a una BBDD
 * y encapsular el modo de acceso a esa BBDD
 *
 * El patron DAO utiliza una factoria abstracta para crear instancias de clases DAO
 */
public class FactoriaDAO extends IFactoriaDAO {

    private AdaptadorVideoDAO adaptadorVideoDAO;            // Adaptador Video
    private AdaptadorUsuarioDAO adaptadorUsuarioDAO;        // Adaptador Usuario
    private AdaptadorVideoListDAO adaptadorVideoListDAO;     // Adaptador VideoList
    private ServicioPersistencia servPersistencia;

    /**
     * Consturctor
     */
    public FactoriaDAO() {
        servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
        adaptadorVideoDAO = AdaptadorVideoDAO.getUnicaInstancia();
        adaptadorUsuarioDAO = AdaptadorUsuarioDAO.getUnicaInstancia();
        adaptadorVideoListDAO = AdaptadorVideoListDAO.getUnicaInstancia();
    }

    @Override
    public IAdaptadorUsuarioDAO getUsuarioDAO() {
        return (IAdaptadorUsuarioDAO) AdaptadorUsuarioDAO.getUnicaInstancia();
    }

    @Override
    public IAdaptadorVideoListDAO getVideoListDAO() {
        return (IAdaptadorVideoListDAO) adaptadorVideoListDAO;
    }


    @Override
    public IAdaptadorVideoDAO getVideoDAO() {
        return adaptadorVideoDAO;
    }


}
