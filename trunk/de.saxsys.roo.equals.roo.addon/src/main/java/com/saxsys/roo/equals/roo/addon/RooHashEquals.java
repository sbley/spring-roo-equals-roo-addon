package de.saxsys.roo.equals.roo.addon;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides {@link Object#equals()} and {@link Object#hashCode()} methods if
 * requested.
 * 
 * @author stefan.bley
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface RooHashEquals {

}
