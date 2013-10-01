package br.com.luxu.DAO;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.luxu.classe.Turma;

public class TurmaDAO extends HibernateDAO<Turma> {

    public TurmaDAO(Session session) {
	super(Turma.class, session);
    }

	public Turma getBean(Serializable codigo) {
		Turma bean = (Turma) session.get(Turma.class,codigo);
		return bean;
	}

    @SuppressWarnings("unchecked")
	public List<Turma> getBeans() {
		Criteria crit = session.createCriteria(Turma.class);
		crit.setCacheable(true);
		List<Turma> bean = (List<Turma>) crit.list();
		//session.flush();
		//session.clear();
		return bean;
    }

	@SuppressWarnings("unchecked")
	public List<Turma> getBeanByReposicao(Integer mod_codigo, Integer tur_codigo, Integer inscritos) {
		Criteria crit = session.createCriteria(Turma.class);
		// pesquisar desde que a turma que será pesquisada NÃO entrar no critério da pesquisa
		crit.add(Restrictions.not(Restrictions.eq("codigo", tur_codigo)));
		// trazer a modalidade que está sendo passada pela turma escolhida
		crit.add(Restrictions.eq("modalidade.codigo", mod_codigo));
		// trazer só as turmas que tenham capacidade para inscrever outros lt->menor do que
		//crit.add(Restrictions.gt("capacidade", inscritos));
		return crit.list();
	}
}
