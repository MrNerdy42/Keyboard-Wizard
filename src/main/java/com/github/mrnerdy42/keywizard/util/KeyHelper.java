package com.github.mrnerdy42.keywizard.util;

import static org.lwjgl.input.Keyboard.*;

import java.util.HashMap;

public class KeyHelper {
	
	private static final HashMap<Integer, String> KEY_LABELS = new HashMap();
	static{
		KEY_LABELS.put(KEY_F1, "F1");
		KEY_LABELS.put(KEY_F2, "F2");
		KEY_LABELS.put(KEY_F3, "F3");
		KEY_LABELS.put(KEY_F4, "F4");
		KEY_LABELS.put(KEY_F5, "F5");
		KEY_LABELS.put(KEY_F6, "F6");
		KEY_LABELS.put(KEY_F7, "F7");
		KEY_LABELS.put(KEY_F8, "F8");
		KEY_LABELS.put(KEY_F9, "F9");
		KEY_LABELS.put(KEY_F10, "F10");
		KEY_LABELS.put(KEY_F11, "F11");
		KEY_LABELS.put(KEY_F12, "F12");
		KEY_LABELS.put(KEY_MINUS, "-");
		KEY_LABELS.put(KEY_EQUALS, "=");
		KEY_LABELS.put(KEY_LBRACKET, "[");
		KEY_LABELS.put(KEY_RBRACKET, "]");
		KEY_LABELS.put(KEY_SEMICOLON, ";");
		KEY_LABELS.put(KEY_COMMA, ",");
		KEY_LABELS.put(KEY_PERIOD, ".");
		KEY_LABELS.put(KEY_APOSTROPHE, "'");
		KEY_LABELS.put(KEY_SLASH, "/");
		KEY_LABELS.put(KEY_BACKSLASH, "\\");
		KEY_LABELS.put(KEY_GRAVE, "`");
		KEY_LABELS.put(KEY_LCONTROL, "LCTRL");
		KEY_LABELS.put(KEY_RCONTROL, "RCTRL");
	}
	
	/**
	 * Translate the LWJGL key name to a symbol or shortened version of the name.
	 * Returns the long version of the key name if there is no symbol available.
	 * 
	 * @param id
	 *            the LWJGL code of the key to translate
	 */
	public static String translateKey(int id) {
		String keyName;
		if (KEY_LABELS.containsKey(id)) {
			return KEY_LABELS.get(id);
		} else {
			return getKeyName(id);
		}
	}
}