package nexterahome.core.services;

import org.apache.sling.api.resource.ResourceResolver;
import org.json.JSONObject;

public interface EnrollNowFormService {
	
	public String getService(String zipcode, ResourceResolver resourceResolver);
	
	public String getAccess_token();
	
	public JSONObject postCustomerData(String tokenString, String payload);
}
