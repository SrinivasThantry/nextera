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
package nexterahome.core.schedulers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.ValueFactory;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * A simple demo for cron-job like tasks that get executed regularly. It also
 * demonstrates how property values can be set. Users can set the property
 * values in /system/console/configMgr
 */
@Designate(ocd = CreateBackupFileScheduledTask.Config.class)
@Component(service = Runnable.class)
public class CreateBackupFileScheduledTask implements Runnable {

	@ObjectClassDefinition(name = "Back Up File Cron Job for nextera home", description = "Backup CronJob for cron-job like task with properties")
	public static @interface Config {

		@AttributeDefinition(name = "Cron-job expression")
		String scheduler_expression() default "0 15 10 * * ? *";

		@AttributeDefinition(name = "Concurrent task", description = "Whether or not to schedule this task concurrently")
		boolean scheduler_concurrent() default false;

		@AttributeDefinition(name = "A parameter", description = "Can be configured in /system/console/configMgr")
		String myParameter() default "";
	}

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Reference
	private ResourceResolverFactory resourceResolverFactory;

	private String myParameter;

	@Override
	public void run() {
		try {
			logger.debug("SimpleScheduledTask is now running, myParameter='{}'", myParameter);
			Map<String, Object> param = new HashMap<String, Object>();
			param.put(ResourceResolverFactory.SUBSERVICE, "writeService");
			logger.error("param::" + param);
			ResourceResolver rr = resourceResolverFactory.getServiceResourceResolver(param);
			Session session = rr.adaptTo(Session.class);
			ValueFactory vf = session.getValueFactory();
			logger.error("session::" + session);
			Node nexteraNode = session.getNode("/content/usergenerated/nextera/enrollnow");

			logger.error("excel start saved");
			JsonObject obj = new JsonObject();
			int counter = 1;
			if (nexteraNode.hasNodes()) {
				JsonArray array = new JsonArray();
				boolean hasdata = false;
				javax.jcr.NodeIterator iter = nexteraNode.getNodes();
				while (iter.hasNext()) {
					try {
						Node childNode = (Node) iter.next();
						if (!childNode.getName().equalsIgnoreCase("customerData")) {
							
							String firstName = childNode.hasProperty("FirstName")
									? childNode.getProperty("FirstName").getString() : "";
							String lastName = childNode.hasProperty("lastName")
									? childNode.getProperty("lastName").getString() : "";
							String zip = childNode.hasProperty("zip") ? childNode.getProperty("zip").getString() : "";
							String email = childNode.hasProperty("email") ? childNode.getProperty("email").getString()
									: "";
							JsonObject jsonobj = new JsonObject();

							String id = childNode.hasProperty("customerIdentifier")
									? childNode.getProperty("customerIdentifier").getValue().getLong() + "" : "";

							String marketingOptIn = childNode.hasProperty("marketingOptIn")
									? childNode.getProperty("marketingOptIn").getValue().getBoolean() + "" : "";
							String addressLine1 = childNode.hasProperty("addressLine1")
									? childNode.getProperty("addressLine1").getString() : "";
							String addressLine2 = childNode.hasProperty("addressLine2")
									? childNode.getProperty("addressLine2").getString() : "";

							String state = childNode.hasProperty("state") ? childNode.getProperty("state").getString()
									: "";
							String city = childNode.hasProperty("city") ? childNode.getProperty("city").getString()
									: "";
							String coverageAddress = childNode.hasProperty("coverageAddress")
									? childNode.getProperty("coverageAddress").getString() : "";
							String isMailingAddressSameasCoverageAddress = childNode
									.hasProperty("isMailingAddressSameasCoverageAddress")
											? childNode.getProperty("isMailingAddressSameasCoverageAddress").getString()
													+ ""
											: "";
							String planName = childNode.hasProperty("planName")
									? childNode.getProperty("planName").getString() : "";
							String marketId = childNode.hasProperty("marketingProgramId")
									? childNode.getProperty("marketingProgramId").getValue().getLong() + "" : "";
							String deductiblestr = childNode.hasProperty("deductible")
									? childNode.getProperty("deductible").getValue().getLong() + "" : "";
							String promoCode = childNode.hasProperty("promoCode")
									? childNode.getProperty("promoCode").getString() : "";

							String phonenumber = childNode.hasProperty("phonenumber")
									? childNode.getProperty("phonenumber").getString() : "";

							String phonenumbertype = childNode.hasProperty("phonenumbertype")
									? childNode.getProperty("phonenumbertype").getString() : "";

							// ---------------------------------------------
							jsonobj.addProperty("firstName", firstName);
							jsonobj.addProperty("lastName", lastName);

							jsonobj.addProperty("email", email);

							jsonobj.addProperty("zip", zip);

							counter = counter + 1;

							jsonobj.addProperty("phonenumber", phonenumber);
							jsonobj.addProperty("phonenumbertype", phonenumbertype);
							jsonobj.addProperty("marketingOptIn", marketingOptIn);
							jsonobj.addProperty("addressLine1", addressLine1);
							jsonobj.addProperty("addressLine2", addressLine2);
							jsonobj.addProperty("coverageAddress", coverageAddress);
							jsonobj.addProperty("isMailingAddressSameasCoverageAddress",
									isMailingAddressSameasCoverageAddress);
							jsonobj.addProperty("planName", planName);
							jsonobj.addProperty("marketId", marketId);
							jsonobj.addProperty("deductiblestr", deductiblestr);
							jsonobj.addProperty("promoCode", promoCode);
							jsonobj.addProperty("id", id);
							jsonobj.addProperty("state", state);
							jsonobj.addProperty("city", city);
							array.add(jsonobj);
							hasdata=true;
						}
					} catch (Exception e) {
						logger.error("ex itr nodes" + e);
					}
					if(hasdata)
					obj.add("data", array);

				}
			}
			// for lead capture

			Node leadcapNode = nexteraNode.getParent().getNode("leadcapture");
			logger.error("leadcapNoder::" + leadcapNode);
			if (leadcapNode.hasNodes()) {
				javax.jcr.NodeIterator iter = leadcapNode.getNodes();
				JsonArray array2 = new JsonArray();
				boolean hasdata = false;
				while (iter.hasNext()) {
					try {

						Node childNode1 = (Node) iter.next();
						if (!childNode1.getName().equalsIgnoreCase("customerData")) {
						logger.error("childNode1::" + childNode1);
						String firstName1 = childNode1.hasProperty("FirstName")
								? childNode1.getProperty("FirstName").getString() : "";
						String lastName1 = childNode1.hasProperty("lastName")
								? childNode1.getProperty("lastName").getString() : "";
						String zip1 = childNode1.hasProperty("zip") ? childNode1.getProperty("zip").getString() : "";
						String email1 = childNode1.hasProperty("email") ? childNode1.getProperty("email").getString()
								: "";
						JsonObject jsonobj = new JsonObject();
						jsonobj.addProperty("firstName", firstName1);
						jsonobj.addProperty("lastName", lastName1);

						jsonobj.addProperty("email", email1);

						jsonobj.addProperty("zip", zip1);

						counter = counter + 1;
						array2.add(jsonobj);
						hasdata=true;
						}
					} catch (Exception e) {
						logger.error("::exception in lead capture::"+e);
					}
					if(hasdata)
					obj.add("customerdata", array2);
				}

			}
			
			// for join us

						Node joinusNode = nexteraNode.getParent().getNode("joinus");
						logger.error("joinus::" + joinusNode);
						if (joinusNode.hasNodes()) {
							javax.jcr.NodeIterator iter = joinusNode.getNodes();
							JsonArray array2 = new JsonArray();
							boolean hasdata = false;
							while (iter.hasNext()) {
								try {

									Node childNode1 = (Node) iter.next();
									if (!childNode1.getName().equalsIgnoreCase("customerData")) {
									logger.error("childNode1::" + childNode1);
									String firstName1 = childNode1.hasProperty("FirstName")
											? childNode1.getProperty("FirstName").getString() : "";
									String lastName1 = childNode1.hasProperty("lastName")
											? childNode1.getProperty("lastName").getString() : "";
									String zip1 = childNode1.hasProperty("zip") ? childNode1.getProperty("zip").getString() : "";
									String email1 = childNode1.hasProperty("email") ? childNode1.getProperty("email").getString() : "";
									String phonenumber = childNode1.hasProperty("phonenumber") ? childNode1.getProperty("phonenumber").getString() : "";
									String phonenumbertype = childNode1.hasProperty("phonenumbertype") ? childNode1.getProperty("phonenumbertype").getString() : "";
									JsonObject jsonobj = new JsonObject();
									jsonobj.addProperty("firstName", firstName1);
									jsonobj.addProperty("lastName", lastName1);

									jsonobj.addProperty("email", email1);

									jsonobj.addProperty("zip", zip1);
									jsonobj.addProperty("phonenumber", phonenumber);
									jsonobj.addProperty("phonenumbertype", phonenumbertype);

									counter = counter + 1;
									array2.add(jsonobj);
									hasdata=true;
									}
								} catch (Exception e) {
									logger.error("::exception in lead capture::"+e);
								}
								if(hasdata)
								obj.add("joinus", array2);
							}

						}
			
			
			
			// create file in AEM
			if(obj.has("data"))				
				createFile(obj.get("data").toString(), nexteraNode, vf, "enrollnowdata.json");
			
			// create file for lead capture
			if(obj.has("customerdata"))
				createFile(obj.get("customerdata").toString(), leadcapNode, vf,"leadcapturedata.json");
			
			if(obj.has("joinus"))
				createFile(obj.get("joinus").toString(), joinusNode, vf,"joinus.json");
				
			session.save();
			session.logout();

		} catch (Exception e) {
			logger.error("ex in schedular::" + e);
		}
	}
	
	private void createFile(String obj, Node nexteraNode, ValueFactory vf, String filename) throws RepositoryException{
		
		InputStream is = new ByteArrayInputStream(obj.getBytes(Charset.forName("UTF-8")));
		logger.error("is input stream:" + obj.toString());

		Binary contentValue = vf.createBinary(is);
		logger.error("contentValue:" + contentValue);
		if (nexteraNode.hasNode("customerData")) {
			Node removeNode = nexteraNode.getNode("customerData");
			removeNode.remove();

		}
		Node node = nexteraNode.addNode("customerData");
		Node fileNode = node.addNode(filename, "nt:file");

		fileNode.addMixin("mix:referenceable");

		Node reNode = fileNode.addNode("jcr:content", "nt:resource");

		reNode.setProperty("jcr:mimeType", "text/json");

		reNode.setProperty("jcr:data", contentValue);

		Calendar lstModified = Calendar.getInstance();
		lstModified.setTimeInMillis(lstModified.getTimeInMillis());
		reNode.setProperty("jcr:lastModified", lstModified);
		logger.error("reNode save:" + reNode);
		
	}

	@Activate
	protected void activate(final Config config) {
		myParameter = config.myParameter();
	}

}
