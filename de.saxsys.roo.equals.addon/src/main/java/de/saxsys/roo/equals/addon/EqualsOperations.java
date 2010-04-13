package de.saxsys.roo.equals.addon;

import org.springframework.roo.model.JavaType;

/**
 * Interface to {@link EqualsOperationsImpl}.
 * 
 * @author stefan.bley
 * @since 1.0.0
 */
public interface EqualsOperations {

	/**
	 * Returns if a project is available in the current Roo Shell.
	 */
	boolean isProjectAvailable();

	/**
	 * Provides equals() and hashCode() method for the given class.
	 * 
	 * @param typeName
	 *            a class
	 */
	void addEquals(JavaType typeName);
}
