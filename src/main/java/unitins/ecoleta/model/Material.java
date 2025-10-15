package unitins.ecoleta.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Material extends DefaultEntity {
    @Column(nullable = false, unique = true)
    private String nome;

    @Column(name = "nome_imagem")
    private String nomeImagem;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeImagem() {
        return nomeImagem;
    }

    public void setNomeImagem(String nomeImagem) {
        this.nomeImagem = nomeImagem;
    }
}
