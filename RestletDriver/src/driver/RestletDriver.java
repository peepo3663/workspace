package driver;

import helper.InsecureSslContextFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openmuc.framework.config.ArgumentSyntaxException;
import org.openmuc.framework.config.ChannelScanInformation;
import org.openmuc.framework.config.DeviceScanInformation;
import org.openmuc.framework.data.ValueType;
import org.openmuc.framework.driver.spi.ChannelRecordContainer;
import org.openmuc.framework.driver.spi.ChannelValueContainer;
import org.openmuc.framework.driver.spi.ConnectionException;
import org.openmuc.framework.driver.spi.DeviceConnection;
import org.openmuc.framework.driver.spi.DriverService;
import org.openmuc.framework.driver.spi.RecordsReceivedListener;
import org.restlet.Context;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;

import resources.MapToChannelInfoList;
import resources.Record;

public class RestletDriver implements DriverService {
	private final Context ctx = new Context();
	private List<ChannelScanInformation> channelInfoList;
	private ObjectMapper mapper;

	public RestletDriver() {
		ctx.getAttributes().put("sslContextFactory",
				new InsecureSslContextFactory());
		mapper = new ObjectMapper();
	}

	@Override
	public Object connect(String interfaceAddress, String deviceAddress,
			String settings, int timeout) throws ArgumentSyntaxException,
			ConnectionException {

		ClientResource client = new ClientResource(ctx, deviceAddress);
		return client;
	}

	@Override
	public void disconnect(DeviceConnection deviceConnection) {

	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object read(DeviceConnection deviceConnection,
			List<ChannelRecordContainer> container, Object obj, String arg3,
			int timout) throws UnsupportedOperationException,
			ConnectionException {

		String result = null;
		ChannelScanInformation channelInfo = null;
		if (obj instanceof ChannelScanInformation) {
			channelInfo = (ChannelScanInformation) obj;
		}
		ClientResource client = (ClientResource) deviceConnection
				.getConnectionHandle();
		
		client.addSegment("rest");
		client.addSegment("channel");
		client.addSegment(channelInfo.getChannelAddress());
		
		System.out.println(client);

		try {
			result = client.get().getText();
		} catch (IOException e) {
			System.out
					.println("scanForChannels : client.get().getText() Error.");
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public List<ChannelScanInformation> scanForChannels(
			DeviceConnection deviceConnection, int timeout)
			throws UnsupportedOperationException, ConnectionException {

		ClientResource client = (ClientResource) deviceConnection
				.getConnectionHandle();
		client.addSegment("rest");
		client.addSegment("channel");

		return  new MapToChannelInfoList().mapToChannelInfoList(client);
	}

	@Override
	public List<DeviceScanInformation> scanForDevices(String arg0, String arg1,
			String arg2, int arg3) throws UnsupportedOperationException,
			ArgumentSyntaxException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startListening(DeviceConnection arg0,
			List<ChannelRecordContainer> arg1, RecordsReceivedListener arg2)
			throws UnsupportedOperationException, ConnectionException {
		// TODO Auto-generated method stub

	}

	@Override
	public Object write(DeviceConnection deviceConnection,
			List<ChannelValueContainer> ValuelList, Object obj, int timeout)
			throws UnsupportedOperationException, ConnectionException {

		ChannelScanInformation channelInfo = null;
		if (obj instanceof ChannelScanInformation) {
			channelInfo = (ChannelScanInformation) obj;
		}
		ClientResource client = (ClientResource) deviceConnection
				.getConnectionHandle();
		client.addSegment("rest");
		client.addSegment("channel");
		client.addSegment(channelInfo.getChannelAddress());

		client.setChallengeResponse(ChallengeScheme.HTTP_BASIC, "admin",
				"admin");
		
		String string = null, result = null;
		Record record = new Record();
		record.setValue(999);
		
		try {
			string = mapper.writeValueAsString(record);
			
			result = client
					.put(new StringRepresentation(string,
							MediaType.APPLICATION_JSON)).getText();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	public DeviceConnection getDeviceConnection(String path) throws ArgumentSyntaxException, ConnectionException{
		
		Object newConnectionHandler = this.connect(null, path, null, 15);
		DeviceConnection newDeviceConnection = new DeviceConnection(null, null,
				null, newConnectionHandler);

		return newDeviceConnection;
	}
}
