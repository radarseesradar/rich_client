package model;

import java.util.*;
import java.lang.reflect.*;
import java.io.*;
import model.Visitor;
import smartClient.framework.Persistable;

/**
 * @author Steve McDaniel
 * <br>Copyright (c) Steve McDaniel
 */
public class PostVisitHelper
{
  private Visitor visitor;

  PostVisitHelper( Visitor _visitor )
  {
    setVisitor( _visitor );
  }

  private void setVisitor( Visitor _visitor )
  {
    visitor = _visitor;
  }

  private Visitor getVisitor( )
  {
    return visitor;
  }

  private void processPrimitive( Object attributeOwner, String attributeName, Class attributeType )
  {
    getVisitor().processPrimitive( attributeOwner, attributeName, attributeType );
  }

  private void processUpdateCounter( Object attributeOwner )
  {
    getVisitor().processUpdateCounter( attributeOwner );
  }

  private Persistable propagateToOne( Object attributeOwner, String attributeName, Class attributeType )
  {
    return getVisitor().propagateToOne( attributeOwner, attributeName, attributeType );
  }

  private Persistable propagateManyToOne( Object attributeOwner, String attributeName, Class attributeType, String inverseAttribute, Class inverseAttributeType )
  {
      return getVisitor().propagateManyToOne( attributeOwner, attributeName, attributeType, inverseAttribute, inverseAttributeType );
  }

  private Persistable propagateOneToOne( Object attributeOwner, String attributeName, Class attributeType, String inverseAttribute, Class inverseAttributeType )
  {
      return getVisitor().propagateOneToOne( attributeOwner, attributeName, attributeType, inverseAttribute, inverseAttributeType );
  }

  private Collection propagateToMany( Object attributeOwner, String attributeName, Class attributeType )
  {
    return getVisitor().propagateToMany( attributeOwner, attributeName, attributeType );
  }

  private Collection propagateOneToMany( Object attributeOwner, String attributeName, Class attributeType, String inverseAttribute, Class inverseType )
  {
    return  getVisitor().propagateOneToMany(  attributeOwner,  attributeName,  attributeType,  inverseAttribute,  inverseType );
  }

  private Collection propagateManyToMany( Object attributeOwner, String attributeName, Class attributeType, String inverseAttribute, Class inverseType )
  {
    return  getVisitor().propagateManyToMany(  attributeOwner,  attributeName,  attributeType,  inverseAttribute,  inverseType );
  }

  void postVisit( PersistenceAdapter aPersistableObject )
  {
    processUpdateCounter( aPersistableObject );
  }

  void postVisit( model.test.client.Female aPersistableObject )
  {
    propagateOneToOne( aPersistableObject, "husband", model.test.client.Male.class, "wife", model.test.client.Female.class );
    propagateManyToMany( aPersistableObject, "brothers", model.test.client.Male.class, "sisters", model.test.client.Female.class );
  }

  void postVisit( model.test.client.Male aPersistableObject )
  {
    propagateOneToOne( aPersistableObject, "wife", model.test.client.Female.class, "husband", model.test.client.Male.class );
    propagateManyToMany( aPersistableObject, "sisters", model.test.client.Female.class, "brothers", model.test.client.Male.class );
  }

  void postVisit( model.test.client.Component aPersistableObject )
  {
    processPrimitive( aPersistableObject, "name", String.class );
    processPrimitive( aPersistableObject, "alias", String.class );
    propagateToMany( aPersistableObject, "playmates", model.test.client.Component.class );
    propagateToOne( aPersistableObject, "bestFriend", model.test.client.Component.class );
    propagateManyToOne( aPersistableObject, "parent", model.test.client.Composite.class, "children", model.test.client.Component.class );
  }

  void postVisit( model.test.client.Composite aPersistableObject )
  {
    propagateOneToMany( aPersistableObject, "children", model.test.client.Component.class, "parent", model.test.client.Composite.class );
  }

  void postVisit( model.test.server.FemaleP aPersistableObject )
  {
    propagateOneToOne( aPersistableObject, "husband", model.test.server.MaleP.class, "wife", model.test.server.FemaleP.class );
    propagateManyToMany( aPersistableObject, "brothers", model.test.server.MaleP.class, "sisters", model.test.server.FemaleP.class );
  }

  void postVisit( model.test.server.MaleP aPersistableObject )
  {
    propagateOneToOne( aPersistableObject, "wife", model.test.server.FemaleP.class, "husband", model.test.server.MaleP.class );
    propagateManyToMany( aPersistableObject, "sisters", model.test.server.FemaleP.class, "brothers", model.test.server.MaleP.class );
  }

  void postVisit( model.test.server.ComponentP aPersistableObject )
  {
    processPrimitive( aPersistableObject, "name", String.class );
    processPrimitive( aPersistableObject, "alias", String.class );
    propagateToMany( aPersistableObject, "playmates", model.test.server.ComponentP.class );
    propagateToOne( aPersistableObject, "bestFriend", model.test.server.ComponentP.class );
    propagateManyToOne( aPersistableObject, "parent", model.test.server.CompositeP.class, "children", model.test.server.ComponentP.class );
  }

  void postVisit( model.test.server.CompositeP aPersistableObject )
  {
    propagateOneToMany( aPersistableObject, "children", model.test.server.ComponentP.class, "parent", model.test.server.CompositeP.class );
  }

  }