package context;


import cartago.Artifact;


public class Context extends Artifact {
	
	private GuiContext gui;
	private String my_context = "Context";
	void init() {
		gui = new GuiContext(this,my_context);
	    gui.setVisible(true);
	}
	
	

	public void addperson(String Name, String PStatus, String WStatus) {
		signal("addPerson", Name, PStatus , WStatus);
		
	}


	public void addevent(String Name, String Type, String Date) {
		signal("addEvent", Name, Type , Date);
		
	}


	public void addphone(String Lable, String Owner, String Location, String Status, String Mode) {
		signal("addPhone", Lable, Owner , Location, Status, Mode);
		
	}


	public void addfurniture(String Name, String Location) {
		signal("addFurniture", Name , Location);
		
	}


	public void addtv(String Name, String Location, String Status) {
		signal("addTV", Name , Location,Status);
		
	}



	public void scenario() {
		signal("scenario");
		
	}


}
