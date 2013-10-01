package br.com.luxu.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;

import br.com.luxu.DAO.FuncionarioDAO;
import br.com.luxu.DAO.HibernateDAO;
import br.com.luxu.DAO.InterfaceDAO;
import br.com.luxu.classe.Funcionario;
import br.com.luxu.classe.Modalidade;
import br.com.luxu.classe.Turma;
import br.com.luxu.util.FacesContextUtil;

@ManagedBean
@ViewScoped
public class TurmaBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Turma turma = new Turma();
	private Turma tipoTurmaSelecionada;
	private Integer codigoProfessor;
	private Integer codigoModalidade;
	private UIData tabela;
	@SuppressWarnings("rawtypes")
	private DataModel model;
	
	public void salvar(){
		InterfaceDAO<Turma> turmaDAO = new HibernateDAO<Turma>(Turma.class, FacesContextUtil.getRequestSession());
		Modalidade modalidade = new Modalidade();
		modalidade.setCodigo(codigoModalidade);
		turma.setModalidade(modalidade);
		Funcionario funcionario = new Funcionario();
		funcionario.setCodigo(codigoProfessor);
		turma.setFuncionario(funcionario);
		turma.setInscritos(0);
		turma.setDescricao(turma.getDia()+"("+turma.getHoraInicio()+"-"+turma.getHoraFim()+")");
		try {
			turmaDAO.salvar(turma);
			model = null;
			FacesContextUtil.setMensagemSucesso("Registro CADASTRADO com sucesso!");
		} finally{
			codigoModalidade = null;
			codigoProfessor = null;
			turma = new Turma();
		}
	}

	public void atualizar(ActionEvent evt){
		InterfaceDAO<Turma> turmaDAO = new HibernateDAO<Turma>(Turma.class, FacesContextUtil.getRequestSession());
		Modalidade modalidade = new Modalidade();
		modalidade.setCodigo(codigoModalidade);
		turma.setModalidade(modalidade);
		Funcionario funcionario = new Funcionario();
		funcionario.setCodigo(codigoProfessor);
		turma.setFuncionario(funcionario);
		turma.setDescricao(turma.getDia()+"("+turma.getHoraInicio()+"-"+turma.getHoraFim()+")");
		try {
			turmaDAO.atualizar(turma);
			model = null;
			FacesContextUtil.setMensagemSucesso("Registro ALTERADO com sucesso!");
		} finally {
			turma = new Turma();
			codigoModalidade = null;
			codigoProfessor = null;
		}
	}
	
	public void excluir(ActionEvent evt){
		InterfaceDAO<Turma> turmaDAO = new HibernateDAO<Turma>(Turma.class, FacesContextUtil.getRequestSession());
		try {
		    turmaDAO.excluir(tipoTurmaSelecionada);
			model = null;
		    FacesContextUtil.setMensagemSucesso("Registro EXCLUÍDO com sucesso!");
		} finally {
		    tipoTurmaSelecionada = new Turma();
		}
	}

	public String alterar(){
		codigoModalidade = null;
		codigoProfessor = null;
		turma = (Turma) getTabela().getRowData();
		codigoModalidade = turma.getModalidade().getCodigo();
		codigoProfessor = turma.getFuncionario().getCodigo();
		return null;
	}
	
	public void novo(ActionEvent evt){
		turma = new Turma();
		codigoModalidade = null;
		codigoProfessor = null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DataModel getTurmas(){
			if(this.model == null){
				InterfaceDAO<Turma> turmaDAO = new HibernateDAO<Turma>(Turma.class, FacesContextUtil.getRequestSession());
				model = new ListDataModel(turmaDAO.getBeans());
			}
			return model;
	}

	public List<SelectItem> getProfessores(){
		FuncionarioDAO funcionarioDAO = new FuncionarioDAO(FacesContextUtil.getRequestSession(), new HibernateDAO<Funcionario>(Funcionario.class, FacesContextUtil.getRequestSession()));
		List<Funcionario> funcionario = funcionarioDAO.getProfFunc();
		List<SelectItem> selectProfessores = new ArrayList<SelectItem>();
		selectProfessores.add(new SelectItem(null, "Selecione um professor..."));
		for (Funcionario func : funcionario) {
			selectProfessores.add(new SelectItem(func.getCodigo().toString(), func.getNome()));
		}
		return selectProfessores;
	}
	
	public List<SelectItem> getModalidades(){
		InterfaceDAO<Modalidade> modalidadeDAO = 
			new HibernateDAO<Modalidade>(Modalidade.class, FacesContextUtil.getRequestSession());
		List<Modalidade> modalidade = modalidadeDAO.getBeans();
		List<SelectItem> selectModalidades = new ArrayList<SelectItem>();
		selectModalidades.add(new SelectItem(null, "Selecione uma modalidade..."));
		for (Modalidade modal : modalidade) {
			selectModalidades.add(new SelectItem(modal.getCodigo().toString(), modal.getDescricao()));
		}
		return selectModalidades;
	}
	
	public void relatorioGeral() throws IOException {
		InterfaceDAO<Turma> dao = new HibernateDAO<Turma>(Turma.class, FacesContextUtil.getRequestSession());
		List<Turma> listas = new ArrayList<Turma>();
		listas = dao.getBeans();
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listas);
		Map<Object,Object> parameters = new HashMap<Object, Object>();
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext.responseComplete();
			ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
			String logoStudio = FacesContext.getCurrentInstance().getExternalContext().getRealPath("images/Studio.png"); 		
			parameters.put("logoStudio", logoStudio);  
			JasperPrint jasperPrint = JasperFillManager.fillReport(scontext.getRealPath("jasper/relatorioTurmas.jasper"), parameters, ds);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
			exporter.exportReport();
			byte[] bytes = baos.toByteArray();
			if (bytes != null && bytes.length > 0) {
			HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "inline; filename=\"relatorioTurmas.pdf\"");
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

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public Integer getCodigoProfessor() {
		return codigoProfessor;
	}

	public void setCodigoProfessor(Integer codigoProfessor) {
		this.codigoProfessor = codigoProfessor;
	}

	public Integer getCodigoModalidade() {
		return codigoModalidade;
	}

	public void setCodigoModalidade(Integer codigoModalidade) {
		this.codigoModalidade = codigoModalidade;
	}

	public void setTabela(UIData tabela) {
		this.tabela = tabela;
	}

	public UIData getTabela() {
		return tabela;
	}

	public Turma getTipoTurmaSelecionada() {
	    return tipoTurmaSelecionada;
	}

	public void setTipoTurmaSelecionada(Turma tipoTurmaSelecionada) {
	    this.tipoTurmaSelecionada = tipoTurmaSelecionada;
	}

	@SuppressWarnings("rawtypes")
	public DataModel getModel() {
		return model;
	}

	@SuppressWarnings("rawtypes")
	public void setModel(DataModel model) {
		this.model = model;
	}

}
