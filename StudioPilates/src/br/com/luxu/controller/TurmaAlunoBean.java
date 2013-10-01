package br.com.luxu.controller;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;

import br.com.luxu.DAO.HibernateDAO;
import br.com.luxu.DAO.InterfaceDAO;
import br.com.luxu.DAO.TurmaAlunoDAO;
import br.com.luxu.classe.Aluno;
import br.com.luxu.classe.Turma;
import br.com.luxu.classe.Turma_Aluno;
import br.com.luxu.util.FacesContextUtil;

@ManagedBean
@ViewScoped
public class TurmaAlunoBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Turma_Aluno turmaAluno = new Turma_Aluno();
	private Turma_Aluno tipoTurmaAlunoSelecionado;
	private Turma turma = new Turma();
	private Aluno aluno = new Aluno();
	private Integer alu_codigo;
	private Integer tur_codigo;
	private Integer tur_codigo_relatorio;
	private Integer capacidadeOK = null;
	private UIData tabela;

	public String salvar() {
		InterfaceDAO<Turma_Aluno> turmaAlunoDAO = new HibernateDAO<Turma_Aluno>(Turma_Aluno.class, FacesContextUtil.getRequestSession());
		InterfaceDAO<Turma> turmaDAO = new HibernateDAO<Turma>(Turma.class, FacesContextUtil.getRequestSession());
		aluno.setCodigo(alu_codigo);
		// seta na tabela TURMA no campo INSCRITOS mais um aluno
		turma.setInscritos(turma.getInscritos()+1);
		turmaAluno.setAluno(aluno);
		turmaAluno.setTurma(turma);
		try {
			turmaAlunoDAO.salvar(turmaAluno);
			turmaDAO.atualizar(turma);
			FacesContextUtil.setMensagemSucesso("Registro CADASTRADO com sucesso!");
		} finally {
			alu_codigo = null;
			turmaAluno = new Turma_Aluno();
			turmaAluno.setInicio(new Date());
			turmaAluno.setValor(turma.getModalidade().getPreco());
		}
		int capacidade = turma.getCapacidade();
		int inscritos = turma.getInscritos();
		if(inscritos >= capacidade){
		    capacidadeOK = 1;
		    FacesContextUtil.setMensagemErro("Capacidade limite atingida!");
		}
		return null;
	}

	public void excluir(ActionEvent evt) {
		InterfaceDAO<Turma_Aluno> turmaAlunoDAO = new HibernateDAO<Turma_Aluno>(Turma_Aluno.class, FacesContextUtil.getRequestSession());
		InterfaceDAO<Turma> turmaDAO = new HibernateDAO<Turma>(Turma.class, FacesContextUtil.getRequestSession());
		turma.setInscritos(turma.getInscritos()-1);
		capacidadeOK = null;
		try {
			turmaAlunoDAO.excluir(tipoTurmaAlunoSelecionado);
			turmaDAO.atualizar(turma);
			FacesContextUtil.setMensagemSucesso("Registro EXCLUÍDO com sucesso!");
		} finally {
			tipoTurmaAlunoSelecionado = new Turma_Aluno();
		}
	}

	public String atualizar(ActionEvent evt) {
		// InterfaceDAO<Turma_Aluno> alunoDAO = new
		// HibernateDAO<Turma_Aluno>(Turma_Aluno.class,
		// FacesContextUtil.getRequestSession());
		// alunoDAO.atualizar(getTurmaAluno());
		turmaAluno = new Turma_Aluno();
		return null;
	}

	public String alterar() {
		// turmaAluno = (Turma_Aluno) getTabela().getRowData();
		return null;
	}

	public String confirmarTurma() {
		InterfaceDAO<Turma> turmaDAO = new HibernateDAO<Turma>(Turma.class,FacesContextUtil.getRequestSession());
		turma = turmaDAO.getBean(tur_codigo);
		int capacidade = turma.getCapacidade();
		int inscritos = turma.getInscritos();
		if(inscritos >= capacidade){
		    capacidadeOK = 1;
		    FacesContextUtil.setMensagemErro("Capacidade limite atingida");
		}
		else{
		    turmaAluno.setInicio(new Date());
		    turmaAluno.setValor(turma.getModalidade().getPreco());
		}
		return null;
	}
	
	public List<SelectItem> getAlunos() {
		List<SelectItem> selectAluno = new ArrayList<SelectItem>();
		selectAluno.add(new SelectItem(null, "Selecione um aluno..."));
		TurmaAlunoDAO turmaAlunosDAO = new TurmaAlunoDAO(FacesContextUtil.getRequestSession());
		List<Aluno> listarAlunos = new ArrayList<Aluno>(); 
		if (turmaAlunosDAO.getTurmaAlunos(tur_codigo).size() <= 0 )
			//só traz alunos que estejam ATIVOS 
			listarAlunos = turmaAlunosDAO.getAlunos();
		else listarAlunos = turmaAlunosDAO.getAlunos(tur_codigo);
		for(Aluno aluno : listarAlunos)
			selectAluno.add(new SelectItem(aluno.getCodigo().toString(), aluno.getNome()));
		return selectAluno;
	}

	public String confirmarAluno() {
		InterfaceDAO<Aluno> alunoDAO = new HibernateDAO<Aluno>(Aluno.class,FacesContextUtil.getRequestSession());
		this.aluno = alunoDAO.getBean(Integer.valueOf(alu_codigo));
		return null;
	}
	
	public String confirmarTurmaRelatorio() {
		InterfaceDAO<Turma> turmaDAO = new HibernateDAO<Turma>(Turma.class,FacesContextUtil.getRequestSession());
		turma = turmaDAO.getBean(tur_codigo_relatorio);
		return null;
	}

	public List<Turma_Aluno> getTurmaAlunos() {
		TurmaAlunoDAO turmaAlunosDAO = new TurmaAlunoDAO(FacesContextUtil.getRequestSession());
		return turmaAlunosDAO.getTurmaAlunos(tur_codigo);
	}

	public String alterarTurma(ActionEvent evt) {
		tur_codigo = null;
		alu_codigo = null;
		turma = new Turma();
		turmaAluno = new Turma_Aluno();
		capacidadeOK = null;
		return null;
	}

	public void alterarTurmaRelatorio(ActionEvent evt) {
		this.tur_codigo_relatorio = null;
		turma = new Turma();
	}

	public String novo(ActionEvent evt) {
		turmaAluno = new Turma_Aluno();
		return null;
	}

	public String carregarAlterar() {
		// this.set((Turma_Aluno)
		// FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get(tabela.getVar()));
		return null;
	}

	public List<SelectItem> getTurmas() {
		TurmaAlunoDAO turmaDAO = new TurmaAlunoDAO(FacesContextUtil.getRequestSession());
		// turmas q estejam ATIVAS 
		List<Turma> turmas = turmaDAO.getBeansByTurmas();
		List<SelectItem> selectTurma = new ArrayList<SelectItem>();
		selectTurma.add(new SelectItem(null, "Selecione uma turma.."));
		for (Turma turma : turmas) {
			selectTurma.add(new SelectItem(turma.getCodigo().toString(),
					turma.getDia()+"("+turma.getHoraInicio()+"-"+turma.getHoraFim()+")"+turma.getModalidade().getDescricao()));
		}
		return selectTurma;
	}
	
	public void relatorioGeral(ActionEvent evt) {
		TurmaAlunoDAO turmaAlunosDAO = new TurmaAlunoDAO(FacesContextUtil.getRequestSession());
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(turmaAlunosDAO.getTurmaAlunos(tur_codigo_relatorio));
		Map<Object, Object> parameters = new HashMap<Object, Object>();
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext.responseComplete();
			ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
			String logoStudio = FacesContext.getCurrentInstance().getExternalContext().getRealPath("images/Studio.png"); 		
			parameters.put("logoStudio", logoStudio);  
			JasperPrint jasperPrint = JasperFillManager.fillReport(scontext.getRealPath("./jasper/relatorioTurmaAluno.jasper"), parameters, ds);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
			exporter.exportReport();
			byte[] bytes = baos.toByteArray();
			if (bytes != null && bytes.length > 0) {
				HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
				response.setContentType("application/pdf");
				response.setHeader("Content-disposition","inline; filename=\"relatorioTurmaAlunos.pdf\"");
				response.setContentLength(bytes.length);
				ServletOutputStream outputStream = response.getOutputStream();
				outputStream.write(bytes, 0, bytes.length);
				outputStream.flush();
				outputStream.close();
				this.tur_codigo_relatorio = null;
				turma = new Turma();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setAlu_codigo(Integer alu_codigo) {
		this.alu_codigo = alu_codigo;
	}

	public Integer getAlu_codigo() {
		return alu_codigo;
	}

	public Turma_Aluno getTurmaAluno() {
		return turmaAluno;
	}

	public void setTurmaAluno(Turma_Aluno turmaAluno) {
		this.turmaAluno = turmaAluno;
	}

	public Turma getTurma() {
		return turma;
	}

	public void setTabela(UIData tabela) {
		this.tabela = tabela;
	}

	public UIData getTabela() {
		return tabela;
	}

	public Integer getTur_codigo() {
		return tur_codigo;
	}

	public void setTur_codigo(Integer tur_codigo) {
		this.tur_codigo = tur_codigo;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setTur_codigo_relatorio(Integer tur_codigo_relatorio) {
		this.tur_codigo_relatorio = tur_codigo_relatorio;
	}

	public Integer getTur_codigo_relatorio() {
		return tur_codigo_relatorio;
	}

	public Turma_Aluno getTipoTurmaAlunoSelecionado() {
	    return tipoTurmaAlunoSelecionado;
	}

	public void setTipoTurmaAlunoSelecionado(
		Turma_Aluno tipoTurmaAlunoSelecionado) {
	    this.tipoTurmaAlunoSelecionado = tipoTurmaAlunoSelecionado;
	}

	public Integer getCapacidadeOK() {
	    return capacidadeOK;
	}

	public void setCapacidadeOK(Integer capacidadeOK) {
	    this.capacidadeOK = capacidadeOK;
	}

}
