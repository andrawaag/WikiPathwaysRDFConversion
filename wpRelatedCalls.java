import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;

import org.bridgedb.BridgeDb;
import org.bridgedb.DataSource;
import org.bridgedb.IDMapper;
import org.bridgedb.IDMapperException;
import org.bridgedb.IDMapperStack;
import org.bridgedb.Xref;
import org.bridgedb.bio.BioDataSource;
import org.pathvisio.model.ConverterException;
import org.pathvisio.model.DataNodeType;
import org.pathvisio.model.GraphLink.GraphIdContainer;
import org.pathvisio.wikipathways.WikiPathwaysClient;
import org.pathvisio.wikipathways.webservice.WSPathway;
import org.pathvisio.wikipathways.webservice.WSPathwayHistory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.DCTerms;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;


import de.fuberlin.wiwiss.ng4j.NamedGraphSet;
import de.fuberlin.wiwiss.ng4j.impl.NamedGraphSetImpl;
import de.fuberlin.wiwiss.ng4j.swp.vocabulary.FOAF;


public class wpRelatedCalls {
	public static WikiPathwaysClient startWpApiClient() throws MalformedURLException, ServiceException {
		return new WikiPathwaysClient(new URL("http://www.wikipathways.org/wpi/webservice/webservice.php"));
	}

	public static Document addWpProvenance(Document currentGPML, String wpIdentifier, String wpRevision) throws ConverterException, ParserConfigurationException, SAXException, IOException{	
		Element pathwayElement = (Element) currentGPML.getElementsByTagName("Pathway").item(0);
		pathwayElement.setAttribute("identifier", wpIdentifier);
		pathwayElement.setAttribute("revision", wpRevision);
		return currentGPML;
	}

	public static HashMap<String, String> getOrganismsTaxonomyMapping() throws ServiceException, ParserConfigurationException, SAXException, IOException{
		HashMap<String, String> hm = new HashMap<String, String>();
		WikiPathwaysClient wpClient = startWpApiClient();
		String[] wpOrganisms = wpClient.listOrganisms();
		for (String organism : wpOrganisms){
			Document taxonomy = basicCalls.openXmlURL(constants.getEUtilsUrl("taxonomy", organism.replace(" ", "_")));
			String ncbiTaxonomy = taxonomy.getElementsByTagName("Id").item(0).getTextContent().trim();
			hm.put(organism, ncbiTaxonomy);
		}
		return hm;
	}

	public static Resource addVoidTriples(Model voidModel, Resource voidBase, Node pathwayNode, WikiPathwaysClient client) throws ParseException, RemoteException{
		String wpIdentifier = pathwayNode.getAttributes().getNamedItem("identifier").getTextContent().trim();
		String wpRevision = pathwayNode.getAttributes().getNamedItem("revision").getTextContent().trim();
		String pathwayName = pathwayNode.getAttributes().getNamedItem("Name").getTextContent().trim();
		String pathwayOrganism ="";
		if (pathwayNode.getAttributes().getNamedItem("Organism") !=null) {
			pathwayOrganism = pathwayNode.getAttributes().getNamedItem("Organism").getTextContent().trim();
		} 
		Resource voidPwResource = voidModel.createResource("http://rdf.wikipathways.org/"+wpIdentifier);
		Resource pwResource = voidModel.createResource("http://www.wikipathways.org/index.php/Pathway:"+wpIdentifier);
		voidPwResource.addProperty(FOAF.page, pwResource);
		voidPwResource.addProperty(DCTerms.title, voidModel.createLiteral(pathwayName, "en"));
		voidPwResource.addProperty(RDF.type, Void.Dataset);
		voidBase.addProperty(Void.subset, voidPwResource);
		voidPwResource.addLiteral(Pav.version, wpRevision);
		voidPwResource.addLiteral(DCTerms.subject, pathwayOrganism);
		Resource subDatadump = voidModel.createResource("http://rdf.wikipathways.org/"+wpIdentifier);
		voidBase.addProperty(Void.dataDump, subDatadump);

		//obtain history
		/* DateFormat formatter;
		Date date;
		formatter = new SimpleDateFormat("yyyymmdd");
		date = (Date) formatter.parse("20000101");
		WSPathwayHistory pathwayHistory = client.getPathwayHistory(wpIdentifier, date);
		for (int i = 0; i < pathwayHistory.getHistory().length; i++) {

			String user = pathwayHistory.getHistory(i).getUser();
			String version = pathwayHistory.getHistory(i).getRevision();
			int year = Integer.parseInt(pathwayHistory.getHistory(i).getTimestamp().substring(0, 4));
			int month = Integer.parseInt(pathwayHistory.getHistory(i).getTimestamp().substring(5, 6));
			int day = Integer.parseInt(pathwayHistory.getHistory(i).getTimestamp().substring(7, 8));
			//System.out.println("void year:"+year);
			//System.out.println("void month:"+month);
			//System.out.println("void day:"+day);

			Calendar cal1 = new GregorianCalendar(year, month, day);	

			voidPwResource.addProperty(Pav.authoredOn, voidModel.createTypedLiteral(cal1));
			Resource wpUser = voidModel
			.createResource("http://www.wikipathways.org/index.php/User:"
					+ user.replace(" ", "_")); //TODO Needs to change to VIVO instance
			voidPwResource.addProperty(Pav.contributedBy, wpUser);
			if (version != wpRevision) {
				voidPwResource.addProperty(Pav.previousVersion, version);
			}
		}
		 */
		return voidPwResource;
	}

