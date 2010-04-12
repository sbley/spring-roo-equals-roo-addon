package de.saxsys.roo.equals.addon;

import org.springframework.roo.model.JavaType;

/**
 * Interface to {@link EqualsOperationsImpl}.
 * 
 * <p>
 * By now, only contains stub operations from the addon archetype.
 * </p>
 * 
 * @author stefan.bley
 */
public interface EqualsOperations {

	public boolean isProjectAvailable();

	public void addEquals(JavaType typeName);


}
