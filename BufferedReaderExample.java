import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BufferedReaderExample {

    private BufferedReader br;
    int numLinea;

    public BufferedReaderExample() {
        br = null;
    }

    public void checkText(String archivo) {
        try {
            numLinea = 0;
            String sCurrentLine;
            br = new BufferedReader(new FileReader(archivo));
            while ((sCurrentLine = br.readLine()) != null) {
                numLinea++;
                System.out.println(numLinea + " " + sCurrentLine);
                if (numLinea == 2 && sCurrentLine.matches("A|G|T|C")) {
                    System.out.println("La cadena de nucleótidos contiene elementos no válidos");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
	public static void main(String[] args) {
    BufferedReaderExample br = new BufferedReaderExample();
		br.checkText("text");
	}
}
