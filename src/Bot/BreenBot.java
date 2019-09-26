package Bot;
import java.awt.List;
import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import com.google.gson.*;

import org.jibble.pircbot.*;

// Get cryptocurrency data - cryptonator API - done
// Currency Exchange rate - done

// Add ability to switch between fahrenheit and celcius (boolean)
// Organize methods by similarity

public class BreenBot extends PircBot
{
	private String channel;
	private String server;
	private BotGuiHandler guiInterface;
	private boolean enabled = true;
		
	public BreenBot()
	{		
		// Default values
		this.setName("BreenBot_v1");
		this.channel = "#BreenChannelDefault";
		this.server = "orwell.freenode.net";
	}
	
	public BreenBot(String newName, String channel, String server)
	{		
		this.setName(newName);
		this.channel = channel;
		this.server = server;
	}
	
	public String getCurrentChannel()
	{
		return this.channel;
	}
	
	public String getCurrentServer()
	{
		return this.server;
	}
	
	public void setNewName(String n)
	{
		this.changeNick(n);
	}
	
	public void setNewChannel(String c)
	{
		this.channel = c;
	}
	
	public void setNewServer(String s)
	{
		this.server = s;
	}
	
	public void setEnabled(boolean val)
	{
		this.enabled = val;
	}
	
	public void setGuiInterface(BotGuiHandler botGui)
	{
		this.guiInterface = botGui;
	}
	
	public void connectBot() throws Exception
	{
		setVerbose(true);
		connect(this.server);
		joinChannel(this.channel);
	}
	
	/*
	 * 	This method gets the raw weather data of the cities by city name current overall condition,
	 * 	maximum temperature, minimum temperature, and current temperature.
	 * 
	 *  The data is returned as a String array of size 4.
	 *  index 0 = overall condition
	 *  index 1 = max temp
	 *  index 2 = min temp
	 *  index 3 = current temp
	 * 
	 * 	Uses openweather API
	 * 
	 */
	String[] getRawWeatherData(String city) throws JsonSyntaxException, Exception
	{
		final String urlHalf1 = "http://api.openweathermap.org/data/2.5/weather?q=";
		final String apiCode = "&APPID=0bc46790fafd1239fff0358dc4cbe982";
		
		String url = urlHalf1 + city + apiCode;
		
		String[] weatherData = new String[4];
		String data = null;
		
		JsonParser parser = new JsonParser();
		
		String hitUrl = url;
		String jsonData = getJsonData(hitUrl);
				
		JsonElement jsonTree = parser.parse(jsonData);
				
		if(jsonTree.isJsonObject())
		{
			JsonObject jsonObject = jsonTree.getAsJsonObject();
			
			JsonElement weather = jsonObject.get("weather");
			JsonElement main = jsonObject.get("main");
						
			if(weather.isJsonArray())
			{
				JsonArray weatherArray = weather.getAsJsonArray();
				
				JsonElement skyElement = weatherArray.get(0);
				
				if(skyElement.isJsonObject())
				{
					JsonObject skyObject = skyElement.getAsJsonObject();
					
					JsonElement skyData = skyObject.get("description");
					
					weatherData[0] = skyData.getAsString();
				}
			}
			
			if(main.isJsonObject())
			{
				JsonObject mainToObject = main.getAsJsonObject();
				System.out.println(mainToObject.toString());
				
				JsonElement tempMin = mainToObject.get("temp_min");
				JsonElement tempMax = mainToObject.get("temp_max");
				JsonElement temp = mainToObject.get("temp");
				
				weatherData[1] = tempMax.getAsString();
				weatherData[2] = tempMin.getAsString();
				weatherData[3] = temp.getAsString();
			}
		}
		
		return weatherData;
	}
	
