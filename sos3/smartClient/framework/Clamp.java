package smartClient.framework;

/**
 * Title:        Smart Client Framework<br>
 * Copyright:    Copyright (c) Steve McDaniel<br>
 * Company:      Smart Client Framework inc.<br>
 * @author Steve McDaniel
 * @version 1.0
 */

public class Clamp
{
  private String fromClassName;
  private String toRoleName;
  private Clamp inverseClamp;
  private AssociationChangeList changeList;

  public Clamp( String _fromClassName, String _toRoleName )
  {
    fromClassName = _fromClassName;
    toRoleName = _toRoleName;
    changeList = new AssociationChangeList();
  }

  public void clearChangeList()
  {
    changeList.clear();
  }

  public AssociationChangeList getChangeList()
  {
    return changeList;
  }

  public void setInverseClamp(Clamp theInverseClamp)
  {
    inverseClamp = theInverseClamp;
  }

  public Clamp getInverseClamp()
  {
    return inverseClamp;
  }

  public String getFromClassName()
  {
    return fromClassName;
  }

  public boolean equals( Object another )
  {
    return fromClassName.equals( ((Clamp)another).fromClassName )  && toRoleName.equals( ((Clamp)another).toRoleName );
  }

  public int hashCode()
  {
    return fromClassName.hashCode() ^ toRoleName.hashCode();
  }

}