package driver;

import helper.InsecureSslContextFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

public class RestletDriver implements DriverService {
	private final Context ctx = new Context();
	private List<ChannelScanInformation> channelInfoList;

	public RestletDriver() {
		ctx.getAttributes().put("sslContextFactory",
				new InsecureSslContextFactory());
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
			List<ChannelRecordContainer> container, Object arg2,
			String channelAddress, int timout)
			throws UnsupportedOperationException, ConnectionException {

		String result = null;
		ClientResource client = (ClientResource) deviceConnection.getConnectionHandle();
		client.addSegment("rest");
		client.addSegment("channel");
		client.addSegment(channelAddress);

		try {
			result = client.get().getText();
		} catch (ResourceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public List<ChannelScanInformation> scanForChannels(
			DeviceConnection deviceConnection, int timeout)
			throws UnsupportedOperationException, ConnectionException {

		channelInfoList = new ArrayList<ChannelScanInformation>();

		ClientResource client = (ClientResource) deviceConnection.getConnectionHandle();
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
			List<ChannelValueContainer> ValuelList, Object arg2, int timeout)
			throws UnsupportedOperationException, ConnectionException {

		StringBuffer strBuffer = new StringBuffer();
		for (ChannelValueContainer cvc : ValuelList) {
			ClientResource client = (ClientResource) deviceConnection.getConnectionHandle();
			client.addSegment("rest");
			client.addSegment("channel");
			client.addSegment(cvc.getChannelAddress());

			try {
				strBuffer.append(client.put(cvc.getValue()));
			} catch (ResourceException e) {
				e.printStackTrace();
			}
		}

		return strBuffer;
	}
}
