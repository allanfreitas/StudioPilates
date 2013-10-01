package br.com.luxu.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
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
import br.com.luxu.DAO.MensalidadeDAO;
import br.com.luxu.DAO.TurmaAlunoDAO;
import br.com.luxu.classe.Aluno;
import br.com.luxu.classe.Mensalidade;
import br.com.luxu.classe.Turma_Aluno;
import br.com.luxu.util.FacesContextUtil;

@ManagedBean(name="entradaMensalidadesBBean")
@ViewScoped
public class EntradaMensalidadesBackingBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Mensalidade mensalidade = new Mensalidade();
	private Character ativarImprimir;
	private Date dtInicial;
	private Date dtFinal;
	private Character tipoRelatorio;
	private Integer tur_codigo_relatorio;
	private Integer alu_codigo_relatorio;
	
	public String confirmarTurmaRelatorio() {
	    	ativarImprimir = 'S';
		return null;
	}
	
	public void alterarTurmaRelatorio(ActionEvent evt) {
		tur_codigo_relatorio = null;
		alu_codigo_relatorio = null;
		ativarImprimir = null;
		tipoRelatorio = null;
	}
	
	public List<Mensalidade> getMensalidades(){
		InterfaceDAO<Mensalidade> mensalidadeDAO
				= new HibernateDAO<Mensalidade>(Mensalidade.class, FacesContextUtil.getRequestSession());
		List<Mensalidade> lista = mensalidadeDAO.getBeansByExample(mensalidade);
		List<Mensalidade> novaLista = new ArrayList<Mensalidade>();
		for (Mensalidade mensalidade : lista) {
		    String novaTurma =  mensalidade.getTurmaAluno().getTurma().getDia()+
			    "("+mensalidade.getTurmaAluno().getTurma().getHoraInicio()+
			    "-"+mensalidade.getTurmaAluno().getTurma().getHoraFim()+")";
		    mensalidade.getTurmaAluno().getTurma().setDescricao(novaTurma);
		    novaLista.add(mensalidade);
		}
		return novaLista;
	}
	
	public List<SelectItem> getTurmasAlunos() {
		TurmaAlunoDAO turmaAlunoDAO = new TurmaAlunoDAO(FacesContextUtil.getRequestSession());
		List<Turma_Aluno> turmas = turmaAlunoDAO.getTurmas();
		List<SelectItem> selectTurma = new ArrayList<SelectItem>();
		selectTurma.add(new SelectItem(null, "Selecione uma turma.."));
		for (Turma_Aluno turma : turmas) {
			selectTurma.add(new SelectItem(turma.getTurma().getCodigo().toString(),
				turma.getTurma().getDia()+"("+turma.getTurma().getHoraInicio()+
				"-"+turma.getTurma().getHoraFim()+")"+turma.getTurma().getModalidade().getDescricao()));
		}
		return selectTurma;
	}
	
	public List<SelectItem> getAlunosTurmas() {
		TurmaAlunoDAO turmaAlunoDAO = new TurmaAlunoDAO(FacesContextUtil.getRequestSession());
		List<Turma_Aluno> alunos = turmaAlunoDAO.getAlunosTurmas();
		List<SelectItem> selectAluno = new ArrayList<SelectItem>();
		selectAluno.add(new SelectItem(null, "Selecione um aluno.."));
		for (Turma_Aluno aluno : alunos) {
			selectAluno.add(new SelectItem(aluno.getAluno().getCodigo().toString(), aluno.getAluno().getNome()));
		}
		return selectAluno;
	}
	
	public List<SelectItem> getAlunos() {
		InterfaceDAO<Aluno> alunoDAO = new HibernateDAO<Aluno>(Aluno.class, FacesContextUtil.getRequestSession());
		List<Aluno> alunos = alunoDAO.getBeans();
		List<SelectItem> selectAluno = new ArrayList<SelectItem>();
		selectAluno.add(new SelectItem(null, "Selecione um aluno..."));
		for (Aluno aluno : alunos) {
			selectAluno.add(new SelectItem(aluno.getCodigo().toString(), aluno.getNome()));
		}
		return selectAluno;
	}
	
	public void relatorioGeral() throws IOException {
		MensalidadeDAO dao = new MensalidadeDAO(FacesContextUtil.getRequestSession());
		List<Mensalidade> listas = new ArrayList<Mensalidade>();
		if (tipoRelatorio == 'T')
		     listas = dao.getBeanTurma(tur_codigo_relatorio);
		else listas = dao.getBeanAluno(alu_codigo_relatorio);
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listas);
		Map<Object,Object> parameters = new HashMap<Object, Object>();
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext.responseComplete();
			ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
			JasperPrint jasperPrint;
			String logoStudio = FacesContext.getCurrentInstance().getExternalContext().getRealPath("images/Studio.png"); 		
			parameters.put("logoStudio", logoStudio);
			if (tipoRelatorio == 'T')
			     jasperPrint = JasperFillManager.fillReport(scontext.getRealPath("./jasper/relatorioMensalidadePorTurma.jasper"), parameters, ds);
			else jasperPrint = JasperFillManager.fillReport(scontext.getRealPath("./jasper/relatorioMensalidadePorAluno.jasper"), parameters, ds);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
			exporter.exportReport();
			byte[] bytes = baos.toByteArray();
			if (bytes != null && bytes.length > 0) {
			HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "inline; filename=\"relatorioMensalidades.pdf\"");
			response.setContentLength(bytes.length);
			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.write(bytes, 0, bytes.length);
			outputStream.flush();
			outputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param mensalidade the mensalidade to set
	 */
	public void setMensalidade(Mensalidade mensalidade) {
		this.mensalidade = mensalidade;
	}

	/**
	 * @return the mensalidade
	 */
	public Mensalidade getMensalidade() {
		return mensalidade;
	}
	
	/**
	 * @return the dtInicial
	 */
	public Date getDtInicial() {
		return dtInicial;
	}

	/**
	 * @param dtInicial the dtInicial to set
	 */
	public void setDtInicial(Date dtInicial) {
		this.dtInicial = dtInicial;
	}

	/**
	 * @return the dtFinal
	 */
	public Date getDtFinal() {
		return dtFinal;
	}

	/**
	 * @param dtFinal the dtFinal to set
	 */
	public void setDtFinal(Date dtFinal) {
		this.dtFinal = dtFinal;
	}

	/**
	 * @return the tipoRelatorio
	 */
	public Character getTipoRelatorio() {
		return tipoRelatorio;
	}

	/**
	 * @param tipoRelatorio the tipoRelatorio to set
	 */
	public void setTipoRelatorio(Character tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

	/**
	 * @return the tur_codigo_relatorio
	 */
	public Integer getTur_codigo_relatorio() {
		return tur_codigo_relatorio;
	}

	/**
	 * @param tur_codigo_relatorio the tur_codigo_relatorio to set
	 */
	public void setTur_codigo_relatorio(Integer tur_codigo_relatorio) {
		this.tur_codigo_relatorio = tur_codigo_relatorio;
	}

	/**
	 * @return the alu_codigo_relatorio
	 */
	public Integer getAlu_codigo_relatorio() {
		return alu_codigo_relatorio;
	}

	/**
	 * @param alu_codigo_relatorio the alu_codigo_relatorio to set
	 */
	public void setAlu_codigo_relatorio(Integer alu_codigo_relatorio) {
		this.alu_codigo_relatorio = alu_codigo_relatorio;
	}

	public Character getAtivarImprimir() {
	    return ativarImprimir;
	}

	public void setAtivarImprimir(Character ativarImprimir) {
	    this.ativarImprimir = ativarImprimir;
	}
}
