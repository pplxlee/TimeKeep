package pplxlee;

import javax.swing.JPanel;

public abstract class MyAnimatPanel extends JPanel implements Runnable {

	//返回ture则继续进入循环，返回false则结束
	abstract boolean change();
	abstract void delay();
	private void process(){
		while(change()){
			repaint();
			delay();
		}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		process();
	}

}
