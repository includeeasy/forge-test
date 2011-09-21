/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package net.flowas.codegen.forge;

import java.io.FileNotFoundException;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.project.Facet;
import org.jboss.forge.project.Project;
import org.jboss.forge.project.dependencies.Dependency;
import org.jboss.forge.project.dependencies.DependencyBuilder;
import org.jboss.forge.project.facets.DependencyFacet;
import org.jboss.forge.project.facets.JavaSourceFacet;
import org.jboss.forge.project.facets.ResourceFacet;
import org.jboss.forge.shell.plugins.Alias;
import org.jboss.forge.shell.plugins.RequiresFacet;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Alias("flowas.mock")
@RequiresFacet({ JavaSourceFacet.class, ResourceFacet.class,
		DependencyFacet.class })
//@RequiresPackagingTypes({ PackagingType.JAR, PackagingType.WAR })
public class MockFacet implements Facet {
	private static final Dependency dep = DependencyBuilder
			.create("org.powermock:powermock-api-mockito:1.4.8:test");
	private Project project;

	@Override
	public boolean install() {
		if (!isInstalled()) {
			DependencyFacet deps = project.getFacet(DependencyFacet.class);
			if (!deps.hasDependency(dep)) {
				deps.addDependency(dep);
			}

			installUtils();

		}
		project.registerFacet(this);
		return isInstalled();
	}

	private void installUtils() {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		JavaClass util = JavaParser
				.parse(JavaClass.class,
						loader.getResourceAsStream("org/jboss/seam/forge/jpa/PersistenceUtil.jtpl"));
		JavaClass producer = JavaParser
				.parse(JavaClass.class,
						loader.getResourceAsStream("org/jboss/seam/forge/jpa/DatasourceProducer.jtpl"));

		JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);

		try {
			java.saveJavaSource(producer);
			java.saveJavaSource(util);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isInstalled() {
		DependencyFacet deps = project.getFacet(DependencyFacet.class);
		boolean hasDependency = deps.hasDependency(dep);
		return hasDependency;
	}

	@Override
	public Project getProject() {
		return project;
	}

	@Override
	public void setProject(final Project project) {
		this.project = project;
	}

	@Override
	public boolean uninstall() {
		// TODO Auto-generated method stub
		return false;
	}
}
