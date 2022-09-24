package net.joshuahughes.fccamateurradio.exam;

import java.util.ArrayList;

import javax.swing.JTextArea;

public class ResultPanel extends JTextArea
{
	private static final long serialVersionUID = 8254183750756503379L;
	ArrayList<Result> list = new ArrayList<>();
	Result result;
	public ResultPanel(){}
	
	public void setExam(Exam exam)
	{
		list.add(this.result = new Result(exam));
		update();
	}
	private void update()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(getName());
		list.stream().forEach(r->
		{
			sb.append(r.toString());	
		});
		setText(sb.toString());
	}
	public void setName(String name)
	{
		super.setName(name);
		update();
	}

	public void setQuestion(Question question, int c)
	{
		result.put(question, question.getAnswer()==c);
		update();
	}
}