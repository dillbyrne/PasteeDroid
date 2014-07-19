package byrne.utilities.pasteedroid;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


public class About extends Activity  {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
       
        
        
       //read in the help text file
        InputStream iFile = getResources().openRawResource(R.raw.about);
        try {
            TextView helpText = (TextView) findViewById(R.id.abouttext);
            String strFile = inputStreamToString(iFile);
            helpText.setText(strFile);
        } catch (Exception e) {
          Log.e("FILE_IO", "InputStreamToString failure", e);
        }
    }

    //converts an input stream to a string
    
    public String inputStreamToString(InputStream is) throws IOException {
        StringBuffer sBuffer = new StringBuffer();
        DataInputStream dis = new DataInputStream(is);
        String strLine = null;
        while ((strLine = dis.readLine()) != null) {
            sBuffer.append(strLine + "\n");
        }
        dis.close();
        is.close();
        return sBuffer.toString();
    }
}

