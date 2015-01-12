package edu.csusb;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.GLSurfaceView;
import android.util.Log;

class ObjectRenderer implements GLSurfaceView.Renderer {
	
	
	private Context context;
	private HashMap<String, GraphicObject> graphicMap = new HashMap<String, GraphicObject>();
	
	public static final float X0 = 45;
	public static final float Y0 = -8;
	
	public float angleX = X0;
	public float angleY = Y0;
	private AssetManager asset;
	
	private GraphicObject mouthObj;
	private GraphicObject headObj;
	
	private boolean firstTime = true;
	
	public boolean isReady = false;
	
	//R:239, G:208, B:207
	float light[] = { 1.0f, 1.0f, 1.0f };
	float lightAmbient[] = { 0.0f, 0.0f, 1.0f, 1.0f };
	float lightDiffuse[] = {1.0f, 0.0f, 0.0f, 1.0f };
	float lightSpecular[] = { 1.0f, 0.0f, 0.0f, 1.0f };
	float lightPosition[] = { 0.0f, 0.0f, 5.0f, 0.0f }; 
	float lightDirection[] = {0.0f, 0.0f, -1.0f};
	
	float lightShininess[] = {500.0f} ;
	
	private FloatBuffer		lightBuffer;
	private FloatBuffer		lightAmbientBuffer;
	private FloatBuffer		lightDiffuseBuffer;
	private FloatBuffer		lightSpecularBuffer;
	private FloatBuffer		lightPositionBuffer;
	private FloatBuffer		lightDirectionBuffer;
	private FloatBuffer		lightShininessBuffer;
	
	public static final int MOUTH = 0;
	public static final int HEAD = 1;

	public ObjectRenderer(Context context) {
		this.context = context;

		this.asset = this.context.getResources().getAssets();

		ByteBuffer lightBB = ByteBuffer.allocateDirect(light.length * 4);
		lightBB.order(ByteOrder.nativeOrder());
		lightBuffer = lightBB.asFloatBuffer();
		lightBuffer.put(light);
		lightBuffer.position(0);
		
		ByteBuffer ambientBB = ByteBuffer.allocateDirect(lightAmbient.length * 4);
		ambientBB.order(ByteOrder.nativeOrder());
		lightAmbientBuffer = ambientBB.asFloatBuffer();
		lightAmbientBuffer.put(lightAmbient);
		lightAmbientBuffer.position(0);
		

		ByteBuffer diffuseBB = ByteBuffer.allocateDirect(lightDiffuse.length * 4);
		diffuseBB.order(ByteOrder.nativeOrder());
		lightDiffuseBuffer = diffuseBB.asFloatBuffer();
		lightDiffuseBuffer.put(lightDiffuse);
		lightDiffuseBuffer.position(0);
		

		ByteBuffer specularBB = ByteBuffer.allocateDirect(lightSpecular.length * 4);
		specularBB.order(ByteOrder.nativeOrder());
		lightSpecularBuffer = specularBB.asFloatBuffer();
		lightSpecularBuffer.put(lightSpecular);
		lightSpecularBuffer.position(0);
		
		ByteBuffer positionBB = ByteBuffer.allocateDirect(lightPosition.length * 4);
		positionBB.order(ByteOrder.nativeOrder());
		lightPositionBuffer = positionBB.asFloatBuffer();
		lightPositionBuffer.put(lightPosition);
		lightPositionBuffer.position(0);
		
		ByteBuffer directionBB = ByteBuffer.allocateDirect(lightDirection.length * 4);
		directionBB.order(ByteOrder.nativeOrder());
		lightDirectionBuffer = directionBB.asFloatBuffer();
		lightDirectionBuffer.put(lightDirection);
		lightDirectionBuffer.position(0);
		
		ByteBuffer shinessBB = ByteBuffer.allocateDirect(lightShininess.length * 4);
		shinessBB.order(ByteOrder.nativeOrder());
		lightShininessBuffer = shinessBB.asFloatBuffer();
		lightShininessBuffer.put(lightShininess);
		lightShininessBuffer.position(0);

		long time1 = System.currentTimeMillis();

		
			
		//Load all objects
		
	
        Thread thread1 = new Thread(){
            public void run(){
            	try {
	            	while(firstTime){
	            		sleep(100);
	            	}
	            	loadMouthObjects();	
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("Error", " "+e);
                }
            }
        };
        
     
        this.updateObject(context.getString(R.string.mouth_base), MOUTH);
		this.updateObject(context.getString(R.string.head_base), HEAD);
		
		thread1.start();
		
		
		long time2 = System.currentTimeMillis() - time1;
		Log.v("TIME", new Double(time2).toString());

		
	}

