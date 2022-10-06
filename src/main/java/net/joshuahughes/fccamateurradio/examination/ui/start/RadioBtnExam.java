package net.joshuahughes.fccamateurradio.examination.ui.start;

import javax.swing.JRadioButtonMenuItem;

import net.joshuahughes.fccamateurradio.examination.exam.DocxPool;

public class RadioBtnExam extends JRadioButtonMenuItem
{
	private static final long serialVersionUID = -6091234641236552179L;
	private DocxPool exam;
	public RadioBtnExam(DocxPool xm)
	{
		this.exam = xm;
		setText(xm.getUtilityClass().name());
	}
	public DocxPool getExam()
	{
		return exam;
	}
}
