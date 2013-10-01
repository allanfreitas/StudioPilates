package br.com.luxu.DAO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import br.com.luxu.classe.Cargo;
import br.com.luxu.classe.Funcionario;
import br.com.luxu.classe.Turma;

public class FuncionarioDAO implements InterfaceDAO<Funcionario> {

	private InterfaceDAO<Funcionario> profFunc;
	Session session;
	
	public FuncionarioDAO(Session session, InterfaceDAO<Funcionario> dao) {
		this.profFunc = dao;
		this.session = session;
	}
	
	@SuppressWarnings("unchecked")
	public List<Turma> getTurmas(Integer fun_codigo){
		Criteria crit = session.createCriteria(Turma.class);
		crit.add(Restrictions.eq("funcionario.codigo",fun_codigo));
		return crit.list();
	}

	@SuppressWarnings("unchecked")
	public List<Funcionario> getProfFunc(){
		Criteria cargos = session.createCriteria(Cargo.class);
		cargos.add(Restrictions.ilike("descricao", "%rofes%"));
		List<Cargo> cargo = cargos.list(); 
		Criteria crit = session.createCriteria(Funcionario.class);
		crit.add(Restrictions.eq("cargo.codigo",cargo.get(0).getCodigo()));
		return crit.list();
	}

	@Override
	public void salvar(Funcionario bean) throws ConstraintViolationException {
		//profFunc.salvar(bean);
        	session.flush();
        	session.clear();
        	session.save(bean); 
        	session.getTransaction().commit();
	}
	
	@Override
	public void atualizar(Funcionario bean) throws ConstraintViolationException {
		//profFunc.atualizar(bean);
        	session.flush();
        	session.clear();
        	session.update(bean); 
        	session.getTransaction().commit();
	}
	
        @Override
        public void excluir(Funcionario bean) throws ConstraintViolationException {
        	session.flush();
        	session.clear();
        	session.delete(bean);
        	session.getTransaction().commit();
        }

	@Override
	public Funcionario getBean(Serializable codigo) {
		return profFunc.getBean(codigo);
	}
	
	@Override
	public List<Funcionario> getBeans() {
		return profFunc.getBeans();
	}
	
	@Override
	public List<Funcionario> getBeansByExample(Funcionario bean) {
		return profFunc.getBeansByExample(bean);
	}
	
	@Override
	public List<Funcionario> getBeansByIds(String atributo,
			List<Serializable> codigos) {
		return profFunc.getBeansByIds(atributo, codigos);
	}
	
	@Override
	public void salvar(List<Funcionario> beans) {
		profFunc.getBeans();
	}

	public List<Funcionario> getBeanAlunos(Integer tur_codigo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getBeanLogin(String login) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getBeanSenha(String senha) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Funcionario> getBeanUsuario(String login) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Funcionario> getBeanByDatas(Date dtInicial, Date dtFinal) {
		// TODO Auto-generated method stub
		return null;
	}

}
