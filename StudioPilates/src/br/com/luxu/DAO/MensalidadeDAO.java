package br.com.luxu.DAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.luxu.classe.Mensalidade;
import br.com.luxu.classe.Turma_Aluno;

public class MensalidadeDAO extends HibernateDAO<Mensalidade> {

    public MensalidadeDAO(Session session) {
    	super(Mensalidade.class, session);
	}

	@SuppressWarnings("unchecked")
	public List<Mensalidade> getBeans() {
		Criteria crit = session.createCriteria(Mensalidade.class);
		crit.setCacheable(true);
		session.flush();
		session.clear();
		return crit.list();
    }

    @SuppressWarnings("unchecked")
	public List<Mensalidade> getBeanTurma(Integer tur_codigo) {
		Criteria crit;
		crit = session.createCriteria(Turma_Aluno.class);
		crit.add(Restrictions.eq("turma.codigo", tur_codigo));
		List<Turma_Aluno> listaTurmaAluno = new ArrayList<Turma_Aluno>();
		listaTurmaAluno = crit.list();
		crit = session.createCriteria(Mensalidade.class);
		crit.add(Restrictions.in("turmaAluno", listaTurmaAluno));
		return crit.list();
    }

    @SuppressWarnings("unchecked")
	public List<Mensalidade> getBeanAluno(Integer alu_codigo) {
		Criteria crit;
		crit = session.createCriteria(Turma_Aluno.class);
		crit.add(Restrictions.eq("aluno.codigo", alu_codigo));
		List<Turma_Aluno> listaTurmaAluno = new ArrayList<Turma_Aluno>();
		listaTurmaAluno = crit.list();
		crit = session.createCriteria(Mensalidade.class);
		crit.add(Restrictions.in("turmaAluno", listaTurmaAluno));
		return crit.list();
    }

    public Mensalidade getBean(Serializable codigo) {
		Mensalidade bean = (Mensalidade) session.get(Mensalidade.class, codigo);
		return bean;
    }

    @SuppressWarnings("unchecked")
	public List<Mensalidade> getBeansByIdsMensalidade(Date vencimento, List<Serializable> codigosTurmaAluno) {
		Criteria crit = session.createCriteria(Mensalidade.class);
		try {
		    //crit.add(Restrictions.in("turmaAluno.codigo", codigosTurmaAluno));
		    crit.add(Restrictions.not(Restrictions.in("turmaAluno.codigo", codigosTurmaAluno)));
		    // setar para o list todas os vencimentos que NÃO se encontram na tabela Mensalidade
		    //crit.add(Restrictions.like("vencimento", vencimento));
		    crit.add(Restrictions.not(Restrictions.like("vencimento", vencimento)));
		    return crit.list();
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return new ArrayList<Mensalidade>();
    }

    @SuppressWarnings("unchecked")
	public List<Mensalidade> getBeansByIdsMensalidade(Date vencimento) {
	Criteria crit = session.createCriteria(Mensalidade.class);
	try {
	    //crit.add(Restrictions.in("turmaAluno.codigo", codigosTurmaAluno));
	    // setar para o list todas os vencimentos que NÃO se encontram na tabela Mensalidade
	    //crit.add(Restrictions.like("vencimento", vencimento));
	    crit.add(Restrictions.like("vencimento", vencimento));
	    return crit.list();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return new ArrayList<Mensalidade>();
    }

}
