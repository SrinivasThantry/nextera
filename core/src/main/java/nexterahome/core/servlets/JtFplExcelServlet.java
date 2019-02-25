/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package nexterahome.core.servlets;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import com.day.cq.commons.jcr.JcrUtil;


import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.ValueFormatException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Servlet that writes some sample content into the response. It is mounted for
 * all resources of a specific Sling resource type. The
 * {@link SlingSafeMethodsServlet} shall be used for HTTP methods that are
 * idempotent. For write operations use the {@link SlingAllMethodsServlet}.
 */
@Component(service=Servlet.class,
           property={
                   Constants.SERVICE_DESCRIPTION + "=Excel Sheet Upload",
                   "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                   "sling.servlet.paths=/bin/jtfplexcelsheet"
           })
public class JtFplExcelServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUid = 1L;
    
    private static final String JT_FPL_EXCEL_FILE_PAHT = "/content/dam/jt-fpl/JT_Zip_Codes_Nov21018.xlsx/jcr:content/renditions/original";
    
    
    private Session session;

    @Override
    protected void doGet(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) throws ServletException, IOException {
    	
    	String service = req.getParameter("service");
    	Resource resource = null; 
    	if(service.equalsIgnoreCase("jtfpl")) {
    		resource = req.getResourceResolver().getResource(JT_FPL_EXCEL_FILE_PAHT);
    	}
        
        Node node = resource.adaptTo(Node.class);
        String message = "Success";
        InputStream in = null;
        ArrayList<Long> zipcodes = new ArrayList<Long>();
        try {
        Node jcrContent = node.getNode("jcr:content");
        
       
		
			in = jcrContent.getProperty("jcr:data").getBinary().getStream();
		} catch (ValueFormatException e) {
			message = e.getMessage();
		} catch (PathNotFoundException e) {
			message = e.getMessage();
		} catch (RepositoryException e) {
			message = e.getMessage();
		}
        		
        XSSFWorkbook workbook = new XSSFWorkbook(in);
		XSSFSheet datatypeSheet = workbook.getSheetAt(0);
		Iterator<Row> iterator = datatypeSheet.iterator();
		

		Map<String, Map<String, String>> rowsMap = null;
		
		while (iterator.hasNext()) {
			Row currentRow = iterator.next();
			Cell cell = currentRow.getCell(0);
			double i = cell.getNumericCellValue();
			zipcodes.add((long) i);
		}
		message +=createZipcodeNodes(zipcodes,service, req);
        resp.setContentType("text/plain");
        resp.getWriter().write("Status = "+message);
    }
    
    @Override
    protected void doPost(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) throws ServletException, IOException {
    	doGet(req, resp);
    }
    
    private String createZipcodeNodes(ArrayList<Long> zipcodes, String service, SlingHttpServletRequest req) {
    	String message = "Successfully Created "+service+" zipcode nodes";
    	session = req.getResourceResolver().adaptTo(Session.class);
    	try {
			Node dataFolder = JcrUtil.createPath("/etc/jtfpl/validZipcodes", "sling:Folder", session);
			Node zipcodeNode = null;
			
			int count = 0;
			for(Long zipcode:zipcodes) {
				zipcodeNode = dataFolder.addNode(zipcode.toString(), "nt:unstructured");
				zipcodeNode.setProperty("zipcode", zipcode);
				count++;
				if(count%500==0) {
					session.save();
				}
			}
			
			session.save();
			
		} catch (RepositoryException e) {
			message = e.getMessage();
		}
    	
    	
    	return message;
    }
    
}
