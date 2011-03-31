package de.saxsys.roo.equals.addon

import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails
import org.springframework.roo.model.ImportRegistrationResolver
import java.util.List
import org.springframework.roo.model.JavaType
import org.springframework.roo.classpath.details.annotations.AnnotatedJavaType
import org.springframework.roo.classpath.details.annotations.AnnotationMetadata
import org.springframework.roo.classpath.details.IdentifiableAnnotatedJavaStructure
import org.springframework.roo.classpath.details.FieldMetadata
import org.springframework.roo.model.JavaSymbolName
import static extension org.springframework.roo.classpath.details.MemberFindingUtils.*
import org.springframework.roo.classpath.details.MethodMetadataBuilder
import org.eclipse.xtext.xtend2.lib.StringConcatenation
import org.springframework.roo.metadata.MetadataItem
import org.springframework.roo.classpath.itd.AbstractItdTypeDetailsProvidingMetadataItem
import org.springframework.roo.classpath.details.annotations.AnnotationMetadataBuilder
import org.springframework.roo.classpath.details.ItdTypeDetailsBuilder

class MethodBuilder{
	buildMethods(ItdTypeDetailsBuilder itdBuilder, String declaringId, ClassOrInterfaceTypeDetails govTypeDetails, 
		List<Method> methods){
		for(m : methods){
			itdBuilder.addMethod(getOrBuildMethod(declaringId, govTypeDetails, m))
		}
	}
	
	getOrBuildMethod(String declaringId, ClassOrInterfaceTypeDetails govTypeDetails, Method method){
		val sig = method.signature
		val existingMethod = govTypeDetails.getDeclaredMethod(sig.name, sig.parameters.map(p | p.type))
		if (existingMethod!=null) 
			existingMethod 
		else {
			val mmdbuilder = new MethodMetadataBuilder(
				declaringId, sig.modifier,
				sig.name, sig.returnType,
				sig.parameters.map(p | p.type.annoJavaType(p.annotations)),
				sig.parameters.map(p | p.name),
				new FakedBodyBuilder(method.body.apply))
			//TODO: Builder oder nicht Builder ??	
			mmdbuilder.setAnnotations(sig.annotations.map(a | new AnnotationMetadataBuilder(a)))
			mmdbuilder.setThrowsTypes(sig.exceptions)
			mmdbuilder.build()
		}
		
	}
	
	
//The following extensions can be moved to some library
	
	javaType(String fqn){
		new JavaType(fqn)
	}

	annoJavaType(JavaType type){
		type.annoJavaType(null)
	}
	
	annoJavaType(JavaType type, List<AnnotationMetadata> annotations){
		new AnnotatedJavaType(type, annotations)
	}
	
	annoJavaType(String fqn, List<AnnotationMetadata> annotations){
		fqn.javaType.annoJavaType(annotations)
	}
	
	resolve(String fqn, ImportRegistrationResolver ir){ 
		(new JavaType(fqn)).getNameIncludingTypeParameters(false, ir)
	}
	
	hasAnnotation(IdentifiableAnnotatedJavaStructure ajs, String fqn){
		ajs.annotations.exists(a | fqn.equals(a.annotationType.fullyQualifiedTypeName)); 
	}
	symbol(FieldMetadata f){
		f.fieldName.symbolName
	}
	
	symbol(String s){
		new JavaSymbolName(s)
	}
}