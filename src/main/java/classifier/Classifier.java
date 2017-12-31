package classifier;
import com.github.jfasttext.JFastText;

import java.io.*;
import java.util.Scanner;


public class Classifier {
    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(new FileReader("news_train.txt"));
             PrintWriter writer = new PrintWriter(new FileWriter("news_train_labels.txt"))){
            while (scanner.hasNextLine()) {
                String curLine = new StringBuilder("__label__").append(scanner.nextLine()).append('\n').toString();
                writer.write(curLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JFastText jft = new JFastText();

        jft.runCmd(new String[] {
                "supervised",
                "-input", "news_train_labels.txt",
                "-output", "supervised.model"
        });

        // Load model from file
        jft.loadModel("supervised.model.bin");

        // Do label prediction
        try (Scanner scanner = new Scanner(new FileReader("news_test.txt"));
             PrintWriter writer = new PrintWriter(new FileWriter("output.txt"))){
            while (scanner.hasNextLine()) {
                String curLine = scanner.nextLine();
                JFastText.ProbLabel probLabel = jft.predictProba(curLine);
                writer.write(probLabel.label.substring(9) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}