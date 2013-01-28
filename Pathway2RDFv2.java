import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;

import org.apache.commons.io.FileUtils;
import org.bridgedb.BridgeDb;
import org.bridgedb.IDMapper;
import org.bridgedb.IDMapperException;
import org.bridgedb.IDMapperStack;
import org.bridgedb.bio.BioDataSource;
import org.pathvisio.model.Pathway;
import org.pathvisio.wikipathways.WikiPathwaysClient;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.DCTerms;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;


public class Pathway2RDFv2 {

	/**
	 * @param args
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ServiceException 
	 * @throws ClassNotFoundException 
	 * @throws IDMapperException 
	 * @throws ParseException 
	 */



	public static void setModelPrefix(Model model){
		model.setNsPrefix("biopax", Biopax_level3.getURI());
		model.setNsPrefix("gpml", Gpml.getURI());
		model.setNsPrefix("wp", Wp.getURI());
		model.setNsPrefix("xsd", XSD.getURI());
		model.setNsPrefix("rdf", RDF.getURI());
		model.setNsPrefix("rdfs", RDFS.getURI());
		model.setNsPrefix("dcterms", DCTerms.getURI());
		model.setNsPrefix("wprdf", "http://rdf.wikipathways.org/");
		model.setNsPrefix("pubmed", "http://www.ncbi.nlm.nih.gov/pubmed/");
		model.setNsPrefix("foaf", FOAF.getURI());
		model.setNsPrefix("ncbigene", "http://identifiers.org/ncbigene/");
		model.setNsPrefix("cas", "http://identifiers.org/cas/");
		model.setNsPrefix("dc", DC.getURI());
		model.setNsPrefix("skos", Skos.getURI());
	}

