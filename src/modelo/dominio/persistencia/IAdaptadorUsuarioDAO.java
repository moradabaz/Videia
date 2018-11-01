package modelo.dominio.persistencia;

import modelo.dominio.Usuario;

import java.util.List;


public interface IAdaptadorUsuarioDAO {


    public void registrarUsuario(Usuario usuario);
    public void borrarUsuario(Usuario usuario);
    public void modificarUsuario(Usuario usuario);
    public Usuario recuperarUsuario(int codigo);
    public List<Usuario> recuperarTodosUsuarios();
}
