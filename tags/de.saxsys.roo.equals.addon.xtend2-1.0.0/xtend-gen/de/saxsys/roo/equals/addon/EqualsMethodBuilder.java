package de.saxsys.roo.equals.addon;

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
import org.springframework.roo.model.ImportRegistrationResolver;
import org.springframework.roo.model.JavaType;

@SuppressWarnings("all")
public class EqualsMethodBuilder extends MethodBuilder {
  private final EqualsMethodBuilder _this = this;
  
  public void buildMethods(final ItdTypeDetailsBuilder itdBuilder, final EqualsMetadata eqMetadata, final ClassOrInterfaceTypeDetails govTypeDetails) {
    {
      ImportRegistrationResolver _importRegistrationResolver = itdBuilder.getImportRegistrationResolver();
      final ImportRegistrationResolver irr = _importRegistrationResolver;
      Method _equalsMethod = _this.getEqualsMethod(eqMetadata, govTypeDetails, irr);
      Method _hashCodeMethod = _this.getHashCodeMethod(eqMetadata, govTypeDetails, irr);
      ArrayList<Method> _newArrayList = CollectionLiterals.newArrayList(_equalsMethod, _hashCodeMethod);
      final ArrayList<Method> methods = _newArrayList;
      final EqualsMetadata typeConverted_eqMetadata = (EqualsMetadata)eqMetadata;
      String _id = typeConverted_eqMetadata.getId();
      final ArrayList<Method> typeConverted_methods = (ArrayList<Method>)methods;
      _this.buildMethods(itdBuilder, _id, govTypeDetails, typeConverted_methods);
    }
  }
  
  public Method getEqualsMethod(final EqualsMetadata eqMetadata, final ClassOrInterfaceTypeDetails typeDetails, final ImportRegistrationResolver ir) {
    Parameter _param = Parameter.param("java.lang.Object", "other");
    MethodSignature _signature = MethodSignature.signature(Modifier.PUBLIC, JavaType.BOOLEAN_PRIMITIVE, "equals", _param);
    final Function0<StringConcatenation> function = new Function0<StringConcatenation>() {
        public StringConcatenation apply() {
          StringConcatenation _xblockexpression = null;
          {
            final ClassOrInterfaceTypeDetails typeConverted_typeDetails = (ClassOrInterfaceTypeDetails)typeDetails;
            JavaType _name = typeConverted_typeDetails.getName();
            String _simpleTypeName = _name.getSimpleTypeName();
            final String simpleName = _simpleTypeName;
            StringConcatenation builder = new StringConcatenation();
            builder.append("if (other == null) { return false; }");
            builder.newLine();
            builder.append("if (other == this) { return true; }");
            builder.newLine();
            {
              boolean _isCallInstanceof = eqMetadata.isCallInstanceof();
              if (((Boolean)_isCallInstanceof)) {
                builder.newLineIfNotEmpty();
                builder.append("if (!(other instanceof ");
                builder.append(simpleName, "");
                builder.append(" )) {");
                builder.newLineIfNotEmpty();} else {
                builder.newLineIfNotEmpty();
                builder.append("if (other.getClass() != getClass()) {");
                builder.newLine();
              }
            }
            builder.newLineIfNotEmpty();
            builder.append("\t");
            builder.append("return false;");
            builder.newLine();
            builder.append("}");
            builder.newLine();
            builder.append(simpleName, "");
            builder.append(" rhs = (");
            builder.append(simpleName, "");
            builder.append(") other;");
            builder.newLineIfNotEmpty();
            builder.append("return new ");
            String _resolve = _this.resolve("org.apache.commons.lang.builder.EqualsBuilder", ir);
            builder.append(_resolve, "");
            builder.append("()");
            builder.newLineIfNotEmpty();
            builder.append("    ");
            {
              boolean _isCallSuper = eqMetadata.isCallSuper();
              if (((Boolean)_isCallSuper)) {
                builder.newLineIfNotEmpty();
                builder.append("    ");
                builder.append(".appendSuper(super.equals(rhs))");
                builder.newLine();
                builder.append("    ");
              }
            }
            builder.newLineIfNotEmpty();
            builder.append("    ");
            {
              boolean _isExclusiveMode = eqMetadata.isExclusiveMode();
              Iterable<FieldMetadata> _fieldsToInclude = _this.fieldsToInclude(typeDetails, _isExclusiveMode);
              for(org.springframework.roo.classpath.details.FieldMetadata field : _fieldsToInclude) {
                builder.newLineIfNotEmpty();
                builder.append("    ");
                builder.append(".append(");
                String _symbol = _this.symbol(field);
                builder.append(_symbol, "    ");
                builder.append(", rhs.");
                String _symbol_1 = _this.symbol(field);
                builder.append(_symbol_1, "    ");
                builder.append(")");
                builder.newLineIfNotEmpty();
                builder.append("    ");
              }
            }
            builder.newLineIfNotEmpty();
            builder.append("    ");
            builder.append(".isEquals();");
            builder.newLine();
            _xblockexpression = (builder);
          }
          return _xblockexpression;
        }
      };
    Method _method = Method.method(_signature, function);
    return _method;
  }
  
