package Bot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * This class prompts the user for the name of their bot
 * and the channel it will be connecting to.
 * 
 */
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
	private GridLayout layout;

	GetBotServerAndNamePrompt()
	{
		int sizeSquared = 250;

		setTitle("New Bot");
		panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Creation of main components in JFrame panel
		enterData = new JLabel("Enter bot parameters below");
		labelName = new JLabel("Bot Name:");

		botName = new JTextField();
		botName.setText("BreenBot_v1");

		labelChannel = new JLabel("Server ID (begins with #):");

		channelName = new JTextField();
		channelName.setText("#BreenChannelDefault");

		submit = new JButton();
		submit.setText("Create new bot");

		layout = new GridLayout();
		layout.setColumns(1);
		layout.setRows(6);
		layout.setVgap(8);

		// Set size and default closing operation
		setSize(sizeSquared, sizeSquared);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// Set panel layout
		panel.setLayout(layout);

		// Align components to be centered
		enterData.setAlignmentX(Component.CENTER_ALIGNMENT);
		labelName.setAlignmentX(Component.CENTER_ALIGNMENT);
		botName.setAlignmentX(Component.CENTER_ALIGNMENT);
		labelChannel.setAlignmentX(Component.CENTER_ALIGNMENT);
		channelName.setAlignmentX(Component.CENTER_ALIGNMENT);
		submit.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Add components to panel
		panel.add(enterData);
		panel.add(labelName);
		panel.add(botName);
		panel.add(labelChannel);
		panel.add(channelName);
		panel.add(submit);

		// Add actionlistener to submit button
		submit.addActionListener(this);

		// Add panel to JFrame(this)
		add(panel, SwingConstants.CENTER);
	}

	/*
	 * This method initializes/resets this handler for getting name and channel data
	 */
	public void reset()
	{
		setResizable(false);
		setLocationRelativeTo(null);
		toFront();
		setVisible(true);
	}

	/*
	 * Implemented method from implemented ActionListener
	 */
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		if (arg0.getSource() == submit)
		{
			if (!botName.getText().isEmpty() && !channelName.getText().isEmpty())
			{
				try
				{
					// Creation and initialization of BreenBot
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
				catch (Exception ex)
				{
					// Display errors during initialiation
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
				if (botName.getText().isEmpty() && channelName.getText().isEmpty())
					JOptionPane.showMessageDialog(null, "Bot and channel name are required.");
				else if (botName.getText().isEmpty())
					JOptionPane.showMessageDialog(null, "Bot name is required.");
				else if (channelName.getText().isEmpty())
					JOptionPane.showMessageDialog(null, "Channel name is required.");
			}
		}
	}
}
