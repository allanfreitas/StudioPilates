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

import br.com.luxu.DAO.HibernateDAO;
import br.com.luxu.DAO.InterfaceDAO;
import br.com.luxu.classe.Cidade;
import br.com.luxu.util.FacesContextUtil;

@ViewScoped
@ManagedBean
public class CidadeBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Cidade cidade = new Cidade();
	private Cidade tipoCidadeSelecionada;
	private UIData tabela;
	@SuppressWarnings("rawtypes")
	private DataModel model;
	
	public void salvar(ActionEvent evt){
		InterfaceDAO<Cidade> cidadeDAO = new HibernateDAO<Cidade>(Cidade.class, FacesContextUtil.getRequestSession());
		try {
			cidadeDAO.salvar(cidade);
			model = null;
			FacesContextUtil.setMensagemSucesso("Registro CADASTRADO com sucesso!");
		} finally{
			cidade = new Cidade();
		}
	}
	
	public void atualizar(ActionEvent evt){
		InterfaceDAO<Cidade> cidadeDAO = new HibernateDAO<Cidade>(Cidade.class, FacesContextUtil.getRequestSession());
		try {
			cidadeDAO.atualizar(cidade);
			model = null;
			FacesContextUtil.setMensagemSucesso("Registro ALTERADO com sucesso!");
		} finally {
			cidade = new Cidade();
		}
	}
	
	public void excluir(ActionEvent event) {
		InterfaceDAO<Cidade> cidadeDAO = new HibernateDAO<Cidade>(Cidade.class, FacesContextUtil.getRequestSession());
		try {
			cidadeDAO.excluir(tipoCidadeSelecionada);
			model = null;
		    FacesContextUtil.setMensagemSucesso("Registro EXCLUÍDO com sucesso!");
		} finally {
			tipoCidadeSelecionada = new Cidade();
		}
	}
	
	public void novo(ActionEvent evt){
		cidade = new Cidade();
	}
	
	public String alterar(){
		cidade = (Cidade) getTabela().getRowData();
		return null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DataModel getCidades(){
			if(this.model == null){
				InterfaceDAO<Cidade> cidadeDAO = new HibernateDAO<Cidade>(Cidade.class, FacesContextUtil.getRequestSession());
				model = new ListDataModel(cidadeDAO.getBeans());
			}
			return model;
	}

	public List<SelectItem> getUF(){
		List<SelectItem> listaUF = new ArrayList<SelectItem>();
		listaUF.add(new SelectItem("SP","São Paulo"));
		listaUF.add(new SelectItem("AC","Acre"));
		listaUF.add(new SelectItem("AL","Alagoas"));
		listaUF.add(new SelectItem("AM","Amazonas"));
		listaUF.add(new SelectItem("AP","Amapá"));
		listaUF.add(new SelectItem("BA","Bahia"));
		listaUF.add(new SelectItem("CE","Ceará"));
		listaUF.add(new SelectItem("DF","Distrito Federal"));
		listaUF.add(new SelectItem("ES","Espírito Santo"));
		listaUF.add(new SelectItem("GO","Goiás"));
		listaUF.add(new SelectItem("MA","Maranhão"));
		listaUF.add(new SelectItem("MG","Minas Gerais"));
		listaUF.add(new SelectItem("MS","Mato Grosso do Sul"));
		listaUF.add(new SelectItem("MT","Mato Grosso"));
		listaUF.add(new SelectItem("PA","Para"));
		listaUF.add(new SelectItem("PB","Paraíba"));
		listaUF.add(new SelectItem("PE","Pernambuco"));
		listaUF.add(new SelectItem("PI","Piauí"));
		listaUF.add(new SelectItem("PR","Paraná"));
		listaUF.add(new SelectItem("RJ","Rio de Janeiro"));
		listaUF.add(new SelectItem("RN","Rio Grande do Norte"));
		listaUF.add(new SelectItem("RO","Rondônia"));
		listaUF.add(new SelectItem("RR","Roraima"));
		listaUF.add(new SelectItem("RS","Rio Grande do Sul"));
		listaUF.add(new SelectItem("SC","Santa Catarina"));
		listaUF.add(new SelectItem("SE","Sergipe"));
		listaUF.add(new SelectItem("TO","Tocantis"));
		return listaUF;
	}
	
	public void relatorioGeral() throws IOException {
		InterfaceDAO<Cidade> dao = new HibernateDAO<Cidade>(Cidade.class, FacesContextUtil.getRequestSession());
		List<Cidade> listas = new ArrayList<Cidade>();
		listas = dao.getBeans();
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listas);
		Map<Object,Object> parameters = new HashMap<Object, Object>();
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext.responseComplete();
			ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
			String logoStudio = FacesContext.getCurrentInstance().getExternalContext().getRealPath("images/Studio.png"); 		
			parameters.put("logoStudio", logoStudio);  
			JasperPrint jasperPrint = JasperFillManager.fillReport(scontext.getRealPath("jasper/relatorioCidade.jasper"), parameters, ds);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
			exporter.exportReport();
			byte[] bytes = baos.toByteArray();
			if (bytes != null && bytes.length > 0) {
			HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "inline; filename=\"relatorioCidades.pdf\"");
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
	
	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setTabela(UIData tabela) {
		this.tabela = tabela;
	}

	public UIData getTabela() {
		return tabela;
	}

	public Cidade getTipoCidadeSelecionada() {
	    return tipoCidadeSelecionada;
	}

	public void setTipoCidadeSelecionada(Cidade tipoCidadeSelecionada) {
	    this.tipoCidadeSelecionada = tipoCidadeSelecionada;
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
