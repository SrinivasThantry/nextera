package nexterahome.core.servlets;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.ValueFactory;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.jackrabbit.oak.commons.json.JsonObject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

			UserPojo useobj = new UserPojo();
			useobj = getRequestJson(req, useobj);
			String tokenString = enrollNowService.getAccess_token();
			String payload = validateInputCheck(useobj.getPayload());
			JSONObject postCustomerData = enrollNowService.postCustomerData(tokenString, payload);
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
			JSONObject resobj = new JSONObject();
			resobj.put("postCustomerData", postCustomerData);
			resobj.put("service", service);
			log.error("Final res ::" + resobj);
			resp.getWriter().write(resobj.toString());
		} catch (Exception e) {
			log.error("exception while calling service::" + e);
			JSONObject resobj = new JSONObject();
			resobj.put("postCustomerData", "error");
			resobj.put("service", "error");
			log.error("Final res ::" + resobj);
			resp.getWriter().write(resobj.toString());
		}
	}

	public static String getZipcode(String payload) {
		String zipcode = "";
		try {
			JSONObject payloadJson = new JSONObject(payload);
			JSONObject coverageAddressJson = payloadJson.getJSONObject("CoverageAddress");
			zipcode = coverageAddressJson.getString("ZipCode");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return zipcode;
	}

	public static UserPojo getRequestJson(SlingHttpServletRequest request, UserPojo useobj) throws IOException {
		String requestJson = "";

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
		log.error(":::"+request.getParameterMap().toString());
		try {
			JSONObject customerDataJson = new JSONObject();
			long id = getCustomerIdentifier();
			customerDataJson.put("CustomerIdentifier", id);
			useobj.setCustomerIdentifier(id);
			customerDataJson.put("FirstName", FirstName);
			useobj.setFirstName(FirstName);
			customerDataJson.put("LastName", LastName);
			useobj.setLastName(LastName);
			customerDataJson.put("EmailAddress", EmailAddress);
			useobj.setEmail(EmailAddress);
			customerDataJson.put("ConfirmEmailAddress", ConfirmEmailAddress);
			customerDataJson.put("MarketingOptIn", true);
			useobj.setMarketingOptIn(true);

			JSONObject coverageAddressJson = new JSONObject();
			coverageAddressJson.put("AddressLine1", AddressLine1);
			useobj.setAddressLine1(AddressLine1);
			coverageAddressJson.put("AddressLine2", AddressLine2);
			useobj.setAddressLine2(AddressLine2);
			coverageAddressJson.put("ZipCode", ZipCode);
			useobj.setZip(ZipCode);
			coverageAddressJson.put("City", City);
			useobj.setCity(City);
			coverageAddressJson.put("State", State);
			useobj.setState(State);
			customerDataJson.put("CoverageAddress", coverageAddressJson);
			customerDataJson.put("MailingAddress", "");
			customerDataJson.put("IsMailingAddressSameasCoverageAddress",
					Boolean.parseBoolean(IsMailingAddressSameasCoverageAddress));
			useobj.setMailingAddressSameasCoverageAddress(Boolean.parseBoolean(IsMailingAddressSameasCoverageAddress));
			JSONArray jsonArray = new JSONArray();
			JSONObject prodSelectionJson = new JSONObject();
			prodSelectionJson.put("PlanName", PlanName);
			useobj.setPlanName(PlanName);
			prodSelectionJson.put("MarketingProgramId", Integer.parseInt(MarketingProgramId));
			useobj.setMarketingProgramId(Long.parseLong(MarketingProgramId));
			prodSelectionJson.put("Deductible", Integer.parseInt(Deductible));
			useobj.setDeductible(Long.parseLong(Deductible));
			prodSelectionJson.put("PromoCode", PromoCode);
			useobj.setPromoCode(PromoCode);
			jsonArray.put(prodSelectionJson);
			customerDataJson.put("ProductSelection", jsonArray);
			requestJson = customerDataJson.toString();
			useobj.setPayload(requestJson);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("exception while converting req obj::" + e);
		}

		return useobj;

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

	private static String validateInputCheck(String value) {
		if (value != null) {

			value = value.replaceAll("\0", "");
			for (Pattern scriptPattern : patterns) {
				value = scriptPattern.matcher(value).replaceAll("");
			}
		}
		return value;
	}

}
