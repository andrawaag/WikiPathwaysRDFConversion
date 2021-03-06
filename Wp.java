/* CVS $Id: $ */
 
import com.hp.hpl.jena.rdf.model.*;
 
/**
 * Vocabulary definitions from http://vocabularies.wikipathways.org/wp.rdf 
 * @author Auto-generated by schemagen on 29 Jan 2013 14:26 
 */
public class Wp {
    /** <p>The RDF model that holds the vocabulary terms</p> */
    private static Model m_model = ModelFactory.createDefaultModel();
    
    /** <p>The namespace of the vocabulary as a string</p> */
    public static final String NS = "http://vocabularies.wikipathways.org/wp#";
    
    /** <p>The namespace of the vocabulary as a string</p>
     *  @see #NS */
    public static String getURI() {return NS;}
    
    /** <p>The namespace of the vocabulary as a resource</p> */
    public static final Resource NAMESPACE = m_model.createResource( NS );
    
    public static final Property bdbChemspider = m_model.createProperty( "http://vocabularies.wikipathways.org/wp#bdbChemspider" );
    
    public static final Property bdbEnsembl = m_model.createProperty( "http://vocabularies.wikipathways.org/wp#bdbEnsembl" );
    
    public static final Property bdbEntrezGene = m_model.createProperty( "http://vocabularies.wikipathways.org/wp#bdbEntrezGene" );
    
    public static final Property bdbHmdb = m_model.createProperty( "http://vocabularies.wikipathways.org/wp#bdbHmdb" );
    
    public static final Property bdbUniprot = m_model.createProperty( "http://vocabularies.wikipathways.org/wp#bdbUniprot" );
    
    /** <p>Indicates the pathway category to which a selected pathway adheres</p> */
    public static final Property category = m_model.createProperty( "http://vocabularies.wikipathways.org/wp#category" );
    
    public static final Property email = m_model.createProperty( "http://vocabularies.wikipathways.org/wp#email" );
    
    /** <p>DC.identifier</p> */
    public static final Property hasIdentifier = m_model.createProperty( "http://vocabularies.wikipathways.org/wp#hasIdentifier" );
    
    public static final Property hasParticipant = m_model.createProperty( "http://vocabularies.wikipathways.org/wp#hasParticipant" );
    
    public static final Property hasPathway = m_model.createProperty( "http://vocabularies.wikipathways.org/wp#hasPathway" );
    
    public static final Property interactionTarget = m_model.createProperty( "http://vocabularies.wikipathways.org/wp#interactionTarget" );
    
    public static final Property organism = m_model.createProperty( "http://vocabularies.wikipathways.org/wp#organism" );
    
    public static final Property pathwayOntology = m_model.createProperty( "http://vocabularies.wikipathways.org/wp#pathwayOntology" );
    
    /** <p>Links to FOAF</p> */
    public static final Property realName = m_model.createProperty( "http://vocabularies.wikipathways.org/wp#realName" );
    
    /** <p>DCidentifier</p> */
    public static final Property revision = m_model.createProperty( "http://vocabularies.wikipathways.org/wp#revision" );
    
    public static final Property unifiedIdentifier = m_model.createProperty( "http://vocabularies.wikipathways.org/wp#unifiedIdentifier" );
    
    /** <p>Links to FOAF, Sciencepeople</p> */
    public static final Property username = m_model.createProperty( "http://vocabularies.wikipathways.org/wp#username" );
    
    /** <p>Definition: An author is creator, editor, or curator of a pathway. The author 
     *  is known by a unique WikiPathways username.</p>
     */
    public static final Resource Author = m_model.createResource( "http://vocabularies.wikipathways.org/wp#Author" );
    
    /** <p>&lt;br&gt;&lt;b&gt;Description:&lt;/b&gt; A binding interaction between two 
     *  physical entities resulting in the formation of an explicit complex that is 
     *  reversible without an external factor (e.g. protein complexes). Binding subclass 
     *  of Mimbinding &lt;br&gt; &lt;b&gt;Properties:&lt;/b&gt; &lt;br&gt; - hasParticipant&lt;br&gt; 
     *  &lt;b&gt;GPML:&lt;/b&gt; Line with both Points having a "mim-binding" arrowhead.</p>
     */
    public static final Resource Binding = m_model.createResource( "http://vocabularies.wikipathways.org/wp#Binding" );
    
