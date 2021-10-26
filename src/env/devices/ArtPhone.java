package devices;

import cartago.Artifact;
import cartago.OPERATION;
import cartago.ObsProperty;

public class ArtPhone extends Artifact {
	
	private GuiPhone gui;
	public String myself;

	void init(String name, String owner, String location, String status, String mode) {
		gui = new GuiPhone(this, name);
		myself =name;
	    gui.setVisible(true);
	    defineObsProperty("device", name);
	    defineObsProperty("callerType", "none", "none");
	    defineObsProperty("type", name, "Phone");
	    defineObsProperty("deviceOwner",name, owner);
	    defineObsProperty("deviceLocation",name, location);
	    defineObsProperty("deviceStatus", name, status);
	    defineObsProperty("deviceMode", name, mode);
	    defineObsProperty("incomingCall", name, "none"); 
	    defineObsProperty("outcomingCall", "none", name); 
	    defineObsProperty("possiblePhoneStatus", "Standby","Off","Busy","Voicemail","Mute","Unmute","Ringing","ReceivingCall");
	}
	
	@OPERATION 
	public void request(String object , String title, String type) {
		if (type=="Channel") {
		signal("loadChannel", object , title ,myself);
		ArtTV tv;
		tv= new ArtTV();
		tv.loadMovie(title);
	}
	}

	@OPERATION 
	public void receivingCall(String personName, String type) {
		getObsProperty("deviceStatus").updateValue(1,"ReceivingCall");
		getObsProperty("incomingCall").updateValue(1,personName);
		getObsProperty("callerType").updateValue(0,personName);
		getObsProperty("callerType").updateValue(1,type);
		signal("updateSMASH");
	}
	@OPERATION 
	public void calling(String Name, String Number) {
		getObsProperty("deviceStatus").updateValue(1,"Calling");
		getObsProperty("outcomingCall").updateValue(0,Name);
		signal("updateSMASH");
	}
	@OPERATION 
	public void answeringCall() {
		if (getObsProperty("deviceStatus").stringValue(1).equals("Ringing"))
		{
			getObsProperty("deviceStatus").updateValue(1,"Busy");
			signal("updateSMASH");
		}
	}
	@OPERATION 
	public void rejectingCall() {
			getObsProperty("deviceStatus").updateValue(1,"Standby");
			signal("updateSMASH");

	}
	@OPERATION 
	public void voicemail() {
		if (getObsProperty("deviceStatus").stringValue(1).equals("ReceivingCall"))
		{
			getObsProperty("deviceStatus").updateValue(1,"Voicemail");
			signal("updateSMASH");
		}
	}
	@OPERATION 
	public void settingMode(String Mode) {
		getObsProperty("deviceSetMode").updateValue(1,Mode);
		signal("updateSMASH");
		
	}
	@OPERATION 	
	void setPhoneStatus (String deviceName, String newDeviceStatus) {	
		
		ObsProperty oldDeviceStatus = getObsProperty("deviceStatus");
		
		if (newDeviceStatus.equals("Voicemail")	&&  (oldDeviceStatus.stringValue(1).equals("ReceivingCall") || oldDeviceStatus.stringValue(1).equals("Ringing")))
		{
			getObsProperty("deviceStatus").updateValue(1,"Voicemail"); //directly to voicemail
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			getObsProperty("deviceStatus").updateValue(1,"Standby");
		};
		
		if ((newDeviceStatus.equals("Ringing"))	&&  (oldDeviceStatus.stringValue(1).equals("ReceivingCall")))
		{
			getObsProperty("deviceStatus").updateValue(1,"Ringing");  //ringing
			
		};
		
		if ((newDeviceStatus.equals("Busy"))	&&  (oldDeviceStatus.stringValue(1).equals("Ringing")))
		{
			getObsProperty("deviceStatus").updateValue(1,"Busy");  //accept call
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			getObsProperty("deviceStatus").updateValue(1,"Standby");
			
		};
		
		if ((newDeviceStatus.equals("Standby"))	&&  (oldDeviceStatus.stringValue(1).equals("Ringing")))
		{
			getObsProperty("deviceStatus").updateValue(1,"Standby"); //reject call
			
		};		
		if ((newDeviceStatus.equals("Off")))
		{
			getObsProperty("deviceStatus").updateValue(1,"Off");	
		};
		
		if ((newDeviceStatus.equals("Mute")))
		{
			getObsProperty("deviceStatus").updateValue(1,"Mute");
			
		};
		
		if ((newDeviceStatus.equals("Unmute")))
		{
			getObsProperty("deviceStatus").updateValue(1,"Unmute");
			
		};
		}
}
