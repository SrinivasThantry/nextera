package nexterahome.core.services.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import nexterahome.core.services.EnrollNowFormService;

@Component(service=EnrollNowFormService.class, property={
        Constants.SERVICE_DESCRIPTION + "= Enroll Now Form Service"
})
public class EnrollNowFormServiceImpl implements EnrollNowFormService{

	@org.osgi.service.component.annotations.Reference
	private ResourceResolverFactory resolverFactory;

	
	@Reference
    private QueryBuilder builder;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	
	@Override
	public String getService(String zipcode, ResourceResolver resourceResolver) {
		String service = "nextEra";
		if(zipcode!="") {
			Boolean validZipcode = validZipcode(zipcode,service,resourceResolver);
	        if(!validZipcode) {//nextEra - false
	        	service = "texas";
	        	validZipcode = validZipcode(zipcode,service, resourceResolver);
	        	if(!validZipcode) {
	        		service = "FPL";
	        		validZipcode = validZipcode(zipcode,service, resourceResolver);
	        		if(!validZipcode) 
	        		service = "No Service available";
	        	}
	        }
		}else {
			service = "No Service available";
		}
		
		return service;
	}

	
	private Boolean validZipcode(String zipcode,String service, ResourceResolver resourceResolver) {
    	Boolean validZipcode = false;
    	
    	Map<String, String> map = new HashMap<String, String>();
    
    	map.put("path", "/etc/"+service+"/validZipcodes");
        map.put("property", "zipcode");
        map.put("property.value", zipcode); 
        
        Query query = builder.createQuery(PredicateGroup.create(map), resourceResolver.adaptTo(Session.class));
        if(query.getResult().getHits().size()>0) {
        	validZipcode = true;
        }
        
    	return validZipcode;
    }
	
	@Override
	public String getAccess_token() {
    	String token = "";
    	
    	HttpClient client = new HttpClient();
    	
    	client.getHttpConnectionManager().getParams().setConnectionTimeout(3000);  
    	client.getHttpConnectionManager().getParams().setSoTimeout(2000); 
    	
    	GetMethod  getRequest = new GetMethod("https://api-atl.assurant.com/assurant/security/token");
    	getRequest.addRequestHeader("client_id", "0oa9uclyn3Ul5utMk1t7");
    	getRequest.addRequestHeader("client_secret", "7fKf809f6Z0GiW2GkyF2wsbGYrh9DeXyNKBbmqNT");
    	
    	  try {
			int status = client.executeMethod(getRequest);
			 if (status == 200) {
		            log.info("NextEraFPLZipcodeValidationServlet: getAccess_token "+getRequest.getResponseBodyAsString());
		            token = getRequest.getResponseBodyAsString();
		            try {
		            	JsonParser parser = new JsonParser();
		    			JsonObject tokenJson = parser.parse(token).getAsJsonObject();
						token = tokenJson.get("access_token").getAsString();
					} catch (Exception e) {
						log.info("NextEraFPLZipcodeValidationServlet: getAccess_token Error:"+e.getMessage());
					}
		        } else {
		        	log.info("NextEraFPLZipcodeValidationServlet: getAccess_token Error");
		            log.info("Unexcepted response code " + status + "; msg: " + getRequest.getResponseBodyAsString());
		        }
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	return token;
    }
	
	@Override
	public JsonObject postCustomerData(String token, String payload) {
		JsonObject obj = new JsonObject();
		Gson gson = new Gson();
    	HttpClient client = new HttpClient();
    	PostMethod postRequest = new PostMethod("https://api-atl.assurant.com/mpos-customer/api/v1/customers");
    	postRequest.addRequestHeader("Content-Type", "application/json");
    	postRequest.addRequestHeader("apim_access_token", token);
    	client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);  
    	client.getHttpConnectionManager().getParams().setSoTimeout(3000); 
    	try {
    		postRequest.setRequestEntity(new StringRequestEntity(payload, "application/json", "UTF-8"));
    		try {
				int status = client.executeMethod(postRequest);
				log.error("::int startus::"+status);
				if (status == 200) {
					String resobj = postRequest.getResponseBodyAsString();
					JsonObject succobj = new JsonParser().parse(resobj).getAsJsonObject();
					log.info("resobj: "+resobj);
					log.info("succobj: "+succobj);
					if(succobj.has("Success")){
					 obj.addProperty("successId", succobj.get("Success").getAsString());
					 obj.addProperty("message", "Success");
					 obj.addProperty("status", 200);
		            log.info("NextEraFPLZipcodeValidationServlet: response in 200 "+obj);
					}
				}else {
					String resobj = postRequest.getResponseBodyAsString();	
					log.info("resoerrbj: "+resobj);
					/*JSONObject objerror = new JSONObject(resobj);
					log.info("objerror: "+objerror);
					 if(objerror.has("error")){
						 log.info(":: error response: "+objerror.get("error"));
						 JSONArray newobj = (JSONArray) objerror.get("error");
						 log.info(":: newobj: "+newobj);
						 JSONObject ob = (JSONObject) newobj.get(0);
						 log.info(":: ob: "+ob);
						 obj.put("message", ob.get("userMessage"));
						 obj.put("status", ob.get("status"));
					 }*/
					 obj.addProperty("message", "Sorry, there is an internal  error");
					 obj.addProperty("status", 500);
					log.info("NextEraFPLZipcodeValidationServlet error response: "+obj);
				}
			} catch (IOException e) {
				log.info("NextEraFPLZipcodeValidationServlet : postCustomerData:"+e.getMessage());
				 obj.addProperty("message", "Sorry, there is an internal  error");
				 obj.addProperty("status", 500);
			}
			//postRequest.setRequestEntity(new StringRequestEntity(dataJsonString, "text/xml", "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			log.info("NextEraFPLZipcodeValidationServlet : postCustomerData:"+e.getMessage());
			 obj.addProperty("message", "Sorry, there is an internal  error");
			 obj.addProperty("status", 500);
		}finally {
			postRequest.releaseConnection();
		}
    	return obj;
    }
	
	

}
