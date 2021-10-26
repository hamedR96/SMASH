package devices;

import java.awt.Color;

import javax.swing.*;

@SuppressWarnings("serial")
public class GuiSofa extends JFrame {

	  private static final int width = 350;
	  private static final int height = 150;
	  protected JPanel mainPanel;
	  public JButton buttonUserIsNear, buttonUserIsFar;

	  public GuiSofa(ArtSofa artSofa, String title){
		this.mainPanel = new JPanel();
		setTitle("SOFA .:: "+title+" ::.");
		setSize(width,height);
	    setContentPane(mainPanel);
	    mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));

	    JPanel panel1 = new JPanel();
	    
	    panel1.add(new JLabel("Name"));
	    JTextField PersonName = new JTextField(10);
	    PersonName.setEditable(true);
	    PersonName.setText("");
	    panel1.add(PersonName);
	    
	    mainPanel.add(panel1);
	    
	    JPanel panel2 = new JPanel();
	    buttonUserIsNear = new JButton("Sitting Down");
	    
	    buttonUserIsNear.addActionListener(e -> {
	    	artSofa.beginExtSession();
	    	artSofa.sittingDown(PersonName.getText());
	    	artSofa.endExtSession();
	    });
	    
	    buttonUserIsFar = new JButton("Standing Up");
	    
	    buttonUserIsFar.addActionListener(e -> {
	    	artSofa.beginExtSession();
	    	artSofa.standingUp(PersonName.getText());
	    	artSofa.endExtSession();
	    });
	    
	    
		panel2.setBackground(Color.gray);
	    panel2.add(buttonUserIsNear);
	    panel2.add(buttonUserIsFar);
	    mainPanel.add(panel2);
	  }
}
