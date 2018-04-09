package Keyboard.clientBusinessObjects;

/**
 * Title:        Persistent Object Graph
 * Description:
 * Copyright:    Copyright (c) Steve McDaniel
 * Company:      Salient
 * @author Steve McDaniel
 * @version 1.0
 */

import java.util.*;
import java.lang.reflect.*;
import java.lang.*;
import Keyboard.clientBusinessObjects.*;
import Keyboard.*;
import java.io.*;

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

  private Collection propagateToMany( Object attributeOwner, String attributeName, Class attributeType )
  {
    return  getVisitor().propagateToMany(  attributeOwner,  attributeName,  attributeType );
  }

  private Visitable propagateToOne( Object attributeOwner, String attributeName, Class attributeType )
  {
    return getVisitor().propagateToOne( attributeOwner, attributeName, attributeType );
  }

  private void processPrimitive( Object attributeOwner, String attributeName, Class attributeType )
  {
    getVisitor().processPrimitive( attributeOwner, attributeName, attributeType );
  }

  private void processUpdateCounter( Object attributeOwner )
  {
    getVisitor().processUpdateCounter( attributeOwner );
  }

  private Visitable propagateManyToOne( Object attributeOwner, String attributeName, Class attributeType, String inverseAttribute, Class inverseAttributeType )
  {
      return getVisitor().propagateManyToOne( attributeOwner, attributeName, attributeType, inverseAttribute, inverseAttributeType );
  }

  private Visitable propagateOneToOne( Object attributeOwner, String attributeName, Class attributeType, String inverseAttribute, Class inverseAttributeType )
  {
      return getVisitor().propagateOneToOne( attributeOwner, attributeName, attributeType, inverseAttribute, inverseAttributeType );
  }

  private Collection propagateOneToMany( Object attributeOwner, String attributeName, Class attributeType, String inverseAttribute, Class inverseType )
  {
    return  getVisitor().propagateOneToMany(  attributeOwner,  attributeName,  attributeType,  inverseAttribute,  inverseType );
  }

  private Collection propagateManyToMany( Object attributeOwner, String attributeName, Class attributeType, String inverseAttribute, Class inverseType )
  {
    return  getVisitor().propagateManyToMany(  attributeOwner,  attributeName,  attributeType,  inverseAttribute,  inverseType );
  }

  void postVisit( TVCa aVisitableObject )
  {
    propagateOneToOne( aVisitableObject, "husband", TVCb.class, "wife", TVCa.class );
    propagateManyToMany( aVisitableObject, "brothers", TVCb.class, "sisters", TVCa.class );
  }

  void postVisit( TVCb aVisitableObject )
  {
    propagateOneToOne( aVisitableObject, "wife", TVCa.class, "husband", TVCb.class );
    propagateManyToMany( aVisitableObject, "sisters", TVCa.class, "brothers", TVCb.class );
  }

  void postVisit( TVComponent aVisitableObject )
  {
    processPrimitive( aVisitableObject, "name", String.class );
    processPrimitive( aVisitableObject, "alias", String.class );
    processUpdateCounter( aVisitableObject );
    propagateManyToOne( aVisitableObject, "parent", TVComposite.class, "children", TVComponent.class );
  }

  void postVisit( TVComposite aVisitableObject )
  {
    propagateOneToMany( aVisitableObject, "children", TVComponent.class, "parent", TVComposite.class );
  }

  void postVisit( TVPCa aVisitableObject )
  {
    propagateOneToOne( aVisitableObject, "husband", TVPCb.class, "wife", TVPCa.class );
    propagateManyToMany( aVisitableObject, "brothers", TVPCb.class, "sisters", TVPCa.class );
  }

  void postVisit( TVPCb aVisitableObject )
  {
    propagateOneToOne( aVisitableObject, "wife", TVPCa.class, "husband", TVPCb.class );
    propagateManyToMany( aVisitableObject, "sisters", TVPCa.class, "brothers", TVPCb.class );
  }

  void postVisit( TVPComponent aVisitableObject )
  {
    processPrimitive( aVisitableObject, "name", String.class );
    processPrimitive( aVisitableObject, "alias", String.class );
    processUpdateCounter( aVisitableObject );
    propagateManyToOne( aVisitableObject, "parent", TVPComposite.class, "children", TVPComponent.class );
  }

  void postVisit( TVPComposite aVisitableObject )
  {
    propagateOneToMany( aVisitableObject, "children", TVPComponent.class, "parent", TVPComposite.class );
  }

  }