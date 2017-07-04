package generatePropertiesfromXhtml;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Main {

	public static void main(String[] args) {
		String fileOut = "CopagoCups";

		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("XHTML", "xhtml");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
		}

		String file = chooser.getSelectedFile().getAbsolutePath();

		Archivo ar = new Archivo(file, "msg-" + fileOut + ".properties", "msg" + fileOut);
	}

}
