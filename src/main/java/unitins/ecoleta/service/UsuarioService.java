package unitins.ecoleta.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import unitins.ecoleta.dto.Request.UsuarioRequestDTO;
import unitins.ecoleta.dto.Response.LoginResponseDTO;
import unitins.ecoleta.dto.Response.UsuarioResponseDTO;
import unitins.ecoleta.model.Usuario;
import unitins.ecoleta.repository.UsuarioRepository;

@ApplicationScoped
public class UsuarioService {

    @Inject
    UsuarioRepository usuarioRepository;

    private String hashSenha(String senha) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(senha.getBytes());
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar hash da senha", e);
        }
    }

    private String gerarToken() {
        return UUID.randomUUID().toString();
    }

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
        usuario.setSenha(hashSenha(dto.senha()));
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
        usuario.setSenha(hashSenha(dto.senha()));
        usuario.setTelefone(dto.telefone());
        usuario.setTipoUsuario(dto.tipoUsuario());

        return new UsuarioResponseDTO(usuario);
    }

    @Transactional
    public void delete(Long id) {
        usuarioRepository.deleteById(id);
    }

    public LoginResponseDTO autenticar(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null)
            return null;

        if (!usuario.getSenha().equals(hashSenha(senha)))
            return null;

        // Generate a simple token (in production, use JWT)
        String token = UUID.randomUUID().toString();
        
        return LoginResponseDTO.valueOf(usuario, token);
    }
}
