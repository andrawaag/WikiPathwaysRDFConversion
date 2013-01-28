

import java.io.FileOutputStream;
import java.io.IOException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFList;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.OWL2;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.VCARD;


public class wpExternalMapping {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			//Below are the Prefix of vocab who can't be imported through jena schemagen
			String citoUri = "http://purl.org/spar/cito/";
			String dbPediaUri = "http://live.dbpedia.org/page/";
			String chemInf = Cheminf.NS;

			Model model = ModelFactory.createDefaultModel();

			Resource wpBasicInhibition = model.createResource(Wp.BasicInhibition);
			//wpBasicInhibition.addProperty(RDF.type, Biopax_level3.)
			wpBasicInhibition.addProperty(Skos.relatedMatch, Biopax_level3.Modulation);

			Resource wpBasicInteraction = model.createResource(Wp.BasicInteraction);
			wpBasicInteraction.addProperty(Skos.relatedMatch, Biopax_level3.Interaction);

			Resource wpBiologicalEntity = model.createResource(Wp.BiologicalEntity);
			wpBiologicalEntity.addProperty(Skos.relatedMatch, Biopax_level3.Entity);

			Resource wpCell = model.createResource(Wp.Cell);
            wpCell.addProperty(Skos.relatedMatch, dbPediaUri+"Cell_%28biology%29");
			
			Resource wpCompartment = model.createResource(Wp.Compartment);
			Resource dbPediaCompartment = model.createResource(dbPediaUri+"Cellular_compartment");
			wpCompartment.addProperty(Skos.relatedMatch, dbPediaCompartment);

			Resource wpComplex = model.createResource(Wp.Complex);
			wpComplex.addProperty(Skos.relatedMatch, Biopax_level3.Complex);

			Resource wpControlledVocabulary = model.createResource(Wp.ControlledVocabulary);
			wpControlledVocabulary.addProperty(Skos.relatedMatch, Biopax_level3.ControlledVocabulary);
			Biopax_level3.ControlledVocabulary.addProperty(Skos.relatedMatch, RDF.type);
            //TODO discuss

			Resource wpCurationTag = model.createResource(Wp.CurationTag);
			wpCurationTag.addProperty(RDF.type, DC.description);

			Resource wpDataNode = model.createResource(Wp.DataNode);
			wpDataNode.addProperty(Skos.relatedMatch, Biopax_level3.PhysicalEntity);

			Resource wpDatasourceReference = model.createResource(Wp.DatasourceReference);
			Resource citoReference = model.createResource(citoUri+"isCitedAsDataSourceBy");
			wpDatasourceReference.addProperty(Skos.relatedMatch, citoReference);

			Resource wpEndoplasmicReticulum = model.createResource(Wp.EndoplasmicReticulum);
			Resource dbPediaEndoplasmicReticulum = model.createResource(dbPediaUri+"EndoplasmicReticulum");
			wpEndoplasmicReticulum.addProperty(Skos.relatedMatch, dbPediaEndoplasmicReticulum);

			Resource wpEntity = model.createResource(Wp.Entity);
			wpEntity.addProperty(RDF.type, OWL2.Class);

			Resource wpExtracellular = model.createResource(Wp.Extracellular);
			Resource dbPediaExtracellular = model.createResource(dbPediaUri+"Extracellular");
			wpExtracellular.addProperty(Skos.relatedMatch, dbPediaExtracellular);

			// Inspired by: http://answers.semanticweb.com/questions/12177/how-do-you-map-an-object-to-be-either-class-a-or-b-but-no-other-class-in-rdf
			Resource wpGeneProduct = model.createResource(Wp.GeneProduct);
			RDFNode[] geneProductelems = new RDFNode[] { Biopax_level3.Dna,
					Biopax_level3.Protein, Biopax_level3.Rna};
			RDFList geneProductRDFNode = model.createList(geneProductelems);
			wpGeneProduct.addProperty(OWL2.disjointUnionOf, geneProductRDFNode);
			
			wpGeneProduct.addProperty(Skos.broadMatch, Biopax_level3.Protein);
			wpGeneProduct.addProperty(Skos.broadMatch, Biopax_level3.Dna);
			wpGeneProduct.addProperty(Skos.broadMatch, Biopax_level3.Rna);
			

			Resource wpGolgiApparatus = model.createResource(Wp.GolgiApparatus);
			Resource dbPediaGolgiApparatus = model.createResource(dbPediaUri+"Golgi_apparatus");
			wpGolgiApparatus.addProperty(Skos.relatedMatch, dbPediaGolgiApparatus); 

			Resource wpGroup = model.createResource(Wp.Group);

			Resource wpInteraction = model.createResource(Wp.Interaction);
			wpInteraction.addProperty(Skos.relatedMatch, Biopax_level3.Interaction);

			Resource wpLabel = model.createResource(Wp.Label);

			Resource wpMetabolite = model.createResource(Wp.Metabolite);
			wpMetabolite.addProperty(RDFS.subClassOf, Biopax_level3.SmallMolecule);
			// TODO chebi
			
			Resource wpMIMBinding = model.createResource(Wp.MIMBinding);
			wpMIMBinding.addProperty(Skos.relatedMatch, Biopax_level3.MolecularInteraction);
			
			Resource wpMIMBranchingLeft = model.createResource(Wp.MIMBranchingLeft);

			Resource wpMIMBranchingRight = model.createResource(Wp.MIMBranchingRight);

			Resource wpMIMCatalysis = model.createResource(Wp.MIMCatalysis);
			wpMIMCatalysis.addProperty(RDFS.subClassOf, Biopax_level3.Catalysis);

