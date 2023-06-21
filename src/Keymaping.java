public class Keymaping implements Comparable<Keymaping> {

	// difine varible name
	private String  keyWord;
	private char    firspoint;
        private char    lastpoint;
	private int     totalValue; 
        private int     firstLetter; 
        private int     lastLetter;
        private int     getfist;
        private int     getlast; 
        private int     unassignedLetterPosition;
	
	//construct the keymaping
	public Keymaping(String keyWord)	{
		this.keyWord = keyWord;
		this.firspoint = keyWord.charAt(0);
		this.lastpoint = keyWord.charAt(keyWord.length() - 1);
		this.totalValue = 0; //updated in setValue method
		this.firstLetter = 0; 
		this.lastLetter = 0; 
		this.getfist = 0; // Cichelli's alogrithm
		this.getlast = 0; 
		//unassignedLetterPosition is set to -1 as an arbitrary value.
		this.unassignedLetterPosition = -1; // Cichelli's alogrithm under certain conditions.
	}
	
	//sorting the list of keys by value.
        //methord override
	
	@Override
	public int compareTo(Keymaping comparesTo)	{
		int compareValue = ((Keymaping)comparesTo).getTotalValue();
		return compareValue-this.totalValue;
	}

	//get and set methord 
	
	public String getKeyWord()	{
		return keyWord;
	}
	
	public char getFirstLetter()	{
		return firspoint;
	}
	
	public char getLastLetter()	{
		return lastpoint;
	}
	
	public int getFirstLetterValue()	{
		return firstLetter;
	}
	
	public void setFirstLetterValue(int value)	{
		this.firstLetter = value;
	}
	
	public int getLastLetterValue()	{
		return lastLetter;
	}
	
	public void setLastLetterValue(int value)	{
		this.lastLetter = value;
	}
	
	public void setTotalValue(int value)	{
		this.totalValue = value;
	}
	
	public int getTotalValue()	{
		return totalValue;
	}
	
	public void setPositionInGFirst(int position)	{
		this.getfist = position;
	}
	
	public int getPositionInGFirst()	{
		return getfist;
	}

	
	public void setpositionInGLast(int position)	{
		this.getlast = position;
	}

	public int getPositionInGLast()	{
		return getlast;
	}
	
	public void setUnassignedLetterPosition(int position)	{
		this.unassignedLetterPosition = position;
	}
	
	public int getUnassignedLetterPosition()	{
		return unassignedLetterPosition;
	}
	
}
