package br.com.luxu.classe;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * @author LUXU
 */
@Entity(name = "turma")
public class Turma implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tur_codigo")
    private Integer codigo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fun_codigo")
    private Funcionario funcionario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mod_codigo")
    private Modalidade modalidade;

    @OneToMany(mappedBy = "turma")
    private Set<Turma_Aluno> turmas = new HashSet<Turma_Aluno>();

    @Column(name = "tur_descricao")
    private String descricao;

    @Column(name = "tur_hora_inicio")
    private String horaInicio;

    @Column(name = "tur_hora_fim")
    private String horaFim;

    @Column(name = "tur_capacidade")
    private Integer capacidade;

    @Column(name = "tur_inscritos")
    private Integer inscritos;

    @Column(name = "tur_status")
    private Character status;

    @Column(name = "tur_dia")
    private String dia;

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

    public Modalidade getModalidade() {
	return modalidade;
    }

    public void setModalidade(Modalidade modalidade) {
	this.modalidade = modalidade;
    }

    public Set<Turma_Aluno> getTurmas() {
	return turmas;
    }

    public void setTurmas(Set<Turma_Aluno> turmas) {
	this.turmas = turmas;
    }

    public String getDescricao() {
	return descricao;
    }

    public void setDescricao(String descricao) {
	this.descricao = descricao;
    }

    public String getHoraInicio() {
	return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
	this.horaInicio = horaInicio;
    }

    public String getHoraFim() {
	return horaFim;
    }

    public void setHoraFim(String horaFim) {
	this.horaFim = horaFim;
    }

    public Integer getCapacidade() {
	return capacidade;
    }

    public void setCapacidade(Integer capacidade) {
	this.capacidade = capacidade;
    }

    public Integer getInscritos() {
	return inscritos;
    }

    public void setInscritos(Integer inscritos) {
	this.inscritos = inscritos;
    }

    public Character getStatus() {
	return status;
    }

    public void setStatus(Character status) {
	this.status = status;
    }

    public String getDia() {
	return dia;
    }

    public void setDia(String dia) {
	this.dia = dia;
    }

}
