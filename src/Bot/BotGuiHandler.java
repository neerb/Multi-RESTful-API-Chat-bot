package Bot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

/*
 *  This class is the admin commands/display for the bot.
 *  It will show all data passed to and from the bot.
 *  It can also send administrative messages to the user.
 *  
 */
public class BotGuiHandler extends JFrame implements ActionListener
{
	private BreenBot mainBot;
	GetBotServerAndNamePrompt leadPrompt;
	
	private JPanel panelTop;
	private JPanel panelBottom;
	private GridLayout layoutTop;
	private GridLayout layoutBottom;
	
	private JLabel chatDataLabel;
	private JTextArea chatData;
	private JScrollPane chatScroller;

	private JButton connectBot;
	private JTextField adminMessageField;
	private JButton sendAdminMessageField;
	private JCheckBox enableCommands;
	
	private Font chatFont;

	BotGuiHandler(BreenBot bot, GetBotServerAndNamePrompt prompt)
	{
		this.mainBot = bot;
		this.leadPrompt = prompt;
		
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
		
		setTitle(mainBot.getName() + " - Admin Controls");
		
		setSize(width, height);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//panelTop.setSize(chatDimension);
		panelTop.setBounds(0, 20, width, height);
		
		//panelBottom.setSize(buttonDimension);
		//panelBottom.setBounds(0, chatDimension.height + 20, buttonDimension.width, buttonDimension.height);
		
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
	
		connectBot = new JButton("Connect");
		connectBot.addActionListener(this);
		connectBot.setBackground(Color.RED);
		connectBot.setForeground(Color.WHITE);
		panelBottom.add(connectBot);
		
		sendAdminMessageField.addActionListener(this);
		enableCommands.addActionListener(this);
		
		// User message
		appendToChat("***New Bot Created...****");
		appendToChat("- Initialization process beginning...");
		appendToChat("- Please wait...");

		
		// Disable some utilities until bot has initialized and connected
		adminMessageField.setEnabled(false);
		sendAdminMessageField.setEnabled(false);
		enableCommands.setEnabled(false);

		add(panelTop, BorderLayout.NORTH);
		add(panelBottom, BorderLayout.SOUTH);
		
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		toFront();
		setVisible(true);
	}

	public void setUtilitiesEnabled(boolean val)
	{
		adminMessageField.setEnabled(val);
		sendAdminMessageField.setEnabled(val);
		enableCommands.setEnabled(val);
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
		String time = new java.util.Date().toString();
		
		if(arg0.getSource() == sendAdminMessageField)
		{
			if(!adminMessageField.getText().isEmpty())
			{
				String message = adminMessageField.getText();

				mainBot.sendMessage(mainBot.getCurrentChannel(), message);

				adminMessageField.setText("");
				appendToChat(time + " - " + mainBot.getName() +  ": " + message);
			}
		}
		else if(arg0.getSource() == connectBot)
		{			
			if(connectBot.getText().equals("Connect"))
			{
				try 
				{
					this.mainBot.connectBot();
					setUtilitiesEnabled(true);
					appendToChat(time + ": Connected");
					connectBot.setBackground(Color.GREEN);
					connectBot.setText("Disconnect");
				} 
				catch (Exception ex) 
				{
					this.leadPrompt.reset();
					setDefaultCloseOperation(DISPOSE_ON_CLOSE);
					dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
					JOptionPane.showMessageDialog(null, ex.getMessage());
					connectBot.setEnabled(true);
				}
			}
			else
			{
				try 
				{
					this.mainBot.disconnect();
					setUtilitiesEnabled(false);
					appendToChat(time + ": Bot disconnected by user");
					connectBot.setBackground(Color.RED);
					connectBot.setText("Connect");
				} 
				catch (Exception ex) 
				{
					this.leadPrompt.reset();
					setDefaultCloseOperation(DISPOSE_ON_CLOSE);
					dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
					JOptionPane.showMessageDialog(null, ex.getMessage());
					connectBot.setEnabled(true);
				}
			}
		}
		else if(arg0.getSource() == enableCommands)
		{
			this.mainBot.setEnabled(enableCommands.isSelected());
		}
	}
}
