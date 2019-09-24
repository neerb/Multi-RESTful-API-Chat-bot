package Bot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class GetBotServerAndNamePrompt extends JFrame implements ActionListener
{
	private BreenBot bot;
	private BotGuiHandler guiInterface;
	
	private JPanel panel;
	private JLabel enterData;
	private JLabel labelName;
	private JTextField botName;
	private JLabel labelChannel;
	private JTextField channelName;
	private JButton submit;
	//private BoxLayout layout;
	private GridLayout layout;
	
	GetBotServerAndNamePrompt()
	{		
		int sizeSquared = 250;
		
		setTitle("New Bot");
		panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		enterData = new JLabel("Enter bot parameters below");
		labelName = new JLabel("Bot Name:");
		botName = new JTextField();
		botName.setText("BreenBot_v1");
		labelChannel = new JLabel("Server ID (begins with #):");
		channelName = new JTextField();
		channelName.setText("#testServer0100");
		submit = new JButton();
		layout = new GridLayout();
		layout.setColumns(1);
		layout.setRows(6);
		layout.setVgap(8);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(sizeSquared, sizeSquared);
		panel.setLayout(layout);
		
		submit.setText("Create new bot");
				
		enterData.setAlignmentX(Component.CENTER_ALIGNMENT);
		labelName.setAlignmentX(Component.CENTER_ALIGNMENT);
		botName.setAlignmentX(Component.CENTER_ALIGNMENT);
		labelChannel.setAlignmentX(Component.CENTER_ALIGNMENT);
		channelName.setAlignmentX(Component.CENTER_ALIGNMENT);
		submit.setAlignmentX(Component.CENTER_ALIGNMENT);

		panel.add(enterData);
		panel.add(labelName);
		panel.add(botName);
		panel.add(labelChannel);
		panel.add(channelName);
		panel.add(submit);
		
		submit.addActionListener(this);
		
		add(panel, SwingConstants.CENTER);		
	}
	
	public void reset()
	{
		setResizable(false);
		setLocationRelativeTo(null);
		toFront();
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		if(arg0.getSource() == submit)
		{
			if(!botName.getText().isEmpty() && !channelName.getText().isEmpty())
			{
				try
				{
					String name = botName.getText();
					String channel = channelName.getText();
					String server = "orwell.freenode.net";

					this.bot = new BreenBot(name, channel, server);
					this.guiInterface = new BotGuiHandler(this.bot, this);
					
					this.bot.setGuiInterface(this.guiInterface);

					setVisible(false);
					String time = new java.util.Date().toString();
					guiInterface.appendToChat(time + ": Bot initializing...");
				}
				catch(Exception ex)
				{
					JOptionPane.showMessageDialog(null, ex.getMessage());
					
					String time = new java.util.Date().toString();
					guiInterface.appendToChat(time + ": " + ex.getMessage());
					
					setLocationRelativeTo(null);
					setVisible(true);
					toFront();
					repaint();
				}
			}
			else
			{
				// Error messages for missing parameters
				if(botName.getText().isEmpty() && channelName.getText().isEmpty())
					JOptionPane.showMessageDialog(null, "Bot and channel name are required.");
				else if(botName.getText().isEmpty())
					JOptionPane.showMessageDialog(null, "Bot name is required.");
				else if(channelName.getText().isEmpty())
					JOptionPane.showMessageDialog(null, "Channel name is required.");
			}
		}
	}
}
