package Bot;

/*
 * This class is the entry point for the entirety of
 * this bot's functionality.
 */
public class BotMain 
{
	public static void main(String[] args) throws Exception
	{			
		// Bot entry point
		GetBotServerAndNamePrompt bot = new GetBotServerAndNamePrompt();
		bot.reset();
	
		// Shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			public void run()
			{
				String currTime = new java.util.Date().toString();

				System.out.println(currTime + ": *** PROGRAM TERMINATED ***");
			}
		});
	}
}
