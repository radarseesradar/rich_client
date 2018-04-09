package smartClient.framework;

import java.io.*;
import java.util.*;

/**
 * @author Steve McDaniel 
 * <br>Copyright (c) Steve McDaniel
 */

public class VisitorPrimitivesCopier extends VisitorSynchronizer
{
  public VisitorPrimitivesCopier( Persistable _other )
  {
    setOther( _other );
  }

  public Persistable propagateManyToOne( Object attributeOwner, String attributeName, Class attributeType, String inverseAttribute, Class inverseAttributeType )
  {
      return null;
  }

  public Persistable propagateOneToOne( Object attributeOwner, String attributeName, Class attributeType, String inverseAttribute, Class inverseAttributeType )
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

  public Persistable propagateToOne( Object attributeOwner, String attributeName, Class attributeType )
  {
    return null;
  }


}