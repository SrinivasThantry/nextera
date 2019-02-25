package nexterahome.core.models;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = { Resource.class })
public class ResourceCheckModel {
	private final Logger LOG = LoggerFactory.getLogger(getClass());

	@Self
	private Resource resource;

	private String size = "false";

	public ResourceCheckModel() {
	}
	
	@Override
	public String toString() {
		return "ResourceCheckModel [resource=" + resource + ", size=" + size + "]";
	}

	@PostConstruct
	protected void init() {

		try {
			String type = resource.getResourceType();
			String child = "";
			LOG.error("In the  **** INIT *** method" + resource.getResourceType());
			
			if(type.equals("nexterahome/components/content/headernavigation"))
				child = "navLinks";
			if(type.equals("nexterahome/components/content/howitworks") || type.equals("nexterahome/components/content/nexterahomeadvantage"))
				child = "iconTexts";
			if(type.equals("nexterahome/components/content/ourphilosophy"))
				child = "philosophies";
			Resource res = resource.getChild(child);
			if (res != null)
				this.size = "true";
		} catch (Exception e) {
			this.size = "false";
		}

	}

	public String getSize() {
        return this.size;
}
}