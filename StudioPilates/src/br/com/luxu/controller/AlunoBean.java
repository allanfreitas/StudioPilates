package br.com.luxu.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
import br.com.luxu.classe.Aluno;
import br.com.luxu.classe.Cidade;
import br.com.luxu.util.FacesContextUtil;

@ManagedBean
@ViewScoped
public class AlunoBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Aluno aluno = new Aluno();
	private Aluno tipoAlunoSelecionado;
	private Integer cid_Codigo;
	private UIData tabela;
	private Integer idade;
	@SuppressWarnings("rawtypes")
	private DataModel model;

	public void salvar(){
		InterfaceDAO<Aluno> alunoDAO = new HibernateDAO<Aluno>(Aluno.class, FacesContextUtil.getRequestSession());
		if(cid_Codigo != null && cid_Codigo > 0) {
			Cidade cidade = new Cidade();
			cidade.setCodigo(this.getCid_Codigo());
			this.aluno.setCidade(cidade);
		}
		try {
			alunoDAO.salvar(aluno);
			model = null;
			mensagem(1);
		} finally{
			aluno = new Aluno();
			cid_Codigo = null;
		}
	}
	
	public void atualizar(ActionEvent evt){
		InterfaceDAO<Aluno> alunoDAO = new HibernateDAO<Aluno>(Aluno.class, FacesContextUtil.getRequestSession());
		if(cid_Codigo != null && cid_Codigo > 0) {
			Cidade cidade = new Cidade();
			cidade.setCodigo(cid_Codigo);
			aluno.setCidade(cidade);
		}
		try {
			alunoDAO.atualizar(aluno);
			model = null;
			mensagem(2);
		} finally {
			aluno = new Aluno();
			idade = null;
			cid_Codigo = null;
		}
	}
	
	public void excluir(){
		InterfaceDAO<Aluno> alunoDAO = new HibernateDAO<Aluno>(Aluno.class, FacesContextUtil.getRequestSession());
		try {
		    alunoDAO.excluir(tipoAlunoSelecionado);
		    model = null;
		    mensagem(3);
		} finally {
		    tipoAlunoSelecionado = new Aluno();
		}
	}
	
	private void mensagem(int num) {
		if (num == 1)
			 FacesContextUtil.setMensagemSucesso("Registro CADASTRADO com sucesso!");
		else if (num == 2)
			 FacesContextUtil.setMensagemSucesso("Registro ALTERADO com sucesso!");
		else FacesContextUtil.setMensagemSucesso("Registro EXCLUÍDO com sucesso!");
	}

	public String alterar(){
		cid_Codigo = null;
		aluno = (Aluno) getTabela().getRowData();
		if(aluno.getCidade()!= null)
			cid_Codigo = aluno.getCidade().getCodigo();
		if (aluno.getNascimento()!=null){
			setarIdade();
			}
		return null;
	}
	
	public void setarIdade(){
		Calendar dataNascimento = Calendar.getInstance();
		dataNascimento.setTime(aluno.getNascimento());
		Calendar dataAtual = Calendar.getInstance();

		Integer diferencaMes = dataAtual.get(Calendar.MONTH) - dataNascimento.get(Calendar.MONTH);
		Integer diferencaDia = dataAtual.get(Calendar.DAY_OF_MONTH) - dataNascimento.get(Calendar.DAY_OF_MONTH);
		Integer idade = (dataAtual.get(Calendar.YEAR) - dataNascimento.get(Calendar.YEAR));

		if(diferencaMes < 0	|| (diferencaMes == 0 && diferencaDia < 0)) {
			idade--;
		}
		aluno.setIdade(idade); 
	}

	public void novo(ActionEvent evt){
		aluno = new Aluno();
		idade = null;
		cid_Codigo = null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DataModel getAlunos(){
			if(this.model == null){
				InterfaceDAO<Aluno> alunoDAO = new HibernateDAO<Aluno>(Aluno.class,FacesContextUtil.getRequestSession());
				model = new ListDataModel(alunoDAO.getBeansByExample(aluno));
			}
			return model;
	}

	public String carregarAlterar(){
		this.setAluno((Aluno) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get(tabela.getVar()));
		return null;
	}

	public List<SelectItem> getCidades() {
		InterfaceDAO<Cidade> cidadeDAO = new HibernateDAO<Cidade>(Cidade.class,FacesContextUtil.getRequestSession());
		List<Cidade> cidades = cidadeDAO.getBeans();
		List<SelectItem> selectCidade = new ArrayList<SelectItem>();
		selectCidade.add(new SelectItem(null, "Selecione uma cidade..."));
		for (Cidade cidade : cidades) {
			selectCidade.add(new SelectItem(cidade.getCodigo().toString(),cidade.getDescricao()));
		}
		return selectCidade;
	}

	public void relatorioGeral() throws IOException {
		InterfaceDAO<Aluno> dao = new HibernateDAO<Aluno>(Aluno.class,FacesContextUtil.getRequestSession());
		List<Aluno> listas = new ArrayList<Aluno>();
		listas = dao.getBeans();
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listas);
		Map<Object, Object> parameters = new HashMap<Object, Object>();
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext.responseComplete();
			ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
			String logoStudio = FacesContext.getCurrentInstance().getExternalContext().getRealPath("images/Studio.png"); 		
			parameters.put("logoStudio", logoStudio);  
			JasperPrint jasperPrint = JasperFillManager.fillReport(scontext.getRealPath("jasper/relatorioAlunos.jasper"),parameters, ds);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
			exporter.exportReport();
			byte[] bytes = baos.toByteArray();
			if (bytes != null && bytes.length > 0) {
				HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
				response.setContentType("application/pdf");
				response.setHeader("Content-disposition","inline; filename=\"relatorioAlunos.pdf\"");
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
	
	
	public void setCid_Codigo(Integer cid_Codigo) {
		this.cid_Codigo = cid_Codigo;
	}

	public Integer getCid_Codigo() {
		return cid_Codigo;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public UIData getTabela() {
		return tabela;
	}

	public void setTabela(UIData tabela) {
		this.tabela = tabela;
	}

	public Integer getIdade() {
		return idade;
	}

	public void setIdade(Integer idade) {
		this.idade = idade;
	}

	public Aluno getTipoAlunoSelecionado() {
	    return tipoAlunoSelecionado;
	}

	public void setTipoAlunoSelecionado(Aluno tipoAlunoSelecionado) {
	    this.tipoAlunoSelecionado = tipoAlunoSelecionado;
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
