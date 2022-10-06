package net.joshuahughes.fccamateurradio.examination.exam;

public class Contains extends Exam
{
	private static final long serialVersionUID = -5592756316715145573L;
	public Contains(Pool exam,String contains) {
		super(exam);
		removeIf(q->
		{
			if(q.getQuestion().toLowerCase().contains(contains.toLowerCase())) return false;
			return !q.stream().anyMatch(c->c.toLowerCase().contains(contains));
		});
	}
}