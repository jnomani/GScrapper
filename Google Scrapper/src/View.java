import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

@SuppressWarnings("serial")
public class View extends JFrame {

	public static final int HEIGHT = 200;
	public static final int WIDTH = 400;

	public static final String TITLE = "Google Scrapper";

	private JPanel queryPanel = new JPanel(), savePanel = new JPanel();
	private JButton queryButton = new JButton("Query");
	private JTextField searchField = new JTextField();
	private JLabel sveLabel = new JLabel("Save as:");
	private JRadioButton excel = new JRadioButton("Excel"),
			html = new JRadioButton("HTML");
			//text = new JRadioButton("Text");
	
	private ButtonGroup radioGroup = new ButtonGroup();

	private GridLayout mainLayout = new GridLayout(2, 1);
	private GridBagLayout queryLayout = new GridBagLayout();
	private GridLayout saveLayout = new GridLayout(1, 3);
	
	private GridBagConstraints
	qbc = new GridBagConstraints(), tfc = new GridBagConstraints();

	

	public View() {
		
		queryButton.addActionListener(new qAction());
		
		setMinimumSize(new Dimension(300, 100));
		searchField.setMinimumSize(searchField.getPreferredSize());
		
		tfc.anchor = GridBagConstraints.FIRST_LINE_START;
		tfc.weightx = 2; tfc.weighty = 2;
		tfc.gridx = 0; tfc.gridy = 0;
		tfc.fill = GridBagConstraints.BOTH;
		
		
		qbc.anchor = GridBagConstraints.FIRST_LINE_END;
		qbc.weightx = .25; tfc.weighty = 1;
		qbc.gridx = GridBagConstraints.RELATIVE;
		qbc.gridy = 0;
		qbc.fill = GridBagConstraints.BOTH;
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setSize(WIDTH, HEIGHT);
		setTitle(TITLE);
		
		queryPanel.setLayout(queryLayout);
		queryPanel.add(searchField, tfc);
		queryPanel.add(queryButton, qbc);
		
		radioGroup.add(excel);
		radioGroup.add(html);
		//radioGroup.add(text);
		
		excel.setActionCommand("Excel");
		html.setActionCommand("HTML");
		//text.setActionCommand("Text");
		
		getRootPane().setBorder(new EmptyBorder(5, 5, 5, 5) );
		
		savePanel.setLayout(saveLayout);
		savePanel.add(sveLabel);
		savePanel.add(excel);
		savePanel.add(html);
		//savePanel.add(text);
		
		setLayout(mainLayout);
		add(queryPanel);
		add(savePanel);
		
		pack();
		
		
		setVisible(true);
	}
	
	
	
	private class qAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			final String ftype;
			try{
				ftype = radioGroup.getSelection().getActionCommand();
			
			}catch(Exception e){
				JOptionPane.showMessageDialog(null, "You must select a file type!");
				return;
			}
			
			String loc; 
			
			JFileChooser j = new JFileChooser(){
			    @Override
			    public void approveSelection(){
			        File f = getSelectedFile();
			        if(f.exists() && getDialogType() == SAVE_DIALOG){
			            int result = JOptionPane.showConfirmDialog(this,"The file exists, overwrite?","Existing file",JOptionPane.YES_NO_CANCEL_OPTION);
			            switch(result){
			                case JOptionPane.YES_OPTION:
			                    super.approveSelection();
			                    return;
			                case JOptionPane.NO_OPTION:
			                	
			                	return;
			                case JOptionPane.CLOSED_OPTION:
			                	
			                	return;
			                case JOptionPane.CANCEL_OPTION:
			                    cancelSelection();
			                    return;
			            }
			        }
			    }
			    //super.approveSelection();
			};
			j.setFileFilter(new FileFilter(){

				@Override
				public boolean accept(File f) {
					if(f.isDirectory()) return true;
					if(f.getName().toLowerCase().endsWith(ftype.equals("Excel") ? ".csv" : ".html")) return true;
					return false;
				}

				@Override
				public String getDescription() {
					if(ftype.equals("Excel")){
						return "*.csv";
					}else{
						return "*.html";
					}
				}
				
			});
			int val = j.showSaveDialog(View.this);
			if(val == JFileChooser.APPROVE_OPTION){
				loc = j.getSelectedFile().getAbsolutePath();
			}else return;
			
			String query = searchField.getText();
			
			Control.query(query, loc, ftype);
		}
		
	}
	
	
}
