package generatePropertiesfromXhtml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessFile{
	private String filePath;
	private String propertiesFileName;
	private String propetiesAlias;
	
	private static final Pattern[] patrones = new Pattern[] {
		Pattern.compile(".*<h:outputText.*value=\"([^\"^#]*)\".*"),
		Pattern.compile(".*<p:commandButton.*value=\"([^\"^#]*)\".*"),
		Pattern.compile(".*<h:commandButton.*value=\"([^\"^#]*)\".*"),
		Pattern.compile(".*<h:commandlink.*value=\"([^\"^#]*)\".*"),
		Pattern.compile(".*<p:commandlink.*value=\"([^\"^#]*)\".*")
	};
	/**
	 * value-key
	 */
	private Map<String, String> propertiesContent;
	private StringBuilder newFileContent;
	
	public ProcessFile(String filePath, String propertiesFileName, String propetiesAlias){
		this.filePath = filePath;
		this.propertiesFileName = propertiesFileName;
		this.propetiesAlias = propetiesAlias;
		
		try {
			AnalyzeFile();
			writeNewFiles();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	* @author <a href="mailto:wilson.andrade@premize.com"> Wilson Fdo. Andrade C. </a>
	 * @throws IOException 
	* @date 05/04/2017
	* @description Analizar el archivo y reemplazar las coincidencias de los labels
	 */
	private void AnalyzeFile() throws IOException {
		propertiesContent = new LinkedHashMap<String, String>();
		Scanner keyboard = new Scanner(System.in);
		newFileContent = new StringBuilder();
		
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			Matcher matcher = null;
			String value = null;
			String key = null;
			Boolean addLine = null;

			String sCurrentLine = null;
			int c = 0;
			while ((sCurrentLine = br.readLine()) != null) {
				addLine = true;
				for(Pattern patron : patrones){
					matcher = patron.matcher(sCurrentLine);
					if(matcher.matches()) {
						value = matcher.group(1);
						if(!propertiesContent.containsKey(value)){
							System.out.println("Key for: "+value);
							//key = keyboard.next();
							key = "msg_"+(c++);
							propertiesContent.put(value, key);
						} else {
							key = propertiesContent.get(value); //reutilizar el key
						}
						
						newFileContent.append(new StringBuilder(sCurrentLine)
							.replace(matcher.start(1), matcher.end(1), "#{"+propetiesAlias+"."+key+"}")); //cambiar la fila en el archivo original
						newFileContent.append("\n");
						addLine = false;
						break;
					}
				}
				if(addLine){
					newFileContent.append(sCurrentLine+"\n");
				}
			}
		} catch (IOException e) {
			throw e;
		}
	}
	
	/**
	 * 
	* @author <a href="mailto:wilson.andrade@premize.com"> Wilson Fdo. Andrade C. </a>
	* @date 05/04/2017
	* @description Escribir los nuevos archivos
	* @throws IOException
	 */
	public void writeNewFiles() throws IOException{
		File myFoo = new File(propertiesFileName+"Source.xhtml");
		FileOutputStream fooStream = new FileOutputStream(myFoo, false);
		byte[] myBytes = newFileContent.toString().getBytes();
		fooStream.write(myBytes);
		fooStream.close();

		PrintWriter writer = new PrintWriter(propertiesFileName, "UTF-8");
		for(Entry<String, String> entry : propertiesContent.entrySet()){
			writer.println(entry.getValue()+"="+entry.getKey());
		}
		
	    writer.close();
	}
	
}
