package catalogos;

import modelo.dominio.Usuario;
import modelo.dominio.persistencia.DAOException;
import modelo.dominio.persistencia.FactoriaDAO;
import modelo.dominio.persistencia.IAdaptadorUsuarioDAO;

import java.util.*;

public class CatalogoUsuarios {
    /**
     * El catalgo de usuarios utiliza el patron Singleton y es una clase única
     * El Catalog esta compuesto por dos HashMaps que contienen a los usuarios
     */
    private Map<String, Usuario> usuarios;                           // username -> Usuario
    private Map<Integer, Usuario> usuariosBBDD;                      // codigoBBDD -> Usuario
    private static CatalogoUsuarios unicaInstancia;                  // Catal

    private FactoriaDAO dao;                                         // Factoría DAO (Data Access Objecto)
    private IAdaptadorUsuarioDAO adaptadorUsuario;                   // Interfaz del Adaptador de Usuario

    /**
     * @return Getter de la unica instancia
     */
    public static CatalogoUsuarios getUnicaInstancia() {
        if (unicaInstancia == null) {
            return new CatalogoUsuarios();
        }
        return unicaInstancia;
    }

    /**
     * Constructor del Catalogo de usuarios
     * - Inicializa la Factoria Abstacata (DAO)
     * - Inicializa los mapas y las factorias
     */
    public CatalogoUsuarios() {
        try{
            dao = FactoriaDAO.getUnicaInstancia(FactoriaDAO.DAO_TDS);
            adaptadorUsuario = dao.getUsuarioDAO();
            usuarios = new HashMap<String, Usuario>();
            usuariosBBDD = new HashMap<Integer, Usuario>();
            cargarCatalogo();
        } catch (DAOException eDAO) {
            eDAO.printStackTrace();
        }
    }

    /**
     * Cargar los catalogos
     * - Recupera todos los usuarios de la BBDD y los inserta en los mapas
     */
    private void cargarCatalogo() {
        List<Usuario> usuariosBD = adaptadorUsuario.recuperarTodosUsuarios();
        if (!usuariosBD.isEmpty()) {
            for (Usuario usuario : usuariosBD) {
                usuarios.put(usuario.getUsername(), usuario);
                usuariosBBDD.put(usuario.getCodigo(), usuario);
            }
        }
    }

    /**
     *
     * @return Devuelve una lista con todos los usuarios de la base de datos
     */
    public List<Usuario> getAllUsuarios() {
        ArrayList<Usuario> list = new ArrayList<Usuario>();
        for (Usuario usuario : usuariosBBDD.values()) {
            list.add(usuario);
        }

        return list;
    }

    /**
     * @return Devuelve un conuntos de todos los nombres de usuario de los objetos Usuario de la BBDD
     */
    public Set<String> getAllusernames() {
        return Collections.unmodifiableSet(usuarios.keySet());
    }

    /**
     *
     * @param username Nombre de usuarioa  verificar
     * @return Verifica si el nombre de usuario esta contenido dentro los usuarios de la BBDD
     */
    public boolean contieneUserName(String username) {
        return getAllusernames().contains(username);
    }

    /**
     *
     * @param userName
     * @return Comprueba que el usuario que se le pasa como parametro existe y en tal caso lo retorna
     * En caso de no existir retorna NULL
     */
    public Usuario getUsuario(String userName) {
        return usuarios.get(userName);
    }

    /**
     * @return Devuelve una LinkedList todos los usuarios que sean premium
     */
    public List<Usuario> getUsuariosPremium() {
        List<Usuario> lista = new LinkedList<>();
        for (Usuario usuario: usuarios.values()) {
            if (usuario.isPremium()) {
                lista.add(usuario);
            }
        }
        return lista;
    }

    /**
     * Esta funcion actualiza los datos de un usuario que si este se encuentra dentro de los catalogos
     * @param usuario
     */
    public void actualizarUsuario(Usuario usuario) {
        if (usuarios.containsKey(usuario.getUsername())) {
            //Usuario u = usuarios.get(usuario.getUsername());
            usuarios.remove(usuario.getUsername());
            usuarios.put(usuario.getUsername(), usuario);
        }
    }

    /**
     * Esta funcion añade un objeto usuario a los catalogos
     * @param usuario
     */
    public void addUsuario(Usuario usuario) {
        usuarios.put(usuario.getUsername(), usuario);
        usuariosBBDD.put(usuario.getCodigo(), usuario);
    }

    /**
     * Este metodo elimina un usuario del catalogo, en caso de que exista en estos.
     * @param usuario
     */
    public void removeUsuario(Usuario usuario) {
        usuarios.remove(usuario.getUsername());
        usuariosBBDD.remove(usuario.getCodigo());
    }

    /**
     * Este metodo devuelve el codigo o Id de un usuario, en caso de exista dentro de los catalogos
     * FRAN MARICÓN
     * @param usuario
     * @return Devuelve -1 en caso de que no exista
     */
    public int getCodigoUsuario(Usuario usuario) {
        Set<Integer> listaIds = usuariosBBDD.keySet();
        for (int id : listaIds) {
            if (usuariosBBDD.get(id).equals(usuario));
            return id;
        }
        return -1;
    }
}
