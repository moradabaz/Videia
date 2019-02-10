package persistencia;

import java.util.Hashtable;

public class PoolDAO {
    private static PoolDAO unicaInstancia;
    private Hashtable<Integer, Object> pool;

    private PoolDAO() {
        pool = new Hashtable<Integer, Object>();
    }

    public static PoolDAO getUnicaInstancia() {
        if (unicaInstancia == null) unicaInstancia = new PoolDAO();
        return unicaInstancia;
    }

    public Object getObjeto(int id) {
        return pool.get(id);
    }

    public void addObjeto(int id, Object object) {
        pool.put(id, object);
    }

    public boolean contiene(int id) {
        return pool.containsKey(id);
    }
}
