package net.joshuahughes.fccamateurradio.examination.ui;

import static com.metsci.glimpse.docking.DockingFrameCloseOperation.DISPOSE_ALL_FRAMES;
import static com.metsci.glimpse.docking.DockingUtils.setArrangementAndSaveOnDispose;
import static com.metsci.glimpse.docking.DockingWindowTitlers.createDefaultWindowTitler;
import static java.awt.Dialog.ModalityType.MODELESS;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
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
import net.joshuahughes.fccamateurradio.examination.pool.Pool;
import net.joshuahughes.fccamateurradio.examination.ui.question.ChoicePanel;
import net.joshuahughes.fccamateurradio.examination.ui.question.DisplayPanel;

public class ExamDialog extends DockingGroupDialog 
{
	LinkedHashSet<JDialog> dlgSet = new LinkedHashSet<>();
	DisplayPanel previousPnl = new DisplayPanel();
	ChoicePanel  currentPnl = new ChoicePanel();
	JTextArea textArea = new JTextArea();
	ArrayList<Question> pending = new ArrayList<>();
	Boolean appendWrong = null;
	Pool exam;
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
					pending.add(q);
			update();
		});
	}
	public void set(Pool newExam)
	{	
		currentPnl.setQuestion(Question.empty);
		previousPnl.setQuestion(Question.empty);
		pending.clear();
		pending.addAll(exam = newExam);
		appendWrong = null;
		exam.stream().forEach(q->q.setSelection(-1));
		List<View> rmvList = views().entrySet().stream().filter(v->v.getValue().viewId.toLowerCase().contains("image")).map(e->e.getValue()).collect(Collectors.toList());
		rmvList.stream().forEach(v->closeView(v));
		IntStream.range(0, newExam.getImages().size()).mapToObj(i->new View("image_"+(i+1), new JScrollPane(new JLabel(new ImageIcon(newExam.getImages().get(i).getScaledInstance(400, 300, BufferedImage.SCALE_SMOOTH)))), "image_"+(i+1))).forEach(v->addView(v));
		update();
	}
	public void update()
	{
		textArea.setText(exam.toString());
		Question previous = currentPnl.getQuestion();
		if(!previous.equals(Question.empty)) previousPnl.setQuestion(previous);
		if(pending.isEmpty())
		{
			currentPnl.setQuestion(Question.empty);
			return;
		}
		Optional<Question> op = pending.stream().filter(q->q.getState().equals(Question.State.pending)).findFirst();
		if(op.isPresent())
		{
			currentPnl.setQuestion(pending.remove(0));
			return;
		}
		if(appendWrong == null)
		{
			appendWrong = JOptionPane.showConfirmDialog(currentPnl, "fix mistakes?", "fix mistakes?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
		}
		if(!appendWrong)
		{
			pending.clear();
			return;
		}
		Optional<Question> wrongOp = pending.stream().filter(q->q.getState().equals(Question.State.wrong)).findFirst();
		if(wrongOp.isPresent())
		{
			currentPnl.setQuestion(pending.remove(0));
		}
	}
	public void setVisible(boolean visible)
	{
		super.setVisible(visible);
		currentPnl.requestFocus();
	}
}
