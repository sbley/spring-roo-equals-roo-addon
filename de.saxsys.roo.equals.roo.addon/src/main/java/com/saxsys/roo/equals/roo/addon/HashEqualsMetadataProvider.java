package com.saxsys.roo.equals.roo.addon;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;
import org.springframework.roo.addon.beaninfo.BeanInfoMetadata;
import org.springframework.roo.addon.beaninfo.BeanInfoMetadataProvider;
import org.springframework.roo.classpath.PhysicalTypeIdentifier;
import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.itd.AbstractItdMetadataProvider;
import org.springframework.roo.classpath.itd.ItdTypeDetailsProvidingMetadataItem;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.project.Dependency;
import org.springframework.roo.project.Path;
import org.springframework.roo.project.ProjectOperations;

/**
 * @author stefan.ocke
 */
@Component(immediate = true)
@Service
public final class HashEqualsMetadataProvider extends
		AbstractItdMetadataProvider {
	@Reference
	private ProjectOperations projectOperations;
	// TODO do we actually need BeanInfoMetadataProvider? [SB]
	@Reference
	private BeanInfoMetadataProvider beanInfoMetadataProvider;

	protected void activate(ComponentContext context) {
		metadataDependencyRegistry.registerDependency(PhysicalTypeIdentifier
				.getMetadataIdentiferType(), getProvidesType());
		addMetadataTrigger(new JavaType(RooHashEquals.class.getName()));
	}

	public HashEqualsMetadataProvider() {
		// beanInfoMetadataProvider.addMetadataTrigger(new JavaType(
		// RooHashEquals.class.getName()));
	}

	protected ItdTypeDetailsProvidingMetadataItem getMetadata(
			String metadataIdentificationString, JavaType aspectName,
			PhysicalTypeMetadata governorPhysicalTypeMetadata,
			String itdFilename) {

		addCommonsLangToClasspath(projectOperations);

		// Acquire bean info
		JavaType javaType = HashEqualsMetadata
				.getJavaType(metadataIdentificationString);
		Path path = HashEqualsMetadata.getPath(metadataIdentificationString);
		String beanInfoMetadataKey = BeanInfoMetadata.createIdentifier(
				javaType, path);

		// We want to be notified if bean info metadata changes in any way
		metadataDependencyRegistry.registerDependency(beanInfoMetadataKey,
				metadataIdentificationString);
		BeanInfoMetadata beanInfoMetadata = (BeanInfoMetadata) metadataService
				.get(beanInfoMetadataKey);

		// TODO do we need beanInfoMetadata? [SB]
		if (beanInfoMetadata == null) {
			return null;
		}

		// Create the metadata
		return new HashEqualsMetadata(metadataIdentificationString, aspectName,
				governorPhysicalTypeMetadata);
	}

	public String getItdUniquenessFilenameSuffix() {
		return "HashEquals";
	}

	protected String getGovernorPhysicalTypeIdentifier(
			String metadataIdentificationString) {
		JavaType javaType = HashEqualsMetadata
				.getJavaType(metadataIdentificationString);
		Path path = HashEqualsMetadata.getPath(metadataIdentificationString);
		String physicalTypeIdentifier = PhysicalTypeIdentifier
				.createIdentifier(javaType, path);
		return physicalTypeIdentifier;
	}

	protected String createLocalIdentifier(JavaType javaType, Path path) {
		return HashEqualsMetadata.createIdentifier(javaType, path);
	}

	public String getProvidesType() {
		return HashEqualsMetadata.getMetadataIdentiferType();
	}

	protected void addCommonsLangToClasspath(
			final ProjectOperations projectOperations) {
		Dependency dependency = new Dependency("org.apache.commons",
				"com.springsource.org.apache.commons.lang", "2.4.0");
		projectOperations.dependencyUpdate(dependency);
	}
}
