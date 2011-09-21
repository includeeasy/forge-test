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
import java.io.IOException;
import java.util.List;

import net.flowas.codegen.model.OperationUtils.UnzipFilter;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.DefaultModelReader;
import org.jboss.forge.project.Project;
import org.jboss.forge.project.dependencies.DependencyBuilder;
import org.jboss.forge.project.facets.DependencyFacet;


public class MavenFilter implements UnzipFilter {
	String file;
	Project project;

	public MavenFilter(Project project) {
		this.project = project;
	}

	@Override
	public String getFileName() {
		return file;
	}

	@Override
	public byte[] filte(String fileName, byte[] data) {
		file = fileName;
		if (file.contains("pom.xml")) {
			DependencyFacet maven = project.getFacet(DependencyFacet.class);
			DefaultModelReader r = new DefaultModelReader();
			try {
				Model model = r.read(new ByteArrayInputStream(data), null);
				List<Dependency> deps = model.getDependencies();
				for (Dependency dep : deps) {
					String scope=dep.getScope()==null?"": ":" + dep.getScope();
					org.jboss.forge.project.dependencies.Dependency dependency = DependencyBuilder
							.create(dep.getGroupId() + ":"
									+ dep.getArtifactId() + ":"
									+ dep.getVersion() +scope);
					if (!maven.hasDependency(dependency)) {
						maven.addDependency(dependency);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		return data;
	}

}
