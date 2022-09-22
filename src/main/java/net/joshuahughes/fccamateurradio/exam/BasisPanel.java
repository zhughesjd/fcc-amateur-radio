package net.joshuahughes.fccamateurradio.exam;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;

public class BasisPanel extends JPanel
{
	private static final long serialVersionUID = 2039847859309485L;
	Color defltBG = new JToggleButton().getBackground();
	Color rightBG = Color.green;
	Color wrongBG = Color.red;
	JLabel qstn = new JLabel();
	ButtonGroup chcGrp = new ButtonGroup();
	ArrayList<JRadioButton> chcs = new ArrayList<>(IntStream.range(0, 4).mapToObj(i->new JRadioButton()).collect(Collectors.toList()));
	Exam exam = null;
	Question question = null;
	public BasisPanel()
	{
		super(new GridBagLayout());
		layoutPanel();
		chcs.stream().forEach(r->chcGrp.add(r));
	}
	public void store(Point qc)
	{
		int q = qc.x;
		int c = qc.y;
		setText(exam.get(q));
		int a = exam.get(q).getAnswer();
		chcs.get(c).setBackground(wrongBG);
		chcs.get(a).setBackground(rightBG);
	}
	public boolean setExam(Exam newExam)
	{
		exam = newExam;
		return true;
	}
	public void setText(Question question)
	{
		chcGrp.clearSelection();
		chcs.stream().forEach(c->c.setBackground(defltBG));
		qstn.setText(question.toString());
		IntStream.range(0, question.size()).forEach(i->
		{
			chcs.get(i).setText(question.get(i));
		});
		chcs.stream().forEach(c->c.setBackground(defltBG));
	}
	private void layoutPanel()
	{
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = gbc.weighty = 1;
		gbc.gridx = gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.LINE_START;
		add(qstn,gbc);

		gbc.gridy++;
		add(new JPanel(),gbc);
		
		for(int x=0;x<chcs.size();x++)
		{
			gbc.gridy++;
			add(chcs.get(x),gbc);
		}

		gbc.gridy++;
		add(new JPanel(),gbc);
	}
}
