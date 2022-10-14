package net.joshuahughes.fccamateurradio.examination.ui;

import java.awt.GridBagLayout;
import java.util.LinkedHashMap;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import com.metsci.glimpse.docking.View;
import com.metsci.glimpse.docking.ViewCloseOption;

import net.joshuahughes.fccamateurradio.examination.LicenseClass;
import net.joshuahughes.fccamateurradio.examination.Utility;
import net.joshuahughes.fccamateurradio.examination.pool.DocxPool;
import net.joshuahughes.fccamateurradio.examination.pool.Pool;
import net.joshuahughes.fccamateurradio.examination.pool.function.Contains;
import net.joshuahughes.fccamateurradio.examination.pool.function.Creator;
import net.joshuahughes.fccamateurradio.examination.pool.function.FCC;
import net.joshuahughes.fccamateurradio.examination.pool.function.Random;
import net.joshuahughes.fccamateurradio.examination.pool.function.Reverse;
import net.joshuahughes.fccamateurradio.examination.pool.function.Stationary;

public class ControlView extends View
{
	LinkedHashMap<JRadioButton,Utility.Ordering> orderMap = new LinkedHashMap<>();
	ButtonGroup orderGrp = new ButtonGroup();
	JComboBox<LicenseClass> classBox = new JComboBox<>(LicenseClass.values());
	JButton fccBtn = new JButton("FCC");
	JTextField containsFld = new JTextField(15);
	Creator[] creators = new Creator[] {new Random(),new Reverse(),new Stationary()};
	JComboBox<Creator> creatorBox =new JComboBox<>(creators);
	JPanel panel = new JPanel(new GridBagLayout());
	SubelementGroupPanel sePnl;
	ExamFrame frame;

	public ControlView(ExamFrame frm) 
	{
		super("control",new SubelementGroupPanel(),"control",ViewCloseOption.VIEW_AUTO_CLOSEABLE,"control",UIManager.getIcon("FileView.computerIcon"),new JToolBar());
		sePnl = (SubelementGroupPanel) this.component.v();
		frame = frm;
		classBox.addActionListener(l->
		{
			sePnl.setLicenseClass((LicenseClass) classBox.getSelectedItem());
		});
		classBox.setSelectedItem(LicenseClass.general);
		toolbar.add(fccBtn);
		toolbar.add(classBox);
		toolbar.add(creatorBox);
		toolbar.add(containsFld);
		
		sePnl.addPropertyChangeListener(SubelementGroupPanel.class.getCanonicalName(), l->
		{
			frame.set(((Creator)creatorBox.getSelectedItem()).apply((Pool)l.getNewValue()));
		});
		
		fccBtn.addActionListener(l->
		{
			frame.set(((Creator)creatorBox.getSelectedItem()).apply(new FCC((LicenseClass) classBox.getSelectedItem()).apply(getPool())));
		});
		containsFld.addActionListener(l->
		{
			frame.set(((Creator)creatorBox.getSelectedItem()).apply(new Contains(containsFld.getText()).apply(getPool())));
		});
	}
	
	public DocxPool getPool()
	{
		return ((LicenseClass)classBox.getSelectedItem()).getPool();
	}
}