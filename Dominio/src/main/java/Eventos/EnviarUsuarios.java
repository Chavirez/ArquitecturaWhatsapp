package Eventos;

import java.util.List;
import DTOs.UsuarioDTO;

public class EnviarUsuarios {

    private final List<UsuarioDTO> usuarios;

    public EnviarUsuarios(List<UsuarioDTO> usuarios) {
        this.usuarios = usuarios;
    }

    public List<UsuarioDTO> getUsuarios() {
        return usuarios;
    }
}
