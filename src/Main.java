/**
 * Created by tony on 1/17/17.
 */
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner ageInput = new Scanner(System.in);
        Path downloadsPath = Paths.get("/home/tony/Desktop/practiceFile");
        DirectoryCleaner dc = new DirectoryCleaner();

        System.out.println("Directory to clean:" + downloadsPath.getFileName());
        System.out.println("Input file age range for deletion(Unit in days)");
        int deletionCriteriaInput = ageInput.nextInt();
        dc.setDeletionCriteriaAge(deletionCriteriaInput);
        ageInput.close();

        try {
            Files.walkFileTree(downloadsPath, dc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

