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

/**
 * 
 * @author luciano
 */
@Entity(name = "turma_aluno")
public class Turma_Aluno implements Serializable {

    /**
	 * 	
	 */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "turmaAluno_codigo")
    private Integer codigo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "alu_codigo")
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tur_codigo")
    private Turma turma;

    @Column(name = "turma_inicio")
    @Temporal(TemporalType.DATE)
    private Date inicio;

    @Column(name = "turma_fim")
    @Temporal(TemporalType.DATE)
    private Date fim;

    @Column(name = "turma_valor")
    private Double valor;

    @Column(name = "turma_obs")
    private String obs;

    public Integer getCodigo() {
	return codigo;
    }

    public void setCodigo(Integer codigo) {
	this.codigo = codigo;
    }

    public Aluno getAluno() {
	return aluno;
    }

    public void setAluno(Aluno aluno) {
	this.aluno = aluno;
    }

    public Turma getTurma() {
	return turma;
    }

    public void setTurma(Turma turma) {
	this.turma = turma;
    }

    public Date getInicio() {
	return inicio;
    }

    public void setInicio(Date inicio) {
	this.inicio = inicio;
    }

    public Date getFim() {
	return fim;
    }

    public void setFim(Date fim) {
	this.fim = fim;
    }

    public Double getValor() {
	return valor;
    }

    public void setValor(Double valor) {
	this.valor = valor;
    }

    public String getObs() {
	return obs;
    }

    public void setObs(String obs) {
	this.obs = obs;
    }

}