    /** <p>Entity in BioPAX</p> */
    public static final Resource BiologicalEntity = m_model.createResource( "http://vocabularies.wikipathways.org/wp#BiologicalEntity" );
    
    /** <p>&lt;br /&gt; &lt;b&gt;Description&lt;/b&gt;:A connection point for two interaction 
     *  lines, stylized to indicate left directionality or flow, but not biologically 
     *  meaningful.&lt;br /&gt; &lt;b&gt;Properties&lt;/b&gt;:&lt;br /&gt; - participant 
     *  &lt;br /&gt; &lt;b&gt;GPML&lt;/b&gt;: A line with a point with branching glyph 
     *  terminating on an anchor; the other point can have another arrowhead type 
     *  and terminate on a datanode or group.</p>
     */
    public static final Resource BranchingLeft = m_model.createResource( "http://vocabularies.wikipathways.org/wp#BranchingLeft" );
    
    /** <p>&lt;br /&gt; &lt;b&gt;Description&lt;/b&gt;:A connection point for two interaction 
     *  lines, stylized to indicate right directionality or flow, but not biologically 
     *  meaningful.&lt;br /&gt; &lt;b&gt;Properties&lt;/b&gt;:&lt;br /&gt; - participant 
     *  &lt;br /&gt; &lt;b&gt;GPML&lt;/b&gt;: A line with a point with branching glyph 
     *  terminating on an anchor; the other point can have another arrowhead type 
     *  and terminate on a datanode or group.</p>
     */
    public static final Resource BranchingRight = m_model.createResource( "http://vocabularies.wikipathways.org/wp#BranchingRight" );
    
    /** <p>&lt;br /&gt; &lt;b&gt;Description&lt;/b&gt;: An interaction where the controller 
     *  entity increases the rate of the controlled reaction.&lt;br /&gt; &lt;b&gt;Properties&lt;/b&gt;:&lt;br 
     *  /&gt; - participant&lt;br /&gt; - controller&lt;br /&gt; - controlled&lt;br 
     *  /&gt; &lt;b&gt;GPML&lt;/b&gt;: A line with one point with arrowhead terminating 
     *  on an anchor; the other point does not have an arrowhead and terminates on 
     *  a datanode or group.</p>
     */
    public static final Resource Catalysis = m_model.createResource( "http://vocabularies.wikipathways.org/wp#Catalysis" );
    
    /** <p>Definition: A Cell is a shape in a pathway that represents the basic structural 
     *  and functional unit of the organism on which this pathways is topical.</p>
     */
    public static final Resource Cell = m_model.createResource( "http://vocabularies.wikipathways.org/wp#Cell" );
    
    public static final Resource CellularProcess = m_model.createResource( "http://vocabularies.wikipathways.org/wp#CellularProcess" );
    
    /** <p>&lt;br /&gt;&lt;b&gt;Description: &lt;/b&gt;The scission of a covalent bond 
     *  or the separation of connections between entity features. &lt;br /&gt; &lt;b&gt;Properties:&lt;/b&gt; 
     *  &lt;br /&gt; - participant - one or more DataNodes&lt;br /&gt; - source - 
     *  one or more DataNodes&lt;br /&gt; - target - one or more DataNodes&lt;br /&gt; 
     *  &lt;b&gt;GPML:&lt;/b&gt; Line with one Point having a "cleavage" arrowhead 
     *  on a datanode; the other Point has no arrowhead and is also on a datanode.</p>
     */
    public static final Resource Cleavage = m_model.createResource( "http://vocabularies.wikipathways.org/wp#Cleavage" );
    
    /** <p>no corresponding elements in BioPAX and GPML</p> */
    public static final Resource Compartment = m_model.createResource( "http://vocabularies.wikipathways.org/wp#Compartment" );
    
    public static final Resource Complex = m_model.createResource( "http://vocabularies.wikipathways.org/wp#Complex" );
    
    public static final Resource ControlledVocabulary = m_model.createResource( "http://vocabularies.wikipathways.org/wp#ControlledVocabulary" );
    
