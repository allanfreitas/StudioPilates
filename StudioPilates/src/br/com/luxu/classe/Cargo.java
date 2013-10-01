package br.com.luxu.classe;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * 
 * @author luciano
 */
@Entity(name = "cargo")
public class Cargo implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "car_codigo")
    private Integer codigo;

    @Column(name = "car_descricao")
    @NotNull(message="Descrição OBRIGATÓRIA!")
    private String descricao;

    @Column(name = "car_salario")
    private Double salario;

    @Column(name = "car_salariocomissao")
    private Double salariocomissao;

    public Integer getCodigo() {
	return codigo;
    }

    public void setCodigo(Integer codigo) {
	this.codigo = codigo;
    }

    public String getDescricao() {
	return descricao;
    }

    public void setDescricao(String descricao) {
	this.descricao = descricao;
    }

    public Double getSalario() {
	return salario;
    }

    public void setSalario(Double salario) {
	this.salario = salario;
    }

    public Double getSalariocomissao() {
	return salariocomissao;
    }

    public void setSalariocomissao(Double salariocomissao) {
	this.salariocomissao = salariocomissao;
    }

}
