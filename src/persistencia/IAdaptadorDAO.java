package persistencia;

import modelo.Usuario;

import java.util.List;

public interface IAdaptadorDAO {

    public void registrarUsuario(Usuario usuario);
    public void borrarUsuario(Usuario usuario);
    public void modificarUsuario(Usuario usuario);
    public Usuario recuperarUsuario(String username);
    public List<Usuario> recuperarTodosUsuarios();
}
