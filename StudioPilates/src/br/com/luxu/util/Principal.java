package br.com.luxu.util;

import org.hibernate.Session;

public class Principal {

	public static void main(String[] args) {
		Session  sessao = null;
		try {
			sessao = HibernateUtil.getSession();
			System.out.println("Conectou !");
		}finally{
			sessao.close();
			System.out.println("Fechou conexão!");
		}
		
	}
}
