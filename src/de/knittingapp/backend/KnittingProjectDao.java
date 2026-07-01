package de.knittingapp.backend;

import de.knittingapp.dto.KnittingPattern;
import de.knittingapp.dto.KnittingProject;

import java.nio.file.Path;

/**
 * <pre>
 *     "standardized" access to backend for loading KnittingProject object from and saving KnittingProject object to file
 * </pre>
 */
public interface KnittingProjectDao {

    KnittingProject loadProject(Path path);

    void saveProject(KnittingProject knittingProject, Path path);


}
