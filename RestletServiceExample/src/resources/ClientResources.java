package resources;

import helper.InsecureSslContextFactory;

import java.io.IOException;
import java.util.List;

import model.Channel;
import model.Record;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONException;
import org.restlet.Context;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;


public class ClientResources {

	public static void main(String[] args) throws ResourceException,
			IOException, JSONException {

		Context ctx = new Context();
		ctx.getAttributes().put("sslContextFactory",
				new InsecureSslContextFactory());

		ClientResource client = new ClientResource(ctx,
				"https://localhost:8443/rest/channel");

		ObjectMapper map = new ObjectMapper();

		List<Channel> listCh = map.readValue(client.get().getText(),
				new TypeReference<List<Channel>>() {
				});

		Record rec = null;
		System.out.println("---------------------------------------");
		for (Channel ch : listCh) {
			rec = ch.getRecord();
			System.out.println("Label: " + ch.getLabel());
			System.out.println("Timestamp: " + rec.getTimestamp());
			System.out.println("Flag: " + rec.getFlag());
			System.out.println("Value: " + rec.getValue());
			System.out.println("---------------------------------------");
		}

		ClientResource channelRoot = new ClientResource(
				"https://localhost:8443/rest/channel/channel2");
		// channelRoot.addSegment("/channel1");

		channelRoot.setChallengeResponse(ChallengeScheme.HTTP_BASIC, "admin",
				"admin");

		Record record = new Record();
		record.setValue(11);

		// Channel ch = listCh.get(0);
		// ch.setLabel("channel 99");

		String string = map.writeValueAsString(record);

		Representation rep = channelRoot.put(new StringRepresentation(string,
				MediaType.APPLICATION_JSON));

		System.out.println(rep.getText());

		// listCh.get(0).setRecord(map.readValue(rep.getStream(),
		// Record.class));
		channelRoot.release();
	}
}
