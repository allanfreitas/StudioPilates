package br.com.luxu.util;

public class Teste {
	
	public static void main (String args[]){
		
		
		//frequencia = entityManager.find(Frequencia.class,hj);
		//Integer nro;
		//Query query;
		
		//query = entityManager.createNativeQuery("select * from Frequencia");
		//nro = (Integer) query.getSingleResult();
		//System.out.println(query.getResultList());
		
		
		/*
		Cidade cidade = new Cidade();
		//cidade.setDescricao("Presidente Prudente");
		//cidade.setEstado("SP");
		
		Cargo cargo = new Cargo();
		cargo = entityManager.find(Cargo.class,1);
		
		cidade = entityManager.find(Cidade.class,1);
		//cargo.setDescricao("Professor");
		
		Funcionario funcionario = new Funcionario();
		funcionario.setNome("Luciano");
		funcionario.setCargo(cargo);
		funcionario.setCidade(cidade);
		
		//cidade = entityManager.find(Cidade.class,2);
		//System.out.println("Codigo.:"+cidade.getCodigo()+" - "+cidade.getDescricao()+"/"+cidade.getEstado());

		entityManager.getTransaction().begin();
		entityManager.persist(funcionario);
		entityManager.getTransaction().commit();
		entityManager.close();
		*/
		}
}


/*
EntityManagerFactory emf = Persistence.createEntityManagerFactory("copa-do-mundo");
EntityManager em = emf.createEntityManager();

Jogador jogador = new Jogador();
jogador.setNome("Luciano");
Jogador.setNascimento(Calendar.getInstance());
jogador.setAltura(1.86);
jogador.setPosicao("zagueiro");
	
em.getTransaction().begin();
em.persist(jogador);
em.getTransaction().commit();
*/