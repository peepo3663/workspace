package resources;

import java.io.IOException;

import model.Record;
import model.Channel;

import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

public class ChannelResources extends ServerResource {
	
	@Put
	public Form storeUser(Representation entity) throws ResourceException, IOException {
	    final Form form = new Form(entity);

	    Channel ch = new Channel();
	    Record rc = ch.getRecord();
	    
	    ch.setLabel(form.getFirstValue("ch2"));
//	    rc.setFlag(form.getFirstValue("flag"));
//	    
//	    getResponse().redirectSeeOther(getRequest().getResourceRef());
		System.out.println(form.getNames());
	    return form;
	}


}
