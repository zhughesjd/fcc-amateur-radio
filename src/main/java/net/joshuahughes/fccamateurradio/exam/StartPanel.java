package net.joshuahughes.fccamateurradio.exam;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class StartPanel extends JPanel
{
	private static final long serialVersionUID = 731131016262512964L;
	Random rnd = new Random(System.currentTimeMillis());
	
	JCheckBox techBox = new JCheckBox("technician",true);
	JSpinner techSpnr = new JSpinner(new SpinnerNumberModel(10, 1, Integer.MAX_VALUE, 1));
	JCheckBox gnrlBox = new JCheckBox("general",true);
	JSpinner gnrlSpnr = new JSpinner(new SpinnerNumberModel(10, 1, Integer.MAX_VALUE, 1));
	JCheckBox xtraBox = new JCheckBox("extra",true);
	JSpinner xtraSpnr = new JSpinner(new SpinnerNumberModel(20, 1, Integer.MAX_VALUE, 1));

	JCheckBox[] boxes = {techBox,gnrlBox,xtraBox};
	JSpinner[] spnrs = {techSpnr,gnrlSpnr,xtraSpnr};
	
	JRadioButton random = new JRadioButton("random");
	JRadioButton sequential = new JRadioButton("sequential");
	ButtonGroup typeGrp = new ButtonGroup();
	
	JButton fcc = new JButton("FCC test");
	JButton all = new JButton("all");
	LinkedHashMap<JCheckBox,Exam> boxTestMap;
	
	public StartPanel(List<Exam> list)
	{
		boxTestMap = getBoxTestMap(boxes, list.toArray(new Exam[0]));
		typeGrp.add(random);
		typeGrp.add(sequential);
		random.setSelected(true);
		IntStream.range(0, boxes.length).forEach(i->
		{
			boxes[i].addChangeListener(l->spnrs[i].setEnabled(boxes[i].isSelected()));
			spnrs[i].setPreferredSize(new Dimension(50,25));
		});
		techBox.addChangeListener(l->techSpnr.setEnabled(techBox.isSelected()));
		gnrlBox.addChangeListener(l->gnrlSpnr.setEnabled(gnrlBox.isSelected()));
		xtraBox.addChangeListener(l->xtraSpnr.setEnabled(xtraBox.isSelected()));
		Stream.of(techSpnr,gnrlSpnr,xtraSpnr).forEach(s->s.setPreferredSize(new Dimension(50,25)));
		all.addActionListener(l->IntStream.range(0, boxes.length).forEach(b->
		{
			JCheckBox box = boxes[b];
			if(boxTestMap.containsKey(box))
				spnrs[b].setValue(boxTestMap.get(box).size());
		}));
		fcc.addActionListener(l->IntStream.range(0, boxes.length).forEach(b->
		{
			JCheckBox box = boxes[b];
			if(boxTestMap.containsKey(box))
			
				spnrs[b].setValue(box.getText().toLowerCase().contains("extra")?50:32);
		}));
		
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = gbc.weighty = 1;
		gbc.gridx = gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.LINE_START;

		IntStream.range(0, boxes.length).forEach(i->
		{
			if(boxTestMap.keySet().contains(boxes[i]))
			{
				add(boxes[i],gbc);
				gbc.gridx++;
				add(spnrs[i],gbc);
				gbc.gridy++;
				gbc.gridx=0;
			}
		});
		add(new JPanel(),gbc);
		gbc.gridy++;
		
		Collections.list(typeGrp.getElements()).stream().forEach(b->
		{
			add(b,gbc);
			gbc.gridy++;
		});
		add(new JPanel(),gbc);
		gbc.gridy++;
		
		add(all,gbc);
		gbc.gridy++;
		add(fcc,gbc);
		gbc.gridy++;

	}
	public List<Exam> getList()
	{
		List<Exam> testList = new ArrayList<>();
		for(int b=0;b<boxes.length;b++)
		{
			JCheckBox box = boxes[b];
			if(!box.isSelected()) continue;
			Exam test = boxTestMap.get(box);
			if(test == null || test.size()<=0) continue;
			Integer cnt = (Integer)spnrs[b].getValue();
			Exam newTest = new Exam(test.toString(),"",test.getImages());
			testList.add(newTest);
			ArrayList<Question> questions = new ArrayList<>(test);
			if(random.isSelected())
				Collections.shuffle(questions,rnd);
			IntStream.range(0, cnt).forEach(i->newTest.add(questions.get(i)));
		}
		return testList ;

	}
	public static LinkedHashMap<JCheckBox,Exam> getBoxTestMap(JCheckBox[] boxes,Exam[] tests)
	{
		String[] names = Stream.of(tests).map(t->t.toString().toLowerCase()).toArray(String[]::new);
		LinkedHashMap<JCheckBox, Exam> map = new LinkedHashMap<>();
		for(int b=0;b<boxes.length;b++)
			for(int n=0;n<names.length;n++)
				if(names[n].contains(boxes[b].getText().toLowerCase()))
					map.put(boxes[b],tests[n]);
		return map;
	}
}
