
package src.res;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.function.BiConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import src.res.Utils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.auth.AccessToken;
import facebook4j.Post;

/**
 * @author Gamez
 *
 */
public class Utils 
{
	/**
	 * Logging class Messages / Info
	 */
	private static final Logger logger = LogManager.getLogger(Utils.class);
	private static String save_post_message;
	
	/**
	 * 
	 * @param folder_name -> When we instantiate this class we'll be able to redirect the parameters
	 * @param file_name -> Attached to folder-name / folder-content 
	 * @return will return the file path / if empty = finish program 
	 * @throws IOException
	 */
	public static Properties loadConfigFile(String folder_name, String file_name) 
			throws IOException 
	{
		
	Properties facebook_properties = new Properties();
	Path configuration_file = Paths.get(folder_name, file_name);
	facebook_properties.load(Files.newInputStream(configuration_file));
	BiConsumer<Object, Object> emptyProperty = (k, v) -> {
		if(((String)v).isEmpty()) 
		logger.error("The property '" + k + "' or '" + v + "' is empty!");
	};
		facebook_properties.forEach(emptyProperty);
		return facebook_properties;
	}
	
	public static Facebook facebook_configuration(Properties facebook_properties)
	{
		/**
		 * At first it is necessary to acquire Facebook instance to use Facebook4J.
         * You can get Facebook instance in FacebookFactory.getInstance().
		 */
		
		Facebook facebook = new FacebookFactory().getInstance();
		
		/**
		 * If App ID / App Secret / access token / access permission are listed in facebook4j.properties then, they are set in Facebook instance given back.
		 * See Configuration | Facebook4J - A Java library for the Facebook Graph API for the detail.
		 * When they are not listed, it is setable later as follows:
		 * facebook.setOAuthAppId(appId, appSecret);
		 * facebook.setOAuthPermissions(commaSeparetedPermissions);
		 * facebook.setOAuthAccessToken(new AccessToken(accessToken, null));
		 */
		
		facebook.setOAuthAppId(facebook_properties.getProperty("oauth.appId"), facebook_properties.getProperty("oauth.appSecret"));
		facebook.setOAuthPermissions(facebook_properties.getProperty("oauth.permissions"));
		facebook.setOAuthAccessToken(new AccessToken(facebook_properties.getProperty("oauth.accessToken"), null));
	
		/**
		 * If not in blank, return FaceBook Access Tokens
		 */
		return facebook;
	}
	
	public static void Print_Post_in_Wall(Post post) 
	{
	
		if(post.getStory() != null) 
		{
			System.out.println("Story: " + post.getStory() + "...");
		}
		if(post.getMessage() != null)
		{
			System.out.println("Message" + post.getMessage() + "...");
		}
		
		System.out.println(". . . . . . . . . . . . . . . . . . . .");	
	}
	
	public static void posting_Status(String message, Facebook facebook)
	{
		try
		{
			facebook.postStatusMessage(message);
		}
		catch (FacebookException e)
		{
			logger.error("Error at: " + e);
		}
	}
	
	public static void posting_Link(String link, Facebook facebook)
	{
		try
		{
			facebook.postLink( new URL(link) );
		}
		catch (MalformedURLException e)
		{
			logger.error("Error at: " + e);
		}
		catch (FacebookException e)
		{
			logger.error("Error at: " + e);
		}
	}
	
	public static String Save_Post(String file_name, List<Post> posts)
	{
		File facebook_file = new File(file_name + ".txt");
		
		try 
		{
			if(!facebook_file.exists())
			{
				facebook_file.createNewFile();
			}
			
			FileOutputStream fops = new FileOutputStream(facebook_file);
			
			for (Post post : posts)
			{
				if(post.getMessage() != null)
					save_post_message += post.getStory() + "\n";
				if(post.getMessage() != null)
					save_post_message += post.getMessage() + "\n";
				save_post_message += ". . . . . . . . . . . . . . . . . . . . \n";
			fops.write(save_post_message.getBytes());
			fops.close();
			
			logger.info("Save files at: " + facebook_file.getName() + "...");
			logger.info("Save files at: " + facebook_file.getName() + "...");
			}
		}
		catch (IOException e)
		{
			logger.error(e);
		}
		
		return facebook_file.getName();
		
	}
	
}