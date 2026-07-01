package de.knittingapp.backend;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.knittingapp.dto.KnittingProject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class KnittingProjectDaoJson implements KnittingProjectDao {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(KnittingProject.class, new KnittingProjectTypeAdapter())
            .setPrettyPrinting()
            .create();

    public KnittingProjectDaoJson(){
    }

    @Override
    public KnittingProject loadProject(Path filePath) {
        KnittingProject knittingProject;
        try {
            String json = Files.readString(filePath);
            knittingProject = gson.fromJson(json,KnittingProject.class);
            return knittingProject;
        } catch (IOException exception) {
            exception.printStackTrace();
            return new KnittingProject();
        }
    }

    @Override
    public void saveProject(KnittingProject knittingProject, Path filePath) {
        try{
            Files.createDirectories(filePath.getParent());

            String json = gson.toJson(knittingProject);
            Files.writeString(filePath, json);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
