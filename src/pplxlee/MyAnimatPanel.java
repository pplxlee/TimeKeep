package pplxlee;

import javax.swing.JPanel;

public abstract class MyAnimatPanel extends JPanel implements Runnable {

	//����ture���������ѭ��������false�����
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
