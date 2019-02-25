package nexterahome.core.models;

import org.apache.sling.api.resource.Resource;

import com.adobe.cq.sightly.WCMUsePojo;

public class ServiceproviderCheck extends WCMUsePojo {

	private String size = "false";

	@Override
	public void activate() throws Exception {

		String child = get("child", String.class);
		Resource childResource = getResource().getChild(child);
		if (childResource != null)
			this.size = "true";
	}

	public String getSize() {
		return this.size;
	}
}