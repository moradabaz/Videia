package catalogos;

import modelo.dominio.Usuario;
import modelo.dominio.persistencia.DAOException;
import modelo.dominio.persistencia.FactoriaDAO;
import modelo.dominio.persistencia.IAdaptadorUsuarioDAO;

import java.util.*;

public class CatalogoUsuarios {

    private Map<String, Usuario> usuarios;          // username -> Usuario
    private Map<Integer, Usuario> usuariosBBDD;
    private static CatalogoUsuarios unicaInstancia;

    private FactoriaDAO dao;
    private IAdaptadorUsuarioDAO adaptadorUsuario;

    public static CatalogoUsuarios getUnicaInstancia() {
        if (unicaInstancia == null) {
            return new CatalogoUsuarios();
        }
        return unicaInstancia;
    }

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



    private void cargarCatalogo() {
        List<Usuario> usuariosBD = adaptadorUsuario.recuperarTodosUsuarios();
        if (!usuariosBD.isEmpty()) {
            for (Usuario usuario : usuariosBD) {
                usuarios.put(usuario.getUsername(), usuario);
                usuariosBBDD.put(usuario.getCodigo(), usuario);
            }
        }
    }

    public List<Usuario> getAllUsuarios() {
        ArrayList<Usuario> list = new ArrayList<Usuario>();
        for (Usuario usuario : usuariosBBDD.values()) {
            list.add(usuario);
        }

        return list;
    }

    public Set<String> getAllusernames() {
        return Collections.unmodifiableSet(usuarios.keySet());
    }

    public boolean contieneUserName(String username) {
        return getAllusernames().contains(username);
    }

    public Usuario getUsuario(String userName) {
        return usuarios.get(userName);
    }



    public List<Usuario> getUsuariosPremium() {
        List<Usuario> lista = new LinkedList<>();
        for (Usuario usuario: usuarios.values()) {
            if (usuario.isPremium()) {
                lista.add(usuario);
            }
        }
        return lista;
    }

    public void actualizarUsuario(Usuario usuario) {
        if (usuarios.containsKey(usuario.getUsername())) {
            //Usuario u = usuarios.get(usuario.getUsername());
            usuarios.remove(usuario.getUsername());
            usuarios.put(usuario.getUsername(), usuario);
        }
    }

    public void addUsuario(Usuario usuario) {
        usuarios.put(usuario.getUsername(), usuario);
        usuariosBBDD.put(usuario.getCodigo(), usuario);
    }

    public void removeUsuario(Usuario usuario) {
        usuarios.remove(usuario.getUsername());
        usuariosBBDD.remove(usuario.getCodigo());
    }

    public int getCodigoUsuario(Usuario usuario) {
        Set<Integer> listaIds = usuariosBBDD.keySet();
        for (int id : listaIds) {
            if (usuariosBBDD.get(id).equals(usuario));
            return id;
        }
        return -1;
    }
}
