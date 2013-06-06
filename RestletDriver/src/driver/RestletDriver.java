package driver;

import helper.InsecureSslContextFactory;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.openmuc.framework.config.ArgumentSyntaxException;
import org.openmuc.framework.config.ChannelScanInformation;
import org.openmuc.framework.config.DeviceScanInformation;
import org.openmuc.framework.driver.spi.ChannelRecordContainer;
import org.openmuc.framework.driver.spi.ChannelValueContainer;
import org.openmuc.framework.driver.spi.ConnectionException;
import org.openmuc.framework.driver.spi.DeviceConnection;
import org.openmuc.framework.driver.spi.DriverService;
import org.openmuc.framework.driver.spi.RecordsReceivedListener;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

public class RestletDriver implements DriverService {
	private ClientResource client;
	//private final String path = "https://localhost:8443/";
	private final Context ctx = new Context();
	private ObjectMapper mapper;
	private String deviceAddress;

	public RestletDriver() {
		ctx.getAttributes().put("sslContextFactory",
				new InsecureSslContextFactory());

		mapper = new ObjectMapper();
	}

	@Override
	public Object connect(String interfaceAddress, String deviceAddress, String settings, int timeout)
			throws ArgumentSyntaxException, ConnectionException {
		
		this.deviceAddress = deviceAddress;
		client = new ClientResource(ctx, deviceAddress);
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
	public Object read(DeviceConnection deviceConnection, List<ChannelRecordContainer> list,
			Object arg2, String arg3, int timout)
			throws UnsupportedOperationException, ConnectionException {

		//client = (ClientResource) deviceConnection.getConnectionHandle();
		client.addSegment("rest");
		client.addSegment("channel");
		client.addSegment("channel1");

		return client;
	}

	@Override
	public List<ChannelScanInformation> scanForChannels(DeviceConnection deviceConnection,
			int arg1) throws UnsupportedOperationException, ConnectionException {

		// device.getDeviceAddress() should be
		// "https://localhost:8443/rest/channel/"
		client = (ClientResource)deviceConnection.getConnectionHandle();
		client.addSegment("rest");
		client.addSegment("channel");

		List<ChannelScanInformation> listChannel = null;
		try {
			listChannel = mapper.readValue(client.get().getText(),
					new TypeReference<List<ChannelScanInformation>>() {
					});
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (ResourceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return listChannel;
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
			List<ChannelValueContainer> list, Object arg2, int timeout)
			throws UnsupportedOperationException, ConnectionException {
		client = (ClientResource)deviceConnection.getConnectionHandle();
		client.addSegment("rest");
		client.addSegment("channel");
		client.addSegment(list.get(0).getChannelAddress());
		
		list.get(0).getChannelHandle();
		String string = null;
		try {
			string = mapper.writeValueAsString(list.get(0).getChannelHandle());
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return client.put(new StringRepresentation(string,
				MediaType.APPLICATION_JSON));
	}
}
