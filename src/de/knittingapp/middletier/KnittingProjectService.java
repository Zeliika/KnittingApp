package de.knittingapp.middletier;

import de.knittingapp.backend.KnittingProjectDao;
import de.knittingapp.backend.KnittingProjectDaoJson;
import de.knittingapp.dto.KnittingProject;

import java.io.File;
import java.nio.file.Path;

/**
 * <pre>
 *     communication between frontend and backend
 *     responsible for initializing saving to and loading from Json file by backend based on path provided by frontend
 * </pre>
 */
public class KnittingProjectService {

    private final KnittingProjectDaoJson knittingProjectDaoJson;

    public KnittingProjectService() {
        knittingProjectDaoJson = new KnittingProjectDaoJson();
    }

    /**
     * gets KnittingProject object from frontend and path for saving from user
     * calls save method of KnittingProjectDao
     * @param knittingProject knittingProject for saving
     * @param path path to file location
     */
    public void save(KnittingProject knittingProject, File path){
        Path filePath = Path.of(path.getAbsolutePath());
        knittingProjectDaoJson.saveProject(knittingProject, filePath);
    }

    /**
     * gets KnittingProject object loaded from file from backend fo frontend access
     * @param path path to file location
     * @return KnittingProject object created from file by backend
     */
    public KnittingProject getKnittingProject(File path){
        Path filePath = Path.of(path.getAbsolutePath());
        return knittingProjectDaoJson.loadProject(filePath);
    }
}
