package test;

import de.knittingapp.backend.KnittingProjectDao;
import de.knittingapp.backend.KnittingProjectDaoJson;
import de.knittingapp.dto.KnittingInstruction;
import de.knittingapp.dto.KnittingPattern;
import de.knittingapp.dto.KnittingProject;
import de.knittingapp.middletier.KnittingProjectService;

public class KnittingProjectDaoJsonTest {
    public static void main(String[] args) {
        KnittingProject test = new KnittingProject();
        test.setTitle("Project");
        test.setDescription("Test");
        KnittingPattern pattern = new KnittingPattern(10,10,1);
        pattern.setName("Pattern");
        pattern.setInstructions("Explanation");
        KnittingInstruction instruction = new KnittingInstruction("Test", "These are instructions");
        test.addKnittingInstruction(instruction);
        test.addKnittingInstruction(pattern);

        KnittingProjectService knittingProjectService = new KnittingProjectService();
        String path = "C:\\Users\\zelii\\IdeaProjects\\KnittingApp\\.json\\test2.json";
//        knittingProjectService.save(test, path);

//        KnittingProject readingTest = knittingProjectService.getKnittingProject(path);
//        System.out.println(test);
//        System.out.println(readingTest);
//        knittingProjectService.save(readingTest, "C:\\Users\\zelii\\IdeaProjects\\KnittingApp\\.json\\test3.json");
    }
}
