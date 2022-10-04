package net.joshuahughes.fccamateurradio.examination.exam;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import net.joshuahughes.fccamateurradio.examination.Question;
import net.joshuahughes.fccamateurradio.examination.Utility;
import net.joshuahughes.fccamateurradio.examination.Utility.Class;

public class FCCExam extends PoolExam
{
	private static final long serialVersionUID = -2085521442066356828L;

	public FCCExam(Exam exam)
	{
		super(exam);
		String string = exam.toString();
		Class cls = Stream.of(Utility.Class.values()).filter(c->string.toLowerCase().contains(c.name())).findAny().get();
		List<Question> fccQuestions = IntStream.
				range(0, cls.getQuestionCount()).
				mapToObj(i->remove(Utility.rnd.nextInt(size()))).
				collect(Collectors.toList());
		clear();
		addAll(fccQuestions);
	}
}
