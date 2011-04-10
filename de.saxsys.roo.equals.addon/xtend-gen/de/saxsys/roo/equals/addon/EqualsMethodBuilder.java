package de.saxsys.roo.equals.addon;

import de.saxsys.roo.equals.addon.Annotation;
import de.saxsys.roo.equals.addon.EqualsMetadata;
import de.saxsys.roo.equals.addon.Method;
import de.saxsys.roo.equals.addon.MethodBuilder;
import de.saxsys.roo.equals.addon.MethodSignature;
import de.saxsys.roo.equals.addon.Parameter;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.xtext.xbase.lib.BooleanExtensions;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xtend2.lib.StringConcatenation;
import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.FieldMetadata;
import org.springframework.roo.classpath.details.ItdTypeDetailsBuilder;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadataBuilder;
import org.springframework.roo.model.ImportRegistrationResolver;
import org.springframework.roo.model.JavaType;

@SuppressWarnings("all")
public class EqualsMethodBuilder extends MethodBuilder {
  
  public void buildMethods(final ItdTypeDetailsBuilder itdBuilder, final EqualsMetadata eqMetadata, final ClassOrInterfaceTypeDetails govTypeDetails) {
    {
      ImportRegistrationResolver _importRegistrationResolver = itdBuilder.getImportRegistrationResolver();
      final ImportRegistrationResolver irr = _importRegistrationResolver;
      Method _equalsMethod = this.getEqualsMethod(eqMetadata, govTypeDetails, irr);
      Method _hashCodeMethod = this.getHashCodeMethod(eqMetadata, govTypeDetails, irr);
      ArrayList<Method> _newArrayList = CollectionLiterals.<Method>newArrayList(_equalsMethod, _hashCodeMethod);
      final ArrayList<Method> methods = _newArrayList;
      final EqualsMetadata typeConverted_eqMetadata = (EqualsMetadata)eqMetadata;
      String _id = typeConverted_eqMetadata.getId();
      final ArrayList<Method> typeConverted_methods = (ArrayList<Method>)methods;
      this.buildMethods(itdBuilder, _id, govTypeDetails, typeConverted_methods);
    }
  }
  
  public Method getEqualsMethod(final EqualsMetadata eqMetadata, final ClassOrInterfaceTypeDetails typeDetails, final ImportRegistrationResolver ir) {
    AnnotationMetadataBuilder _annotation = Annotation.annotation("java.lang.Override");
    Parameter _param = Parameter.param("java.lang.Object", "other");
    MethodSignature _signature = MethodSignature.signature(_annotation, Modifier.PUBLIC, JavaType.BOOLEAN_PRIMITIVE, "equals", _param);
    final Function0<StringConcatenation> _function = new Function0<StringConcatenation>() {
        public StringConcatenation apply() {
          StringConcatenation _xblockexpression = null;
          {
            final ClassOrInterfaceTypeDetails typeConverted_typeDetails = (ClassOrInterfaceTypeDetails)typeDetails;
            JavaType _name = typeConverted_typeDetails.getName();
            String _simpleTypeName = _name.getSimpleTypeName();
            final String simpleName = _simpleTypeName;
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("if (other == null) { return false; }");
            _builder.newLine();
            _builder.append("if (other == this) { return true; }");
            _builder.newLine();
            {
              boolean _isCallInstanceof = eqMetadata.isCallInstanceof();
              if (_isCallInstanceof) {
                _builder.newLineIfNotEmpty();
                _builder.append("if (!(other instanceof ");
                _builder.append(simpleName, "");
                _builder.append(" )) {");
                _builder.newLineIfNotEmpty();} else {
                _builder.newLineIfNotEmpty();
                _builder.append("if (other.getClass() != getClass()) {");
                _builder.newLine();
              }
            }
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("return false;");
            _builder.newLine();
            _builder.append("}");
            _builder.newLine();
            _builder.append(simpleName, "");
            _builder.append(" rhs = (");
            _builder.append(simpleName, "");
            _builder.append(") other;");
            _builder.newLineIfNotEmpty();
            _builder.append("return new ");
            String _resolve = EqualsMethodBuilder.this.resolve("org.apache.commons.lang.builder.EqualsBuilder", ir);
            _builder.append(_resolve, "");
            _builder.append("()");
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            {
              boolean _isCallSuper = eqMetadata.isCallSuper();
              if (_isCallSuper) {
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
                _builder.append(".appendSuper(super.equals(rhs))");
                _builder.newLine();
                _builder.append("    ");
              }
            }
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            {
              boolean _isExclusiveMode = eqMetadata.isExclusiveMode();
              Iterable<FieldMetadata> _fieldsToInclude = EqualsMethodBuilder.this.fieldsToInclude(typeDetails, _isExclusiveMode);
              for(FieldMetadata field : _fieldsToInclude) {
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
                _builder.append(".append(");
                String _symbol = EqualsMethodBuilder.this.symbol(field);
                _builder.append(_symbol, "    ");
                _builder.append(", rhs.");
                String _symbol_1 = EqualsMethodBuilder.this.symbol(field);
                _builder.append(_symbol_1, "    ");
                _builder.append(")");
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
              }
            }
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            _builder.append(".isEquals();");
            _builder.newLine();
            _xblockexpression = (_builder);
          }
          return _xblockexpression;
        }
      };
    Method _method = Method.method(_signature, _function);
    return _method;
  }
  
