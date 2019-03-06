package nexterahome.core.services;

import org.apache.sling.api.resource.ResourceResolver;
import com.google.gson.JsonObject;
public interface EnrollNowFormService {
	//
	public String getService(String zipcode, ResourceResolver resourceResolver);
	
	public String getAccess_token();
	
	public JsonObject postCustomerData(String tokenString, String payload);
}
