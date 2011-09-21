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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.flowas.codegen.model.OperationUtils.UnzipFilter;

import org.jboss.forge.project.Project;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


@Named
@ApplicationScoped
public class ExcuteEngine {

	@Inject
	OperationUtils operation;
	
	@Inject
	Project project;	

	public void project(Element e) {
		invoke_children(e);
	}	
	private void invoke_children(Element e) {
		NodeList nodes = e.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node instanceof Element) {
				Element element = (Element) node;
				System.out.println("process node :  " + element.getNodeName());
				Method method;
				try {
					method = ExcuteEngine.class.getDeclaredMethod(
							element.getNodeName(), Element.class);
					if (method != null) {
						method.invoke(this, element);
					}
				} catch (SecurityException e1) {
					e1.printStackTrace();
				} catch (NoSuchMethodException e1) {
					// e1.printStackTrace();
					continue;
				} catch (IllegalArgumentException ex) {
					ex.printStackTrace();
				} catch (IllegalAccessException ex) {
					ex.printStackTrace();
				} catch (InvocationTargetException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
    
	private UnzipFilter warFilter() {
		UnzipFilter filter = new UnzipFilter() {
			String file;

			@Override
			public byte[] filte(String fileName, byte[] data) {
				if (fileName.endsWith("web.xml")
						|| (fileName.length() > 8 && "META-INF".equals(fileName
								.substring(0, 8)))
						|| fileName.contains("WEB-INF/classes")
						|| fileName.contains("WEB-INF\\classes")) {
					return null;
				} else {
					file = "src/main/webapp/" + fileName;
					return data;
				}

			}

			@Override
			public String getFileName() {
				return file;
			}
		};
		return filter;
	}

	private UnzipFilter initwarFilter() {
		UnzipFilter filter = new UnzipFilter() {
			String file;

			@Override
			public byte[] filte(String fileName, byte[] data) {
				file = "src/main/webapp/" + fileName;
				return data;
			}

			@Override
			public String getFileName() {
				return file;
			}
		};
		return filter;
	}

	/**
	 * process preditor(acewiki) tag in xml
	 * 
	 * @param e
	 */
	public void preditor(Element e) {
		String value = e.getAttribute("zip");
		System.out.println(value);
		if (value != null) {
			InputStream file = this.getClass()
					.getResourceAsStream(value);
			try {
				if (value.endsWith(".zip")) {
					operation.unzip(file);
				} else if (value.endsWith(".war")) {
					operation.unzip(file, warFilter());
				}
				// other

			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		invoke_children(e);
	}

	/**
	 * process x3dom tag in xml
	 * 
	 * @param e
	 */
	public void x3dom(Element e) {
		String value = e.getAttribute("zip");
		System.out.println(value);
		if (value != null && value.endsWith(".zip")) {
			InputStream file = this.getClass()			
					.getResourceAsStream(value);
			try {
				operation.unzip(file);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		invoke_children(e);
	}

	/**
	 * process netlogo tag in xml
	 * 
	 * @param e
	 */
	public void netlogo(Element e) {
		String value = e.getAttribute("zip");
		System.out.println(value);
		if (value != null && value.endsWith(".zip")) {
			InputStream file = this.getClass()
			.getResourceAsStream(value);
			try {
				operation.unzip(file);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		invoke_children(e);
	}

	/**
	 * process web tag in xml
	 * 
	 * @param e
	 */
	public void initweb(Element e) {
		String value = e.getAttribute("zip");
		System.out.println(value);
		if (value != null) {
			InputStream file = this.getClass()
			.getResourceAsStream(value);
			try {
				if (value.endsWith(".zip")) {
					operation.unzip(file);
				} else if (value.endsWith(".war")) {
					operation.unzip(file, initwarFilter());
				}
				// other

			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		invoke_children(e);
	}

	/**
	 * process achieve tag in xml
	 * 
	 * @param e
	 */
	public void achieve(Element e) {
		invoke_children(e);
	}

	/**
	 * process maven tag in xml
	 * 
	 * @param e
	 */
	public void maven(Element e) {
		String value = e.getAttribute("zip");
		System.out.println(value);
		if (value != null && value.endsWith(".zip")) {
			InputStream file = this.getClass()
			.getResourceAsStream(value);
			try {
				operation.unzip(file,new MavenFilter(project));
			} catch (IOException e1) {				
				e1.printStackTrace();
			}
		}
		invoke_children(e);		
	}	
}
