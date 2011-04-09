package de.saxsys.roo.equals.addon;

import de.saxsys.roo.equals.addon.FakedBodyBuilder;
import de.saxsys.roo.equals.addon.Method;
import de.saxsys.roo.equals.addon.MethodSignature;
import de.saxsys.roo.equals.addon.Parameter;
import java.util.List;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xtend2.lib.StringConcatenation;
import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.FieldMetadata;
import org.springframework.roo.classpath.details.IdentifiableAnnotatedJavaStructure;
import org.springframework.roo.classpath.details.ItdTypeDetailsBuilder;
import org.springframework.roo.classpath.details.MemberFindingUtils;
import org.springframework.roo.classpath.details.MethodMetadata;
import org.springframework.roo.classpath.details.MethodMetadataBuilder;
import org.springframework.roo.classpath.details.annotations.AnnotatedJavaType;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadata;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadataBuilder;
import org.springframework.roo.model.ImportRegistrationResolver;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;

@SuppressWarnings("all")
public class MethodBuilder {
  private final MethodBuilder _this = this;
  
  public void buildMethods(final ItdTypeDetailsBuilder itdBuilder, final String declaringId, final ClassOrInterfaceTypeDetails govTypeDetails, final List<Method> methods) {
    for (Method m : methods) {
      final ItdTypeDetailsBuilder typeConverted_itdBuilder = (ItdTypeDetailsBuilder)itdBuilder;
      MethodMetadata _orBuildMethod = _this.getOrBuildMethod(declaringId, govTypeDetails, m);
      typeConverted_itdBuilder.addMethod(_orBuildMethod);
    }
  }
  
  public MethodMetadata getOrBuildMethod(final String declaringId, final ClassOrInterfaceTypeDetails govTypeDetails, final Method method) {
    MethodMetadata _xblockexpression = null;
    {
      MethodSignature _signature = method.getSignature();
      final MethodSignature sig = _signature;
      final ClassOrInterfaceTypeDetails typeConverted_govTypeDetails = (ClassOrInterfaceTypeDetails)govTypeDetails;
      JavaSymbolName _name = sig.getName();
      List<Parameter> _parameters = sig.getParameters();
      final Function1<Parameter,JavaType> function = new Function1<Parameter,JavaType>() {
          public JavaType apply(Parameter p) {
            JavaType _type = p.getType();
            return _type;
          }
        };
      List<JavaType> _map = ListExtensions.map(_parameters, function);
      MethodMetadata _declaredMethod = MemberFindingUtils.getDeclaredMethod(typeConverted_govTypeDetails, _name, _map);
      final MethodMetadata existingMethod = _declaredMethod;
      MethodMetadata _xifexpression = null;
      boolean _operator_notEquals = ObjectExtensions.operator_notEquals(existingMethod, null);
      if (((Boolean)_operator_notEquals)) {
        _xifexpression = existingMethod;
      } else {
        MethodMetadata _xblockexpression_1 = null;
        {
          int _modifier = sig.getModifier();
          JavaSymbolName _name_1 = sig.getName();
          JavaType _returnType = sig.getReturnType();
          List<Parameter> _parameters_1 = sig.getParameters();
          final Function1<Parameter,AnnotatedJavaType> function_1 = new Function1<Parameter,AnnotatedJavaType>() {
              public AnnotatedJavaType apply(Parameter p_1) {
                JavaType _type_1 = p_1.getType();
                List<AnnotationMetadata> _annotations = p_1.getAnnotations();
                AnnotatedJavaType _annoJavaType = _this.annoJavaType(_type_1, _annotations);
                return _annoJavaType;
              }
            };
          List<AnnotatedJavaType> _map_1 = ListExtensions.map(_parameters_1, function_1);
          List<Parameter> _parameters_2 = sig.getParameters();
          final Function1<Parameter,JavaSymbolName> function_2 = new Function1<Parameter,JavaSymbolName>() {
              public JavaSymbolName apply(Parameter p_2) {
                JavaSymbolName _name_2 = p_2.getName();
                return _name_2;
              }
            };
          List<JavaSymbolName> _map_2 = ListExtensions.map(_parameters_2, function_2);
          Function0<StringConcatenation> _body = method.getBody();
          StringConcatenation _apply = _body.apply();
          FakedBodyBuilder _fakedBodyBuilder = new FakedBodyBuilder(_apply);
          MethodMetadataBuilder _methodMetadataBuilder = new MethodMetadataBuilder(declaringId, _modifier, _name_1, _returnType, _map_1, _map_2, _fakedBodyBuilder);
          final MethodMetadataBuilder mmdbuilder = _methodMetadataBuilder;
          final MethodMetadataBuilder typeConverted_mmdbuilder = (MethodMetadataBuilder)mmdbuilder;
          List<AnnotationMetadataBuilder> _annotations_1 = sig.getAnnotations();
          typeConverted_mmdbuilder.setAnnotations(_annotations_1);
          final MethodMetadataBuilder typeConverted_mmdbuilder_1 = (MethodMetadataBuilder)mmdbuilder;
          List<JavaType> _exceptions = sig.getExceptions();
          typeConverted_mmdbuilder_1.setThrowsTypes(_exceptions);
          MethodMetadata _build = mmdbuilder.build();
          _xblockexpression_1 = (_build);
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
  }
  
  public JavaType javaType(final String fqn) {
    JavaType _javaType = new JavaType(fqn);
    return _javaType;
  }
  
  public AnnotatedJavaType annoJavaType(final JavaType type) {
    AnnotatedJavaType _annoJavaType = _this.annoJavaType(type, null);
    return _annoJavaType;
  }
  
  public AnnotatedJavaType annoJavaType(final JavaType type, final List<AnnotationMetadata> annotations) {
    AnnotatedJavaType _annotatedJavaType = new AnnotatedJavaType(type, annotations);
    return _annotatedJavaType;
  }
  
  public AnnotatedJavaType annoJavaType(final String fqn, final List<AnnotationMetadata> annotations) {
    JavaType _javaType = _this.javaType(fqn);
    AnnotatedJavaType _annoJavaType = _this.annoJavaType(_javaType, annotations);
    return _annoJavaType;
  }
  
  public String resolve(final String fqn, final ImportRegistrationResolver ir) {
    JavaType _javaType = new JavaType(fqn);
    String _nameIncludingTypeParameters = _javaType.getNameIncludingTypeParameters(false, ir);
    return _nameIncludingTypeParameters;
  }
  
  public boolean hasAnnotation(final IdentifiableAnnotatedJavaStructure ajs, final String fqn) {
    List<AnnotationMetadata> _annotations = ajs.getAnnotations();
    final Function1<AnnotationMetadata,Boolean> function = new Function1<AnnotationMetadata,Boolean>() {
        public Boolean apply(AnnotationMetadata a) {
          JavaType _annotationType = a.getAnnotationType();
          String _fullyQualifiedTypeName = _annotationType.getFullyQualifiedTypeName();
          boolean _equals = fqn.equals(_fullyQualifiedTypeName);
          return ((Boolean)_equals);
        }
      };
    boolean _exists = IterableExtensions.exists(_annotations, function);
    return _exists;
  }
  
  public String symbol(final FieldMetadata f) {
    JavaSymbolName _fieldName = f.getFieldName();
    String _symbolName = _fieldName.getSymbolName();
    return _symbolName;
  }
  
  public JavaSymbolName symbol(final String s) {
    JavaSymbolName _javaSymbolName = new JavaSymbolName(s);
    return _javaSymbolName;
  }
}