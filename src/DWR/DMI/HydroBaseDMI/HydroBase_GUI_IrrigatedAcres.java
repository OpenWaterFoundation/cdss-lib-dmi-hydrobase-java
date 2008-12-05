//-----------------------------------------------------------------------------
// HydroBase_GUI_IrrigatedAcres - Irrigated Acres Data GUI
//-----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
//-----------------------------------------------------------------------------
// History:
// 14 Oct 1997	DLG, RTi	Created initial version.
// 07 Dec 1997	SAM RTi		Enable export, print.
// 30 Apr 1998  DLG, RTi	Updated to 1.1 event model, added
//				javadoc comments.
// 22 May 1998  CGB		Comment out soil type and AWC displays
// 09 Feb 1999	CEN, RTi	The field choice selection was being filled
//				by calling "getIrrigationStructureNumber".
//				That should have been "getIrrigatedAcreNumber".
//				Therefore, the initial data in the remainder
//				of the GUI was correct until the user selected
//				an item from the field choice.  From that point
//				on, the user was working with bogus field #'s.
// 04 Apr 1999	SAM, RTi	Add HBDMI to queries.
// 07 Nov 2000	CEN, RTi	Pulled contents of the irrig summary window
//				into this window.
// 2001-11-12	SAM, RTi	Change GUI to JGUIUtil.  Remove import *.
// 2002-02-25	SAM, RTi	Lengthen ID field (need for dams).
//-----------------------------------------------------------------------------
// 2003-09-24	J. Thomas Sapienza, RTi	Initial Swing version.
// 2004-01-20	JTS, RTi		Began using the JScrollWorksheet in
//					order to use worksheet row headers.
// 2004-02-20	SAM, RTi		Change IrrigSummaryStructure to
//					StructureIrrigSummary.
// 2004-05-12	JTS, RTi		Parcel details moved out of the old
//					method of displaying them and into
//					a worksheet.
// 2004-09-22	SAM, RTi		Change HydroBase_ParcelUseTS to joined
//					HydroBase_ParcelUseTSStructureToParcel.
// 2005-03-24	JTS, RTi		Now supports display of View data.
// 2005-04-28	JTS, RTi		Added finalize().
// 2005-05-09	JTS, RTi		All structure queries now return
//					structure view objects.
// 2005-06-22	JTS, RTi		* Column widths now come from the 
//					  table model, not the cell renderer.
//					* The table-specific cell renderers 
//					  were removed and replaced with a 
//					  single generic one.
// 2005-07-11	SAM, RTi		Widen the window to view additional
//					columns without resizing.
// 2007-02-08	SAM, RTi		Remove dependence on CWRAT.
//					Pass JFrame to constructor.
//					Clean up code based on Eclipse feedback.
//-----------------------------------------------------------------------------
// EndHeader

package DWR.DMI.HydroBaseDMI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import RTi.DMI.DMIUtil;

import RTi.Util.IO.PrintJGUI;
import RTi.Util.IO.PropList;

import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.JScrollWorksheet;
import RTi.Util.GUI.JWorksheet;
import RTi.Util.GUI.SimpleJButton;

import RTi.Util.Message.Message;

import RTi.Util.String.StringUtil;

