package net.joshuahughes.fccamateurradio.examination;

import java.util.Random;

public class Utility
{
	public static final Random rnd = new Random(System.currentTimeMillis());
	public enum Class
	{
		technician(412,35,26),general(454,35,26),extra(622,50,37);
	
		private int poolCount;
		private int questionCount;
		private int passingCount;
	
		Class(int plCnt,int qstnCnt, int pssCnt)
		{
			poolCount = plCnt;
			questionCount = qstnCnt;
			passingCount = pssCnt;
		}
	
		public int getPoolCount()
		{
			return poolCount;
		}
		public int getQuestionCount()
		{
			return questionCount;
		}
	
		public int getPassingCount()
		{
			return passingCount;
		}
	}
}
