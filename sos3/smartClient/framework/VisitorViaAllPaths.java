package smartClient.framework;


import java.util.*;
import java.io.*;
import model.*;

/**
 * @author Steve McDaniel 
 * <br>Copyright (c) Steve McDaniel
 */
public class VisitorViaAllPaths extends Visitor 
{
  /**
   * @associates Transition 
   */
  private Set previouslyTransitioned;

  public VisitorViaAllPaths()
  {
    previouslyTransitioned = new HashSet();
  }

  protected Set getPreviouslyTransitioned( )
  {
    return previouslyTransitioned;
  }

  protected void displayPreviousTransitions()
  {
    System.out.println( "Previously transitioned set is: ");
    for( Iterator anIterator = previouslyTransitioned.iterator(); anIterator.hasNext(); )
    {
      System.out.println( "     " + anIterator.next() );
    }
  }

  public boolean wasPreviouslyTransitioned( Transition aTransition )
  {
    return previouslyTransitioned.contains( aTransition ) || isClamped( aTransition );
  }

  public void addPreviouslyTransitioned( Transition aTransition )
  {
    previouslyTransitioned.add( aTransition );
  }

  public Persistable propagateToOne( Object attributeOwner, String attributeName, Class attributeType )
  {
      // get toOne destination and create its transient companion
      Persistable thisDestination;
      if( attributeName == null  )
      {
        return (Persistable) attributeOwner;
      }

      thisDestination = (Persistable) getToOneDestination( attributeOwner, attributeName );


      if( thisDestination == null  )
        return null;
      if( wasPreviouslyTransitioned( new Transition( attributeOwner, attributeName, thisDestination ) ) )
        return null;
      addPreviouslyTransitioned( new Transition( attributeOwner, attributeName, thisDestination ) );

      // Return the toOne.
      return (Persistable) thisDestination.accept( propagationVisitor() );
  }

  public Collection propagateManyToMany( Object attributeOwner, String attributeName, Class elementType,
  String inverseAttributeName, Class inverseType )
  {
    return propagateToMany( attributeOwner, attributeName, elementType );
  }

  public Collection propagateOneToMany( Object attributeOwner, String attributeName, Class elementType,
  String inverseAttributeName, Class inverseType )
  {
    return propagateToMany( attributeOwner, attributeName, elementType );
  }

  public Collection propagateToMany( Object attributeOwner, String attributeName, Class elementType )
  {
  	PropagatorViaAllPaths aPropagator = new PropagatorViaAllPaths( this, attributeOwner, attributeName, elementType );
  	return aPropagator.propagateToMany();
  }

  public Persistable propagateManyToOne( Object attributeOwner, String attributeName, Class attributeType,
  String inverseAttributeName, Class inverseType )
  {
    return propagateToOne( attributeOwner,  attributeName,  attributeType );
  }

  public Persistable propagateOneToOne( Object attributeOwner, String attributeName, Class attributeType,
  String inverseAttributeName, Class inverseType )
  {
    return propagateToOne( attributeOwner,  attributeName,  attributeType );
  }

  protected void processPrimitive( Object attributeOwner, String attributeName, Class attributeType )
  {
    Object thisPrimitive;
    thisPrimitive = getToOnePrimitive( attributeOwner, attributeName );
  }
}