package net.joshuahughes.fccamateurradio.examination.ui;

import static com.metsci.glimpse.docking.DockingFrameCloseOperation.DISPOSE_ALL_FRAMES;
import static com.metsci.glimpse.docking.DockingUtils.setArrangementAndSaveOnDispose;
import static com.metsci.glimpse.docking.DockingWindowTitlers.createDefaultWindowTitler;
import static java.awt.Dialog.ModalityType.MODELESS;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
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

import net.joshuahughes.fccamateurradio.examination.Question;
import net.joshuahughes.fccamateurradio.examination.exam.Exam;
import net.joshuahughes.fccamateurradio.examination.ui.question.ChoicePanel;
import net.joshuahughes.fccamateurradio.examination.ui.question.DisplayPanel;

public class ExamDialog extends DockingGroupDialog 
{
	LinkedHashSet<JDialog> dlgSet = new LinkedHashSet<>();
	DisplayPanel previousPnl = new DisplayPanel();
	ChoicePanel  currentPnl = new ChoicePanel();
	JTextArea textArea = new JTextArea();
	ArrayList<Question> qs = new ArrayList<>();
	int ndx=0;
	Exam exam;
	boolean fixMistakes;
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
				new View(2+"", new JScrollPane(textArea), "result")
			}
		);
		currentPnl.addPropertyChangeListener(ChoicePanel.class.getCanonicalName(),l->
		{
			Question q = currentPnl.getQuestion();
			if(q.getState().equals(Question.State.wrong))
					qs.add(q);
			update();
		});
	}
	public void set(Exam newExam,boolean fix)
	{	
		ndx = 0;
		fixMistakes = fix;
		qs.addAll(exam = newExam);
		List<View> rmvList = views().entrySet().stream().filter(v->v.getValue().viewId.toLowerCase().contains("image")).map(e->e.getValue()).collect(Collectors.toList());
		rmvList.stream().forEach(v->closeView(v));
		IntStream.range(0, newExam.getImages().size()).mapToObj(i->new View("image_"+(i+1), new JScrollPane(new JLabel(new ImageIcon(newExam.getImages().get(i).getScaledInstance(400, 300, BufferedImage.SCALE_SMOOTH)))), "image_"+(i+1))).forEach(v->addView(v));
		update();
	}
	public void update()
	{
		textArea.setText(exam.toString());
		previousPnl.setQuestion(currentPnl.getQuestion());
		currentPnl.setQuestion(qs.isEmpty()?Question.empty:qs.remove(0));
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
		dialog.set(startPanel.getExam(),startPanel.fixMistakes.isSelected());
		dialog.setVisible(true);
	}
}
