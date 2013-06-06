package main;

import java.io.IOException;

import org.openmuc.framework.config.ArgumentSyntaxException;
import org.openmuc.framework.driver.spi.ConnectionException;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import driver.RestletDriver;

public class Main {
	public static void main(String[] args) throws ArgumentSyntaxException, ConnectionException, ResourceException, IOException {
		RestletDriver driver = new RestletDriver();
		
		driver.connect(null, "https://localhost:8443/", null, 15);
		ClientResource client = (ClientResource) driver.read(null, null, null, null, 15);
		
		System.out.println(client.get().getText());
	}
}
