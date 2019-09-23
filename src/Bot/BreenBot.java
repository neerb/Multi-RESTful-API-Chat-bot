package Bot;
import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import org.jibble.pircbot.*;

// Get cryptocurrency data - cryptonator API
// Get google images data
// Currency Exchange rate

// Add ability to switch between fahrenheit and celcius (boolean)
// Organize methods by similarity

public class BreenBot extends PircBot
{
	private String channel;
	private String server;
	private BotGuiHandler guiInterface;
	private GetBotServerAndNamePrompt prompt;
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
	
	public void begin() throws Exception
	{
		guiInterface = new BotGuiHandler(this);
	}
	
	public void connectBot() throws Exception
	{
		String time = new java.util.Date().toString();

		guiInterface.appendToChat(time + ": Bot initializing...");
		
		setVerbose(true);
		connect(this.server);
		joinChannel(this.channel);
		
		time = new java.util.Date().toString();
		
		guiInterface.appendToChat(time + ": Connected.");
		guiInterface.enableUtilities();
	}
	
	/*
	 * 	This method gets the raw weather data of the cities current overall condition,
	 * 	maximum temperature, minimum temperature, and current temperature.
	 * 
	 *  The data is returned as a String array of size 4.
	 *  index 0 = overall condition
	 *  index 1 = max temp
	 *  index 2 = min temp
	 *  index 3 = current temp
	 * 
	 * 	Uses openweather API
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
				
				String arrayToJsonObject = weatherArray.toString().substring(1, weather.toString().length() - 1);
				
				JsonParser arrayParser = new JsonParser();
				
				JsonElement jsonArrayTree = arrayParser.parse(arrayToJsonObject);
				
				if(jsonArrayTree.isJsonObject())
				{
					JsonObject arrayObject = jsonArrayTree.getAsJsonObject();
					JsonElement desc = arrayObject.get("description");
					
					weatherData[0] = desc.getAsString();
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
	 * 	This method gets the raw weather data of the cities current overall condition,
	 * 	maximum temperature, minimum temperature, and current temperature.
	 * 
	 *  The data is returned as a String array of size 4.
	 *  index 0 = overall condition
	 *  index 1 = max temp
	 *  index 2 = min temp
	 *  index 3 = current temp
	 * 
	 * 	Uses openweather API
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
				
				String arrayToJsonObject = weatherArray.toString().substring(1, weather.toString().length() - 1);
				
				JsonParser arrayParser = new JsonParser();
				
				JsonElement jsonArrayTree = arrayParser.parse(arrayToJsonObject);
				
				if(jsonArrayTree.isJsonObject())
				{
					JsonObject arrayObject = jsonArrayTree.getAsJsonObject();
					JsonElement desc = arrayObject.get("description");
					
					weatherData[0] = desc.getAsString();
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
	 * This method gets the cryptocurrency data
	 * Uses cryptonator API
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
	
	// This function is called upon the bot connecting to the channel
	public void onConnect()
	{
		String welcomeMessage = "Hello! My name is BreenBot. " +
				"I can tell you the statistics and prices of any cryptocurrency you choose!  I can also tell you the current weather for yours or anyone else's area!" +
				"  Commands: " +
				"!multiply (any number of arguments) - returns multiplied arguments, " +
				"!factorial (number) - returns factorial of input number, " + 
				"!repeat (string) - repeats the given string, ";
			
		sendMessage(this.channel, welcomeMessage);
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
	 * 
	 * Math functions:
	 * 	!ex
	 * 	!multiply
	 * 	!factorial
	 * 
	 * Miscellaneous:
	 * 	!repeat
	 * 	!changenick
	 */
    public void onMessage(String channel, String sender, String login, String hostname, String message) 
    {
		String[] args = getArgs(message);
		
		// Update UI
    	String time = new java.util.Date().toString();
		guiInterface.appendToChat(time + " - " + sender +  ": " + message);
		
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
					
					try 
					{
						String weatherDataString = "";
						
						String[] data = getRawWeatherData(city);
						
						weatherDataString = "The sky in " + city + " is " + data[0] + " and the max temperature is " + kelvinToFahrenheit(Double.parseDouble(data[1])) + 
								"F while the minimum temperature is " + kelvinToFahrenheit(Double.parseDouble(data[2])) +
								"F and the current temperature is " + kelvinToFahrenheit(Double.parseDouble(data[3])) + "F";
						
						sendMessage(channel, weatherDataString);
					} 
					catch (Exception e) 
					{
						System.out.println(e.getMessage());
					}
				}
				
				// Returns distance between two zip codes
				// Format: !distance <zipcode 1> <zipcode 2> <unit of measurement: 'k' or 'm'>
				if(getPrefixCommand(message).equalsIgnoreCase("!distance"))
				{
					double distance;
					try 
					{
						distance = getDistanceData(args[0], args[1], args[2].charAt(0));
						
						sendMessage(channel, "Distance between " + args[0] + " and " + args[1] + " is " + 
								distance + (args[2].charAt(0) == 'm' ? " miles" : " kilometers"));
					} 
					catch (Exception e) 
					{
						e.printStackTrace();
					}
				}
				
				// Gets cryptocurrency price of first argument (symbol of crypto)
				// Format: !cprice <crypto symbol(BTC, ETH, etc...)>
				if(getPrefixCommand(message).equalsIgnoreCase("!cprice"))
				{
					String symbol = args[0];
					symbol = symbol.toUpperCase();
					
					try 
					{
						double price = getCryptoPrice(symbol);
						
						if(price != -1)
						{
							String priceDataString = "The price of " + symbol + " is currently $" + price;
							
							sendMessage(channel, priceDataString);
						}
						else
						{
							String errorMessage = "Error: please make sure you are entering valid data for this command.";
							
							sendMessage(channel, errorMessage);
						}
					} 
					catch (Exception e) 
					{
						System.out.println(e.getMessage());
					}
				}

				if(getPrefixCommand(message).equalsIgnoreCase("!exchange"))
				{
					String symbol = args[0];
					symbol = symbol.toUpperCase();
					
					try 
					{
						sendMessage(channel, "Exchange rates for " + symbol + " are: " + getExchangeRates(symbol));
					} 
					catch (Exception e) 
					{
						System.out.println(e.getMessage());
					}
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
					
					sendMessage(channel, base + "^" + exponent + " = " + number);
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
					
					sendMessage(channel, "The product of your numbers is " + product);
				}
				
				// Returns factorial of first argument
				// Format: !factorial <number>
				if(getPrefixCommand(message).equalsIgnoreCase("!factorial"))
				{
					int num = Integer.parseInt(args[0]);
					
					sendMessage(channel, "Factorial of " + num + ": " + factorial(num));
				}
				/// Math function commands end
				
				
				/// Miscellaneous commands
				/// ***
				// Gets current time
				if(getPrefixCommand(message).equalsIgnoreCase("!time")) 
				{
					 String currTime = new java.util.Date().toString();
					 sendMessage(channel, sender + ": The time is now " + currTime);
				}
				
				// Quick ping command
				if(getPrefixCommand(message).equalsIgnoreCase("!ping"))
				{
					String pong = "Pong!";
					sendMessage(channel, pong);
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
					
					sendMessage(channel, returnMessage);
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
			sendMessage(channel, getName() + " is not currently taking commands/messages.");
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
        				
        				sendMessage(this.channel, weatherDataString);
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
