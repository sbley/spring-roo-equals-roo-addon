package com.saxsys.roo.equals.roo.addon;

/**
 * Interface to {@link HashEqualsOperationsImpl}.
 * 
 * <p>
 * By now, only contains stub operations from the addon archetype.
 * </p>
 * 
 * @author stefan.bley
 */
public interface HashEqualsOperations {

	public boolean isProjectAvailable();

	/**
	 * @return true if the user's project has a /[name].txt file
	 */
	public boolean isTextFileAvailable(String name);

	public void writeTextFile(String name);

}
