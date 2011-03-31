package de.saxsys.roo.equals.addon;

import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xtend2.lib.StringConcatenation;

public class Method {
	private MethodSignature signature;
	private Function0<StringConcatenation> body;
	public Method(MethodSignature signature, Function0<StringConcatenation> body) {
		super();
		this.signature = signature;
		this.body = body;
	}
	public MethodSignature getSignature() {
		return signature;
	}
	public void setSignature(MethodSignature signature) {
		this.signature = signature;
	}
	public Function0<StringConcatenation> getBody() {
		return body;
	}
	public void setBody(Function0<StringConcatenation> body) {
		this.body = body;
	}
	
	public static Method method(MethodSignature signature, Function0<StringConcatenation> body){
		return new Method(signature, body);
	}
	
}
