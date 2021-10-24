package context;

import java.awt.Color;

import javax.swing.*; 


@SuppressWarnings("serial")
public class GuiContext extends JFrame {

	  private static final int width = 1100;
	  private static final int height = 400;
	  protected JPanel mainPanel;
	  public JButton addPerson, addEvent, addPhone, addFurniture, addTV,scenario;

	  @SuppressWarnings({ "rawtypes", "unchecked" })
	public GuiContext(Context context, String title){
		this.mainPanel = new JPanel();
		setTitle("Context .:: "+title+" ::.");
		setSize(width,height);
	    setContentPane(mainPanel);
	    mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS)); 
	    
	     
        JPanel panel1 = new JPanel();
	    
        panel1.add(new JLabel("Name"));
	    JTextField PersonName = new JTextField(10);
	    PersonName.setEditable(true);
	    PersonName.setText("");
	    panel1.add(PersonName);
	    panel1.add(new JLabel("Personal Status:"));
	   
	    String[] option1_Name = { "Hangingout","Alone","OnDate" }; 
        JComboBox box1_Name = new JComboBox(option1_Name); 
        panel1.add(box1_Name);
        
        panel1.add(new JLabel("Working State:"));
        
        String[] option2_Name = {"OffDuty","OnDuty","InMeeting"};  
        JComboBox box2_Name = new JComboBox(option2_Name);   
        panel1.add(box2_Name);         
        
        addPerson = new JButton("Add Person"); 
        addPerson.addActionListener(e -> {
	    	context.beginExtSession();
	    	context.addperson(PersonName.getText(),String.valueOf(box1_Name.getSelectedItem()),String.valueOf(box2_Name.getSelectedItem()));
	    	context.endExtSession();
	    });
        panel1.add(addPerson); 
        
        mainPanel.add(panel1); 
	    
        
        JPanel panel2 = new JPanel();
	    
        panel2.add(new JLabel("User"));
	    JTextField UserName_event = new JTextField(10);
	    UserName_event.setEditable(true);
	    UserName_event.setText("");
	    panel2.add(UserName_event);
	    panel2.add(new JLabel("Type:"));
	   
	    String[] option1_Event = { "Meeting","Presentation"}; 
        JComboBox box1_Event = new JComboBox(option1_Event); 
        panel2.add(box1_Event);
        
        panel2.add(new JLabel("Date:"));
        
        String[] option2_Event = {"Tomorrow","NextWeek"};  
        JComboBox box2_Event = new JComboBox(option2_Event);   
        panel2.add(box2_Event);         
        
        addEvent = new JButton("Add Event"); 
        addEvent.addActionListener(e -> {
	    	context.beginExtSession();
	    	context.addevent(UserName_event.getText(),String.valueOf(box1_Event.getSelectedItem()),String.valueOf(box2_Event.getSelectedItem()));
	    	context.endExtSession();
	    });
        panel2.add(addEvent); 
        
        mainPanel.add(panel2); 
        
       
        
        JPanel panel3 = new JPanel();
	    
        panel3.add(new JLabel("Device Name"));
	    JTextField DeviceName = new JTextField(10);
	    DeviceName.setEditable(true);
	    DeviceName.setText("");
	    panel3.add(DeviceName);
	    
	    panel3.add(new JLabel("Device Owner:"));
	    JTextField DeviceOwner = new JTextField(10);
	    DeviceOwner.setEditable(true);
	    DeviceOwner.setText("");
	    panel3.add(DeviceOwner);
	    
	    panel3.add(new JLabel("Device Location:"));
	   
	    String[] option1_Phone = { "LivingRoom","Kitchen","BedRoom"}; 
        JComboBox box1_Phone = new JComboBox(option1_Phone); 
        panel3.add(box1_Phone);
        
        panel3.add(new JLabel("Device Status:"));
        
        String[] option2_Phone = {"Standby","Off","Busy","Voicemail","Ringing","ReceivingCall"};  
        JComboBox box2_Phone = new JComboBox(option2_Phone);   
        panel3.add(box2_Phone);     
        
        panel3.add(new JLabel("Device Mode:"));
        
        String[] option3_Phone = {"Standard","Airplane","Voicemail"};  
        JComboBox box3_Phone = new JComboBox(option3_Phone);   
        panel3.add(box3_Phone);     
        
        addPhone = new JButton("Add Phone"); 
        addPhone.addActionListener(e -> {
	    	context.beginExtSession();
	    	context.addphone(DeviceName.getText(),DeviceOwner.getText(),String.valueOf(box1_Phone.getSelectedItem()),String.valueOf(box2_Phone.getSelectedItem()),String.valueOf(box3_Phone.getSelectedItem()));
	    	context.endExtSession();
	    });
        panel3.add(addPhone); 
        
        mainPanel.add(panel3); 
        
        
        
        JPanel panel4 = new JPanel();
	    
        panel4.add(new JLabel("Furniture Name"));
	    JTextField FurnitureName = new JTextField(10);
	    FurnitureName.setEditable(true);
	    FurnitureName.setText("");
	    panel4.add(FurnitureName);

	    
	    panel4.add(new JLabel("Furniture Location:"));
	   
	    String[] option1_Furniture = { "LivingRoom","BedRoom" }; 
        JComboBox box1_Furniture= new JComboBox(option1_Furniture); 
        panel4.add(box1_Furniture);
        
 
        addFurniture = new JButton("Add Furniture"); 
        addFurniture.addActionListener(e -> {
	    	context.beginExtSession();
	    	context.addfurniture(FurnitureName.getText(),String.valueOf(box1_Furniture.getSelectedItem()));
	    	context.endExtSession();
	    });
        panel4.add(addFurniture); 
        
        mainPanel.add(panel4); 
        
        JPanel panel5 = new JPanel();
	    
        panel5.add(new JLabel("TV Name"));
	    JTextField TVName = new JTextField(10);
	    TVName.setEditable(true);
	    TVName.setText("");
	    panel5.add(TVName);
	    
	    panel5.add(new JLabel("TV Location:"));
	   
	    String[] option1_TV = {"LivingRoom","BedRoom"}; 
        JComboBox box1_TV = new JComboBox(option1_TV); 
        panel5.add(box1_TV);
        
        panel5.add(new JLabel("TV Status:"));
        
        String[] option2_TV = {"Off","Playing","Standby","Mute","Unmute"};  
        JComboBox box2_TV = new JComboBox(option2_TV);   
        panel5.add(box2_TV);         
        
        addTV = new JButton("Add TV"); 
        addTV.addActionListener(e -> {
	    	context.beginExtSession();
	    	context.addtv(TVName.getText(),String.valueOf(box1_TV.getSelectedItem()),String.valueOf(box2_TV.getSelectedItem()));
	    	context.endExtSession();
	    });
        panel5.add(addTV); 
        
        mainPanel.add(panel5); 
        
        
        
        JPanel panel6 = new JPanel();
		
        panel6.setBackground(Color.gray);
		
		scenario = new JButton("Main Scenario");
		 
		scenario.addActionListener(e -> {
			context.beginExtSession();
			context.scenario();
			context.endExtSession();		});
		
		
		
		panel6.add(scenario);
		
		mainPanel.add(panel6);
        
	  }
}