	/*
	 * 	This method gets the raw weather data of the cities by zipcode current overall condition,
	 * 	maximum temperature, minimum temperature, and current temperature.
	 * 
	 *  The data is returned as a String array of size 4.
	 *  index 0 = overall condition
	 *  index 1 = max temp
	 *  index 2 = min temp
	 *  index 3 = current temp
	 * 
	 * 	Uses openweather API
	 * 
	 */
	String[] getRawWeatherDataByZipcode(String zipcode) throws JsonSyntaxException, Exception
	{
		final String urlHalf1 = "http://api.openweathermap.org/data/2.5/weather?q=";
		final String apiCode = "&APPID=0bc46790fafd1239fff0358dc4cbe982";
		
		String url = urlHalf1 + zipcode + ",us" + apiCode;
		
		String[] weatherData = new String[4];
		String data = null;
		
		JsonParser parser = new JsonParser();
		
		String hitUrl = url;
		String jsonData = getJsonData(hitUrl);
				
		JsonElement jsonTree = parser.parse(jsonData);
				
		if(jsonTree.isJsonObject())
		{
			JsonObject jsonObject = jsonTree.getAsJsonObject();
			
			JsonElement weather = jsonObject.get("weather");
			JsonElement main = jsonObject.get("main");
						
			if(weather.isJsonArray())
			{
				JsonArray weatherArray = weather.getAsJsonArray();
				
				JsonElement skyElement = weatherArray.get(0);
				
				if(skyElement.isJsonObject())
				{
					JsonObject skyObject = skyElement.getAsJsonObject();
					
					JsonElement skyData = skyObject.get("description");
					
					weatherData[0] = skyData.getAsString();
				}
			}
						
			if(main.isJsonObject())
			{
				JsonObject mainToObject = main.getAsJsonObject();
				System.out.println(mainToObject.toString());
				
				JsonElement tempMin = mainToObject.get("temp_min");
				JsonElement tempMax = mainToObject.get("temp_max");
				JsonElement temp = mainToObject.get("temp");
				
				weatherData[1] = tempMax.getAsString();
				weatherData[2] = tempMin.getAsString();
				weatherData[3] = temp.getAsString();
			}
		}
		
		return weatherData;
	}
	
	/*
	 *	Returns distance data from zipcode API
	 *	
	 *	Uses zipcode API
	 */
	double getDistanceData(String zip1, String zip2, char unit) throws Exception
	{
		double distanceData = -1;
		String halfOne = "https://www.zipcodeapi.com/rest/";
		String apiKey = "O7e7IbSxKhpVEySjnMn0aSmz6twM9FJ7gC3kyQGy0hp1DXO3AWYNavHpFDtOx9JI";
		String half2 = "/distance.json/" + zip1 + "/" + zip2 + "/" + (unit == 'm' ? "mile" : "kilometer");
		
		String url = halfOne + apiKey + half2;
		
		JsonParser parser = new JsonParser();
		
		String jsonData = getJsonData(url);
		
		JsonElement jsonTree = parser.parse(jsonData);
		
		if(jsonTree.isJsonObject())
		{
			JsonObject jObject = jsonTree.getAsJsonObject();
			JsonElement distanceElement = jObject.get("distance");
			
			distanceData = distanceElement.getAsDouble();
		}
		
		return distanceData;
	}
	
	/*
	 *	This method gets the cryptocurrency data
	 * 
	 *	Uses cryptonator API
	 */
	double getCryptoPrice(String symbol) throws JsonSyntaxException, Exception
	{
		final String urlHalf1 = "https://api.cryptonator.com/api/ticker/";
		final String urlHalf2 = "-usd";
		
		String url = urlHalf1 + symbol + urlHalf2;
		
		double price = -1;
		
		JsonParser parser = new JsonParser();
		
		String hitUrl = url;
		String jsonData = getJsonData(hitUrl);
				
		JsonElement jsonTree = parser.parse(jsonData);
				
		if(jsonTree.isJsonObject())
		{
			JsonObject jsonObject = jsonTree.getAsJsonObject();
			
			JsonElement ticker = jsonObject.get("ticker");
						
			if(ticker.isJsonObject())
			{
				JsonObject tickObject = ticker.getAsJsonObject();
				
				JsonElement priceData = tickObject.get("price");
				
				price = priceData.getAsDouble();
			}
			
		}
		
		return price;
	}
	
