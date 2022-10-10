package net.joshuahughes.fccamateurradio.examination.ui;

import java.awt.BorderLayout;
import java.util.stream.IntStream;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.joshuahughes.fccamateurradio.examination.LicenseClass;
import net.joshuahughes.fccamateurradio.examination.pool.Exam;

public class SubelementGroupPanel extends JPanel
{
	private static final long serialVersionUID = -5849119413689778366L;
	DefaultListModel<String> sMdl = new DefaultListModel<String>();
	JList<String> sLst = new JList<String>(sMdl);
	DefaultListModel<String> gMdl = new DefaultListModel<String>();
	JList<String> gLst = new JList<String>(gMdl);
	LicenseClass licenseClass = null;
	public SubelementGroupPanel()
	{
		super(new BorderLayout());
		sLst.setPrototypeCellValue("**********************************************");
		gLst.setPrototypeCellValue("**********************************************");
		add(new JScrollPane(sLst,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER),BorderLayout.CENTER);
		add(new JScrollPane(gLst, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER),BorderLayout.EAST);
		
		sLst.addListSelectionListener(l->
		{
			if(l.getValueIsAdjusting()) return;
			updateGroupList();
		});
		gLst.addListSelectionListener( e ->	firePropertyChange(SubelementGroupPanel.class.getName(), null, getExam()));
	}
	private Exam getExam()
	{
		Exam exam = new Exam();
		exam.getImages().addAll(licenseClass.getPool().getImages());
		IntStream.of(gLst.getSelectedIndices()).forEach(ndx->
		{
			String group = gMdl.get(ndx);
			licenseClass.
				getPool().
				stream().
				filter(q->q.getGroup().equals(group)).
				forEach(s->exam.add(s));
		});
		return exam;
	}
	private void updateGroupList()
	{
		gMdl.clear();
		sLst.getSelectedIndices();
		IntStream.of(sLst.getSelectedIndices()).forEach(ndx->
		{
			String subelement = sMdl.get(ndx);
			licenseClass.getPool().stream().filter(q->q.getSubelement().equals(subelement)).map(q->q.getGroup()).distinct().forEach(s->gMdl.addElement(s));
		});
		gLst.setSelectionInterval(0, gLst.getModel().getSize()-1);
	}
	public void setLicenseClass(LicenseClass cls)
	{
		licenseClass = cls;
		sMdl.clear();
		gMdl.clear();
		cls.getPool().stream().map(q->q.getSubelement()).distinct().forEach(s->sMdl.addElement(s));
	}
}
