// ----------------------------------------------------------------------------
// HydroBase_Use.java - Class to hold data from the HydroBase use table.
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
// 2003-02-17	J. Thomas Sapienza, RTi	Initial version from HBUse.
// 2003-02-24	JTS, RTi		Corrected error in finalize() so that 
//					super.finalize() gets called.
// ----------------------------------------------------------------------------

package DWR.DMI.HydroBaseDMI;

import RTi.DMI.DMI;
import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

/**
Class to store data from the the HydroBase use table.
*/
public class HydroBase_Use 
extends DMIDataObject {

protected String _use = 	DMIUtil.MISSING_STRING;
protected String _xuse = 	DMIUtil.MISSING_STRING;
protected String _use_def = 	DMIUtil.MISSING_STRING;
protected String _ok_for_wr = 	DMIUtil.MISSING_STRING;
protected String _rpt_code = 	DMIUtil.MISSING_STRING;

/**
Constructor.
*/
public HydroBase_Use() {
	super();
}

/**
cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
@exception Throwable if an error occurs.
*/
protected void finalize()
throws Throwable {
	_use = null;
	_xuse = null;
	_use_def = null;
	_ok_for_wr = null;
	_rpt_code = null;
	
	super.finalize();
}

/**
Returns _ok_for_wr
@return _ok_for_wr
*/
public String getOk_for_wr() {
	return _ok_for_wr;
}

/**
Returns _rpt_code
@return _rpt_code
*/
public String getRpt_code() {
	return _rpt_code;
}

/**
Returns _use
@return _use
*/
public String getUse() {
	return _use;
}

/**
Returns _use_def
@return _use_def
*/
public String getUse_def() {
	return _use_def;
}

/**
Returns _xuse
@return _xuse
*/
public String getXuse() {
	return _xuse;
}

/**
Sets _ok_for_wr
@param ok_for_wr value to put into _ok_for_wr
*/
public void setOk_for_wr(String ok_for_wr) {
	_ok_for_wr = ok_for_wr;
}

/**
Sets _rpt_code
@param rpt_code value to put into _rpt_code
*/
public void setRpt_code(String rpt_code) {
	_rpt_code = rpt_code;
}

/**
Sets _use
@param use value to put into _use
*/
public void setUse(String use) {
	_use = use;
}

/**
Sets _use_def
@param use_def value to put into _use_def
*/
public void setUse_def(String use_def) {
	_use_def = use_def;
}

/**
Sets _xuse
@param xuse value to put into _xuse
*/
public void setXuse(String xuse) {
	_xuse = xuse;
}

/** 
returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return "HydroBase_Use {"		+ "\n" + 
		"Use:       " + _use + "\n" + 
		"Xuse:      " + _xuse + "\n" + 
		"Use_def:   " + _use_def + "\n" + 
		"Ok_for_wr: " + _ok_for_wr + "\n" + 
		"Rpt_code:  " + _rpt_code + "\n}\n";
}

}
