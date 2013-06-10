package domain;

import org.openmuc.framework.config.ChannelScanInformation;

public class Validate {
	public boolean isChannelInfo(Object obj) {
		if (obj instanceof ChannelScanInformation) {
			return true;
		}
		return false;
	}
}
