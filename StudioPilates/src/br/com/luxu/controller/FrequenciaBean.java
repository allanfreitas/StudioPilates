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
import br.com.luxu.DAO.TurmaAlunoDAO;
import br.com.luxu.DAO.TurmaDAO;
import br.com.luxu.classe.Aluno;
import br.com.luxu.classe.Frequencia;
import br.com.luxu.classe.Turma;
import br.com.luxu.classe.Turma_Aluno;
import br.com.luxu.util.FacesContextUtil;

@ManagedBean
@ViewScoped
public class FrequenciaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Frequencia frequencia = new Frequencia();
	private Frequencia tipoFrequenciaSelecionada;
	private Aluno aluno = new Aluno();
	private Turma turma = new Turma();
	private Turma_Aluno turma_Aluno = new Turma_Aluno();
	private Integer tur_codigo;
	private Integer tur_codigo_reposicao;
	private Integer alu_codigo;
	private UIData tabela;
	@SuppressWarnings("rawtypes")
	private DataModel model;
	
	public void confirmarTurma(ActionEvent evt) {
		TurmaAlunoDAO turmaAlunosDAO = new TurmaAlunoDAO(FacesContextUtil.getRequestSession());
		FrequenciaDAO frequenciaDAO = new FrequenciaDAO(FacesContextUtil.getRequestSession());
		// lista das turmas na data passada
		List<Frequencia> listaFrequencia = frequenciaDAO.getBeanByDataTurma(frequencia.getData(),tur_codigo);
		if(listaFrequencia.isEmpty()){
			List<Turma_Aluno> listTurAluno = turmaAlunosDAO.getTurmaAlunos(tur_codigo);
			for(int i=0;i<listTurAluno.size();i++){
				frequencia.setAluno(listTurAluno.get(i).getAluno());
				frequencia.setTurma(listTurAluno.get(i).getTurma());
				frequenciaDAO.salvar(frequencia);
				frequenciaDAO = new FrequenciaDAO(FacesContextUtil.getRequestSession());
				Date data = frequencia.getData();
				frequencia = new Frequencia();
				frequencia.setData(data);
			}
		}
		model = null;
	}

	public void atualizar(ActionEvent evt) {
		TurmaDAO turmaDAO = new TurmaDAO(FacesContextUtil.getRequestSession());
		FrequenciaDAO frequenciaDAO = new FrequenciaDAO(FacesContextUtil.getRequestSession());
		Turma tur = null;
		// se vier com alguma turma para repor entra aki, caso contrário não precisa de turma de reposição e a mesma fica nula
		if(tur_codigo_reposicao!=null){
			tur = turmaDAO.getBean(tur_codigo_reposicao);
			// incrementar mais um aluno na turma para fins de ter capacidade
			tur.setInscritos(tur.getInscritos()+1);
			// atualiza a turma atual com o acréscimo de mais um aluno
			turmaDAO.atualizar(tur);
			Turma turma = frequencia.getTurma();
			// decrementar mais um aluno na turma corrente
			turma.setInscritos(turma.getInscritos()-1);
			turmaDAO.atualizar(turma);
			frequencia.setTurma(turma);
		} 
		frequencia.setTurmaReposicao(tur);
		frequenciaDAO.atualizar(frequencia);
		Date data = frequencia.getData();
		frequencia = new Frequencia();
		frequencia.setData(data);
	}

	public void excluir(ActionEvent evt) {
		FrequenciaDAO frequenciaDAO = new FrequenciaDAO(FacesContextUtil.getRequestSession());
	    frequenciaDAO.excluir(tipoFrequenciaSelecionada);
	    tipoFrequenciaSelecionada = new Frequencia();
		model = null;
	}
	public String alterarTurma(ActionEvent evt) {
		tur_codigo = null;
		frequencia = new Frequencia();
		return null;
	}

	public String alterar() {
		frequencia = (Frequencia) getTabela().getRowData();
		return null;
	}

	public String cancelar() {
		Date data = frequencia.getData();
		frequencia = new Frequencia();
		frequencia.setData(data);
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DataModel getFrequencias(){
			if(this.model == null){
				FrequenciaDAO frequenciaDAO = new FrequenciaDAO(FacesContextUtil.getRequestSession());
				model = new ListDataModel(frequenciaDAO.getBeanByDataTurma(frequencia.getData(),tur_codigo));
			}
			return model;
	}

	public List<SelectItem> getTurmas() {
		//InterfaceDAO<Turma> turmaDAO = new HibernateDAO<Turma>(Turma.class, FacesContextUtil.getRequestSession());
		TurmaDAO turmaDAO = new TurmaDAO(FacesContextUtil.getRequestSession());
		List<Turma> turmas = turmaDAO.getBeans();
		List<SelectItem> selectTurma = new ArrayList<SelectItem>();
		//selectTurma = null;
		//selectTurma.add(new SelectItem(null, "Selecione uma turma.."));
		for (Turma turma : turmas) {
			selectTurma.add(new SelectItem(turma.getCodigo().toString(),
			turma.getDia()+"("+turma.getHoraInicio()+"-"+turma.getHoraFim()+")"+turma.getModalidade().getDescricao()));
		}
		return selectTurma;
	}

	public List<SelectItem> getReposicoes(){
		//InterfaceDAO<Turma> dao = new HibernateDAO<Turma>(Turma.class, FacesContextUtil.getRequestSession());
		TurmaDAO dao = new TurmaDAO(FacesContextUtil.getRequestSession());
		Turma tur = new Turma();
		// turma que está sendo feito a frequencia
		tur = dao.getBean(tur_codigo);
		List<Turma> turmas = null;
		List<SelectItem> selectTurma = new ArrayList<SelectItem>();
		// pesquisará se existe uma turma de modalidade igual e que não seja ela mesma
		// FALTA FAZER A PARTE DE DA TURMA TER MAIS CAPACIDADE DO QUE SE PERMITE
		turmas = dao.getBeanByReposicao(tur.getModalidade().getCodigo(),tur_codigo, tur.getInscritos());
		if (turmas.size()>0){
			selectTurma.add(new SelectItem(null, "Selecione uma turma.."));
			for (Turma turma : turmas) {
				selectTurma.add(new SelectItem(turma.getCodigo().toString(),turma.getDia()+
						"("+turma.getHoraInicio()+"-"+turma.getHoraFim()+")"+turma.getModalidade().getDescricao()));
			}
		}
		else selectTurma.add(new SelectItem(null, "Nenhuma turma para reposição"));
		return selectTurma;
	}
	
	public List<Aluno> getAlunos() {
		TurmaAlunoDAO turmaAlunosDAO = new TurmaAlunoDAO(FacesContextUtil.getRequestSession());
		List<Turma_Aluno> listTurAluno = turmaAlunosDAO.getTurmaAlunos(tur_codigo);
		List<Aluno> listarAlunos = new ArrayList<Aluno>();
		for(Turma_Aluno listAlunos : listTurAluno)
			listarAlunos.add(listAlunos.getAluno());
		return listarAlunos;
	}

	public void relatorioGeral() throws IOException {
		InterfaceDAO<Frequencia> dao = new HibernateDAO<Frequencia>(Frequencia.class, FacesContextUtil.getRequestSession());
		List<Frequencia> listas = new ArrayList<Frequencia>();
		listas = dao.getBeans();
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listas);
		Map<Object,Object> parameters = new HashMap<Object, Object>();
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext.responseComplete();
			ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
			String logoStudio = FacesContext.getCurrentInstance().getExternalContext().getRealPath("images/Studio.png"); 		
			parameters.put("logoStudio", logoStudio);  
			JasperPrint jasperPrint = JasperFillManager.fillReport(scontext.getRealPath("jasper/relatorioFrequencia.jasper"), parameters, ds);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
			exporter.exportReport();
			byte[] bytes = baos.toByteArray();
			if (bytes != null && bytes.length > 0) {
			HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "inline; filename=\"relatorioFrequencia.pdf\"");
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
	
	public Frequencia getFrequencia() {
		return frequencia;
	}

	public void setFrequencia(Frequencia frequencia) {
		this.frequencia = frequencia;
	}

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public Integer getTur_codigo() {
		return tur_codigo;
	}

	public void setTur_codigo(Integer tur_codigo) {
		this.tur_codigo = tur_codigo;
	}

	public UIData getTabela() {
		return tabela;
	}

	public void setTabela(UIData tabela) {
		this.tabela = tabela;
	}

	public Integer getAlu_codigo() {
		return alu_codigo;
	}

	public void setAlu_codigo(Integer alu_codigo) {
		this.alu_codigo = alu_codigo;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public Turma_Aluno getTurma_Aluno() {
		return turma_Aluno;
	}

	public void setTurma_Aluno(Turma_Aluno turma_Aluno) {
		this.turma_Aluno = turma_Aluno;
	}

	public Frequencia getTipoFrequenciaSelecionada() {
	    return tipoFrequenciaSelecionada;
	}

	public void setTipoFrequenciaSelecionada(Frequencia tipoFrequenciaSelecionada) {
	    this.tipoFrequenciaSelecionada = tipoFrequenciaSelecionada;
	}

	public Integer getTur_codigo_reposicao() {
		return tur_codigo_reposicao;
	}

	public void setTur_codigo_reposicao(Integer tur_codigo_reposicao) {
		this.tur_codigo_reposicao = tur_codigo_reposicao;
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
