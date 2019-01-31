package modelo.dominio;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class Usuario {

    public static final int TAM_RECIENTES = 10;

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
    private Filtro filtro;
    private HashMap<String, Filtro> filtrosDisponibles;

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
        this.filtro = NoFiltro.getUnicaInstancia();
        this.filtrosDisponibles = inicizalizarFiltros();
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
        this.codigo = -111;
        myVideoLists = new LinkedList<VideoList>();
        videosRecientes = new LinkedList<Video>();
        this.filtro = NoFiltro.getUnicaInstancia();
        this.filtrosDisponibles = inicizalizarFiltros();
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

    public void setFechaNac(String fecha) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.fechaNac = formatter.parse(fecha);
            System.out.println(this.fechaNac);
        } catch (ParseException e) {
            System.err.println("Fallo en el parseo de la fecha :S");
        }
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
            if (!PL.contains(Video))
                 PL.addVideo(Video);
        }
    }

    public void eliminarVideoList(VideoList videoList) {
        if (myVideoLists.contains(videoList)) {
            this.myVideoLists.remove(videoList);
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

    public boolean isBirthday() {
        Date hoy = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(hoy);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);

        calendar.setTime(fechaNac);
        int birthday = calendar.get(Calendar.DAY_OF_MONTH);
        int birthmonth = calendar.get(Calendar.MONTH);

        if (month == birthmonth) {
            if (day == birthday)
                return true;
        }
        return false;
    }

    public String getFechaNacString() { // TODO: Aqui esta el poblema con las fecha xD
        LocalDate localDate = fechaNac.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        int day = localDate.getDayOfMonth();
        return day + "-" + month + "-" + year;
    }

    public Filtro getFiltro(String filtro) {
        return filtrosDisponibles.get(filtro);
    }

    public boolean contieneEsteVideo(Video video) {
        for (VideoList vdList: getMyVideoLists()) {
           if (vdList.contains(video))
                return true;
        }
        return false;
    }

    public Filtro getFiltro() {
        return filtro;
    }

    public String getFiltroNombre() {
        if (filtro == null)
            filtro = NoFiltro.getUnicaInstancia();
        return filtro.getNombre();
    }

    public void setFiltro(String nombreFiltro) {
        if (premium)    this.filtro = filtrosDisponibles.get(nombreFiltro);
    }

    private HashMap<String, Filtro> inicizalizarFiltros() {
        HashMap<String, Filtro> filtros = new HashMap<>();
        filtros.put(NoFiltro.getUnicaInstancia().getNombre(), NoFiltro.getUnicaInstancia());
        filtros.put(MisListasFiltro.getUnicaInstancia().getNombre(), MisListasFiltro.getUnicaInstancia());
        filtros.put(NombresLargosFiltro.getUnicaInstancia().getNombre(), NombresLargosFiltro.getUnicaInstancia());
        filtros.put(MenoresFiltro.getUnicaInstancia().getNombre(), MenoresFiltro.getUnicaInstancia());
        filtros.put(ImpopularesFiltro.getUnicaInstancia().getNombre(), ImpopularesFiltro.getUnicaInstancia());
        return filtros;
    }

}


