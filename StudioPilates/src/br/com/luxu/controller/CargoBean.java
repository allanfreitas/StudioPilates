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
import br.com.luxu.classe.Cargo;
import br.com.luxu.util.FacesContextUtil;

@ManagedBean
@ViewScoped
public class CargoBean implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private Cargo cargo = new Cargo();
    private Cargo tipoCargoSelecionado;
    private UIData tabela;
	@SuppressWarnings("rawtypes")
	private DataModel model;

	public void salvar(ActionEvent evt){
		InterfaceDAO<Cargo> cargoDAO = new HibernateDAO<Cargo>(Cargo.class,FacesContextUtil.getRequestSession());
		try {
		    cargoDAO.salvar(cargo);
			model = null;
			FacesContextUtil.setMensagemSucesso("Registro CADASTRADO com sucesso!");
		} finally {
		    tipoCargoSelecionado = new Cargo();
		    cargo = new Cargo();
		}
    }

    public void atualizar(ActionEvent evt) {
		InterfaceDAO<Cargo> cargoDAO = new HibernateDAO<Cargo>(Cargo.class,FacesContextUtil.getRequestSession());
		try {
		    cargoDAO.atualizar(cargo);
			model = null;
			FacesContextUtil.setMensagemSucesso("Registro ALTERADO com sucesso!");
		} finally {
		    tipoCargoSelecionado = new Cargo();
		    cargo = new Cargo();
		}
    }

    public void excluir(ActionEvent evt) {
		InterfaceDAO<Cargo> cargoDAO = new HibernateDAO<Cargo>(Cargo.class,FacesContextUtil.getRequestSession());
		try {
		    cargoDAO.excluir(tipoCargoSelecionado);
			//cargoDAO.excluir(tipoCargoSelecionado);
			model = null;
			FacesContextUtil.setMensagemSucesso("Registro EXCLUÍDO com sucesso!");
		} finally {
		    tipoCargoSelecionado = new Cargo();
		    cargo = new Cargo();
		}
    }

    public String alterar() {
		cargo = (Cargo) getTabela().getRowData();
		return null;
    }

    public void novo(ActionEvent evt) {
    	cargo = new Cargo();
    }

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DataModel getCargos(){
			if(this.model == null){
				InterfaceDAO<Cargo> cargoDAO = new HibernateDAO<Cargo>(Cargo.class,FacesContextUtil.getRequestSession());
				model = new ListDataModel(cargoDAO.getBeans());
				//model = new ListDataModel(cargoDAO.getBeansByExample(this.getCargo()));
			}
			return model;
	}

	public void relatorioGeral() throws IOException {
		InterfaceDAO<Cargo> cargoDAO = new HibernateDAO<Cargo>(Cargo.class,FacesContextUtil.getRequestSession());
		List<Cargo> listaCargo = new ArrayList<Cargo>();
		listaCargo = cargoDAO.getBeans();
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listaCargo);
		Map<Object, Object> parameters = new HashMap<Object, Object>();
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext.responseComplete();
			ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
			String logoStudio = FacesContext.getCurrentInstance().getExternalContext().getRealPath("images/Studio.png"); 		
			parameters.put("logoStudio", logoStudio);  
			JasperPrint jasperPrint = JasperFillManager.fillReport(scontext.getRealPath("jasper/relatorioCargo.jasper"),parameters, ds);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
			exporter.exportReport();
			byte[] bytes = baos.toByteArray();
			if (bytes != null && bytes.length > 0) {
				HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
				response.setContentType("application/pdf");
				response.setHeader("Content-disposition","inline; filename=\"relatorioCargo.pdf\"");
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

	public Cargo getCargo() {
	return cargo;
    }

    public void setCargo(Cargo cargo) {
	this.cargo = cargo;
    }

    public void setTabela(UIData tabela) {
	this.tabela = tabela;
    }

    public UIData getTabela() {
	return tabela;
    }

    public Cargo getTipoCargoSelecionado() {
	return tipoCargoSelecionado;
    }

    public void setTipoCargoSelecionado(Cargo tipoCargoSelecionado) {
	this.tipoCargoSelecionado = tipoCargoSelecionado;
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
