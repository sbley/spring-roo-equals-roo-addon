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
	private static final JavaSymbolName EXCLUSIVEMODE = new JavaSymbolName(
			"exclusiveMode");

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
			final Boolean callInstanceOf, final Boolean exclusiveMode) {
		addDependencies();
		addEqualsAnnotation(typeName, callSuper, callInstanceOf, exclusiveMode);
	}

	/**
	 * Adds the dependency on the annotation jar and on required libraries to
	 * the project's pom.xml.
	 * <p>
	 * Additionally, it inserts the repository which hosts the annotation jar
	 * into the pom.
	 * </p>
	 */
	private void addDependencies() {
		try {
			Element configuration = XmlUtils.getConfiguration(getClass());

			// add dependencies
			List<Element> dependencies = XmlUtils.findElements(
					"/configuration/dependencies/dependency", configuration);
			for (Element dependencyElement : dependencies) {
				Dependency dependency = new Dependency(dependencyElement);
				projectOperations.addDependency(dependency);
			}

			// add repository
			List<Element> repositories = XmlUtils.findElements(
					"/configuration/repositories/repository", configuration);
			for (Element repositoryElement : repositories) {
				Repository repository = new Repository(repositoryElement);
					projectOperations.addRepository(repository);
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
	 */
	private void addEqualsAnnotation(final JavaType typeName,
			Boolean callSuper, Boolean callInstanceOf, Boolean exclusiveMode) {
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

		PhysicalTypeDetails ptd = physicalTypeMetadata.getMemberHoldingTypeDetails();
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
		if (exclusiveMode != null) {
			attributes.add(new BooleanAttributeValue(EXCLUSIVEMODE, exclusiveMode));
		}
		AnnotationMetadata annotation = new AnnotationMetadataBuilder(
				annotationType, attributes).build();
		mutable.addTypeAnnotation(annotation);
	}
}