	/*
	 *	Returns exchange rates in string format
	 *
	 *	uses exchangeratesapi
	 */
	String getExchangeRates(String currency) throws Exception
	{
		String[] rateSymbols = {
				 "CAD",
				 "HKD",
				 "ISK",
				 "PHP",
				 "DKK",
				 "HUF",
				 "CZK",
				 "GBP",
				 "RON",
				 "HRK",
				 "JPY",
				 "THB",
				 "CHF",
				 "EUR",
				 "TRY",
				 "CNY",
				 "NOK",
				 "NZD",
				 "ZAR",
				 "USD",
				 "MXN",
				 "AUD",
				 };
		
		String rates = "";
		
		final String urlHalf1 = "https://api.exchangeratesapi.io/latest?base=";
		
		
		String url = urlHalf1 + currency.toUpperCase();
				
		JsonParser parser = new JsonParser();
		
		String hitUrl = url;
		String jsonData = getJsonData(hitUrl);
				
		JsonElement jsonTree = parser.parse(jsonData);
		
		if(jsonTree.isJsonObject())
		{
			JsonObject jObject = jsonTree.getAsJsonObject();
			
			JsonElement rateElement = jObject.get("rates");
			
			if(rateElement.isJsonObject())
			{
				JsonObject rateObject = rateElement.getAsJsonObject();
				
				for(int i = 0; i < rateSymbols.length; i++)
				{
					JsonElement currentExchangeElement = rateObject.get(rateSymbols[i]);
					
					rates += rateSymbols[i] + "=" + currentExchangeElement.getAsDouble() + (i < rateSymbols.length - 1 ? " " : ".");
				}
			}
		}
		
		return rates;
	}
	
	ArrayList<String> getRepresentativeDataByZipcode(String zipcode) throws Exception
	{
		ArrayList<String> returnData = new ArrayList<String>();
		
		String urlHalf1 = "https://whoismyrepresentative.com/getall_mems.php?zip=";
		String urlHalf2 = "&output=json";
		
		String url = urlHalf1 + zipcode + urlHalf2;
		
		JsonParser parser = new JsonParser();
		
		String jsonData = getJsonData(url);
				
		JsonElement jsonTree = parser.parse(jsonData);
		
		if(jsonTree.isJsonObject())
		{
			JsonObject treeObject = jsonTree.getAsJsonObject();
			
			JsonElement resultsElement = treeObject.get("results");
			
			if(resultsElement.isJsonArray())
			{
				JsonArray resultsArray = resultsElement.getAsJsonArray();
				
				int count = 0;
				// Saves three representatives max from json
				for(int pos = 0; pos < resultsArray.size() && count < 3; pos++)
				{
					JsonElement currentElement = resultsArray.get(pos);
					
					if(currentElement.isJsonObject()) 
					{
						JsonObject currentObject = currentElement.getAsJsonObject();
						
						returnData.add(currentObject.get("name").getAsString());
						returnData.add(currentObject.get("party").getAsString());
						returnData.add(currentObject.get("phone").getAsString());
						returnData.add(currentObject.get("link").getAsString());
						
						count++;
					}
				}				
			}
		}
				
		return returnData;
	}
	
	String getJsonData(String urlString) throws IOException
	{
	    BufferedReader reader = null;
	    try 
	    {
	        URL url = new URL(urlString);
	        reader = new BufferedReader(new InputStreamReader(url.openStream()));
	        StringBuffer buffer = new StringBuffer();
	        int read;
	        char[] chars = new char[1024];
	        
	        while ((read = reader.read(chars)) != -1)
	            buffer.append(chars, 0, read); 

	        return buffer.toString();
	    } 
	    catch(Exception ex)
	    {
	    	System.out.println(ex.getMessage());
	    }
	    finally 
	    {
	        if (reader != null)
	            reader.close();
	    }
	    
	    return null;
	}
	
	private void sendHelpOperations()
	{
		sendMessageAndAppend(this.channel, "- Weather data by zipcode or city name.  Use the explicit command: !weather <city name or zipcode> or just ask me something like: How's the weather in 75087?");
		sendMessageAndAppend(this.channel, "- Cryptocurrency price data: !cprice <crypto symbol (BTC, ETH, etc...)>");
		sendMessageAndAppend(this.channel, "- Exchange rates for any currency: !exchange <currency symbol (USD, JPY, MXN, etc...)>");
		sendMessageAndAppend(this.channel, "- State/city government representatives by zipcode: !representatives <zipcode> or just ask me something like: Who are the representatives for 01002?");
		sendMessageAndAppend(this.channel, "- Get distance between two zip codes in miles or kilometers: !distance <zipcode 1> <zipcode 2> <m or k>");
		sendMessageAndAppend(this.channel, "- Multiply a list of numbers: !multiply <num1> <num2> <num3> ... <num N>");
		sendMessageAndAppend(this.channel, "- Apply factorial on a number: !factorial <number>");
		sendMessageAndAppend(this.channel, "- Calculate exponential: !ex <base> <exponent>");
		sendMessageAndAppend(this.channel, "- Change my nickname: !changenick <new nickname>");
		sendMessageAndAppend(this.channel, "- Get the current time: !time");
		sendMessageAndAppend(this.channel, "- Ping the bot: !ping");
	}
	
