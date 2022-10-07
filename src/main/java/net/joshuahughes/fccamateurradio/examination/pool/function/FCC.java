package net.joshuahughes.fccamateurradio.examination.pool.function;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.joshuahughes.fccamateurradio.examination.LicenseClass;
import net.joshuahughes.fccamateurradio.examination.Question;
import net.joshuahughes.fccamateurradio.examination.Utility;
import net.joshuahughes.fccamateurradio.examination.pool.Exam;
import net.joshuahughes.fccamateurradio.examination.pool.Pool;

public class FCC implements Creator
{
	LicenseClass cls;
	public FCC(LicenseClass cls){this.cls = cls;}
	@Override
	public Exam apply(Pool pool)
	{
		Exam exam = new Exam(pool);
		List<Question> fccQuestions = IntStream.
				range(0, cls.getQuestionCount()).
				mapToObj(i->exam.remove(Utility.rnd.nextInt(exam.size()))).
				collect(Collectors.toList());
		exam.clear();
		exam.addAll(fccQuestions);
		return exam;
	}

}
