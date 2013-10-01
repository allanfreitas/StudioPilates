package br.com.luxu.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIData;
import javax.faces.event.ActionEvent;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import br.com.luxu.DAO.MensalidadeDAO;
import br.com.luxu.DAO.TurmaAlunoDAO;
import br.com.luxu.classe.Mensalidade;
import br.com.luxu.classe.Turma_Aluno;
import br.com.luxu.util.FacesContextUtil;

@ManagedBean
@ViewScoped
public class MensalidadeBean implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private Mensalidade mensalidade = new Mensalidade();
    private Mensalidade tipoMensalidadeSelecionada;
    private UIData tabela;
    private Turma_Aluno turma_Aluno = new Turma_Aluno();

    public void gerar(ActionEvent evt) {
	TurmaAlunoDAO turmaAlunoDAO = new TurmaAlunoDAO(FacesContextUtil.getRequestSession());
	List<Turma_Aluno> listaAluno = new ArrayList<Turma_Aluno>();
	DateTime vencimento = new DateTime();
	LocalDate data = new LocalDate();
	// se a data for do ano atual gera os dados do ano atual caso contrário
	// do próximo ano sempre como base o dia 5 do mês seguinte
	int mes = data.getMonthOfYear()+1;
	listaAluno = turmaAlunoDAO.getGerarLista();
	if (listaAluno.size() > 0) {
			if (vencimento.getMonthOfYear() == 12) 
				 vencimento = data.plusYears(1).withDayOfMonth(10).withMonthOfYear(1).toDateTime(vencimento);
			else vencimento = data.withDayOfMonth(05).withMonthOfYear(mes).toDateTime(vencimento);
			// todos os alunos que estão regularmente matriculados
		    MensalidadeDAO mensalidadeDAO = new MensalidadeDAO(FacesContextUtil.getRequestSession());
		    // envia para a comparar se o código da Turma_Aluno e a data do
		    // vencimento já existem na tabela Mensalidade
		    List<Mensalidade> listaMensalidades = new ArrayList<Mensalidade>();
		    // gera a lista de MENSALIDADES com a data de vencimento específico
		    listaMensalidades = mensalidadeDAO.getBeans();
		    // se NÃO tiver alguém na tabela Mensalidade entra aki
		    if (listaMensalidades.size() <= 0) {
				// tabela MENSALIDADE não tem nada gravado
				for (Turma_Aluno tAluno : listaAluno) {
				    mensalidade = new Mensalidade();
				    mensalidade.setTurmaAluno(tAluno);
				    mensalidade.setValor(tAluno.getValor());
				    mensalidade.setData(new Date());
				    mensalidade.setDesconto(tAluno.getAluno().getDesconto());
				    mensalidade.setVencimento(vencimento);
				    mensalidadeDAO.salvar(mensalidade);
				}
		    }
		    else
	    	// se tiver alguém na tabela Mensalidade entra aki
		    { 
				Boolean flag = false; // NÃO tem na tabela MENSALIDADE
				String dataSistema = vencimento.getDayOfMonth() + "/"+ vencimento.getMonthOfYear() + "/"+ vencimento.getYear();
				for (Turma_Aluno tAluno : listaAluno) {
				    for (Mensalidade mensalidade : listaMensalidades) {
						String dataBanco = mensalidade.getVencimento().getDayOfMonth()+ "/"+ mensalidade.getVencimento().getMonthOfYear()+ "/" + mensalidade.getVencimento().getYear();
						if (tAluno.getCodigo() == mensalidade.getTurmaAluno().getCodigo() && dataSistema.equals(dataBanco)) {
						    flag = true; // já tem na tabela MENSALIDADE
						    break;
						}
				    }
				    if (!flag) // NÃO tem na tabela MENSALIDADE
				    {
						mensalidade = new Mensalidade();
						mensalidade.setTurmaAluno(tAluno);
						mensalidade.setValor(tAluno.getValor());
						mensalidade.setData(new Date());
						mensalidade.setDesconto(tAluno.getAluno().getDesconto());
						mensalidade.setVencimento(vencimento);
						mensalidadeDAO.salvar(mensalidade);
				    }
				    	flag = false;
				}
		    }
		}
		else
		{ 
			System.out.println(">>>>>>>>>>>>>>>>>>>>LISTA VAZIA !!!!!<<<<<<<<<<<<<<<<<<<");
			FacesContextUtil.setMensagemErro("Lista Vazia!!");
		}
    }

    public void atualizar(ActionEvent evt) {
		MensalidadeDAO mensalidadeDAO = new MensalidadeDAO(FacesContextUtil.getRequestSession());
		Double valor = 0.0, acrescimo = 0.0, desconto = 0.0;
		if (mensalidade.getValor() != null)
		    valor = mensalidade.getValor();
		if (mensalidade.getAcrescimo() != null)
		    acrescimo = mensalidade.getAcrescimo();
		if (mensalidade.getDesconto() != null)
		    desconto = mensalidade.getDesconto();
		mensalidade.setValor(valor + acrescimo - desconto);
		try {
		    mensalidadeDAO.atualizar(mensalidade);
		    FacesContextUtil.setMensagemSucesso("Registro ALTERADO com sucesso!");
		} finally {
		    mensalidade = new Mensalidade();
		}
    }

    public void excluir(ActionEvent evt) {
		MensalidadeDAO mensalidadeDAO = new MensalidadeDAO(FacesContextUtil.getRequestSession());
		try {
		    mensalidadeDAO.excluir(tipoMensalidadeSelecionada);
			FacesContextUtil.setMensagemSucesso("Registro EXCLUÍDO com sucesso!");
		} finally {
		    tipoMensalidadeSelecionada = new Mensalidade();
		}
    }

    public String alterar() {
		mensalidade = (Mensalidade) getTabela().getRowData();
		return null;
    }

    public String cancelar() {
		mensalidade = new Mensalidade();
		return null;
    }

    public void novo(ActionEvent evt) {
		mensalidade = new Mensalidade();
    }

    public Mensalidade getMensalidade() {
		return mensalidade;
    }

    public void setMensalidade(Mensalidade mensalidade) {
		this.mensalidade = mensalidade;
    }

    public UIData getTabela() {
    	return tabela;
    }

    public void setTabela(UIData tabela) {
    	this.tabela = tabela;
    }

    public Turma_Aluno getTurma_Aluno() {
    	return turma_Aluno;
    }

    public void setTurma_Aluno(Turma_Aluno turma_Aluno) {
    	this.turma_Aluno = turma_Aluno;
    }

    public Mensalidade getTipoMensalidadeSelecionada() {
    	return tipoMensalidadeSelecionada;
    }

    public void setTipoMensalidadeSelecionada(Mensalidade tipoMensalidadeSelecionada) {
    	this.tipoMensalidadeSelecionada = tipoMensalidadeSelecionada;
    }
}
