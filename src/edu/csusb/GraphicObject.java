package edu.csusb;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import edu.csusb.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

/**
 */
public class GraphicObject implements Cloneable{

	public FloatBuffer	vertexBuffer;
	public FloatBuffer 	normalsBuffer;
	public FloatBuffer	textureBuffer;
    
	
	public FloatBuffer eyesVertexBuffer;
	public FloatBuffer eyesNormalsBuffer;
	
	public ShortBuffer	indexBuffer;
    public Material[] materials;
	
    public int verticesSize;
	//int[] upAxis;
	public String name;
	
	
	private boolean isDrawing = false;
	
	
	public GraphicObject duplicate(){
		GraphicObject obj = new GraphicObject(this.name);
		
		ByteBuffer bbv = ByteBuffer.allocateDirect(this.vertexBuffer.limit() * 4);
		bbv.order(ByteOrder.nativeOrder());
		obj.vertexBuffer = bbv.asFloatBuffer();
		obj.vertexBuffer.put(this.vertexBuffer);
		
		ByteBuffer bbn = ByteBuffer.allocateDirect(this.normalsBuffer.limit()* 4);
		bbn.order(ByteOrder.nativeOrder());
		obj.normalsBuffer = bbn.asFloatBuffer();
		obj.normalsBuffer.put(this.normalsBuffer);

		ByteBuffer bbi = ByteBuffer.allocateDirect(this.indexBuffer.limit() * 2);
		bbi.order(ByteOrder.nativeOrder());
		obj.indexBuffer = bbi.asShortBuffer();
		
		
		Log.v("LIMIT ", obj.indexBuffer.limit() + "  " + this.indexBuffer.limit());
		
		obj.indexBuffer.put(this.indexBuffer);
		
		
		
		
		obj.materials = new Material[this.materials.length];
		
		for(int i=0; i<this.materials.length; i++){
			obj.materials[i] = this.materials[i];
		}

		return obj;
	}
	
	@Override
	public GraphicObject clone() throws CloneNotSupportedException {
		
		
		GraphicObject obj= (GraphicObject)super.clone(); //new GraphicObject();
		
		
		ByteBuffer bb = ByteBuffer.allocateDirect(this.vertexBuffer.limit() * 4);
		bb.order(ByteOrder.nativeOrder());
		obj.vertexBuffer = bb.asFloatBuffer();
		obj.vertexBuffer.put(this.vertexBuffer);
		
		
		bb = ByteBuffer.allocateDirect(this.normalsBuffer.limit()* 4);
		bb.order(ByteOrder.nativeOrder());
		obj.normalsBuffer = bb.asFloatBuffer();
		obj.normalsBuffer.put(this.normalsBuffer);

		
		bb = ByteBuffer.allocateDirect(this.indexBuffer.limit() * 2);
		bb.order(ByteOrder.nativeOrder());
		obj.indexBuffer = bb.asShortBuffer();
		obj.indexBuffer.put(this.indexBuffer);
		
		for(int i=0; i<this.materials.length; i++){
			obj.materials[i] = this.materials[i];
		}

		
		//Reading the buffers have changed the position of their pointers. 
		this.load();
		return obj;
	}
	
	
	public GraphicObject(String name){
		//upAxis = new int[] { 0, 0, 1 };
		this.name = name;
	}
	
	/*
	 * Class Constructor 
	 */
	public void load(){
		isDrawing = true;
		
		vertexBuffer.position(0);
		normalsBuffer.position(0);
		indexBuffer.position(0);
	}
	


	/**
	 * Renders the graphics
	 * @param gl
	 */
	public void draw(GL10 gl) {
		isDrawing = true;
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		
		
		
		//long time1 = System.currentTimeMillis();
		
		//GLU.gluLookAt(gl, 5, 0, 3, 0, 0, 0, upAxis[0], upAxis[1], upAxis[2]);

		gl.glFrontFace(GL10.GL_CW);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		
		gl.glNormalPointer(GL10.GL_FLOAT, 0, normalsBuffer);
		
		//gl.glTexCoordPointer(2, GL10.GL_SHORT, 0, getTextureBuffer(materials[1]));		

		// gl.glColorPointer(4, GL10.GL_FIXED, 0, colorBuffer);
		
		
		//gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, getTextureBuffer(texture1));
		
		int offset =0;
		for(int i=0; i<materials.length; i++){
			gl.glColor4f(materials[i].rgb[0], materials[i].rgb[1], materials[i].rgb[2], materials[i].rgb[3]);
			
			int length = materials[i].length;
			//!!!!Super important
			indexBuffer.position(offset);

			//int mode = GL10.GL_TRIANGLES;
			
			int mode = GL10.GL_LINES;
					
			gl.glDrawElements(mode, length * 3, GL10.GL_UNSIGNED_SHORT, indexBuffer);
			
			
			//gl.glDrawElements(GL10.GL_TRIANGLES, length * 3, GL10.GL_UNSIGNED_SHORT, indexBuffer);
			
			offset += length * 3;
			
			
		}
		
		//!!!!Super important
		indexBuffer.position(0);
    	
		//gl.glDrawArrays(GL10.GL_TRIANGLES, 0, verticesSize);
		//R:239, G:208, B:207 Human skin
		//gl.glColor4f(249.0f/255.0f, 182.0f/255.0f, 157.0f/255f, 1);
    	//gl.glDrawElements(GL10.GL_TRIANGLES, indexBuffer.limit(), GL10.GL_UNSIGNED_SHORT, indexBuffer);
		
		
		//this.drawEyes(gl);
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
		

		//long time2 = System.currentTimeMillis() - time1;
		//Log.v("TIME2: " + this.name, Double.valueOf(time2).toString());
			
		isDrawing = false;
		
	}

	
	
	
	public boolean isDrawing(){
		return this.isDrawing;
	}
    
}






