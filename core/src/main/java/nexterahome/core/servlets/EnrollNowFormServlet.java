package nexterahome.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import nexterahome.core.services.EnrollNowFormService;

/**
 * Servlet that writes some sample content into the response. It is mounted for
 * all resources of a specific Sling resource type. The
 * {@link SlingSafeMethodsServlet} shall be used for HTTP methods that are
 * idempotent. For write operations use the {@link SlingAllMethodsServlet}.
 */
@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Nexter Enroll Now FormServlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_POST, "sling.servlet.paths=/bin/enrollnowformsubmit" })
public class EnrollNowFormServlet extends SlingAllMethodsServlet {

	@Reference
	private ResourceResolverFactory resolverFactory;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Pattern[] patterns = new Pattern[] {
			Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
			Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
			Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
			Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
			Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL) };

	@Reference
	private EnrollNowFormService enrollNowService;

	private static Logger log = LoggerFactory.getLogger(EnrollNowFormServlet.class);

	@SuppressWarnings("deprecation")
	@Override
	protected void doPost(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {
		try {
			Gson gson = new Gson();
			
			UserPojo useobj = new UserPojo();
			useobj = getRequestJson(req, useobj);
			String tokenString = enrollNowService.getAccess_token();
			String payload = validateInaddPropertyCheck(useobj.getPayload());
			log.error(":: payload::"+payload);
			JsonObject postCustomerData = enrollNowService.postCustomerData(tokenString, payload);
			String zipcode = getZipcode(payload);
			String service = enrollNowService.getService(zipcode, req.getResourceResolver());

			if (postCustomerData.get("message").equals("Success")) {
				// save to node
				try {

					log.error(":: inside node and file creation::");
					// Create a node that represents the root node
					// Get the root node
					Map <String, Object> param = new HashMap<String, Object>();
					param.put(ResourceResolverFactory.SUBSERVICE, "writeService");
					log.error("param::" + param);
					ResourceResolver rr = resolverFactory.getServiceResourceResolver(param);
					log.error("rr::" + rr);
					Session session = rr.adaptTo(Session.class);
					log.error("session::" + session);
					Node nexteraNode = session.getNode("/content/usergenerated/nextera");
					
					log.error("nexteraNode::" + nexteraNode);
				//	Node nexteraNode = res.adaptTo(Node.class);
					
					Node nexteracustNode = nexteraNode.addNode(useobj.getCustomerIdentifier() + "-nextera");
					log.error("nexteracustNode::" + nexteracustNode.getPath());
					nexteracustNode.setProperty("FirstName", useobj.getFirstName());
					nexteracustNode.setProperty("lastName", useobj.getLastName());
					nexteracustNode.setProperty("customerIdentifier", useobj.getCustomerIdentifier());
					nexteracustNode.setProperty("email", useobj.getEmail());
					nexteracustNode.setProperty("marketingOptIn", useobj.getMarketingOptIn() + "");
					nexteracustNode.setProperty("addressLine1", useobj.getAddressLine1());
					nexteracustNode.setProperty("addressLine2", useobj.getAddressLine2());
					nexteracustNode.setProperty("zip", useobj.getZip());
					nexteracustNode.setProperty("state", useobj.getState());
					nexteracustNode.setProperty("city", useobj.getCity());
					nexteracustNode.setProperty("coverageAddress", useobj.getCoverageAddress());
					nexteracustNode.setProperty("isMailingAddressSameasCoverageAddress",
							useobj.getIsMailingAddressSameasCoverageAddress() + "");
					nexteracustNode.setProperty("planName", useobj.getPlanName());
					nexteracustNode.setProperty("marketingProgramId", useobj.getMarketingProgramId());
					nexteracustNode.setProperty("deductible", useobj.getDeductible());
					nexteracustNode.setProperty("promoCode", useobj.getPromoCode());
					nexteracustNode.save();
					session.save();
				  	session.logout();
					log.error("Node saved");

					

				} catch (

				Exception e) {
					log.error("exception while saving to node::" + e);
				}
			}
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			JsonObject  resobj = new JsonObject();
			
			resobj.addProperty("postCustomerData", gson.toJson(postCustomerData));
			resobj.addProperty("service", service);
			log.error("Final res ::" + resobj);
			resp.getWriter().write(gson.toJson(resobj));
		} catch (Exception e) {
			log.error("exception while calling service::" + e);
			JsonObject resobj = new JsonObject();
			resobj.addProperty("postCustomerData", "error");
			resobj.addProperty("service", "error");
			log.error("Final res ::" + resobj);
			resp.getWriter().write(resobj.toString());
		}
	}

	public static String getZipcode(String payload) {
		String zipcode = "";
		try {
			Gson gson = new Gson();
			log.error("getZipcode res ::" );
			JsonElement jelem = gson.fromJson(payload, JsonElement.class);
			JsonObject payloadJson = jelem.getAsJsonObject();
			log.error("payloadJson res ::"+payloadJson );
			JsonObject coverageAddressJson = (JsonObject) payloadJson.get("CoverageAddress");
			log.error("coverageAddressJson res ::"+coverageAddressJson );
			zipcode = coverageAddressJson.get("ZipCode").getAsString();
			log.error("zipcode res ::"+zipcode );
		} catch (Exception e) {
			e.printStackTrace();
		}

		return zipcode;
	}

	public static UserPojo getRequestJson(SlingHttpServletRequest request, UserPojo useobj) throws IOException {
		String requestJson = "";
		Gson gson = new Gson();
		String CustomerIdentifier = request.getParameter("CustomerIdentifier");
		String FirstName = request.getParameter("FirstName");
		String LastName = request.getParameter("LastName");
		String ConfirmEmailAddress = request.getParameter("ConfirmEmailAddress");
		String EmailAddress = request.getParameter("EmailAddress");
		String AddressLine1 = request.getParameter("AddressLine1");
		String AddressLine2 = request.getParameter("AddressLine2");
		String ZipCode = request.getParameter("ZipCode");
		String State = request.getParameter("State");
		String City = request.getParameter("City");
		String IsMailingAddressSameasCoverageAddress = request.getParameter("IsMailingAddressSameasCoverageAddress");
		String PlanName = request.getParameter("PlanName");
		String MarketingProgramId = request.getParameter("MarketingProgramId");
		String Deductible = request.getParameter("Deductible");
		String PromoCode = request.getParameter("PromoCode");
		
		try {
			JsonObject customerDataJson = new JsonObject();
			log.error(":::"+customerDataJson);
			long id = getCustomerIdentifier();
			customerDataJson.addProperty("CustomerIdentifier", id);
			useobj.setCustomerIdentifier(id);
			customerDataJson.addProperty("FirstName", FirstName);
			useobj.setFirstName(FirstName);
			customerDataJson.addProperty("LastName", LastName);
			useobj.setLastName(LastName);
			customerDataJson.addProperty("EmailAddress", EmailAddress);
			useobj.setEmail(EmailAddress);
			customerDataJson.addProperty("ConfirmEmailAddress", ConfirmEmailAddress);
			customerDataJson.addProperty("MarketingOptIn", true);
			useobj.setMarketingOptIn(true);

			JsonObject coverageAddressJson = new JsonObject();
			coverageAddressJson.addProperty("AddressLine1", AddressLine1);
			useobj.setAddressLine1(AddressLine1);
			coverageAddressJson.addProperty("AddressLine2", AddressLine2);
			useobj.setAddressLine2(AddressLine2);
			coverageAddressJson.addProperty("ZipCode", ZipCode);
			useobj.setZip(ZipCode);
			coverageAddressJson.addProperty("City", City);
			useobj.setCity(City);
			coverageAddressJson.addProperty("State", State);
			useobj.setState(State);
		
			String temp = gson.toJson(coverageAddressJson);
			log.error("temp"+temp);
			customerDataJson.add("CoverageAddress", coverageAddressJson);
			customerDataJson.addProperty("MailingAddress", "");
			customerDataJson.addProperty("IsMailingAddressSameasCoverageAddress",
					Boolean.parseBoolean(IsMailingAddressSameasCoverageAddress));
			useobj.setMailingAddressSameasCoverageAddress(Boolean.parseBoolean(IsMailingAddressSameasCoverageAddress));
			JsonArray jsonArray = new JsonArray();
			JsonObject prodSelectionJson = new JsonObject();
			prodSelectionJson.addProperty("PlanName", PlanName);
			useobj.setPlanName(PlanName);
			prodSelectionJson.addProperty("MarketingProgramId", Integer.parseInt(MarketingProgramId));
			useobj.setMarketingProgramId(Long.parseLong(MarketingProgramId));
			prodSelectionJson.addProperty("Deductible", Integer.parseInt(Deductible));
			useobj.setDeductible(Long.parseLong(Deductible));
			prodSelectionJson.addProperty("PromoCode", PromoCode);
			useobj.setPromoCode(PromoCode);
			jsonArray.add(prodSelectionJson);
			log.error("::jsonArray:"+jsonArray);
			customerDataJson.add("ProductSelection", jsonArray);
			requestJson = gson.toJson(customerDataJson);
			log.error("::requestJson final:"+requestJson);
			useobj.setPayload(requestJson);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("exception while converting req obj::" + e);
		}

		return useobj;

	}
	private String getNullAsEmptyString(JsonElement jsonElement) {
        return jsonElement.isJsonNull() ? "" : jsonElement.getAsString();
    }
	
	private static long getCustomerIdentifier() {
		long custId = 0;

		custId = ThreadLocalRandom.current().nextLong(0, System.nanoTime());
		while (custId < 0.0) {

			custId = ThreadLocalRandom.current().nextLong();
			break;
		}

		return custId;
	}

	private static String validateInaddPropertyCheck(String value) {
		if (value != null) {

			value = value.replaceAll("\0", "");
			for (Pattern scriptPattern : patterns) {
				value = scriptPattern.matcher(value).replaceAll("");
			}
		}
		return value;
	}

}
