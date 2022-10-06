package net.joshuahughes.fccamateurradio.examination.ui;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.stream.Stream;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import net.joshuahughes.fccamateurradio.examination.Utility;
import net.joshuahughes.fccamateurradio.examination.Utility.Class;
import net.joshuahughes.fccamateurradio.examination.exam.DocxPool;
import net.joshuahughes.fccamateurradio.examination.exam.function.Contains;
import net.joshuahughes.fccamateurradio.examination.exam.function.FCC;
import net.joshuahughes.fccamateurradio.examination.exam.function.Group;
import net.joshuahughes.fccamateurradio.examination.exam.function.Subelement;

public class StartFrame extends JFrame
{
	private static final long serialVersionUID = 2261331185832055649L;
	LinkedHashMap<JRadioButton,Utility.Ordering> orderMap = new LinkedHashMap<>();
	ButtonGroup orderGrp = new ButtonGroup();

	JComboBox<Utility.Class> classBox = new JComboBox<>(Utility.Class.values());
	LinkedHashMap<Utility.Class, DocxPool> examMap = new LinkedHashMap<>();
	JComboBox<String> subelementBox = new JComboBox<String>();
	JComboBox<String> groupBox = new JComboBox<String>();
	JButton fccBtn = new JButton("FCC");
	JTextField containsFld = new JTextField(15);
	ExamDialog dialog;

	public StartFrame(ExamDialog dlg) 
	{
		dialog = dlg;
		subelementBox.setPrototypeDisplayValue("*********************************");
		groupBox.setPrototypeDisplayValue("*********************************");
		setContentPane(new JPanel(new GridBagLayout()));
		File file = new File(ExamDialog.class.getClassLoader().getResource("docx/").getFile());
		Stream.of(file.listFiles()).forEach(f->
		{
			Utility.Class cls = Stream.of(Utility.Class.values()).filter(c->f.getName().toLowerCase().contains(c.name())).findAny().get();
			DocxPool pool = new DocxPool(f);
			examMap.put(cls, pool);
		});
		classBox.addActionListener(l->
		{
			subelementBox.removeAllItems();
			getPool().stream().map(q->q.getSubelement()).distinct().forEach(s->subelementBox.addItem(s));
			groupBox.removeAllItems();
			getPool().stream().map(q->q.getGroup()).distinct().forEach(s->groupBox.addItem(s));
			pack();
		});
		classBox.setSelectedItem(Utility.Class.general);
		Container p = getContentPane();
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.weightx = gbc.weighty = 1;
		gbc.gridx = gbc.gridy = 0;
		p.add(new JLabel("class: "),gbc);
		
		gbc.gridx++;
		p.add(classBox,gbc);
		
		gbc.gridx=0;
		gbc.gridy++;
		p.add(new JLabel("subelement: "),gbc);

		gbc.gridx++;
		p.add(subelementBox,gbc);

		gbc.gridx=0;
		gbc.gridy++;
		p.add(new JLabel("group: "),gbc);

		gbc.gridx++;
		p.add(groupBox,gbc);
		
		gbc.gridx=0;
		gbc.gridy++;
		p.add(new JLabel("fcc: "),gbc);

		gbc.gridx++;
		p.add(fccBtn,gbc);
		
		gbc.gridx=0;
		gbc.gridy++;
		p.add(new JLabel("contains: "),gbc);

		gbc.gridx++;
		p.add(containsFld,gbc);
		
		groupBox.addActionListener(l->
		{
			if(groupBox.getSelectedItem()!=null)
				dialog.set(new Group(groupBox.getSelectedItem().toString()).apply(getPool()));
		});
		subelementBox.addActionListener(l->
		{
			if(subelementBox.getSelectedItem()!=null)
				dialog.set(new Subelement(subelementBox.getSelectedItem().toString()).apply(getPool()));
		});
		fccBtn.addActionListener(l->
		{
			dialog.set(new FCC((Class) this.classBox.getSelectedItem()).apply(getPool()));
		});
		containsFld.addActionListener(l->
		{
			dialog.set(new Contains(containsFld.getText()).apply(getPool()));
		});
		pack();
	}
	
	public DocxPool getPool()
	{
		return examMap.get((Utility.Class) classBox.getSelectedItem());
		
	}
	public static void main(String[] args)
	{
		ExamDialog dialog = new ExamDialog();
		dialog.setVisible(true);
		StartFrame frame = new StartFrame(dialog);
		frame.setVisible(true);
	}
}