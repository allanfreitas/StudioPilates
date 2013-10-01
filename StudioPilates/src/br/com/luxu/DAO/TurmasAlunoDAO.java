package br.com.luxu.DAO;

import org.hibernate.Session;
import br.com.luxu.classe.Turma_Aluno;

public class TurmasAlunoDAO extends HibernateDAO<Turma_Aluno> {

    public TurmasAlunoDAO(Session session) {
	super(Turma_Aluno.class, session);
    }

}
