package pplxlee;

import java.util.Timer;
import java.util.TimerTask;

public class TimerSys{
	final long minuteTime = 1000L * 60L;
	//默认提醒周期5minutes
	private long remindPeriodInMinute = 5;
	private long remindPeriod = remindPeriodInMinute * minuteTime;
	private Timer timer = new Timer();
	private RefreshUI ru;

	public void setUISetter(RefreshUI ru){
		this.ru = ru;
	}
	
	public void startKeep(int delayInMinute){
		if(delayInMinute<5){
			System.out.println("最少集中5分钟！");
			ru.error("最少集中5分钟！");
			return;
		}
		System.out.println("集中注意力！开始！"+delayInMinute);
		ru.start();
		timer.schedule(new KeepUpTimerTask(), delayInMinute*minuteTime);
		timer.schedule(new ReminderInKeepTime(), remindPeriod, remindPeriod);
		timer.schedule(new LastFiveMinutesReminder(), (delayInMinute-5)*minuteTime);
	}

	// 用于更新UI的接口
	public interface RefreshUI {
		void start();
		
		void error(String info);

		void remind(String s);

		void remind(String s, boolean priority);

		void timeUp();
	}

	// keep时间结束
	public class KeepUpTimerTask extends TimerTask{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			System.out.println("修行成功！你又变强了！休息一下，再感受自己的力量吧！");
			ru.timeUp();
			TimerSys.this.timer.cancel();
		}

	}

	//每隔一段时间提醒
	public class ReminderInKeepTime extends TimerTask{

		private int count = 0;
		@Override
		public void run() {
			// TODO Auto-generated method stub
			count++;
			System.out.println("你已坚持" + count * remindPeriodInMinute + "分钟！");
			ru.remind("你已坚持" + count * remindPeriodInMinute + "分钟！");
		}

	}

	//还剩5分钟提示
	public class LastFiveMinutesReminder extends TimerTask{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			System.out.println("还剩最后5分钟，坚持就是胜利！");
			ru.remind("还剩最后5分钟，坚持就是胜利！",true);
		}
		
	}
	
	//设置
	public class setter{
		public void set(int remindPeriodInMinute){
			TimerSys.this.remindPeriod = remindPeriodInMinute * minuteTime;
		}
	}

}

