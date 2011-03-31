package de.saxsys.roo.equals.addon;

import java.util.logging.Logger;

import org.springframework.roo.classpath.PhysicalTypeIdentifierNamingUtils;
import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.details.MemberFindingUtils;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadata;
import org.springframework.roo.classpath.details.annotations.populator.AutoPopulate;
import org.springframework.roo.classpath.details.annotations.populator.AutoPopulationUtils;
import org.springframework.roo.classpath.itd.AbstractItdTypeDetailsProvidingMetadataItem;
import org.springframework.roo.metadata.MetadataIdentificationUtils;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.project.Path;
import org.springframework.roo.support.logging.HandlerUtils;
import org.springframework.roo.support.style.ToStringCreator;
import org.springframework.roo.support.util.Assert;

/**
 * Metadata for {@link RooEquals}.
 * 
 * @author stefan.ocke
 * @author stefan.bley
 * @since 1.0
 * 
 */
public class EqualsMetadata extends AbstractItdTypeDetailsProvidingMetadataItem {

	protected Logger logger = HandlerUtils.getLogger(getClass());
	private static final String PROVIDES_TYPE_STRING = EqualsMetadata.class
			.getName();
	private static final String PROVIDES_TYPE = MetadataIdentificationUtils
			.create(PROVIDES_TYPE_STRING);

	/** append super equals and hashCode method calls */
	@AutoPopulate
	private boolean callSuper;
	/** use instanceof rather than getClass() */
	@AutoPopulate
	private boolean callInstanceof;

	/** only include fields annotated by {@link EqualsInclude} */
	@AutoPopulate
	private boolean exclusiveMode;
	
	public boolean isCallSuper() {
		return callSuper;
	}

	public boolean isCallInstanceof() {
		return callInstanceof;
	}

	public boolean isExclusiveMode() {
		return exclusiveMode;
	}

	public EqualsMetadata(String identifier, JavaType aspectName,
			PhysicalTypeMetadata governorPhysicalTypeMetadata) {
		super(identifier, aspectName, governorPhysicalTypeMetadata);

		Assert.isTrue(isValid(identifier), "Metadata identification string '"
				+ identifier + "' does not appear to be a valid");

		if (!isValid()) {
			return;
		}

		// Process values from the annotation, if present
		AnnotationMetadata annotation = MemberFindingUtils
				.getDeclaredTypeAnnotation(governorTypeDetails, new JavaType(
						RooEquals.class.getName()));
		if (annotation != null) {
			AutoPopulationUtils.populate(this, annotation);
		}

		// Generate equals methods
		new EqualsMethodBuilder().buildMethods(builder, this, governorTypeDetails);

		itdTypeDetails = builder.build();
	}

	public String toString() {
		ToStringCreator tsc = new ToStringCreator(this);
		tsc.append("identifier", getId());
		tsc.append("valid", valid);
		tsc.append("aspectName", aspectName);
		tsc.append("destinationType", destination);
		tsc.append("governor", governorPhysicalTypeMetadata.getId());
		tsc.append("itdTypeDetails", itdTypeDetails);
		return tsc.toString();
	}

	public static final String getMetadataIdentiferType() {
		return PROVIDES_TYPE;
	}

	public static final String createIdentifier(JavaType javaType, Path path) {
		return PhysicalTypeIdentifierNamingUtils.createIdentifier(
				PROVIDES_TYPE_STRING, javaType, path);
	}

	public static final JavaType getJavaType(String metadataIdentificationString) {
		return PhysicalTypeIdentifierNamingUtils.getJavaType(
				PROVIDES_TYPE_STRING, metadataIdentificationString);
	}

	public static final Path getPath(String metadataIdentificationString) {
		return PhysicalTypeIdentifierNamingUtils.getPath(PROVIDES_TYPE_STRING,
				metadataIdentificationString);
	}

	public static boolean isValid(String metadataIdentificationString) {
		return PhysicalTypeIdentifierNamingUtils.isValid(PROVIDES_TYPE_STRING,
				metadataIdentificationString);
	}
	
}
