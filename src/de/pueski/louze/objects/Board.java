package de.pueski.louze.objects;

import java.util.HashMap;


public class Board {

	public static final int A = 7;
	public static final int B = 6;
	public static final int C = 5;
	public static final int D = 4;
	public static final int E = 3;
	public static final int F = 2;
	public static final int G = 1;
	public static final int H = 0;
	
	public static final int ONE   = 7;
	public static final int TWO   = 6;
	public static final int THREE = 5;
	public static final int FOUR  = 4;
	public static final int FIVE  = 3;
	public static final int SIX   = 2;
	public static final int SEVEN = 1;
	public static final int EIGHT = 0;
	
	public static final HashMap<String, Integer> boardMap           = new HashMap<String, Integer>();
	public static final HashMap<Integer, String> modelToBoardLetter = new HashMap<Integer, String>();
	public static final HashMap<Integer, String> modelToBoardValue  = new HashMap<Integer, String>();
	
	static  {
		
		boardMap.put("A", 0);
		boardMap.put("B", 1);
		boardMap.put("C", 2);
		boardMap.put("D", 3);
		boardMap.put("E", 4);
		boardMap.put("F", 5);
		boardMap.put("G", 6);
		boardMap.put("H", 7);

		boardMap.put("1", ONE);
		boardMap.put("2", TWO);
		boardMap.put("3", THREE);
		boardMap.put("4", FOUR);
		boardMap.put("5", FIVE);
		boardMap.put("6", SIX);
		boardMap.put("7", SEVEN);
		boardMap.put("8", EIGHT);
		
		modelToBoardLetter.put(0, "A");
		modelToBoardLetter.put(1, "B");
		modelToBoardLetter.put(2, "C");
		modelToBoardLetter.put(3, "D");
		modelToBoardLetter.put(4, "E");
		modelToBoardLetter.put(5, "F");
		modelToBoardLetter.put(6, "G");
		modelToBoardLetter.put(7, "H");
		
		modelToBoardValue.put(0,"8");
		modelToBoardValue.put(1,"7");
		modelToBoardValue.put(2,"6");
		modelToBoardValue.put(3,"5");
		modelToBoardValue.put(4,"4");
		modelToBoardValue.put(5,"3");
		modelToBoardValue.put(6,"2");
		modelToBoardValue.put(7,"1");
	}
	
	
}
