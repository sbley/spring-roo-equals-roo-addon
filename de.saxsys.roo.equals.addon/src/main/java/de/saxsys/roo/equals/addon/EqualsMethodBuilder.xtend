package de.saxsys.roo.equals.addon

import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails
import org.springframework.roo.model.ImportRegistrationResolver
import org.springframework.roo.model.JavaType
import org.springframework.roo.classpath.details.FieldMetadata
import org.springframework.roo.classpath.details.MethodMetadata
import java.util.List
import org.springframework.roo.classpath.details.IdentifiableAnnotatedJavaStructure
import static extension java.lang.reflect.Modifier.*
import static extension de.saxsys.roo.equals.addon.Parameter.*
import static extension de.saxsys.roo.equals.addon.MethodSignature.*
import static extension de.saxsys.roo.equals.addon.Method.*
import java.lang.reflect.Modifier
import org.springframework.roo.model.JavaSymbolName
import org.springframework.roo.classpath.details.MethodMetadataBuilder
import org.springframework.roo.classpath.details.annotations.AnnotatedJavaType
import org.springframework.roo.classpath.details.annotations.AnnotationMetadata
import org.springframework.roo.classpath.details.ItdTypeDetailsBuilder

class EqualsMethodBuilder extends de.saxsys.roo.equals.addon.MethodBuilder{
	buildMethods(ItdTypeDetailsBuilder itdBuilder, EqualsMetadata eqMetadata, ClassOrInterfaceTypeDetails govTypeDetails){
			val irr = itdBuilder.importRegistrationResolver
			val methods = newArrayList(
				getEqualsMethod(eqMetadata, govTypeDetails, irr),
				getHashCodeMethod(eqMetadata, govTypeDetails, irr)
			)
			buildMethods(itdBuilder, eqMetadata.id, govTypeDetails, methods)
	}
	getEqualsMethod(EqualsMetadata eqMetadata, ClassOrInterfaceTypeDetails typeDetails, ImportRegistrationResolver ir){
		method( 
			signature(Modifier::PUBLIC, JavaType::BOOLEAN_PRIMITIVE, "equals", param("java.lang.Object", "other")),
		 	[|{
			val simpleName = typeDetails.name.simpleTypeName
			''' 
			if (other == null) { return false; }
			if (other == this) { return true; }
			«IF eqMetadata.callInstanceof»
			if (!(other instanceof «simpleName» )) {
			«ELSE»
			if (other.getClass() != getClass()) {
			«ENDIF»
				return false;
			}
			«simpleName» rhs = («simpleName») other;
			return new «"org.apache.commons.lang.builder.EqualsBuilder".resolve(ir)»()
			    «IF eqMetadata.callSuper»
			    .appendSuper(super.equals(rhs))
			    «ENDIF»
			    «FOR field : typeDetails.fieldsToInclude(eqMetadata.exclusiveMode)»			
			    .append(«field.symbol», rhs.«field.symbol»)
			    «ENDFOR»
			    .isEquals();
			'''
			}]			
		);
	}
	
	getHashCodeMethod(EqualsMetadata eqMetadata, ClassOrInterfaceTypeDetails typeDetails, ImportRegistrationResolver ir){
		method( 
			signature(Modifier::PUBLIC, JavaType::INT_PRIMITIVE, "hashCode"),
		 	[|{
			val simpleName = typeDetails.name.simpleTypeName
			''' 
			return new «"org.apache.commons.lang.builder.HashCodeBuilder".resolve(ir)»(43, 11)
			    «IF eqMetadata.callSuper»
			    .appendSuper(super.hashCode())
			    «ENDIF»
			    «FOR field : typeDetails.fieldsToInclude(eqMetadata.exclusiveMode)»			
			    .append(«field.symbol»)
			    «ENDFOR»
			    .toHashCode();
			'''
			}]
		);
	}
	fieldsToInclude(ClassOrInterfaceTypeDetails typeDetails, boolean exclusiveMode){
		(typeDetails.declaredFields as List<FieldMetadata>)
		.filter(f | f.hasProperModifiers && f.shallInclude(exclusiveMode))
	} 	
	hasProperModifiers(FieldMetadata f){
		!f.modifier.isTransient && !f.modifier.isStatic
	}
	shallInclude(FieldMetadata f, boolean exclusiveMode){
		if (exclusiveMode) f.hasAnnotation(typeof(EqualsInclude).name)
		else ! f.hasAnnotation(typeof(EqualsExclude).name)
	}	
}