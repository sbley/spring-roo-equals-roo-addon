package com.saxsys.roo.equals.roo.addon;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.classpath.PhysicalTypeIdentifierNamingUtils;
import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.details.DefaultMethodMetadata;
import org.springframework.roo.classpath.details.FieldMetadata;
import org.springframework.roo.classpath.details.MemberFindingUtils;
import org.springframework.roo.classpath.details.MethodMetadata;
import org.springframework.roo.classpath.details.annotations.AnnotatedJavaType;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadata;
import org.springframework.roo.classpath.details.annotations.populator.AutoPopulationUtils;
import org.springframework.roo.classpath.itd.AbstractItdTypeDetailsProvidingMetadataItem;
import org.springframework.roo.classpath.itd.InvocableMemberBodyBuilder;
import org.springframework.roo.metadata.MetadataIdentificationUtils;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.project.Path;
import org.springframework.roo.support.style.ToStringCreator;
import org.springframework.roo.support.util.Assert;

/**
 * Metadata for {@link RooHashEquals}.
 * 
 * @author stefan.ocke
 * @author stefan.bley
 * @since 1.0
 * 
 */
public class HashEqualsMetadata extends
		AbstractItdTypeDetailsProvidingMetadataItem {

	private static Logger logger = LoggerFactory
			.getLogger(HashEqualsMetadata.class);
	private static final String PROVIDES_TYPE_STRING = HashEqualsMetadata.class
			.getName();
	private static final String PROVIDES_TYPE = MetadataIdentificationUtils
			.create(PROVIDES_TYPE_STRING);
	private static final JavaType EQUALS_BUILDER = new JavaType(
			"org.apache.commons.lang.builder.EqualsBuilder");
	private static final JavaType HASHCODE_BUILDER = new JavaType(
			"org.apache.commons.lang.builder.HashCodeBuilder");
	private static final JavaType OBJECT = new JavaType("java.lang.Object");

	// From annotation
	// @AutoPopulate private String toStringMethod = "toString";

	public HashEqualsMetadata(String identifier, JavaType aspectName,
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
						RooHashEquals.class.getName()));
		if (annotation != null) {
			AutoPopulationUtils.populate(this, annotation);
		}

		// Generate equals method
		MethodMetadata equalsMethod = getEqualsMethod();
		builder.addMethod(equalsMethod);

		// Generate hashCode method
		MethodMetadata hashCodeMethod = getHashCodeMethod();
		builder.addMethod(hashCodeMethod);

		// Create a representation of the desired output ITD
		itdTypeDetails = builder.build();
	}

	/**
	 * Obtains the {@code equals} method for this type, if available.
	 * 
	 * <p>
	 * If the user provided a non-default name for {@code equals}, that method
	 * will be returned.
	 * 
	 * @return the {@code equals} method declared on this type or that will be
	 *         introduced (or null if undeclared and not introduced)
	 */
	public MethodMetadata getEqualsMethod() {

		JavaSymbolName methodName = new JavaSymbolName("equals");
		final String simpleTypeName = governorTypeDetails.getName()
				.getSimpleTypeName();

		// See if the type itself declared the method
		MethodMetadata result = MemberFindingUtils.getDeclaredMethod(
				governorTypeDetails, methodName, Collections
						.singletonList(OBJECT));
		if (result != null) {
			return result;
		}

		List<? extends FieldMetadata> declaredFields = governorTypeDetails
				.getDeclaredFields();
		for (FieldMetadata fieldMetadata : declaredFields) {
			logger.warn("FieldMetadata: " + fieldMetadata);
		}

		InvocableMemberBodyBuilder bodyBuilder = new InvocableMemberBodyBuilder();
		// simple checks
		bodyBuilder.appendFormalLine("if (other == null) { return false; }");
		bodyBuilder.appendFormalLine("if (other == this) { return true; }");
		bodyBuilder.appendFormalLine("if (other.getClass() != getClass()) {");
		bodyBuilder.indent();
		bodyBuilder.appendFormalLine("return false;");
		bodyBuilder.indentRemove();
		bodyBuilder.appendFormalLine("}");

		// cast other object
		bodyBuilder.appendFormalLine(simpleTypeName + " rhs = ("
				+ simpleTypeName + ") other;");
		bodyBuilder.appendFormalLine("return new "
				+ EQUALS_BUILDER.getNameIncludingTypeParameters(false, builder
						.getImportRegistrationResolver()) + "()");

		// call super.equals()
		bodyBuilder.indent();
		bodyBuilder.appendFormalLine(".appendSuper(super.equals(other))");

		// add comparisons
		for (FieldMetadata field : declaredFields) {
			if (!Modifier.isTransient(field.getModifier())
					&& !Modifier.isStatic(field.getModifier())) {
				bodyBuilder.appendFormalLine(".append("
						+ field.getFieldName().getSymbolName() + ", rhs."
						+ field.getFieldName().getSymbolName() + ")");
			}
		}

		bodyBuilder.appendFormalLine(".isEquals();");
		bodyBuilder.indentRemove();

		result = new DefaultMethodMetadata(getId(), Modifier.PUBLIC,
				methodName, JavaType.BOOLEAN_PRIMITIVE,
				AnnotatedJavaType.convertFromJavaTypes(Collections
						.singletonList(OBJECT)), Collections
						.singletonList(new JavaSymbolName("other")),
				new ArrayList<AnnotationMetadata>(), null, bodyBuilder
						.getOutput());

		return result;
	}

	public MethodMetadata getHashCodeMethod() {
		return new HashCodeMethodBuilder().createHashCodeMethod();
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

	/**
	 * Creates a {@code hashCode()} method.
	 * 
	 * @author Stefan Bley
	 * @since 1.0
	 */
	class HashCodeMethodBuilder {

		InvocableMemberBodyBuilder bodyBuilder = new InvocableMemberBodyBuilder();
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
			appendSuper(); // TODO make call to super configurable [SB]
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
			return bodyBuilder;
		}

		/** Calls {@code super.hashCode()}. */
		private InvocableMemberBodyBuilder appendSuper() {
			bodyBuilder.indent();
			bodyBuilder.appendFormalLine(".appendSuper(super.hashCode())");
			return bodyBuilder;
		}

		/** Calls {@code append} for each non-transient non-static field. */
		private InvocableMemberBodyBuilder appendFields() {
			List<? extends FieldMetadata> declaredFields = governorTypeDetails
					.getDeclaredFields();
			for (FieldMetadata field : declaredFields) {
				if (!Modifier.isTransient(field.getModifier())
						&& !Modifier.isStatic(field.getModifier())) {
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
			return new DefaultMethodMetadata(getId(), Modifier.PUBLIC,
					methodName, JavaType.INT_PRIMITIVE, null, null,
					new ArrayList<AnnotationMetadata>(), null, bodyBuilder
							.getOutput());
		}
	}
}
