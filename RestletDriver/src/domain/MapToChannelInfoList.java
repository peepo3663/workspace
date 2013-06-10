package domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openmuc.framework.config.ChannelScanInformation;
import org.openmuc.framework.data.ValueType;
import org.restlet.resource.ClientResource;

public class MapToChannelInfoList {

	public List<ChannelScanInformation> mapToChannelInfoList(ClientResource client) {
		
		JSONArray jsonArray = null;
		List<ChannelScanInformation> channelInfoList = new ArrayList<ChannelScanInformation>();
		
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
}