    /** <p>&lt;br&gt;&lt;b&gt;Description: &lt;/b&gt;An interaction in which one entity 
     *  is transformed into one or more other entities (e.g. the conversion of ATP 
     *  to cyclical-AMP). &lt;br&gt; &lt;b&gt;Properties:&lt;/b&gt; &lt;br&gt; - participant 
     *  - one or more DataNodes&lt;br&gt; - source - one or more DataNodes&lt;br&gt; 
     *  - target - one or more DataNodes&lt;br&gt; &lt;b&gt;GPML:&lt;/b&gt; Line with 
     *  one Point having a "conversion" arrowhead (if both Points have a conversion 
     *  arrowhead then we are actually talking about two interactions and they should 
     *  be split up in the pathway).</p>
     */
    public static final Resource Conversion = m_model.createResource( "http://vocabularies.wikipathways.org/wp#Conversion" );
    
    /** <p>&lt;br /&gt;&lt;b&gt;Description: &lt;/b&gt;An asymmetric covalent non-reversible 
     *  binding reaction (e.g. phosphorylation and acetylation) where one of the substrates 
     *  must be a modifier physical entity. &lt;br /&gt; &lt;b&gt;Properties:&lt;/b&gt; 
     *  &lt;br /&gt; - participant - one or more DataNodes&lt;br /&gt; - source - 
     *  one or more DataNodes&lt;br /&gt; - target - one or more DataNodes&lt;br /&gt; 
     *  &lt;b&gt;GPML:&lt;/b&gt; Line with a Point having a "mim-covalent-bond" arrowhead 
     *  on a datanode; and the Point with no arrowhead on a datanode.</p>
     */
    public static final Resource CovalentBond = m_model.createResource( "http://vocabularies.wikipathways.org/wp#CovalentBond" );
    
    public static final Resource CurationTag = m_model.createResource( "http://vocabularies.wikipathways.org/wp#CurationTag" );
    
    /** <p>Definition: A datanode is used to denote a biological entity that participates 
     *  in a pathway. In WikiPathways the following DataNode types can be present 
     *  in a pathway: GeneProduct, Metabolite, Protein, Complex, Rna, Pathway and 
     *  Unknown.</p>
     */
    public static final Resource DataNode = m_model.createResource( "http://vocabularies.wikipathways.org/wp#DataNode" );
    
    /** <p>UnificationXref in BioPAX</p> */
    public static final Resource DatasourceReference = m_model.createResource( "http://vocabularies.wikipathways.org/wp#DatasourceReference" );
    
    /** <p>&lt;br&gt;&lt;b&gt;A BasicInteraction is a generic biological interaction 
     *  between two or more pathway elements that is not identified with a MIM type.&lt;/b&gt;&lt;br&gt; 
     *  * GPML: Line (ArrowHead = none, "Line", "Arrow" or "TBar")&lt;br&gt; * BioPAX: 
     *  Interaction</p>
     */
    public static final Resource DirectedInteraction = m_model.createResource( "http://vocabularies.wikipathways.org/wp#DirectedInteraction" );
    
    /** <p>Shape in GPML, no corresponding element in BioPAX</p> */
    public static final Resource EndoplasmicReticulum = m_model.createResource( "http://vocabularies.wikipathways.org/wp#EndoplasmicReticulum" );
    
    public static final Resource Entity = m_model.createResource( "http://vocabularies.wikipathways.org/wp#Entity" );
    
    /** <p>no corresponding elements in BioPAX and GPML (might be nice to add functionality 
     *  to define extracellular space in a pathway)</p>
     */
    public static final Resource Extracellular = m_model.createResource( "http://vocabularies.wikipathways.org/wp#Extracellular" );
    
    public static final Resource Gap = m_model.createResource( "http://vocabularies.wikipathways.org/wp#Gap" );
    
    /** <p>DNA+Gene+RNA+Protein in BioPAX</p> */
    public static final Resource GeneProduct = m_model.createResource( "http://vocabularies.wikipathways.org/wp#GeneProduct" );
    
    /** <p>Shape in GPML, no corresponding element in BioPAX</p> */
    public static final Resource GolgiApparatus = m_model.createResource( "http://vocabularies.wikipathways.org/wp#GolgiApparatus" );
    
    public static final Resource Group = m_model.createResource( "http://vocabularies.wikipathways.org/wp#Group" );
    
