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
import br.com.luxu.classe.Modalidade;
import br.com.luxu.util.FacesContextUtil;

@ManagedBean
@ViewScoped
public class ModalidadeBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Modalidade modalidade = new Modalidade();
	private Modalidade tipoModalidadeSelecionada;
	private UIData tabela;
	private static final String teste = "Luciano";
	@SuppressWarnings("rawtypes")
	private DataModel model;
	
	public void salvar(ActionEvent evt){
		InterfaceDAO<Modalidade> modalidadeDAO = new HibernateDAO<Modalidade>(Modalidade.class, FacesContextUtil.getRequestSession());
		try {
			modalidadeDAO.salvar(modalidade);
			FacesContextUtil.setMensagemSucesso("Registro CADASTRADO com sucesso!");
		} finally{
			model = null;
			modalidade = new Modalidade();
		}
	}

	public void atualizar(ActionEvent evt){
		InterfaceDAO<Modalidade> modalidadeDAO = new HibernateDAO<Modalidade>(Modalidade.class, FacesContextUtil.getRequestSession());
		try {
			modalidadeDAO.atualizar(modalidade);
			FacesContextUtil.setMensagemSucesso("Registro ALTERADO com sucesso!");
		} finally {
			model = null;
			modalidade = new Modalidade();
		}
	}
	
	public void excluir(ActionEvent evt){
		InterfaceDAO<Modalidade> modalidadeDAO = new HibernateDAO<Modalidade>(Modalidade.class, FacesContextUtil.getRequestSession());
		try {
            modalidadeDAO.excluir(tipoModalidadeSelecionada);
		    FacesContextUtil.setMensagemSucesso("Registro EXCLUÍDO com sucesso!");
		} finally {
			model = null;
		    tipoModalidadeSelecionada = new Modalidade();
		}
	}

	public String alterar(){
		modalidade = (Modalidade) getTabela().getRowData();
		return null;
	}
	
	public void novo(ActionEvent evt){
		modalidade = new Modalidade();
	}
	
	public static String teste() {
	    return teste;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DataModel getModalidades(){
		if(this.model == null){
			InterfaceDAO<Modalidade> modalidadeDAO = new HibernateDAO<Modalidade>(Modalidade.class, FacesContextUtil.getRequestSession());
			model = new ListDataModel(modalidadeDAO.getBeans());
		}
		return model;
	}
	
	public void relatorioGeral() throws IOException {
		InterfaceDAO<Modalidade> dao = new HibernateDAO<Modalidade>(Modalidade.class, FacesContextUtil.getRequestSession());
		List<Modalidade> listas = new ArrayList<Modalidade>();
		listas = dao.getBeans();
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listas);
		Map<Object,Object> parameters = new HashMap<Object, Object>();
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext.responseComplete();
			ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
			String logoStudio = FacesContext.getCurrentInstance().getExternalContext().getRealPath("images/Studio.png"); 		
			parameters.put("logoStudio", logoStudio);  
			JasperPrint jasperPrint = JasperFillManager.fillReport(scontext.getRealPath("jasper/relatorioModalidade.jasper"), parameters, ds);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
			exporter.exportReport();
			byte[] bytes = baos.toByteArray();
			if (bytes != null && bytes.length > 0) {
			HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "inline; filename=\"relatorioModalidade.pdf\"");
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

	public Modalidade getModalidade() {
		return modalidade;
	}

	public void setModalidade(Modalidade modalidade) {
		this.modalidade = modalidade;
	}

	public void setTabela(UIData tabela) {
		this.tabela = tabela;
	}

	public UIData getTabela() {
		return tabela;
	}
	
	public Modalidade getTipoModalidadeSelecionada() {
	    return tipoModalidadeSelecionada;
	}

	public void setTipoModalidadeSelecionada(Modalidade tipoModalidadeSelecionada) {
	    this.tipoModalidadeSelecionada = tipoModalidadeSelecionada;
	}

}
