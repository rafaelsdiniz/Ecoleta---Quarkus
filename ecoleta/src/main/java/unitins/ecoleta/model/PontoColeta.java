package unitins.ecoleta.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "ponto_coleta")
public class PontoColeta extends DefaultEntity {
    @Column(nullable = false)
    private String nome;

    private String descricao;

    private String email;

    
    @Column(nullable = false)
    private String endereco;
    
    @Column(name = "nome_imagem")
    private String nomeImagem;
    
    @Column(nullable = false)
    private Double latitude;
    
    @Column(nullable = false)
    private Double longitude;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_ponto_coleta", nullable = false)
    private List<Telefone> listaTelefone;

    @ManyToMany
    @JoinTable(name = "material_ponto_coleta",joinColumns = @JoinColumn(name = "id_ponto_coleta"),
                inverseJoinColumns = @JoinColumn(name = "id_material"))
    private List<Material> listaMaterial;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_ponto_coleta", nullable = false)
    private List<DiaFuncionamento> listaDiaFuncionamento;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNomeImagem() {
        return nomeImagem;
    }

    public void setNomeImagem(String nomeImagem) {
        this.nomeImagem = nomeImagem;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public List<Telefone> getListaTelefone() {
        return listaTelefone;
    }

    public void setListaTelefone(List<Telefone> listaTelefone) {
        this.listaTelefone = listaTelefone;
    }

    public List<Material> getListaMaterial() {
        return listaMaterial;
    }

    public void setListaMaterial(List<Material> listaMaterial) {
        this.listaMaterial = listaMaterial;
    }

    public List<DiaFuncionamento> getListaDiaFuncionamento() {
        return listaDiaFuncionamento;
    }

    public void setListaDiaFuncionamento(List<DiaFuncionamento> listaDiaFuncionamento) {
        this.listaDiaFuncionamento = listaDiaFuncionamento;
    }
}
