package Part2;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class FileChooser extends JPanel implements ActionListener {

	public static final String newline = "\n";
	private JFileChooser fc;
	private JTextArea ta;
	private JButton selectButton;
	private ElevatorSimulator mySim;
	
	public FileChooser(ElevatorSimulator simulator) {
		super(new BorderLayout());
		this.mySim = simulator;
		this.fc = new JFileChooser();
		this.ta = new JTextArea();
		this.ta.setMargin(new Insets(5, 5, 5, 5));
		this.ta.setEditable(false);
		JScrollPane taScrollPane = new JScrollPane(this.ta);
		this.selectButton = new JButton("Select input file"); 
		this.selectButton.addActionListener(this);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(this.selectButton);
		
		add(buttonPanel, BorderLayout.PAGE_START);
		add(taScrollPane, BorderLayout.CENTER);
	}
	
	  public void actionPerformed(ActionEvent e) {
		  
	        //Handle open button action.
	        if (e.getSource() == selectButton) {
	            int returnVal = fc.showOpenDialog(FileChooser.this);
	 
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File file = fc.getSelectedFile();
	                ta.append("Opening: " + file.getName() + "." + newline);
	            } else {
	                ta.append("Open command cancelled by user." + newline);
	            }
	            ta.setCaretPosition(ta.getDocument().getLength());
	        }
	    }
	 
	    private static ImageIcon createImageIcon(String path) {
	        java.net.URL imgURL = FileChooser.class.getResource(path);
	        if (imgURL != null) {
	            return new ImageIcon(imgURL);
	        } else {
	            System.err.println("Couldn't find file: " + path);
	            return null;
	        }
	    }
	 
	    private static void createAndShowGUI(ElevatorSimulator sim) {
	        JFrame frame = new JFrame("FileChooserDemo");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.add(new FileChooser(sim));
	        frame.pack();
	        frame.setVisible(true);
	    }
	 
	    public static void launch(final ElevatorSimulator sim) {
	        SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                UIManager.put("swing.boldMetal", Boolean.FALSE); 
	                createAndShowGUI(sim);
	            }
	        });
	    }	
	
	
	
	
	
}
