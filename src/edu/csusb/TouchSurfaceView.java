package edu.csusb;

import java.util.Locale;
import java.util.Random;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Implement a simple rotation control.
 *
 */
class TouchSurfaceView extends GLSurfaceView {

	private static final char BLINK = '^';
	private static final char ANGRY = '$';
	private static final char SURPRISED = '@';
	private static final char YAWN = '*';
	private static final char Th = '~';
	private static final char Oo = '#';
	
	private String rendering = "";
	
	//private ArrayList<String> textBuffer = new ArrayList<String>();
	private TextToSpeech tts;
	
	private static final int offset = 45; 
	private long ttsHead = 0;
	private long animHead = 0;
	private long tail = 0;
	
	private int timer = 0;
	
	Random generator = new Random();
	
	private String inputText = "";

	
	private char[] textBuffer = new char[offset];
	private StringBuffer word = new StringBuffer();
	
	public TouchSurfaceView(Context context){
		super(context);
	}
	
	
    public TouchSurfaceView(Context context, TextToSpeech tts) {
        super(context);
        
        
        this.tts = tts;
        
        renderer = new ObjectRenderer(context);
        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        
        //Update Text buffer
        Thread producerThread = new Thread(){
            public void run(){
                
            	try {
                    while(true){
                    	sleep(200);	
                    		
                    	setTextBuffer();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("Error", " "+e);
                }
            }
        };
        
        //Update face
        Thread animationThread = new Thread(){
            public void run(){
                
            	try {
                    while(true){
    
                    	 sleep(100);	
                    	 
                    	updateFace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("Error", " "+e);
                }
            }
        };
        
      
        //Update TTS
        Thread ttsThread = new Thread(){
            public void run(){
                
            	try {
                    while(true){
                    	sleep(100);	
                    	 
                    	updateTTS();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("Error", " "+e);
                }
            }
        };
        
        
        //Start threads
        producerThread.start();
        //animationThread.start();
        ttsThread.start();
        
       
        
        
    }

 
    public void setInputText(String text){
    	
    	if(text.length() == 0){
    		return;
    	}
    	
    	
    	text = text.toLowerCase(Locale.US);
    	
    	//Add a word terminator to the last word
    	if(!isPunctuation(text.charAt(text.length() - 1))){
    		text += ".";
    	}
    	
    	
    	//Phenomes need to be characters
    	text = text.replace("th", Th + "");
    	text = text.replace("oo", Oo + "");
    	//text = text.replace(" ", " ");
    	
    	//TODO
    	//replace numbers with words
    	
    	this.inputText = text;
    }

    
     private void setTextBuffer(){
    	if(ttsHead == tail /*&& animHead == tail*/){
    		animateRandom(true);
    	}
    	else{
    		animateRandom(false);
    	}
    	
    	
    	if(this.inputText.length() > 0 && (Math.abs(renderer.angleX) > 5 || Math.abs(renderer.angleY) > 5)){
    		resetGraphics();
    	}
    	 
    	for(int i=0; i< this.inputText.length(); i++){
    		while(tail - ttsHead >= offset /*|| tail - animHead >= offset*/){
    	    	SystemClock.sleep(50);
    		}

    		int idx = (int)(tail % offset);
    		
    		this.textBuffer[idx] = this.inputText.charAt(i);
    		tail = incrementLong(tail);
    	}
    	
    	this.inputText = "";
    }

    private long incrementLong(Long l){
		if(l + 1 < Long.MAX_VALUE){
    		return l + 1;
    	}
    	return l % offset + 1;
    	
    }
    
    
    private void updateTTS(){
    	
    	//Pass the text over to the animation
    	//if(ttsHead - animHead > 0){
    		
    	//	return;
    	//}
    	
    	for(long i= ttsHead; i< tail; i++){
    		
    		
    		int idx = (int) (i%offset);
    		char ch = this.textBuffer[idx];
    		word.append(ch);
    		
    		ttsHead = incrementLong(ttsHead);
    		
    	}	

	
    	
    	if(word.length() > 0){
    	
    		int idx = word.length();
    		char ch = word.charAt(word.length() - 1);
    		
    		if(!isPunctuation(ch)){
    			for(int i=0; i<word.length(); i++){
    	    		 
    				ch = word.charAt(word.length() - i - 1);
    	    		
    	    		if(isPunctuation(ch)){
    	    			idx = word.length() - i - 1;
    	    			break;
    	    		}
    	    		
    			}
    			
    		}
    		char[] chb = new char[idx];
    		//char[] chr = new char[word.length() - idx];
    		
    		//Shift back the TTS head to the spoken index
    		ttsHead = ttsHead - word.length() + idx;
    		
    		word.getChars(0, idx, chb, 0);
    		//word.getChars(idx, word.length(), chr, 0);
    		
    		String wordStr = new String(chb);
    		String wordOrig = wordStr; 
    		
    		//Phonemes need to be characters
    		wordStr = wordStr.replaceAll(Th + "" , "th");
    		wordStr = wordStr.replaceAll(Oo + "", "oo");

    		
    		this.tts.speak(wordStr, TextToSpeech.QUEUE_ADD, null);
    		
    		for(int k = 0; k<wordOrig.length(); k++){
    			if(!tts.isSpeaking()){
    				animateFace(' ');
    				break;
    			}
    			animateFace(wordOrig.charAt(k));
    		}
    		
    		SystemClock.sleep(100);
    		
    		word = new StringBuffer();
		
    	}
    	
    }
    
    
    private void updateFace(){
    	
    	if(animHead - ttsHead > 0){
    		animateFace(' ');
    		return;
    	}
    	
    	
    	for(long i= animHead; i< tail; i++){
    		
    		int idx = (int) (i%offset);
    		char ch = this.textBuffer[idx];
    		
    		animateFace(ch);
    		
    		//Emphasis punctuations 
    		//if(isPunctuation(ch)){
    		//	SystemClock.sleep(50);
    		//}
    		
    		//Log.v("Animate Face", ch + "");
    		
    		animHead = incrementLong(animHead);
    		
    		if(animHead > tail){
    			animHead = tail;
    		}
    		
    	}
    }
    
    private void animateRandom(boolean idle){
    	
    	if(timer < 10 || !renderer.isReady){
    		//if(renderer.angleX > 5 || renderer.angleX > 5){
    		//	resetGraphics();
    		//}
    		timer++;
    		return;
    	}

    	
		int r = generator.nextInt(100);
		
		//Log.v("Random", r + "");
		
		switch(r){
			case 10:
				animateFace(BLINK);
				break;
			case 20:
				animateFace(ANGRY);
				break;
			case 15:
				animateFace(SURPRISED);
				break;
			case 5:
				animateFace(' ');
				break;	
			case 50 : case 60:
				if(idle)
					animateFace(YAWN);
				else
					animateFace(BLINK);	
				break;
			case 25: case 27:
				if(idle){
					while(Math.abs(renderer.angleX) < 25){
						renderer.angleX += (r > 26 ? 1 : -1);
						requestRender();
						SystemClock.sleep(getFrameRate(25, renderer.angleX));
					}
					if(Math.abs(renderer.angleX) > 15){
						while(Math.abs(renderer.angleX) > 3){
							renderer.angleX += -1 * (renderer.angleX / Math.abs(renderer.angleX));
							requestRender();
							SystemClock.sleep(getFrameRate(3, renderer.angleX));
						}
					}
				}else{
					animateFace(BLINK);
				}
				
				break;
			
			case 35:  case 38:
				
				if(idle){
					while(Math.abs(renderer.angleY) < 10){
						renderer.angleY += (r > 37 ? -1 : 1);
						requestRender();
						SystemClock.sleep(getFrameRate(10, renderer.angleY));
					}
					
					if(Math.abs(renderer.angleY) > 7){
						while(Math.abs(renderer.angleY) > 3){
							renderer.angleY += -1 * (renderer.angleY / Math.abs(renderer.angleY));
							requestRender();
							SystemClock.sleep(getFrameRate(3, renderer.angleY));
						}
					}
				}
				else{
					animateFace(ANGRY);
				}
				break;
			default:
				return;
		
		}
		timer = 0;
    }
    
    
    private int getFrameRate(int maxValue, float currentValue){
    	if(currentValue == 0){
    		currentValue = 0.1f;
    	}
    	int value = (int) (5 * (float)maxValue / currentValue) ;
    	if(value > 200){
    		return 200;
    	}
    	if(value < 20){
    		return 20;
    	}
    	
    	return value;
    }
    
    
    
    private void updateFrame(String filename, int objectType){
    	if(filename.compareTo(rendering) == 0){
    		SystemClock.sleep(30);
    		return;
    	}
    	
    	renderer.updateObject(filename, objectType);
    	requestRender();
    	SystemClock.sleep(70);
    	
    	rendering = filename;
    }
    
    
    @Override
    public boolean onKeyUp(int keyCode, android.view.KeyEvent event){
    	
    	
    	boolean result = super.onKeyUp(keyCode, event);
    	
    	char ch = (char)event.getUnicodeChar();
    	
    	//Refresh scene
    	animateFace(ch);
    	 
    	
    	return result;
    }
    
    
    private void animateFace(char ch){

    	
    	//Log.v("Animating", ch + "");
    	
    	switch(ch){
			case BLINK:
				updateFrame(this.getResources().getString(R.string.eyes_closed), ObjectRenderer.HEAD);
				SystemClock.sleep(200);
				updateFrame(this.getResources().getString(R.string.head_base), ObjectRenderer.HEAD);
				break;
			case ANGRY:
				updateFrame(this.getResources().getString(R.string.eyes_angrey), ObjectRenderer.HEAD);
				break;	
			case SURPRISED:
				updateFrame(this.getResources().getString(R.string.eyes_surprised), ObjectRenderer.HEAD);
				break;	
			case YAWN:
				updateFrame(this.getResources().getString(R.string.eyes_closed), ObjectRenderer.HEAD);
				updateFrame(this.getResources().getString(R.string.mouth_O), ObjectRenderer.MOUTH);
				SystemClock.sleep(1000);
				updateFrame(this.getResources().getString(R.string.mouth_base), ObjectRenderer.MOUTH);
				SystemClock.sleep(500);
				updateFrame(this.getResources().getString(R.string.head_base), ObjectRenderer.HEAD);
				break;	
			case 'a': case 'i':
				updateFrame(this.getResources().getString(R.string.mouth_AI), ObjectRenderer.MOUTH);
				break;
				
			case 'c': case 'k': case 'g': case 'j':	case 'r': 
			case 's': case Th: case 'h': case 'y': case 'z':	
				updateFrame(this.getResources().getString(R.string.mouth_CKGJRSTHYZ), ObjectRenderer.MOUTH);
				break;

			case 'e':
				updateFrame(this.getResources().getString(R.string.mouth_E), ObjectRenderer.MOUTH);
				break;
				
			case 'f': case 'v':
				updateFrame(this.getResources().getString(R.string.mouth_FV), ObjectRenderer.MOUTH);
				break;
				
			case 'm': case  'b': case 'p':
				updateFrame(this.getResources().getString(R.string.mouth_MBP), ObjectRenderer.MOUTH);
				break;
	
			case 'o':
				updateFrame(this.getResources().getString(R.string.mouth_O), ObjectRenderer.MOUTH);
				break;
				
			//UDLNT
			case 'u': case 'd': case 'l': case 'n': case 't':
				updateFrame(this.getResources().getString(R.string.mouth_ULDNT), ObjectRenderer.MOUTH);
				break;
			
			case 'w': case 'q': case Oo:
				updateFrame(this.getResources().getString(R.string.mouth_WQOo), ObjectRenderer.MOUTH);
				break;
			
			default:
				updateFrame(this.getResources().getString(R.string.head_base), ObjectRenderer.HEAD);
				updateFrame(this.getResources().getString(R.string.mouth_base), ObjectRenderer.MOUTH);
    	}
    	
    	
    }

	private boolean isPunctuation(char ch){
		char[] punctuations = {
				' ', '.', '?', '!', ':', ';', '-', '(', ')',
				'[', ']', '\\', '\''	};
		
		for(char p : punctuations){
			if(p == ch){
				return true;
			}
		}
		
		return false;
		
		
	}


	@Override 
	public boolean onTrackballEvent(MotionEvent e) {
	    renderer.angleX += e.getX() * TRACKBALL_SCALE_FACTOR;
	    renderer.angleY += e.getY() * TRACKBALL_SCALE_FACTOR;
	    requestRender();
	    return true;
	}
	
	@Override 
	public boolean onTouchEvent(MotionEvent e) {
	    float x = e.getX();
	    float y = e.getY();
	    switch (e.getAction()) {
	    case MotionEvent.ACTION_MOVE:
	        float dx = x - previousX;
	        float dy = y - previousY;
	        renderer.angleX += dx * TOUCH_SCALE_FACTOR;
	        renderer.angleY += dy * TOUCH_SCALE_FACTOR;
	        requestRender();
	    }
	    previousX = x;
	    previousY = y;
	    return true;
	}
	
	public void reset(boolean resetGraphics){
		tail = ttsHead = animHead = 0;
		textBuffer = new char[offset];
		word = new StringBuffer();
		tts.stop();
		if(resetGraphics){
			renderer.angleX = 0;
			renderer.angleY = 0;
			requestRender();
		}
		
	}
	
	private void resetGraphics(){
		if(Math.abs(renderer.angleX) > 15){
			while(Math.abs(renderer.angleX) > 5){
				renderer.angleX += -3 * (renderer.angleX / Math.abs(renderer.angleX));
				requestRender();
				SystemClock.sleep(50);
			}
		}
		
		if(Math.abs(renderer.angleY) > 7){
			while(Math.abs(renderer.angleY) > 4){
				renderer.angleY += -3 * (renderer.angleY / Math.abs(renderer.angleY));
				requestRender();
				SystemClock.sleep(50);
			}
		}
		
        animateFace(' ');
	}
	

	
    
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private final float TRACKBALL_SCALE_FACTOR = 36.0f;
    public ObjectRenderer renderer;
    private float previousX;
    private float previousY;
}
