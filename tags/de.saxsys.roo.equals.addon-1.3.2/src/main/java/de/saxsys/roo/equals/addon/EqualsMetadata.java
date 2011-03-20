package de.saxsys.roo.equals.addon;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.roo.classpath.PhysicalTypeIdentifierNamingUtils;
import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.details.FieldMetadata;
import org.springframework.roo.classpath.details.MemberFindingUtils;
import org.springframework.roo.classpath.details.MethodMetadata;
import org.springframework.roo.classpath.details.MethodMetadataBuilder;
import org.springframework.roo.classpath.details.annotations.AnnotatedJavaType;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadata;
import org.springframework.roo.classpath.details.annotations.populator.AutoPopulate;
import org.springframework.roo.classpath.details.annotations.populator.AutoPopulationUtils;
import org.springframework.roo.classpath.itd.AbstractItdTypeDetailsProvidingMetadataItem;
import org.springframework.roo.classpath.itd.InvocableMemberBodyBuilder;
import org.springframework.roo.metadata.MetadataIdentificationUtils;
import org.springframework.roo.model.JavaSymbolName;
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
	private static final JavaType EQUALS_BUILDER = new JavaType(
			"org.apache.commons.lang.builder.EqualsBuilder");
	private static final JavaType HASHCODE_BUILDER = new JavaType(
			"org.apache.commons.lang.builder.HashCodeBuilder");
	private static final JavaType OBJECT = new JavaType("java.lang.Object");
	private static final String EQUALS_EXCLUDE_NAME = EqualsExclude.class.getCanonicalName();
	private static final String EQUALS_INCLUDE_NAME = EqualsInclude.class.getCanonicalName();

	/** append super equals and hashCode method calls */
	@AutoPopulate
	private boolean callSuper;
	/** use instanceof rather than getClass() */
	@AutoPopulate
	private boolean callInstanceof;
	/** only include fields annotated by {@link EqualsInclude} */
	@AutoPopulate
	private boolean exclusiveMode;

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

		// Generate equals method
		MethodMetadata equalsMethod = new EqualsMethodBuilder()
				.createEqualsMethod();
		builder.addMethod(equalsMethod);

		// Generate hashCode method
		MethodMetadata hashCodeMethod = new HashCodeMethodBuilder()
				.createHashCodeMethod();
		builder.addMethod(hashCodeMethod);

		// Create a representation of the desired output ITD
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

	private boolean shouldIncludeField(FieldMetadata field) {
		if (exclusiveMode) {
			return containsAnnotation(field.getAnnotations(),
					EQUALS_INCLUDE_NAME);
		}
		return !containsAnnotation(field.getAnnotations(), EQUALS_EXCLUDE_NAME);
	}
	
	private static boolean containsAnnotation(List<AnnotationMetadata> annotationsOnField,
			String annotationName) {
		for (AnnotationMetadata annotationMetadata : annotationsOnField) {
			if (annotationMetadata.getAnnotationType()
					.getFullyQualifiedTypeName().equals(annotationName))
				return true;
		}
		return false;
	}
	
	/**
	 * Creates an {@code equals(Object)} method.
	 * 
	 * @author Stefan Bley
	 * @since 1.0
	 */
	class EqualsMethodBuilder {
		final InvocableMemberBodyBuilder bodyBuilder = new InvocableMemberBodyBuilder();
		final JavaSymbolName methodName = new JavaSymbolName("equals");
		final String simpleTypeName = governorTypeDetails.getName()
				.getSimpleTypeName();

		/**
		 * Obtains the {@code equals} method for this type, if available.
		 * 
		 * <p>
		 * If the user provided a non-default name for {@code equals}, that
		 * method will be returned.
		 * 
		 * @return the {@code equals} method declared on this type or that will
		 *         be introduced (or null if undeclared and not introduced)
		 */
		MethodMetadata createEqualsMethod() {

			// See if the type itself declared the method
			MethodMetadata result = MemberFindingUtils.getDeclaredMethod(
					governorTypeDetails, methodName,
					Collections.singletonList(OBJECT));
			if (result != null) {
				return result;
			}

			appendClassComparison();
			initEqualsBuilder();
			if (callSuper) {
				appendSuper();
			}
			appendFields();
			finishEqualsBuilder();
			return createMethodMetadata();
		}

		private InvocableMemberBodyBuilder appendClassComparison() {
			bodyBuilder
					.appendFormalLine("if (other == null) { return false; }");
			bodyBuilder.appendFormalLine("if (other == this) { return true; }");
			if (callInstanceof) {
				bodyBuilder.appendFormalLine("if (!(other instanceof "
						+ simpleTypeName + ")) {");
			} else {
				bodyBuilder
						.appendFormalLine("if (other.getClass() != getClass()) {");
			}
			bodyBuilder.indent();
			bodyBuilder.appendFormalLine("return false;");
			bodyBuilder.indentRemove();
			bodyBuilder.appendFormalLine("}");
			return bodyBuilder;
		}

		private InvocableMemberBodyBuilder initEqualsBuilder() {
			bodyBuilder.appendFormalLine(simpleTypeName + " rhs = ("
					+ simpleTypeName + ") other;");
			bodyBuilder.appendFormalLine("return new "
					+ EQUALS_BUILDER.getNameIncludingTypeParameters(false,
							builder.getImportRegistrationResolver()) + "()");
			bodyBuilder.indent();
			return bodyBuilder;
		}

		private InvocableMemberBodyBuilder appendSuper() {
			bodyBuilder.appendFormalLine(".appendSuper(super.equals(rhs))");
			return bodyBuilder;
		}

		private InvocableMemberBodyBuilder appendFields() {
			List<? extends FieldMetadata> declaredFields = governorTypeDetails
					.getDeclaredFields();
			for (FieldMetadata field : declaredFields) {
				logger.fine("FieldMetadata: " + field);
				if (!Modifier.isTransient(field.getModifier())
						&& !Modifier.isStatic(field.getModifier()) 
						&& shouldIncludeField(field)
				) {
						bodyBuilder.appendFormalLine(".append("
							+ field.getFieldName().getSymbolName() + ", rhs."
							+ field.getFieldName().getSymbolName() + ")");
					
				}
			}
			return bodyBuilder;
		}

		private InvocableMemberBodyBuilder finishEqualsBuilder() {
			bodyBuilder.appendFormalLine(".isEquals();");
			bodyBuilder.indentRemove();
			return bodyBuilder;
		}

		private MethodMetadata createMethodMetadata() {
			return new MethodMetadataBuilder(getId(), Modifier.PUBLIC,
					methodName, JavaType.BOOLEAN_PRIMITIVE,
					AnnotatedJavaType.convertFromJavaTypes(Collections
							.singletonList(OBJECT)),
					Collections.singletonList(new JavaSymbolName("other")),
					bodyBuilder).build();
		}
	}

	/**
	 * Creates a {@code hashCode()} method.
	 * 
	 * @author Stefan Bley
	 * @since 1.0
	 */
	class HashCodeMethodBuilder {
		final InvocableMemberBodyBuilder bodyBuilder = new InvocableMemberBodyBuilder();
		final JavaSymbolName methodName = new JavaSymbolName("hashCode");

		/**
		 * Obtains the {@code hashCode()} method for this type, if available.
		 * 
		 * <p>
		 * If the user provided a non-default name for {@code hashCode}, that
		 * method will be returned.
		 * 
		 * @return the {@code hashCode()} method declared on this type or that
		 *         will be introduced (or {@code null} if undeclared and not
		 *         introduced)
		 */
		MethodMetadata createHashCodeMethod() {

			// See if the type itself declared the method
			MethodMetadata result = MemberFindingUtils.getDeclaredMethod(
					governorTypeDetails, methodName, null);
			if (result != null) {
				return result;
			}

			initHashCodeBuilder();
			if (callSuper) {
				appendSuper();
			}
			appendFields();
			finishHashCodeBuilder();
			return createMethodMetadata();
		}

		/** Initializes the {@link HashCodeBuilder}. */
		private InvocableMemberBodyBuilder initHashCodeBuilder() {
			bodyBuilder.appendFormalLine("return new "
					+ HASHCODE_BUILDER.getNameIncludingTypeParameters(false,
							builder.getImportRegistrationResolver())
					+ "(43, 11)");
			bodyBuilder.indent();
			return bodyBuilder;
		}

		/** Calls {@code super.hashCode()}. */
		private InvocableMemberBodyBuilder appendSuper() {
			bodyBuilder.appendFormalLine(".appendSuper(super.hashCode())");
			return bodyBuilder;
		}

		/** Calls {@code append} for each non-transient non-static field. */
		private InvocableMemberBodyBuilder appendFields() {
			List<? extends FieldMetadata> declaredFields = governorTypeDetails
					.getDeclaredFields();
			for (FieldMetadata field : declaredFields) {
				if (!Modifier.isTransient(field.getModifier())
						&& !Modifier.isStatic(field.getModifier())
						&& shouldIncludeField(field)
				) {
					bodyBuilder.appendFormalLine(".append("
							+ field.getFieldName().getSymbolName() + ")");
				}
			}
			return bodyBuilder;
		}

		/** Calls {@code toHashCode()}. */
		private InvocableMemberBodyBuilder finishHashCodeBuilder() {
			bodyBuilder.appendFormalLine(".toHashCode();");
			bodyBuilder.indentRemove();
			return bodyBuilder;
		}

		/** Creates the {@link MethodMetadata} for {@code hashCode()}. */
		private MethodMetadata createMethodMetadata() {
			return new MethodMetadataBuilder(getId(), Modifier.PUBLIC,
					methodName, JavaType.INT_PRIMITIVE, null, null,				
					bodyBuilder).build();
		}
	}
}
