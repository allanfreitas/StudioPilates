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

import br.com.luxu.DAO.FrequenciaDAO;
import br.com.luxu.DAO.HibernateDAO;
import br.com.luxu.DAO.InterfaceDAO;
import br.com.luxu.classe.Frequencia;
import br.com.luxu.classe.Turma;
import br.com.luxu.util.FacesContextUtil;

@ManagedBean
@ViewScoped
public class ReposicaoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Frequencia frequencia = new Frequencia();
	private UIData tabela;
	private Date dtInicial = null;
	private Date dtFinal = null;
	private String tur_codigo = null;
	@SuppressWarnings("rawtypes")
	private DataModel model;

	public String confirmar() {
		//InterfaceDAO<Frequencia> frequenciaDAO = new HibernateDAO<Frequencia>(Frequencia.class, FacesContextUtil.getRequestSession());
		//List<Frequencia> listaFrequencia = frequenciaDAO.getBeanByDatas(dtInicial,dtFinal);
		model = null;
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DataModel getFrequencias(){
			if(this.model == null){
				FrequenciaDAO frequenciaDAO = new FrequenciaDAO(FacesContextUtil.getRequestSession());
				model = new ListDataModel(frequenciaDAO.getBeanByDatas(dtInicial,dtFinal));
			}
			return model;
	}

	public String alterar() {
	    this.dtInicial = null;
	    this.dtFinal = null;
	    tur_codigo = null;
		return null;
	}
	
	public String alterarRepor(){
	    frequencia = (Frequencia) getTabela().getRowData();
	    tur_codigo = "teste";
	    return null;
	}

	public String cancelar(){
	    tur_codigo = null;
	    return null;
	}

	public String atualizar() {
		InterfaceDAO<Frequencia> frequenciaDAO = new HibernateDAO<Frequencia>(Frequencia.class, FacesContextUtil.getRequestSession());
		InterfaceDAO<Turma> turmaDAO = new HibernateDAO<Turma>(Turma.class, FacesContextUtil.getRequestSession());
		//FrequenciaDAO frequenciaDAO = new FrequenciaDAO();
		//TurmaDAO turmaDAO = new TurmaDAO();
		if(frequencia.getRepos().equals("Sim")){
			// se reposição for SIM é passado pra SIM a presença tb para que não saia na próxima lista de reposição 
			frequencia.setPresenca("Sim");
			Turma tur = turmaDAO.getBean(frequencia.getTurmaReposicao().getCodigo());
			// decrementa da turma de reposição que foi incrementada no formulário FREQUENCIA
			tur.setInscritos(tur.getInscritos()-1);
			// atualiza a turma atual com o acréscimo de mais um aluno
			turmaDAO.atualizar(tur);
			Turma turma = frequencia.getTurma();
			// incrementar mais um aluno na turma corrente
			turma.setInscritos(turma.getInscritos()+1);
			turmaDAO.atualizar(turma);
			frequencia.setTurma(turma);
		}
		frequenciaDAO.atualizar(frequencia);
		Date data = frequencia.getData();
		frequencia = new Frequencia();
		frequencia.setData(data);
		tur_codigo = null;
		model = null;
		return null;
	}

	public String excluir(ActionEvent evt) {
		frequencia = (Frequencia) getTabela().getRowData();
		InterfaceDAO<Frequencia> frequenciaDAO = new HibernateDAO<Frequencia>(Frequencia.class, FacesContextUtil.getRequestSession());
		//FrequenciaDAO frequenciaDAO = new FrequenciaDAO();
		frequenciaDAO.excluir(frequencia);
		return null;
	}
	
	public List<SelectItem> getTurmas() {
	    	FrequenciaDAO frequenciaDAO = new FrequenciaDAO(FacesContextUtil.getRequestSession());
	    	//FrequenciaDAO frequenciaDAO = new FrequenciaDAO();
        	List<Turma> turmas = frequenciaDAO.getBeanTurma(frequencia.getTurma().getModalidade().getCodigo(), frequencia.getTurma().getInscritos()); 
        	List<SelectItem> selectTurma = new ArrayList<SelectItem>();
        	selectTurma.add(new SelectItem(null, "Turma disponíveis..."));
        	for (Turma turma : turmas) {
        		selectTurma.add(new SelectItem(turma.getCodigo().toString(),
        		turma.getDia()+"("+turma.getHoraInicio()+"-"+turma.getHoraFim()+")"));
        	}
        	return selectTurma;
	}

	public void relatorioGeral() throws IOException {
		InterfaceDAO<Frequencia> dao = new HibernateDAO<Frequencia>(Frequencia.class, FacesContextUtil.getRequestSession());
		List<Frequencia> listas = new ArrayList<Frequencia>();
		// se não digitar nada no campo data
		if(getDtInicial() == null)
		     listas = dao.getBeanByDatas(dtInicial,dtFinal);
		else listas = dao.getBeanByDatas(dtInicial,dtFinal);
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listas);
		Map<Object,Object> parameters = new HashMap<Object, Object>();
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext.responseComplete();
			ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
			String logoStudio = FacesContext.getCurrentInstance().getExternalContext().getRealPath("images/Studio.png"); 		
			parameters.put("logoStudio", logoStudio);  
			JasperPrint jasperPrint = JasperFillManager.fillReport(scontext.getRealPath("jasper/relatorioReposicao.jasper"), parameters, ds);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
			exporter.exportReport();
			byte[] bytes = baos.toByteArray();
			if (bytes != null && bytes.length > 0) {
			HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "inline; filename=\"relatorioReposicao.pdf\"");
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
	
	
	public Date getDtInicial() {
		return dtInicial;
	}

	public void setDtInicial(Date dtInicial) {
		this.dtInicial = dtInicial;
	}

	public Date getDtFinal() {
		return dtFinal;
	}

	public void setDtFinal(Date dtFinal) {
	    	
		this.dtFinal = dtFinal;
	}

	public Frequencia getFrequencia() {
	    return frequencia;
	}

	public void setFrequencia(Frequencia frequencia) {
		this.frequencia = frequencia;
	}

	public UIData getTabela() {
		return tabela;
	}

	public void setTabela(UIData tabela) {
		this.tabela = tabela;
	}

	public String getTur_codigo() {
	    return tur_codigo;
	}

	public void setTur_codigo(String tur_codigo) {
	    this.tur_codigo = tur_codigo;
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
