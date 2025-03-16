import java.io.*;
import java.text.DecimalFormat;

public class App {
    public static void main(String[] args) {
        String filename = "arquivos\\ordExt_teste.txt";
        String outputFile = "arquivos\\resposta.txt";
        Btree btree = new Btree(5);

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {

                try {
                    double num = Double.parseDouble(line);
                    btree.insert(Double.valueOf(num));
                } catch (NumberFormatException e) {
                    System.err.println("Erro ao converter: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
            for (Double num : btree.getSortedElements()) {
                DecimalFormat df = new DecimalFormat("#.##################");
                writer.println(df.format(num));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}