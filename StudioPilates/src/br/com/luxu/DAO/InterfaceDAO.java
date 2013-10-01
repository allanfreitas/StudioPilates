package br.com.luxu.DAO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public interface InterfaceDAO<T> {
	void salvar (T bean);
	void atualizar(T bean);
	void excluir(T bean);
	void salvar(List<T> beans);
	T getBean(Serializable codigo);
	List<T> getBeans();
	List<T> getBeansByExample(T bean);
	List<T> getBeansByIds(String atributo, List<Serializable> codigos);
	boolean getBeanLogin(String login);
	boolean getBeanSenha(String senha);
	List<T> getBeanUsuario(String login);
	List<T> getBeanByDatas(Date dtInicial, Date dtFinal);

}
