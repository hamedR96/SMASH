package devices;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class GuiTV extends JFrame {

	private static final int width = 300;
	private static final int height = 150;
	protected JPanel mainPanel;
	public JButton buttonPlay, buttonPause, buttonStop, buttonLoad;
	 
	public GuiTV(ArtTV artTV, String title){
		this.mainPanel = new JPanel();
		setTitle("TV .:: "+title+" ::.");
		setSize(width,height);
	    setContentPane(mainPanel);
	    mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
		
		JPanel panel1 = new JPanel();
		
		panel1.setBackground(Color.gray);
		
		buttonPause = new JButton("Mute");
		 
		buttonPause.addActionListener(e -> {
			artTV.beginExtSession();
	    	artTV.mute();
	    	artTV.endExtSession();		});
		
		
		buttonPlay = new JButton("Unmute");
		buttonPlay.addActionListener(e -> {
			artTV.beginExtSession();
	    	artTV.unmute();
	    	artTV.endExtSession();		});
		
		buttonStop = new JButton("On/Off");
		buttonStop.addActionListener(e -> {
			artTV.beginExtSession();
	    	artTV.turnOnOff();
	    	artTV.endExtSession();	
	    	});		
		
		panel1.add(buttonPlay);
		panel1.add(buttonPause);
		panel1.add(buttonStop);
		mainPanel.add(panel1);
		
		JPanel panel2 = new JPanel();
		
		
		 panel2.add(new JLabel("Media:"));
		 JTextField Name = new JTextField(10);
		 Name.setEditable(true);
		 Name.setText("");
		 panel2.add(Name);
		 buttonLoad = new JButton("Load"); 
		 buttonLoad.addActionListener(e -> {
		    	artTV.beginExtSession();
		    	artTV.loadMovie(Name.getText());
		    	artTV.endExtSession();
		    });
		    	panel2.add(buttonLoad);
				mainPanel.add(panel2);
	}
}
	
