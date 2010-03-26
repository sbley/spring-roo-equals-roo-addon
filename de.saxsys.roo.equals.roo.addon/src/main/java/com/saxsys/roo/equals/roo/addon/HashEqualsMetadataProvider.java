package com.saxsys.roo.equals.roo.addon;

import org.springframework.roo.addon.beaninfo.BeanInfoMetadata;
import org.springframework.roo.addon.beaninfo.BeanInfoMetadataProvider;
import org.springframework.roo.classpath.PhysicalTypeIdentifier;
import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.itd.AbstractItdMetadataProvider;
import org.springframework.roo.classpath.itd.ItdTypeDetailsProvidingMetadataItem;
import org.springframework.roo.metadata.MetadataDependencyRegistry;
import org.springframework.roo.metadata.MetadataService;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.process.manager.FileManager;
import org.springframework.roo.project.Dependency;
import org.springframework.roo.project.Path;
import org.springframework.roo.project.ProjectMetadataProvider;
import org.springframework.roo.support.lifecycle.ScopeDevelopment;
import org.springframework.roo.support.util.Assert;

/**
 * @author stefan.ocke
 */
@ScopeDevelopment
public final class HashEqualsMetadataProvider extends
		AbstractItdMetadataProvider {

	public HashEqualsMetadataProvider(MetadataService metadataService,
			MetadataDependencyRegistry metadataDependencyRegistry,
			FileManager fileManager,
			BeanInfoMetadataProvider beanInfoMetadataProvider,
			ProjectMetadataProvider projectMetadataProvider) {
		super(metadataService, metadataDependencyRegistry, fileManager);
		Assert.notNull(beanInfoMetadataProvider,
				"Bean info metadata provider required");
		Assert.notNull(projectMetadataProvider,
				"Project metadata provider required");
		beanInfoMetadataProvider.addMetadataTrigger(new JavaType(
				RooHashEquals.class.getName()));
		addMetadataTrigger(new JavaType(RooHashEquals.class.getName()));

		// TODO is this the right place to add a dependency? [SB]
		addCommonsLangToClasspath(projectMetadataProvider);
	}

	protected ItdTypeDetailsProvidingMetadataItem getMetadata(
			String metadataIdentificationString, JavaType aspectName,
			PhysicalTypeMetadata governorPhysicalTypeMetadata,
			String itdFilename) {
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

		if (beanInfoMetadata == null) {
			return null;
		}

		// Create the metadata
		return new HashEqualsMetadata(metadataIdentificationString, aspectName,
				governorPhysicalTypeMetadata, beanInfoMetadata);
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
			final ProjectMetadataProvider projectMetadataProvider) {
		Dependency dependency = new Dependency("org.apache.commons",
				"com.springsource.org.apache.commons.lang", "2.4.0");
		projectMetadataProvider.addDependency(dependency);
	}
}
