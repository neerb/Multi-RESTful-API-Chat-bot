package Bot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;

/*
 *  This class is the admin commands/display for the bot.
 *  It will show all data passed to and from the bot.
 *  It can also send administrative messages to the user.
 *  
 */
public class BotGuiHandler extends JFrame implements ActionListener, KeyListener
{
	private BreenBot mainBot;
	GetBotServerAndNamePrompt leadPrompt;

	private JPanel panelTop;
	private JPanel panelBottom;
	private BoxLayout layoutTop;
	private GridLayout layoutBottom;

	private JTextArea chatData;
	private JScrollPane chatScroller;

	private JButton connectBot;
	private JTextField adminMessageField;
	private JButton sendAdminMessageField;
	private JButton saveLog;
	private JCheckBox enableCommands;
	private JLabel botStatus;

	private Font chatFont;
	private Font statusFont;

	BotGuiHandler(BreenBot bot, GetBotServerAndNamePrompt prompt)
	{
		this.mainBot = bot;
		this.leadPrompt = prompt;

		// Creation of GUI
		int width = 1200;
		int height = 550;

		chatFont = new Font(Font.MONOSPACED, Font.PLAIN, 14);
		statusFont = new Font(Font.MONOSPACED, Font.BOLD, 16);

		panelTop = new JPanel();
		panelBottom = new JPanel();

		layoutTop = new BoxLayout(panelTop, BoxLayout.Y_AXIS);

		// Align top panel
		panelTop.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Bottom layout has gridlayout
		layoutBottom = new GridLayout(3, 2);
		layoutBottom.setHgap(5);
		layoutBottom.setVgap(5);

		// Align bottom panel
		panelBottom.setAlignmentX(Component.CENTER_ALIGNMENT);

		setTitle(mainBot.getName() + " - Admin Controls : Channel Name = " + mainBot.getCurrentChannel());

		// Set size of JFrame and set default close operation
		setSize(width, height);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// Set up panels
		panelTop.setLayout(layoutTop);
		panelTop.setBorder(BorderFactory.createTitledBorder("Live chat (" + this.mainBot.getName() + "):"));
		panelBottom.setLayout(layoutBottom);
		panelBottom.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Set up chat data field
		chatData = new JTextArea(15, 100);
		chatData.setEditable(false);
		chatData.setFont(chatFont);
		chatData.setLineWrap(true);
		chatData.setWrapStyleWord(true);

		// Initialize chat scroller
		chatScroller = new JScrollPane(chatData);

		// Add the chat scroller to upper panel
		panelTop.add(chatScroller);

		// Set up the admin message field (sends admin messages)
		adminMessageField = new JTextField();
		adminMessageField.setEditable(true);
		adminMessageField.setFont(chatFont);
		// adminMessageField.setColumns(50);
		adminMessageField.setHorizontalAlignment(JTextField.LEFT);
		adminMessageField.addKeyListener(this);

		// Set up admin message field send button
		sendAdminMessageField = new JButton();
		sendAdminMessageField.setText("Send Administrator Message");

		// Add components to bottom panel
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
		connectBot.setFont(statusFont);

		botStatus = new JLabel("Bot Status: Disconnected");
		botStatus.setFont(statusFont);
		botStatus.setForeground(Color.RED);

		panelBottom.add(botStatus);

		saveLog = new JButton("Save this chat log");
		panelBottom.add(saveLog);
		panelBottom.add(connectBot);

		sendAdminMessageField.addActionListener(this);
		enableCommands.addActionListener(this);
		saveLog.addActionListener(this);

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

	/*
	 * Appends a message to the chat
	 */
	public void appendToChat(String message)
	{
		chatData.setText(chatData.getText() + message + "\n");

		JScrollBar verticalBar = chatScroller.getVerticalScrollBar();

		verticalBar.setValue(verticalBar.getMaximum());
	}

	/*
	 * Sends message from Administrator to client
	 */
	private void sendAdminMessageFromField()
	{
		String time = new java.util.Date().toString();

		if (!adminMessageField.getText().isEmpty())
		{
			String message = adminMessageField.getText();

			mainBot.sendMessage(mainBot.getCurrentChannel(), message);

			adminMessageField.setText("");
			appendToChat(time + " - " + mainBot.getName() + ": " + message);
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		String time = new java.util.Date().toString();

		if (arg0.getSource() == sendAdminMessageField)
		{
			sendAdminMessageFromField();
		}
		else if (arg0.getSource() == connectBot)
		{
			if (connectBot.getText().equals("Connect"))
			{
				try
				{
					// Connect bot to server
					this.mainBot.connectBot();
					setUtilitiesEnabled(true);
					appendToChat(time + ": Connected");
					botStatus.setText("Bot Status: Connected");
					botStatus.setForeground(Color.GREEN);
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
					// Disconnect bot from server
					this.mainBot.disconnect();
					setUtilitiesEnabled(false);
					appendToChat(time + ": Bot disconnected by user");
					botStatus.setText("Bot Status: Disconnected");
					botStatus.setForeground(Color.RED);
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
		else if (arg0.getSource() == enableCommands)
		{
			this.mainBot.setEnabled(enableCommands.isSelected());
		}
		else if (arg0.getSource() == saveLog)
		{
			try
			{
				saveLogData();
				this.appendToChat("- Chat log saved");
			}
			catch (Exception ex)
			{
				JOptionPane.showMessageDialog(null, "Error saving log: " + ex.getMessage());
				ex.printStackTrace();
			}
		}
	}

	/*
	 * This method retrieves the data from the console field and saves it to a file
	 * location specified by the user. A file dialog is shown to allow the user to
	 * choose where they wish to store the chat log.
	 */
	private void saveLogData() throws Exception
	{
		FileWriter fw;
		JFileChooser getFileLocation = new JFileChooser();

		getFileLocation.setDialogTitle("Save chat log as");
		getFileLocation.setDialogType(JFileChooser.SAVE_DIALOG);

		int selection = getFileLocation.showSaveDialog(this);

		if (selection == JFileChooser.APPROVE_OPTION)
		{
			File saveFile = getFileLocation.getSelectedFile();

			fw = new FileWriter(saveFile, true);

			// Store chat data
			chatData.write(fw);

			fw.close();
		}
	}

	/*
	 * Unused but necessary interface method
	 */
	@Override
	public void keyTyped(KeyEvent e)
	{

	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if (e.getSource() == adminMessageField && e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			sendAdminMessageFromField();
		}
	}

	/*
	 * Unused but necessary interface method
	 */
	@Override
	public void keyReleased(KeyEvent e)
	{

	}
}
