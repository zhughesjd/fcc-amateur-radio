package net.joshuahughes.fccamateurradio.examination.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashMap;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import net.joshuahughes.fccamateurradio.examination.Exam;
import net.joshuahughes.fccamateurradio.examination.Utility;

public class StartPanel extends JPanel
{
	private static final long serialVersionUID = 731131016262512964L;
	
	LinkedHashMap<JRadioButton,File> fileMap = new LinkedHashMap<>();
	LinkedHashMap<JRadioButton,Exam.Ordering> orderMap = new LinkedHashMap<>();
	JSpinner count = new JSpinner(new SpinnerNumberModel(10, 1, Integer.MAX_VALUE, 1));
	JButton fcc = new JButton("FCC test");
	JButton all = new JButton("all");
	JCheckBox correctMissed = new JCheckBox("correct missed",true);
	ButtonGroup fileGrp = new ButtonGroup();
	ButtonGroup orderGrp = new ButtonGroup();
	JTextField contains = new JTextField();
	
	public StartPanel()
	{
		File file = new File(ExamDialog.class.getClassLoader().getResource("docx/").getFile());
		Arrays.asList(file.listFiles()).forEach(f->
		{
			Utility.Class cls =Arrays.asList(Utility.Class.values()).stream().filter(c->f.getName().toLowerCase().contains(c.name())).findAny().get();
			JRadioButton b = new JRadioButton(cls.name());
			fileGrp.add(b);
			fileMap.put(b, f);
		});
		
		Arrays.asList(Exam.Ordering.values()).forEach(o->
		{
			JRadioButton b = new JRadioButton(o.name());
			orderGrp.add(b);
			orderMap.put(b, o);
		});

		count.setPreferredSize(new Dimension(50,count.getPreferredSize().height));

		all.addActionListener(l->
		{
			JRadioButton btn = fileMap.keySet().stream().filter(box->box.isSelected()).findFirst().get();
			count.setValue(Utility.Class.valueOf(btn.getText()).getPoolCount());
		});
		fcc.addActionListener(l->
		{
			JRadioButton btn = fileMap.keySet().stream().filter(box->box.isSelected()).findFirst().get();
			count.setValue(Utility.Class.valueOf(btn.getText()).getQuestionCount());
		});
		fileMap.keySet().stream().filter(r->r.getText().toLowerCase().contains("technician")).findAny().get().setSelected(true);
		orderMap.keySet().stream().filter(r->r.getText().toLowerCase().contains("random")).findAny().get().setSelected(true);
		
		setLayout(new GridBagLayout());
		JPanel countPnl = new JPanel(new BorderLayout());
		countPnl.add(new JLabel("count: "),BorderLayout.CENTER);
		countPnl.add(count,BorderLayout.EAST);

		JPanel containsPnl = new JPanel(new BorderLayout());
		containsPnl.add(new JLabel("match: "),BorderLayout.WEST);
		containsPnl.add(contains,BorderLayout.CENTER);
		containsPnl.setPreferredSize(new Dimension(150, contains.getPreferredSize().height));

		
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
		add(containsPnl,gbc);

		gbc.gridy++;
		add(countPnl,gbc);


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
				orderMap.get(orderMap.keySet().parallelStream().filter(o->o.isSelected()).findAny().get()),
				contains.getText().toLowerCase()
				);
		return exam;
	}
	public boolean correctMissed()
	{
		return correctMissed.isSelected();
	}
}
