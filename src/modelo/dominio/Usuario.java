package modelo.dominio;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Usuario {

    public static final int TAM_RECIENTES = 10;
    private static final int EDAD_JOVEN_MAX = 25;

    private int codigo;
    private String username;
    private String password;
    private String nombre;
    private String apellidos;
    private Date fechaNac;
    private String email;
    private boolean premium;
    private List<VideoList> myVideoLists;
    private List<Video> videosRecientes;



    public Usuario(String username, String password, String nombre, String apellidos, Date fechaNac, String email) {
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNac = fechaNac;
        this.email = email;
        myVideoLists = new LinkedList<VideoList>();
        videosRecientes = new LinkedList<Video>();
        this.codigo = -1111;
    }

    public Usuario(int codigo, String username, String password, String nombre, String apellidos, String fechaNac, String email) {
        this.codigo = codigo;
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.premium = false;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        Date fecha = null;
        try {
            this.fechaNac = formatter.parse(fechaNac);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
      //  this.VideoesRecientes = new LinkedList<Video>();
      //  this.descuento = new NoDescuento();
        this.codigo = -111;
        myVideoLists = new LinkedList<VideoList>();
        videosRecientes = new LinkedList<Video>();
    }

    /**
     * @return Devuelve una lista con todos los videos del usuario
     */
    public LinkedList<VideoList> getMyVideoLists() {
        return (LinkedList<VideoList>) myVideoLists;
    }

    /**
     * @return Devuelve una lista de videos recientes
     */
    public LinkedList<Video> getVideosRecientes() {
        return (LinkedList<Video>) videosRecientes;
    }

    /**
     * Anade una lista de videos al conjunto de listas del usuario
     * @param videoList
     */
    public void addVideoList(VideoList videoList){
        if (myVideoLists.contains(videoList)) {
            this.myVideoLists.add(videoList);
        }
    }

    /**
     * @return Retorna el Id de la BBDD
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     * @return Retorna el nombre de usuario
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return Retorna la contrasena del usuario
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return Retorna el nombre del usuario
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @return Retorna los apellidos del usuario
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * @return Retorna (Objeto Date) la fecha de nacimiento
     */
    public Date getFechaNac() {
        return fechaNac;
    }

    /**
     * @return Retorna el email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return Retorna la condicion de premium del usuario
     */
    public boolean isPremium() {
        return premium;
    }


    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setFechaNac(Date fechaNac) {
        this.fechaNac = fechaNac;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public void setMyVideoLists(List<VideoList> myVideoLists) {
        this.myVideoLists = myVideoLists;
    }

    public void setVideosRecientes(List<Video> videosRecientes) {
        this.videosRecientes = videosRecientes;
    }


    /**
     *
     * @param nombre
     * @return Retorna la videolist dado el nombre. Si no existe, retorna NULL
     */
    public VideoList getListaVideo (String nombre){
        for (VideoList l : myVideoLists) {
            if (l.tieneMismoNombre(nombre)) {
                return l;
            }
        }
        return null;
    }


    /**
     * @return Retorna los codigos de todas la listas separadas por ":" en un string
     */
    public String getStringCodigosVideoesRecientesString() {
        String linea = "";
        for (Video c : videosRecientes) {
            linea += c.getCodigo() + ":";
        }
        return linea.trim();
    }

    public String getStringCodigosVideoList() {
        String linea = "";
        for (VideoList l : myVideoLists) {
            linea += l.getCodigo() + ":";
        }
        return linea.trim();
    }

    public String getStringFecha() {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(fechaNac);
    }

    /**
     * @return Retorna la edad
     */
    public int getEdad() {
        Calendar now = Calendar.getInstance();
        Calendar dob = Calendar.getInstance();
        dob.setTime(fechaNac);
        if (dob.after(now)) {
            throw new IllegalArgumentException("Can't be born in the future");
        }
        int year1 = now.get(Calendar.YEAR);
        int year2 = dob.get(Calendar.YEAR);
        int age = year1 - year2;
        int month1 = now.get(Calendar.MONTH);
        int month2 = dob.get(Calendar.MONTH);
        if (month2 > month1) {
            age--;
        } else if (month1 == month2) {
            int day1 = now.get(Calendar.DAY_OF_MONTH);
            int day2 = dob.get(Calendar.DAY_OF_MONTH);
            if (day2 > day1) {
                age--;
            }
        }
        return age;
    }

 /*   public Descuento getDescuento() {
        return descuento;
    }

    public String getDescuentoString() {
        if (descuento instanceof DescuentoFijo) return "descuentoFijo";
        if (descuento instanceof DescuentoJovenes) return "descuentoJoven";
        return "noDescuento";
    }*/

    public void mostrarListas() {
        for (VideoList l : myVideoLists) {
            System.out.println(l.getNombre());
            for (Video Video : l.getVideos()) {
                System.out.println("   Titulo: " + Video.getTitulo());
            }
        }
    }

    public void anadirVideoList(VideoList... VideoList) {
        myVideoLists.addAll(Arrays.asList(VideoList));
    }

    public void anadirVideoList(List<VideoList> list) {
        /*for (VideoList vL : list) {
            if (!myVideoLists.contains(vL)) {
                myVideoLists.add(vL);
            }
        }*/
        list.stream().filter(VideoList -> !myVideoLists.contains(VideoList))
                      .forEach(myVideoLists::add);
    }

    public void anadirVideoReciente(Video Video) {
        if (videosRecientes.size() >= TAM_RECIENTES) {
            ((LinkedList) videosRecientes).removeFirst();
        }
        if (videosRecientes.contains(Video)) {
            videosRecientes.remove(Video);
        }
        ((LinkedList) videosRecientes).addLast(Video);
    }

    public void anadirVideoALista(Video Video, VideoList PL) {
        if (myVideoLists.contains(PL)) {
            PL.addVideo(Video);
        }
    }


    public void eliminarVideoList(VideoList videoList) {
        if (myVideoLists.contains(videoList)) {
            this.myVideoLists.remove(videoList);
        }
    }

    public void eliminarVideoDeLista(Video Video, VideoList videoList) {
        if (myVideoLists.contains(videoList)) {
            if (myVideoLists.contains(Video)) {
                myVideoLists.remove(Video);
            }
        }
    }

    public void setVideoesRecientes(LinkedList<Video> recientes) {
        this.videosRecientes = new LinkedList<Video>(recientes);
    }

    public boolean contieneVideoList(String nombreLista) {
        if (myVideoLists.isEmpty() || myVideoLists == null ) return false;
        for (VideoList pl : myVideoLists) {
            if (pl.tieneMismoNombre(nombreLista)) {
                return true;
            }
        }
        return false;
    }

    public int getDescuento() {
        return 0;
    }
}