	public static Model createPathwayModel(){
		Model pathwayModel = ModelFactory.createDefaultModel();
		setModelPrefix(pathwayModel);
		return pathwayModel;
	}
	

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, ServiceException, ClassNotFoundException, IDMapperException, ParseException {
		
		int softwareVersion = 0;
		int schemaVersion = 0;
		int latestRevision = 0;
	
		 
		Model bridgeDbmodel = ModelFactory.createDefaultModel();
		InputStream in = new FileInputStream("/tmp/BioDataSource.ttl");
		bridgeDbmodel.read(in, "", "TURTLE");
		
		WikiPathwaysClient client = new WikiPathwaysClient(new URL(
		"http://www.wikipathways.org/wpi/webservice/webservice.php"));

		basicCalls.printMemoryStatus();

		//Map wikipathway organisms to NCBI organisms
		HashMap<String, String> organismTaxonomy = wpRelatedCalls.getOrganismsTaxonomyMapping();
		//HashMap<String, String> miriamSources = new HashMap<String, String>();
		//		HashMap<String, Str ing> miriamLinks = basicCalls.getMiriamUriBridgeDb();

		//Document wikiPathwaysDom = basicCalls.openXmlFile(args[0]);
		Document wikiPathwaysDom = basicCalls.openXmlFile("/tmp/WpGPML.xml");

		//initiate the Jena model to be populated
		Model model = ModelFactory.createDefaultModel();
		Model voidModel = ModelFactory.createDefaultModel();

		voidModel.setNsPrefix("xsd", XSD.getURI());
		voidModel.setNsPrefix("void", Void.getURI());
		voidModel.setNsPrefix("wprdf", "http://rdf.wikipathways.org/");
		voidModel.setNsPrefix("pav", Pav.getURI());
		voidModel.setNsPrefix("prov", Prov.getURI());
		voidModel.setNsPrefix("dcterms", DCTerms.getURI());
		voidModel.setNsPrefix("biopax", Biopax_level3.getURI());
		voidModel.setNsPrefix("gpml", Gpml.getURI());
		voidModel.setNsPrefix("wp", Wp.getURI());
		voidModel.setNsPrefix("foaf", FOAF.getURI());
		voidModel.setNsPrefix("hmdb", "http://identifiers.org/hmdb/");
		voidModel.setNsPrefix("freq", Freq.getURI());
		voidModel.setNsPrefix("dc", DC.getURI());
		setModelPrefix(model);

		//Populate void.ttl
		Calendar now = Calendar.getInstance();
		Literal nowLiteral = voidModel.createTypedLiteral(now);
		Literal titleLiteral = voidModel.createLiteral("WikiPathways-RDF VoID Description", "en");
		Literal descriptionLiteral = voidModel.createLiteral("This is the VoID description for a WikiPathwyas-RDF dataset.", "en");
		Resource voidBase = voidModel.createResource("http://rdf.wikipathways.org/");
		Resource identifiersOrg = voidModel.createResource("http://identifiers.org");
		Resource wpHomeBase = voidModel.createResource("http://www.wikipathways.org/");
		Resource authorResource = voidModel.createResource("http://semantics.bigcat.unimaas.nl/figshare/search_author.php?author=waagmeester");
		Resource apiResource = voidModel.createResource("http://www.wikipathways.org/wpi/webservice/webservice.php");
		Resource mainDatadump = voidModel.createResource("http://rdf.wikipathways.org/wpContent.ttl.gz");
		Resource license = voidModel.createResource("http://creativecommons.org/licenses/by/3.0/");
		Resource instituteResource = voidModel.createResource("http://dbpedia.org/page/Maastricht_University");
		voidBase.addProperty(RDF.type, Void.Dataset);
		voidBase.addProperty(DCTerms.title, titleLiteral);
		voidBase.addProperty(DCTerms.description, descriptionLiteral);
		voidBase.addProperty(FOAF.homepage, wpHomeBase);
		voidBase.addProperty(DCTerms.license, license);
		voidBase.addProperty(Void.uriSpace, voidBase);
		voidBase.addProperty(Void.uriSpace, identifiersOrg);
		voidBase.addProperty(Pav.importedBy, authorResource);
		voidBase.addProperty(Pav.importedFrom, apiResource);
		voidBase.addProperty(Pav.importedOn, nowLiteral);
		voidBase.addProperty(Void.dataDump, mainDatadump);
		voidBase.addProperty(Voag.frequencyOfChange, Freq.Irregular);
		voidBase.addProperty(Pav.createdBy, authorResource);
		voidBase.addProperty(Pav.createdAt, instituteResource);		 
		voidBase.addLiteral(Pav.createdOn, nowLiteral);
		voidBase.addProperty(DCTerms.subject, Biopax_level3.Pathway);
		voidBase.addProperty(Void.exampleResource, voidModel.createResource("http://identifiers.org/ncbigene/2678"));
		voidBase.addProperty(Void.exampleResource, voidModel.createResource("http://identifiers.org/pubmed/15215856"));
		voidBase.addProperty(Void.exampleResource, voidModel.createResource("http://identifiers.org/hmdb/HMDB02005"));
		voidBase.addProperty(Void.exampleResource, voidModel.createResource("http://rdf.wikipathways.org/WP15"));
		voidBase.addProperty(Void.exampleResource, voidModel.createResource("http://identifiers.org/obo.chebi/17242"));

		for (String organism : organismTaxonomy.values()) {
			voidBase.addProperty(DCTerms.subject, voidModel.createResource("http://dbpedia.org/page/"+organism.replace(" ", "_")));
		}
		voidBase.addProperty(Void.vocabulary, Biopax_level3.NAMESPACE);
		voidBase.addProperty(Void.vocabulary, voidModel.createResource(Wp.getURI()));
		voidBase.addProperty(Void.vocabulary, voidModel.createResource(Gpml.getURI()));
		voidBase.addProperty(Void.vocabulary, FOAF.NAMESPACE);
		voidBase.addProperty(Void.vocabulary, Pav.NAMESPACE);
		//Custom Properties
		String baseUri = "http://rdf.wikipathways.org/";
		NodeList pathwayElements = wikiPathwaysDom.getElementsByTagName("Pathway");
		
		//BioDataSource.init();
		for (int i=0; i<pathwayElements.getLength(); i++){
			Model pathwayModel = createPathwayModel();
			String wpId = pathwayElements.item(i).getAttributes().getNamedItem("identifier").getTextContent();
			String revision = pathwayElements.item(i).getAttributes().getNamedItem("revision").getTextContent();
			String pathwayOrganism = "";
			if (pathwayElements.item(i).getAttributes().getNamedItem("Organism") != null)
				pathwayOrganism = pathwayElements.item(i).getAttributes().getNamedItem("Organism").getTextContent().trim();
            if (Integer.valueOf(revision) > latestRevision){
            	latestRevision = Integer.valueOf(revision);
            }
			File f = new File("/tmp/"+args[0]+"/"+wpId+"_r"+revision+".ttl");
			System.out.println("Start");
			System.out.println(f.getName());
			if(!f.exists()) {
				System.out.println("1");
				Resource voidPwResource = wpRelatedCalls.addVoidTriples(voidModel, voidBase, pathwayElements.item(i), client);
				Resource pwResource = wpRelatedCalls.addPathwayLevelTriple(pathwayModel, pathwayElements.item(i), organismTaxonomy);
				
				// Get the comments
				NodeList commentElements = ((Element) pathwayElements.item(i)).getElementsByTagName("Comment");
				wpRelatedCalls.addCommentTriples(pathwayModel, pwResource, commentElements, wpId, revision);
				// Get the Groups
				NodeList groupElements = ((Element) pathwayElements.item(i)).getElementsByTagName("Group");
				for (int n=0;n<groupElements.getLength(); n++){
					wpRelatedCalls.addGroupTriples(pathwayModel, pwResource, groupElements.item(n), wpId, revision);
				}
				// Get all the Datanodes
				NodeList dataNodesElement = ((Element) pathwayElements.item(i)).getElementsByTagName("DataNode");
				for (int j=0; j<dataNodesElement.getLength(); j++){
					wpRelatedCalls.addDataNodeTriples(pathwayModel, pwResource, dataNodesElement.item(j), wpId, revision, bridgeDbmodel);
				}
				// Get all the lines
				NodeList linesElement = ((Element) pathwayElements.item(i)).getElementsByTagName("Line");
				for (int k=0; k<linesElement.getLength(); k++){
					wpRelatedCalls.addLineTriples(pathwayModel, pwResource, linesElement.item(k), wpId, revision);
				}
				//Get all the labels
				NodeList labelsElement = ((Element) pathwayElements.item(i)).getElementsByTagName("Label");
				for (int l=0; l<labelsElement.getLength(); l++){
					wpRelatedCalls.addLabelTriples(pathwayModel, pwResource, labelsElement.item(l), wpId, revision);
				}
				System.out.println("5");
				NodeList referenceElements = ((Element) pathwayElements.item(i)).getElementsByTagName("bp:PublicationXref");
				for (int m=0; m<referenceElements.getLength(); m++){
					wpRelatedCalls.addReferenceTriples(pathwayModel, pwResource, referenceElements.item(m), wpId, revision);
				}
				NodeList referenceElements2 = ((Element) pathwayElements.item(i)).getElementsByTagName("bp:publicationXref");
				for (int m=0; m<referenceElements2.getLength(); m++){
					wpRelatedCalls.addReferenceTriples(pathwayModel, pwResource, referenceElements2.item(m), wpId, revision);
				}
				NodeList referenceElements3 = ((Element) pathwayElements.item(i)).getElementsByTagName("bp:PublicationXRef");
				for (int m=0; m<referenceElements3.getLength(); m++){
					wpRelatedCalls.addReferenceTriples(pathwayModel, pwResource, referenceElements3.item(m), wpId, revision);
				}

				NodeList ontologyElements = ((Element) pathwayElements.item(i)).getElementsByTagName("bp:openControlledVocabulary");
				for (int n=0; n<ontologyElements.getLength();n++){
					wpRelatedCalls.addPathwayOntologyTriples(pathwayModel, pwResource, ontologyElements.item(n));
				}
				System.out.println(wpId);
				basicCalls.saveRDF2File(pathwayModel, "/tmp/"+args[0]+"/"+wpId+"_r"+revision+".ttl", "TURTLE");

				model.add(pathwayModel);
				pathwayModel.removeAll();
			}
		}
		Date myDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String myDateString = sdf.format(myDate);
		FileUtils.writeStringToFile(new File("latestVersion.txt"), "v"+schemaVersion+"."+softwareVersion+"."+latestRevision+"_"+myDateString);
		basicCalls.saveRDF2File(model, "/tmp/wpContent_v"+schemaVersion+"."+softwareVersion+"."+latestRevision+"_"+myDateString+".ttl", "TURTLE");
		basicCalls.saveRDF2File(voidModel, "/tmp/void.ttl", "TURTLE");
	}

}
