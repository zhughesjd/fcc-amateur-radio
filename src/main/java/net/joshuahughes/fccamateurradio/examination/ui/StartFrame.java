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

import net.joshuahughes.fccamateurradio.examination.LicenseClass;
import net.joshuahughes.fccamateurradio.examination.Utility;
import net.joshuahughes.fccamateurradio.examination.pool.DocxPool;
import net.joshuahughes.fccamateurradio.examination.pool.Pool;
import net.joshuahughes.fccamateurradio.examination.pool.function.Contains;
import net.joshuahughes.fccamateurradio.examination.pool.function.FCC;

public class StartFrame extends JFrame
{
	private static final long serialVersionUID = 2261331185832055649L;
	LinkedHashMap<JRadioButton,Utility.Ordering> orderMap = new LinkedHashMap<>();
	ButtonGroup orderGrp = new ButtonGroup();
	SubelementGroupPanel sePnl = new SubelementGroupPanel();
	JComboBox<LicenseClass> classBox = new JComboBox<>(LicenseClass.values());
	LinkedHashMap<LicenseClass, DocxPool> examMap = new LinkedHashMap<>();
	JButton fccBtn = new JButton("FCC");
	JTextField containsFld = new JTextField(15);
	ExamDialog dialog;

	public StartFrame(ExamDialog dlg) 
	{
		dialog = dlg;
		setContentPane(new JPanel(new GridBagLayout()));
		File file = new File(ExamDialog.class.getClassLoader().getResource("docx/").getFile());
		Stream.of(file.listFiles()).forEach(f->
		{
			LicenseClass cls = Stream.of(LicenseClass.values()).filter(c->f.getName().toLowerCase().contains(c.name())).findAny().get();
			DocxPool pool = new DocxPool(f);
			examMap.put(cls, pool);
		});
		classBox.addActionListener(l->
		{
			sePnl.setLicenseClass((LicenseClass) classBox.getSelectedItem());
		});
		classBox.setSelectedItem(LicenseClass.general);
		Container p = getContentPane();
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.weightx = gbc.weighty = 1;
		gbc.gridx = gbc.gridy = 0;
		p.add(new JLabel("class: "),gbc);
		
		gbc.gridx++;
		p.add(classBox,gbc);
		
		gbc.gridx=0;
		gbc.gridy++;
		gbc.gridwidth=2;
		p.add(sePnl,gbc);

		gbc.gridwidth=1;
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
		
		sePnl.addPropertyChangeListener(SubelementGroupPanel.class.getCanonicalName(), l->
		{
			dialog.set((Pool)l.getNewValue());
		});
		
		fccBtn.addActionListener(l->
		{
			dialog.set(new FCC((LicenseClass) classBox.getSelectedItem()).apply(getPool()));
		});
		containsFld.addActionListener(l->
		{
			dialog.set(new Contains(containsFld.getText()).apply(getPool()));
		});
		pack();
	}
	
	public DocxPool getPool()
	{
		return examMap.get((LicenseClass) classBox.getSelectedItem());
		
	}
	public static void main(String[] args)
	{
		ExamDialog dialog = new ExamDialog();
		dialog.setVisible(true);
		StartFrame frame = new StartFrame(dialog);
		frame.setVisible(true);
	}
}