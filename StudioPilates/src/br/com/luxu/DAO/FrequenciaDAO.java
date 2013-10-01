package br.com.luxu.DAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.luxu.classe.Frequencia;
import br.com.luxu.classe.Turma;

public class FrequenciaDAO extends HibernateDAO<Frequencia> {

    public FrequenciaDAO(Session session) {
	super(Frequencia.class, session);
    }

    @SuppressWarnings("unchecked")
	@Override
    public List<Frequencia> getBeans() {
	Criteria crit = session.createCriteria(Frequencia.class);
	crit.setCacheable(true);
	session.flush();
	session.clear();
	return crit.list();
    }

    @SuppressWarnings("unchecked")
	public List<Turma> getBeanTurma(Integer codigoModalidade, Integer inscritos) {
	Criteria crit;
	crit = session.createCriteria(Turma.class);
	// trazer todas as turmas com a modalidade passada pelo codigoModalidade
	crit.add(Restrictions.eq("modalidade.codigo", codigoModalidade));
	// capacidade maior que o nro de inscritos 
	crit.add(Restrictions.gt("capacidade", inscritos));
	List<Turma> listaTurma = crit.list();
	return listaTurma;
    }

    @SuppressWarnings("unchecked")
	public List<Frequencia> getBeanAluno(Integer alu_codigo) {
	Criteria crit;
	crit = session.createCriteria(Turma.class);
	crit.add(Restrictions.eq("aluno.codigo", alu_codigo));
	List<Turma> listaTurmaAluno = new ArrayList<Turma>();
	listaTurmaAluno = crit.list();
	crit = session.createCriteria(Frequencia.class);
	crit.add(Restrictions.in("turmaAluno", listaTurmaAluno));
	return crit.list();
    }

    @Override
    public Frequencia getBean(Serializable codigo) {
	Frequencia bean = (Frequencia) session.get(Frequencia.class, codigo);
	return bean;
    }

    @SuppressWarnings("unchecked")
	public List<Frequencia> getBeansByIdsFrequencia(Date vencimento, List<Serializable> codigosTurmaAluno) {
	Criteria crit = session.createCriteria(Frequencia.class);
	try {
	    //crit.add(Restrictions.in("turmaAluno.codigo", codigosTurmaAluno));
	    crit.add(Restrictions.not(Restrictions.in("turmaAluno.codigo", codigosTurmaAluno)));
	    // setar para o list todas os vencimentos que NÃO se encontram na tabela Frequencia
	    //crit.add(Restrictions.like("vencimento", vencimento));
	    crit.add(Restrictions.not(Restrictions.like("vencimento", vencimento)));
	    return crit.list();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return new ArrayList<Frequencia>();
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Frequencia> getBeanByDataTurma(Date data,Serializable tur_codigo) {
		Criteria crit = session.createCriteria(Frequencia.class);
		// retorna uma lista onde CONTENHA o tur_codigo
		crit.add(Restrictions.eq("turma.codigo", tur_codigo));
		// retorna uma lista onde CONTENHA a data passada
		crit.add(Restrictions.eq("data", data));
		List lista = crit.list();
		return lista;
    }

}
