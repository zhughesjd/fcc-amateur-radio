package net.joshuahughes.fccamateurradio.examination.ui.question;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;

import net.joshuahughes.fccamateurradio.examination.Question;

public class QuestionPanel extends JPanel
{
	private static final long serialVersionUID = -1771840904347845324L;
	Color defltBG = new JToggleButton().getBackground();
	Color rightBG = Color.green;
	Color wrongBG = Color.red;
	JLabel qstn = new JLabel();
	ButtonGroup chcGrp = new ButtonGroup();
	ArrayList<JRadioButton> chcs = new ArrayList<>(IntStream.range(0, 4).mapToObj(i->new JRadioButton()).collect(Collectors.toList()));
	Question question = Question.empty;
	public QuestionPanel()
	{
		super(new GridBagLayout());
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
	public void setQuestion(Question qstn)
	{
		question = qstn;
		updateComponents();
	}
	public Question getQuestion()
	{
		return question;
	}
	public void updateComponents()
	{
		qstn.setText(question.toString());
		chcGrp.clearSelection();
		chcs.stream().forEach(c->c.setBackground(defltBG));
		IntStream.range(0, question.size()).forEach(i->
		{
			chcs.get(i).setText(question.get(i));
		});
	}
	protected JRadioButton getRadioAnswer()
	{
		return chcs.get(question.getAnswer());
	}
}
