package de.saxsys.roo.equals.addon;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.shell.CliAvailabilityIndicator;
import org.springframework.roo.shell.CliCommand;
import org.springframework.roo.shell.CliOption;
import org.springframework.roo.shell.CommandMarker;

/**
 * Commands for the roo-equals addon.
 * 
 */
@Component
@Service
public class EqualsCommands implements CommandMarker {

	@Reference
	private EqualsOperations operations;

	protected void activate(ComponentContext context) {
	}

	protected void deactivate(ComponentContext context) {
	}

	@CliAvailabilityIndicator("equals")
	public boolean isEqualsAvailable() {
		return operations.isProjectAvailable();
	}

	@CliCommand(value = "equals", help = "Add equals() and hashCode() methods to a class")
	public void addEquals(
			@CliOption(key = "class", mandatory = false, unspecifiedDefaultValue = "*", optionContext = "update,project", help = "The name of the class") JavaType typeName) {

		operations.addEquals(typeName);
	}

}