
package com.togethersoft.modules.smartClient;

import com.togethersoft.openapi.ide.IdeContext;
import com.togethersoft.openapi.ide.IdeScript;
import com.togethersoft.openapi.ide.window.IdeWindowManager;
import com.togethersoft.openapi.ide.window.IdeWindowManagerAccess;
import com.togethersoft.openapi.ide.message.IdeMessageManagerAccess;
import com.togethersoft.openapi.ide.project.IdeProjectManagerAccess;
import com.togethersoft.openapi.ide.message.IdeMessageType;
import com.togethersoft.openapi.rwi.RwiProperty;
import com.togethersoft.openapi.rwi.RwiElement;
import com.togethersoft.openapi.rwi.RwiShapeType;
import com.togethersoft.openapi.rwi.RwiLink;
import com.togethersoft.openapi.rwi.enum.RwiPropertyEnumeration;

/**
 * @author Steve McDaniel
 */
public class ConvertToSmartAssociation implements IdeScript
{
   /**
    * Runs this module.
    *
    * @param context the IdeContext instance containing
    * the selection information at the moment the module was called.
    */
    public void run(IdeContext context)
    {

        // printing information message into message pane
        IdeMessageManagerAccess.printMessage
            (IdeMessageType.INFORMATION, "ConvertToSmartAssociation module: started");

        // checking if project is opened
        if (IdeProjectManagerAccess.getProjectManager().getActiveProject() == null)
        {
            // project was not opened
            IdeMessageManagerAccess.printMessage
                (IdeMessageType.ERROR_MODAL, "No open project");
            IdeMessageManagerAccess.printMessage
                (IdeMessageType.INFORMATION, "ConvertToSmartAssociation module: finished");
            return;
        }

        // start work with the selection

        // getting the array of selected RwiElements from context
        RwiElement[] selectedRwiElements = context.getRwiElements();

        // checking if array of elements is empty
        if (selectedRwiElements == null || selectedRwiElements.length == 0)
        {
            // there are not selected elements
            IdeMessageManagerAccess.printMessage
                (IdeMessageType.ERROR_MODAL, "No selection was made");
            IdeMessageManagerAccess.printMessage
                (IdeMessageType.INFORMATION, "ConvertToSmartAssociation module: finished");
            return;
        }

        // making the message pane visible and printing message
        IdeMessageManagerAccess.getMessageManager().setPaneVisible(true);
        this.printMessage( "Selection contains the following elements:");

        // iterating the selection
        for (int i = 0; i < selectedRwiElements.length; i++)
	    {
            this.processSelectedElement( selectedRwiElements[i] );
        }

        // printing final message
        IdeMessageManagerAccess.printMessage
            (IdeMessageType.INFORMATION, "Lesson02 module: finished");
    }

	private void processSelectedElement( RwiElement selection )
    {
    	Association anAssociation = null;
		if (selection == null)
		{
			return;
		}
		if ( ! RwiShapeType.ASSOCIATION.equals(selection.getProperty(RwiProperty.SHAPE_TYPE)) )
		{
        	return;
        }
		if( ! (selection instanceof RwiLink) )
		{
        	return;
		}

		anAssociation = new Association( (RwiLink) selection );
        anAssociation.extractModelInfo();
        anAssociation.generateCode();
        this.printMessage( "anAssociation: \n" + anAssociation.toString() );
    }

    private void printAssociationEnd( RwiElement associationEnd )
    {
        printMessage( "NAME: " + associationEnd.getProperty( RwiProperty.NAME ));
       printMessage( "FULL_NAME: " + associationEnd.getProperty( RwiProperty.FULL_NAME ) );
    }

	private void printMessage( String message )
    {
		IdeMessageManagerAccess.printMessage(IdeMessageType.INFORMATION, message );
    }
}



