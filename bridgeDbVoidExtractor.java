import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.bridgedb.BridgeDb;
import org.bridgedb.DataSource;
import org.bridgedb.IDMapper;
import org.bridgedb.IDMapperException;
import org.bridgedb.IDMapperStack;
import org.bridgedb.bio.BioDataSource;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.DCTerms;
import com.hp.hpl.jena.vocabulary.RDF;


public class bridgeDbVoidExtractor {

	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws IDMapperException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, IDMapperException, FileNotFoundException {
		BioDataSource.init();
		Class.forName("org.bridgedb.rdb.IDMapperRdb");
		File dir = new File("/Users/andra/Downloads/bridge");
		File[] bridgeDbFiles = dir.listFiles();
		Model bridgeDbmodel = ModelFactory.createDefaultModel();
		Resource bridgeDb = bridgeDbmodel.createResource();
		bridgeDb.addLiteral(DCTerms.title, "BridgeDb example files");
		bridgeDb.addLiteral(DCTerms.description, "TODO: Extend bridge description Information about the BridgeDb layout: http://bridgedb.org/wiki/GeneDatabaseLayout");
		for (File bridgeDbFile : bridgeDbFiles) {
			Long lastModified = bridgeDbFile.lastModified(); 
			Date date = new Date(lastModified); 
			
			Resource mainResource = bridgeDbmodel.createResource("http://bridgedb.org/data/gene_database/"+bridgeDbFile.getName());
			bridgeDb.addProperty(FOAF.primaryTopic, mainResource);
			mainResource.addProperty(RDF.type, Void.Linkset);
			mainResource.addLiteral(DCTerms.title, "BridgeDb mappings in bridgeDb database :"+bridgeDbFile.getName());
			mainResource.addProperty(DCTerms.license, bridgeDbmodel.createResource("http://creativecommons.org/licenses/by-sa/3.0/"));
			mainResource.addProperty(Pav.derivedFrom, bridgeDbmodel.createResource("http://www.ensembl.org/info/docs/Doxygen/core-api/index.html")); //TODO adapt to cover metabolites as well
			mainResource.addLiteral(Pav.derivedDate, date);
			mainResource.addProperty(Pav.derivedBy, bridgeDbmodel.createResource("http://bridgedb.org/browser/trunk/dbbuilder/src/org/bridgedb/README"));
			mainResource.addProperty(DCTerms.subject, bridgeDbmodel.createResource("http://dbpedia.org/resource/Identifier"));
			
			System.out.println(bridgeDbFile.getAbsolutePath());
			System.out.println(bridgeDbFile.getName());
			IDMapper mapper = BridgeDb.connect("idmapper-pgdb:" + bridgeDbFile.getAbsolutePath());
			Set<DataSource> targetDataSourcesSet = mapper.getCapabilities().getSupportedTgtDataSources();
			Set<DataSource> sourceDataSourcesSet = mapper.getCapabilities().getSupportedSrcDataSources();
			Iterator<DataSource> itr = targetDataSourcesSet.iterator();
			while (itr.hasNext()){
				DataSource target = itr.next();
				if (target.getFullName() != null) {
					mainResource.addLiteral(Void.subset, "Ensembl_"+target.getFullName());	
				}
			}
			
			mapper.close();
		}
		basicCalls.saveRDF2File(bridgeDbmodel, "/tmp/bridgeDbVoid.ttl", "TURTLE");
	}

}
