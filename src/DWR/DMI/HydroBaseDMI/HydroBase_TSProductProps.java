// ----------------------------------------------------------------------------
// HydroBase_TSProductProps - class to store information from the TSProduct
//	table
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
// 2004-03-08	J. Thomas Sapienza, RTi	Initial version from 
//					RiversideDB_TSProductProps
// ----------------------------------------------------------------------------

package DWR.DMI.HydroBaseDMI;

import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

/**
Class to store data from the Area table
*/
public class HydroBase_TSProductProps 
extends DMIDataObject {

protected int _TSProduct_num = 	DMIUtil.MISSING_INT;
protected String _Property = 	DMIUtil.MISSING_STRING;
protected String _Value = 	DMIUtil.MISSING_STRING;
protected int _Sequence = 	DMIUtil.MISSING_INT;

/**
HydroBase_TSProductProps constructor.
*/
public HydroBase_TSProductProps () {
	super();
}

/**
cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() 
throws Throwable {
	_Value = null;
	_Property = null;
	super.finalize();
}

/**
Returns _Property
@return _Property
*/
public String getProperty() {
	return _Property;
}

/**
Returns _Sequence
@return _Sequence
*/
public int getSequence() {
	return _Sequence;
}

/**
Returns _Value
@return _Value
*/
public String getValue() {
	return _Value;
}

/**
Returns _TSProduct_num
@return _TSProduct_num
*/
public int getTSProduct_num() {
	return _TSProduct_num;
}

/**
Sets _Property
@param Property value to put into _Property
*/
public void setProperty(String Property) {
	 _Property = Property;
}

/**
Sets _Sequence
@param Sequence value to put into _Identifier
*/
public void setSequence(int Sequence) {
	 _Sequence = Sequence;
}

/**
Sets _Value
@param Value value to put into _Value
*/
public void setValue(String Value) {
	 _Value = Value;
}

/**
Sets _TSProduct_num
@param TSProduct_num value to put into _TSProduct_num
*/
public void setTSProduct_num(int TSProduct_num) {
	 _TSProduct_num = TSProduct_num;
}

/** 
returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return "HydroBase_TSProductProps{" 			+ "\n" + 
		"TSProduct_num: " + _TSProduct_num + "\n" +
		"Property:      '" + _Property + "'\n" + 
		"Value:         '" + _Value + "'\n" + 
		"Sequence:      " + _Sequence + "\n}\n";
}

}
