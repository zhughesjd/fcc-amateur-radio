package net.joshuahughes.fccamateurradio.examination.exam;

public class SubelementExam extends Exam
{
	private static final long serialVersionUID = -5592756316715145573L;
	public SubelementExam(Pool exam,String subelement)
	{
		super(exam);
		this.removeIf(q->
		{
			return !q.getSubelement().equals(subelement);
		});
	}
}