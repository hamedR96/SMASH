package devices;

import cartago.Artifact;
import cartago.OPERATION;
import cartago.ObsProperty;
import utils.REST;

public class ArtTV extends Artifact {
	private REST rest;
	private GuiTV gui;
	private String myself;

	void init(String name, String owner, String location, String status, String restUrl) {
		this.init(name, owner, location, status);
		this.rest = new REST(restUrl);
	}

	void init(String name, String owner, String location, String status) {
		gui = new GuiTV(this, name);
		this.myself = name;
		gui.setVisible(true);
		defineObsProperty("device", name);
		defineObsProperty("type", name, "TV");
		defineObsProperty("deviceLocation", name, location);
		defineObsProperty("deviceStatus", name, status);
		defineObsProperty("deviceOwner", name, owner);
		defineObsProperty("possibleTVStatus", "Off", "Standby", "Mute", "Unmute", "Playing");
	}

	public enum TvCmd {
		POWER("116"), MENU("139"),

		PLAY_PAUSE("164"), FORWARD("208"), REWIND("168"),

		MUTE("113"), VOL_P("115"), VOL_M("114"),

		CH_P("402"), CH_M("403"), CH0("512"), CH1("513"), CH2("514"), CH3("515"), CH4("516"), CH5("517"), CH6("518"),
		CH7("519"), CH8("520"), CH9("521");

		private final String text;

		TvCmd(final String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return text;
		}
		
		public String getFormattedRemoteControlCmd() {
			return "/remoteControl/cmd" + "?operation=01" + "&mode=0" + "&key=" + text;
		}
	}
	
	public String sendCmd(TvCmd cmd) {
		return this.rest.send(cmd.getFormattedRemoteControlCmd(), REST.RestAcceptFormat.JSON);
	}

	@OPERATION
	public void turnOnOff() {
		beginExtSession();
		if (!getObsProperty("deviceStatus").stringValue(1).equals("Off")) {
			signal("!!gl::turnOff", myself);
		} else {
			signal("!!gl::turnOn", myself);
		}
		
		this.sendCmd(TvCmd.POWER);
		endExtSession();
	}

	@OPERATION
	public void unmute() {
		beginExtSession();
		signal("!!gl::unmute", myself);
		
		if (!getObsProperty("deviceStatus").stringValue(1).equals("Mute")) {
			this.sendCmd(TvCmd.MUTE);
		}
		endExtSession();
	}

	@OPERATION
	public void mute() {
		beginExtSession();
		signal("!!gl::unmute", myself);
		
		if (!getObsProperty("deviceStatus").stringValue(1).equals("Mute")) {
			this.sendCmd(TvCmd.MUTE);
		}
		endExtSession();
	}

	@OPERATION
	public void loadMovie(String movie) {
		beginExtSession();
		signal("!!gl::play", myself, movie);
		endExtSession();
	}

	@OPERATION
	void setTVStatus(String deviceName, String newDeviceStatus) {
		beginExtSession();
		ObsProperty oldDeviceStatus = getObsProperty("deviceStatus");

		if ((newDeviceStatus.equals("Standby")) && (oldDeviceStatus.stringValue(1).equals("Off"))) {
			getObsProperty("deviceStatus").updateValue(1, "Standby");

		}
		if ((newDeviceStatus.equals("Playing")) && (oldDeviceStatus.stringValue(1).equals("Standby"))) {
			getObsProperty("deviceStatus").updateValue(1, "Playing");

		}
		if ((newDeviceStatus.equals("Off"))) {
			getObsProperty("deviceStatus").updateValue(1, "Off");

		}
		if ((newDeviceStatus.equals("Mute"))) {
			getObsProperty("deviceStatus").updateValue(1, "Mute");

		}
		if ((newDeviceStatus.equals("Unmute"))) {
			getObsProperty("deviceStatus").updateValue(1, "Playing");

		}
		endExtSession();
	}
}