  public Method getHashCodeMethod(final EqualsMetadata eqMetadata, final ClassOrInterfaceTypeDetails typeDetails, final ImportRegistrationResolver ir) {
    MethodSignature _signature = MethodSignature.signature(Modifier.PUBLIC, JavaType.INT_PRIMITIVE, "hashCode");
    final Function0<StringConcatenation> function = new Function0<StringConcatenation>() {
        public StringConcatenation apply() {
          StringConcatenation _xblockexpression = null;
          {
            final ClassOrInterfaceTypeDetails typeConverted_typeDetails = (ClassOrInterfaceTypeDetails)typeDetails;
            JavaType _name = typeConverted_typeDetails.getName();
            String _simpleTypeName = _name.getSimpleTypeName();
            final String simpleName = _simpleTypeName;
            StringConcatenation builder = new StringConcatenation();
            builder.append("return new ");
            String _resolve = _this.resolve("org.apache.commons.lang.builder.HashCodeBuilder", ir);
            builder.append(_resolve, "");
            builder.append("(43, 11)");
            builder.newLineIfNotEmpty();
            builder.append("    ");
            {
              boolean _isCallSuper = eqMetadata.isCallSuper();
              if (((Boolean)_isCallSuper)) {
                builder.newLineIfNotEmpty();
                builder.append("    ");
                builder.append(".appendSuper(super.hashCode())");
                builder.newLine();
                builder.append("    ");
              }
            }
            builder.newLineIfNotEmpty();
            builder.append("    ");
            {
              boolean _isExclusiveMode = eqMetadata.isExclusiveMode();
              Iterable<FieldMetadata> _fieldsToInclude = _this.fieldsToInclude(typeDetails, _isExclusiveMode);
              for(org.springframework.roo.classpath.details.FieldMetadata field : _fieldsToInclude) {
                builder.newLineIfNotEmpty();
                builder.append("    ");
                builder.append(".append(");
                String _symbol = _this.symbol(field);
                builder.append(_symbol, "    ");
                builder.append(")");
                builder.newLineIfNotEmpty();
                builder.append("    ");
              }
            }
            builder.newLineIfNotEmpty();
            builder.append("    ");
            builder.append(".toHashCode();");
            builder.newLine();
            _xblockexpression = (builder);
          }
          return _xblockexpression;
        }
      };
    Method _method = Method.method(_signature, function);
    return _method;
  }
  
  public Iterable<FieldMetadata> fieldsToInclude(final ClassOrInterfaceTypeDetails typeDetails, final boolean exclusiveMode) {
    final ClassOrInterfaceTypeDetails typeConverted_typeDetails = (ClassOrInterfaceTypeDetails)typeDetails;
    List<? extends FieldMetadata> _declaredFields = typeConverted_typeDetails.getDeclaredFields();
    final Function1<FieldMetadata,Boolean> function = new Function1<FieldMetadata,Boolean>() {
        public Boolean apply(FieldMetadata f) {
          boolean _hasProperModifiers = _this.hasProperModifiers(f);
          boolean _shallInclude = _this.shallInclude(f, exclusiveMode);
          boolean _operator_and = BooleanExtensions.operator_and(_hasProperModifiers, _shallInclude);
          return ((Boolean)_operator_and);
        }
      };
    Iterable<FieldMetadata> _filter = IterableExtensions.filter(((java.util.List<org.springframework.roo.classpath.details.FieldMetadata>) _declaredFields), function);
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
    if (((Boolean)exclusiveMode)) {
      final FieldMetadata typeConverted_f = (FieldMetadata)f;
      String _name = de.saxsys.roo.equals.addon.EqualsInclude.class.getName();
      boolean _hasAnnotation = _this.hasAnnotation(typeConverted_f, _name);
      _xifexpression = _hasAnnotation;
    } else {
      final FieldMetadata typeConverted_f_1 = (FieldMetadata)f;
      String _name_1 = de.saxsys.roo.equals.addon.EqualsExclude.class.getName();
      boolean _hasAnnotation_1 = _this.hasAnnotation(typeConverted_f_1, _name_1);
      boolean _operator_not = BooleanExtensions.operator_not(_hasAnnotation_1);
      _xifexpression = _operator_not;
    }
    return _xifexpression;
  }
}