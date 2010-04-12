package de.saxsys.roo.equals.addon;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.metadata.MetadataService;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.process.manager.FileManager;
import org.springframework.roo.process.manager.MutableFile;
import org.springframework.roo.project.Dependency;
import org.springframework.roo.project.Path;
import org.springframework.roo.project.PathResolver;
import org.springframework.roo.project.ProjectMetadata;
import org.springframework.roo.project.ProjectMetadataProvider;
import org.springframework.roo.project.ProjectOperations;
import org.springframework.roo.project.Repository;
import org.springframework.roo.support.logging.HandlerUtils;
import org.springframework.roo.support.util.Assert;
import org.springframework.roo.support.util.FileCopyUtils;

/**
 * Implementation of commands that are available via the Roo shell.
 * 
 * @author stefan.bley
 * @since 1.0
 */
@Component
@Service
public class EqualsOperationsImpl implements EqualsOperations {

	private final Logger logger = HandlerUtils.getLogger(getClass());

	@Reference
	private MetadataService metadataService;

	@Reference
	private ProjectOperations projectOperations;

	@Reference
	ProjectMetadataProvider projectMetadataProvider;

	public boolean isProjectAvailable() {
		return getProjectMetadata() != null;
	}

	/**
	 * @return the project metadata or null if there is no user project
	 */
	ProjectMetadata getProjectMetadata() {
		ProjectMetadata projectMetadata = (ProjectMetadata) metadataService
				.get(ProjectMetadata.getProjectIdentifier());
		return projectMetadata;
	}

	public void addEquals(JavaType typeName) {

		String repoId =	"googlecode.rooequals.snapshotrepo";
		String repoName= "SNAPSHOT repository for Roo equals addon";
		String repoUrl= "https://spring-roo-equals-roo-addon.googlecode.com/svn/maven-snapshot-repository/";

		projectOperations.addRepository(repoId, repoName, repoUrl);

		Dependency dependencyAnno = new Dependency("de.saxsys.roo", "de.saxsys.roo.equals.annotations",
				"1.0.0-SNAPSHOT");
		projectOperations.dependencyUpdate(dependencyAnno);

		Dependency dependencyCmnLang = new Dependency("commons-lang", "commons-lang",
				"2.5");
		projectOperations.dependencyUpdate(dependencyCmnLang );

		logger.severe(typeName.toString());

	}

}