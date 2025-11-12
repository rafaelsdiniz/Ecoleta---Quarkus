package unitins.ecoleta.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Material extends DefaultEntity {

    @Column(nullable = false, unique = true)
    private String nome;

    // ⚠️ Removemos o @Lob para evitar o stream de Large Object
    @Column(name = "imagem_base64", columnDefinition = "TEXT")
    private String imagemBase64;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getImagemBase64() {
        return imagemBase64;
    }

    public void setImagemBase64(String imagemBase64) {
        this.imagemBase64 = imagemBase64;
    }
}
