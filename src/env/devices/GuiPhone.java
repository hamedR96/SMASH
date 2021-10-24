package devices;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class GuiPhone extends JFrame {

	private static final int width = 550;
	private static final int height = 250;
	protected JPanel mainPanel;
	public JButton buttonRequestMovie;
	public JTextField fieldMovieName;

	@SuppressWarnings("rawtypes")
	public GuiPhone(ArtPhone artPhone, String title){
	
		this.mainPanel = new JPanel();
		setTitle("Phone .:: "+title+" ::.");
		setSize(width,height);
	    setContentPane(mainPanel);
	    mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
		
		JPanel panel1 = new JPanel();
		JButton buttonAnswerCall = new JButton("Accept call");
		buttonAnswerCall.addActionListener(e -> {
			artPhone.beginExtSession();
			artPhone.answeringCall();
			artPhone.endExtSession();
		});
		JButton buttonRejectCall = new JButton("Reject call");
		buttonRejectCall.addActionListener(e -> {
			artPhone.beginExtSession();
			artPhone.rejectingCall();
			artPhone.endExtSession();
		});
		JButton buttonVoicemail = new JButton("Voicemail");
		buttonVoicemail.addActionListener(e -> {
			artPhone.beginExtSession();
			artPhone.voicemail();
			artPhone.endExtSession();
		});
		panel1.add(buttonAnswerCall);
		panel1.add(buttonRejectCall);
		panel1.add(buttonVoicemail);
		mainPanel.add(panel1);
		
		JPanel panel2 = new JPanel();
	    
		panel2.add(new JLabel("type:"));
		String[] option1_Request = {"Channel","Movie","Music" }; 
        @SuppressWarnings("unchecked")
		JComboBox box1_Request = new JComboBox(option1_Request); 
        panel2.add(box1_Request);
		
	    panel2.add(new JLabel("title:"));
	    JTextField titleofcontent = new JTextField(10);
	    titleofcontent.setEditable(true);
	    titleofcontent.setText("");
	    panel2.add(titleofcontent);
	    
	    
	    panel2.add(new JLabel("from:"));
	    JTextField Requestobject = new JTextField(10);
	    Requestobject.setEditable(true);
	    Requestobject.setText("");
	    panel2.add(Requestobject);
	    
	    
		JButton buttonRequestMovie = new JButton("Load");
	    buttonRequestMovie.addActionListener(e -> {
	    	artPhone.beginExtSession();
	    	artPhone.request(Requestobject.getText(),titleofcontent.getText(),String.valueOf(box1_Request.getSelectedItem()));
	    	artPhone.endExtSession();
	    });
	    panel2.add(buttonRequestMovie);
		mainPanel.add(panel2);
		
		
		
		
		
		
		JPanel panel4 = new JPanel();
		
        panel4.add(new JLabel("Name:"));
	    JTextField name = new JTextField(10);
	    name.setEditable(true);
	    name.setText("");
	    panel4.add(name);
	    
	    
	    panel4.add(new JLabel("Number:"));
	    JTextField number = new JTextField(10);
	    number.setEditable(true);
	    number.setText("");
	    panel4.add(number);
	    
	    
		JButton call = new JButton("Call");
	    call.addActionListener(e -> {
	    	artPhone.beginExtSession();
	    	artPhone.calling(name.getText(),number.getText());
	    	artPhone.endExtSession();
	    });
	    panel4.add(call);
		mainPanel.add(panel4);
		
		
		JPanel panel5 = new JPanel();
		 panel5.add(new JLabel("Mode:"));
			String[] option1_Mode = {"Standard","AirPlane","Voicemail"}; 
			@SuppressWarnings("unchecked")
			JComboBox box1_Mode = new JComboBox(option1_Mode); 
	        panel5.add(box1_Mode);
		    
		    
			JButton Set = new JButton("Set");
		    Set.addActionListener(e -> {
		    	artPhone.beginExtSession();
		    	artPhone.settingMode(String.valueOf(box1_Mode.getSelectedItem()));
		    	artPhone.endExtSession();
		    });
		    panel5.add(Set);
			mainPanel.add(panel5);
			
		
		JPanel panel3 = new JPanel();
		panel3.setBackground(Color.gray);
		panel3.add(new JLabel("Incoming call from:"));
	    JTextField textPersonName = new JTextField(10);
	    textPersonName.setEditable(true);
	    textPersonName.setText("");
	    panel3.add(textPersonName);
		
	    panel3.add(new JLabel("type:"));
		String[] option1_Ringing = { "Family","Work","Friend","Unkown" }; 
		@SuppressWarnings("unchecked")
		JComboBox box1_Ringing = new JComboBox(option1_Ringing); 
        panel3.add(box1_Ringing);
	    
		JButton buttonIncomingCall = new JButton("Receiving");
		buttonIncomingCall.addActionListener(e -> {
	    	artPhone.beginExtSession();
	    	artPhone.receivingCall(textPersonName.getText(),String.valueOf(box1_Ringing.getSelectedItem()));
	    	artPhone.endExtSession();
	    });
	    panel3.add(buttonIncomingCall);
		mainPanel.add(panel3);
		
			

		
		
	}
}
