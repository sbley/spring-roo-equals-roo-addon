package de.saxsys.roo.equals.addon;

import org.eclipse.xtext.xtend2.lib.StringConcatenation;
import org.springframework.roo.classpath.itd.InvocableMemberBodyBuilder;

public class FakedBodyBuilder extends InvocableMemberBodyBuilder {
	private StringConcatenation sc;

	public FakedBodyBuilder(StringConcatenation sc) {
		this.sc = sc;
	}
	
	@Override
	public String getOutput() {
		StringConcatenation builder = new StringConcatenation();
		//By default, we indent method bodies with 2 tabs
		builder.append("        ");
		builder.append(sc,"        ");
		builder.newLineIfNotEmpty();
		return builder.toString();
	}
	
}
