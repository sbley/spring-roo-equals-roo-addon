package de.saxsys.roo.equals.addon;

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

	/**
	 * @return true if the user's project has a /[name].txt file
	 */
	public boolean isTextFileAvailable(String name);

	public void writeTextFile(String name);

	/**
	 * @param propertyName
	 *            to obtain (required)
	 * @return a message that will ultimately be displayed on the shell
	 */
	public String getProperty(PropertyName propertyName);

}