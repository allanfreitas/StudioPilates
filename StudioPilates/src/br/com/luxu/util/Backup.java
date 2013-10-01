package br.com.luxu.util;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.joda.time.DateTime;

public class Backup {

	public static void main(String[] args) {
		fazBackup();
	}

	public static void restaurar_backup() {  
        File file = new File("D:\\studio.sql");  
        try {  
            Runtime.getRuntime().exec("cmd /c C:\\Program Files (x86)\\MySQL\\MySQL Server 5.1\\bin\\mysql.exe" +
            		" --host=localhost --user=root --password=admin --database=studio < " + file);  
        } catch (Exception ex) {  
            JOptionPane.showMessageDialog(null, "Erro ao restaurar backup.\nMotivo: " + ex);  
        }  
    }  	
	
	public static void backupBD(String nomeBD, String nomeBKP) {

		try {

			String comando = "C:\\Program Files (x86)\\MySQL\\MySQL Server 5.1\\bin\\mysqldump";
			ProcessBuilder pb = new ProcessBuilder(comando, "--user=root",
					"--password=admin", nomeBD, "--result-file=D:\\" + nomeBKP
							+ ".sql");
			pb.start();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void restauraBD(String nomeArqOrigem, String nomeBDDestino) {

		try {
			String comando = "C:\\Program Files (x86)\\MySQL\\MySQL Server 5.1\\bin\\mysql";
			ProcessBuilder pb = new ProcessBuilder(comando,"--user=root --password=admin studio --execute='source D:\\studio.sql'");
			pb.start();
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		/*
		 * E para restaurar o banco de dados extraído anteriormente, utilize:
		 * mysql -u root -p123 -h localhost banco_exemplo < backup_banco.sql
		 */
	}

	public static void fazBackup()
	  {
	  DateTime cal = new DateTime();
	  int dia = cal.getDayOfMonth();
	  int mes = cal.getMonthOfYear();
	  int ano = cal.getYear();
	  String snh = "admin"; //props.getSenha_bd();
	  String banco ="studio"; // props.getNm_banco();
	  File diretorio = new File("D:/SDO-Backup");
	  File arquivo = new File("D:/SDO-Backup/bkp_"+banco+"_"+ano+"_"+mes+"_"+dia+".sql");
	  Boolean snbkp = true;
	  // Cria diretório
	  if(!diretorio.isDirectory()) new File("D:/SDO-Backup").mkdir();
	  // Cria Arquivo de Backup
	  try {
	  	  if (arquivo.isFile()){ 
			  if(JOptionPane.showConfirmDialog(null,"Ja foi criado backup hoje, deseja substituir ?","Backup ja existe",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION)
			  { 
				  arquivo.delete();
				  snbkp = true;
			  }
			  else
			  {
				  snbkp = false;
			  } 
		  }
		  	  
		  if(snbkp==true)
		  { 
			  String comando = "C:\\Program Files (x86)\\MySQL\\MySQL Server 5.1\\bin\\mysqldump";
				ProcessBuilder pb = new ProcessBuilder(comando, "--user=root",
						"--password="+snh, banco, "--result-file=D:\\SDO-Backup\\bkp_" +banco+"_"+ano+
						  "_"+mes+"_"+dia+".sql");
				pb.start();
			  int res = 0;//proc.exitValue();
		  
		  if (res == 0)
			  JOptionPane.showMessageDialog(null,"Backup criado com Sucesso !");
		  else 	{
			  JOptionPane.showMessageDialog(null,"Falha ao criar Backup. \n Verifique as configurações ou entre em contato com o suporte !",
					  "Erro ao criar backup", JOptionPane.ERROR_MESSAGE);
		  	  } 
		  }
	  }
	  catch (IOException ex) {
		  ex.printStackTrace();
		  System.out.println(ex); 
		  JOptionPane.showMessageDialog(null,"Erro na criação do Backup !");
	  } catch(Exception err){
		  System.out.println(err);
		  JOptionPane.showMessageDialog(null,"Erro na criação do Backup !");
	  }
	  
}
	 
	public static void restauraBackup() {
		int res;
		String arq, snh = "admin", banco = "studio";
		try{
			JFileChooser chooser = new JFileChooser("D:\\");
			chooser.setDialogTitle("Selecione o arquivo de backup");
			chooser.showOpenDialog(chooser);
			File bkp;
			bkp = chooser.getSelectedFile();
			arq = bkp.getPath();
			Process proc = Runtime.getRuntime().exec("cmd /c mysql -uroot -p" + snh + " -f " + banco + " < " + arq);
			proc.waitFor();
			res = proc.exitValue();
			if (res == 0) {
				JOptionPane.showMessageDialog(null,"Backup Restaurado com sucesso !");
			} else {
				JOptionPane.showMessageDialog(null,"Falha ao restaurar backup. \n Verifique as configurações ou entre em contato com o suporte !");
			}
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