			Resource wpMIMCleavage = model.createResource(Wp.MIMCleavage);

			Resource wpMIMConversion = model.createResource(Wp.MIMConversion);
			wpMIMConversion.addProperty(RDFS.subClassOf, Biopax_level3.Conversion);

			Resource wpMIMCovalentBond = model.createResource(Wp.MIMCovalentBond);
			wpMIMCovalentBond.addProperty(RDFS.subClassOf, Biopax_level3.CovalentBindingFeature);

			Resource wpMIMGap = model.createResource(Wp.MIMGap);

			Resource wpMIMInteraction = model.createResource(Wp.MIMInteraction);
		    wpMIMInteraction.addProperty(Skos.relatedMatch, Biopax_level3.Interaction);
		    
			Resource wpMIMInhibition = model.createResource(Wp.MIMInhibition);
			wpMIMInhibition.addProperty(Skos.relatedMatch, Biopax_level3.Modulation);

			Resource wpMIMModification = model.createResource(Wp.MIMModification);
			wpMIMModification.addProperty(RDFS.subClassOf, Biopax_level3.ModificationFeature);

			Resource wpMIMNecessaryStimulation = model.createResource(Wp.MIMNecessaryStimulation);

			Resource wpMIMStimulation = model.createResource(Wp.MIMStimulation);

			Resource wpMIMTranscriptionTranslation = model.createResource(Wp.MIMTranscriptionTranslation);
			wpMIMTranscriptionTranslation.addProperty(Skos.relatedMatch, Biopax_level3.TemplateReaction);
			
			Resource wpMitochondria = model.createResource(Wp.Mitochondria);
			Resource dbPediaMitochondria = model.createResource("http://dbpedia.org/page/Mitochondrion");
			wpMitochondria.addProperty(Skos.relatedMatch, dbPediaMitochondria);

			Resource wpNucleus = model.createResource(Wp.Nucleus);
			Resource dbPediaNucleus = model.createResource("http://dbpedia.org/page/Cell_nucleus");
			wpNucleus.addProperty(Skos.relatedMatch, dbPediaNucleus);

			Resource wpOrganelle = model.createResource(Wp.Organelle);
			Resource dbPediaOrganelle = model.createResource("http://dbpedia.org/page/Organelle");
			wpOrganelle.addProperty(Skos.relatedMatch, dbPediaOrganelle);

			Resource wpPathway = model.createResource(Wp.Pathway);
			wpPathway.addProperty(Skos.relatedMatch, Biopax_level3.Pathway);

			Resource wpPathwayAnnotation = model.createResource(Wp.PathwayAnnotation);

			Resource wpPathwayVersion = model.createResource(Wp.PathwayVersion);

			Resource wpPerson = model.createResource(Wp.Person);
			wpPerson.addProperty(Skos.relatedMatch, FOAF.Person);


			Resource wpProtein = model.createResource(Wp.Protein);
			wpProtein.addProperty(Skos.relatedMatch, Biopax_level3.Protein);

			Resource wpPublicationReference = model.createResource(Wp.PublicationReference);
			wpPublicationReference.addProperty(Skos.relatedMatch, bibo.AcademicArticle);
            wpPublicationReference.addProperty(Skos.relatedMatch, Biopax_level3.PublicationXref);

			Resource wpReference = model.createResource(Wp.Reference);
			wpReference.addProperty(Skos.relatedMatch, Biopax_level3.Xref);
			wpReference.addProperty(Skos.relatedMatch, citoReference);

			Resource wpRNA = model.createResource(Wp.RNA);
			wpRNA.addProperty(Skos.relatedMatch, Biopax_level3.Rna);

			Resource wpSarcoPlasmicReticulum = model.createResource(Wp.SarcoplasmicReticulum);
			Resource dbPediaEndoPlasmicReticulum = model.createResource("http://dbpedia.org/page/Endoplasmic_reticulum");
			wpSarcoPlasmicReticulum.addProperty(RDFS.subClassOf, dbPediaEndoPlasmicReticulum);
			//TDOD Discuss Sarcoplasmic Reticulum refers to Endoplasmic Reticulum in WikiPedia

			//Resource wpUnknown = model.createResource(Wp.Unknown);
			//TODO add GPML link

			Resource wpUtilityClass = model.createResource(Wp.UtilityClass);
			//TODO Discuss

			Resource wpVesicle = model.createResource(Wp.Vesicle);
			Resource dbPediaVesicle = model.createResource("http://dbpedia.org/page/Vesicle");
			wpVesicle.addProperty(Skos.relatedMatch, dbPediaVesicle);

			Property wpEmail = model.createProperty(Wp.email.getURI());
			wpEmail.addProperty(Skos.relatedMatch, VCARD.EMAIL);


			Property wpHasIdentifier = model.createProperty(Wp.hasIdentifier.getURI());
			wpHasIdentifier.addProperty(Skos.relatedMatch, DC.identifier);

			Property wpHasPathway = model.createProperty(Wp.hasPathway.getURI());

			Property wpRealName = model.createProperty(Wp.realName.getURI());
			wpRealName.addProperty(Skos.relatedMatch, FOAF.name);

			Property wpRevision = model.createProperty(Wp.revision.getURI());

			Property wpUsername = model.createProperty(Wp.username.getURI());
			wpUsername.addProperty(Skos.relatedMatch, FOAF.accountName);
			
			
			//Resource gpml = model.createResource(Gpml.NS+);
			
			

			FileOutputStream fout;
			fout = new FileOutputStream("/tmp/wpExternalMappings.ttl");
			model.write(fout, "TURTLE");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