	// This function is called upon the bot connecting to the channel
	public void onConnect()
	{
		String welcomeMessage = "Hello! My name is BreenBot. Here's a list of things I can do for you:";
		
			
		sendMessageAndAppend(this.channel, welcomeMessage);

		sendHelpOperations();
		
		sendMessageAndAppend(this.channel, "Please use !help to get this list of operations again.");
	}
	
	public String getPrefixCommand(String message)
	{
		return message.split(" ")[0];
	}
	
	
	// Iterative factorial: returns factorial of n
	public long factorial(int n)
	{
		int product = 1;
		
		for(int i = n; i >= 1; i--)
		{
			product *= i;
		}
		
		return product;
	}

	/*
	 * Convert kelvin to fahrenheit
	 * 
	 * Parameter: double containing kelvin temperature
	 */
    double kelvinToFahrenheit(double kelvin)
    {    	
    	return Math.round(((kelvin - 273.15) * (9.0/5.0) + 32) * 100.0) / 100.0;
    }
	
	/*
	 * This function returns all the arguments within a message passed to it.
	 * Disregards first function code beginning with '!'
	 * 
	 */
	String[] getArgs(String message)
	{
		String[] messageArgs = message.split(" ");
		String[] args = new String[messageArgs.length - 1];
		
		for(int i = 1; i < messageArgs.length; i++)
		{
			args[i - 1] = messageArgs[i];
		}
		
		return args;
	}
	
	private void sendMessageAndAppend(String channel, String message)
	{
    	String time = new java.util.Date().toString();

		sendMessage(channel, message);
		guiInterface.appendToChat(time + " - " + this.getName() + ": " + message);
	}
	
