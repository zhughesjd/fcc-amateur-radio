package net.joshuahughes.fccamateurradio.examination.exam;

import static com.metsci.glimpse.docking.DockingFrameCloseOperation.DISPOSE_ALL_FRAMES;
import static com.metsci.glimpse.docking.DockingUtils.setArrangementAndSaveOnDispose;
import static com.metsci.glimpse.docking.DockingWindowTitlers.createDefaultWindowTitler;
import static java.awt.Dialog.ModalityType.MODELESS;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.apache.commons.math3.stat.Frequency;
import org.apache.poi.extractor.POITextExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import com.metsci.glimpse.docking.View;
import com.metsci.glimpse.docking.examples.ModalDialogDockingExample;
import com.metsci.glimpse.docking.group.dialog.DockingGroupDialog;

import net.joshuahughes.fccamateurradio.examination.Question;
import net.joshuahughes.fccamateurradio.examination.ui.ResultPanel;
import net.joshuahughes.fccamateurradio.examination.ui.StartPanel;
import net.joshuahughes.fccamateurradio.examination.ui.question.ChoicePanel;
import net.joshuahughes.fccamateurradio.examination.ui.question.DisplayPanel;

public class FccAmateurRadioExam extends DockingGroupDialog 
{
	public static Random rnd = new Random(System.currentTimeMillis());
	LinkedHashSet<JDialog> dlgSet = new LinkedHashSet<>();
	DisplayPanel previousPnl = new DisplayPanel();
	ChoicePanel  currentPnl = new ChoicePanel();
	ResultPanel resultPnl = new ResultPanel();
	boolean appendWrong = false;
	Question previousQuestion;
	Exam exam;
	public FccAmateurRadioExam()
	{
		super(null, MODELESS, DISPOSE_ALL_FRAMES );
        addListener( createDefaultWindowTitler( "Docking Example" ) );
        setArrangementAndSaveOnDispose( this, FccAmateurRadioExam.class.getName(), ModalDialogDockingExample.class.getResource( "docking/simple-arrangement-default.xml" ) );
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
			update((Integer) l.getNewValue());
		});
	}
	public void set(Exam newExam, boolean appendWrong)
	{	
		this.appendWrong = appendWrong;
		if(newExam.isEmpty()) return;
		exam = newExam;
		resultPnl.setExam(exam);
		List<View> rmvList = views().entrySet().stream().filter(v->v.getValue().viewId.toLowerCase().contains("image")).map(e->e.getValue()).collect(Collectors.toList());
		rmvList.stream().forEach(v->closeView(v));
		IntStream.range(0, exam.getImages().size()).mapToObj(i->new View("image"+i, new JScrollPane(new JLabel(new ImageIcon(exam.getImages().get(i).getScaledInstance(400, 300, BufferedImage.SCALE_SMOOTH)))), "image"+i)).forEach(v->addView(v));
		update(-1);
	}
	public void update(int c)
	{
		if(previousQuestion!=null)
		{
			previousPnl.setQuestion(previousQuestion,c);
			resultPnl.setQuestion(previousQuestion,c);
		}
		if(exam.isEmpty())
		{
			currentPnl.setQuestion(Question.empty);
			return;
		}
		if(previousQuestion!=null && c != previousQuestion.getAnswer() && appendWrong)
			exam.add(previousQuestion);
		currentPnl.setQuestion(previousQuestion = exam.remove(0));
	}
	public static Exam convert(File file)
	{
		try
		{
			FileInputStream fis = new FileInputStream(file);
			XWPFDocument doc = new XWPFDocument(fis);
			POITextExtractor extractor = new XWPFWordExtractor(doc);
			String string = extractor.getText();
			List<BufferedImage> imageList = doc.getAllPictures().stream().map(p->
			{
				try
				{
					return ImageIO.read(new ByteArrayInputStream(p.getData()));
					
				}
				catch (Exception e)
				{
					return (BufferedImage)null;
				}
			}).collect(Collectors.toList());
			extractor.close();
			return new Exam(file.getName(),string,imageList);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	private static String answerStats(Exam exam)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
		PrintStream ps = new PrintStream(baos);
		Frequency singular = new Frequency(); 
		Frequency inclusive = new Frequency(); 
		ps.println("---------------------------------------------------------------");
		ps.println(exam.toString());
		ps.println("---------------------------------------------------------------");
		exam.stream().forEach(q->
		{
			Frequency f =q.get(3).toLowerCase().contains("are correct")?inclusive:singular;
			f.addValue((char)(q.getAnswer()+'A'));
		
		});
		ps.println("------- singular --------");
		ps.println(toString(singular));
		ps.println("------- inclusive --------");
		ps.println(toString(inclusive));
		ps.close();
		return new String(baos.toByteArray());
	}
	private static String toString(Frequency f)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
		PrintStream ps = new PrintStream(baos);
		Iterable<Map.Entry<Comparable<?>,Long>> iterable = () -> f.entrySetIterator();
		StreamSupport.stream(iterable.spliterator(), false).forEach(e->ps.println(e.getKey()+" -> "+f.getPct(e.getKey())));
		return new String(baos.toByteArray());
	}
	public static void main(String[] args) throws Exception
	{
		URL pathUrl = FccAmateurRadioExam.class.getClassLoader().getResource("docx/");
		List<Exam> list = Arrays.stream(new File(pathUrl.getFile()).listFiles()).map(f->convert(f)).filter(t->t!=null).collect(Collectors.toList());
		StringBuilder prefix = new StringBuilder();
		list.forEach(e->prefix.append(answerStats(e)));
		StartPanel startPanel = new StartPanel(list);
		if(JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(null, startPanel,"select below",JOptionPane.OK_CANCEL_OPTION))
			return;
		FccAmateurRadioExam test = new FccAmateurRadioExam();
		test.resultPnl.setName(prefix.toString());
		test.setVisible(true);
		Exam exam = startPanel.getList().get(0);
		test.set(exam,startPanel.appendWrong());
	}
}
