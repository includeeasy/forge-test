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
package net.flowas.codegen.resource;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

public class ResourceRepository {
	static OWLOntologyManager manager;
	static OWLReasoner reasoner;
	static String iri = "http://flowas.com/ontology/utdemo.owl#";

	// static String file =
	// TestPlugin.class.getResource("utdemo.owl").toExternalForm();

	public static void main(String[] args) {
		System.out
				.println(getTemplate("javax.pefrsistence.Persistence:createEntityManagerFactory(java.lang.String)"));
	}

	public static Map<GenEnum, Object> getTemplate(String name) {
		HashMap<GenEnum, Object> map = new HashMap<GenEnum, Object>();
		try {
			if (null == reasoner) {
				init();
			}

			OWLDataFactory dFactory = manager.getOWLDataFactory();
			OWLNamedIndividual model = dFactory.getOWLNamedIndividual(IRI
					.create(iri + name));

			OWLDataProperty dataProperty = dFactory.getOWLDataProperty(IRI
					.create(iri + "body"));
			Set<OWLLiteral> bodyList = reasoner.getDataPropertyValues(model,
					dataProperty);
			for (OWLLiteral text : bodyList) {
				map.put(GenEnum.BODY, text.getLiteral());
			}
			OWLDataProperty importProperty = dFactory.getOWLDataProperty(IRI
					.create(iri + "import"));
			Set<OWLLiteral> importList = reasoner.getDataPropertyValues(model,
					importProperty);
			for (OWLLiteral text : importList) {
				// mod.setImports(text.getLiteral());
				map.put(GenEnum.IMPORTS, text.getLiteral());
			}
		} catch (OWLOntologyCreationException ex) {
			Logger.getLogger(ResourceRepository.class.getName()).log(
					Level.SEVERE, null, ex);
		} catch (NullPointerException e) {
			// TODO: handle exception
			System.out.println("Hermit error!");
		}
		return map;
	}

	public static Map<String, String> getCommand() {
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			if (null == reasoner) {
				init();
			}
		} catch (OWLOntologyCreationException ex) {
			Logger.getLogger(ResourceRepository.class.getName()).log(
					Level.SEVERE, null, ex);
		}
		OWLDataFactory dFactory = manager.getOWLDataFactory();
		OWLNamedIndividual model = dFactory.getOWLNamedIndividual(IRI
				.create(iri + "TestGen"));
		OWLObjectProperty objectProperty = dFactory.getOWLObjectProperty(IRI
				.create(iri + "include"));
		NodeSet<OWLNamedIndividual> bodyList = reasoner
				.getObjectPropertyValues(model, objectProperty);
		OWLDataProperty helpProperty = dFactory.getOWLDataProperty(IRI
				.create(iri + "help"));
		for (OWLNamedIndividual individ : bodyList.getFlattened()) {
			Set<OWLLiteral> helpList = reasoner.getDataPropertyValues(individ,
					helpProperty);
			String help = "";
			for (OWLLiteral text : helpList) {
				help += text.getLiteral();
			}
			map.put(individ.getIRI().getFragment().replace(iri, ""), help);
		}
		return map;
	}

	public static void init() throws OWLOntologyCreationException {
		manager = OWLManager.createOWLOntologyManager();
		// SimpleIRIMapper novelMapper = new SimpleIRIMapper(IRI.create(iri),
		// IRI.create(file));
		// manager.addIRIMapper(novelMapper);
		OWLOntology ontology = manager
				.loadOntologyFromOntologyDocument(Settings.class
						.getResourceAsStream("utdemo.owl"));// .loadOntology(IRI.create(file));
		ReasonerFactory factory = new ReasonerFactory();
		Configuration configuration = new Configuration();
		configuration.throwInconsistentOntologyException = false;
		reasoner = factory.createReasoner(ontology, configuration);
	}
}
