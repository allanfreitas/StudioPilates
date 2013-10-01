package br.com.luxu.classe;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;



/**
 * 
 * @author luciano
 */
@Entity(name = "frequencia")
public class Frequencia implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "fre_data")
    @Temporal(TemporalType.DATE)
    private Date data;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "alu_codigo")
    private Aluno aluno;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tur_codigo")
    private Turma turma;

    @OneToOne
    @JoinColumn(name = "tur_turreposicao")
    private Turma turmaReposicao;

    @Column(name = "fre_presenca")
    private String presenca;

    @Column(name = "fre_dtreposicao")
    @Temporal(TemporalType.DATE)
    private Date datareposicao;

    @Column(name = "fre_repos")
    private String repos;

    @Column(name = "fre_obs")
    private String obs;
    
    public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
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

	public String getPresenca() {
		return presenca;
	}

	public void setPresenca(String presenca) {
		this.presenca = presenca;
	}

	public Date getDatareposicao() {
		return datareposicao;
	}

	public void setDatareposicao(Date datareposicao) {
		this.datareposicao = datareposicao;
	}

	public Turma getTurmaReposicao() {
		return turmaReposicao;
	}

	public void setTurmaReposicao(Turma turmaReposicao) {
		this.turmaReposicao = turmaReposicao;
	}

	public String getRepos() {
		return repos;
	}

	public void setRepos(String repos) {
		this.repos = repos;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	@Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((aluno == null) ? 0 : aluno.hashCode());
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
	Frequencia other = (Frequencia) obj;
	if (aluno == null) {
	    if (other.aluno != null)
		return false;
	} else if (!aluno.equals(other.aluno))
	    return false;
	return true;
    }
}
