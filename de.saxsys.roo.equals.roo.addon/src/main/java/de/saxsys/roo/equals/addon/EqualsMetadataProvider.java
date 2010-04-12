package de.saxsys.roo.equals.addon;

import java.util.logging.Logger;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;
import org.springframework.roo.classpath.PhysicalTypeIdentifier;
import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.itd.AbstractItdMetadataProvider;
import org.springframework.roo.classpath.itd.ItdTypeDetailsProvidingMetadataItem;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.project.Dependency;
import org.springframework.roo.project.Path;
import org.springframework.roo.project.ProjectOperations;
import org.springframework.roo.support.logging.HandlerUtils;

/**
 * @author stefan.ocke
 */
@Component(immediate = true)
@Service
public final class EqualsMetadataProvider extends AbstractItdMetadataProvider {

	protected final Logger logger = HandlerUtils.getLogger(getClass());

	@Reference
	private ProjectOperations projectOperations;

	protected void activate(ComponentContext context) {
		metadataDependencyRegistry.registerDependency(PhysicalTypeIdentifier
				.getMetadataIdentiferType(), getProvidesType());
		addMetadataTrigger(new JavaType(RooEquals.class.getName()));
	}

	protected ItdTypeDetailsProvidingMetadataItem getMetadata(
			String metadataIdentificationString, JavaType aspectName,
			PhysicalTypeMetadata governorPhysicalTypeMetadata,
			String itdFilename) {

		addCommonsLangToClasspath(projectOperations);

		// Create the metadata
		return new EqualsMetadata(metadataIdentificationString, aspectName,
				governorPhysicalTypeMetadata);
	}

	public String getItdUniquenessFilenameSuffix() {
		return "Equals";
	}

	protected String getGovernorPhysicalTypeIdentifier(
			String metadataIdentificationString) {
		JavaType javaType = EqualsMetadata
				.getJavaType(metadataIdentificationString);
		Path path = EqualsMetadata.getPath(metadataIdentificationString);
		String physicalTypeIdentifier = PhysicalTypeIdentifier
				.createIdentifier(javaType, path);
		return physicalTypeIdentifier;
	}

	protected String createLocalIdentifier(JavaType javaType, Path path) {
		return EqualsMetadata.createIdentifier(javaType, path);
	}

	public String getProvidesType() {
		return EqualsMetadata.getMetadataIdentiferType();
	}

	protected void addCommonsLangToClasspath(
			final ProjectOperations projectOperations) {
		Dependency dependency = new Dependency("commons-lang", "commons-lang",
				"2.5");
		projectOperations.dependencyUpdate(dependency);
	}
}
