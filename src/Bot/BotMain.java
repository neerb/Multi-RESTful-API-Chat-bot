package Bot;

public class BotMain 
{
	public static void main(String[] args) throws Exception
	{			
		// Bot entry point
		GetBotServerAndNamePrompt bot = new GetBotServerAndNamePrompt();
		//bot.begin();
	
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
