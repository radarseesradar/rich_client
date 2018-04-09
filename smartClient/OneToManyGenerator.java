/* Generated by Together */

package com.togethersoft.modules.smartClient;

import com.togethersoft.openapi.rwi.RwiProperty;
import com.togethersoft.openapi.rwi.RwiElement;
import com.togethersoft.openapi.rwi.RwiNode;
import com.togethersoft.openapi.rwi.RwiPattern;
import com.togethersoft.openapi.rwi.RwiMember;

public class OneToManyGenerator extends CommonGenerator implements CodeGenerator
{

	public void generateCode( SupplierEnd aSupplierEnd )
    {
		adjustField( aSupplierEnd, Literals.TO_MANY, Literals.ONE_TO_MANY  );
        if( aSupplierEnd.getField() != null )
        {
    		aSupplierEnd.getField().setProperty( Association.UserDefinedPropertyName.UNDIRECTED, true );
        }
        generateGetAll( aSupplierEnd );
        generateAdder( aSupplierEnd );
        generateRemoveOneOfMany( aSupplierEnd );
    }


	public void generateCode( ClientEnd aClientEnd )
    {
        adjustField( aClientEnd, Literals.TO_ONE, Literals.MANY_TO_ONE );
        generateGetOne( aClientEnd );
        generateSetter( aClientEnd );
    }

}