/**
This class is a GUI for displaying information about a structure's
irrigated acres.
*/
public class HydroBase_GUI_IrrigatedAcres 
extends JFrame
implements ActionListener, WindowListener {

private final String __CLASS = "HydroBase_GUI_IrrigatedAcres";

/**
Button labels.
*/
private final String    
	__BUTTON_CLOSE = 	"Close",
	__BUTTON_EXPORT = 	"Export",
	__BUTTON_PRINT = 	"Print";

/**
The DMI through which to connect to the database.
*/
private HydroBaseDMI __dmi;

/**
The number of the structure for which irrigated acres data is being shown.
*/
private int __structureNum;

/**
GUI JTextfields to display data.
*/
private JTextField 
	__statusJTextField,
	__wdJTextField,
	__idJTextField,
	__divJTextField,
	__gisTotJTextField,
	__gisTotDateJTextField,
	__divTotJTextField,
	__divTotDateJTextField,
	__structTotJTextField,
	__structTotDateJTextField,
	__structureJTextField;
	//__awcJTextField,
	//__soilTypeJTextField;

/**
The worksheet in which irrig time series data will be displayed.
*/
private JWorksheet __worksheet;

/**
The name of the structure whose data is displayed on the form.
*/
private String __structureName;

/**
Constructor.
@param dmi the dmi connection to use for communicating with the database.
@param structureName Structure name as determined by the Structure Query GUI.
@param structureNum Structure number as determined by the Structure Query GUI.
*/
public HydroBase_GUI_IrrigatedAcres(HydroBaseDMI dmi, String structureName, 
int structureNum) {
        __dmi = dmi;
        __structureNum = structureNum;
        __structureName = structureName;
	JGUIUtil.setIcon(this, JGUIUtil.getIconImage());
        setupGUI();

	submitAndDisplayIrrigSummaryStructureQuery();
	submitAndDisplayStructureQuery();

	int wd = (new Integer(__wdJTextField.getText().trim())).intValue();
	int id = (new Integer(__idJTextField.getText().trim())).intValue();
	String name = __structureJTextField.getText().trim();

	String rest = "Structure Data - Irrigated Acres by Parcel - "
		+ HydroBase_WaterDistrict.formWDID(wd, id)
		+ " (" + name + ")";
	if ((JGUIUtil.getAppNameForWindows() == null) 
		|| JGUIUtil.getAppNameForWindows().equals("")) {
		setTitle(rest);
	}
	else {	
		setTitle(JGUIUtil.getAppNameForWindows() + " - " + rest);
	}				

	try {
		List v = 
			__dmi.
			readParcelUseTSStructureToParcelListForStructure_num(
			__structureNum);
		__worksheet.setData(v);
	}
	catch (Exception e) {	
		Message.printWarning(1, __CLASS + ".constructor()", 
			"Error reading from database.");
		Message.printWarning(2, __CLASS + ".constructor()", e);
	}
}

/**
Responds to action performed events.
@param e the ActionEvent that happened.
*/         
public void actionPerformed(ActionEvent e) {
	String routine = "HydroBase_GUI_IrrigatedAcres.actionPerformed";
	String s = e.getActionCommand();

        if (s.equals(__BUTTON_CLOSE)) {
                closeClicked();
        }
	else if (s.equals(__BUTTON_EXPORT)) {
		try {
			String[] eff = 
				HydroBase_GUI_Util.getExportFilenameAndFormat(
				this, 
				HydroBase_GUI_Util.getFormatsAndExtensions());

			if (eff == null) {
				return ;
			}

			int format = new Integer(eff[1]).intValue();
	 		// First format the output...
			List outputStrings = formatOutput(format);
 			// Now export, letting the user decide the file...
			HydroBase_GUI_Util.export(this, eff[0], outputStrings);
		} 
		catch (Exception ex) {
			Message.printWarning (2, routine, ex);
		}	
	}
	else if (s.equals(__BUTTON_PRINT)) {
		try {
			SelectFormatTypeJDialog d = 
				new SelectFormatTypeJDialog(this, 
				HydroBase_GUI_Util.getFormats());
			int format = d.getSelected();
			if (format == HydroBase_GUI_Util.CANCEL) {
				return;
			}
			d.dispose();
	 		// First format the output...
			List outputStrings = formatOutput(format);
	 		// Now print...
			PrintJGUI.print(this, outputStrings);
		}
		catch (Exception ex) {
			Message.printWarning (2, routine, ex);
		}		
	}
}

/**
Closes the GUI.  
*/
private void closeClicked() {
	dispose();
}

/**
Cleans up member variables.
*/
public void finalize()
throws Throwable {
	__dmi = null;
	__statusJTextField = null;
	__wdJTextField = null;
	__idJTextField = null;
	__divJTextField = null;
	__gisTotJTextField = null;
	__gisTotDateJTextField = null;
	__divTotJTextField = null;
	__divTotDateJTextField = null;
	__structTotJTextField = null;
	__structTotDateJTextField = null;
	__structureJTextField = null;
	//__awcJTextField = null;
	//__soilTypeJTextField = null;
	__worksheet = null;
	__structureName = null;
	super.finalize();
}

/**
Formats the data on the form for output to a file or the printer.
@param format the format delimiter flag to use for delimiting fields.
*/
public List formatOutput(int format) {
	List v = new Vector();
	int size = __worksheet.getRowCount();

	if (format == HydroBase_GUI_Util.SCREEN_VIEW) {
		v.add(
			HydroBase_GUI_Util.formatStructureHeader(
			HydroBase_GUI_Util.trimText(__structureJTextField),
			HydroBase_GUI_Util.trimText(__divJTextField),
			HydroBase_GUI_Util.trimText(__wdJTextField),
			HydroBase_GUI_Util.trimText(__idJTextField),
			format));
		v.add("");
		v.add("GIS Total: "
			+ StringUtil.formatString(
				HydroBase_GUI_Util.trimText(__gisTotJTextField),
				"%-10.3f")
			+ "Most Recently Reported: "
			+ StringUtil.formatString(
				HydroBase_GUI_Util.trimText(
				__gisTotDateJTextField),
				"%-10.0d"));
		v.add("Diversion Comments Total: "
			+ StringUtil.formatString(
				HydroBase_GUI_Util.trimText(__divTotJTextField),
				"%-10.3f")
			+ "Most Recently Reported: "
			+ StringUtil.formatString(
				HydroBase_GUI_Util.trimText(
				__divTotDateJTextField), 
				"%-10.0d"));
		v.add("Structure Total: "
			+ StringUtil.formatString(
				HydroBase_GUI_Util.trimText(
				__structTotJTextField),
				"%-10.3f")
			+ "Most Recently Reported: "
			+ StringUtil.formatString(
				HydroBase_GUI_Util.trimText(
				__structTotDateJTextField), 
				"%-10.0d"));
		v.add("");
		v.add("                                    "
			+ "  PARCEL DETAILS");

		// Need to make this sensitive the the number of fields.  Right
		// now, just show what is on the screen...
		v.add("           PARCEL                                  "
			+ "           IRRIGATION       PERCENT");
		v.add("YEAR  DIV  ID        PERIMETER    AREA       CROP "
			+ "TYPE        TYPE             IRRIGATED");
		v.add("___________________________________________________"
			+ "________________________________________________");
		
		for (int i = 0; i < size; i++) {
			v.add(
				__worksheet.getValueAtAsString(i, 0, "%4d") 
				+ "  "
				+ __worksheet.getValueAtAsString(i, 1, "%3d") 
				+ "  "
				+ __worksheet.getValueAtAsString(i, 2, "%8d") 
				+ "  "
				+ __worksheet.getValueAtAsString(i, 3, "%10.3f")
				+ "  "
				+ __worksheet.getValueAtAsString(i, 4, "%10.3f")
				+ "  "
				+ __worksheet.getValueAtAsString(i, 5, 
				"%-15.15s") + "  "
				+ __worksheet.getValueAtAsString(i, 6, 
				"%-15.15s") + "  "
				+ __worksheet.getValueAtAsString(i, 7, 
				"%10.3f"));
		}
	}
	else {	
                char delim = HydroBase_GUI_Util.getDelimiterForFormat(format);	
		v.add(HydroBase_GUI_Util.formatStructureHeader(format));
		v.add(HydroBase_GUI_Util.formatStructureHeader(
			HydroBase_GUI_Util.trimText(__structureJTextField),
			HydroBase_GUI_Util.trimText(__divJTextField),
			HydroBase_GUI_Util.trimText(__wdJTextField),
			HydroBase_GUI_Util.trimText(__idJTextField),
			format));
		v.add("");
		v.add("GIS Total" + delim + "Year:" + delim
			+ "Diversion Comments Total:" + delim + "Year:" + delim
			+ "Structure Total" + delim + "Year:" + delim);
		v.add(HydroBase_GUI_Util.trimText(__gisTotJTextField) 
			+ delim
			+ HydroBase_GUI_Util.trimText(__gisTotDateJTextField) 
			+ delim
			+ HydroBase_GUI_Util.trimText(__divTotJTextField) 
			+ delim
			+ HydroBase_GUI_Util.trimText(__divTotDateJTextField) 
			+ delim
			+ HydroBase_GUI_Util.trimText(__structTotJTextField) 
			+ delim
			+ HydroBase_GUI_Util.trimText(__structTotDateJTextField)
			+ delim);
		v.add("");

		v.add("YEAR" + delim + "DIV" + delim + "PARCEL ID" + delim 
			+ "PERIMETER" + delim + "AREA" + delim + "CROP TYPE" 
			+ delim + "IRRIGATION TYPE" + delim 
			+ "PERCENT IRRIGATED" + delim);
		for (int i = 0; i < size; i++) {
			v.add(
				__worksheet.getValueAtAsString(i, 0, 
				"%4d").trim() + delim
				+ __worksheet.getValueAtAsString(i, 1, 
				"%3d").trim() + delim
				+ __worksheet.getValueAtAsString(i, 2, 
				"%8d").trim() + delim
				+ __worksheet.getValueAtAsString(i, 3, 
				"%10.3f").trim() + delim
				+ __worksheet.getValueAtAsString(i, 4, 
				"%10.3f").trim() + delim
				+ __worksheet.getValueAtAsString(i, 5, 
				"%-15.15s").trim() + delim
				+ __worksheet.getValueAtAsString(i, 6, 
				"%-15.15s").trim() + delim
				+ __worksheet.getValueAtAsString(i, 7, 
				"%10.3f").trim() + delim);
		}
	}	

	return v;
}

/**
Sets up the GUI.
*/
private void setupGUI() {
	String routine = "HydroBase_GUI_IrrigatedAcres.setupGUI";
	addWindowListener(this);

        // objects used throughout the GUI layout
        Insets insetsNLNR = new Insets(0,7,0,7);
        Insets insetsNLBR = new Insets(0,7,7,7);
        Insets insetsTLNR = new Insets(7,7,0,7);
        GridBagLayout gbl = new GridBagLayout();

        // Top JPanel
        JPanel topJPanel = new JPanel();
        topJPanel.setLayout(new BorderLayout());
        getContentPane().add("North", topJPanel);

        // Top: West JPanel
        JPanel topWJPanel = new JPanel();
  	topWJPanel.setLayout(new BorderLayout());
        topJPanel.add("West", topWJPanel);        

	JPanel topWNorthJPanel = new JPanel();
	topWNorthJPanel.setLayout(gbl);
	topWJPanel.add("West", topWNorthJPanel);

        JLabel nameJLabel = new JLabel("Structure Name:");
        JGUIUtil.addComponent(topWNorthJPanel, nameJLabel, 
		0, 0, 1, 1, 0, 0, insetsTLNR, GridBagConstraints.NONE, GridBagConstraints.WEST);
 
        JLabel divJLabel = new JLabel("DIV:");
        JGUIUtil.addComponent(topWNorthJPanel, divJLabel, 
		1, 0, 1, 1, 0, 0, insetsTLNR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JLabel wdJLabel = new JLabel("WD:");
        JGUIUtil.addComponent(topWNorthJPanel, wdJLabel, 
		2, 0, 1, 1, 0, 0, insetsTLNR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JLabel idJLabel = new JLabel("ID:");
        JGUIUtil.addComponent(topWNorthJPanel, idJLabel, 
		3, 0, 1, 1, 0, 0, insetsTLNR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        __structureJTextField = new JTextField();
        __structureJTextField.setText(" " + __structureName + "       ");
        __structureJTextField.setEditable(false);
        JGUIUtil.addComponent(topWNorthJPanel, __structureJTextField, 
		0, 1, 1, 1, 0, 0, insetsNLBR, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

        __divJTextField = new JTextField(5);
        __divJTextField.setEditable(false);
        JGUIUtil.addComponent(topWNorthJPanel, __divJTextField, 
		1, 1, 1, 1, 0, 0, insetsNLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        __wdJTextField = new JTextField(5);
        __wdJTextField.setEditable(false);
        JGUIUtil.addComponent(topWNorthJPanel, __wdJTextField, 
		2, 1, 1, 1, 0, 0, insetsNLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        __idJTextField = new JTextField(10);
        __idJTextField.setEditable(false);
        JGUIUtil.addComponent(topWNorthJPanel, __idJTextField, 
		3, 1, 1, 1, 0, 0, insetsNLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

	// Center North JPanel
	JPanel topWSouthJPanel = new JPanel();
	topWSouthJPanel.setLayout(gbl);
	topWJPanel.add("South", topWSouthJPanel);

	int y=0;
	topWSouthJPanel.setBorder(BorderFactory.createTitledBorder(
		"Irrigated Acres Summary"));

        JGUIUtil.addComponent(topWSouthJPanel, new JLabel("GIS Total (Acres):"),
		0, y, 1, 1, 0, 0, insetsNLNR, GridBagConstraints.NONE, GridBagConstraints.EAST);
        __gisTotJTextField = new JTextField(9);
        __gisTotJTextField.setEditable(false);
        JGUIUtil.addComponent(topWSouthJPanel, __gisTotJTextField, 
		1, y, 1, 1, 0, 0, insetsNLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(topWSouthJPanel, 
		new JLabel("Most Recent Report Year:"),
		2, y, 1, 1, 0, 0, insetsNLNR, GridBagConstraints.NONE, GridBagConstraints.EAST);
        __gisTotDateJTextField = new JTextField(5);
        __gisTotDateJTextField.setEditable(false);
        JGUIUtil.addComponent(topWSouthJPanel, __gisTotDateJTextField, 
		3, y++, 1, 1, 0, 0, insetsNLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);


        JGUIUtil.addComponent(topWSouthJPanel, 
		new JLabel("Diversion Comments Total (Acres):"),
		0, y, 1, 1, 0, 0, insetsNLNR, GridBagConstraints.NONE, GridBagConstraints.EAST);
        __divTotJTextField = new JTextField(9);
        __divTotJTextField.setEditable(false);
        JGUIUtil.addComponent(topWSouthJPanel, __divTotJTextField, 
		1, y, 1, 1, 0, 0, insetsNLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(topWSouthJPanel, 
		new JLabel("Most Recent Report Year:"),
		2, y, 1, 1, 0, 0, insetsNLNR, GridBagConstraints.NONE, GridBagConstraints.EAST);
        __divTotDateJTextField = new JTextField(5);
        __divTotDateJTextField.setEditable(false);
        JGUIUtil.addComponent(topWSouthJPanel, __divTotDateJTextField, 
		3, y++, 1, 1, 0, 0, insetsNLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        JGUIUtil.addComponent(topWSouthJPanel, 
		new JLabel("Structure Total (Acres):"),
		0, y, 1, 1, 0, 0, insetsNLNR, GridBagConstraints.NONE, GridBagConstraints.EAST);
        __structTotJTextField = new JTextField(9);
        __structTotJTextField.setEditable(false);
        JGUIUtil.addComponent(topWSouthJPanel, __structTotJTextField, 
		1, y, 1, 1, 0, 0, insetsNLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);
        JGUIUtil.addComponent(topWSouthJPanel, 
		new JLabel("Most Recent Report Year:"),
		2, y, 1, 1, 0, 0, insetsNLNR, GridBagConstraints.NONE, GridBagConstraints.EAST);
        __structTotDateJTextField = new JTextField(5);
        __structTotDateJTextField.setEditable(false);
        JGUIUtil.addComponent(topWSouthJPanel, __structTotDateJTextField, 
		3, y++, 1, 1, 0, 0, insetsNLBR, GridBagConstraints.NONE, GridBagConstraints.WEST);

        // Center JPanel
        JPanel centerJPanel = new JPanel();
        centerJPanel.setLayout(new GridBagLayout());

        // Center: West JPanel
	centerJPanel.setBorder(BorderFactory.createTitledBorder(
		"Parcel Details"));
        getContentPane().add("Center", centerJPanel);
	PropList p = new PropList("HydroBase_GUI_IrrigatedAcres.JWorksheet");
	p.add("JWorksheet.ShowRowHeader=true");
	p.add("JWorksheet.AllowCopy=true");	
	p.add("JWorksheet.ShowPopupMenu=true");
	p.add("JWorksheet.SelectionMode=ExcelSelection");

	int[] widths = null;
	JScrollWorksheet jsw = null;
	try {
		HydroBase_TableModel_IrrigatedAcres tm = new
			HydroBase_TableModel_IrrigatedAcres(new Vector());
		HydroBase_CellRenderer cr = new HydroBase_CellRenderer(tm);
	
		jsw = new JScrollWorksheet(cr, tm, p);
		__worksheet = jsw.getJWorksheet();

		widths = tm.getColumnWidths();
	}
	catch (Exception e) {
		Message.printWarning(2, routine, e);
		jsw = new JScrollWorksheet(0, 0, p);
		__worksheet = jsw.getJWorksheet();
	}
	__worksheet.setPreferredScrollableViewportSize(null);
	__worksheet.setHourglassJFrame(this);

        JGUIUtil.addComponent(centerJPanel, jsw,
		0, 5, 5, 1, 1, 1, 
		insetsTLNR, GridBagConstraints.BOTH, GridBagConstraints.WEST);

        // Bottom JPanel
        JPanel bottomJPanel = new JPanel();
        bottomJPanel.setLayout(new BorderLayout());
        getContentPane().add("South", bottomJPanel);

        // Bottom: South JPanel
        JPanel bottomSouthJPanel = new JPanel();
        bottomSouthJPanel.setLayout(new BorderLayout());
        bottomJPanel.add("South", bottomSouthJPanel);

        // Bottom: South: North JPanel
        JPanel bottomSouthNorthJPanel = new JPanel();
        bottomSouthNorthJPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        bottomSouthJPanel.add("North", bottomSouthNorthJPanel);

	SimpleJButton print = new SimpleJButton(__BUTTON_PRINT, this);
	print.setToolTipText("Print form data.");
        bottomSouthNorthJPanel.add(print);
	SimpleJButton export = new SimpleJButton(__BUTTON_EXPORT, this);
	export.setToolTipText("Export data to a file.");
	bottomSouthNorthJPanel.add(export);
	SimpleJButton close = new SimpleJButton(__BUTTON_CLOSE, this);
	close.setToolTipText("Close the form.");
        bottomSouthNorthJPanel.add(close);

        // Bottom: South: South JPanel
        JPanel bottomSSJPanel = new JPanel();
        bottomSSJPanel.setLayout(gbl);
        bottomSouthJPanel.add("South", bottomSSJPanel);

        __statusJTextField = new JTextField();
        __statusJTextField.setEditable(false);
        JGUIUtil.addComponent(bottomSSJPanel, __statusJTextField, 
		0, 1, 10, 1, 1, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
      
        // Frame settings
        setTitle("Irrigated Acres Data");
        pack(); 
	setSize(getWidth() + 350, getHeight() + 100);

        JGUIUtil.center(this);
        setVisible(true);

	if (widths != null) {
		__worksheet.setColumnWidths(widths);
	}
}

/**
Submits a query for the desired HydroBase_StructureIrrigSummary data and
displays the results in the gui.
*/
private void submitAndDisplayIrrigSummaryStructureQuery() { 
	JGUIUtil.setWaitCursor(this, true);
	String routine = "HydroBase_GUI_IrrigatedAcres"
		+ ".submitAndDisplayIrrigSummaryStructureQuery";

	List results = null;
	try {
		results = __dmi.readStructureIrrigSummaryListForStructure_num(
			__structureNum);
	}
	catch (Exception e) {
		Message.printWarning(1, routine, e);
		JGUIUtil.setWaitCursor(this, false);
		return;
	}

	if (results == null || results.size() == 0) {
		JGUIUtil.setWaitCursor(this, false);
		return;
	}		

        int curInt;
        double curDouble;

	HydroBase_StructureView iss 
		= (HydroBase_StructureView)results.get(0);
    
	curDouble = iss.getTia_gis();
	if (!DMIUtil.isMissing(curDouble)) {
		__gisTotJTextField.setText(
			StringUtil.formatString(curDouble, 
			"%6.3f").trim());
	}

	curInt = iss.getTia_gis_calyear();
	if (!DMIUtil.isMissing(curInt)) {
		__gisTotDateJTextField.setText("" + curInt);
	}

	curDouble = iss.getTia_div();
	if (!DMIUtil.isMissing(curDouble)) {
		__divTotJTextField.setText(
			StringUtil.formatString(curDouble, 
			"%6.3f").trim());
	}

	curInt = iss.getTia_div_calyear();
	if (!DMIUtil.isMissing(curInt)) {
		__divTotDateJTextField.setText("" + curInt);
	}        

	curDouble = iss.getTia_struct();
	if (!DMIUtil.isMissing(curDouble)) {
		__structTotJTextField.setText(
			StringUtil.formatString(curDouble, 
			"%6.3f").trim());
	}        

	curInt = iss.getTia_struct_calyear();
	if (!DMIUtil.isMissing(curInt)) {
		__structTotDateJTextField.setText("" + curInt);
	}       	

	JGUIUtil.setWaitCursor(this, false);
}

/**
Submits a query for the desired structure and displays the data in the GUI.
*/
private void submitAndDisplayStructureQuery() {
	JGUIUtil.setWaitCursor(this, true);
	String routine = "HydroBase_GUI_IrrigatedAcres"
		+ ".submitAndDisplayStructureQuery";
	
	HydroBase_StructureView view = null;
	try {
		view = __dmi.readStructureViewForStructure_num(
			__structureNum);
	}
	catch (Exception e) {
		Message.printWarning(1, routine, e);
		JGUIUtil.setWaitCursor(this, false);
		return;
	}

	if (view == null) {
		JGUIUtil.setWaitCursor(this, false);
		return;
	}

	int curInt = view.getWD();
        if (!DMIUtil.isMissing(curInt)) {
                __wdJTextField.setText("" + curInt);
        }
 
        curInt = view.getID();
        if (!DMIUtil.isMissing(curInt)) {
                __idJTextField.setText("" + curInt);        
        }

        curInt = view.getDiv();
        if (!DMIUtil.isMissing(curInt)) {
                __divJTextField.setText("" + curInt);
        }
	JGUIUtil.setWaitCursor(this, false);	
}

public void windowActivated(WindowEvent e) {;}
public void windowClosed(WindowEvent e) {;}

/**
Closes the GUI.
@param e WindowEvent object
*/
public void windowClosing(WindowEvent e) {
	closeClicked();
}
public void windowDeactivated(WindowEvent e) {;}
public void windowDeiconified(WindowEvent e) {;}
public void windowIconified(WindowEvent e) {;}
public void windowOpened(WindowEvent e) {;}

}
