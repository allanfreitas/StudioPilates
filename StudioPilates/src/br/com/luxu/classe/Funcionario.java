package br.com.luxu.classe;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * 
 * @author luciano
 */
@Entity(name="funcionario")
public class Funcionario implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
	
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="fun_codigo") 
    private Integer codigo;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="cid_codigo")
    private Cidade cidade;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_codigo")
    private Cargo cargo;

    @Column(name="fun_nome")
    @NotNull(message="Nome OBRIGATÓRIO!")
    private String nome;

    @Column(name="fun_sobrenome")
    private String sobrenome;

    @Column(name="fun_endereco")
    private String endereco;

    @Column(name="fun_numero")
    private String numero;
    
    @Column(name="fun_complemento")
    private String complemento;
    
    @Column(name="fun_bairro")
    private String bairro;

    @Column(name="fun_cep")
    private String cep;

    @Column(name="fun_fonecom")
    private String fonecom;

    @Column(name="fun_foneres")
    private String foneres;

    @Column(name="fun_fonecel")
    private String fonecel;

    @Column(name="fun_fonecel2")
    private String fonecel2;

    @Column(name="fun_fonecel3")
    private String fonecel3;

    @Column(name="fun_sexo")
    private String sexo;

    @Column(name="fun_idade")
    private Integer idade;

    @Column(name="fun_estadocivil")
    private String estadocivil;

    @Column(name="fun_email")
    private String email;

    @Column(name="fun_datanasc")
    private Date nascimento;

    @Column(name="fun_datacad")
    private Date cadastro;

    @Column(name="fun_datademissao")
    private Date demissao;

    @Column(name="fun_rg")
    private String rg;

    @Column(name="fun_cpf")
    private String cpf;
    
    @Column(name="fun_obs")
    private String obs;

    @Column(name="fun_foto")
    private String foto;

    @Column(name="fun_tituloeleitor")
    private String tituloeleitor;

    @Column(name="fun_carttrabnumero")
    private Long carttrabnumero;

    @Column(name="fun_carttrabserie")
    private String carttrabSerie;
    
    @Column(name="fun_ativo")
    @NotNull
    private String ativo;

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getFonecom() {
        return fonecom;
    }

    public void setFonecom(String fonecom) {
        this.fonecom = fonecom;
    }

    public String getFoneres() {
        return foneres;
    }

    public void setFoneres(String foneres) {
        this.foneres = foneres;
    }

    public String getFonecel() {
        return fonecel;
    }

    public void setFonecel(String fonecel) {
        this.fonecel = fonecel;
    }

    public String getFonecel2() {
        return fonecel2;
    }

    public void setFonecel2(String fonecel2) {
        this.fonecel2 = fonecel2;
    }

    public String getFonecel3() {
        return fonecel3;
    }

    public void setFonecel3(String fonecel3) {
        this.fonecel3 = fonecel3;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public String getEstadocivil() {
        return estadocivil;
    }

    public void setEstadocivil(String estadocivil) {
        this.estadocivil = estadocivil;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getNascimento() {
        return nascimento;
    }

    public void setNascimento(Date nascimento) {
        this.nascimento = nascimento;
    }

    public Date getCadastro() {
        return cadastro;
    }

    public void setCadastro(Date cadastro) {
        this.cadastro = cadastro;
    }

    public Date getDemissao() {
        return demissao;
    }

    public void setDemissao(Date demissao) {
        this.demissao = demissao;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getTituloeleitor() {
        return tituloeleitor;
    }

    public void setTituloeleitor(String tituloeleitor) {
        this.tituloeleitor = tituloeleitor;
    }

    public Long getCarttrabnumero() {
        return carttrabnumero;
    }

    public void setCarttrabnumero(Long carttrabnumero) {
        this.carttrabnumero = carttrabnumero;
    }

    public String getCarttrabSerie() {
        return carttrabSerie;
    }

    public void setCarttrabSerie(String carttrabSerie) {
        this.carttrabSerie = carttrabSerie;
    }

    public String getAtivo() {
        return ativo;
    }

    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Funcionario other = (Funcionario) obj;
	if (codigo == null) {
	    if (other.codigo != null)
		return false;
	} else if (!codigo.equals(other.codigo))
	    return false;
	return true;
    }

}
