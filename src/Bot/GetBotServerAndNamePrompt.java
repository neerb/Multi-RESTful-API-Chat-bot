package Bot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GetBotServerAndNamePrompt extends JFrame
{
	private BreenBot bot;
	
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
		// Create bot with default parameters
		
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
		//layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
		layout = new GridLayout();
		layout.setColumns(1);
		layout.setRows(6);
		layout.setVgap(8);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(sizeSquared, sizeSquared);
		panel.setLayout(layout);
		
		submit.setText("Connect Bot");
		
		add(panel, SwingConstants.CENTER);		
		
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
		
		submit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String name = botName.getText();
				String channel = channelName.getText();
				String server = "orwell.freenode.net";
				
				bot = new BreenBot(name, channel, server);
				
				if(!name.isEmpty() && !channel.isEmpty() && !server.isEmpty())
				{
					setVisible(false);

					try
					{
						bot.begin();
						bot.connectBot();
					}
					catch(Exception ex)
					{
						System.out.println(ex.getMessage());
					}
				}

			}
		});
		
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
