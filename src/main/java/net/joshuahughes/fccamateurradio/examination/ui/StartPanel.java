package net.joshuahughes.fccamateurradio.examination.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashMap;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import net.joshuahughes.fccamateurradio.examination.Exam;

public class StartPanel extends JPanel
{
	private static final long serialVersionUID = 731131016262512964L;
	
	LinkedHashMap<JRadioButton,File> fileMap = new LinkedHashMap<>();
	LinkedHashMap<JRadioButton,Exam.Ordering> orderMap = new LinkedHashMap<>();
	JSpinner count = new JSpinner(new SpinnerNumberModel(10, 1, Integer.MAX_VALUE, 1));
	JButton fcc = new JButton("FCC test");
	JButton all = new JButton("all");
	JCheckBox correctMissed = new JCheckBox("correct missed",false);
	ButtonGroup fileGrp = new ButtonGroup();
	ButtonGroup orderGrp = new ButtonGroup();
	
	public StartPanel()
	{
		File file = new File(ExamDialog.class.getClassLoader().getResource("docx/").getFile());
		Arrays.asList(file.listFiles()).forEach(f->
		{
			JRadioButton b = new JRadioButton(f.getName());
			fileGrp.add(b);
			fileMap.put(b, f);
		});
		
		Arrays.asList(Exam.Ordering.values()).forEach(o->
		{
			JRadioButton b = new JRadioButton(o.name());
			orderGrp.add(b);
			orderMap.put(b, o);
		});

		count.setPreferredSize(new Dimension(50,25));

		all.addActionListener(l->
		{
			count.setValue(Integer.MAX_VALUE);	
		});
		fcc.addActionListener(l->
		{
			JRadioButton btn = fileMap.keySet().stream().filter(box->box.isSelected()).findFirst().get();
			count.setValue(btn.getText().toLowerCase().contains("extra")?50:32);	
		});
		fileMap.keySet().stream().filter(r->r.getText().toLowerCase().contains("technician")).findAny().get().setSelected(true);
		orderMap.keySet().stream().filter(r->r.getText().toLowerCase().contains("random")).findAny().get().setSelected(true);
		
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = gbc.weighty = 1;
		gbc.gridx = gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.LINE_START;

		gbc.gridy++;
		add(new JPanel(),gbc);
		fileMap.keySet().forEach(f->
		{
			gbc.gridy++;
			add(f,gbc);
		});
		
		gbc.gridy++;
		add(new JPanel(),gbc);

		gbc.gridy++;
		add(count,gbc);

		orderMap.keySet().forEach(o->
		{
			gbc.gridy++;
			add(o,gbc);
		});

		gbc.gridy++;
		add(new JPanel(),gbc);

		gbc.gridy++;
		add(all,gbc);
		gbc.gridy++;
		add(fcc,gbc);
		gbc.gridy++;
		add(correctMissed,gbc);
	}
	public Exam getExam()
	{
		Exam exam = new Exam();
		exam.set(
				fileMap.get(fileMap.keySet().stream().filter(b->b.isSelected()).findAny().get()),
				(Integer)count.getModel().getValue(),
				correctMissed.isSelected(),
				orderMap.get(orderMap.keySet().parallelStream().filter(o->o.isSelected()).findAny().get()));
		return exam;
	}
	public boolean correctMissed()
	{
		return correctMissed.isSelected();
	}
}
