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

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;


import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet that writes some sample content into the response. It is mounted for
 * all resources of a specific Sling resource type. The
 * {@link SlingSafeMethodsServlet} shall be used for HTTP methods that are
 * idempotent. For write operations use the {@link SlingAllMethodsServlet}.
 */
@Component(service=Servlet.class,
           property={
                   Constants.SERVICE_DESCRIPTION + "=Zipcode Validation",
                   "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                   "sling.servlet.paths=/bin/zipcodevalidation"
           })
public class ZipcodeValidationServlet extends SlingAllMethodsServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Reference
    private QueryBuilder builder;
	
    @Override
    protected void doGet(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) throws ServletException, IOException {
    	
    	String zipcode = req.getParameter("zipcode");
    	String service = "No Service";
    	Boolean  valid = false;
    	if(zipcode!=null && zipcode.length()>0) {
    	Boolean validNextEraZipcode = validZipcode(zipcode,"nextEra",req);
        valid = validNextEraZipcode;
        if(validNextEraZipcode) {
        	service = "nextEra";
        }
        if(!valid) {        
        	Boolean validFPLcode = validZipcode(zipcode,"FPL",req);
        	if(validFPLcode) {
        		service = "FPL";
        	}
        	valid = validFPLcode;
        }
        if(!valid) {        
        	Boolean validFPLcode = validZipcode(zipcode,"texas",req);
        	if(validFPLcode) {
        		service = "texas";
        	}
        	valid = validFPLcode;
        }
    	}
        resp.setContentType("application/json");
        resp.getWriter().write("{\"validZipcode\":"+valid+",\"service\":\""+service+"\"}");
    }
    
    @Override
    protected void doPost(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) throws ServletException, IOException {
    	doGet(req, resp);
    }
    
    private Boolean validZipcode(String zipcode,String service,SlingHttpServletRequest req) {
    	Boolean validZipcode = false;
    	
    	Map<String, String> map = new HashMap<String, String>();
    
    	map.put("path", "/etc/"+service+"/validZipcodes");
        map.put("property", "zipcode");
        map.put("property.value", zipcode); 
        
        Query query = builder.createQuery(PredicateGroup.create(map), req.getResourceResolver().adaptTo(Session.class));
        if(query.getResult().getHits().size()>0) {
        	validZipcode = true;
        }
    	return validZipcode;
    }
    
}