	private void loadMouthObjects(){
		getObject(context.getString(R.string.mouth_AI));
		getObject(context.getString(R.string.mouth_CKGJRSTHYZ));
		getObject(context.getString(R.string.mouth_E));
		getObject(context.getString(R.string.mouth_FV));
		getObject(context.getString(R.string.mouth_MBP));
		getObject(context.getString(R.string.mouth_O));
		getObject(context.getString(R.string.mouth_ULDNT));
		getObject(context.getString(R.string.mouth_WQOo));
		
		
		isReady = true;
	}
	
	/*
	private void loadUpperHead(){
		String fileName = context.getString(R.string.upper_head);
		this.upperHeadObj = new GraphicObject(fileName);
		try {
			GraphicObjectParser.parseFile(asset.open(fileName), this.upperHeadObj );
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.upperHeadObj.load();
		
	}
	*/
	
	@Override
	public void onDrawFrame(GL10 gl) {
		
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		
		
		gl.glLoadIdentity();
		
		gl.glTranslatef(0.0f, -1.0f, -8.0f);
		
		
		//gl.glScalef(0.9f, 0.9f, 0.9f);
		
		//Rotation by surface touch
		gl.glRotatef(angleX , 0, 1, 0);
		gl.glRotatef(angleY , 1, 0, 0);
		
		
		//General adjustment
		gl.glRotatef(-90, 0, 1, 0);
		//gl.glRotatef(-90 , 1, 0, 0);

		
		this.mouthObj.draw(gl);
		this.headObj.draw(gl);
		
		firstTime = false;
		//gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPositionBuffer);
		 
		
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		
		gl.glViewport(0, 0, width, height);
		float ratio = (float) width / height;
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
	}

	
	private GraphicObject getObject(String dataFileName){
		return this.getObject(dataFileName, null);
	}
	
	private GraphicObject getObject(String dataFileName, GraphicObject cloneObj){
		GraphicObject obj = null;
		
		if(this.graphicMap.containsKey(dataFileName)){
			obj =  this.graphicMap.get(dataFileName);
			
		}else{
			try {
				if(cloneObj == null){
					obj = new GraphicObject(dataFileName);
				}else{
					obj = cloneObj.duplicate();
				}
				
				GraphicObjectParser.parseFile(asset.open(dataFileName), obj );
				this.graphicMap.put(dataFileName, obj);
			//} catch (CloneNotSupportedException e) {
		//		e.printStackTrace();
			}
			catch (IOException e){
				e.printStackTrace();
			}
		}
		return obj;
	}
	
	
	public void updateObject(String dataFileName, int objectType){
		if(objectType == HEAD){
			headObj = this.getObject(dataFileName, headObj);
			headObj.load();
		}else if(objectType == MOUTH) {
			mouthObj = this.getObject(dataFileName);
			mouthObj.load();
		}
	}
	
	
	public boolean isDrawing(){
		return this.mouthObj.isDrawing();
	}
	
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		
		//this.eyes.loadGLTexture(gl, this.context);
		//gl.glEnable(GL10.GL_TEXTURE_2D);
		
		
		//Green=41,244,24
		//Blue = 24,71,241
		//gl.glClearColor(41.0f/255.0f, 244.0f/255.0f, 24.0f/255.0f, 1.0f); // Background
		//gl.glClearColor(24.0f/255.0f, 71.0f/255.0f, 241.0f/255.0f, 1.0f); // Background
		gl.glClearColor(238/255f, 238/255f, 238/255f, 1.0f); // Background
		
		
		gl.glClearDepthf(1.0f); // Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); // Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); // The Type Of Depth Testing To Do
		gl.glEnable(GL10.GL_COLOR_MATERIAL);
		
		
		
		//Material
		gl.glShadeModel(GL10.GL_SMOOTH); // Enable Smooth Shading
	    
		gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_SPECULAR, lightSpecularBuffer);
		gl.glMaterialfv(GL10.GL_FRONT,GL10.GL_SHININESS, lightShininessBuffer);
	    gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_DIFFUSE, lightDiffuseBuffer);
	    //gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_AMBIENT, lightAmbientBuffer);
	    gl.glEnable(GL10.GL_COLOR_MATERIAL);
	    
	    
	    gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, lightBuffer);
	    gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SHININESS, lightBuffer);
	    gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightBuffer);
	    //gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lightBuffer);

	    
	    
	    //Direct light
	    gl.glEnable(GL10.GL_LIGHTING);
	    gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPositionBuffer);
	    gl.glEnable(GL10.GL_LIGHT0);

	    
	    
		// Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

		
	}

	
	
}