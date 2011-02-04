package de.saxsys.roo.equals.addon;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides {@link Object#equals(Object)} and {@link Object#hashCode()} methods
 * if requested.
 * 
 * @author stefan.bley
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface RooEquals {
	/**
	 * If {@code true} then inserts calls to the super class equals()
	 * and hashCode() methods.
	 */
	boolean callSuper() default false;

	/**
	 * <p>
	 * If {@code true} then performs class comparison in equals() method using
	 * {@code instanceof} rather than {@code getClass()}.
	 * </p>
	 * <p>
	 * Defaults to {@code false}.
	 * </p>
	 */
	boolean callInstanceof() default false;
	
	/**
	 * If set to {@code true}, exclude all fields except the ones annotated with {@link EqualsInclude}.
	 * If set to {@code false}, include all fields except for ones annotated with {@link EqualsExclude}.
	 * <p>
	 * Defaults to {@code false}.
	 * </p> 
	 */
	boolean exclusiveMode() default false;
}
