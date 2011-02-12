package de.saxsys.roo.equals.addon;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Fields annotated with EqualsInclude will be included in equals/hashCode
 * regardless of {@link RooEquals#exclusiveMode() exclusiveMode} settings.
 * 
 * @since 1.3.1
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface EqualsInclude {
}