  public Method getHashCodeMethod(final EqualsMetadata eqMetadata, final ClassOrInterfaceTypeDetails typeDetails, final ImportRegistrationResolver ir) {
    AnnotationMetadataBuilder _annotation = Annotation.annotation("java.lang.Override");
    MethodSignature _signature = MethodSignature.signature(_annotation, Modifier.PUBLIC, JavaType.INT_PRIMITIVE, "hashCode");
    final Function0<StringConcatenation> _function = new Function0<StringConcatenation>() {
        public StringConcatenation apply() {
          StringConcatenation _xblockexpression = null;
          {
            final ClassOrInterfaceTypeDetails typeConverted_typeDetails = (ClassOrInterfaceTypeDetails)typeDetails;
            JavaType _name = typeConverted_typeDetails.getName();
            String _simpleTypeName = _name.getSimpleTypeName();
            final String simpleName = _simpleTypeName;
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("return new ");
            String _resolve = EqualsMethodBuilder.this.resolve("org.apache.commons.lang.builder.HashCodeBuilder", ir);
            _builder.append(_resolve, "");
            _builder.append("(43, 11)");
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            {
              boolean _isCallSuper = eqMetadata.isCallSuper();
              if (_isCallSuper) {
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
                _builder.append(".appendSuper(super.hashCode())");
                _builder.newLine();
                _builder.append("    ");
              }
            }
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            {
              boolean _isExclusiveMode = eqMetadata.isExclusiveMode();
              Iterable<FieldMetadata> _fieldsToInclude = EqualsMethodBuilder.this.fieldsToInclude(typeDetails, _isExclusiveMode);
              for(FieldMetadata field : _fieldsToInclude) {
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
                _builder.append(".append(");
                String _symbol = EqualsMethodBuilder.this.symbol(field);
                _builder.append(_symbol, "    ");
                _builder.append(")");
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
              }
            }
            _builder.newLineIfNotEmpty();
            _builder.append("    ");
            _builder.append(".toHashCode();");
            _builder.newLine();
            _xblockexpression = (_builder);
          }
          return _xblockexpression;
        }
      };
    Method _method = Method.method(_signature, _function);
    return _method;
  }
  
  public Iterable<FieldMetadata> fieldsToInclude(final ClassOrInterfaceTypeDetails typeDetails, final boolean exclusiveMode) {
    final ClassOrInterfaceTypeDetails typeConverted_typeDetails = (ClassOrInterfaceTypeDetails)typeDetails;
    List<? extends FieldMetadata> _declaredFields = typeConverted_typeDetails.getDeclaredFields();
    final Function1<FieldMetadata,Boolean> _function = new Function1<FieldMetadata,Boolean>() {
        public Boolean apply(FieldMetadata f) {
          boolean _hasProperModifiers = EqualsMethodBuilder.this.hasProperModifiers(f);
          boolean _shallInclude = EqualsMethodBuilder.this.shallInclude(f, exclusiveMode);
          boolean _operator_and = BooleanExtensions.operator_and(_hasProperModifiers, _shallInclude);
          return ((Boolean)_operator_and);
        }
      };
    Iterable<FieldMetadata> _filter = IterableExtensions.<FieldMetadata>filter(((java.util.List<org.springframework.roo.classpath.details.FieldMetadata>) _declaredFields), _function);
    return _filter;
  }
  
  public boolean hasProperModifiers(final FieldMetadata f) {
    final FieldMetadata typeConverted_f = (FieldMetadata)f;
    int _modifier = typeConverted_f.getModifier();
    boolean _isTransient = Modifier.isTransient(_modifier);
    boolean _operator_not = BooleanExtensions.operator_not(_isTransient);
    final FieldMetadata typeConverted_f_1 = (FieldMetadata)f;
    int _modifier_1 = typeConverted_f_1.getModifier();
    boolean _isStatic = Modifier.isStatic(_modifier_1);
    boolean _operator_not_1 = BooleanExtensions.operator_not(_isStatic);
    boolean _operator_and = BooleanExtensions.operator_and(_operator_not, _operator_not_1);
    return _operator_and;
  }
  
  public boolean shallInclude(final FieldMetadata f, final boolean exclusiveMode) {
    boolean _xifexpression = false;
    if (exclusiveMode) {
      final FieldMetadata typeConverted_f = (FieldMetadata)f;
      String _name = de.saxsys.roo.equals.addon.EqualsInclude.class.getName();
      boolean _hasAnnotation = this.hasAnnotation(typeConverted_f, _name);
      _xifexpression = _hasAnnotation;
    } else {
      final FieldMetadata typeConverted_f_1 = (FieldMetadata)f;
      String _name_1 = de.saxsys.roo.equals.addon.EqualsExclude.class.getName();
      boolean _hasAnnotation_1 = this.hasAnnotation(typeConverted_f_1, _name_1);
      boolean _operator_not = BooleanExtensions.operator_not(_hasAnnotation_1);
      _xifexpression = _operator_not;
    }
    return _xifexpression;
  }
}