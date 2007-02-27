// ----------------------------------------------------------------------------
// HydroBase_MonthlyBase.java - Base class for the HydroBase monthly_* tables.
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
// 2003-02-18	J. Thomas Sapienza, RTi	Initial version from
//					HBStationMonthTSRecord.
// 2003-02-24	JTS, RTi		Corrected error in finalize() so that 
//					super.finalize() gets called.
// 2007-02-26	SAM, RTi		Clean up code based on Eclipse feedback.
// ----------------------------------------------------------------------------

package DWR.DMI.HydroBaseDMI;

import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

import java.util.Date;

/**
Base class for classes to hold data from the HydroBase monthly_* tables.  
This class is the base class for:
HydroBase_MonthlyEvap
HydroBase_MonthlyFlow
HydroBase_MonthlyNflow
HydroBase_MonthlyPcpn
HydroBase_MonthlySnow
HydroBase_MonthlyTemp
*/
public class HydroBase_MonthlyBase 
extends DMIDataObject {

protected int _meas_num = 	DMIUtil.MISSING_INT;
protected int _cal_year = 	DMIUtil.MISSING_INT;
protected int _cal_mon_num = 	DMIUtil.MISSING_INT;
protected int _station_num = 	DMIUtil.MISSING_INT;
protected String _cal_mon = 	DMIUtil.MISSING_STRING;
protected String _unit = 	DMIUtil.MISSING_STRING;
protected String _data_source = DMIUtil.MISSING_STRING;
protected Date _modified = 	DMIUtil.MISSING_DATE;
protected int _user_num = 	DMIUtil.MISSING_INT;

/**
Constructor.
*/
public HydroBase_MonthlyBase() {
	super();
}

/**
cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
@exception Throwable if an error occurs.
*/
protected void finalize()
throws Throwable {
	_cal_mon = null;
	_unit = null;
	_data_source = null;
	_modified = null;

	super.finalize();
}

/**
Returns _cal_mon
@return _cal_mon
*/
public String getCal_mon() {
	return _cal_mon;
}

/**
Returns _cal_mon_num
@return _cal_mon_num
*/
public int getCal_mon_num() {
	return _cal_mon_num;
}

/**
Returns _cal_year
@return _cal_year
*/
public int getCal_year() {
	return _cal_year;
}

/**
Returns _data_source
@return _data_source
*/
public String getData_source() {
	return _data_source;
}

/**
Returns _meas_num
@return _meas_num
*/
public int getMeas_num() {
	return _meas_num;
}

/**
Returns _modified
@return _modified
*/
public Date getModified() {
	return _modified;
}

/**
Returns _station_num
@return _station_num
*/
public int getStation_num() {
	return _station_num;
}

/**
Returns _unit
@return _unit
*/
public String getUnit() {
	return _unit;
}

/**
Returns _user_num
@return _user_num
*/
public int getUser_num() {
	return _user_num;
}

/**
Sets _cal_mon
@param cal_mon value to put into _cal_mon
*/
public void setCal_mon(String cal_mon) {
	_cal_mon = cal_mon;
}

/**
Sets _cal_mon_num
@param cal_mon_num value to put into _cal_mon_num
*/
public void setCal_mon_num(int cal_mon_num) {
	_cal_mon_num = cal_mon_num;
}

/**
Sets _cal_year
@param cal_year value to put into _cal_year
*/
public void setCal_year(int cal_year) {
	_cal_year = cal_year;
}

/**
Sets _data_source
@param data_source value to put into _data_source
*/
public void setData_source(String data_source) {
	_data_source = data_source;
}

/**
Sets _meas_num
@param meas_num value to put into _meas_num
*/
public void setMeas_num(int meas_num) {
	_meas_num = meas_num;
}

/**
Sets _modified
@param modified value to put into _modified
*/
public void setModified(Date modified) {
	_modified = modified;
}

/**
Sets _station_num
@param station_num value to put into _station_num
*/
public void setStation_num(int station_num) {
	_station_num = station_num;
}

/**
Sets _unit
@param unit value to put into _unit
*/
public void setUnit(String unit) {
	_unit = unit;
}

/**
Sets _user_num
@param user_num value to put into _user_num
*/
public void setUser_num(int user_num) {
	_user_num = user_num;
}

/** 
returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return "HydroBase_MonthlyBase {"		+ "\n" + 
		"Meas_num:    " + _meas_num + "\n" +
		"Cal_year:    " + _cal_year + "\n" + 
		"Cal_mon_num: " + _cal_mon_num + "\n" +
		"Station_num: " + _station_num + "\n" +
		"Cal_mon:     " + _cal_mon + "\n" + 
		"Unit:        " + _unit + "\n" + 
		"Data_source: " + _data_source + "\n" +
		"Modified:    " + _modified + "\n" + 
		"User_num:    " + _user_num + "\n}\n";
}	

}
