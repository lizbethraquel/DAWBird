package dawbird;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;

import javax.swing.JTextArea;

public class ReadingFromStream2 extends Thread{
	   private BufferedReader input;
	   private JTextArea textArea;
	   public ReadingFromStream2(BufferedReader input,JTextArea textArea) {
		   this.input = input;
	        this.textArea = textArea;
	   }

	@Override
    public void run() {
        try {
            String line;
            while ((line = input.readLine()) != null) {
                textArea.append( line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
