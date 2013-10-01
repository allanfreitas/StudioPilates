package br.com.luxu.controller;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import br.com.luxu.DAO.HibernateDAO;
import br.com.luxu.DAO.InterfaceDAO;
import br.com.luxu.classe.Usuario;
import br.com.luxu.util.FacesContextUtil;

@ManagedBean
@SessionScoped
public class LoginBean {
	
	private Usuario usuario = new Usuario();
	private Boolean autenticado = false;
	private HttpSession session;
	
	public String verificarLogin() {
		InterfaceDAO<Usuario> dao = new HibernateDAO<Usuario>(Usuario.class,FacesContextUtil.getRequestSession());
		if (dao.getBeanLogin(usuario.getLogin()) == true)
		{
			String senha = criptografarSenha(usuario.getSenha());
			if(dao.getBeanSenha(senha) == true){
				List<Usuario> listaUsuario = dao.getBeanUsuario(usuario.getLogin());
				usuario.setFuncionario(listaUsuario.get(0).getFuncionario());
				autenticado = true;
				return "/formularios/principal";
			}
			else FacesContextUtil.setMensagemErro("Senha errada!!");
		}
		else FacesContextUtil.setMensagemErro("Usuario: " + usuario.getLogin() + " não existe"); 
		limpar();
		return null;
	}

	private String criptografarSenha(String senha) {
		MessageDigest algoritmo;
		byte messageDigest[];
		StringBuilder hexString;
		try {
			algoritmo = MessageDigest.getInstance("MD5");  // 32 letras
			messageDigest = algoritmo.digest(senha.getBytes("UTF-8"));
			hexString = new StringBuilder();
			for (byte b : messageDigest) {
				hexString.append(String.format("%02X", 0xFF & b));
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) { 
			e.printStackTrace();
			return null;
		}
		
	}

	public void efetuaLogoff() {
//		public String efetuaLogoff() {
		try {
            FacesContext ctx = FacesContext.getCurrentInstance();
            session = (HttpSession) ctx.getExternalContext().getSession(false);
            session.setAttribute("usuario", null);
            session.setAttribute("autenticado", false);
            //ctx.getExternalContext().redirect(ctx.getExternalContext().getRequestContextPath() + "/app" + Constantes.PAGINA_INDEX);
            ctx.getExternalContext().redirect(ctx.getExternalContext().getRequestContextPath() + "/index.jsp");
            session.invalidate();
        } catch (Exception e) {
	        }
		//limpar();
		//return /formularios/index ;
	}

	public void limpar() {
		usuario = new Usuario();
		autenticado = false;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public Boolean getAutenticado() {
		return autenticado;
	}
}
