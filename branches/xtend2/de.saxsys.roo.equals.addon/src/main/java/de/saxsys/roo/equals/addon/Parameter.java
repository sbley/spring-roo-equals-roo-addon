package de.saxsys.roo.equals.addon;

import java.util.ArrayList;
import java.util.List;

import org.springframework.roo.classpath.details.annotations.AnnotationMetadataBuilder;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;

public class Parameter {
	private List<AnnotationMetadataBuilder> annotations;
	private JavaType type;
	private JavaSymbolName name;
	
	public Parameter(List<AnnotationMetadataBuilder> annotations, JavaType type,
			JavaSymbolName name) {
		super();
		this.annotations = annotations;
		this.type = type;
		this.name = name;
	}
	
	public Parameter(List<AnnotationMetadataBuilder> annotations, JavaType type,
			String name) {
		this(annotations, type, new JavaSymbolName(name));
	}

	public List<AnnotationMetadataBuilder> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(List<AnnotationMetadataBuilder> annotations) {
		this.annotations = annotations;
	}

	public JavaType getType() {
		return type;
	}

	public void setType(JavaType type) {
		this.type = type;
	}

	public JavaSymbolName getName() {
		return name;
	}

	public void setName(JavaSymbolName name) {
		this.name = name;
	}
	
	public Parameter with(AnnotationMetadataBuilder annotation){
		if(annotations==null){
			annotations = new ArrayList<AnnotationMetadataBuilder>();
		}
		annotations.add(annotation);
		return this;
	}
	
	public static Parameter param(List<AnnotationMetadataBuilder> annotations, JavaType type,
			String name){
		return new Parameter(annotations, type, name);
	}	
	public static Parameter param(JavaType type, String name){
		return new Parameter(null, type, name);
	}
	public static Parameter param(String fullyQualifiedTypeName, String name){
		return new Parameter(null, new JavaType(fullyQualifiedTypeName), name);
	}
	
	
}
