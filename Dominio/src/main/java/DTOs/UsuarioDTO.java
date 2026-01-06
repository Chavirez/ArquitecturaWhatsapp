package DTOs;

public class UsuarioDTO {
    private int id;
    private String nombre;
    private String contrasenia;

    public UsuarioDTO() {}

    public UsuarioDTO(int id, String nombre, String contrasenia) {
        this.id = id;
        this.nombre = nombre;
        this.contrasenia = contrasenia;
    }

    public int getId() { 
        return id; 
    }
    
    public void setId(int id) { 
        this.id = id; 
    }
    
    public String getNombre() {
        return nombre; 
    }
    
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }
    
    public String getPassword() { 
        return contrasenia;
    }
    
    public void setPassword(String password) {
        this.contrasenia = password; 
    }
}