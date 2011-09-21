package net.flowas.codegen.model.event;

import java.util.Map;

import org.jboss.forge.parser.java.JavaClass;

import net.flowas.codegen.resource.GenEnum;

public class TechEvent {
	GenEnum name;
	private JavaClass underOperation;
	public JavaClass getUnderOperation() {
		return underOperation;
	}
	public void setUnderOperation(JavaClass underOperation) {
		this.underOperation = underOperation;
	}
	String dependncy;
	Map<GenEnum,String> annotation;
	public GenEnum getName() {
		return name;
	}
	public void setName(GenEnum name) {
		this.name = name;
	}
	public String getDependncy() {
		return dependncy;
	}
	public void setDependncy(String dependncy) {
		this.dependncy = dependncy;
	}
	public Map<GenEnum, String> getAnnotation() {
		return annotation;
	}
	public void setAnnotation(Map<GenEnum, String> annotation) {
		this.annotation = annotation;
	}
}
