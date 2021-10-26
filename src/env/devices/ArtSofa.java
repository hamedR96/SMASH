package devices;

import java.util.Map;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;

import com.github.underscore.lodash.U;

import cartago.*;
import utils.MQTT;

public class ArtSofa extends Artifact {

	private MQTT mqtt;
	private GuiSofa gui;

	void init(String name, String location, String mqttUrl, String topic, String msgSource) {
		this.init(name, location);
		
		String sofaLocation = "living-room"; // "sofa";

		if (!mqttUrl.isEmpty() && !topic.isEmpty()) {
			mqtt = new MQTT(mqttUrl, topic, (receivedTopic, receivedMsg) -> {
				byte[] payload = receivedMsg.getPayload();
				System.out.println("Received: " + receivedTopic + " " + new String(payload));
				// defineObsProperty("receivedMsg", new String(payload));
				// ObsProperty property = getObsProperty("receivedMsg");
				// property.updateValue(new String(payload));

				String jsonString = new String(payload);
				Map<String, Object> jsonObject = U.fromJsonMap(jsonString);

				String type = U.<String>get(jsonObject, "type");
				String source = U.<String>get(jsonObject, "source");

				// Long I = U.<Long>get(jsonObject, "timestamp");
				
				if (type.equalsIgnoreCase("location") && source.equalsIgnoreCase(msgSource)) {
					String ctxOp = U.<String>get(jsonObject, "subtype");
					String userName = U.<String>get(jsonObject, "data.label");
					String receivedLocation = U.<String>get(jsonObject, "data.value");

					System.out.println("LOCATION: " + ctxOp + " " + userName + " " + receivedLocation);

					if (ctxOp.equalsIgnoreCase("update") || ctxOp.equalsIgnoreCase("new")) {
						if (receivedLocation.equalsIgnoreCase(sofaLocation)) {
							this.sittingDown(userName);
						} else {
							this.standingUp(userName);
						}
					} else if (ctxOp.equalsIgnoreCase("delete") && receivedLocation.equalsIgnoreCase(sofaLocation)) {
						this.standingUp(userName);
					}
				}
			});
		}
	}
	
	void init(String name, String location) {
		gui = new GuiSofa(this, name);
		gui.setVisible(true);
		defineObsProperty("device", name);
		defineObsProperty("type", name, "Furniture");
		defineObsProperty("deviceLocation", name, location);
		defineObsProperty("sittingOn", "none", name);
		defineObsProperty("standingUp", "none", name);
		defineObsProperty("sensing", name, "none");
	}

	@OPERATION
	public void sittingDown(String name) {
		beginExtSession();
		defineObsProperty("person", name);
		getObsProperty("sittingOn").updateValue(0, name);
		getObsProperty("sensing").updateValue(1, name);
		signal("updateSMASH");
		endExtSession();
	}

	@OPERATION
	public void standingUp(String name) {
		beginExtSession();
		defineObsProperty("person", name);
		getObsProperty("sittingOn").updateValue(0, "none");
		getObsProperty("sensing").updateValue(1, "none");
		getObsProperty("standingUp").updateValue(0, name);
		signal("updateSMASH");
		endExtSession();
	}

}
