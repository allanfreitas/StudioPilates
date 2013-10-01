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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 * 
 * @author luciano
 */
@Entity(name = "aluno")
public class Aluno implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "alu_codigo")
    private Integer codigo;

    // Vários alunos podem estar em uma cidade
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cid_codigo")
    private Cidade cidade;

    @Column(name = "alu_nome")
    @NotNull(message="Nome OBRIGATÓRIO!")
    private String nome;

    @Column(name = "alu_sobrenome")
    private String sobrenome;

    @Column(name = "alu_endereco")
    private String endereco;

    @Column(name = "alu_numero")
    private String numero;

    @Column(name = "alu_complemento")
    private String complemento;

    @Column(name = "alu_bairro")
    private String bairro;

    @Column(name = "alu_cep")
    private String cep;

    @Column(name = "alu_fonecom")
    private String fonecom;

    @Column(name = "alu_foneres")
    private String foneres;

    @Column(name = "alu_fonecel")
    private String fonecel;

    @Column(name = "alu_fonecel2")
    private String fonecel2;

    @Column(name = "alu_sexo")
    private String sexo;

    @Column(name = "alu_idade")
    private Integer idade;

    @Column(name = "alu_estadocivil")
    private String estcivil;

    @Column(name = "alu_email")
    private String email;

    @Column(name = "alu_datanasc")
    private Date nascimento;

    @Column(name = "alu_datacad")
    @Temporal(TemporalType.DATE)
    private Date cadastro;

    @Column(name = "alu_datasaida")
    @Temporal(TemporalType.DATE)
    private Date demissao;

    @Column(name = "alu_peso")
    private Double peso;

    @Column(name = "alu_altura")
    private Double altura;

    @Column(name = "alu_rg")
    private String rg;

    @Column(name = "alu_cpf")
    private String cpf;

    @Column(name = "alu_obs")
    private String obs;

    @Column(name = "alu_foto")
    private String foto;

    @Column(name = "alu_desconto")
    private Double desconto;

    @Column(name = "alu_atividadefisica")
    private Character atividadefisica;

    @Column(name = "alu_descatividade")
    private String descAtividade;

    @Column(name = "alu_qtdatividade")
    private String qtatifisica;

    @Column(name = "alu_dorcorpo")
    private Character dorcorpo;

    @Column(name = "alu_descdorcorpo")
    private String descDorCorpo;

    @Column(name = "alu_patologia")
    private Character patologia;

    @Column(name = "alu_descpatologia")
    private String descPatologia;

    @Column(name = "alu_restricaomedica")
    private Character restricaomedica;

    @Column(name = "alu_desrestricao")
    private String descRestricaoMedica;

    @Column(name = "alu_profissao")
    private String profissao;

    @Column(name = "alu_nivelescolaridade")
    private Character escolaridade;

    @Column(name = "alu_graduacao")
    private String graduacao;

    @Column(name = "alu_ativo")
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

    public String getEstcivil() {
	return estcivil;
    }

    public void setEstcivil(String estcivil) {
	this.estcivil = estcivil;
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

    public Double getPeso() {
	return peso;
    }

    public void setPeso(Double peso) {
	this.peso = peso;
    }

    public Double getAltura() {
	return altura;
    }

    public void setAltura(Double altura) {
	this.altura = altura;
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

    public Double getDesconto() {
	return desconto;
    }

    public void setDesconto(Double desconto) {
	this.desconto = desconto;
    }

    public Character getAtividadefisica() {
	return atividadefisica;
    }

    public void setAtividadefisica(Character atividadefisica) {
	this.atividadefisica = atividadefisica;
    }

    public String getDescAtividade() {
	return descAtividade;
    }

    public void setDescAtividade(String descAtividade) {
	this.descAtividade = descAtividade;
    }

    public String getQtatifisica() {
	return qtatifisica;
    }

    public void setQtatifisica(String qtatifisica) {
	this.qtatifisica = qtatifisica;
    }

    public Character getDorcorpo() {
	return dorcorpo;
    }

    public void setDorcorpo(Character dorcorpo) {
	this.dorcorpo = dorcorpo;
    }

    public String getDescDorCorpo() {
	return descDorCorpo;
    }

    public void setDescDorCorpo(String descDorCorpo) {
	this.descDorCorpo = descDorCorpo;
    }

    public Character getPatologia() {
	return patologia;
    }

    public void setPatologia(Character patologia) {
	this.patologia = patologia;
    }

    public String getDescPatologia() {
	return descPatologia;
    }

    public void setDescPatologia(String descPatologia) {
	this.descPatologia = descPatologia;
    }

    public Character getRestricaomedica() {
	return restricaomedica;
    }

    public void setRestricaomedica(Character restricaomedica) {
	this.restricaomedica = restricaomedica;
    }

    public String getDescRestricaoMedica() {
	return descRestricaoMedica;
    }

    public void setDescRestricaoMedica(String descRestricaoMedica) {
	this.descRestricaoMedica = descRestricaoMedica;
    }

    public String getProfissao() {
	return profissao;
    }

    public void setProfissao(String profissao) {
	this.profissao = profissao;
    }

    public Character getEscolaridade() {
	return escolaridade;
    }

    public void setEscolaridade(Character escolaridade) {
	this.escolaridade = escolaridade;
    }

    public String getGraduacao() {
	return graduacao;
    }

    public void setGraduacao(String graduacao) {
	this.graduacao = graduacao;
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
	Aluno other = (Aluno) obj;
	if (codigo == null) {
	    if (other.codigo != null)
		return false;
	} else if (!codigo.equals(other.codigo))
	    return false;
	return true;
    }
}