    /** <p>&lt;br&gt;&lt;b&gt;Description: &lt;/b&gt; An interaction where a controller 
     *  entity causes a decrease in the velocity or extent of controlled reaction 
     *  or contingency; the probability of the controlled interaction occurring remains 
     *  greater than 0. &lt;br&gt; &lt;b&gt;Properties:&lt;/b&gt;&lt;br&gt; - participant 
     *  (DataNodes or Interactions)&lt;br&gt; - controller (DataNodes)&lt;br&gt; - 
     *  controlled (DataNodes or Interactions)&lt;br&gt; &lt;b&gt;GPML: &lt;/b&gt; 
     *  Line with one Point having an arrowhead "conversion". The Point without the 
     *  arrowhead cannot be an interaction.</p>
     */
    public static final Resource Inhibition = m_model.createResource( "http://vocabularies.wikipathways.org/wp#Inhibition" );
    
    /** <p>&lt;br&gt;&lt;b&gt;An interaction is a generic biological interaction between 
     *  two or more pathway elements.&lt;/b&gt;&lt;br&gt; * GPML: Line (ArrowHead 
     *  = none or all others)&lt;br&gt; * BioPAX: Interaction</p>
     */
    public static final Resource Interaction = m_model.createResource( "http://vocabularies.wikipathways.org/wp#Interaction" );
    
    /** <p>no corresponding element in BioPAX GPML:Label</p> */
    public static final Resource Label = m_model.createResource( "http://vocabularies.wikipathways.org/wp#Label" );
    
    public static final Resource MetabolicProcess = m_model.createResource( "http://vocabularies.wikipathways.org/wp#MetabolicProcess" );
    
    /** <p>SmallMolecule in BioPAX</p> */
    public static final Resource Metabolite = m_model.createResource( "http://vocabularies.wikipathways.org/wp#Metabolite" );
    
    /** <p>Shape in GPML, no element in BioPAX</p> */
    public static final Resource Mitochondria = m_model.createResource( "http://vocabularies.wikipathways.org/wp#Mitochondria" );
    
    /** <p>&lt;br&gt; &lt;b&gt;Description: &lt;/b&gt;An asymmetric covalent non-reversible 
     *  binding reaction (e.g. phosphorylation and acetylation) where one of the substrates 
     *  must be a modifier physical entity. &lt;br&gt; &lt;b&gt;Properties:&lt;/b&gt;&lt;br&gt; 
     *  - participant (one or more DataNodes)&lt;br&gt; - source (one or more DataNodes)&lt;br&gt; 
     *  - target (one or more DataNodes)&lt;br&gt; &lt;b&gt;GPML: &lt;/b&gt;A Line 
     *  with one Point having an arrowhead "mim-modification".</p>
     */
    public static final Resource Modification = m_model.createResource( "http://vocabularies.wikipathways.org/wp#Modification" );
    
    public static final Resource MolecularFunction = m_model.createResource( "http://vocabularies.wikipathways.org/wp#MolecularFunction" );
    
    /** <p>&lt;br&gt;&lt;b&gt;Description: &lt;/b&gt;An interaction where the controller 
     *  entity is necessary for the controlled interaction to proceed. &lt;br&gt; 
     *  &lt;b&gt;Properties: &lt;/b&gt;&lt;br&gt; - participant (DataNodes or Interactions)&lt;br&gt; 
     *  - controller (DataNodes)&lt;br&gt; - controlled (DataNodes or Interactions)&lt;br&gt; 
     *  &lt;b&gt;GPML: &lt;/b&gt;Line with one Point having an arrowhead "mim-necessary-stimulation". 
     *  The Point without the arrowhead cannot be an interaction.</p>
     */
    public static final Resource NecessaryStimulation = m_model.createResource( "http://vocabularies.wikipathways.org/wp#NecessaryStimulation" );
    
    /** <p>Shape in GPML, no corresponding element in BioPAX</p> */
    public static final Resource Nucleus = m_model.createResource( "http://vocabularies.wikipathways.org/wp#Nucleus" );
    
    /** <p>Shape in GPML, no corresponding element in BioPAx</p> */
    public static final Resource Organelle = m_model.createResource( "http://vocabularies.wikipathways.org/wp#Organelle" );
    