	/*
	 * Called when a message is sent within the channel
	 * 
	 * Current command list:
	 * 
	 * API calls:
	 * 	!weather
	 * 	!distance
	 * 	!cprice
	 * 	!exchange
	 * 	!representative <zipcode>
	 * 
	 * Math functions:
	 * 	!ex
	 * 	!multiply
	 * 	!factorial
	 * 
	 * Miscellaneous:
	 * 	!repeat
	 * 	!changenick
	 * 	!help
	 */
    public void onMessage(String channel, String sender, String login, String hostname, String message) 
    {
		String[] args = getArgs(message);
		
		// Update UI
    	String time = new java.util.Date().toString();
		guiInterface.appendToChat(time + " - " + sender +  ": " + message);
		
		try
		{
			if(this.enabled == true)
			{
				if(message.charAt(0) == '!')
				{
					/// API call commands
					/// ***
					// Get weather data
					// Format: !weather <String: City Name("Dallas", "London", "Los Angeles">
					if(getPrefixCommand(message).equalsIgnoreCase("!weather"))
					{
						String city = "";
						
						for(int i = 0; i < args.length; i++)
						{
							city += args[i]  + (i < args.length - 1 ? " " : "");
						}
						
						String weatherDataString = "";
						
						String[] data = getRawWeatherData(city);
						
						weatherDataString = "The sky in " + city + " is " + data[0] + " and the max temperature is " + kelvinToFahrenheit(Double.parseDouble(data[1])) + 
								"F while the minimum temperature is " + kelvinToFahrenheit(Double.parseDouble(data[2])) +
								"F and the current temperature is " + kelvinToFahrenheit(Double.parseDouble(data[3])) + "F";
						
						sendMessageAndAppend(channel, weatherDataString);
					}
					
					// Returns distance between two zip codes
					// Format: !distance <zipcode 1> <zipcode 2> <unit of measurement: 'k' or 'm'>
					if(getPrefixCommand(message).equalsIgnoreCase("!distance"))
					{
						double distance;

						distance = getDistanceData(args[0], args[1], args[2].charAt(0));
						
						sendMessageAndAppend(channel, "Distance between " + args[0] + " and " + args[1] + " is " + 
								distance + (args[2].charAt(0) == 'm' ? " miles" : " kilometers"));
					}
					
					// Gets cryptocurrency price of first argument (symbol of crypto)
					// Format: !cprice <crypto symbol(BTC, ETH, etc...)>
					if(getPrefixCommand(message).equalsIgnoreCase("!cprice"))
					{
						String symbol = args[0];
						symbol = symbol.toUpperCase();
						
						double price = getCryptoPrice(symbol);
						
						if(price != -1)
						{
							String priceDataString = "The price of " + symbol + " is currently $" + price;
							
							sendMessageAndAppend(channel, priceDataString);
						}
						else
						{
							String errorMessage = "Error: please make sure you are entering valid data for this command.";
							
							sendMessageAndAppend(channel, errorMessage);
						}
					}
	
					if(getPrefixCommand(message).equalsIgnoreCase("!exchange"))
					{
						String symbol = args[0];
						symbol = symbol.toUpperCase();
						

						sendMessageAndAppend(channel, "Exchange rates for " + symbol + " are: " + getExchangeRates(symbol));
					}
					/// API call commands end
					
					
					
					/// Math function commands
					/// ***
					// Returns exponential calculation of arg1(base) and arg2(exponent)
					// Format: !ex <base> <exponent>
					if(getPrefixCommand(message).equalsIgnoreCase("!ex"))
					{
						double base = Double.parseDouble(args[0]);
						double exponent = Double.parseDouble(args[1]);
						
						double number = Math.pow(base,  exponent);
						
						sendMessageAndAppend(channel, base + "^" + exponent + " = " + number);
					}
					
					// Multiplies as many arguments as there are
					// Format: !multiply <num1> <num2> <num3> ..... and so on to <numN>
					if(getPrefixCommand(message).equalsIgnoreCase("!multiply"))
					{
						double product = 1;
						
						for(int i = 0; i < args.length; i++)
						{
							product *= Double.parseDouble(args[i]);
						}
						
						sendMessageAndAppend(channel, "The product of your numbers is " + product);
					}
					
					// Returns factorial of first argument
					// Format: !factorial <number>
					if(getPrefixCommand(message).equalsIgnoreCase("!factorial"))
					{
						int num = Integer.parseInt(args[0]);
						
						sendMessageAndAppend(channel, "Factorial of " + num + ": " + factorial(num));
					}
					/// Math function commands end
					
					
					
					/// Miscellaneous commands
					/// ***
					// Gets current time
					if(getPrefixCommand(message).equalsIgnoreCase("!time")) 
					{
						 String currTime = new java.util.Date().toString();
						 sendMessageAndAppend(channel, sender + ": The time is now " + currTime);
					}
					
					// Quick ping command
					if(getPrefixCommand(message).equalsIgnoreCase("!ping"))
					{
						String pong = "Pong!";
						sendMessageAndAppend(channel, pong);
					}
					
					// Repeat command: repeats whatever the string parameter is
					if(getPrefixCommand(message).equalsIgnoreCase("!repeat"))
					{
						String returnMessage = "";
						String[] split = message.split(" ");
						
						for(int i = 1; i < split.length; i++)
						{
							returnMessage += split[i] + ((i < split.length - 1) ? " " : "");
						}
						
						sendMessageAndAppend(channel, returnMessage);
					}
					
					// Changes the bot nickname
					if(getPrefixCommand(message).equalsIgnoreCase("!changenick"))
					{
						String newUser = "";
						
						for(int i = 0; i < args.length; i++)
						{
							newUser += args[i]  + (i < args.length - 1 ? " " : "");
						}
						
						changeNick(newUser);
					}
					
					// Shows user list of available commands
					if(getPrefixCommand(message).equalsIgnoreCase("!help"))
					{
						sendHelpOperations();
					}
					/// Miscellaneous commands end
				}
				// Handles word commands
				else
				{
					handleWordMessage(channel, sender, message, args);
				}
			}
			// Commands not enabled
			else
			{
				sendMessageAndAppend(channel, getName() + " is not currently taking commands/messages.");
			}
		}
		catch(Exception ex)
		{
			sendMessageAndAppend(channel, "Error: invalid data entered - Exception occured: " + ex.getMessage());
			guiInterface.appendToChat(ex.getMessage());
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
    }
    
    private String removeSpecialCharacters(String word)
    {
    	String newString = "";
    	
    	for(int i = 0; i < word.length(); i++)
    	{
    		if(word.charAt(i) != '?' &&
    				word.charAt(i) != '!' && 
    				word.charAt(i) != '"' && 
    				word.charAt(i) != '\'' && 
    				word.charAt(i) != '#' && 
    				word.charAt(i) != '(' && 
    				word.charAt(i) != ')' && 
    				word.charAt(i) != '$' && 
    				word.charAt(i) != '%' &&
    				word.charAt(i) != ',' && 
    				word.charAt(i) != '&')
    		{
    			newString += word.charAt(i);
    		}
    	}
    	
    	return newString;
    }
    
    private void handleWordMessage(String channel, String sender, String message, String[] args)
    {
    	message = message.toLowerCase();
    	
    	if(message.contains("weather"))
    	{
    		long potentialZipcode;
    		
        	for(int i = 0; i < args.length; i++)
        	{
        		try
        		{
        			potentialZipcode = Long.parseUnsignedLong(removeSpecialCharacters(args[i]));
        			
        			//If valid parse, then print zipcode weather to user
        			try 
        			{
        				String zipToString = Long.toString(potentialZipcode);
        				
        				// Handles zipcodes beginning with 0
        				zipToString = (zipToString.length() == 4 ? "0" : "") + zipToString;
        				
        				String weatherDataString;
        				
        				String[] data = getRawWeatherDataByZipcode(zipToString);
        				
        				weatherDataString = "The sky in " + zipToString + " is " + data[0] + " and the max temperature is " + kelvinToFahrenheit(Double.parseDouble(data[1])) + 
        						"F while the minimum temperature is " + kelvinToFahrenheit(Double.parseDouble(data[2])) +
        						"F and the current temperature is " + kelvinToFahrenheit(Double.parseDouble(data[3])) + "F";
        				
        				sendMessageAndAppend(this.channel, weatherDataString);
        			} 
        			catch (Exception e) 
        			{
        				System.out.println(e.getMessage());
        			}
        		}
        		catch(Exception ex)
        		{
        			System.out.println("- Argument " + i + " is not a valid zipcode number");
        		}
        	}
    	}
    	else if(message.contains("representative"))
    	{
    		long potentialZipcode;
    		
        	for(int i = 0; i < args.length; i++)
        	{
        		try
        		{
        			// A potential zipcode to be used
        			potentialZipcode = Long.parseUnsignedLong(removeSpecialCharacters(args[i]));
        			
        			//If valid parse, then print zipcode weather to user
        			try 
        			{
        				String zipToString = Long.toString(potentialZipcode);
        				
        				// Handles zipcodes beginning with 0
        				zipToString = (zipToString.length() == 4 ? "0" : "") + zipToString;
        				        				
        				System.out.println(zipToString);
        				ArrayList<String> representativeDataArray = getRepresentativeDataByZipcode(zipToString);
        				String representativeDataString = "";
        				
        				// Construct return string to user
        				int num = 1;
        				for(int pos = 0; pos < representativeDataArray.size(); pos += 4)
        				{
        					representativeDataString += "Rep. " + num++ + " = { Name: " + representativeDataArray.get(pos);
        					representativeDataString += " / Party: " + representativeDataArray.get(pos + 1);
        					representativeDataString += " / Phone: " + representativeDataArray.get(pos + 2);
        					representativeDataString += " / Link: " + representativeDataArray.get(pos + 3) + " } ";
        				}
        				
        				sendMessageAndAppend(this.channel, representativeDataString);
        			} 
        			catch (Exception e) 
        			{
        				System.out.println(e.getMessage());
        			}
        		}
        		catch(Exception ex)
        		{
        			System.out.println("- Argument " + i + " is not a valid zipcode number");
        		}
        	}
    	}
    }
    
    protected void onDisconnect()
    {
		sendMessage(this.channel, "BreenBot signing off... Goodbye!");
    }
}
