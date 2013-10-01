package br.com.luxu.util;

import java.net.MalformedURLException;
//import java.net.URL;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;

public class enviarEmail {
	
	public enviarEmail() throws EmailException, MalformedURLException {
		enviaEmailSimples();
		enviaEmailComAnexo();
		enviaEmailFormatoHtml();
	}

	public void enviaEmailSimples() throws EmailException {
		SimpleEmail email = new SimpleEmail();
		email.setHostName("smtp.gmail.com"); // o servidor SMTP para envio do e-mail
		email.addTo("email@gmail", "Nome"); // destinatário
		email.setFrom("email@gmail.com", "Nome"); // remetente
		email.setSubject("Teste -> Email simples"); // assunto do e-mail
		email.setMsg("Teste de Email utilizando commons-email"); // conteudo do e-mail
		email.setAuthentication("login", "senha");
		email.setSmtpPort(465);
		email.setSSL(true);
		email.setTLS(true);
		email.send();
	}

	public void enviaEmailComAnexo() throws EmailException{
		// cria o anexo 1.
		EmailAttachment anexo1 = new EmailAttachment();
		anexo1.setPath("caminho do arquivo"); // caminho do arquivo
		// (RAIZ_PROJETO/teste/teste.txt)
		anexo1.setDisposition(EmailAttachment.ATTACHMENT);
		anexo1.setDescription("Exemplo de arquivo anexo");
		anexo1.setName("nome_do_arquivo");
		// cria o anexo 2.
		EmailAttachment anexo2 = new EmailAttachment();
		anexo2.setPath("caminho do arquivo"); // caminho do arquivo
		// (RAIZ_PROJETO/teste/teste2.jsp)
		anexo2.setDisposition(EmailAttachment.ATTACHMENT);
		anexo2.setDescription("Exemplo de arquivo anexo");
		anexo2.setName("nome_do_arquivo");
		// configura o email
		MultiPartEmail email = new MultiPartEmail();
		email.setHostName("smtp.gmail.com"); // o servidor SMTP para envio do e-mail
		email.addTo("email@gmail.com", "nome"); // destinatário
		email.setFrom("email@gmail.com", "nome"); // remetente
		email.setSubject("Teste -> Email com anexo"); // assunto do e-mail
		email.setMsg("conteudo"); // conteudo do e-mail
		email.setAuthentication("login", "senha");
		email.setSmtpPort(465);
		email.setSSL(true);
		email.setTLS(true);
		email.send();
		// adiciona arquivo(s) anexo(s)
		email.attach(anexo1);
		email.attach(anexo2);
		// envia o email
		email.send();
	}
	
	public void enviaEmailFormatoHtml() throws EmailException, MalformedURLException {
		HtmlEmail email = new HtmlEmail();
		// adiciona uma imagem ao corpo da mensagem e retorna seu id</div>
		//URL url = new URL("http://www.apache.org/images/asf_logo_wide.gif");
		//String cid = email.embed(url, "Apache logo");
		// configura a mensagem para o formato HTML
		email.setHtmlMsg("<html>Logo do Apache - <img ></html>");
		// configure uma mensagem alternativa caso o servidor não suporte HTML
		email.setTextMsg("Seu servidor de e-mail não suporta mensagem HTML");
		email.setHostName("smtp.gmail.com"); // o servidor SMTP para envio do e-mail
		email.addTo("teste@gmail.com", "Guilherme"); //destinatário
		email.setFrom("teste@gmail.com", "Eu"); // remetente</div>
		email.setSubject("Teste -> Html Email"); // assunto do e-mail
		email.setMsg("Teste de Email HTML utilizando commons-email"); //conteudo do e-mail
		email.setAuthentication("teste", "xxxxx");
		email.setSmtpPort(465);
		email.setSSL(true);
		email.setTLS(true);
		// envia email
		email.send();
	}

	public static void main(String[] args) throws EmailException, MalformedURLException {
		try {
			new enviarEmail();
		} catch (EmailException e) {
			System.out.println("ERRO EMAIL" + " " + e.getMessage());
		}
	}
}