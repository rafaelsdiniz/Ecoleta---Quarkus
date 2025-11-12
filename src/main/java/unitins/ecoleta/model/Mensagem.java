package unitins.ecoleta.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import unitins.ecoleta.model.enums.Remetente;

@Entity
@Table(name = "mensagem",
       indexes = {@Index(name = "idx_mensagem_chat", columnList = "chat_id"),
                  @Index(name = "idx_mensagem_data", columnList = "data_envio")})
public class Mensagem extends DefaultEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @Enumerated(EnumType.STRING)
    @Column(name = "remetente", nullable = false, length = 30)
    private Remetente remetente;

    @Column(name = "conteudo", nullable = false)
    private String conteudo;

    @Column(name = "data_envio", nullable = false)
    private LocalDateTime dataEnvio = LocalDateTime.now();

    // getters / setters
    public Chat getChat() { return chat; }
    public void setChat(Chat chat) { this.chat = chat; }

    public Remetente getRemetente() { return remetente; }
    public void setRemetente(Remetente remetente) { this.remetente = remetente; }

    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }

    public LocalDateTime getDataEnvio() { return dataEnvio; }
    public void setDataEnvio(LocalDateTime dataEnvio) { this.dataEnvio = dataEnvio; }
}
