package br.com.luxu.DAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import br.com.luxu.classe.Aluno;
import br.com.luxu.classe.Turma;
import br.com.luxu.classe.Turma_Aluno;

public class TurmaAlunoDAO extends HibernateDAO<Turma_Aluno> {

	public TurmaAlunoDAO(Session session) {
		super(Turma_Aluno.class, session);
	}

	@SuppressWarnings("unchecked")
	public List<Turma_Aluno> getTurmaAlunos(Integer codigoTurma){
		Criteria crit = session.createCriteria(Turma_Aluno.class);
		crit.add(Restrictions.eq("turma.codigo", codigoTurma));
		return crit.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Turma_Aluno> getTurmaAlunosFrequencia(Integer codigoTurma){
		Criteria crit = session.createCriteria(Turma_Aluno.class);
		crit.add(Restrictions.not(Restrictions.like("turma.codigo", codigoTurma)));
		return crit.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Aluno> getAlunos(Integer codigoTurma){
		Criteria crit = session.createCriteria(Turma_Aluno.class);
		crit.add(Restrictions.eq("turma.codigo", codigoTurma));
		List<Turma_Aluno> lista = new ArrayList<Turma_Aluno>();
		lista = crit.list();
		List<Serializable> ids = new ArrayList<Serializable>();
		for (Turma_Aluno i : lista ) {
			ids.add(i.getAluno().getCodigo());
		}
		return getBeansByIds(ids);
	}

	@SuppressWarnings("unchecked")
	public List<Aluno> getAlunos(){
		Criteria crit = session.createCriteria(Aluno.class);
		// se o campo ativo da tabela aluno estiver com "S"
		crit.add(Restrictions.eq("ativo", "S"));
		return crit.list(); 
	}

	@SuppressWarnings("unchecked")
	public List<Aluno> getAlunosFrequencia(Integer codigoTurma){
		Criteria crit = session.createCriteria(Turma_Aluno.class);
		crit.add(Restrictions.like("turma.codigo", codigoTurma));
		List<Turma_Aluno> lista = new ArrayList<Turma_Aluno>();
		lista = crit.list();
		List<Serializable> ids = new ArrayList<Serializable>();
		for (Turma_Aluno i : lista ) {
			ids.add(i.getAluno().getCodigo());
		}
		return getBeansByIds(ids);
	}

	@SuppressWarnings("unchecked")
	public List<Aluno> getBeansByIds(List<Serializable> codigos) {
		try {
        		Criteria crit = session.createCriteria(Aluno.class);
        		crit.add(Restrictions.not(Restrictions.in("codigo", codigos)));
        		// se o campo ativo da tabela aluno estiver com "S"
        		crit.add(Restrictions.eq("ativo", "S"));
        		return crit.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Aluno>();
	}

	@SuppressWarnings("unchecked")
	public List<Turma_Aluno> getGerarLista(){
		Criteria crit = session.createCriteria(Turma_Aluno.class);
		// se tiver aluno cadastrado(campo 'fim' estiver nulo) em alguma turma
		crit.add(Restrictions.isNull("fim"));
		return crit.list();
	}

	protected Example getExample() {
		Example example = Example.create(Turma_Aluno.class);
		example.enableLike(MatchMode.START);
		example.ignoreCase();
		example.excludeZeroes();
		return example;
	}

	@SuppressWarnings("unchecked")
	public List<Aluno> getBeanAluno(Serializable tur_codigo) {
		Criteria crit = session.createCriteria(Turma_Aluno.class);
		List<Turma_Aluno> codigos = new ArrayList<Turma_Aluno>();
		codigos.add((Turma_Aluno) tur_codigo);
		crit.add(Restrictions.in("codigo", codigos));
		return crit.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Turma_Aluno> getTurmas() {
		Criteria crit = session.createCriteria(Turma_Aluno.class);
		List<Turma_Aluno> result = crit.list();
		HashSet<Turma_Aluno> hs = new HashSet<Turma_Aluno>();
		hs.addAll(result);
		result.clear();
		result.addAll(hs);
		crit.setCacheable(true);
		session.flush();
		session.clear();		
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Turma_Aluno> getAlunosTurmas() {
		Criteria crit = session.createCriteria(Turma_Aluno.class);
		List<Turma_Aluno> result = crit.list();
		HashSet<Turma_Aluno> hs = new HashSet<Turma_Aluno>();
		hs.addAll(result);
		result.clear();
		result.addAll(hs);
		crit.setCacheable(true);
		session.flush();
		session.clear();		
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Turma> getBeansByTurmas() {
		Criteria crit = session.createCriteria(Turma.class);
		crit.add(Restrictions.eq("status", 'S'));
		return crit.list();
	}

}
