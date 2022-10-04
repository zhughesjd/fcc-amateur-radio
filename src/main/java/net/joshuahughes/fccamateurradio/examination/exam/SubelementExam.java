package net.joshuahughes.fccamateurradio.examination.exam;

public class SubelementExam extends PoolExam
{
	private static final long serialVersionUID = -5592756316715145573L;
	public SubelementExam(Exam exam,String subelement)
	{
		super(exam);
		this.removeIf(q->
		{
			return !q.getSubelement().equals(subelement);
		});
	}
}