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

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 * 
 * @author luciano
 */
@Entity(name = "mensalidade")
public class Mensalidade implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "men_codigo")
    private Integer codigo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "turmaAluno_codigo")
    private Turma_Aluno turmaAluno;

    @Column(name = "men_data")
    @Temporal(TemporalType.DATE)
    private Date data;

    @Column(name = "men_datapagto")
    @Temporal(TemporalType.DATE)
    private Date pagamento;

    //@Temporal(TemporalType.DATE)
    @Column(name = "men_datavencimento")
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    //private Date vencimento;
    private DateTime vencimento;

    @Column(name = "men_valor")
    private Double valor;

    @Column(name = "men_desconto")
    private Double desconto;

    @Column(name = "men_acrescimo")
    private Double acrescimo;

    public Integer getCodigo() {
	return codigo;
    }

    public void setCodigo(Integer codigo) {
	this.codigo = codigo;
    }

    public Turma_Aluno getTurmaAluno() {
	return turmaAluno;
    }

    public void setTurmaAluno(Turma_Aluno turmaAluno) {
	this.turmaAluno = turmaAluno;
    }

    public Date getData() {
	return data;
    }

    public void setData(Date data) {
	this.data = data;
    }

    public Date getPagamento() {
	return pagamento;
    }

    public void setPagamento(Date pagamento) {
	this.pagamento = pagamento;
    }

    public DateTime getVencimento() {
	return vencimento;
    }

    public void setVencimento(DateTime vencimento) {
	this.vencimento = vencimento;
    }

    public Double getValor() {
	return valor;
    }

    public void setValor(Double valor) {
	this.valor = valor;
    }

    public Double getDesconto() {
	return desconto;
    }

    public void setDesconto(Double desconto) {
	this.desconto = desconto;
    }

    public Double getAcrescimo() {
	return acrescimo;
    }

    public void setAcrescimo(Double acrescimo) {
	this.acrescimo = acrescimo;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
	return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Mensalidade other = (Mensalidade) obj;
	if (codigo == null) {
	    if (other.codigo != null)
		return false;
	} else if (!codigo.equals(other.codigo))
	    return false;
	return true;
    }
}
