/**
 * FBEntityFactory.java
 *
 * Created on 7. 3. 2018, 13:13:03 by burgetr
 */
package cz.vutbr.fit.xtutko00.source.facebook.model;

import cz.vutbr.fit.xtutko00.model.core.ResourceFactory;
import cz.vutbr.fit.xtutko00.model.rdf.*;
import org.eclipse.rdf4j.model.IRI;

/**
 * Reused from <a href="https://github.com/nesfit/timeline-analyzer">timeline-analyzer</a>
 *
 * @author burgetr
 */
public class FBEntityFactory implements TAFactory
{
    private static final FBEntityFactory instance = new FBEntityFactory();
    
    public static FBEntityFactory getInstance()
    {
        return instance;
    }
    
    protected FBEntityFactory()
    {
    }

    @Override
    public Content createContent(IRI iri)
    {
        return null; //content is never created, use sublcasses
    }

    @Override
    public TextContent createTextContent(IRI iri)
    {
        return new FBTextContent(iri);
    }

    public TextContent createTextContent(String postId)
    {
        return createTextContent(ResourceFactory.createResourceIRI("fb", "entryText", postId));
    }
    
    @Override
    public Image createImage(IRI iri)
    {
        return new FBImage(iri);
    }
    
    public Image createImage(String imageId)
    {
        return createImage(ResourceFactory.createResourceIRI("fb", "image", imageId));
    }

    @Override
    public Entry createEntry(IRI iri)
    {
        return new FBEntry(iri);
    }

    public Entry createEntry(String postId)
    {
        return createEntry(ResourceFactory.createResourceIRI("fb", "entry", postId));
    }
    
    @Override
    public Timeline createTimeline(IRI iri)
    {
        return new FBTimeline(iri);
    }
    
    public Timeline createTimeline(String profileId)
    {
        return createTimeline(ResourceFactory.createResourceIRI("fb", "timeline", profileId));
    }

    @Override
    public GeoContent createGeoContent(IRI iri)
    {
        return new FBGeoContent(iri);
    }

    public GeoContent createGeoContent(String postId)
    {
        return new FBGeoContent(ResourceFactory.createResourceIRI("fb", "geo", postId));
    }

    @Override
    public URLContent createURLContent(IRI iri)
    {
        return new FBURLContent(iri);
    }

    public URLContent createURLContent(String postId, int index)
    {
        return new FBURLContent(ResourceFactory.createResourceIRI("fb", "url", postId + "-" + index));
    }

}
