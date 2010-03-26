package com.saxsys.roo.equals.roo.addon;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.beaninfo.BeanInfoMetadata;
import org.springframework.roo.classpath.PhysicalTypeIdentifierNamingUtils;
import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.details.DefaultMethodMetadata;
import org.springframework.roo.classpath.details.FieldMetadata;
import org.springframework.roo.classpath.details.MemberFindingUtils;
import org.springframework.roo.classpath.details.MethodMetadata;
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
import org.springframework.roo.support.style.ToStringCreator;
import org.springframework.roo.support.util.Assert;

/**
 * Metadata for {@link RooToString}.
 * 
 * @author Ben Alex
 * @since 1.0
 *
 */
public class HashEqualsMetadata extends AbstractItdTypeDetailsProvidingMetadataItem {

	private static Logger logger = LoggerFactory.getLogger(HashEqualsMetadata.class);
	private static final String PROVIDES_TYPE_STRING = HashEqualsMetadata.class.getName();
	private static final String PROVIDES_TYPE = MetadataIdentificationUtils.create(PROVIDES_TYPE_STRING);
	
	private BeanInfoMetadata beanInfoMetadata;
	
	// From annotation
	// @AutoPopulate private String toStringMethod = "toString";

	public HashEqualsMetadata(String identifier, JavaType aspectName, PhysicalTypeMetadata governorPhysicalTypeMetadata, BeanInfoMetadata beanInfoMetadata) {
		super(identifier, aspectName, governorPhysicalTypeMetadata);
		Assert.isTrue(isValid(identifier), "Metadata identification string '" + identifier + "' does not appear to be a valid");
		Assert.notNull(beanInfoMetadata, "Bean info metadata required");
		
		if (!isValid()) {
			return;
		}
		
		this.beanInfoMetadata = beanInfoMetadata;

		// Process values from the annotation, if present
		AnnotationMetadata annotation = MemberFindingUtils.getDeclaredTypeAnnotation(governorTypeDetails, new JavaType(RooHashEquals.class.getName()));
		if (annotation != null) {
			AutoPopulationUtils.populate(this, annotation);
		}

		// Generate equals method
		MethodMetadata equalsMethod = getEqualsMethod();
		builder.addMethod(equalsMethod);
		
		// Create a representation of the desired output ITD
		itdTypeDetails = builder.build();
	}
	
	/**
	 * Obtains the "toString" method for this type, if available.
	 * 
	 * <p>
	 * If the user provided a non-default name for "toString", that method will be returned.
	 * 
	 * @return the "toString" method declared on this type or that will be introduced (or null if undeclared and not introduced)
	 */
	public MethodMetadata getEqualsMethod() {

		JavaSymbolName methodName = new JavaSymbolName("equals");

		// See if the type itself declared the method
		MethodMetadata result = MemberFindingUtils.getDeclaredMethod(
				governorTypeDetails, methodName, Collections
						.singletonList(new JavaType("java.lang.Object")));
		if (result != null) {
			return result;
		}
		
		
			InvocableMemberBodyBuilder builder = new InvocableMemberBodyBuilder();
			for (MethodMetadata accessor : beanInfoMetadata.getPublicAccessors(false)) {
				logger.warn("Accessor: "+accessor);
			}
			List<? extends FieldMetadata> declaredFields = governorTypeDetails.getDeclaredFields();
			for (FieldMetadata fieldMetadata : declaredFields) {
				logger.warn("FieldMetadata: "+fieldMetadata);
			}
			
			
			/*	builder.appendFormalLine("StringBuilder sb = new StringBuilder();");
			
			*//** key: field name, value: accessor name*//*
			Map<String, String> map = new HashMap<String, String>();
			
			*//** field names*//*
			List<String> order = new ArrayList<String>();

			for (MethodMetadata accessor : beanInfoMetadata.getPublicAccessors(false)) {
				String accessorName = accessor.getMethodName().getSymbolName();
				String fieldName = beanInfoMetadata.getPropertyNameForJavaBeanMethod(accessor).getSymbolName();
				String accessorText = accessorName + "()";
				if (accessor.getReturnType().isCommonCollectionType()) {
					accessorText = accessorName + "() == null ? \"null\" : " + accessorName + "().size()";
				} else if (accessor.getReturnType().isArray()) {
					accessorText = "java.util.Arrays.toString(" + accessorName + "())";
				} else if (new JavaType(Calendar.class.getName()).equals(accessor.getReturnType())) {
					accessorText = accessorName + "().getTime()";
				}
				map.put(fieldName, accessorText);
				order.add(fieldName);
			}

			int index = 0;
			int size = map.keySet().size();
			for (String fieldName : order) {
				index++;
				String accessorText = map.get(fieldName);
				StringBuilder string = new StringBuilder();
				string.append("sb.append(\"" + fieldName  + ": \").append(" + accessorText + ")");
				if (index < size) {
					string.append(".append(\", \")");
				}
				string.append(";");
				builder.appendFormalLine(string.toString());
			}*/
					
		builder.appendFormalLine("return true;");

		result = new DefaultMethodMetadata(getId(), Modifier.PUBLIC,
				methodName, JavaType.BOOLEAN_PRIMITIVE, AnnotatedJavaType
						.convertFromJavaTypes(Collections
								.singletonList(new JavaType("java.lang.Object"))),
								Collections
								.singletonList(new JavaSymbolName("other")),
				new ArrayList<AnnotationMetadata>(), null, builder.getOutput());

		return result;
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
		return PhysicalTypeIdentifierNamingUtils.createIdentifier(PROVIDES_TYPE_STRING, javaType, path);
	}

	public static final JavaType getJavaType(String metadataIdentificationString) {
		return PhysicalTypeIdentifierNamingUtils.getJavaType(PROVIDES_TYPE_STRING, metadataIdentificationString);
	}

	public static final Path getPath(String metadataIdentificationString) {
		return PhysicalTypeIdentifierNamingUtils.getPath(PROVIDES_TYPE_STRING, metadataIdentificationString);
	}

	public static boolean isValid(String metadataIdentificationString) {
		return PhysicalTypeIdentifierNamingUtils.isValid(PROVIDES_TYPE_STRING, metadataIdentificationString);
	}

	
}
