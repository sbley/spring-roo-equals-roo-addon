package de.saxsys.roo.equals.addon;

import java.util.ArrayList;
import java.util.List;
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
import org.springframework.roo.classpath.details.annotations.AnnotationMetadataBuilder;
import org.springframework.roo.classpath.details.annotations.BooleanAttributeValue;
import org.springframework.roo.metadata.MetadataService;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.project.Dependency;
import org.springframework.roo.project.ProjectMetadata;
import org.springframework.roo.project.ProjectOperations;
import org.springframework.roo.project.Repository;
import org.springframework.roo.support.logging.HandlerUtils;
import org.springframework.roo.support.util.Assert;
import org.springframework.roo.support.util.XmlUtils;
import org.w3c.dom.Element;

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

	private static final JavaSymbolName CALLSUPER = new JavaSymbolName(
			"callSuper");

	private static final JavaSymbolName CALLINSTANCEOF = new JavaSymbolName(
			"callInstanceof");

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

	public void addEquals(final JavaType typeName, final Boolean callSuper,
			final Boolean callInstanceOf) {
		addDependencies();
		addEqualsAnnotation(typeName, callSuper, callInstanceOf);
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
		// String repoId = "";
		// String repoName = "Repository for Roo equals addon";
		// String repoUrl = "";
		// String annotationsGroupId = "";
		// String annotationsArtifactId = "";
		// String annotationsVersion = "";
		// InputStream inputStream = getClass().getClassLoader()
		// .getResourceAsStream("dependencies.properties");
		try {
			// Properties properties = new Properties();
			// properties.load(inputStream);
			// annotationsGroupId =
			// properties.getProperty("annotations.groupId");
			// annotationsArtifactId = properties
			// .getProperty("annotations.artifactId");
			// annotationsVersion =
			// properties.getProperty("annotations.version");
			// boolean isSnapshot = annotationsVersion.endsWith("SNAPSHOT");
			// if (isSnapshot) {
			// repoId = properties.getProperty("annotations.snapshotrepo.id");
			// repoUrl = properties
			// .getProperty("annotations.snapshotrepo.url");
			// } else {
			// repoId = properties.getProperty("annotations.repo.id");
			// repoUrl = properties.getProperty("annotations.repo.url");
			// }
			// // String commonslangVersion = properties
			// // .getProperty("commonslang.version");
			//
			// projectOperations.addRepository(new Repository(repoId, repoName,
			// repoUrl, isSnapshot));
			//
			// Dependency dependencyAnno = new Dependency(annotationsGroupId,
			// annotationsArtifactId, annotationsVersion);
			// projectOperations.dependencyUpdate(dependencyAnno);
			// Dependency dependencyCmnLang = new Dependency("commons-lang",
			// "commons-lang", commonslangVersion);
			// projectOperations.dependencyUpdate(dependencyCmnLang);

			Element configuration = XmlUtils.getConfiguration(getClass());
			boolean snapshot = false;

			// add dependencies
			List<Element> dependencies = XmlUtils.findElements(
					"/configuration/dependencies/dependency", configuration);
			for (Element dependencyElement : dependencies) {
				Dependency dependency = new Dependency(dependencyElement);
				projectOperations.dependencyUpdate(dependency);

				// WORKAROUND:
				// We have to check for SNAPSHOT dependency of annotation
				// project
				// because Roo 1.1.0.M2 still does not add snapshot tag to the
				// repository
				// section in the pom.
				if ("de.saxsys.roo.equals.annotations".equals(dependency
						.getArtifactId().getSymbolName())) {
					snapshot = dependency.getVersionId().endsWith("SNAPSHOT");
				}
			}

			// add repository
			List<Element> repositories = XmlUtils.findElements(
					"/configuration/repositories/repository", configuration);
			for (Element repositoryElement : repositories) {
				Repository repository = new Repository(repositoryElement);
				// WORKAROUND:
				// See above.
				if (repository.isEnableSnapshots() == snapshot) {
					projectOperations.addRepository(repository);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds the annotation {@link RooEquals} to the class declaration.
	 * 
	 * @param typeName
	 *            a class
	 * @param callInstanceOf
	 * @param callSuper
	 */
	private void addEqualsAnnotation(final JavaType typeName,
			Boolean callSuper, Boolean callInstanceOf) {
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
			logger.warning("Cannot provide equals because '"
					+ typeName.getFullyQualifiedTypeName() + "' is unavailable");
			return;
		}

		PhysicalTypeDetails ptd = physicalTypeMetadata.getPhysicalTypeDetails();
		Assert.isInstanceOf(MutableClassOrInterfaceTypeDetails.class, ptd);
		MutableClassOrInterfaceTypeDetails mutable = (MutableClassOrInterfaceTypeDetails) ptd;

		JavaType annotationType = new JavaType(RooEquals.class.getName());
		// remove the old annotation
		List<? extends AnnotationMetadata> annotations = mutable
				.getAnnotations();

		AnnotationMetadata found = MemberFindingUtils.getAnnotationOfType(
				annotations, annotationType);
		if (found != null) {
			mutable.removeTypeAnnotation(annotationType);
		}

		// add the new annotation
		List<AnnotationAttributeValue<?>> attributes = new ArrayList<AnnotationAttributeValue<?>>();
		if (callSuper != null) {

			attributes.add(new BooleanAttributeValue(CALLSUPER, callSuper));
		}
		if (callInstanceOf != null) {

			attributes.add(new BooleanAttributeValue(CALLINSTANCEOF,
					callInstanceOf));
		}
		AnnotationMetadata annotation = new AnnotationMetadataBuilder(
				annotationType, attributes).build();
		mutable.addTypeAnnotation(annotation);
	}
}