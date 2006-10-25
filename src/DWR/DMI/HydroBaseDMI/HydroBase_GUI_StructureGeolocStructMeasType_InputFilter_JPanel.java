//------------------------------------------------------------------------------
// HydroBase_GUI_StructureGeolocStructMeasType_InputFilter_JPanel -
//	input filter panel for HydroBase_StructureGeolocStructMeasType data 
//------------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
//------------------------------------------------------------------------------
// History:
//
// 2004-08-27	Steven A. Malers, RTi	Implement to simplify generic code that
//					can use instanceof to figure out the
//					input filter panel type.
// 2004-08-31	SAM, RTi		Add a proplist to set the number of
//					input filters.
// 2005-04-05	J. Thomas Sapienza, RTi	Adapted the fields for use with 
//					stored procedures.
// 2005-07-14	JTS, RTi		Added all the structure fields from
//					HydroBase_GUI_Structure
//					_InputFilter_JPanel.
//------------------------------------------------------------------------------

package DWR.DMI.HydroBaseDMI;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTextField;

import RTi.Util.GUI.InputFilter;
import RTi.Util.GUI.InputFilter_JPanel;
import RTi.Util.GUI.JGUIUtil;

import RTi.Util.IO.PropList;

import RTi.Util.String.StringUtil;

