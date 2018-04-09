package Keyboard.clientBusinessObjects;

/**
 * Title:        Persistent Object Graph
 * Description:
 * Copyright:    Copyright (c) Steve McDaniel
 * Company:      Salient
 * @author Steve McDaniel
 * @version 1.0
 */

import java.io.*;
import java.util.*;

public class VisitorPrimitivesCopier extends VisitorSynchronizer
{
  public VisitorPrimitivesCopier( Visitable _other )
  {
    setOther( _other );
  }

  public Visitable propagateManyToOne( Object attributeOwner, String attributeName, Class attributeType, String inverseAttribute, Class inverseAttributeType )
  {
      return null;
  }

  public Visitable propagateOneToOne( Object attributeOwner, String attributeName, Class attributeType, String inverseAttribute, Class inverseAttributeType )
  {
      return null;
  }

  public Collection propagateOneToMany( Object attributeOwner, String attributeName, Class attributeType, String inverseAttribute, Class inverseType )
  {
    return  null;
  }

  public Collection propagateManyToMany( Object attributeOwner, String attributeName, Class attributeType, String inverseAttribute, Class inverseType )
  {
    return  null;
  }
  public Collection propagateToMany( Object attributeOwner, String attributeName, Class attributeType )
  {
    return  null;
  }

  public Visitable propagateToOne( Object attributeOwner, String attributeName, Class attributeType )
  {
    return null;
  }


}