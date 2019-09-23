package Bot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 *  This class is the admin commands/display for the bot.
 *  It will show all data passed to and from the bot.
 *  It can also send administrative messages to the user.
 *  
 */
public class BotGuiHandler extends JFrame implements ActionListener
{
	private BreenBot mainBot;
	
	private JPanel panelTop;
	private JPanel panelBottom;
	private GridLayout layoutTop;
	private GridLayout layoutBottom;
	
	private JLabel chatDataLabel;
	private JTextArea chatData;
	private JScrollPane chatScroller;

	private JTextField adminMessageField;
	private JButton sendAdminMessageField;
	private JCheckBox enableCommands;
	
	private Font chatFont;

	BotGuiHandler(BreenBot bot)
	{
		this.mainBot = bot;
		
		// Creation of GUI
		int width = 1200;
		int height = 400;
		
		chatFont = new Font(Font.MONOSPACED, Font.PLAIN, 16);
		
		Dimension chatDimension = new Dimension(400, height);
		Dimension buttonDimension = new Dimension(width - chatDimension.width, height);

		layoutTop = new GridLayout();
		layoutTop.setColumns(1);
		layoutTop.setRows(1);
		//layoutTop.setHgap(10);
		//layoutTop.setVgap(10);
		
		layoutBottom = new GridLayout(2, 1);
		
		panelTop = new JPanel();
		panelBottom = new JPanel();
		
		setTitle("BreenBot - Admin Controls");
		
		setSize(width, height);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//panelTop.setSize(chatDimension);
		panelTop.setBounds(0, 20, width, height);
		
		//panelBottom.setSize(buttonDimension);
		//panelBottom.setBounds(0, chatDimension.height + 20, buttonDimension.width, buttonDimension.height);
		
		add(panelTop, BorderLayout.NORTH);
		add(panelBottom, BorderLayout.SOUTH);
		
		panelTop.setLayout(layoutTop);
		panelTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panelBottom.setLayout(layoutBottom);
		panelBottom.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		chatDataLabel = new JLabel("Live Chat:");
		chatDataLabel.setSize(10, 10);
		//add(chatDataLabel, BorderLayout.NORTH);
		
		chatData = new JTextArea(15, 100);
		chatData.setEditable(false);
		chatData.setFont(chatFont);
		chatData.setLineWrap(true);
		chatData.setWrapStyleWord(true);
		
		chatScroller = new JScrollPane(chatData);
		
		//panelTop.add(chatDataLabel, BorderLayout.WEST);
		panelTop.add(chatScroller);
		
		adminMessageField = new JTextField();
		adminMessageField.setEditable(true);
		adminMessageField.setFont(chatFont);
		//adminMessageField.setColumns(50);
		adminMessageField.setHorizontalAlignment(JTextField.LEFT);
		
		sendAdminMessageField = new JButton();
		sendAdminMessageField.setText("Send Administrator Message");
		panelBottom.add(adminMessageField);
		panelBottom.add(sendAdminMessageField);
		
		enableCommands = new JCheckBox();
		enableCommands.setText("Enable commands");
		enableCommands.setSelected(true);
		panelBottom.add(enableCommands);
		
		sendAdminMessageField.addActionListener(this);
		enableCommands.addActionListener(this);
		
		// User message
		appendToChat("***New Bot Created...****");
		appendToChat("- Initialization process beginning...");
		appendToChat("- Please wait...");

		
		// Disable some utilities until bot has initialized
		adminMessageField.setEnabled(false);
		sendAdminMessageField.setEnabled(false);
		
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void enableUtilities()
	{
		adminMessageField.setEnabled(true);
		sendAdminMessageField.setEnabled(true);
	}
	
	private void sendBotMessage(String message)
	{
		mainBot.sendMessage(mainBot.getCurrentChannel(), message);
	}
	
	public void appendToChat(String message)
	{
		chatData.setText(chatData.getText() + message + "\n");
		
		JScrollBar verticalBar = chatScroller.getVerticalScrollBar();
		
		verticalBar.setValue(verticalBar.getMaximum());
	}

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		if(arg0.getSource() == sendAdminMessageField)
		{
			String message = adminMessageField.getText();
			String time = new java.util.Date().toString();

			mainBot.sendMessage(mainBot.getCurrentChannel(), message);
			adminMessageField.setText("");
			appendToChat(time + " - " + mainBot.getName() +  ": " + message);
		}
		else if(arg0.getSource() == enableCommands)
		{
			mainBot.setEnabled(enableCommands.isSelected());
		}
	}
}
