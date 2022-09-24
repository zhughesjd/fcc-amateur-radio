package net.joshuahughes.fccamateurradio.examination;

import java.util.Random;

public class Utility
{
	public static final Random rnd = new Random(System.currentTimeMillis());
	public enum Class
	{
		technician(35,26),general(35,26),extra(50,37);
	
		private int questionCount;
		private int passing;
	
		Class(int q, int p)
		{
			questionCount = q;
			passing = p;
		}
	
		public int getQuestionCount() {
			return questionCount;
		}
	
		public int getPassing() {
			return passing;
		}
	}
}