	public static Resource addPathwayLevelTriple(Model model, Node pathwayNode, HashMap<String, String> organismMap) throws IOException{
		String wpIdentifier = pathwayNode.getAttributes().getNamedItem("identifier").getTextContent().trim();
		String wpRevision = pathwayNode.getAttributes().getNamedItem("revision").getTextContent().trim();
		String pathwayName = pathwayNode.getAttributes().getNamedItem("Name").getTextContent().trim();
		String pathwayOrganism = "";
		if (pathwayNode.getAttributes().getNamedItem("Organism") != null)
			pathwayOrganism = pathwayNode.getAttributes().getNamedItem("Organism").getTextContent().trim();

		System.out.println(wpIdentifier);
		Resource pathwayResource = model.createResource("http://rdf.wikipathways.org/Pathway/"+wpIdentifier+"_r"+wpRevision);
		Resource pathwayIdentifier = model.createResource("http://identifiers.org/wikipathways/"+wpIdentifier+"_r"+wpRevision);
		pathwayResource.addProperty(FOAF.page, model.createResource(constants.getWikiPathwaysURL()+wpIdentifier+"_r"+wpRevision));
		pathwayResource.addProperty(RDF.type, Wp.Pathway);
		pathwayResource.addProperty(RDF.type, Skos.Collection);
		pathwayResource.addProperty(DC.identifier, pathwayIdentifier);
		pathwayResource.addLiteral(Pav.version, wpRevision);
		pathwayResource.addLiteral(DC.title, model.createLiteral(pathwayName, "en"));
		Resource organismResource = model.createResource("http://purl.obolibrary.org/obo/NCBITaxon_"+ organismMap.get(pathwayOrganism));
		organismResource.addLiteral(RDFS.label, pathwayOrganism);
		pathwayResource.addProperty(Wp.organism, organismResource);	
		if (((Element) pathwayNode).getElementsByTagName("BiopaxRef").getLength()>0){
			String biopaxRef = ((Element) pathwayNode).getElementsByTagName("BiopaxRef").item(0).getTextContent().trim();
			pathwayResource.addProperty(Gpml.biopaxref, biopaxRef);	
		}	
		if (((Element) pathwayNode).getElementsByTagName("BiopaxRef").getLength()==0){
			basicCalls.appendToFile("/tmp/noBiopaxRef.log", wpIdentifier+"\n");
		}
		if (((Element) pathwayNode).getElementsByTagName("Graphics").item(0).getAttributes().getNamedItem("BoardHeight") != null)
		{
			Float height = Float.valueOf(((Element) pathwayNode).getElementsByTagName("Graphics").item(0).getAttributes().getNamedItem("BoardHeight").getTextContent().trim().trim()).floatValue();
			Float width = Float.valueOf(((Element) pathwayNode).getElementsByTagName("Graphics").item(0).getAttributes().getNamedItem("BoardWidth").getTextContent().trim().trim()).floatValue();
			pathwayResource.addLiteral(Gpml.width, width);
			pathwayResource.addLiteral(Gpml.height, height);
		}
		if (((Element) pathwayNode).getElementsByTagName("Graphics").item(0).getAttributes().getNamedItem("BoardHeight") == null)
		{
			basicCalls.appendToFile("/tmp/noBoardDimensions.log", wpIdentifier+"\n");
		}

		return pathwayResource;
	}

