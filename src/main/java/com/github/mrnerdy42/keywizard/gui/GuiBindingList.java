package com.github.mrnerdy42.keywizard.gui;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import com.github.mrnerdy42.keywizard.util.KeybindUtils;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.GuiScrollingList;

public class GuiBindingList extends GuiScrollingList {
	
	private GuiKeyWizard parent;
	private KeyBinding[] bindings;
	
	private String searchText;
	private String selectedCategory;
	
	private KeyBinding selectedKeybind;
	private int selectedKeybindId;

	public GuiBindingList(GuiKeyWizard parent, int left, int bottom, int width, int height, int entryHeight) {
		     //Minecraft client, int width, int height, int top, int bottom, int left, int entryHeight, int screenWidth, int screenHeight
		super(parent.getClient(), width, height, bottom - height, bottom, left, entryHeight, parent.width, parent.height);
		
		this.parent = parent;
		this.bindings = KeybindUtils.ALL_BINDINGS;
		this.searchText = this.parent.getSearchText();
		this.selectedCategory = this.parent.getSelectedCategory();
		this.selectKeybind(0);
	}

	@Override
	protected void elementClicked(int index, boolean doubleClick) {
		this.selectKeybind(index);
	}

	@Override
	protected boolean isSelected(int index) {
		return this.selectedKeybindId == index;
	}

	@Override
	protected void drawBackground() {

	}

	@Override
	protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
		FontRenderer fontRender = this.parent.getFontRenderer();
		KeyBinding currentBinding = this.bindings[slotIdx];
		
		fontRender.drawStringWithShadow(I18n.format(currentBinding.getKeyDescription()), this.left + 3 , slotTop, 0xFFFFFF);
		
		fontRender.drawStringWithShadow("("+I18n.format(currentBinding.getKeyCategory())+")", this.left + 3, slotTop + fontRender.FONT_HEIGHT + 2, 0x444444);
		int color = 0;
		if ( currentBinding.getKeyCode() == 0 || KeybindUtils.getNumConficts(currentBinding) > 0) {
			color = 0x993333;
		} else if(!currentBinding.isSetToDefaultValue()){
			color = 0x339933;
		} else {
			color = 0x999999;
		}
	    //int len = (I18n.format("gui.key")+": ").length() * 5;
		//fontRender.drawStringWithShadow(I18n.format("gui.key")+": ", this.left + 3 , slotTop + fontRender.FONT_HEIGHT * 2 + 3, 0x999999);
		fontRender.drawStringWithShadow(currentBinding.getDisplayName(), this.left + 3, slotTop + fontRender.FONT_HEIGHT * 2 + 3, color);
	}
	
	@Override
	public void handleMouseInput(int mouseX, int mouseY) throws IOException {
		super.handleMouseInput(mouseX, mouseY);
		
        int listLength     = this.getSize();
        int scrollBarWidth = 6;
        int scrollBarRight = this.left + this.listWidth;
        int scrollBarLeft  = scrollBarRight - scrollBarWidth;
        int entryLeft      = this.left;
        int entryRight     = scrollBarLeft - 1;
        int border         = 4;
        int mouseListY = mouseY - this.top + (int)this.getScrollDistance() - border;
        int slotIndex = mouseListY / this.slotHeight;

        if (mouseX >= entryLeft && mouseX <= entryRight && slotIndex >= 0 && mouseListY >= 0 && slotIndex < listLength) {
        	ArrayList<String> text = new ArrayList<String>(1);
        	text.add(this.bindings[slotIndex].getKeyDescription());
        	System.out.println(text);
        	net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText(text,mouseX, mouseY, this.parent.width, this.parent.height, -1, this.parent.getFontRenderer());
		    GlStateManager.disableLighting();
            GlStateManager.disableDepth();
        }
	}
	
	protected void updateList(){
		
		if ( !this.searchText.equals(this.parent.getSearchText()) || !this.selectedCategory.equals(this.parent.getSelectedCategory()) ) {
			this.searchText = this.parent.getSearchText();
			this.selectedCategory = this.parent.getSelectedCategory();
			KeyBinding[] bindingsNew = bindingsByCategory(this.selectedCategory);
			String[] words = this.searchText.split("\\s+");
			
			if (words.length != 0) {
				if (words[0].length()>0 && words[0].charAt(0) == '@') {
					bindingsNew = filterBindingsByKey(bindingsNew, words[0].substring(1, words[0].length()));
					words[0] = "";
				}
				bindingsNew = filterBindingsByName(bindingsNew, words);
			}
					
			this.bindings = bindingsNew;
			
			if (this.bindings.length != 0)
				this.selectKeybind(0);
		}
	}
	
	private void selectKeybind(int id){
		this.selectedKeybindId = id;
		this.selectedKeybind = this.bindings[id];
		this.parent.setSelectedKeybind(this.selectedKeybind);
	}
	
	private KeyBinding[] bindingsByCategory(String category) {
		KeyBinding[] bindings = KeybindUtils.ALL_BINDINGS;
		
		switch (category) {
		case "categories.all":
			return bindings;
		case "categories.conflicts":
			return Arrays.stream(bindings).filter(binding -> KeybindUtils.getNumConficts(binding) >= 1 && binding.getKeyCode() != 0).toArray(KeyBinding[]::new);
		case "categories.unbound":
			return Arrays.stream(bindings).filter(binding -> binding.getKeyCode() == 0).toArray(KeyBinding[]::new);
		default:
			return Arrays.stream(bindings).filter(binding -> binding.getKeyCategory() == category).toArray(KeyBinding[]::new);
		}
	}
	
	private KeyBinding[] filterBindingsByName(KeyBinding[] bindings, String[] words){
		KeyBinding[] filtered = {};
		filtered = Arrays.stream(bindings).filter(binding -> {
				boolean flag = true;
				for (String w:words) {
					flag = flag && I18n.format(binding.getKeyDescription()).toLowerCase().contains(w.toLowerCase());
				}
				return flag;
			}).toArray(KeyBinding[]::new);
		return filtered;
	}
	
	private KeyBinding[] filterBindingsByKey(KeyBinding[] bindings, String keyName) {
		KeyBinding[] filtered = {};
		filtered = Arrays.stream(bindings).filter(binding -> binding.getDisplayName().toLowerCase().contains(keyName.toLowerCase())).toArray(KeyBinding[]::new);
		return filtered;
	}
	
	public KeyBinding getSelectedKeybind(){
		return this.selectedKeybind;
	}
	
	@Override
	protected int getSize() {
		return bindings.length;
	}
	
	protected float getScrollDistance() {
		try {
			Class<?> clazz = this.getClass().getSuperclass();
			Field scrollDistance = clazz.getDeclaredField("scrollDistance");
			scrollDistance.setAccessible(true);
			return scrollDistance.getFloat(this);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return -1.0f;
	}

}
