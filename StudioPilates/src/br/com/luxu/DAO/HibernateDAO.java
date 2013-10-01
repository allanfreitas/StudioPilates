package br.com.luxu.DAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class HibernateDAO<T> implements InterfaceDAO<T> {

	private Class<T> classe;
	protected Session session;

	public HibernateDAO(Class<T> classe, Session session) {
		super();
		this.classe = classe;
		this.session = session;
	}

	@Override
	public void salvar(T bean) {
		session.flush();
		session.clear();
		session.save(bean);
	}

	@Override
	public void atualizar(T bean) {
		session.flush();
		session.clear();
		session.update(bean);
	}

	@Override
	public void excluir(T bean) {
		session.flush();
		session.clear();
		session.delete(bean);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getBeans() {
		Criteria crit = session.createCriteria(classe);
		crit.setCacheable(true);
		List<T> bean = (List<T>) crit.list();
		session.flush();
		session.clear();
		return bean;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getBeansByExample(T bean) {
		Example example = getExample(bean);
		session.flush();
		session.clear();
		return session.createCriteria(classe).add(example).list();
	}

	protected Example getExample(T bean) {
		Example example = Example.create(bean);
		example.enableLike(MatchMode.START);
		example.ignoreCase();
		example.excludeZeroes();
		return example;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getBean(Serializable codigo) {
		T bean = (T) session.get(classe, codigo);
		return bean;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getBeansByIds(String atributo, List<Serializable> codigos) {
		try {
			if (codigos.size() == 1) {
				List<T> resultado = new ArrayList<T>();
				resultado.add(getBean(codigos.get(0)));
				return resultado;
			} else if (codigos.size() > 1) {
				Criteria crit = session.createCriteria(classe);
				crit.add(Restrictions.in("codigo", codigos));
				return crit.list();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<T>();
	}

	@Override
	public void salvar(List<T> beans) {
		for (int i = 0; i < beans.size(); i++) {
			session.save(beans.get(i));
			if (i % 20 == 0 || i >= beans.size() - 1) {
				session.flush();
				session.clear();
			}
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean getBeanLogin(String login) {
		Criteria crit = session.createCriteria(classe);

		// retorna uma lista onde CONTENHA o login
		crit.add(Restrictions.eq("login", login));

		List lista = crit.list();
		if (lista.isEmpty())
		    return false;
		return true;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean getBeanSenha(String senha) {
		Criteria crit = session.createCriteria(classe);

		// retorna uma lista onde CONTENHA a senha
		crit.add(Restrictions.eq("senha", senha));

		List lista = crit.list();
		if (lista.isEmpty())
		    return false;
		return true;
	}

    @SuppressWarnings("unchecked")
	@Override
    public List<T> getBeanUsuario(String login) {
		Criteria crit = session.createCriteria(classe);
		// retorna uma lista onde CONTENHA o login
		crit.add(Restrictions.eq("login", login));
		return crit.list();
    }

    @SuppressWarnings("unchecked")
	@Override
    public List<T> getBeanByDatas(Date dtInicial, Date dtFinal) {
		Criteria crit = session.createCriteria(classe);
		// retorna uma lista dos intervalos entre as datas(INICIAL-FINAL)
		crit.add(Restrictions.between("data", dtInicial, dtFinal));
		// retorna uma lista dos alunos que faltaram na data especificada acima
		crit.add(Restrictions.eq("presenca", "Não"));
		return crit.list();
    }
}
