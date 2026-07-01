package test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.knittingapp.dto.KnittingInstruction;
import de.knittingapp.dto.KnittingPattern;
import de.knittingapp.dto.KnittingProject;
import de.knittingapp.backend.KnittingProjectTypeAdapter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class KnittingProjectTest {
    public static void main(String[] args) {
        KnittingProject test = new KnittingProject();
        test.setTitle("Project");
        test.setDescription("Test");
        KnittingPattern pattern = new KnittingPattern(10,10,1);
        pattern.setName("Pattern");
        pattern.setInstructions("Explanation");
        System.out.println(pattern);
        KnittingInstruction instruction = new KnittingInstruction("Test", "These are instructions");
        System.out.println(instruction);
        test.addKnittingInstruction(instruction);
        test.addKnittingInstruction(pattern);
        System.out.println(test);

        Gson gson = new GsonBuilder().registerTypeAdapter(KnittingProject.class, new KnittingProjectTypeAdapter()).setPrettyPrinting().create();
        String toJson = gson.toJson(test);
        Path path = Path.of(".json", "test.json");
        try{
            Files.createDirectories(path.getParent());
            Files.writeString(path, toJson);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        System.out.println(toJson);
        KnittingProject fromJson = gson.fromJson(toJson, KnittingProject.class);
        System.out.println(fromJson);
//        System.out.println(test.getNextFreeId());
//        System.out.println(test.getNextFreeId());
//        System.out.println(test.getNextFreeId());
    }
}
