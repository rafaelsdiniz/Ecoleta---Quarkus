package unitins.ecoleta.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import unitins.ecoleta.dto.Request.UsuarioRequestDTO;
import unitins.ecoleta.dto.Response.UsuarioResponseDTO;
import unitins.ecoleta.model.Usuario;
import unitins.ecoleta.repository.UsuarioRepository;

@ApplicationScoped
public class UsuarioService {

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    HashService hashService;

    public List<UsuarioResponseDTO> findAll() {
        return usuarioRepository.listAll().stream()
                .map(UsuarioResponseDTO::new)
                .toList();
    }

    public UsuarioResponseDTO findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id);
        if (usuario == null)
            return null;
        return new UsuarioResponseDTO(usuario);
    }

    @Transactional
    public UsuarioResponseDTO create(UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(hashService.hashPassword(dto.senha()));
        usuario.setTelefone(dto.telefone());
        usuario.setTipoUsuario(dto.tipoUsuario());

        usuarioRepository.persist(usuario);

        return new UsuarioResponseDTO(usuario);
    }

    @Transactional
    public UsuarioResponseDTO update(Long id, UsuarioRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(id);
        if (usuario == null)
            return null;

        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        if (dto.senha() != null && !dto.senha().isEmpty()) {
            // Check if password is already a BCrypt hash (starts with $2a$, $2b$, or $2y$)
            if (!dto.senha().startsWith("$2")) {
                usuario.setSenha(hashService.hashPassword(dto.senha()));
            } else {
                usuario.setSenha(dto.senha());
            }
        }
        usuario.setTelefone(dto.telefone());
        usuario.setTipoUsuario(dto.tipoUsuario());

        return new UsuarioResponseDTO(usuario);
    }

    @Transactional
    public void delete(Long id) {
        usuarioRepository.deleteById(id);
    }

    public Usuario autenticar(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            return null;
        }

        if (!hashService.verifyPassword(senha, usuario.getSenha())) {
            return null;
        }

        return usuario;
    }
}
