package persistencia;

public abstract class IFactoriaDAO {

    private static FactoriaDAO unicaInstancia;

    public static final String DAO_TDS = "persistencia.TDSFactoriaDAO";

    public static FactoriaDAO getUnicaInstancia(String tipo) throws DAOException {
        if (unicaInstancia == null) {
            try {
                unicaInstancia = new FactoriaDAO();     //(TDSFactoriaDAO) Class.forName(tipo).newInstance();
            } catch (Exception e) {
                throw new DAOException(e.getMessage());
            }
        }
        return unicaInstancia;
    }

    public static FactoriaDAO getUnicaInstancia() throws DAOException {
        if (unicaInstancia == null) return getUnicaInstancia(FactoriaDAO.DAO_TDS);
        else  return unicaInstancia;
    }

    protected IFactoriaDAO() {}

    public abstract IAdaptadorUsuarioDAO getUsuarioDAO();
    public abstract IAdaptadorVideoListDAO getVideoListDAO();
    public abstract IAdaptadorVideoDAO getVideoDAO();
}
