package de.saxsys.roo.equals.addon;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.roo.classpath.details.annotations.AnnotationMetadata;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;

public class MethodSignature {
	private List<AnnotationMetadata> annotations = new ArrayList<AnnotationMetadata>();
	private int modifier = Modifier.PUBLIC;
	private JavaType returnType = JavaType.VOID_PRIMITIVE;
	private JavaSymbolName name;
	private List<Parameter> parameters = new ArrayList<Parameter>();
	private List<JavaType> exceptions = new ArrayList<JavaType>();

	public MethodSignature(List<AnnotationMetadata> annotations, int modifier,
			JavaType returnType, JavaSymbolName name,
			List<Parameter> parameters, List<JavaType> exceptions) {
		super();
		if (annotations != null) {
			this.annotations = annotations;
		}
		if (modifier != 0) {
			this.modifier = modifier;
		}
		if (returnType != null) {
			this.returnType = returnType;
		}
		this.name = name;
		if (parameters != null) {
			this.parameters = parameters;
		}
		if (exceptions != null) {
			this.exceptions = exceptions;
		}
	}

	public List<AnnotationMetadata> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(List<AnnotationMetadata> annotations) {
		this.annotations = annotations;
	}

	public int getModifier() {
		return modifier;
	}

	public void setModifier(int modifier) {
		this.modifier = modifier;
	}

	public JavaType getReturnType() {
		return returnType;
	}

	public void setReturnType(JavaType returnType) {
		this.returnType = returnType;
	}

	public JavaSymbolName getName() {
		return name;
	}

	public void setName(JavaSymbolName name) {
		this.name = name;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	public List<JavaType> getExceptions() {
		return exceptions;
	}

	public void setExceptions(List<JavaType> exceptions) {
		this.exceptions = exceptions;
	}

	public MethodSignature withThrows(JavaType... exceptions) {
		this.exceptions.addAll(Arrays.asList(exceptions));
		return this;
	}
	
	public MethodSignature withThrows(String... exceptionFqns) {
		for (String fqn : exceptionFqns) {
			this.exceptions.add(new JavaType(fqn));
		}	
		return this;
	}

	public MethodSignature withAnnotations(AnnotationMetadata... annotations) {
		this.annotations.addAll(Arrays.asList(annotations));
		return this;
	}

	public static MethodSignature signature(int modifier, JavaType returnType,
			String name, Parameter... parameters) {
		return new MethodSignature(null, modifier, returnType,
				new JavaSymbolName(name), Arrays.asList(parameters), null);

	}

	public static MethodSignature signature(int modifier, String returnTypeFqn,
			String name, Parameter... parameters) {
		return new MethodSignature(null, modifier, new JavaType(returnTypeFqn),
				new JavaSymbolName(name), Arrays.asList(parameters), null);

	}

	public static MethodSignature signature(
			List<AnnotationMetadata> annotations, int modifier,
			JavaType returnType, String name, Parameter... parameters) {
		return new MethodSignature(annotations, modifier, returnType,
				new JavaSymbolName(name), Arrays.asList(parameters), null);

	}
	
	public static MethodSignature signature(
			List<AnnotationMetadata> annotations, int modifier,
			String returnTypeFqn, String name, Parameter... parameters) {
		return new MethodSignature(annotations, modifier, new JavaType(returnTypeFqn),
				new JavaSymbolName(name), Arrays.asList(parameters), null);

	}

}
