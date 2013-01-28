import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.pathvisio.model.ConverterException;
import org.pathvisio.wikipathways.WikiPathwaysClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.apache.commons.io.FilenameUtils;


public class MergeGpmltoCache {

	/**
	 * @param args
	 * @throws ParserConfigurationException 
	 * @throws TransformerFactoryConfigurationError 
	 * @throws TransformerException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws XMLStreamException 
	 * @throws ServiceException 
	 * @throws ConverterException 
	 */
	public static void main(String[] args) throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException, SAXException, IOException, XMLStreamException, ServiceException, ConverterException {
// Based on: http://stackoverflow.com/questions/10759775/how-to-merge-1000-xml-files-into-one-in-java
		//for (int i = 1; i < 8 ; i++) {		
		Writer outputWriter = new FileWriter("/tmp/WpGPML.xml");
        XMLOutputFactory xmlOutFactory = XMLOutputFactory.newFactory();
        XMLEventWriter xmlEventWriter = xmlOutFactory.createXMLEventWriter(outputWriter);
        XMLEventFactory xmlEventFactory = XMLEventFactory.newFactory();

        xmlEventWriter.add(xmlEventFactory.createStartDocument("ISO-8859-1", "1.0"));
        xmlEventWriter.add(xmlEventFactory.createStartElement("", null, "PathwaySet"));
        xmlEventWriter.add(xmlEventFactory.createAttribute("creationData", basicCalls.now()));
        XMLInputFactory xmlInFactory = XMLInputFactory.newFactory();
        
        	
        File dir = new File("/tmp/"+args[0]);
        
        File[] rootFiles = dir.listFiles();
        //the section below is only in case of analysis sets
        for (File rootFile : rootFiles) {
        	String fileName = FilenameUtils.removeExtension(rootFile.getName());
        	System.out.println(fileName);
        	String[] identifiers = fileName.split("_");
        	System.out.println(fileName);
        	String wpIdentifier = identifiers[identifiers.length-2];
        	String wpRevision = identifiers[identifiers.length-1];
        	//Pattern pattern = Pattern.compile("_(WP[0-9]+)_([0-9]+).gpml");
        	//Matcher matcher = pattern.matcher(fileName);
        	//System.out.println(matcher.find());
        	//String wpIdentifier = matcher.group(1);
        	File tempFile = new File(constants.localAllGPMLCacheDir()+wpIdentifier+"_"+wpRevision+".gpml");
        	//System.out.println(matcher.group(1));
        	//String wpRevision = matcher.group(2);
        	//System.out.println(matcher.group(2));
        	if (!(tempFile.exists())){					
				System.out.println(tempFile.getName());
				Document currentGPML = basicCalls.openXmlFile(rootFile.getPath());
				basicCalls.saveDOMasXML(wpRelatedCalls.addWpProvenance(currentGPML, wpIdentifier, wpRevision), constants.localCurrentGPMLCache() + tempFile.getName());
			}
        }
        
        
        dir = new File("/tmp/GPML");
        rootFiles = dir.listFiles();
        for (File rootFile : rootFiles) {
        	System.out.println(rootFile);
            XMLEventReader xmlEventReader = xmlInFactory.createXMLEventReader(new StreamSource(rootFile));
            XMLEvent event = xmlEventReader.nextEvent();
            // Skip ahead in the input to the opening document element
            try {
            while (event.getEventType() != XMLEvent.START_ELEMENT) {
                event = xmlEventReader.nextEvent();
            } 


            do {
                xmlEventWriter.add(event);
                event = xmlEventReader.nextEvent();
            } while (event.getEventType() != XMLEvent.END_DOCUMENT);
            xmlEventReader.close();
            } catch (Exception e) {
            	System.out.println("Malformed gpml file");
            }
        }

        xmlEventWriter.add(xmlEventFactory.createEndElement("", null, "PathwaySet"));
        xmlEventWriter.add(xmlEventFactory.createEndDocument());

        xmlEventWriter.close();
        outputWriter.close();
		
	}
	
}
