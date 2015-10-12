package model.guiComponents;


import java.util.ArrayList;
import java.util.HashMap;
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
	public static final Vector2f MESSAGE_POS = new Vector2f(0f, 0.5f);
	public static final Vector2f MESSAGE_SCALE = new Vector2f(1f,1f); 
	
	private GuiFactory guiFactory;
	private List<GuiTexture> messages;
	private long timer;
	private double messageTime;

	HashMap<String, GuiTexture> messageMap;
	
	public GuiMessages(GuiFactory gui){
		this.guiFactory = gui;
		loadImages();
		messages = new ArrayList<GuiTexture>();
		
	}

	/**
	 * 
	 */
	private void loadImages() {
		 messageMap = new HashMap<String,GuiTexture>();
		 messageMap.put("codeCompiledMessage", guiFactory.makeGuiTexture("codeCompiledMessage", MESSAGE_POS, MESSAGE_SCALE));
		 messageMap.put("codeCopied", guiFactory.makeGuiTexture("codeCopied", MESSAGE_POS, MESSAGE_SCALE));
		 messageMap.put("inGameMessage", guiFactory.makeGuiTexture("inGameMessage", MESSAGE_POS, MESSAGE_SCALE));
		 messageMap.put("laptopEmpty", guiFactory.makeGuiTexture("laptopEmpty", MESSAGE_POS, MESSAGE_SCALE));
		 messageMap.put("laptopMemoryFull", guiFactory.makeGuiTexture("laptopMemoryFull", MESSAGE_POS, MESSAGE_SCALE));
		 messageMap.put("patchComplete", guiFactory.makeGuiTexture("patchComplete", MESSAGE_POS, MESSAGE_SCALE));
		 messageMap.put("pressEToInteract", guiFactory.makeGuiTexture("pressEToInteract", MESSAGE_POS, MESSAGE_SCALE));
		 messageMap.put("unsuccessfulUnlock", guiFactory.makeGuiTexture("unsuccessfulUnlock", MESSAGE_POS, MESSAGE_SCALE));
		 
	}
	
	public void setMessage(String msg, long time){
		this.timer = System.currentTimeMillis();
		this.messages.add(messageMap.get(msg));
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
