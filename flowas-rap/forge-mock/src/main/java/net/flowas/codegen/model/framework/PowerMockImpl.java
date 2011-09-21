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
package net.flowas.codegen.model.framework;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

import net.flowas.codegen.model.event.DocEvent;
import net.flowas.codegen.model.event.TechEvent;
import net.flowas.codegen.resource.GenEnum;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.parser.java.JavaSource;
import org.jboss.forge.project.Project;
import org.jboss.forge.project.facets.FacetNotFoundException;
import org.jboss.forge.project.facets.JavaSourceFacet;
//import org.jboss.forge.project.resources.builtin.java.JavaResource;
//import org.jboss.forge.project.util.Packages;


@Default
public class PowerMockImpl implements Technology {

	@Override
	public void isolate(String testname, Map<String, Set<String>> docList,
			Map<GenEnum, Object> template) {
		// TODO Auto-generated method stub
		
	}

	
}
