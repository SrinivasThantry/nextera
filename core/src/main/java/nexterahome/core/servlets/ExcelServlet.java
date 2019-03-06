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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                   "sling.servlet.paths=/bin/excelsheet"
           })
public class ExcelServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUid = 1L;
    
    private static final String NEXTERA_EXCEL_FILE_PAHT = "/content/dam/nextEra/nextEra-validzipcodes.xlsx/jcr:content/renditions/original";
    private static final String FPL_EXCEL_FILE_PAHT = "/content/dam/FPL/fpl-validzipcodes.xlsx/jcr:content/renditions/original";
    private static final String TEXAS_EXCEL_FILE_PAHT = "/content/dam/texas/texas-validzipcode.xlsx/jcr:content/renditions/original";
    
    private static Logger log = LoggerFactory.getLogger(ExcelServlet.class);
    private Session session;

    @Override
    protected void doGet(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) throws ServletException, IOException {
    	log.error("::in GET::");
    	String service = req.getParameter("service");
    	log.error("::in service::"+service);
    	Resource resource = null; 
    	if(service.equalsIgnoreCase("nextEra")) {
    		resource = req.getResourceResolver().getResource(NEXTERA_EXCEL_FILE_PAHT);
    	}if(service.equalsIgnoreCase("FPL")) {
    		resource = req.getResourceResolver().getResource(FPL_EXCEL_FILE_PAHT);
    	}
    	if(service.equalsIgnoreCase("texas")) {
    		resource = req.getResourceResolver().getResource(TEXAS_EXCEL_FILE_PAHT);
    	}
    	log.error("::in resource::"+resource);
        Node node = resource.adaptTo(Node.class);
        String message = "Success";
        InputStream in = null;
        ArrayList<Long> zipcodes = new ArrayList<Long>();
        try {
        Node jcrContent = node.getNode("jcr:content");
        
       
        log.error("::in jcrContent::"+jcrContent);
			in = jcrContent.getProperty("jcr:data").getBinary().getStream();
		} catch (ValueFormatException e) {
			log.error("::in ValueFormatException::"+e);
			message = e.getMessage();
		} catch (PathNotFoundException e) {
			log.error("::in PathNotFoundException::"+e);
			message = e.getMessage();
		} catch (RepositoryException e) {
			log.error("::in RepositoryException::"+e);
			message = e.getMessage();
		}
        		
        XSSFWorkbook workbook = new XSSFWorkbook(in);
        log.error("::in workbook::"+workbook);
		XSSFSheet datatypeSheet = workbook.getSheetAt(0);
		log.error("::in datatypeSheet::"+workbook);
		Iterator<Row> iterator = datatypeSheet.iterator();
		log.error("::in iterator::"+iterator);

		Map<String, Map<String, String>> rowsMap = null;
		
		while (iterator.hasNext()) {
			Row currentRow = iterator.next();
			Cell cell = currentRow.getCell(0);
			double i = cell.getNumericCellValue();
			log.error("::in zip ::"+i);
			zipcodes.add((long) i);
		}
		message +=createZipcodeNodes(zipcodes,service, req);
		log.error("::in message ::"+message);
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
    	log.error("::in session ::"+session);
    	try {
    		String serviceType = null;
    		if(service.equalsIgnoreCase("nextEra")) {
    			serviceType = "nextEra";
    		}else if(service.equalsIgnoreCase("FPL")) {
    			serviceType = "FPL";
    		}else if(service.equalsIgnoreCase("texas")) {
    			serviceType = "texas";
    		}
			Node dataFolder = JcrUtil.createPath("/etc/"+serviceType+"/validZipcodes", "sling:Folder", session);
			Node zipcodeNode = null;
			log.error("::in dataFolder ::"+dataFolder);
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
