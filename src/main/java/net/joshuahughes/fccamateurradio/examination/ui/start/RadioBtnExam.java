package net.joshuahughes.fccamateurradio.examination.ui.start;

import javax.swing.JRadioButtonMenuItem;

import net.joshuahughes.fccamateurradio.examination.exam.DocxExam;

public class RadioBtnExam extends JRadioButtonMenuItem
{
	private static final long serialVersionUID = -6091234641236552179L;
	private DocxExam exam;
	public RadioBtnExam(DocxExam xm)
	{
		this.exam = xm;
		setText(xm.getUtilityClass().name());
	}
	public DocxExam getExam()
	{
		return exam;
	}
}
