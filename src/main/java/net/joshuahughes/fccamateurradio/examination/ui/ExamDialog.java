package net.joshuahughes.fccamateurradio.examination.ui;

import static com.metsci.glimpse.docking.DockingFrameCloseOperation.DISPOSE_ALL_FRAMES;
import static com.metsci.glimpse.docking.DockingUtils.setArrangementAndSaveOnDispose;
import static com.metsci.glimpse.docking.DockingWindowTitlers.createDefaultWindowTitler;
import static java.awt.Dialog.ModalityType.MODELESS;

import java.awt.image.BufferedImage;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.metsci.glimpse.docking.View;
import com.metsci.glimpse.docking.examples.ModalDialogDockingExample;
import com.metsci.glimpse.docking.group.dialog.DockingGroupDialog;

import net.joshuahughes.fccamateurradio.examination.Exam;
import net.joshuahughes.fccamateurradio.examination.Question;
import net.joshuahughes.fccamateurradio.examination.ui.question.ChoicePanel;
import net.joshuahughes.fccamateurradio.examination.ui.question.DisplayPanel;

public class ExamDialog extends DockingGroupDialog 
{
	LinkedHashSet<JDialog> dlgSet = new LinkedHashSet<>();
	DisplayPanel previousPnl = new DisplayPanel();
	ChoicePanel  currentPnl = new ChoicePanel();
	JTextArea resultPnl = new JTextArea();
	Exam exam;
	public ExamDialog()
	{
		super(null, MODELESS, DISPOSE_ALL_FRAMES );
        addListener( createDefaultWindowTitler( "Docking Example" ) );
        setArrangementAndSaveOnDispose( this, ExamDialog.class.getName(), ModalDialogDockingExample.class.getResource( "docking/simple-arrangement-default.xml" ) );
		addViews
		(
			new View[] 
			{
				new View(0+"", currentPnl, "current"),
				new View(1+"", previousPnl, "previous"),
				new View(2+"", new JScrollPane(resultPnl), "result")
			}
		);
		currentPnl.addPropertyChangeListener(ChoicePanel.class.getCanonicalName(),l->
		{
			update();
		});
	}
	public void set(Exam newExam)
	{	
		if(newExam.isEmpty()) return;
		exam = newExam;
		List<View> rmvList = views().entrySet().stream().filter(v->v.getValue().viewId.toLowerCase().contains("image")).map(e->e.getValue()).collect(Collectors.toList());
		rmvList.stream().forEach(v->closeView(v));
		IntStream.range(0, exam.getImages().size()).mapToObj(i->new View("image"+i, new JScrollPane(new JLabel(new ImageIcon(exam.getImages().get(i).getScaledInstance(400, 300, BufferedImage.SCALE_SMOOTH)))), "image"+i)).forEach(v->addView(v));
		update();
	}
	public void update()
	{
		resultPnl.setText(exam.getStats());
		if(exam.hasNext())
		{
			previousPnl.setQuestion(currentPnl.getQuestion());
			currentPnl.setQuestion(exam.next());
			return;
		}
		if(!currentPnl.getQuestion().equals(Question.empty))
		{
			Question qstn = currentPnl.getQuestion();
			previousPnl.setQuestion(qstn);
			currentPnl.setQuestion(Question.empty);
		}
	}
	public void setVisible(boolean visible)
	{
		super.setVisible(visible);
		currentPnl.requestFocus();
	}
	public static void main(String[] args) throws Exception
	{
		StartPanel startPanel = new StartPanel();
		if(JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(null, startPanel,"select below",JOptionPane.OK_CANCEL_OPTION))
			return;
		ExamDialog dialog = new ExamDialog();
		dialog.set(startPanel.getExam());
		dialog.setVisible(true);
	}
}
