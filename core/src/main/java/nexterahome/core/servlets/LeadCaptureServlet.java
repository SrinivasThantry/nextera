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
		"sling.servlet.methods=" + HttpConstants.METHOD_POST, "sling.servlet.paths=/bin/leadcapture" })
public class LeadCaptureServlet extends SlingAllMethodsServlet {

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

	private static Logger log = LoggerFactory.getLogger(LeadCaptureServlet.class);

	@SuppressWarnings("deprecation")
	@Override
	protected void doPost(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {
		try {
			Gson gson = new Gson();
			
			UserPojo useobj = new UserPojo();
			useobj = getRequestJson(req, useobj);
			String payload = validateInaddPropertyCheck(useobj.getPayload());
			log.error(":: payload::"+payload);
			String service = "";
			if(useobj.getPhonenumber()!=null && useobj.getPhonenumbertype() != null)
				service = "joinus";
			else
				service = enrollNowService.getService(useobj.getZip(), req.getResourceResolver());
				

			if (true) {
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
					Node leadcaptureNode = null;
					
					if(service.equalsIgnoreCase("joinus"))
						leadcaptureNode = session.getNode("/content/usergenerated/nextera/joinus");
					else
						leadcaptureNode = session.getNode("/content/usergenerated/nextera/leadcapture");
						
					
					log.error("leadcaptureNode::" + leadcaptureNode);
				//	Node nexteraNode = res.adaptTo(Node.class);
					
					Node nexteracustNode = leadcaptureNode.addNode(useobj.getCustomerIdentifier() + "-data","nt:unstructured");
					log.error("nexteracustNode::" + nexteracustNode.getPath());
					nexteracustNode.setProperty("FirstName", useobj.getFirstName());
					nexteracustNode.setProperty("lastName", useobj.getLastName());
					nexteracustNode.setProperty("email", useobj.getEmail());
					nexteracustNode.setProperty("zip", useobj.getZip());
					if(service.equalsIgnoreCase("joinus")){
						nexteracustNode.setProperty("phonenumber", useobj.getPhonenumber());
						nexteracustNode.setProperty("phonenumbertype", useobj.getPhonenumbertype());
					}
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
			
			resobj.addProperty("service", service);
			log.error("Final res ::" + resobj);
			resp.getWriter().write(gson.toJson(resobj));
		} catch (Exception e) {
			log.error("exception while calling service::" + e);
		}
	}

	
	public static UserPojo getRequestJson(SlingHttpServletRequest request, UserPojo useobj) throws IOException {
		String requestJson = "";
		Gson gson = new Gson();
		String FirstName = request.getParameter("FirstName");
		String LastName = request.getParameter("LastName");
		String EmailAddress = request.getParameter("EmailAddress");
		String ZipCode = request.getParameter("zipCode");
		
		String phonenumber = request.getParameter("phonenumber");
		String phonenumbertype = request.getParameter("phonenumbertype");
		
		try {
			JsonObject customerDataJson = new JsonObject();
			long id = getCustomerIdentifier();
			customerDataJson.addProperty("CustomerIdentifier", id);
			useobj.setCustomerIdentifier(id);
			
			customerDataJson.addProperty("FirstName", FirstName);
			useobj.setFirstName(FirstName);
			customerDataJson.addProperty("LastName", LastName);
			useobj.setLastName(LastName);
			customerDataJson.addProperty("EmailAddress", EmailAddress);
			useobj.setEmail(EmailAddress);
			customerDataJson.addProperty("ZipCode", ZipCode);
			useobj.setZip(ZipCode);
			if(phonenumber!=null && phonenumbertype!=null){
				customerDataJson.addProperty("phonenumber", phonenumber);
				useobj.setPhonenumber(phonenumber);
				customerDataJson.addProperty("phonenumbertype", phonenumbertype);
				useobj.setPhonenumbertype(phonenumbertype);
			}
			requestJson = gson.toJson(customerDataJson);
			log.error(":::"+customerDataJson);
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
