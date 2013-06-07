package main;

import java.io.IOException;
import java.util.List;

import org.openmuc.framework.config.ArgumentSyntaxException;
import org.openmuc.framework.config.ChannelScanInformation;
import org.openmuc.framework.driver.spi.ConnectionException;
import org.openmuc.framework.driver.spi.DeviceConnection;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import driver.RestletDriver;

/**
 * @author students
 * 
 */
public class Main {
	private static final String path = "https://localhost:8443/";
	
	public static void main(String[] args) throws ArgumentSyntaxException,
			ConnectionException, ResourceException, IOException {

		RestletDriver driver = new RestletDriver();

		Object connectionHandler = driver.connect(null, path, null, 15);

		DeviceConnection deviceConnection = new DeviceConnection(null, null,
				null, connectionHandler);
		
		//driver.scanForChannels(deviceConnection, 15);
		List<ChannelScanInformation> listChannelInfo = driver.scanForChannels(
				deviceConnection, 15);
		
		for (ChannelScanInformation channelInfo : listChannelInfo) {
			
			System.out.println(channelInfo.getChannelAddress());
			
			Object newConnectionHandler = driver.connect(null, path, null, 15);
			DeviceConnection newDeviceConnection = new DeviceConnection(null, null,
					null, newConnectionHandler);

			//driver.read(deviceConnection2, null, null, channelInfo.getChannelAddress(), 15);
			//System.out.println(driver.read(newDeviceConnection, null, channelInfo, null, 15));
			
			System.out.println(driver.write(newDeviceConnection, null, channelInfo, 15));
		}

	}
}
