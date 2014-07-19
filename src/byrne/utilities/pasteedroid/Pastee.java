package byrne.utilities.pasteedroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class Pastee extends Activity implements OnClickListener{

	

	private String content;
	private String encrypt;
	private String lexer;
	private String ttl;
	private String key;
	private TextView result;
	private Button copy;
	private Button share;
	private Button quit;
	private String URL = "pastee.org/";
	private LinearLayout linProgressBar;
	
	
	
	/** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pastee);
                
        //get UI component references
        result = (TextView)findViewById(R.id.result);
    	copy   = (Button)findViewById(R.id.copy);
    	share  = (Button)findViewById(R.id.share);
    	quit   = (Button)findViewById(R.id.quit);
    	
        linProgressBar = (LinearLayout) findViewById(R.id.lin_progress_bar);
    	
    	//add listeners to buttons
    	copy.setOnClickListener(this);
    	share.setOnClickListener(this);
    	quit.setOnClickListener(this);
    	
    	//access the strings that were passed to this activity 
    	Bundle b = getIntent().getExtras();
    	key = 	  b.getString("key");
    	encrypt = b.getString("encrypt");
    	ttl =     b.getString("ttl");
    	content = b.getString("content");
    	lexer =   b.getString("lexer");
    	    	
    	//create post paste instance and execute it
    	postPaste post_paste = new postPaste();
    	post_paste.execute();
       
    }
	

    
	@Override
	public void onClick(View v) {
		
		//copy to clipboard
		if(v == copy){
			
			ClipboardManager clipboard = 
				      (ClipboardManager) getSystemService(CLIPBOARD_SERVICE); 
			clipboard.setText("https://" + result.getText().toString());
			Toast.makeText(this, "Paste URL copied to clipboard", Toast.LENGTH_SHORT).show();
		}
		
		//share via various installed applications
		if(v == share){
			
			Intent send = new Intent(Intent.ACTION_SEND);
			send.setType("text/plain");
			send.putExtra(Intent.EXTRA_TEXT, "https://" + result.getText().toString());
			startActivity(Intent.createChooser(send, "Share Paste"));
		}
		
		//if an error is shown and the user selects quit to return to previous screen
		if(v == quit){
			
			this.finish();
		}
		
	}  
    
    /***
     * postPaste is an asynchronous task that posts a paste 
     * in the background and updates the result (the paste url or a network failure)
     */
    private class postPaste extends AsyncTask<Context, String, String> {

    	
    	
  	  //perform the connection and sending in a separate thread
  		protected String doInBackground(Context... params) {
  			
  			String result = null;
  			  			
  				
  			  try {
  		           HttpClient client = new DefaultHttpClient();            
  		           String postURL = "https://pastee.org:443/submit";
  		           HttpPost post = new HttpPost(postURL);            
  		           
  		           //Add parameters to post
  		           
  		           List<NameValuePair> paramaters = new ArrayList<NameValuePair>(); 
  		            paramaters.add(new BasicNameValuePair("content", content));
  		            paramaters.add(new BasicNameValuePair("lexer", lexer));
  		            paramaters.add(new BasicNameValuePair("ttl", ttl));
  		         
  		            //add encryption only if it was selected in the previous screen
  		            if(encrypt.compareTo("on") == 0)
  		            	paramaters.add(new BasicNameValuePair("encrypt",encrypt));
  		            
  		            paramaters.add(new BasicNameValuePair("key", key));
  		            
  		            
  		            //encode the parameters 
  		            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(paramaters,HTTP.UTF_8);
  		               post.setEntity(ent); 
  		               //send post and get result
  		               HttpResponse responsePOST = client.execute(post);
  		               HttpEntity resEntity = responsePOST.getEntity();
  		               
  		               if (resEntity != null) {  
  		            	   //get the response and put it into an input stream
  		            	   InputStream is = resEntity.getContent();
  		            	   //get the line containing the paste code
  		            	   result = getKeywordLineFromStream(is , "<code>");      
  		            	   result = getTextBetweenTags(result, "code");
  		            	    		            	   
  		            	 return result;
  		               }
  		               

  		              
  				  } catch (UnsupportedEncodingException uee){
  						//Log.e("UEE","Exception caught " + uee.toString());
							//return "UEE";

  				        
  				       } catch (ClientProtocolException cpe){
  						//Log.e("CPE","Exception caught " + cpe.toString());
							//return "CPE";

  				        
  				       } catch (IOException ioe){
  						//Log.e("IOE","Exception caught " + ioe.toString());
  							//return "IOE";
  				       
  				       }
  				       
  			  //return null if no string is returned
  			return  result;
  				
  		}
    	
 	   //Display the results from the connection thread
 	   protected void onPostExecute( String response )  {
    		
 		  //set progress bar to hidden
 		  linProgressBar.setVisibility(View.GONE);
 		   
 		  //set text view to visible
 		  result.setVisibility(View.VISIBLE);

 		  
 		  //if there was a connection drop in the activity
 		  if(response == null){
 	 		  
 			  quit.setVisibility(View.VISIBLE);

 			  result.setText("There was a network error\nPlease ensure the network is connected and try again");
 		  }else{
 		  
 			  //set buttons to visible
 	 		  copy.setVisibility(View.VISIBLE);
 	 		  share.setVisibility(View.VISIBLE);
 			  
	 		  //add paste code to pastee url
	 		  URL = URL + response;
			  //Display the resulting text
			  result.setText(URL);
			  
			  //pattern we want to match and turn into a clickable link
			  Pattern pattern = Pattern.compile(URL);
			  
			  //prefix pattern with https
			  Linkify.addLinks(result,pattern, "https://");
 		  }
 		 
 	   }
 	 
 	  /**
 	   * Get a line from an input stream which contains a specific keyword
 	   * @param InputStream inputStream : the input stream
 	   * @param String keyword : the keyword that the line must contain
 	   * @return String line or null if no match is found
 	   * 
 	   * */
 	  private String getKeywordLineFromStream(InputStream inputStream, String keyword){
 		  
 		  BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
 		  String line;
 		  try {
 			  while ((line = r.readLine()) != null) {
 				  //get line with the keyword in it 
 				  if (line.contains(keyword)== true){
 					  //close streams
 					  inputStream.close();
 					  r.close();
		    	 
 					  return line;
 				  }
 			 }
 		  } catch (IOException e) {
 			  
 			  e.printStackTrace();
 		  }
 	 return null;
 	 }
 	   
 	  /**
 	   * Get the text between two <tags> in a line 
 	   *  @param String text : the line or text to parse
 	   *  @param String tag : the tag which will surround the text that is returned
 	   *  @param string parsedText : the text extracted from the tags
 	   */
 	  
 	private String getTextBetweenTags(String text, String tag){
 		

 		final Pattern pattern = Pattern.compile("<"+tag+">(.+?)</"+tag+">");
 		final Matcher matcher = pattern.matcher(text);
 		matcher.find();
 		
 		return matcher.group(1);

 		
 	 }//close gtbt
 	
	
   }//close postpaste class

}
	

	  

	