	public static void addDataNodeTriples(Model model, Resource pwResource, Node dataNode, String wpId, String revId, Model bridgeDbModel, IDMapper mapper) throws IOException, IDMapperException{
		String dataNodeLabel = dataNode.getAttributes().getNamedItem("TextLabel").getTextContent().trim();
		String dataNodeType="";
		if (dataNode.getAttributes().getNamedItem("Type") != null){ 
			dataNodeType = dataNode.getAttributes().getNamedItem("Type").getTextContent().trim();
		}
		String dataNodeGroupRef = "";
		if (dataNode.getAttributes().getNamedItem("GroupRef") != null) {
			dataNodeGroupRef = dataNode.getAttributes().getNamedItem("GroupRef").getTextContent().trim();
		}
		String dataNodeDataSource = ((Element) dataNode).getElementsByTagName("Xref").item(0).getAttributes().getNamedItem("Database").getTextContent().trim();
		String dataNodeIdentifier = ((Element) dataNode).getElementsByTagName("Xref").item(0).getAttributes().getNamedItem("ID").getTextContent().trim().replace(" ", "_");
 		Float dataNodeGraphicsCenterX = Float.valueOf(((Element) dataNode).getElementsByTagName("Graphics").item(0).getAttributes().getNamedItem("CenterX").getTextContent().trim());
		Float dataNodeGraphicsCenterY = Float.valueOf(((Element) dataNode).getElementsByTagName("Graphics").item(0).getAttributes().getNamedItem("CenterY").getTextContent().trim());
		Float dataNodeGraphicsHeight = Float.valueOf(((Element) dataNode).getElementsByTagName("Graphics").item(0).getAttributes().getNamedItem("Height").getTextContent().trim());
		Float dataNodeGraphicsWidth = Float.valueOf(((Element) dataNode).getElementsByTagName("Graphics").item(0).getAttributes().getNamedItem("Width").getTextContent().trim());
		String dataNodeDataColor = null;
		if (((Element) dataNode).getElementsByTagName("Graphics").item(0).getAttributes().getNamedItem("Color")!=null)
			dataNodeDataColor = ((Element) dataNode).getElementsByTagName("Graphics").item(0).getAttributes().getNamedItem("Color").getTextContent().trim();

		String dataNodeZorder=null;
		if (((Element) dataNode).getElementsByTagName("Graphics").item(0).getAttributes().getNamedItem("ZOrder")!=null)
			dataNodeZorder = ((Element) dataNode).getElementsByTagName("Graphics").item(0).getAttributes().getNamedItem("ZOrder").getTextContent().trim();
		Property mainUrlProperty = bridgeDbModel.createProperty("http://openphacts.cs.man.ac.uk:9090//ontology/DataSource.owl#mainUrl");
		//String mainUrl = bridgeDbModel.listObjectsOfProperty(mainUrlProperty).next().toString();

		String sparqlQueryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
		"			PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" + 
		"			PREFIX dc: <http://purl.org/dc/terms/>\n" + 
		"			PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + 
		"			PREFIX schema: <http://schema.org/>\n" + 
		"			PREFIX bridgeDb: <http://openphacts.cs.man.ac.uk:9090//ontology/DataSource.owl#>\n" + 
		"			SELECT DISTINCT  ?identifiers_org_base ?urlPattern ?bio2rdf ?origrdf\n" + 
		"			WHERE {\n" + 
		"			?datasource bridgeDb:fullName \""+dataNodeDataSource+"\" .\n" + 
		"			OPTIONAL {?datasource bridgeDb:urnBase ?urnBase .}\n" + 
		"			OPTIONAL {?datasource bridgeDb:code ?code .}\n" + 
		"			OPTIONAL {?datasource bridgeDb:mainUrl ?mainUrl .}\n" + 
		"			OPTIONAL {?datasource bridgeDb:type ?bdtype .}\n" + 
		"			?datasource bridgeDb:identifiers_org_base ?identifiers_org_base .\n" + 
		"			OPTIONAL {?datasource bridgeDb:urlPattern ?urlPattern .}\n" + 
		"			OPTIONAL {?datasource bridgeDb:shortName ?shortName .}\n" + 
		"			OPTIONAL {?datasource bridgeDb:bio2RDF ?bio2rdf.}\n" + 
		"			OPTIONAL {?datasource bridgeDb:sourceRDFURI ?origrdf.}\n" + 
		"			}";
		Query query = QueryFactory.create(sparqlQueryString);
		QueryExecution queryExecution = QueryExecutionFactory.create(query, bridgeDbModel);

		ResultSet resultSet = queryExecution.execSelect();
		String sourceRDFURI=null;
		String bio2RdfURI=null;
		String nonRDFURIURI=null;
		String identifiersorgURI=null;
		while (resultSet.hasNext()) {
			QuerySolution solution = resultSet.next();
			if (solution.get("origrdf") != null) {
				sourceRDFURI = solution.get("origrdf").toString();
			}
			if (solution.get("bio2rdf") != null){
				bio2RdfURI = solution.get("bio2rdf").toString();
			}
			if (solution.get("urlPattern") != null){
				nonRDFURIURI = solution.get("urlPattern").toString();
			}
			if (solution.get("identifiers_org_base") != null){
				identifiersorgURI = solution.get("identifiers_org_base").toString();
			}
		}
		String conceptUrl = "$id"; // The ConceptUrl is the main URI for a given skos:concept in WikiPathways
		if (sourceRDFURI!= null) {
			conceptUrl = sourceRDFURI;
		} else if (bio2RdfURI != null){
			conceptUrl = bio2RdfURI;
		} else if (nonRDFURIURI != null){
			conceptUrl = nonRDFURIURI;
		}

		String dataNodeGraphId = "";
		if (dataNode.getAttributes().getNamedItem("GraphId")!=null){
			dataNodeGraphId = dataNode.getAttributes().getNamedItem("GraphId").getTextContent().trim();
		}

		Resource internalWPDataNodeResource = model.createResource("http://rdf.wikipathways.org/Pathway/"+wpId+"_r"+revId+"/DataNode/"+dataNodeGraphId);
		Resource dataNodeResource = model.createResource(conceptUrl.replace("$id", dataNodeIdentifier));
		Resource identifiersOrgResource= model.createResource();
		if (dataNodeDataSource == ""){
			internalWPDataNodeResource = model.createResource("http://rdf.wikipathways.org/Pathway/"+wpId+"_r"+revId+"/noDatasource/"+ UUID.randomUUID());
			internalWPDataNodeResource.addProperty(RDF.type, Gpml.requiresCurationAttention);
			internalWPDataNodeResource.addLiteral(RDFS.comment, "This URI represents a DataNode in GPML where there is no DataSource set. ");
			if (dataNodeIdentifier==""){
				identifiersOrgResource = model.createResource("http://rdf.wikipathways.org/Pathway/"+wpId+"_r"+revId+"/DataNode/noIdentifier");
				identifiersOrgResource.addProperty(RDF.type, Gpml.requiresCurationAttention);
				identifiersOrgResource.addLiteral(RDFS.comment, "This URI represents a DataNode in GPML where there is no Identifier given set. ");
			} else {
			}
		}
		else {
			//System.out.println(dataNodeIdentifier);
			if ((dataNodeIdentifier=="") || (dataNodeIdentifier==null)){
				internalWPDataNodeResource= model.createResource(conceptUrl.replace("$id", "noIdentifier"));
				identifiersOrgResource = model.createResource("http://rdf.wikipathways.org/Pathway/"+wpId+"_r"+revId+"/DataNode/noIdentifier");
				identifiersOrgResource.addLiteral(RDFS.comment, "This URI represents a DataNode in GPML where there is no Identifier given set. ");
				internalWPDataNodeResource.addProperty(RDF.type, Gpml.requiresCurationAttention);
			} else {
				internalWPDataNodeResource= model.createResource(conceptUrl.replace("$id", dataNodeIdentifier));
				identifiersOrgResource = model.createResource(identifiersorgURI + dataNodeIdentifier);
			}

		}
		Xref idXref = new Xref(dataNodeIdentifier, DataSource.getByFullName(dataNodeDataSource));
		if (dataNodeType != ""){
			if (dataNodeType.equals("GeneProduct")){
				internalWPDataNodeResource.addProperty(RDF.type, Wp.GeneProduct);
				Set<Xref> unifiedEnsemblIdXref = mapper.mapID(idXref, BioDataSource.ENSEMBL);
				Iterator<Xref> iter = unifiedEnsemblIdXref.iterator();
				while (iter.hasNext()){
					Xref unifiedId = (Xref) iter.next();
					String unifiedEnsemblDataNodeIdentifier = unifiedId.getId();
				    Resource unifiedEnsemblIdResource = model.createResource("http://identifiers.org/ensembl/"+unifiedEnsemblDataNodeIdentifier);
				    internalWPDataNodeResource.addProperty(Wp.bdbEnsembl, unifiedEnsemblIdResource);
				}
				Set<Xref> unifiedUniprotIdXref = mapper.mapID(idXref, DataSource.getBySystemCode("S"));
				Iterator<Xref> iterUniprot = unifiedUniprotIdXref.iterator();
				while (iter.hasNext()){
					Xref unifiedUniprotId = (Xref) iterUniprot.next();
					String unifiedUniprotDataNodeIdentifier = unifiedUniprotId.getId();
				    Resource unifiedUniprotIdResource = model.createResource("http://identifiers.org/uniprot/"+unifiedUniprotDataNodeIdentifier);
				    internalWPDataNodeResource.addProperty(Wp.bdbUniprot, unifiedUniprotIdResource);
				}
				Set<Xref> unifiedEntrezGeneIdXref = mapper.mapID(idXref, BioDataSource.ENTREZ_GENE);
				Iterator<Xref> iterEntrezGene = unifiedEntrezGeneIdXref.iterator();
				while (iterEntrezGene.hasNext()){
					Xref unifiedEntrezGeneId = (Xref) iterEntrezGene.next();
					String unifiedEntrezGeneDataNodeIdentifier = unifiedEntrezGeneId.getId();
				    Resource unifiedEntrezGeneIdResource = model.createResource("http://identifiers.org/entrez.gene/"+unifiedEntrezGeneDataNodeIdentifier);
				    internalWPDataNodeResource.addProperty(Wp.bdbEntrezGene, unifiedEntrezGeneIdResource);
				}
				/*
				Set<Xref> seeAlsoXRef = mapper.mapID(idXref);
				Iterator<Xref> iter2 = seeAlsoXRef.iterator();
				while(iter2.hasNext()){
					Xref seeAlsoId = (Xref) iter2.next();
					seeAlsoIdentifier = seeAlsoId.getId();
					sparqlQueryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +  
					"			PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" + 
					"			PREFIX dc: <http://purl.org/dc/terms/>\n" + 
					"			PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + 
					"			PREFIX schema: <http://schema.org/>\n" + 
					"			PREFIX bridgeDb: <http://openphacts.cs.man.ac.uk:9090//ontology/DataSource.owl#>\n" + 
					"			SELECT DISTINCT  ?identifiers_org_base\n" +
					"			WHERE {\n" +
					"				?datasource bridgeDb:identifiers_org_base ?identifiers_org_base .\n" +
					"			}";
					Query query2 = QueryFactory.create(sparqlQueryString);
					QueryExecution queryExecution2 = QueryExecutionFactory.create(query2, bridgeDbModel);

					ResultSet resultSet2 = queryExecution2.execSelect();
					while (resultSet2.hasNext()) {
						QuerySolution solution = resultSet2.next();
						if (solution.get("identifiers_org_base") != null){
							Resource identifiersorgSeeAlsoURI = model.createResource(solution.get("identifiers_org_base").toString().replace("$id",seeAlsoIdentifier));
							dataNodeResource.addProperty(RDFS.seeAlso, identifiersorgSeeAlsoURI);
						}
						
					}
					
					
				}*/
				
			}
			if (dataNodeType.equals("Metabolite")){
				internalWPDataNodeResource.addProperty(RDF.type, Wp.Metabolite);
				Set<Xref> unifiedHmdbIdXref = mapper.mapID(idXref, BioDataSource.HMDB);
				Iterator<Xref> iterHmdb = unifiedHmdbIdXref.iterator();
				while (iterHmdb.hasNext()){
					Xref unifiedHmdbId = (Xref) iterHmdb.next();
				    String unifiedHmdbDataNodeIdentifier = unifiedHmdbId.getId();
				    Resource unifiedHmdbIdResource = model.createResource("http://identifiers.org/hmdb/"+unifiedHmdbDataNodeIdentifier);
				    internalWPDataNodeResource.addProperty(Wp.bdbHmdb, unifiedHmdbIdResource);
				}
				Set<Xref> unifiedChemspiderIdXref = mapper.mapID(idXref, BioDataSource.CHEMSPIDER);
				Iterator<Xref> iterChemspider = unifiedChemspiderIdXref.iterator();
				while (iterChemspider.hasNext()){
					Xref unifiedChemspiderId = (Xref) iterChemspider.next();
				    String unifiedChemspiderDataNodeIdentifier = unifiedChemspiderId.getId();
				    Resource unifiedChemspiderIdResource = model.createResource("http://identifiers.org/chemspider/"+unifiedChemspiderDataNodeIdentifier);
				    internalWPDataNodeResource.addProperty(Wp.bdbChemspider, unifiedChemspiderIdResource);
				}
				/*Set<Xref> seeAlsoXRef = mapper.mapID(idXref);
				Iterator<Xref> iter2 = seeAlsoXRef.iterator();
				while(iter2.hasNext()){
					Xref seeAlsoId = (Xref) iter2.next();
					seeAlsoIdentifier = seeAlsoId.getId();
					sparqlQueryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
					"			PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" + 
					"			PREFIX dc: <http://purl.org/dc/terms/>\n" + 
					"			PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + 
					"			PREFIX schema: <http://schema.org/>\n" + 
					"			PREFIX bridgeDb: <http://openphacts.cs.man.ac.uk:9090//ontology/DataSource.owl#>\n" + 
					"			SELECT DISTINCT  ?identifiers_org_base\n" +
					"			WHERE {\n" + 
					"			    ?datasource bridgeDb:identifiers_org_base ?identifiers_org_base .\n" +
					"			}";
					Query query2 = QueryFactory.create(sparqlQueryString);
					QueryExecution queryExecution2 = QueryExecutionFactory.create(query2, bridgeDbModel);

					ResultSet resultSet2 = queryExecution2.execSelect();
					while (resultSet2.hasNext()) {
						QuerySolution solution = resultSet2.next();
						if (solution.get("identifiers_org_base") != null){
							Resource identifiersorgSeeAlsoURI = model.createResource(solution.get("identifiers_org_base").toString().replace("$id",seeAlsoIdentifier));
							dataNodeResource.addProperty(RDFS.seeAlso, identifiersorgSeeAlsoURI);
						}
						
					}
					
					
				}*/
			}
			if (dataNodeType.equals("Pathway")){
				internalWPDataNodeResource.addProperty(RDF.type, Wp.Pathway);
			}
			if (dataNodeType.equals("Protein")){
				internalWPDataNodeResource.addProperty(RDF.type, Wp.Protein);
			}
			if (dataNodeType.equals("Complex")){
				internalWPDataNodeResource.addProperty(RDF.type, Wp.Complex);
			}
		}
		
		
		internalWPDataNodeResource.addProperty(RDFS.subClassOf, dataNodeResource);
		internalWPDataNodeResource.addProperty(DC.identifier, identifiersOrgResource);
		internalWPDataNodeResource.addLiteral(DCTerms.identifier, dataNodeIdentifier);
		if (dataNodeGroupRef != ""){

			Resource groupRefResource = model.createResource("http://rdf.wikipathways.org/Pathway/"+wpId+"_r"+revId+"/group/"+dataNodeGroupRef); 
			groupRefResource.addProperty(DCTerms.isPartOf, pwResource);
			internalWPDataNodeResource.addProperty(DCTerms.isPartOf, groupRefResource);
		}
		//Mapping to GPML
		internalWPDataNodeResource.addLiteral(DC.source, dataNodeDataSource);
		internalWPDataNodeResource.addProperty(RDF.type, Gpml.DataNode);
		internalWPDataNodeResource.addProperty(RDF.type, Skos.Concept);
		internalWPDataNodeResource.addProperty(DCTerms.isPartOf, pwResource);

		internalWPDataNodeResource.addLiteral(RDFS.label, model.createLiteral(dataNodeLabel, "en"));
		internalWPDataNodeResource.addLiteral(Gpml.centerx, dataNodeGraphicsCenterX);
		internalWPDataNodeResource.addLiteral(Gpml.centery, dataNodeGraphicsCenterY);
		internalWPDataNodeResource.addLiteral(Gpml.height, dataNodeGraphicsHeight);
		internalWPDataNodeResource.addLiteral(Gpml.width, dataNodeGraphicsWidth);
		internalWPDataNodeResource.addLiteral(Gpml.graphid, dataNodeGraphId);
		if (dataNodeDataColor != null)
			internalWPDataNodeResource.addLiteral(Gpml.color, dataNodeDataColor);
		if (dataNodeZorder!= null)
			internalWPDataNodeResource.addLiteral(Gpml.zorder, dataNodeZorder);

		internalWPDataNodeResource.addProperty(RDFS.isDefinedBy, Gpml.DataNode);

	}

