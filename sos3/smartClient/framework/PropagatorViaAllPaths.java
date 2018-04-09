/* Generated by Together */

package smartClient.framework;

import java.util.*;
import model.*;

/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 */
public class PropagatorViaAllPaths extends Propagator 
{
	public PropagatorViaAllPaths( Visitor _delegator, Object _attributeOwner, String _attributeName, Class _elementType )
    {
    	super(   _delegator,  _attributeOwner,  _attributeName,  _elementType );
    }

	protected void selectToManyTransitions()
    {
		Collection thisDestinationCollection = getDelegator().getToManyDestination( getAttributeOwner(), getAttributeName() );

		// Select elements that haven't previously been transitioned.
		for( Iterator anIterator = thisDestinationCollection.iterator(); anIterator.hasNext(); )
		{
        	selectToManyTransition( anIterator.next() );
		}
    }

	protected void selectToManyTransition( Object nextThis )
    {
		if( (nextThis!= null)
		&& !((StubInterface)nextThis).isStub()
		&& ! getDelegator().wasPreviouslyTransitioned( new Transition( getAttributeOwner(), getAttributeName(), nextThis ) ) )
		{
			getNonTransitionedMembers().add( nextThis );
		}
    }

	protected void sortToManyTransitions()
    {
    	// Sort transitions
    	Collections.sort( (List) getNonTransitionedMembers() );
    }

	protected void recordToManyTransitions()
    {
		// Record the transitions.
		for( Iterator anIterator = getNonTransitionedMembers().iterator(); anIterator.hasNext(); )
		{
			recordToManyTransition( anIterator.next() );
		}

    }

	protected void recordToManyTransition( Object nextThis )
    {
		getDelegator().addPreviouslyTransitioned( new Transition( getAttributeOwner(), getAttributeName(), nextThis ) );
    }

	protected void performToManyTransitions()
    {
		for( Iterator anIterator = getNonTransitionedMembers().iterator(); anIterator.hasNext(); )
		{
			performToManyTransition( anIterator.next() );
		}
    }

	protected void performToManyTransition( Object nextObj )
    {
		if( nextObj != null )
		{
			((Persistable)nextObj).accept( getDelegator().propagationVisitor() );
		}
    }

	protected Collection propagateToMany()
    {
    	selectToManyTransitions();

		if( getNonTransitionedMembers().isEmpty() )
			return getNonTransitionedMembers();

        sortToManyTransitions();

        recordToManyTransitions();

        performToManyTransitions();

		return getNonTransitionedMembers();
    }



}
