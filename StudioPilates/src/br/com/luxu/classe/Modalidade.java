package br.com.luxu.classe;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 
 * @author luciano
 */
@Entity(name = "modalidade")
public class Modalidade implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "mod_codigo")
    private Integer codigo;

    @Column(name = "mod_descricao")
    @Size(message = "Tamanho máximo atingido", max = 30)
    @NotNull(message="Descrição OBRIGATÓRIA!")
    private String descricao;

    @Column(name = "mod_preco")
    private Double preco;

    @Column(name = "mod_qtsemanal")
    @Size(message = "Tamanho máximo atingido", max = 30)
    private String qtsemanal;

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

    public Double getPreco() {
	return preco;
    }

    public void setPreco(Double preco) {
	this.preco = preco;
    }

    public String getQtsemanal() {
	return qtsemanal;
    }

    public void setQtsemanal(String qtsemanal) {
	this.qtsemanal = qtsemanal;
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
	Modalidade other = (Modalidade) obj;
	if (codigo == null) {
	    if (other.codigo != null)
		return false;
	} else if (!codigo.equals(other.codigo))
	    return false;
	return true;
    }
}
