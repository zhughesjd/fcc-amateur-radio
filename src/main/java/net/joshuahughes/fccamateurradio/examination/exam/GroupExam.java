package net.joshuahughes.fccamateurradio.examination.exam;

public class GroupExam extends PoolExam
{
	private static final long serialVersionUID = -5592756316715145573L;
	public GroupExam(Exam exam,String group)
	{
		super(exam);
		this.removeIf(q->
		{
			return !q.getSubelement().equals(group);
		});
	}
}