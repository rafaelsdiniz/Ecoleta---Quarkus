package unitins.ecoleta.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "chat",
       uniqueConstraints = {@UniqueConstraint(columnNames = {"usuario_id", "ponto_coleta_id"})},
       indexes = {@Index(name = "idx_chat_usuario", columnList = "usuario_id"),
                  @Index(name = "idx_chat_ponto", columnList = "ponto_coleta_id")})
public class Chat extends DefaultEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ponto_coleta_id", nullable = false)
    private PontoColeta pontoColeta;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Mensagem> mensagens = new ArrayList<>();

    // getters / setters
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public PontoColeta getPontoColeta() { return pontoColeta; }
    public void setPontoColeta(PontoColeta pontoColeta) { this.pontoColeta = pontoColeta; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public List<Mensagem> getMensagens() { return mensagens; }
    public void setMensagens(List<Mensagem> mensagens) { this.mensagens = mensagens; }

    public void addMensagem(Mensagem m) {
        mensagens.add(m);
        m.setChat(this);
    }

    public void removeMensagem(Mensagem m) {
        mensagens.remove(m);
        m.setChat(null);
    }
}
