package model.guiComponents;


import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import model.factories.GuiFactory;
import model.textures.GuiTexture;

/**
 * 
 * @author Divya
 *
 */
public class GuiMessages {
	private static final Vector2f MESSAGE_POS = new Vector2f(0f, 0.5f);
	private static final Vector2f MESSAGE_SCALE = new Vector2f(1f,1f); 
	
	private GuiFactory guiFactory;
	private List<GuiTexture> messages;
	private long timer;
	private double messageTime;
	
	public GuiMessages(GuiFactory gui){
		this.guiFactory = gui;
		messages = new ArrayList<GuiTexture>();
	}
	
	public void setMessage(String msg, long time){
		this.timer = System.currentTimeMillis();
		this.messages.add(guiFactory.makeGuiTexture(msg, MESSAGE_POS, MESSAGE_SCALE));
		this.messageTime = time;
	}
	
	private void updateMessages() {
		if(!this.messages.isEmpty()){
			long currentTime = System.currentTimeMillis();
			if(currentTime - this.timer >= this.messageTime){
				this.messages.clear();
			}
		}
	}
	
	public List<GuiTexture> getMessages(){
		updateMessages();
		return this.messages;
	}
}
