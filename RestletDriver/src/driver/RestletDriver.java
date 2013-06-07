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
import org.openmuc.framework.data.IntValue;
import org.openmuc.framework.data.Record;
import org.openmuc.framework.data.ValueType;
import org.openmuc.framework.driver.spi.ChannelRecordContainer;
import org.openmuc.framework.driver.spi.ChannelValueContainer;
import org.openmuc.framework.driver.spi.ConnectionException;
import org.openmuc.framework.driver.spi.DeviceConnection;
import org.openmuc.framework.driver.spi.DriverService;
import org.openmuc.framework.driver.spi.RecordsReceivedListener;
import org.restlet.Context;
import org.restlet.data.ChallengeScheme;
import org.restlet.resource.ClientResource;

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

		channelInfoList = new ArrayList<ChannelScanInformation>();

		ClientResource client = (ClientResource) deviceConnection
				.getConnectionHandle();
		client.addSegment("rest");
		client.addSegment("channel");

		JSONArray jsonArray = null;
		try {
			jsonArray = new JSONArray(client.get().getText());
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject json;
				String label;
				try {
					json = jsonArray.getJSONObject(i);
					label = json.get("label").toString();
					channelInfoList.add(new ChannelScanInformation(label,
							label, ValueType.BYTE_STRING, null));
				} catch (JSONException e) {
					System.out
							.println("scanForChannels : Forloop JSONArray Error.");
				}
			}
		} catch (JSONException e) {
			System.out
					.println("scanForChannels : Assign values to jsonArray Error.");
		} catch (IOException e) {
			System.out
					.println("scanForChannels : client.get().getText() Error.");
		}

		return channelInfoList;
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
		
		Record record = new Record(new IntValue(999), new Long(999L));
		String string = null;
		String result = null;
		String json = "{"+"timestamp : "+record.getTimestamp()+", flag : "+record.getFlag()+ ", value : "+record.getValue().toString()+"}";
		
		try {
			System.out.println(record);
			
			string = mapper.writeValueAsString(new JSONObject(json));
			
//			result = client
//					.put(new StringRepresentation(string,
//							MediaType.APPLICATION_JSON)).getText();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
}
