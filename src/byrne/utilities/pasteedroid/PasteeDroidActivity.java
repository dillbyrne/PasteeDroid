package byrne.utilities.pasteedroid;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class PasteeDroidActivity extends Activity implements OnClickListener , OnItemSelectedListener{
    
	//Create Ui Components
	
	private EditText content;
	private EditText password;
	private Spinner lexer;
	private Spinner ttl;
	private CheckBox encrypt;
	private Button paste;
	private String time; // selected time to live
	private String lex; // selected lexer
	private String the_paste ; //the paste entered
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        //get references to ui components
        
        content  = (EditText) findViewById(R.id.content);
        password = (EditText) findViewById(R.id.password);
        lexer    = (Spinner)  findViewById(R.id.lexer);
        ttl      = (Spinner)  findViewById(R.id.ttl);
        encrypt  = (CheckBox) findViewById(R.id.encrypt);
        paste    = (Button)   findViewById(R.id.submit);
         
        
        //add listeners
        paste.setOnClickListener(this);
        lexer.setOnItemSelectedListener(this);
        
        //set Text only as the default lexer 
        lexer.setSelection(15, true);
            
        
        
        //handle share intent to allow users to send
        //text via this application
        Intent intent = getIntent();
        
        if (savedInstanceState == null && intent != null) {
           
            if (intent.getAction().equals(Intent.ACTION_SEND)) {
                the_paste = intent.getStringExtra(Intent.EXTRA_TEXT);
                content.setText(the_paste);          
            }
        }
        
        
    }//end onCreate


	@Override
	public void onClick(View v) {
		
		//if a paste has been entered
		if(v == paste && content.getText().toString().length() != 0){
			
			if (isOnline() == true){
				
				//get ttl and the paste
				time = "" + getTimeToLive(ttl.getSelectedItem().toString());
				the_paste = content.getText().toString();
				
				
				//if a password has been specified and encrypt is selected
				if(encrypt.isChecked() == true && password.getText().toString().length() != 0){
					
										
					//pass parameter to sending activity and call it
					//bundle post parameters and add them to the child activity
					Bundle b=new Bundle();
					b.putString("content", the_paste);
					b.putString("lexer", lex);
					b.putString("ttl", time);
					b.putString("encrypt", "on");
					b.putString("key", password.getText().toString());
					
					Intent intent = new Intent(PasteeDroidActivity.this, Pastee.class);
					intent.putExtras(b);
			        startActivity(intent);
					
				}
				//no password has been entered but encrypt is checked
				else if(encrypt.isChecked() == true && password.getText().toString().length() == 0){
					Toast.makeText(this, "please enter a password or unselect encrypt", Toast.LENGTH_SHORT).show();
				
				}
				//a password has been entered but encrypt is not checked
				else if(encrypt.isChecked() == false && password.getText().toString().length() != 0){
					Toast.makeText(this, "please select encrypt or remove password ", Toast.LENGTH_SHORT).show();
	
				}
				else{ // no password and encrypt is not checked
					
					//pass parameter to sending activity and call it
					//bundle post parameters and add them to the child activity
					Bundle b=new Bundle();
					b.putString("content", the_paste);
					b.putString("lexer", lex);
					b.putString("ttl", time);
					b.putString("encrypt", "off");
					b.putString("key", "");
					
					Intent intent = new Intent(PasteeDroidActivity.this, Pastee.class);
					intent.putExtras(b);
			        startActivity(intent);
						
				}
			}
			else{
				//Turn on networking message
				Toast.makeText(this, "Please enable the mobile network or wifi", Toast.LENGTH_SHORT).show();
			}
		}
		else{
			//if paste is empty show message
			Toast.makeText(this, "Paste must not be empty", Toast.LENGTH_SHORT).show();
		}
		
		
		
	}
	
	/**
	 * Calculates the time to live based on the input 
	 * @param String ttl : Time to live (an hour , a day , a week , a month or a year ) 
	 * @return int Seconds : The number of seconds  
	 * 
	 * */
	private int getTimeToLive(String ttl){
		 
		// A Year
		if(ttl.equals(getResources().getString(R.string.year)) == true){
			return 60 * 60 * 24 * 365 ;
		}
		// A Day
		if(ttl.equals(getResources().getString(R.string.day)) == true){
			return 60 * 60 * 24  ;
		}
		// A month
		if(ttl.equals(getResources().getString(R.string.month)) == true){
			return 60 * 60 * 24 * 30 ;
		} 
		// A week
		if(ttl.equals(getResources().getString(R.string.week)) == true){
			return 60 * 60 * 24 * 7 ;
		}
		// An Hour
		else{
			return 60 * 60;
		} 
		
	}


	@Override
	public void onItemSelected(AdapterView<?> arg0, View v, int pos,
			long id) {
		
		//Get the correct Lexer codes based on the selected item
		
		
		//common lexers
		
		//C
		if(pos == 0){
			lex = getResources().getString(R.string.C1);
		}
		
		//C#
		if(pos == 1){
			lex = getResources().getString(R.string.Csharp1);
		}
		
		//C++
		if(pos == 2){
			lex = getResources().getString(R.string.Cplusplus1);
		}
		
		//CSS
		if(pos == 3){
			lex = getResources().getString(R.string.CSS1);
		}
		
		//Diff
		if(pos == 4){
			lex = getResources().getString(R.string.Diff1);
		}
		
		//HTML
		if(pos == 5){
			lex = getResources().getString(R.string.HTML1);
		}
		
		//Java
		if(pos == 6){
			lex = getResources().getString(R.string.Java1);
		}
		
		//JavaScript
		if(pos == 7){
			lex = getResources().getString(R.string.JavaScript1);
		}
		
		//ObjectiveC
		if(pos == 8){
			lex = getResources().getString(R.string.ObjectiveC1);
		}
		
		//PHP
		if(pos == 9){
			lex = getResources().getString(R.string.PHP1);
		}
		
		//Perl
		if(pos == 10){
			lex = getResources().getString(R.string.Perl1);
		}
		
		//Python
		if(pos == 11){
			lex = getResources().getString(R.string.Python1);
		}
		
		//Ruby
		if(pos == 12){
			lex = getResources().getString(R.string.Ruby1);
		}
		
		//SQL
		if(pos == 13){
			lex = getResources().getString(R.string.SQL1);
		}
		
		//TeX
		if(pos == 14){
			lex = getResources().getString(R.string.TeX1);
		}
		
		//Text Only
		if(pos == 15){
			lex = getResources().getString(R.string.TextOnly1);
		}
		
		//XML
		if(pos == 16){
			lex = getResources().getString(R.string.XML1);
		}
		
		
		// Other Lexer Codes
		
		//ActionScript
		if(pos == 17){
			lex = getResources().getString(R.string.ActionScript1);
		}
		
		//Apache Conf
		if(pos == 18){
			lex = getResources().getString(R.string.ApacheConf1);
		}
		
		//BBCode
		if(pos == 19){
			lex = getResources().getString(R.string.BBCode1);
		}
		//Bash
		if(pos == 20){
			lex = getResources().getString(R.string.Bash1);
		}
		//Batchfile
		if(pos == 21){
			lex = getResources().getString(R.string.Batchfile1);
		}
		
		//Befunge
		if(pos == 22){
			lex = getResources().getString(R.string.Befunge1);
		}
		
		//Boo
		if(pos == 23){
			lex = getResources().getString(R.string.Boo1);
		}

		//Brainfuck
		if(pos == 24){
			lex = getResources().getString(R.string.Brainfuck1);
		}
		
		//Common Lisp
		if(pos == 25){
			lex = getResources().getString(R.string.Common_Lisp1);
		}
		
		//D
		if(pos == 26){
			lex = getResources().getString(R.string.D1);
		}
		
		//Darcs Patch
		if(pos == 27){
			lex = getResources().getString(R.string.Darcs_Patch1);
		}
		
		//Debian Control file
		if(pos == 28){
			lex = getResources().getString(R.string.Debian_Control_file1);
		}
	
		//Debian Sourcelist
		if(pos == 29){
			lex = getResources().getString(R.string.Debian_Sourcelist1);
		}
		
		//Delphi
		if(pos == 30){
			lex = getResources().getString(R.string.Delphi1);
		}
		
		//Django/Jinja
		if(pos == 31){
			lex = getResources().getString(R.string.Django_Jinja1);
		}
		
		//Dylan
		if(pos == 32){
			lex = getResources().getString(R.string.Dylan1);
		}
		
		//ERB
		if(pos == 33){
			lex = getResources().getString(R.string.ERB1);
		}
		
		//Erlang
		if(pos == 34){
			lex = getResources().getString(R.string.Erlang1);
		}
		
		//Fortran
		if(pos == 35){
			lex = getResources().getString(R.string.Fortran1);
		}
		
		//Gas
		if(pos == 36){
			lex = getResources().getString(R.string.GAS1);
		}

		//Genshi
		if(pos == 37){
			lex = getResources().getString(R.string.Genshi1);
		}
		
		//Genshi Text
		if(pos == 38){
			lex = getResources().getString(R.string.Genshi_Text1);
		}
		
		//Gettext Catalog 
		if(pos == 39){
			lex = getResources().getString(R.string.Gettext_Catalog1);
		}
		
		//Groff
		if(pos == 40){
			lex = getResources().getString(R.string.Groff1);
		}

		//Haskell
		if(pos == 41){
			lex = getResources().getString(R.string.Haskell1);
		}
		
		//INI
		if(pos == 42){
			lex = getResources().getString(R.string.INI1);
		}
		
		//IRC logs
		if(pos == 43){
			lex = getResources().getString(R.string.IRC_logs1);
		}
		
		//Io
		if(pos == 44){
			lex = getResources().getString(R.string.Io1);
		}
		
		//Java Server Page
		if(pos == 45){
			lex = getResources().getString(R.string.Java_Server_Page1);
		}
		
		//LLVM
		if(pos == 46){
			lex = getResources().getString(R.string.LLVM1);
		}
		
		//Literate Haskell
		if(pos == 47){
			lex = getResources().getString(R.string.Literate_Haskell1);
		}
		
		//Logtalk
		if(pos == 48){
			lex = getResources().getString(R.string.Logtalk1);
		}
		
		//Lua
		if(pos ==49){
			lex = getResources().getString(R.string.Lua1);
		}

		//MOOCode
		if(pos == 50){
			lex = getResources().getString(R.string.MOOCode1);
		}
		
		//Makefile
		if(pos == 51){
			lex = getResources().getString(R.string.Makefile1);
		}
		
		//Mako
		if(pos == 52){
			lex = getResources().getString(R.string.Mako1);
		}
		
		//Matlab
		if(pos == 53){
			lex = getResources().getString(R.string.Matlab1);
		}
		
		//Matlab Session
		if(pos == 54){
			lex = getResources().getString(R.string.Matlab_Session1);
		}
		
		//MiniD
		if(pos == 55){
			lex = getResources().getString(R.string.MiniD1);
		}
		
		//MoinMoin / Trac Wiki
		if(pos == 56){
			lex = getResources().getString(R.string.MoinMoin_Trac_Wiki_markup1);
		}
	
		//MuPAD
		if(pos == 57){
			lex = getResources().getString(R.string.MuPAD1);
		}
		
		//mySQL
		if(pos == 58){
			lex = getResources().getString(R.string.mySQL1);
		}
		
		//Myghty
		if(pos == 59){
			lex = getResources().getString(R.string.Myghty1);
		}
		
		//NumPy
		if(pos == 60){
			lex = getResources().getString(R.string.NumPy1);
		}
		
		//OCaml
		if(pos == 61){
			lex = getResources().getString(R.string.OCaml1);
		}
		
		//Python 3
		if(pos == 62){
			lex = getResources().getString(R.string.Python_31);
		}
		
		//Python Traceback
		if(pos == 63){
			lex = getResources().getString(R.string.Python_Traceback1);
		}
		
		//Python console session
		if(pos == 64){
			lex = getResources().getString(R.string.Python_console_session1);
		}
		
		//RHTML
		if(pos == 65){
			lex = getResources().getString(R.string.RHTML1);
		}
		
		//Raw token data
		if(pos == 66){
			lex = getResources().getString(R.string.Raw_token_data1);
		}
		
		//Redcode
		if(pos == 67){
			lex = getResources().getString(R.string.Redcode1);
		}
		
		//Ruby irb session
		if(pos == 68){
			lex = getResources().getString(R.string.Ruby_irb_session1);
		}
		
		//S
		if(pos == 69){
			lex = getResources().getString(R.string.S1);
		}
		
		//Scheme
		if(pos == 70){
			lex = getResources().getString(R.string.Scheme1);
		}
		
		//Smalltalk
		if(pos == 71){
			lex = getResources().getString(R.string.Smalltalk1);
		}
		
		//Smarty
		if(pos == 72){
			lex = getResources().getString(R.string.Smarty1);
		}
		
		//Squid Conf
		if(pos == 73){
			lex = getResources().getString(R.string.SquidConf1);
		}
		
		//Tcl1
		if(pos == 74){
			lex = getResources().getString(R.string.Tcl1);
		}
		
		//Tcsh
		if(pos == 75){
			lex = getResources().getString(R.string.Tcsh1);
		}
		
		//VB.net
		if(pos == 76){
			lex = getResources().getString(R.string.VB_net1);
		}
		
		//VimL
		if(pos == 77){
			lex = getResources().getString(R.string.VimL1);
		}
		
		//XSLT
		if(pos == 78){
			lex = getResources().getString(R.string.XSLT1);
		}
		
		//C Objdump
		if(pos == 79){
			lex = getResources().getString(R.string.c_objdump1);
		}
		
		//CPP Objdump
		if(pos == 80){
			lex = getResources().getString(R.string.cpp_objdump1);
		}
		
		//D Objdump
		if(pos == 81){
			lex = getResources().getString(R.string.d_objdump1);
		}
		
		//objdump
		if(pos == 82){
			lex = getResources().getString(R.string.objdump1);
		}
		
		//reStructuredText
		if(pos == 83){
			lex = getResources().getString(R.string.reStructuredText1);
		}
		

		//Combo Lexers
		
		//CSS+Django/Jinja
		if(pos == 84){
			lex = getResources().getString(R.string.CSS_Django_Jinja1);
		}
		
		//CSS+Genshi Text
		if(pos == 85){
			lex = getResources().getString(R.string.CSS_Genshi_Text1);
		}
		
		//CSS+Mako
		if(pos == 86){
			lex = getResources().getString(R.string.CSS_Mako1);
		}
		
		//CSS+Myghty
		if(pos == 87){
			lex = getResources().getString(R.string.CSS_Myghty1);
		}
		
		//CSS+PHP
		if(pos == 88){
			lex = getResources().getString(R.string.CSS_PHP1);
		}
		
		//CSS+Ruby
		if(pos == 89){
			lex = getResources().getString(R.string.CSS_Ruby1);
		}
		
		//CSS+Smarty
		if(pos == 90){
			lex = getResources().getString(R.string.CSS_Smarty1);
		}
		
		//HTML+Django/Jinja
		if(pos == 91){
			lex = getResources().getString(R.string.HTML_Django_Jinja1);
		}
		
		//HTML+Genshi
		if(pos == 92){
			lex = getResources().getString(R.string.HTML_Genshi1);
		}
		
		//HTML+Mako
		if(pos == 93){
			lex = getResources().getString(R.string.HTML_Mako1);
		}
		
		//HTML+Myghty
		if(pos == 94){
			lex = getResources().getString(R.string.HTML_Myghty1);
		}
		
		
		//HTML+PHP
		if(pos == 95){
			lex = getResources().getString(R.string.HTML_PHP1);
		}
		
		//HTML+Smarty
		if(pos == 96){
			lex = getResources().getString(R.string.HTML_Smarty1);
		}
		
		//JavaScript+Django/Jinja
		if(pos == 97){
			lex = getResources().getString(R.string.JavaScript_Django_Jinja1);
		}
		
		//JavaScript+Genshi Text
		if(pos == 98){
			lex = getResources().getString(R.string.JavaScript_Genshi_Text1);
		}

		//JavaScript+Mako
		if(pos == 99){
			lex = getResources().getString(R.string.JavaScript_Mako1);
		}

		//JavaScript+Myghty
		if(pos == 100){
			lex = getResources().getString(R.string.JavaScript_Myghty1);
		}
		
		//JavaScript+PHP
		if(pos == 101){
			lex = getResources().getString(R.string.JavaScript_PHP1);
		}

		//JavaScript+Ruby
		if(pos == 102){
			lex = getResources().getString(R.string.JavaScript_Ruby1);
		}
		
		//JavaScript+Smarty
		if(pos == 103){
			lex = getResources().getString(R.string.JavaScript_Smarty1);
		}
		
		//XML+Django/Jinja
		if(pos == 104){
			lex = getResources().getString(R.string.XML_Django_Jinja1);
		}
		
		//XML+Mako
		if(pos == 105){
			lex = getResources().getString(R.string.XML_Mako1);
		}
		
		//XML+Myghty
		if(pos == 106){
			lex = getResources().getString(R.string.XML_Myghty1);
		}
		
		//XML+PHP
		if(pos == 107){
			lex = getResources().getString(R.string.XML_PHP1);
		}
		
		//XML+Ruby
		if(pos == 108){
			lex = getResources().getString(R.string.XML_Ruby1);
		}
		
		//XML+Smarty
		if(pos == 109){
			lex = getResources().getString(R.string.XML_Smarty1);
		}
		  
	}


	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// Does not apply 
		
	}
	
	/**
	 * Checks for 3G or WiFi connectivity 
	 * @return boolean : true or false
	 * */
	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.app_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.about:
	            	
				//Start the about activity
				Intent intent = new Intent(PasteeDroidActivity.this, About.class);
		        startActivity(intent);
			
	            return true;

	        case R.id.more:
	        	
	        	String url = "https://play.google.com/store/apps/developer?id=dillbyrne";
	        	Intent i = new Intent(Intent.ACTION_VIEW);
	        	i.setData(Uri.parse(url));
	        	startActivity(i);
	        	
	            return true;
	            
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
}
