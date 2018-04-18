package cz.vutbr.fit.xtutko00.model.rdf;

import com.github.radkovo.rdf4j.builder.EntityFactory;
import cz.vutbr.fit.xtutko00.model.property.RdfPropertyEntity;
import cz.vutbr.fit.xtutko00.model.rdf.vocabulary.TA;
import cz.vutbr.fit.xtutko00.utils.IdMaker;
import io.hgraphdb.HBaseGraph;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;

import java.util.HashSet;
import java.util.Set;

/**
 * An entry in the timeline.
 * <p>
 * IRI: {@code <http://nesfit.github.io/ontology/ta.owl#Entry>}
 */
public class Entry extends RdfPropertyEntity
{
	public static final IRI CLASS_IRI = vf.createIRI("http://nesfit.github.io/ontology/ta.owl#Entry");

	/**
	 * IRI: {@code <http://nesfit.github.io/ontology/ta.owl#sourceTimeline>}
	 */
	private Timeline sourceTimeline;

	/**
	 * IRI: {@code <http://nesfit.github.io/ontology/ta.owl#contains>}
	 */
	private Set<Content> contains;

	/**
	 * An identifier of the entry in the source timeline (e.g. Twitter id).
	 * <p>
	 * IRI: {@code <http://nesfit.github.io/ontology/ta.owl#sourceId>}
	 */
	private String sourceId;

	/**
	 * Entry creation timestamp.
	 * <p>
	 * IRI: {@code <http://nesfit.github.io/ontology/ta.owl#timestamp>}
	 */
	private java.util.Date timestamp;


	public Entry(IRI iri) {
		super(iri);
		contains = new HashSet<Content>();
	}

	@Override
	public IRI getClassIRI() {
		return Entry.CLASS_IRI;
	}

	public Timeline getSourceTimeline() {
		return sourceTimeline;
	}

	public void setSourceTimeline(Timeline sourceTimeline) {
		this.sourceTimeline = sourceTimeline;
	}

	public Set<Content> getContains() {
		return contains;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public java.util.Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(java.util.Date timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public void addToModel(Model model) {
		super.addToModel(model);
		addObject(model, TA.sourceTimeline, sourceTimeline);
		addCollectionWithData(model, TA.contains, contains);
		addValue(model, TA.sourceId, sourceId);
		addValue(model, TA.timestamp, timestamp);
	}

	@Override
	public void loadFromModel(Model model, EntityFactory efactory) {
		super.loadFromModel(model, efactory);
		if (!(efactory instanceof TAFactory))
			throw new IllegalArgumentException("factory must be instance of TAFactory");
		final TAFactory factory = (TAFactory) efactory;

		final Model m = model.filter(getIRI(), null, null);
		//load object sourceTimeline
		final Set<IRI> sourceTimelineIRIs = getObjectIRIs(m, TA.sourceTimeline);
		if (!sourceTimelineIRIs.isEmpty()) {
			final IRI iri = sourceTimelineIRIs.iterator().next();
			sourceTimeline = factory.createTimeline(iri);
			sourceTimeline.loadFromModel(m, factory);
		} else {
			sourceTimeline = null;
		}
		//load collection contains
		final Set<IRI> containsIRIs = getObjectIRIs(m, TA.contains);
		contains = new HashSet<>();
		for (IRI iri : containsIRIs) {
			Content item = factory.createContent(iri);
			item.loadFromModel(m, factory);
			contains.add(item);
		}
		sourceId = loadStringValue(m, TA.sourceId);
		timestamp = loadDateValue(m, TA.timestamp);
	}

	@Override
	public Vertex addToGraph(HBaseGraph graph, IdMaker idMaker) {
		Vertex vertex = graph.addVertex(T.id, idMaker.getId(), T.label, buildLabel(getClassIRI()));
		addProperty(vertex, "label", getLabel());
		addProperty(vertex, "sourceId", getSourceId());
		addProperty(vertex, "timestamp", getTimestamp());

		getContains().forEach(content -> {
			Vertex entryVertex = content.addToGraph(graph, idMaker);
			vertex.addEdge("has", entryVertex, T.id, idMaker.getId());
		});

		return vertex;
	}
}