public class HydroBase_GUI_StructureGeolocStructMeasType_InputFilter_JPanel
extends InputFilter_JPanel
implements MouseListener
{

/**
Create an InputFilter_JPanel for creating where clauses
for HydroBase_StructureGeolocStructMeasType queries.  This is used by TSTool.
Default filter panel properties are used (e.g., 3 filter groups).
@return a JPanel containing InputFilter instances for 
HydroBase_StructureGeolocStructMeasType queries.
@param hdmi HydroBaseDMI instance.
@param include_SFUT If true, include a filter for the SFUT.
@exception Exception if there is an error.
*/
public HydroBase_GUI_StructureGeolocStructMeasType_InputFilter_JPanel (
						HydroBaseDMI hbdmi,
						boolean include_SFUT )
throws Exception
{	this ( hbdmi, include_SFUT, null );
}

/**
Create an InputFilter_JPanel for creating where clauses
for HydroBase_StructureGeolocStructMeasType queries.  This is used by TSTool.
@return a JPanel containing InputFilter instances for 
HydroBase_StructureGeolocStructMeasType queries.
@param hdmi HydroBaseDMI instance.
@param include_SFUT If true, include a filter for the SFUT.
@param filter_props Properties to configure the input filter, passed to the base
class.
@exception Exception if there is an error.
*/
public HydroBase_GUI_StructureGeolocStructMeasType_InputFilter_JPanel (
						HydroBaseDMI hbdmi,
						boolean include_SFUT,
						PropList filter_props )
throws Exception
{	// Fill in the county for input filters...

	Vector county_data_Vector = hbdmi.getCountyRef();
	Vector county_Vector = new Vector ( county_data_Vector.size() );
	Vector county_internal_Vector = new Vector ( county_data_Vector.size());
	HydroBase_CountyRef county;
	int size = county_data_Vector.size();
	for ( int i = 0; i < size; i++ ) {
		county = (HydroBase_CountyRef)county_data_Vector.elementAt(i);
		county_Vector.addElement (
			county.getCounty() + ", " + county.getST() );
		county_internal_Vector.addElement (county.getCounty() );
	}

	// Fill in the water district data for input filters...

	Vector district_data_Vector = hbdmi.getWaterDistricts();
	Vector district_Vector = new Vector ( district_data_Vector.size() );
	Vector district_internal_Vector=new Vector(district_data_Vector.size());
	HydroBase_WaterDistrict wd;
	size = district_data_Vector.size();
	for ( int i = 0; i < size; i++ ) {
		wd = (HydroBase_WaterDistrict)district_data_Vector.elementAt(i);
		district_Vector.addElement (wd.getWD() + " - "+wd.getWd_name());
		district_internal_Vector.addElement ("" + wd.getWD() );
	}

	// Fill in the division data for input filters...

	Vector division_data_Vector = hbdmi.getWaterDivisions();
	Vector division_Vector = new Vector ( 7 );
	Vector division_internal_Vector = new Vector ( 7 );
	HydroBase_WaterDivision div;
	size = division_data_Vector.size();
	for ( int i = 0; i < size; i++ ) {
		div =(HydroBase_WaterDivision)division_data_Vector.elementAt(i);
		division_Vector.addElement (div.getDiv() + " - " +
			div.getDiv_name());
		division_internal_Vector.addElement ("" + div.getDiv() );
	}

	Vector input_filters = new Vector(8);
	input_filters.addElement ( new InputFilter (
		"", "", StringUtil.TYPE_STRING,
		null, null, true ) );	// Blank to disable filter
		
	Vector v1 = new Vector();
	Vector v2 = new Vector();
	Vector cius = hbdmi.getCIUVector();
	size = cius.size();
	HydroBase_RefCIU ciu = null;
	for (int i = 0; i < size; i++) {
		ciu = (HydroBase_RefCIU)cius.elementAt(i);
		v1.add(ciu.getCode() + " - " + ciu.getDescription());
		v2.add(ciu.getCode());
	}

	InputFilter filter = new InputFilter (
		"County Name", "geoloc.county", "county",
		StringUtil.TYPE_STRING,
		county_Vector, county_internal_Vector, true );
	filter.setTokenInfo(",",0);
	input_filters.addElement ( filter );
	
	filter = new InputFilter (
		"District", "geoloc.wd", "wd",
		StringUtil.TYPE_INTEGER,
		district_Vector, district_internal_Vector, true );
	filter.setTokenInfo("-",0);
	input_filters.addElement ( filter );

	filter = new InputFilter (
		"Division", "geoloc.div", "div",
		StringUtil.TYPE_INTEGER,
		division_Vector, division_internal_Vector, true );
	filter.setTokenInfo("-",0);
	input_filters.addElement ( filter );
	
	input_filters.addElement ( new InputFilter (
		"Elevation", "geoloc.elev", "elev",
		StringUtil.TYPE_DOUBLE,
		null, null, true ) );	
	
	input_filters.addElement ( new InputFilter (
		"HUC", "geoloc.huc", "huc",
		StringUtil.TYPE_STRING,
		null, null, true ) );

	input_filters.addElement ( new InputFilter (
		"Latitude", "geoloc.latdecdeg", "latdecdeg",
		StringUtil.TYPE_DOUBLE,
		null, null, true ) );	
		
	input_filters.addElement ( new InputFilter (
		"Longitude", "geoloc.longdecdeg", "longdecdeg",
		StringUtil.TYPE_DOUBLE,
		null, null, true ) );	

	// create the input filter for the PLSS Location
	filter = new InputFilter(
		HydroBase_GUI_Util._PLSS_LOCATION_LABEL,
		HydroBase_GUI_Util._PLSS_LOCATION, 
		HydroBase_GUI_Util._PLSS_LOCATION, StringUtil.TYPE_STRING,
		null, null, false);
	// all constraints other than EQUALS are removed because PLSS Locations
	// are compared in a special way
	filter.removeConstraint(filter.INPUT_ONE_OF);
	filter.removeConstraint(filter.INPUT_STARTS_WITH);
	filter.removeConstraint(filter.INPUT_ENDS_WITH);
	filter.removeConstraint(filter.INPUT_CONTAINS);
	// the PLSS Location text field is not editable because users must go
	// through the PLSS Location JDialog to build a location
	filter.setInputJTextFieldEditable(false);
	// this listener must be set up so that the location builder dialog
	// can be opened when the PLSS Location text field is clicked on.
	filter.addInputComponentMouseListener(this);
	filter.setInputComponentToolTipText("Click in this field to build "
		+ "a PLSS Location to use as a query constraint.");
	filter.setInputJTextFieldWidth(20);
	input_filters.add(filter);

	if (hbdmi.isDatabaseVersionAtLeast(HydroBaseDMI.VERSION_19990305)) {
		input_filters.add(new InputFilter("Stream Mile",
			"geoloc.str_mile", "str_mile", 
			StringUtil.TYPE_DOUBLE, null, null, false));
	}
	else {
		input_filters.add(new InputFilter("Stream Mile",
			"structure.abbrev", "abbrev", 
			StringUtil.TYPE_DOUBLE, null, null, false));
	}

	input_filters.addElement ( new InputFilter (
		"Structure ID", "structure.id", "id",
		StringUtil.TYPE_INTEGER,
		null, null, true ) );
		
	input_filters.addElement ( new InputFilter (
		"Structure Name", "structure.str_name", "str_name",
		StringUtil.TYPE_STRING,
		null, null, true ) );

	if ( include_SFUT ) {
		input_filters.addElement ( new InputFilter (
			"SFUT", "struct_meas_type.identifier", "identifier",
			StringUtil.TYPE_STRING,
			null, null, true ) );
	}

	v1 = new Vector();
	v1.add("1");

	input_filters.addElement ( new InputFilter (
		"UTM X", "geoloc.utm_x", "utm_x",
		StringUtil.TYPE_DOUBLE,
		null, null, true ) );		

	input_filters.addElement ( new InputFilter (
		"UTM Y", "geoloc.utm_y", "utm_y",
		StringUtil.TYPE_DOUBLE,
		null, null, true ) );		

	if ( filter_props == null ) {
		filter_props = new PropList ( "InputFilter" );
	}
	if ( filter_props.getValue ( "NumFilterGroups" ) == null ) {
		// REVISIT - need larger default?
		filter_props.set ( "NumFilterGroups=3" );
		filter_props.set("NumWhereRowsToDisplay=" 
			+ input_filters.size());
	}
	setToolTipText (
		"<HTML>HydroBase queries can be filtered" +
		"<BR>based on structure data." +
		"</HTML>" );
	setInputFilters ( input_filters, filter_props );
}

public void mouseClicked(MouseEvent event) {}

public void mouseExited(MouseEvent event) {}

public void mouseEntered(MouseEvent event) {}

/**
Responds to mouse pressed events.
@param event the event that happened.
*/
public void mousePressed(MouseEvent event) {
	JFrame temp = new JFrame();
	JGUIUtil.setIcon(temp, JGUIUtil.getIconImage());	
	HydroBase_GUI_Util.buildLocation(temp, (JTextField)event.getSource());
}

public void mouseReleased(MouseEvent event) {}

}
