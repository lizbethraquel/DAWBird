package dawbird;
import java.io.BufferedReader;
import java.io.IOException;

public class ReadingFromStream extends Thread{

private BufferedReader input;

public ReadingFromStream (BufferedReader in){
    this.input= in;

}
@Override
public void run() {
    String st = "";
    try {
                String line;
                while ((line = input.readLine()) != null) {
                    System.out.println( line);
                    if (line.equals("exit")) {
                        break;
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading from client: " + e.getMessage());
            }
}
}
