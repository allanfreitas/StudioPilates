package br.com.luxu.classe;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 * 
 * @author luciano
 */
@Entity(name = "usuario")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "usu_codigo")
    private Integer codigo;

    @OneToOne
    @JoinColumn(name = "fun_codigo")
    private Funcionario funcionario;

    @Column(name = "usu_login")
    @NotNull(message="Descrição OBRIGATÓRIA!")
    private String login;

    @Column(name = "usu_senha")
    @NotNull(message="Digite a senha")
    private String senha;

    @Column(name = "usu_nivelacesso")
    @NotNull(message="Escolha o nível de acesso!")
    private String nivelacesso;

    @Column(name = "usu_ativo")
    @NotNull(message="Ativo SIM/NÃO")
    private String ativo;

    public Integer getCodigo() {
	return codigo;
    }

    public void setCodigo(Integer codigo) {
	this.codigo = codigo;
    }

    public Funcionario getFuncionario() {
	return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
	this.funcionario = funcionario;
    }

    public String getLogin() {
	return login;
    }

    public void setLogin(String login) {
	this.login = login;
    }

    public String getSenha() {
	return senha;
    }

    public void setSenha(String senha) {
	this.senha = senha;
    }

    public String getNivelacesso() {
	return nivelacesso;
    }

    public void setNivelacesso(String nivelacesso) {
	this.nivelacesso = nivelacesso;
    }

    public String getAtivo() {
	return ativo;
    }

    public void setAtivo(String ativo) {
	this.ativo = ativo;
    }
}