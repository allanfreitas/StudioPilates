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
import br.com.luxu.DAO.FuncionarioDAO;
import br.com.luxu.DAO.HibernateDAO;
import br.com.luxu.DAO.InterfaceDAO;
import br.com.luxu.classe.Cargo;
import br.com.luxu.classe.Cidade;
import br.com.luxu.classe.Funcionario;
import br.com.luxu.util.FacesContextUtil;

@ManagedBean
@ViewScoped
public class FuncionarioBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Funcionario funcionario = new Funcionario();
	private Cargo cargo = new Cargo();
	private Funcionario tipoFuncionarioSelecionado;
	private Integer cid_Codigo;
	private Integer car_codigo;
	private UIData tabela;
	private Integer idade;
	@SuppressWarnings("rawtypes")
	private DataModel model;
	private List<SelectItem> selectCargo;

	public void salvar(ActionEvent evt){
		InterfaceDAO<Funcionario> funcionarioDAO = new HibernateDAO<Funcionario>(Funcionario.class, FacesContextUtil.getRequestSession());
		// setar a cidade selecionada 
		if(cid_Codigo != null && cid_Codigo > 0) {
			Cidade cidade = new Cidade();
			cidade.setCodigo(this.getCid_Codigo());
			this.funcionario.setCidade(cidade);
		}
		// setar o cargo selecionado 
		Cargo cargo = new Cargo();
		cargo.setCodigo(this.getCar_codigo());
		this.funcionario.setCargo(cargo);
		try {
			funcionarioDAO.salvar(funcionario);
			FacesContextUtil.setMensagemSucesso("Registro CADASTRADO com sucesso!");
		} finally{
			funcionario = new Funcionario();
			car_codigo = null;
			cid_Codigo = null;
			model = null;
		}
	}
	
	public void atualizar(ActionEvent evt){
		InterfaceDAO<Funcionario> funcionarioDAO = new HibernateDAO<Funcionario>(Funcionario.class, FacesContextUtil.getRequestSession());
		Cargo cargo = new Cargo();
		cargo.setCodigo(car_codigo);
		funcionario.setCargo(cargo);
		if(cid_Codigo != null && cid_Codigo > 0) {
			Cidade cidade = new Cidade();
			cidade.setCodigo(cid_Codigo);
			funcionario.setCidade(cidade);
		}
		if (funcionario.getNascimento()!=null)
			setarIdade();
		else funcionario.setIdade(null);
		try {
		    funcionarioDAO.atualizar(funcionario);
		    FacesContextUtil.setMensagemSucesso("Registro alterado com sucesso!");
		} finally {
	    	funcionario = new Funcionario();
    		car_codigo = null;
    		cid_Codigo = null;
    		model = null;
		}
	}
	
	public void excluir(ActionEvent evt){
		FuncionarioDAO funcionarioDAO = new FuncionarioDAO(FacesContextUtil.getRequestSession(),new HibernateDAO<Funcionario>(Funcionario.class, FacesContextUtil.getRequestSession()));
		try {
            funcionarioDAO.excluir(tipoFuncionarioSelecionado);
		    FacesContextUtil.setMensagemSucesso("Registro EXCLUÍDO com sucesso!");
		} finally {
		    tipoFuncionarioSelecionado = new Funcionario();
		    model = null;
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DataModel getFuncionarios(){
			if(this.model == null){
				InterfaceDAO<Funcionario> funcionarioDAO = new HibernateDAO<Funcionario>(Funcionario.class, FacesContextUtil.getRequestSession());
				model = new ListDataModel(funcionarioDAO.getBeans());
			}
			return model;
	}

	public String alterar(){
		car_codigo = null;
		cid_Codigo = null;
		funcionario = (Funcionario) getTabela().getRowData();
		if (funcionario.getCidade() != null)
			 cid_Codigo = funcionario.getCidade().getCodigo();
		car_codigo = funcionario.getCargo().getCodigo();
		return null;
	}

	public void setarIdade(){
		Calendar dataNascimento = Calendar.getInstance();
		dataNascimento.setTime(funcionario.getNascimento());
		Calendar dataAtual = Calendar.getInstance();
		Integer diferencaMes = dataAtual.get(Calendar.MONTH) - dataNascimento.get(Calendar.MONTH);
		Integer diferencaDia = dataAtual.get(Calendar.DAY_OF_MONTH) - dataNascimento.get(Calendar.DAY_OF_MONTH);
		Integer idade = (dataAtual.get(Calendar.YEAR) - dataNascimento.get(Calendar.YEAR));
		if(diferencaMes < 0	|| (diferencaMes == 0 && diferencaDia < 0)) {
			idade--;
		}
		funcionario.setIdade(idade); 
	}

	public void novo(ActionEvent evt){
		funcionario = new Funcionario();
		car_codigo = null;
		cid_Codigo = null;
	}
	
	public String carregarAlterar(){
		this.setFuncionario((Funcionario) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get(tabela.getVar()));
		return null;
	}

	public List<SelectItem> getCargos() {
		InterfaceDAO<Cargo> cargoDAO = new HibernateDAO<Cargo>(Cargo.class, FacesContextUtil.getRequestSession());
		List<Cargo> cargos = cargoDAO.getBeansByExample(cargo);
		selectCargo = new ArrayList<SelectItem>();
		selectCargo.add(new SelectItem(null, "Selecione cargo..."));
		for (Cargo cargo : cargos) {
			selectCargo.add(new SelectItem(cargo.getCodigo().toString(), cargo.getDescricao()));
		}
		return selectCargo;
	}
	
	public void relatorioGeral() throws IOException {
		InterfaceDAO<Funcionario> dao = new HibernateDAO<Funcionario>(Funcionario.class, FacesContextUtil.getRequestSession());
		List<Funcionario> listas = new ArrayList<Funcionario>();
		listas = dao.getBeans();
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listas);
		Map<Object,Object> parameters = new HashMap<Object, Object>();
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext.responseComplete();
			ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
			String logoStudio = FacesContext.getCurrentInstance().getExternalContext().getRealPath("images/Studio.png"); 		
			parameters.put("logoStudio", logoStudio);  
			JasperPrint jasperPrint = JasperFillManager.fillReport(scontext.getRealPath("jasper/relatorioFuncionarios.jasper"), parameters, ds);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
			exporter.exportReport();
			byte[] bytes = baos.toByteArray();
			if (bytes != null && bytes.length > 0) {
			HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "inline; filename=\"relatorioFuncionarios.pdf\"");
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


	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Integer getCid_Codigo() {
		return cid_Codigo;
	}

	public void setCid_Codigo(Integer cid_Codigo) {
		this.cid_Codigo = cid_Codigo;
	}

	public Integer getCar_codigo() {
		return car_codigo;
	}

	public void setCar_codigo(Integer car_codigo) {
		this.car_codigo = car_codigo;
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

	public Funcionario getTipoFuncionarioSelecionado() {
	    return tipoFuncionarioSelecionado;
	}

	public void setTipoFuncionarioSelecionado(Funcionario tipoFuncionarioSelecionado) {
	    this.tipoFuncionarioSelecionado = tipoFuncionarioSelecionado;
	}

	public Cargo getCargo() {
		return cargo;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

}
