package edu.csusb;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import android.text.format.Time;
import android.util.Log;



public class GraphicObjectParser {
	
	
	private static final  int IN_VERTICES = 1;
	private static final  int IN_NORMALS  = 2;
	private static final  int IN_INDICES  = 3;
	private static final  int IN_TEXTURE  = 4;
	private static final int IN_MATERIALS = 5;
	
	private static final String OBJECT = "object";
	private static final String VERTICES = "vertices";
	private static final String NORMALS = "normals";
	private static final String INDICES = "indices";
	private static final String TEXTURE = "texture";
	private static final String MATERIALS = "materials";
	private static final String OPENTAG = "{";
	private static final String CLOSETAG = "}";
	
	private static String getAttributeValue(String line) throws IOException{
		String[] tokens = line.split(":");
		if(tokens.length == 2){
			return (tokens[1]).trim();
		}
		throw new IOException("Cannot read value from string: " + line);
	}
	
	
	
	public static void parseFile(InputStream input, GraphicObject object ) {
		
		int mode = 0;
		BufferedReader reader ;
		
		try {
			reader = new BufferedReader(new InputStreamReader(input));
			
			for (String line; (line = reader.readLine()) != null;) {
				
				//Read Object
				if(line.startsWith(OBJECT)){
					//String name = this.getAttributeValue(line);		
					//object.name = name;
				}	
				//Read Vertices
				else if(line.startsWith(VERTICES)){
					
					//New object
					if(object.vertexBuffer == null){
						String size = GraphicObjectParser.getAttributeValue(line);
						object.verticesSize = Integer.parseInt(size);
						ByteBuffer vbb = ByteBuffer.allocateDirect(object.verticesSize * 3 * 4);
						vbb.order(ByteOrder.nativeOrder());
						object.vertexBuffer = vbb.asFloatBuffer();
					}

					mode = IN_VERTICES;

					
				}
				
				//Read Normals
				else if(line.startsWith(NORMALS)){
					
					if(object.normalsBuffer == null){
						String size = GraphicObjectParser.getAttributeValue(line);
						ByteBuffer nbb = ByteBuffer.allocateDirect(Integer.parseInt(size) * 3 * 4);
						nbb.order(ByteOrder.nativeOrder());
						object.normalsBuffer = nbb.asFloatBuffer();
					}
					
					mode = IN_NORMALS;
					
				}
				
				else if(line.startsWith(INDICES)){
					
					if(object.indexBuffer  == null){
						String size = GraphicObjectParser.getAttributeValue(line);
						ByteBuffer ibb = ByteBuffer.allocateDirect(Integer.parseInt(size) * 3 * 2);
						ibb.order(ByteOrder.nativeOrder());
						object.indexBuffer = ibb.asShortBuffer();
	
					}

					mode = IN_INDICES;

					
				}
				
				else if(line.startsWith(MATERIALS)){
					
					if(object.materials  == null){
						String size = GraphicObjectParser.getAttributeValue(line);
						object.materials = new Material[Integer.parseInt(size)];
						for(int i=0; i < object.materials.length; i++){
							object.materials[i] = new Material("");
						}
					}

					mode = IN_MATERIALS;

					
				}
				/*
				else if(line.startsWith(TEXTURE)){
					String size = this.getAttributeValue(line);
					object.texture = new short[Integer.parseInt(size) * 3];
					mode = IN_TEXTURE;
				}
				*/
				else if(line.startsWith(OPENTAG)) {
					//idx++;
				}
				else if(line.startsWith(CLOSETAG)){
					if(mode == 0 ){
						//result.add(object);
					}
					else{
						mode = 0;
					}
					
				}
			
				else if(mode > 0){
					String[] tokens = line.split(" ");
					int  line_number = Integer.parseInt(tokens[0].trim());
					int  position = line_number * 3; //use it for the Buffers
					String[] cords = tokens[1].split(",");
					
					switch(mode){
						case IN_VERTICES:
							for(String co : cords){
								//object.vertices[idx++] = Float.parseFloat(co.trim());
								object.vertexBuffer.put(position++, Float.parseFloat(co.trim()));
								
								//idx++;
							}
							break;
						case IN_NORMALS:
							for(String co : cords){
								//object.normals[idx++] = Float.parseFloat(co.trim());
								object.normalsBuffer.put(position++, Float.parseFloat(co.trim()));
								//idx++;
								
							}
							break;
							
						case IN_INDICES:
							for(String co : cords){
								//object.indices[idx++] = Short.parseShort(co.trim());
								//Log.v(new Integer(idx).toString(), );
								//object.indexBuffer.put(idx++, Short.parseShort(co.trim()));
								object.indexBuffer.put(position++, Short.parseShort(co.trim()));
							}
							break;
						
						case IN_MATERIALS:
							object.materials[line_number].length = Integer.parseInt(cords[0]);
							for(int i=0; i < 4; i++){
								object.materials[line_number].rgb[i] = Float.parseFloat(cords[i+1]);
							}
							
							break;	
						/*	
						case IN_TEXTURE:
							for(String co : cords){
								object.texture[idx++] = Short.parseShort(co.trim());
							}
							break;
						*/
							default:
								break;
					}
					
				}
				
			}			
			
			reader.close();
		} catch (IOException e) {
			Log.v("Error: ","We have an error");
		}
		
	}

}
