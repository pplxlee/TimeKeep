package pplxlee;

import java.util.Timer;
import java.util.TimerTask;

public class TimerSys{
	final long minuteTime = 1000L * 60L;
	//Ĭ����������5minutes
	private long remindPeriodInMinute = 5;
	private long remindPeriod = remindPeriodInMinute * minuteTime;
	private Timer timer = new Timer();
	private RefreshUI ru;

	public void setUISetter(RefreshUI ru){
		this.ru = ru;
	}
	
	public void startKeep(int delayInMinute){
		if(delayInMinute<5){
			System.out.println("���ټ���5���ӣ�");
			ru.error("���ټ���5���ӣ�");
			return;
		}
		System.out.println("����ע��������ʼ��"+delayInMinute);
		ru.start();
		timer.schedule(new KeepUpTimerTask(), delayInMinute*minuteTime);
		timer.schedule(new ReminderInKeepTime(), remindPeriod, remindPeriod);
		timer.schedule(new LastFiveMinutesReminder(), (delayInMinute-5)*minuteTime);
	}

	// ���ڸ���UI�Ľӿ�
	public interface RefreshUI {
		void start();
		
		void error(String info);

		void remind(String s);

		void remind(String s, boolean priority);

		void timeUp();
	}

	// keepʱ�����
	public class KeepUpTimerTask extends TimerTask{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			System.out.println("���гɹ������ֱ�ǿ�ˣ���Ϣһ�£��ٸ����Լ��������ɣ�");
			ru.timeUp();
			TimerSys.this.timer.cancel();
		}

	}

	//ÿ��һ��ʱ������
	public class ReminderInKeepTime extends TimerTask{

		private int count = 0;
		@Override
		public void run() {
			// TODO Auto-generated method stub
			count++;
			System.out.println("���Ѽ��" + count * remindPeriodInMinute + "���ӣ�");
			ru.remind("���Ѽ��" + count * remindPeriodInMinute + "���ӣ�");
		}

	}

	//��ʣ5������ʾ
	public class LastFiveMinutesReminder extends TimerTask{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			System.out.println("��ʣ���5���ӣ���־���ʤ����");
			ru.remind("��ʣ���5���ӣ���־���ʤ����",true);
		}
		
	}
	
	//����
	public class setter{
		public void set(int remindPeriodInMinute){
			TimerSys.this.remindPeriod = remindPeriodInMinute * minuteTime;
		}
	}

}

