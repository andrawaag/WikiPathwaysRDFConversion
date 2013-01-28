
public class constants {
      public static String localTempDir() {
    	  return "/tmp/";
      }
      
      public static String localCurrentGPMLCache() {
    	  return localTempDir()+"GPML/";
      }
      
      public static String localAllGPMLCacheDir() {
    	  return localTempDir()+"GPMLHistory/";
      }
      
      public static String getWikiPathwaysURL() {
    	  return "http://www.wikipathways.org/instance/";
      }
      
      public static String getDbPediaUrl() {
    	  return "http://dbpedia.org/page/";
      }
      
      public static String getEUtilsUrl(String database, String term) {
    	  return "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db="+database+"&term="+term;
      }
      

      
      //PathwayOntologies
      public static String getOntologyURI(String identifier){
    	  return "http://purl.obolibrary.org/obo/"+identifier;
      }
}
