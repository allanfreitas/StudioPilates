package br.com.luxu.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
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

import org.hibernate.exception.ConstraintViolationException;

import br.com.luxu.DAO.HibernateDAO;
import br.com.luxu.DAO.InterfaceDAO;
import br.com.luxu.classe.Funcionario;
import br.com.luxu.classe.Usuario;
import br.com.luxu.util.FacesContextUtil;

@ManagedBean
@ViewScoped
public class UsuarioBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Usuario usuario = new Usuario();
	private Usuario tipoUsuarioSelecionado;
	private UIData tabela;
	private Integer codigoFuncionario;
	private String confirmarSenha;
	@SuppressWarnings("rawtypes")
	private DataModel model;

	public String salvar() {
		if (confirmarSenha.equals(usuario.getSenha())) {
			MessageDigest algoritmo;
			byte messageDigest[];
			StringBuilder hexString;
			InterfaceDAO<Usuario> usuarioDAO = new HibernateDAO<Usuario>(Usuario.class, FacesContextUtil.getRequestSession());
			Funcionario funcionario = new Funcionario();
			funcionario.setCodigo(codigoFuncionario);
			usuario.setFuncionario(funcionario);
			try {
				algoritmo = MessageDigest.getInstance("MD5");  // 32 letras
				messageDigest = algoritmo.digest(usuario.getSenha().getBytes("UTF-8"));
				hexString = new StringBuilder();
				for (byte b : messageDigest) {
					hexString.append(String.format("%02X", 0xFF & b));
				}
				usuario.setSenha(hexString.toString());
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			try {
				usuarioDAO.salvar(usuario);
				model = null;
				FacesContextUtil.setMensagemSucesso("Registro CADASTRADO com sucesso!");
			} finally {
				usuario = new Usuario();
				codigoFuncionario = null;
			}
		} else {
		    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro", "Senha e Contra Senha não conferem!!"));
		    usuario.setSenha(null);
		}
		confirmarSenha = null;
		return null;
	}

	public void atualizar(ActionEvent evt) {
		if (confirmarSenha.equals(usuario.getSenha())) {
			InterfaceDAO<Usuario> usuarioDAO = new HibernateDAO<Usuario>(Usuario.class, FacesContextUtil.getRequestSession());
			Funcionario funcionario = new Funcionario();
			funcionario.setCodigo(codigoFuncionario);
			usuario.setFuncionario(funcionario);
			try {
				usuarioDAO.atualizar(usuario);
				model = null;
				FacesContextUtil.setMensagemSucesso("Registro ALTERADO com sucesso!");
			} finally {
				usuario = new Usuario();
				codigoFuncionario = null;
			}
		} else  FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro", "Senha e Contra Senha não conferem!!"));
		confirmarSenha = null;
	}

	public void excluir(ActionEvent evt) {
		InterfaceDAO<Usuario> usuarioDAO = new HibernateDAO<Usuario>(Usuario.class, FacesContextUtil.getRequestSession());
		LoginBean loginBean = (LoginBean)FacesContextUtil.getSessionAttribute("loginBean");
		if(!loginBean.getUsuario().getLogin().equals(tipoUsuarioSelecionado.getLogin()))
		{
		    try {
				usuarioDAO.excluir(tipoUsuarioSelecionado);
				model = null;
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Concluído", "Registro excluído com sucesso!"));
		    } catch (ConstraintViolationException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atenção", "Registro não pode ser excluído!"));
		    } finally {
			tipoUsuarioSelecionado  = new Usuario();
			codigoFuncionario = null;
		    } 
		} else FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Atenção", "Este usuário não pode ser excluído nesse momento!"));
		
	}

	public String alterar() {
		codigoFuncionario = null;
		usuario = (Usuario) getTabela().getRowData();
		confirmarSenha = usuario.getSenha();
		codigoFuncionario = usuario.getFuncionario().getCodigo();
		return null;
	}

	public void novo(ActionEvent evt) {
		usuario = new Usuario();
		codigoFuncionario = null;
		confirmarSenha = null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DataModel getUsuarios(){
			if(this.model == null){
				InterfaceDAO<Usuario> usuarioDAO = new HibernateDAO<Usuario>(Usuario.class, FacesContextUtil.getRequestSession());
				model = new ListDataModel(usuarioDAO.getBeans());
			}
			return model;
	}

	public List<SelectItem> getFuncionarios() {
		InterfaceDAO<Funcionario> funcionarioDAO = new HibernateDAO<Funcionario>(Funcionario.class, FacesContextUtil.getRequestSession());
		List<Funcionario> funcionarios = funcionarioDAO.getBeans();
		List<SelectItem> selectFuncionario = new ArrayList<SelectItem>();
		selectFuncionario.add(new SelectItem(null, "Selecione um funcionário.."));
		for (Funcionario funcionario : funcionarios) {
			selectFuncionario.add(new SelectItem(funcionario.getCodigo().toString(), funcionario.getNome()));
		}
		return selectFuncionario;
	}

	public List<SelectItem> getNivel(){
		List<SelectItem> listaNivel = new ArrayList<SelectItem>();
		listaNivel.add(new SelectItem("Selecione o nível",null));
		listaNivel.add(new SelectItem("Gerente","Gerente"));
		listaNivel.add(new SelectItem("Operador","Operador"));
		listaNivel.add(new SelectItem("Usuário","Usuário"));
		return listaNivel;
	}
	
	public void relatorioGeral() throws IOException {
		InterfaceDAO<Usuario> dao = new HibernateDAO<Usuario>(Usuario.class, FacesContextUtil.getRequestSession());
		List<Usuario> listas = new ArrayList<Usuario>();
		listas = dao.getBeans();
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listas);
		Map<Object,Object> parameters = new HashMap<Object, Object>();
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext.responseComplete();
			ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
			String logoStudio = FacesContext.getCurrentInstance().getExternalContext().getRealPath("images/Studio.png"); 		
			parameters.put("logoStudio", logoStudio);  
			JasperPrint jasperPrint = JasperFillManager.fillReport(scontext.getRealPath("jasper/relatorioUsuario.jasper"), parameters, ds);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
			exporter.exportReport();
			byte[] bytes = baos.toByteArray();
			if (bytes != null && bytes.length > 0) {
			HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "inline; filename=\"relatorioUsuarios.pdf\"");
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public UIData getTabela() {
		return tabela;
	}

	public void setTabela(UIData tabela) {
		this.tabela = tabela;
	}

	public void setCodigoFuncionario(Integer codigoFuncionario) {
		this.codigoFuncionario = codigoFuncionario;
	}

	public Integer getCodigoFuncionario() {
		return codigoFuncionario;
	}

	public String getConfirmarSenha() {
		return confirmarSenha;
	}

	public void setConfirmarSenha(String confirmarSenha) {
		this.confirmarSenha = confirmarSenha;
	}

	public Usuario getTipoUsuarioSelecionado() {
	    return tipoUsuarioSelecionado;
	}

	public void setTipoUsuarioSelecionado(Usuario tipoUsuarioSelecionado) {
	    this.tipoUsuarioSelecionado = tipoUsuarioSelecionado;
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
