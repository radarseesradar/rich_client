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
public class PropagatorSynchronizer extends PropagatorViaAllPaths 
{
	private String inverseAttributeName;
    private Class inverseElementType;
    private Class otherElementType;

	public PropagatorSynchronizer( Visitor _delegator, Object _attributeOwner, String _attributeName, Class _elementType )
    {
    	super(   _delegator,  _attributeOwner,  _attributeName,  _elementType );
    }

	public PropagatorSynchronizer( Visitor _delegator, Object _attributeOwner, String _attributeName, Class _elementType,
    String _inverseAttributeName, Class _inverseElementType )
    {
    	super(   _delegator,  _attributeOwner,  _attributeName,  _elementType );
        inverseAttributeName = _inverseAttributeName;
        inverseElementType = _inverseElementType;
    }

	protected Class getOtherElementType()
    {
    	if( otherElementType == null )
        	otherElementType = getDelegator().deriveOthersType( getElementType() );
    	return otherElementType;
    }

	protected String getInverseAttributeName()
    {
    	return inverseAttributeName;
    }

	protected Class getInverseElementType()
    {
    	return inverseElementType;
    }

	protected void selectOneToManyTransitions()
    {
		Collection thisDestinationCollection = getDelegator().getToManyDestination( getAttributeOwner(), getAttributeName() );

		// Select elements that haven't previously been transitioned.
		for( Iterator anIterator = thisDestinationCollection.iterator(); anIterator.hasNext(); )
		{
			selectOneToManyTransition( anIterator.next() );
		}
    }

	protected void selectOneToManyTransition( Object nextThis )
    {
		if( (nextThis!= null)
		&& ! ( getDelegator().wasPreviouslyTransitioned( new Transition( getAttributeOwner(), getAttributeName(), nextThis ) )
        || getDelegator().wasPreviouslyTransitioned( new Transition( nextThis, getInverseAttributeName(), getAttributeOwner() ) ) ) )
		{
			getNonTransitionedMembers().add( nextThis );
		}
    }

	protected void recordOneToManyTransitions()
    {
		// Record the transitions.
		for( Iterator anIterator = getNonTransitionedMembers().iterator(); anIterator.hasNext(); )
		{
			recordOneToManyTransition( anIterator.next() );
		}
    }

	protected void recordOneToManyTransition( Object nextThis )
    {
		getDelegator().addPreviouslyTransitioned( new Transition( getAttributeOwner(), getAttributeName(), nextThis ) );
		getDelegator().addPreviouslyTransitioned( new Transition( nextThis, getInverseAttributeName(), getAttributeOwner() ) );
    }

	protected void performOneToManyTransitions()
    {
		for( Iterator anIterator = getNonTransitionedMembers().iterator(); anIterator.hasNext(); )
		{
			performOneToManyTransition( anIterator.next() );
		}
	}

	protected void performCommonToManyTransition( Object nextThis )
    {
		Persistable nextOther = getDelegator().createOther( nextThis );
		((VisitorSynchronizer)getDelegator()).addToManyDestination( getDelegator().getOther(), getAttributeName(), nextOther,
           									 getOtherElementType() );
		((Persistable)nextThis).accept( ((VisitorSynchronizer)getDelegator()).propagationVisitor( (Persistable) nextOther ) );
    }

	protected void performOneToManyTransition( Object nextThis )
    {
    	performCommonToManyTransition( nextThis );
    }

	protected void performToManyTransition( Object nextThis )
    {
    	performCommonToManyTransition( nextThis );
    }

	protected void sortToManyTransitions()
    {
    	// Currently a noop at this level of the inheritance hierarchy
    }

	protected void selectToManyTransition( Object nextThis )
    {
		if( (nextThis!= null)
		&& ! getDelegator().wasPreviouslyTransitioned( new Transition( getAttributeOwner(), getAttributeName(), nextThis ) ) )
		{
			getNonTransitionedMembers().add( nextThis );
		}
    }

	protected Collection propagateOneToMany()
    {
    	selectOneToManyTransitions();

		if( getNonTransitionedMembers().isEmpty() )
			return getNonTransitionedMembers();

        recordOneToManyTransitions();

        performOneToManyTransitions();

		return getNonTransitionedMembers();
    }

}
