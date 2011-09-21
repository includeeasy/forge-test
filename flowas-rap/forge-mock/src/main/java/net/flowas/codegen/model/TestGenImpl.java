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
package net.flowas.codegen.model;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import net.flowas.codegen.model.event.DocEvent;
import net.flowas.codegen.model.event.TechEvent;
import net.flowas.codegen.model.framework.TestFramework;
import net.flowas.codegen.resource.Settings;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.parser.java.JavaSource;
import org.jboss.forge.project.facets.FacetNotFoundException;
import org.jboss.forge.project.facets.JavaSourceFacet;
//import org.jboss.forge.project.resources.builtin.java.JavaResource;
//import org.jboss.forge.project.util.Packages;
import org.jboss.forge.resources.java.JavaResource;
import org.jboss.forge.shell.Shell;
import org.jboss.forge.shell.util.Packages;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.flowas.generic.utils.XmlUtils;

//@Default
public class TestGenImpl implements TestGen {
	@Inject
	Shell shell;
	
	@Inject
	Event<DocEvent> docEvent;

	@Inject
	Event<TechEvent> techEvent;
    
	@Inject
	TestFramework testFramework ;
	@Override
	public void generateFor(File sutClass) {
		shell.println(sutClass.getName());
	}

	@Override
	public void isolateDOC(File sutClass) {
		shell.println(sutClass.getName());
	}

	@Override
	public void sample(File sutClass, String name) {		
		shell.println("TODO Auto-generated method stub");
	}

	@Override
	public void fromXML(File xmlfile) {
		try {
			Document doc = XmlUtils.getDocumentBuilder().parse(xmlfile);
			Element testElemet = XmlUtils.findFirstElement("/root/test-class",
					doc.getDocumentElement());
			String testname = testElemet.getTextContent().trim();
			
			List<Element> docNodes = XmlUtils.findElements("/root/depend-on-component/class",
					doc.getDocumentElement());
			Map<String,Set<String>> docList=new HashMap<String,Set<String>>();
			for(Element docElemet:docNodes){
				String classname = docElemet.getAttribute("name");
		        NodeList ms = docElemet.getElementsByTagName("method");				
				Set<String> methods=new HashSet<String>();
		        for (int i = 0; i < ms.getLength(); i++) {
					Element meth = (Element) ms.item(i);
					methods.add(meth.getTextContent().trim());
				}
		        docList.put(classname, methods);
			}	        
	        isolate(testname, docList);	              
		} catch (SAXException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param testname
	 * @param docList
	 * @param template
	 */
	private void isolate(String testname, Map<String, Set<String>> docList) {
		String path = Packages.toFileSyntax(testname) + ".java";
		JavaClass cl = null;		
		try {
			JavaResource javaSource = shell.getCurrentProject().getFacet(JavaSourceFacet.class)
					.getTestJavaResource(path);
			if (javaSource.exists()) {
				cl = (JavaClass) javaSource.getJavaSource();				
			} else {
				String body = testFramework.getSample(testname);
				String newName = testname;
				if (testname.contains(".")) {
					String[] array = newName.split("\\.");
					newName = array[array.length - 1];					
				}		
				if (body != null) {					
					JavaSource<JavaClass> clo = JavaParser.parse(
							JavaClass.class,
							new ByteArrayInputStream(body.getBytes()));
					cl = clo.getOrigin();									
				} else {
					cl = JavaParser.create(JavaClass.class);
					cl.setPublic();
				}
				if (testname.contains(".")) {
					cl.setPackage(testname.replace("." + newName, ""));	
				}			
			}
		} catch (FacetNotFoundException e1) {
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}		
		// dependent of component
		for(String classname:docList.keySet()){			
			DocEvent event = new DocEvent();
			event.setClassName(classname);
			event.setMethodList(docList.get(classname));
			event.setUnderOperation(cl);
			docEvent.fire(event);
		}		
		// tech event
		TechEvent tech=new TechEvent();
		tech.setUnderOperation(cl);
		tech.setName(Settings.getMockFramework());
		techEvent.fire(tech);
		JavaSourceFacet java = shell.getCurrentProject().getFacet(JavaSourceFacet.class);
		try {
			java.saveTestJavaSource(cl);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
