package net.joshuahughes.fccamateurradio.examination.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.function.Predicate;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import net.joshuahughes.fccamateurradio.examination.Question;
import net.joshuahughes.fccamateurradio.examination.Utility;
import net.joshuahughes.fccamateurradio.examination.Utility.Ordering;
import net.joshuahughes.fccamateurradio.examination.exam.Exam;
import net.joshuahughes.fccamateurradio.examination.exam.ExamDocx;

public class StartPanel extends JPanel
{
	private static final long serialVersionUID = 731131016262512964L;
	
	LinkedHashMap<JRadioButton,File> fileMap = new LinkedHashMap<>();
	LinkedHashMap<JRadioButton,Utility.Ordering> orderMap = new LinkedHashMap<>();
	JSpinner min = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
	JSpinner max = new JSpinner(new SpinnerNumberModel(1000, 0, Integer.MAX_VALUE, 1));
	JSpinner cnt = new JSpinner(new SpinnerNumberModel(1000, 0, Integer.MAX_VALUE, 1));
	JButton fcc = new JButton("FCC test");
	JButton all = new JButton("all");
	JCheckBox fixMistakes = new JCheckBox("fix mistakes",true);
	ButtonGroup fileGrp = new ButtonGroup();
	ButtonGroup orderGrp = new ButtonGroup();
	JTextField find = new JTextField();
	
	public StartPanel()
	{
		File file = new File(ExamDialog.class.getClassLoader().getResource("docx/").getFile());
		Arrays.asList(file.listFiles()).forEach(f->
		{
			Utility.Class cls =Arrays.asList(Utility.Class.values()).stream().filter(c->f.getName().toLowerCase().contains(c.name())).findAny().get();
			JRadioButton b = new JRadioButton(cls.name());
			b.addActionListener(l->
			{
				min.setValue(0);
				max.setValue(cls.getPoolCount());
				cnt.setValue(cls.getQuestionCount());
				orderMap.entrySet().stream().filter(e->e.getValue().equals(Ordering.random)).findAny().get().getKey().setSelected(true);
			});
			fileGrp.add(b);
			fileMap.put(b, f);
		});
		
		Arrays.asList(Utility.Ordering.values()).forEach(o->
		{
			JRadioButton b = new JRadioButton(o.name());
			orderGrp.add(b);
			orderMap.put(b, o);
		});

		min.setPreferredSize(new Dimension(50,min.getPreferredSize().height));
		max.setPreferredSize(new Dimension(50,max.getPreferredSize().height));
		cnt.setPreferredSize(new Dimension(50,max.getPreferredSize().height));

		all.addActionListener(l->
		{
			JRadioButton btn = fileMap.keySet().stream().filter(box->box.isSelected()).findFirst().get();
			min.setValue(0);
			max.setValue(Utility.Class.valueOf(btn.getText()).getPoolCount());
			cnt.setValue(Utility.Class.valueOf(btn.getText()).getPoolCount());
		});
		fcc.addActionListener(l->
		{
			JRadioButton btn = fileMap.keySet().stream().filter(box->box.isSelected()).findFirst().get();
			cnt.setValue(Utility.Class.valueOf(btn.getText()).getQuestionCount());
			min.setValue(0);
			max.setValue(Utility.Class.valueOf(btn.getText()).getPoolCount());
		});
		fileMap.keySet().stream().filter(r->r.getText().toLowerCase().contains(Utility.Class.technician.name())).findAny().get().doClick();
		
		setLayout(new GridBagLayout());

		JPanel containsPnl = new JPanel(new BorderLayout());
		containsPnl.add(new JLabel("find: "),BorderLayout.WEST);
		containsPnl.add(find,BorderLayout.CENTER);
		containsPnl.setPreferredSize(new Dimension(150, find.getPreferredSize().height));

		
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
		add(fixMistakes,gbc);

		gbc.gridy++;
		add(containsPnl,gbc);

		gbc.gridy++;
		add(new JPanel(),gbc);

		gbc.gridy++;
		add(new JLabel("min:"),gbc);

		gbc.gridx++;
		add(new JLabel("max:"),gbc);
		
		gbc.gridx=0;
		gbc.gridy++;
		add(min,gbc);
		
		gbc.gridx++;
		add(max,gbc);
		
		
		gbc.gridx=0;
		gbc.gridy++;
		add(new JLabel("count:"),gbc);
		gbc.gridy++;
		add(cnt,gbc);
		
		gbc.gridx=0;
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
	}
	public Exam getExam()
	{
		ExamDocx exam = new ExamDocx();
		exam.set
		(
			fileMap.get(fileMap.keySet().stream().filter(b->b.isSelected()).findAny().get()),
			orderMap.get(orderMap.keySet().parallelStream().filter(o->o.isSelected()).findAny().get()),
			(int)cnt.getValue(),
			find.getText().isEmpty()?index:substring
		);
		return exam;
	}
	public boolean correctMissed()
	{
		return fixMistakes.isSelected();
	}
	Predicate<Question> index = q -> (int)min.getValue()<=q.getIndex() && q.getIndex()<=(int)max.getValue();
	Predicate<Question> substring = q ->
	{
		String text = find.getText().toLowerCase();
		if(q.getQuestion().toLowerCase().contains(text)) return true;
		Optional<String> op = q.stream().filter(c->c.toLowerCase().contains(text)).findAny();
		return op.isPresent();
	};
}
