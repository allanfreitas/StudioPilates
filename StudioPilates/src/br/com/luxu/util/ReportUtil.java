package br.com.luxu.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class ReportUtil {
    
    private static final String JASPER_COMPILED_EXTENSION = ".jasper";
    private static final String JASPER_SOURCE_EXTENSION = ".jrxml";

	public static DecimalFormat dcFormat = new DecimalFormat("###,###,##0.00");
	public static DecimalFormat dcFormat3 = new DecimalFormat("###,###,##0.000");
	public static SimpleDateFormat dtFormat = new SimpleDateFormat("dd/MM/yyyy");
	public static SimpleDateFormat dtFormatHM = new SimpleDateFormat("HH:mm");

	public static String getNumber(BigDecimal value) {
		return (value == null)?null:dcFormat.format(value);
	}

	public static String getNumber3(BigDecimal value) {
		return (value == null)?null:dcFormat3.format(value);
	}

	public static String getDate(Date value) {
		return (value == null)?null:dtFormat.format(value);
	}

	public static String getHourMinute(Date value) {
		return (value == null)?null:dtFormatHM.format(value);
	}

	public static String getLeftZero(String value, int lenght) {
		String ret = value;
		if (ret.length() > lenght)
			return ret.substring(lenght);
		else if (ret.length() == lenght)
			return ret;
		else {
			while (ret.length() < lenght)
				ret = "0" + ret;
			return ret;
		}
	}

	public static String getStringDigitsLastPosition(String value) {
		String ret = "";
		if(value!=null && value.length()>0){
			return value.substring(0,value.length()-1).concat("-").concat(value.substring(value.length()-1,value.length()));
		}
		return ret;
	}


	public static Map<String,String> getPropriedadesDoParametro(JRParameter jrParameter){
		Map<String,String> propriedades = new HashMap<String,String>();

		if(jrParameter.getPropertiesMap()!=null){

			for (final String propriedade : jrParameter.getPropertiesMap().getPropertyNames()){
				propriedades.put(propriedade, jrParameter.getPropertiesMap().getProperty(propriedade));
			}

		}

		return propriedades;
	}    
	
	public static boolean existeRelatorio(String title, String nomeRelatorio){
		//Tenta sem remover os espacos
		if((title.toUpperCase().indexOf(nomeRelatorio.toUpperCase()) > -1) || (title.toUpperCase().trim().indexOf(nomeRelatorio.toUpperCase()) > -1) ){
			return true;
		}else{
			//senao encontrar tenta sem os espacos
			title = ReportUtil.removeEspaco(title);
			nomeRelatorio = ReportUtil.removeEspaco(nomeRelatorio);

			if((title.toUpperCase().indexOf(nomeRelatorio.toUpperCase()) > -1) || (title.toUpperCase().trim().indexOf(nomeRelatorio.toUpperCase()) > -1) ){
				return true;
			}
		}
		return false;
	}	

	public static String getNomeRelatorio(String str){
		if (str.indexOf("-") > -1){
			str = str.substring(str.indexOf("-")+1, str.length() );
		}
		return str.trim();
	}


	public static String removeAcentos(String acentuada) {
		return Normalizer.normalize(acentuada, Normalizer.Form.NFKD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}  

	public static String removeExtensaoJrxml(String fileName){
		return StringUtils.remove(fileName, JASPER_SOURCE_EXTENSION);
	}

	public static  String removeEspaco(final String titulo){
		if(titulo.indexOf("-") > -1){
			String aux = titulo.substring(0, titulo.indexOf("-") );
			return aux.replaceAll(" ","").trim() + titulo.substring( titulo.indexOf("-") , titulo.length() );
		}else{
			return titulo.replaceAll(" ","");
		}
	}

	public static boolean isReportLink(final String isLinked){
		return (isLinked!=null && Boolean.valueOf(isLinked));
	}

	private static Map<String,JasperDesign> compileReports(String sourceFolder, String destFolder, final boolean force){
        Map<String,JasperDesign> relatoriosCompilados = new Hashtable<String,JasperDesign>();
	    
        final File[] files = new File(sourceFolder).listFiles((FilenameFilter)FileFilterUtils.suffixFileFilter(JASPER_SOURCE_EXTENSION));
        
        if (!ArrayUtils.isEmpty(files)) {
            for (File file : files) {
                try {
                    final JasperDesign report = JRXmlLoader.load(new FileInputStream(file));

                    //compila os relatorios verificando se o jrxml Ã© mais novo que o jasper ou se force=true
                    final String nomeRelatorioCompilado=FilenameUtils.concat(destFolder ,FilenameUtils.removeExtension(file.getName())  + JASPER_COMPILED_EXTENSION);
                    final File relatorioCompilado = new File(nomeRelatorioCompilado);
                    if(   (!relatorioCompilado.exists()) || (file.lastModified() > relatorioCompilado.lastModified()) || force ){
                        JasperCompileManager.compileReportToFile(report, relatorioCompilado.getAbsolutePath());
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Falha na leitura ou compicao do relatorio " + file.getName() + " " +e.getLocalizedMessage(),e); 
                }
            }
        }
        
        return relatoriosCompilados;
	}

    private static final String REPORTS_FOLDER_SOURCE="WEB-INF/rpt/jrxml";
    private static final String REPORTS_FOLDER_COMPILED="WEB-INF/rpt/jasper";
	
	public static void compileReports(ServletContext context){
		//compilando os relatórios do previne
		String sourcePath =  context.getRealPath(REPORTS_FOLDER_SOURCE);
		final String destPath = context.getRealPath(REPORTS_FOLDER_COMPILED);
		compileReports(sourcePath,destPath,false);
	}

	/* jasperFileName é o nome do relatório com ou sem a extensão .jrxml, vai buscar o .jasper do disco */
/*	@SuppressWarnings("deprecation")
	public static JasperPrint generateReport(String jasperFileName,Map<String, Object> parameters) throws Exception {
		//final String compiledReportFolder=WebAppUtil.getRequest().getSession().getServletContext().getRealPath(REPORTS_FOLDER_COMPILED)+ File.separator;
		//parameters.put("SUBREPORT_DIR",compiledReportFolder);

//		File fileLogo = new File(WebAppUtil.getSession(false).getServletContext().getRealPath(REPORTS_LOGO_PATH));
//		FileInputStream fileInputStreamLogo = new FileInputStream(fileLogo);
//		parameters.put("LOGONE_PREVINE_LOGOTIPO", fileInputStreamLogo);        

		//final Session session = SpringUtil.getCurrentHibernateSession();
		//parameters.put(JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION, session );
		//parameters.put(JRParameter.REPORT_CONNECTION, session.connection() );
		//JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReportFolder+removeExtensaoJrxml(jasperFileName)+JASPER_COMPILED_EXTENSION, parameters);
		//return jasperPrint;
	}

	public static JasperPrint preparaRelatorio(String fileName,
			Map<String,Object> valoresParametros) {

		final JasperPrint jasperPrint;
		try {
			jasperPrint = ReportUtil.generateReport(fileName,  valoresParametros);
		} catch (Exception e) {
			throw new RuntimeException("manutencao.de.relatorios.relatorio.relatorio.falha.geracao: "+e.getLocalizedMessage(),e);
		}

		return jasperPrint;
	}
*/
	private static final String MIME_TYPE_PDF="application/pdf";
	
	public static void exportarRelatorio(final JasperPrint jasperPrint, final String downloadFileName, final HttpServletResponse response) throws Exception{
		response.setContentType(MIME_TYPE_PDF);
        final OutputStream output = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, output);    
        output.flush();
        output.close();
        
		response.setHeader("Content-disposition", "attachment; filename=" + downloadFileName );
	    //WebAppUtil.getFacesContext().responseComplete();
	}
	
/*	public static void exportarRelatorio(final String jasperFileName, final Map<String,Object> parameters, final String downloadFileName, final HttpServletResponse response) throws Exception{
	    final JasperPrint jasperPrint=generateReport(jasperFileName, parameters);
	    exportarRelatorio(jasperPrint, downloadFileName, response);
	}
*/
}
