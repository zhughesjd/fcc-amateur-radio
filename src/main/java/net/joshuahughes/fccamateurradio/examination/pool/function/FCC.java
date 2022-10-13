package net.joshuahughes.fccamateurradio.examination.pool.function;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.joshuahughes.fccamateurradio.examination.LicenseClass;
import net.joshuahughes.fccamateurradio.examination.Question;
import net.joshuahughes.fccamateurradio.examination.Utility;
import net.joshuahughes.fccamateurradio.examination.pool.Exam;
import net.joshuahughes.fccamateurradio.examination.pool.Pool;
import net.joshuahughes.fccamateurradio.examination.pool.PoolImpl;

public class FCC implements Creator
{
	LicenseClass cls;
	public FCC(LicenseClass cls){this.cls = cls;}
	@Override
	public Exam apply(Pool pool)
	{
		Set<String> groups = pool.stream().map(q->q.getGroup()).distinct().collect(Collectors.toSet());
		List<Question> qList = groups.stream().map(g->
		{
			List<Question> qs = pool.stream().filter(q->q.getGroup().equals(g.toString())).collect(Collectors.toList());
			return qs.get(Utility.rnd.nextInt(qs.size()));
		}).collect(Collectors.toList());
		PoolImpl poolImpl = new PoolImpl();
		poolImpl.addAll(qList);
		poolImpl.getImages().addAll(pool.getImages());
		Exam exam = new Exam(poolImpl);
		List<Question> fccQuestions = IntStream.
				range(0, cls.getQuestionCount()).
				mapToObj(i->exam.remove(Utility.rnd.nextInt(exam.size()))).
				collect(Collectors.toList());
		exam.clear();
		exam.addAll(fccQuestions);
		return exam;
	}
}