	public static void addLineTriples(Model model, Resource pwResource, Node lineNode, String wpId, String revId){
		// Make Line Resource
		String graphId = String.valueOf(UUID.randomUUID()); //No graphRef set.
		if (lineNode.getAttributes().getNamedItem("GraphId")!=null){
			graphId = lineNode.getAttributes().getNamedItem("GraphId").getTextContent().trim();
		}
		Resource lineResource = model.createResource("http://rdf.wikipathways.org/Pathway/"+wpId+"_r"+revId+"/Line/"+graphId);
		lineResource.addLiteral(Gpml.graphid, graphId);
		if (lineNode.getAttributes().getNamedItem("GraphId")==null){
			lineResource.addProperty(RDF.type, Gpml.requiresCurationAttention);
		}
		NodeList lineGraphics = ((Element) lineNode).getElementsByTagName("Graphics");
		Float lineGraphicsLineThickness=null;
		if (((Element) lineNode).getElementsByTagName("Graphics").item(0).getAttributes().getNamedItem("LineThickness") != null) {
			lineGraphicsLineThickness = Float.valueOf(((Element) lineNode).getElementsByTagName("Graphics").item(0).getAttributes().getNamedItem("LineThickness").getTextContent().trim());
		}
		int zOrder = 0;
		if (lineGraphics.item(0).getAttributes().getNamedItem("ZOrder") != null){
			zOrder = Integer.valueOf(lineGraphics.item(0).getAttributes().getNamedItem("ZOrder").getTextContent().trim());
			lineResource.addLiteral(Gpml.zorder, zOrder);
		}
		lineResource.addProperty(DCTerms.isPartOf, pwResource);
		lineResource.addProperty(RDF.type, Gpml.Line);
		if (lineGraphicsLineThickness!=null){
			lineResource.addLiteral(Gpml.linethickness, lineGraphicsLineThickness);
		}
		//Point Nodes and its attributes
		NodeList points = ((Element) lineGraphics.item(0)).getElementsByTagName("Point");
		List<String> graphRefs = new ArrayList<String>();
		List<String> arrowHeads = new ArrayList<String>();
		List<String> arrowTowards = new ArrayList<String>(); 	
		for (int i=0; i<points.getLength(); i++){
			String arrowHead = "";
			if (points.item(i).getAttributes().getNamedItem("ArrowHead")!= null){ 
				arrowHead = points.item(i).getAttributes().getNamedItem("ArrowHead").getTextContent().trim();

			}
			String graphRef = "";
			if (points.item(i).getAttributes().getNamedItem("GraphRef") != null){
				graphRef = points.item(i).getAttributes().getNamedItem("GraphRef").getTextContent().trim();
				//TODO add graphref to model
			}
			Float relX =null;
			Float relY = null;
			if ((points.item(i).getAttributes().getNamedItem("RelX") != null) &&  (points.item(i).getAttributes().getNamedItem("RelY") != null)) {
				relX =Float.valueOf(points.item(i).getAttributes().getNamedItem("RelX").getTextContent().trim());
				relY =Float.valueOf(points.item(i).getAttributes().getNamedItem("RelY").getTextContent().trim());
				lineResource.addLiteral(Gpml.relX, relX);
				lineResource.addLiteral(Gpml.relY, relY);
			}
			Float x =null;
			Float y = null;
			if ((points.item(i).getAttributes().getNamedItem("RelX") != null) &&  (points.item(i).getAttributes().getNamedItem("RelY") != null)) {
				x =Float.valueOf(points.item(i).getAttributes().getNamedItem("RelX").getTextContent().trim());
				y =Float.valueOf(points.item(i).getAttributes().getNamedItem("RelY").getTextContent().trim());
			}

			if (arrowHead !=""){
				lineResource.addProperty(Gpml.arrowTowards, graphRef);
				lineResource.addLiteral(Gpml.arrowHead, arrowHead);
				arrowHeads.add(arrowHead);
				arrowTowards.add(graphRef);
			}
			if ((graphRef != null) && (graphRef != "" )){
				lineResource.addLiteral(Gpml.graphref, graphRef);
				graphRefs.add(graphRef);
			}
		}
		/*if (graphRefs.size() == 2) {
			Resource interactionResource = model.createResource("http://rdf.wikipathways.org/Pathway/\"+wpId+\"_r\"+revId+\"/Interaction/"+UUID.randomUUID());
			interactionResource.addProperty(DCTerms.isPartOf, pwResource);
			interactionResource.addProperty(RDF.type, Wp.Interaction);
			interactionResource.addProperty(DCTerms.format, lineResource);
			if (arrowHeads.size() == 1){
				if (arrowHeads.get(0).equals("Arrow"))
					interactionResource.addProperty(RDF.type, Wp.DirectedInteraction);
				if (arrowHeads.get(0).equals("mim-catalysis"))
					interactionResource.addProperty(RDF.type, Wp.Catalysis);
				if (arrowHeads.get(0).equals("mim-stimulation"))
					interactionResource.addProperty(RDF.type, Wp.Stimulation);
				if (arrowHeads.get(0).equals("mim-conversion"))
					interactionResource.addProperty(RDF.type, Wp.Conversion);
				if (arrowHeads.get(0).equals("mim-transcription-translation"))
					interactionResource.addProperty(RDF.type, Wp.TranscriptionTranslation);
				if (arrowHeads.get(0).equals("mim-inhibition"))
					interactionResource.addProperty(RDF.type, Wp.Inhibition);
				if (arrowHeads.get(0).equals("mim-catalysis"))
					interactionResource.addProperty(RDF.type, Wp.Catalysis);
				if (arrowHeads.get(0).equals("mim-necesssary-stimulation"))
					interactionResource.addProperty(RDF.type, Wp.NecessaryStimulation);
				if (arrowHeads.get(0).equals("mim-binding"))
					interactionResource.addProperty(RDF.type, Wp.Binding);
				if (arrowHeads.get(0).equals("mim-cleavage"))
					interactionResource.addProperty(RDF.type, Wp.Cleavage);
				if (arrowHeads.get(0).equals("mim-modification"))
					interactionResource.addProperty(RDF.type, Wp.Modification);
				if (arrowHeads.get(0).equals("mim-gap"))
					interactionResource.addProperty(RDF.type, Wp.Gap);

			}

			String queryString = 
				"PREFIX gpml: <http://vocabularies.wikipathways.org/gpml#> " +
				"PREFIX dc:      <http://purl.org/dc/elements/1.1/> " +
				"SELECT * " +
				"WHERE {" +
				"      ?node gpml:graphid \""+graphRefs.get(0)+"\" . " +
				"      ?node dc:identifier ?nodeIdentifier . " +
				"      }";

			Query query = QueryFactory.create(queryString);
			QueryExecution qe = QueryExecutionFactory.create(query, model);
			ResultSet resultSet = qe.execSelect();
			while (resultSet.hasNext()) {
				QuerySolution solution = resultSet.next();
				if (solution.get("nodeIdentifier") != null) {
					Resource nodeIdentifierResource = model.createResource(solution.get("nodeIdentifier").toString());
					interactionResource.addProperty(Wp.hasParticipant, nodeIdentifierResource);
				}
			}
			queryString = 
				"PREFIX gpml: <http://vocabularies.wikipathways.org/gpml#> " +
				"PREFIX dc:      <http://purl.org/dc/elements/1.1/> " +
				"SELECT * " +
				"WHERE {" +
				"      ?node gpml:graphid \""+graphRefs.get(1)+"\" . " +
				"      ?node dc:identifier ?nodeIdentifier . " +
				"      }";
			query = QueryFactory.create(queryString);
			qe = QueryExecutionFactory.create(query, model);
			resultSet = qe.execSelect();
			while (resultSet.hasNext()) {
				QuerySolution solution = resultSet.next();
				if (solution.get("nodeIdentifier") != null) {
					Resource nodeIdentifierResource = model.createResource(solution.get("nodeIdentifier").toString());
					interactionResource.addProperty(Wp.hasParticipant, nodeIdentifierResource);
				}
			}
			for (int j=0; j<arrowTowards.size(); j++){
				queryString = 
					"PREFIX gpml: <http://vocabularies.wikipathways.org/gpml#> " +
					"PREFIX dc:      <http://purl.org/dc/elements/1.1/> " +
					"SELECT * " +
					"WHERE {" +
					"      ?node gpml:graphid \""+arrowTowards.get(j)+"\" . " +
					"      ?node dc:identifier ?nodeIdentifier . " +
					"      }";

				query = QueryFactory.create(queryString);
				qe = QueryExecutionFactory.create(query, model);
				resultSet = qe.execSelect();
				while (resultSet.hasNext()) {
					QuerySolution solution = resultSet.next();
					if (solution.get("nodeIdentifier") != null) {
						Resource nodeIdentifierResource = model.createResource(solution.get("nodeIdentifier").toString());
						interactionResource.addProperty(Wp.interactionTarget, nodeIdentifierResource);						
					}
				}
			}
		} */

		NodeList anchors = ((Element) lineGraphics.item(0)).getElementsByTagName("Anchor");
		for (int i=0; i<anchors.getLength(); i++){
			String anchorGraphId = "";
			if ( anchors.item(i).getAttributes().getNamedItem("GraphId") != null){
				anchorGraphId = anchors.item(i).getAttributes().getNamedItem("GraphId").getTextContent().trim();
			}
			String anchorPosition = "";
			if (anchors.item(i).getAttributes().getNamedItem("Position")!=null){
				anchorPosition = anchors.item(i).getAttributes().getNamedItem("Position").getTextContent().trim();
			}
			String anchorShape = anchors.item(i).getAttributes().getNamedItem("Shape").getTextContent().trim();
			//TODO add Anchor to GPML
			Boolean uuidset = false;
			if (anchorGraphId == ""){
				anchorGraphId = String.valueOf(UUID.randomUUID());
				uuidset = true;
			}
			Resource anchorResource = model.createResource("http://rdf.wikipathways.org/Pathway/"+wpId+"_r"+revId+"/Line/"+graphId+"/anchor/"+anchorGraphId);
			if (uuidset){
				anchorResource.addProperty(RDF.type, Gpml.requiresCurationAttention);
				anchorResource.addLiteral(RDFS.comment, "This anchor does not have a graphId set");
			}
			anchorResource.addProperty(RDF.type, Gpml.Anchor);
			anchorResource.addLiteral(Gpml.graphid, anchorGraphId);
			if (anchorPosition != ""){
				anchorResource.addLiteral(Gpml.anchorPosition, anchorPosition);
			}
			anchorResource.addLiteral(Gpml.anchorShape, anchorShape);
			lineResource.addLiteral(Gpml.hasAnchor, anchorResource);
		}
	}

