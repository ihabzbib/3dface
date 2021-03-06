package edu.csusb;


import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;

/**
 * Wrapper activity demonstrating the use of {@link GLSurfaceView}, a view
 * that uses OpenGL drawing into a dedicated surface.
 */
public class GLSurfaceViewActivity extends Activity implements TextToSpeech.OnInitListener {
    
	
	private TextToSpeech tts;
    private Button btnSpeak;
    private EditText txtText;
	
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // requesting to turn the title OFF
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        // making it full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    
		
		tts = new TextToSpeech(this, this);
       //Slows down the speech rate
        //tts.setSpeechRate(1.0f);
        //tts.setPitch(1.0f);
	
		
		// Create our Preview view and set it as the content of our
        // Activity
        gLSurfaceView = new TouchSurfaceView(this, tts);
        setContentView(gLSurfaceView);
        gLSurfaceView.requestFocus();
        gLSurfaceView.setFocusableInTouchMode(true);
        
        
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
        View view = layoutInflater.inflate(R.layout.main, null);
        this.addContentView(view, lp);
        
        txtText = (EditText)view.findViewById(R.id.SpeakEditText);
        txtText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				//Log.v("after textchanges", "text has changed");
			}
		});
        
        
        btnSpeak = (Button)view.findViewById(R.id.SpeakButton);
        
        
        // button on click event        
        btnSpeak.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View arg0) {
                speakOut();
                clear();
            }
            
        });
    }
	
	private void clearTextEditFocus(){
		txtText.clearFocus();
    	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    	imm.hideSoftInputFromWindow(txtText.getWindowToken(), 0);
	}
	
	private void speakOut() {
        String text = txtText.getText().toString();
        speakOut(text);
	}   
	
	private void speakOut(String text) {
		gLSurfaceView.reset(false);
        gLSurfaceView.setInputText(text);
	}

    @Override
    protected void onResume() {
    	tts = new TextToSpeech(this, this);
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onResume();
    }

    @Override
    protected void onPause() {
        clear();
        super.onPause();
    }
    
    
    @Override
    public void onStop(){
    tts.shutdown();
    	super.onStop();
    }
    
    
    public void clear(){
    	//Reset text
        //txtText.setText("");
        clearTextEditFocus();
        
        gLSurfaceView.reset(true);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.ClearText:
                clear();
                break;
            case R.id.SampleText1:
            	speakOut(this.getResources().getString(R.string.sample_hello));
            	break;
            case R.id.SampleText2:
            	speakOut(this.getResources().getString(R.string.sample_think));
            	break;
            case R.id.ExitApplication:
            	tts.shutdown();
            	finish();
            	break;
        }
        
        return false;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    private TouchSurfaceView gLSurfaceView;
    
	@Override
	public void onInit(int arg0) {
		// TODO Auto-generated method stub
		
	}
}