    /** <p>Pathway in BioPAX</p> */
    public static final Resource Pathway = m_model.createResource( "http://vocabularies.wikipathways.org/wp#Pathway" );
    
    /** <p>no corresponding element in BioPAX</p> */
    public static final Resource PathwayAnnotation = m_model.createResource( "http://vocabularies.wikipathways.org/wp#PathwayAnnotation" );
    
    public static final Resource PathwayCategory = m_model.createResource( "http://vocabularies.wikipathways.org/wp#PathwayCategory" );
    
    public static final Resource PathwayVersion = m_model.createResource( "http://vocabularies.wikipathways.org/wp#PathwayVersion" );
    
    public static final Resource Person = m_model.createResource( "http://vocabularies.wikipathways.org/wp#Person" );
    
    public static final Resource PhysiologicalProcess = m_model.createResource( "http://vocabularies.wikipathways.org/wp#PhysiologicalProcess" );
    
    /** <p>Protein in BioPAX</p> */
    public static final Resource Protein = m_model.createResource( "http://vocabularies.wikipathways.org/wp#Protein" );
    
    /** <p>PublicationXref in Biopax</p> */
    public static final Resource PublicationReference = m_model.createResource( "http://vocabularies.wikipathways.org/wp#PublicationReference" );
    
    /** <p>RNA in Biopax, DataNode in GPML</p> */
    public static final Resource RNA = m_model.createResource( "http://vocabularies.wikipathways.org/wp#RNA" );
    
    /** <p>Xref in BioPAX</p> */
    public static final Resource Reference = m_model.createResource( "http://vocabularies.wikipathways.org/wp#Reference" );
    
    /** <p>Shape in GPML, no corresponding element in BioPAX</p> */
    public static final Resource SarcoplasmicReticulum = m_model.createResource( "http://vocabularies.wikipathways.org/wp#SarcoplasmicReticulum" );
    
    /** <p>&lt;br&gt;&lt;b&gt;Description: &lt;/b&gt;Enhancement of the velocity or extent 
     *  of a reaction or contingency by the controller entity. &lt;br&gt; &lt;b&gt;Properties: 
     *  &lt;/b&gt;&lt;br&gt; - participant (DataNodes or Interactions)&lt;br&gt; - 
     *  controller (DataNodes)&lt;br&gt; - controlled (DataNodes or Interactions)&lt;br&gt; 
     *  &lt;b&gt;GPML: &lt;/b&gt;Line with one Point having an arrowhead "mim-stimulation". 
     *  The Point without the arrowhead cannot be an interaction.</p>
     */
    public static final Resource Stimulation = m_model.createResource( "http://vocabularies.wikipathways.org/wp#Stimulation" );
    
    /** <p>&lt;br&gt; &lt;b&gt;Description: &lt;/b&gt;A type of production without loss 
     *  that involves a template (e.g. the polymerization of a nucleic acid macromolecule 
     *  from a nucleic acid macromolecule template). &lt;br&gt; &lt;b&gt;Properties:&lt;/b&gt;&lt;br&gt; 
     *  - particpant (two or more DataNodes)&lt;br&gt; - template (one or more DataNodes) 
     *  &lt;br&gt; - product (one or more DataNodes)&lt;br&gt; &lt;b&gt;GPML: &lt;/b&gt;Line 
     *  with one Point having an arrowhead "mim-transcription-translation". Cannot 
     *  be reversible!</p>
     */
    public static final Resource TranscriptionTranslation = m_model.createResource( "http://vocabularies.wikipathways.org/wp#TranscriptionTranslation" );
    
    public static final Resource Unknown = m_model.createResource( "http://vocabularies.wikipathways.org/wp#Unknown" );
    
    public static final Resource UtilityClass = m_model.createResource( "http://vocabularies.wikipathways.org/wp#UtilityClass" );
    
    /** <p>Shape in GPML, no corresponding element in BioPAX</p> */
    public static final Resource Vesicle = m_model.createResource( "http://vocabularies.wikipathways.org/wp#Vesicle" );
    
    public static final Resource University = m_model.createResource( "http://vocabularies.wikipathways.org/wp#Maastricht%20University" );
    
    public static final Resource andra = m_model.createResource( "http://vocabularies.wikipathways.org/wp#andra" );
    
}
