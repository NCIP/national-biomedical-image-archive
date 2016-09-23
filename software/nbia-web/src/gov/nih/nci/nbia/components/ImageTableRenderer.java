/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id$
*
* $Log: not supported by cvs2svn $
* Revision 1.1  2007/08/05 21:06:47  bauerd
* Initial Check in of reorganized components
*
* Revision 1.9  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import com.sun.faces.renderkit.html_basic.TableRenderer;



public class ImageTableRenderer extends TableRenderer {
    

        
    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException {

    	encodeChildrenPreconditions(context, component);

        if (!component.isRendered()) {
            return;
        }

        UIData data = (UIData) component;

        // Set up variables we will need
        String[] columnClasses = getColumnClasses(data);
        int columnStyle = 0;
        int columnStyles = columnClasses.length;
        String[] rowClasses = getRowClasses(data);
        int rowStyles = rowClasses.length;
        ResponseWriter writer = context.getResponseWriter();

        // Iterate over the rows of data that are provided
        int processed = 0;
        int rowIndex = data.getFirst() - 1;
        Integer numRows = initializeNumRows(context, component);

        int rowStyle = 0;
        Integer columns = intializeColumns(context, component);

        int rows = numRows ;

        writer.startElement("tbody", component);
        writer.writeText("\n", null);

        while (true) {
            if (haveWeDisplayRequestedNumberOfRows(processed, rows)) {
                writer.endElement("tr");
                writer.writeText("\n", null);

                break;
            }

            // Select the current row
            data.setRowIndex(++rowIndex);

            if (!data.isRowAvailable()) {
                writer.endElement("tr");
                writer.writeText("\n", null);

                break; // Scrolled past the last row
            }

            if (((processed - 1) % columns) == 0) {
            	rowStyle = renderBeginningOfRow(writer,
            			                        data,
            			                        rowClasses,
            			                        rowStyles,
            			                        rowStyle);
            }

            // Iterate over the child UIColumn components for each row
            columnStyle = 0;
            Iterator kids = getColumns(data);

            while (kids.hasNext()) {
                // Identify the next renderable column
                UIColumn column = (UIColumn) kids.next();

                columnStyle = renderBeginningOfCell(writer,
                		                            column,
                		                            columnClasses,
                		                            columnStyles,
                		                            columnStyle);

                // Render the contents of this cell by iterating over
                // the kids of our kids
                Iterator grandkids = getChildren(column);

                while (grandkids.hasNext()) {
                    encodeRecursive(context, (UIComponent) grandkids.next());
                }

                // Render the ending of this cell
                writer.endElement("td");
                writer.writeText("\n", null);
            }

            // Render the ending of this row
            if ((processed  % columns) == 0) {
                writer.endElement("tr");
                writer.writeText("\n", null);
            }
        }

        writer.endElement("tbody");
        writer.writeText("\n", null);

        // Clean up after ourselves
        data.setRowIndex(-1);
    }

    /**
     * The return value is the iteration/count for cell/column CSS classes.
     */
    private static int renderBeginningOfCell(ResponseWriter writer, 
    		                                 UIColumn column,
    		                                 String[] columnClasses,    		                                 
    		                                 int columnStyles,
    		                                 int columnStyle) throws IOException {
        // Render the beginning of this cell
        writer.startElement("td", column);

        if (columnStyles > 0) {
            writer.writeAttribute("class",
                                  columnClasses[columnStyle++], 
                                  "columnClasses");

            if (columnStyle >= columnStyles) {
                columnStyle = 0;
            }
        }   
        return columnStyle;
    }
    
    
    /**
     * The return value is the iteration/count for row CSS classes.
     */    
    private static int renderBeginningOfRow(ResponseWriter writer, 
    		                                UIData data,
    		                                String[] rowClasses,
    		                                int rowStyles,
    		                                int rowStyle) throws IOException {
        writer.startElement("tr", data);

        if (rowStyles > 0) {
            writer.writeAttribute("class",
            		              rowClasses[rowStyle++],
            		              "rowClasses");

            if (rowStyle >= rowStyles) {
                rowStyle = 0;
            }
        }

        writer.writeText("\n", null);   
        
        return rowStyle;
    }
    
    
    private String[] getClasses(String values) {
        if (values == null) {
            return (new String[0]);
        }

        values = values.trim();

        ArrayList<String> list = new ArrayList<String>();

        while (values.length() > 0) {
            int comma = values.indexOf(',');

            if (comma >= 0) {
                list.add(values.substring(0, comma).trim());
                values = values.substring(comma + 1);
            } else {
                list.add(values.trim());
                values = "";
            }
        }

        String[] results = new String[list.size()];

        return ((String[]) list.toArray(results));    	
    }
    
    
    private String[] getColumnClasses(UIData data) {
        String values = (String) data.getAttributes().get("columnClasses");

        return getClasses(values);
    }

    private String[] getRowClasses(UIData data) {
        String values = (String) data.getAttributes().get("rowClasses");

        return getClasses(values);

    }

    private Iterator getColumns(UIData data) {
        List<UIComponent> results = new ArrayList<UIComponent>();
        Iterator kids = data.getChildren().iterator();

        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();

            if ((kid instanceof UIColumn) && kid.isRendered()) {
                results.add(kid);
            }
        }

        return (results.iterator());
    }

    private static Object get(FacesContext context, UIComponent component,
        String name) {
        ValueBinding binding = component.getValueBinding(name);

        if (binding != null) {
            return binding.getValue(context);
        } else {
            return component.getAttributes().get(name);
        }
    }
    
    private static void encodeChildrenPreconditions(FacesContext context, UIComponent component) {
        if ((context == null) || (component == null)) {
        	throw new RuntimeException("image table renderer context comp null");
//            throw new NullPointerException(Util.getExceptionMessageString(
//                    Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }    	
    }    
    
    private Integer initializeNumRows(FacesContext context, UIComponent component) {
        Integer numRows = (Integer) get(context, component, "viewRows");

        if (numRows == null) {
            numRows = 5;
        }
        return numRows;
    }

    private Integer intializeColumns(FacesContext context, UIComponent component) {
        Integer columns = (Integer) get(context, component, "columns");

        if (columns == null) {
            columns = 3;
        }
        return columns;        
    }    

    private static boolean haveWeDisplayRequestedNumberOfRows(int numProcessed, 
    		                                                  int numRowsToDisplay) {
    	return (numRowsToDisplay > 0) && (++numProcessed > numRowsToDisplay);
    }
}