	public static void addLabelTriples(Model model, Resource pwResource, Node labelNode, String wpId, String revId){
		String graphId = "";
		if (labelNode.getAttributes().getNamedItem("GraphId")!=null){
			graphId = labelNode.getAttributes().getNamedItem("GraphId").getTextContent().trim();
		}
		String textLabel = labelNode.getAttributes().getNamedItem("TextLabel").getTextContent().trim();
		Resource labelResource = model.createResource("http://rdf.wikipathways.org/Pathway/"+wpId+"_r"+revId+"/GpmlLabel/"+UUID.randomUUID());
		labelResource.addProperty(DCTerms.isPartOf, pwResource);
		labelResource.addLiteral(Gpml.graphid, graphId);
		labelResource.addLiteral(RDFS.label, textLabel);
		labelResource.addProperty(RDF.type, Gpml.Label);

		NodeList lineGraphics = ((Element) labelNode).getElementsByTagName("Graphics");
		int zOrder = 0;
		if (lineGraphics.item(0).getAttributes().getNamedItem("ZOrder") != null){
			zOrder = Integer.valueOf(lineGraphics.item(0).getAttributes().getNamedItem("ZOrder").getTextContent().trim());
			labelResource.addLiteral(Gpml.zorder, zOrder);
		}

		Float centerX = null;
		Float centerY = null;
		if (lineGraphics.item(0).getAttributes().getNamedItem("CenterX") != null){
			centerX = Float.valueOf(lineGraphics.item(0).getAttributes().getNamedItem("CenterX").getTextContent().trim());
		}
		if (lineGraphics.item(0).getAttributes().getNamedItem("CenterY")!= null){
			centerY = Float.valueOf(lineGraphics.item(0).getAttributes().getNamedItem("CenterY").getTextContent().trim());
		}
		String fillColor = "";
		if (lineGraphics.item(0).getAttributes().getNamedItem("FillColor")!= null){
			fillColor = lineGraphics.item(0).getAttributes().getNamedItem("FillColor").getTextContent().trim();
		}
		int fontSize =0;
		if (lineGraphics.item(0).getAttributes().getNamedItem("FontSize")!=null){
			fontSize = Integer.valueOf(lineGraphics.item(0).getAttributes().getNamedItem("FontSize").getTextContent().trim());
		}
		String fontWeight = "";
		if (lineGraphics.item(0).getAttributes().getNamedItem("FontWeight")!=null){
			fontWeight = lineGraphics.item(0).getAttributes().getNamedItem("FontWeight").getTextContent().trim();
		}
		Float height = Float.valueOf(lineGraphics.item(0).getAttributes().getNamedItem("Height").getTextContent().trim());
		Float width = Float.valueOf(lineGraphics.item(0).getAttributes().getNamedItem("Width").getTextContent().trim());
		String valign ="";
		if (lineGraphics.item(0).getAttributes().getNamedItem("Valign")!=null){
			valign = lineGraphics.item(0).getAttributes().getNamedItem("Valign").getTextContent().trim();
		}

		labelResource.addLiteral(Gpml.centerx, centerX);
		labelResource.addLiteral(Gpml.centery, centerY);
		labelResource.addLiteral(Gpml.fillcolor, fillColor);
		labelResource.addLiteral(Gpml.fontsize, fontSize);
		labelResource.addLiteral(Gpml.fontweight, fontWeight);
		labelResource.addLiteral(Gpml.height, height);
		labelResource.addLiteral(Gpml.width, width);
		labelResource.addLiteral(Gpml.valign, valign);

	}
	public static void addReferenceTriples(Model model, Resource pwResource, Node referenceNode, String wpId, String revision){
		String id = basicCalls.getStringNodeContent(referenceNode, "bp:ID").trim();
		String db = basicCalls.getStringNodeContent(referenceNode, "bp:DB").trim();
		String title = basicCalls.getStringNodeContent(referenceNode, "bp:TITLE");
		String source = basicCalls.getStringNodeContent(referenceNode, "bp:SOURCE");
		String year = basicCalls.getStringNodeContent(referenceNode, "bp:YEAR");
		ArrayList<String> authors = new ArrayList<String>();
		NodeList authorsNL = ((Element) referenceNode).getElementsByTagName("bp:AUTHORS");
		for (int i=0; i<authorsNL.getLength(); i++){
			authors.add(authorsNL.item(i).getTextContent().trim());
		}
		Resource referenceResource;
		if (db.equals("PubMed") && (id !="") && id.trim().matches("^[0-9]+$")){
			referenceResource = model.createResource("http://identifiers.org/pubmed/"+id);
			referenceResource.addProperty(FOAF.page, model.createResource("http://www.ncbi.nlm.nih.gov/pubmed/"+id)); 

		} else {
			referenceResource = model.createResource();
			referenceResource.addLiteral(DCTerms.identifier, id);
			referenceResource.addLiteral(DC.source, db);
			referenceResource.addLiteral(DCTerms.title, title);
			referenceResource.addLiteral(DCTerms.source, source);
			referenceResource.addLiteral(DCTerms.date, year);

		}
		referenceResource.addProperty(RDF.type, Wp.PublicationReference);
		referenceResource.addProperty(DCTerms.isPartOf, pwResource);

	}

