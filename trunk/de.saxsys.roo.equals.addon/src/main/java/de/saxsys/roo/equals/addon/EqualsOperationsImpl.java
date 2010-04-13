package de.saxsys.roo.equals.addon;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.classpath.PhysicalTypeDetails;
import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.PhysicalTypeMetadataProvider;
import org.springframework.roo.classpath.details.MemberFindingUtils;
import org.springframework.roo.classpath.details.MutableClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.annotations.AnnotationAttributeValue;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadata;
import org.springframework.roo.classpath.details.annotations.DefaultAnnotationMetadata;
import org.springframework.roo.metadata.MetadataService;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.project.Dependency;
import org.springframework.roo.project.ProjectMetadata;
import org.springframework.roo.project.ProjectOperations;
import org.springframework.roo.support.logging.HandlerUtils;
import org.springframework.roo.support.util.Assert;

/**
 * Implementation of commands that are available via the Roo shell.
 * 
 * @author stefan.bley
 * @author stefan.ocke
 * @since 1.0.0
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
	private PhysicalTypeMetadataProvider physicalTypeMetadataProvider;

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

	public void addEquals(final JavaType typeName) {
		addDependencies();
		addEqualsAnnotation(typeName);
	}

	/**
	 * Adds the dependency to the annotation jar and to required libraries to
	 * the project's pom.xml.
	 * <p>
	 * Additionally, it inserts the repository which hosts the annotation jar
	 * into the pom.
	 * </p>
	 */
	private void addDependencies() {
		String repoId = "";
		String repoName = "Repository for Roo equals addon";
		String repoUrl = "";
		String annotationsGroupId = "";
		String annotationsArtifactId = "";
		String annotationsVersion = "";
		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream("dependencies.properties");
		try {
			Properties properties = new Properties();
			properties.load(inputStream);
			annotationsGroupId = properties.getProperty("annotations.groupId");
			annotationsArtifactId = properties
					.getProperty("annotations.artifactId");
			annotationsVersion = properties.getProperty("annotations.version");
			if (annotationsVersion.endsWith("SNAPSHOT")) {
				repoId = properties.getProperty("annotations.snapshotrepo.id");
				repoUrl = properties
						.getProperty("annotations.snapshotrepo.url");
			} else {
				repoId = properties.getProperty("annotations.repo.id");
				repoUrl = properties.getProperty("annotations.repo.url");
			}
			String commonslangVersion = properties
					.getProperty("commonslang.version");

			projectOperations.addRepository(repoId, repoName, repoUrl);

			Dependency dependencyAnno = new Dependency(annotationsGroupId,
					annotationsArtifactId, annotationsVersion);
			projectOperations.dependencyUpdate(dependencyAnno);

			Dependency dependencyCmnLang = new Dependency("commons-lang",
					"commons-lang", commonslangVersion);
			projectOperations.dependencyUpdate(dependencyCmnLang);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds the annotation {@link RooEquals} to the class declaration.
	 * 
	 * @param typeName
	 *            a class
	 */
	private void addEqualsAnnotation(final JavaType typeName) {
		String id = physicalTypeMetadataProvider.findIdentifier(typeName);
		if (id == null) {
			logger.warning("Cannot locate source for '"
					+ typeName.getFullyQualifiedTypeName() + "'");
			return;
		}

		PhysicalTypeMetadata physicalTypeMetadata = (PhysicalTypeMetadata) metadataService
				.get(id);
		if (physicalTypeMetadata == null) {
			// For some reason we found the source file a few lines ago, but
			// suddenly it has gone away
			logger
					.warning("Cannot provide equals because '"
							+ typeName.getFullyQualifiedTypeName()
							+ "' is unavailable");
			return;
		}

		PhysicalTypeDetails ptd = physicalTypeMetadata.getPhysicalTypeDetails();
		Assert.isInstanceOf(MutableClassOrInterfaceTypeDetails.class, ptd);
		MutableClassOrInterfaceTypeDetails mutable = (MutableClassOrInterfaceTypeDetails) ptd;

		JavaType annotationType = new JavaType(RooEquals.class.getName());
		// remove the old annotation
		List<? extends AnnotationMetadata> annotations = mutable
				.getTypeAnnotations();

		AnnotationMetadata found = MemberFindingUtils.getAnnotationOfType(
				annotations, annotationType);
		if (found != null) {
			mutable.removeTypeAnnotation(annotationType);
		}

		// add the new annotation
		List<AnnotationAttributeValue<?>> attributes = new ArrayList<AnnotationAttributeValue<?>>();
		AnnotationMetadata annotation = new DefaultAnnotationMetadata(
				annotationType, attributes);
		mutable.addTypeAnnotation(annotation);
	}
}