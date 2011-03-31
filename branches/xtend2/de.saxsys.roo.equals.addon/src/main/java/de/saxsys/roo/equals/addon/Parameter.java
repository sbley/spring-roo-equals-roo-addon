package de.saxsys.roo.equals.addon;

import java.util.ArrayList;
import java.util.List;

import org.springframework.roo.classpath.details.annotations.AnnotationMetadata;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;

public class Parameter {
	private List<AnnotationMetadata> annotations;
	private JavaType type;
	private JavaSymbolName name;
	
	public Parameter(List<AnnotationMetadata> annotations, JavaType type,
			JavaSymbolName name) {
		super();
		this.annotations = annotations;
		this.type = type;
		this.name = name;
	}
	
	public Parameter(List<AnnotationMetadata> annotations, JavaType type,
			String name) {
		this(annotations, type, new JavaSymbolName(name));
	}

	public List<AnnotationMetadata> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(List<AnnotationMetadata> annotations) {
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
	
	public Parameter with(AnnotationMetadata annotation){
		if(annotations==null){
			annotations = new ArrayList<AnnotationMetadata>();
		}
		annotations.add(annotation);
		return this;
	}
	
	public static Parameter param(List<AnnotationMetadata> annotations, JavaType type,
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