	public static void addGroupTriples(Model model, Resource pwResource, Node groupNode, String wpId, String revId){
		String graphId = "";
		if (((Element) groupNode).getAttributes().getNamedItem("GraphId") != null){
			graphId = ((Element) groupNode).getAttributes().getNamedItem("GraphId").getTextContent().trim();
		}
		String groupId = ((Element) groupNode).getAttributes().getNamedItem("GroupId").getTextContent().trim();

		Resource groupResource = model.createResource("http://rdf.wikipathways.org/Pathway/"+wpId+"_r"+revId+"/group/"+graphId);
		groupResource.addProperty(RDF.type, Gpml.Group);
		groupResource.addLiteral(Gpml.graphid, graphId);
		groupResource.addProperty(DCTerms.isPartOf, pwResource);
		//TODO understand what groupRef means
	}

	public static void addCommentTriples(Model model, Resource pwResource, NodeList commentNodeList, String wpId, String revId){
		String wpCategory = "";
		String wpDescription = "";
		for ( int n=0; n<commentNodeList.getLength(); n++) {
			String commentType = basicCalls.getStringAttributeContent(commentNodeList.item(n), "Source");
			String commentContents = commentNodeList.item(n).getTextContent().trim(); 
			if (commentType.equals("WikiPathways-category")){
				if (commentContents.equals("Physiological Process")){
					pwResource.addProperty(Wp.category, Wp.PhysiologicalProcess);
					//System.out.println(commentType);
				}
				if (commentContents.equals("Metabolic Process")){
					pwResource.addProperty(Wp.category, Wp.MetabolicProcess);
					//System.out.println(commentType);
				}
				if (commentContents.equals("Cellular Process")){
					pwResource.addProperty(Wp.category, Wp.CellularProcess);
				}
				if (commentContents.equals("Molecular Function")){
					pwResource.addProperty(Wp.category, Wp.MolecularFunction);
				}
			}
			if (commentType.equals("WikiPathways-description")){
				pwResource.addLiteral(DCTerms.description, commentContents);		
			}
			if (commentType.equals("GenMAPP notes")){
				pwResource.addLiteral(DCTerms.description, commentContents);
				pwResource.addLiteral(Gpml.genmappNotes, commentContents);		
			}

		}
	}

	public static void addPathwayOntologyTriples(Model model, Resource pwResource, Node ontologyNode){
		String identifier = basicCalls.getStringNodeContent(ontologyNode, "bp:ID");
		pwResource.addProperty(Wp.pathwayOntology, model.createResource(constants.getOntologyURI(identifier).replace(":", "_").replace("http_", "http:"))); //TDOD discuss with Tina and Alex about what to do with this

	}

}
