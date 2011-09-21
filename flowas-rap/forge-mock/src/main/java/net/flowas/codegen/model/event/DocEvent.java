/**
 * Copyright (C) 2010 http://flowas.net/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * You can write to flowas@gmial.com for more customer requirement.
 */
package net.flowas.codegen.model.event;

import java.util.Set;

import org.jboss.forge.parser.java.JavaClass;


public  class DocEvent {
	private String className;
	private Set<String> methodList;
	private JavaClass underOperation;
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Set<String> getMethodList() {
		return methodList;
	}

	public void setMethodList(Set<String> methodList) {
		this.methodList = methodList;
	}

	public JavaClass getUnderOperation() {
		return underOperation;
	}

	public void setUnderOperation(JavaClass underOperation) {
		this.underOperation = underOperation;
	}

	
}
