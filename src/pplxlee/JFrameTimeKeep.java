package pplxlee;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JFrameTimeKeep extends JFrame {
	
	public JFrameTimeKeep() {
		setUndecorated(true);
		setVisible(true);
		setSize(87, 105);
		setAlwaysOnTop(true);
	}

	private void showCute() {
		MyCuteAPanel panel = new MyCuteAPanel();// 得到面板对象
		panel.setBackground(Color.WHITE);
		Thread t = new Thread(panel);// 启动面板的动画线程
		t.start();
		getContentPane().add(panel);// 将面板加载到Frame主窗口里
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrameTimeKeep frame = new JFrameTimeKeep();
					frame.setVisible(true);
					frame.showCute();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public class MyCuteAPanel extends MyAnimatPanel implements ActionListener {
		//这几个可以给用户设置
		private int keepTime = 25;		//默认每次专注25分钟
		private Point afterKeepWindowLocation = new Point(500,300);
		
		final JPopupMenu rightButtonMenu = new JPopupMenu();
		final JMenuItem itemKeep = new JMenuItem("集中注意力"+keepTime+"分钟");
		final JMenuItem itemExit = new JMenuItem("退出");
		
		private final int REFRESH_INTERVAL = 150;
		private final int STATE_KEEP = 0;
		private final int STATE_DIZZY = 1;
		private final int STATE_CHEER = 2;
		private final int STATE_RUN = 3;
		private final int STATE_DARG = 4;
		private int state = STATE_RUN;
		private int stateTemp = 0;
		private final String[] stateType = { "KEEP", "DIZZY", "CHEER", "RUN", "DARG" };

		private final int[] imageNum = { 4, 4, 4, 4, 3 };
		private Image[][] images = new Image[5][];
		private String string = "感觉自己萌萌哒";
		private int imageIndex = 0;
		private Image image;
		private TimerSys timerSys;
		private boolean busy = false;

		public MyCuteAPanel() {
			init();
		}

		private void init() {
			initImgResource();
			initTimerSys();
			initRightButtonMenu();
			initMouseEvent();
		}
		private void initImgResource() {
			for (int i = 0; i < stateType.length; i++) {
				images[i] = new Image[imageNum[i]];
				for (int j = 0; j < imageNum[i]; j++) {
					System.out.println("img/" + stateType[i] + j + ".jpg");
					try {
						java.net.URL imgURL = JFrameTimeKeep.class.getResource("/img/" + stateType[i] + j + ".jpg");
						images[i][j] = ImageIO.read(imgURL);

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		private void initTimerSys(){
			timerSys = new TimerSys();
			timerSys.setUISetter(new TimerSysRefreshUI());
		}
		private void initRightButtonMenu() {

			rightButtonMenu.add(itemKeep);
			rightButtonMenu.add(itemExit);
			itemKeep.addActionListener(this);
			itemExit.addActionListener(this);
			
		}
		private void initMouseEvent(){
			MouseAdapter ma = new MouseAdapter() {
				Point origin;
				boolean isPressed = false;

				@Override
				public void mouseEntered(MouseEvent e) {
					if (!isPressed) {
						System.out.println("进入");
						busy = true;
						stateTemp = state;
						state = STATE_DIZZY;
						imageIndex = 0;
					}
				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					if (!isPressed) {
						System.out.println("离开");
						state = stateTemp;
						imageIndex = 0;
						busy = false;
					}
				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					busy = true;
					isPressed = true;
					System.out.println("按下");
					origin = e.getPoint();
					state = STATE_DARG;
					imageIndex = 0;
				}

				@Override
				public void mouseDragged(MouseEvent e) {
					// TODO Auto-generated method stub
					// state = 4;
					System.out.println("拖动");
					Point p = JFrameTimeKeep.this.getLocation();
					JFrameTimeKeep.this.setLocation(p.x + (e.getX() - origin.x), p.y + (e.getY() - origin.y));
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					isPressed = false;
					busy = false;
					System.out.println("松开");
					state = STATE_DARG;
					imageIndex = 0;
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getButton() == MouseEvent.BUTTON3) {
						// 弹出菜单
						rightButtonMenu.show(MyCuteAPanel.this, e.getX(), e.getY());
					}
					else if(e.getButton() == MouseEvent.BUTTON1){
						System.out.println("点击");
						
					}
				}
			};
			addMouseListener(ma);
			addMouseMotionListener(ma);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String str = e.getActionCommand();
			if (str.equals("集中注意力"+keepTime+"分钟")) {
				timerSys.startKeep(keepTime); // 专注时间
			} else if (str.equals("退出")) {
				System.exit(0);
			}
		}
		
		@Override
		public void paint(Graphics g) {
			super.paint(g);

			g.drawImage(image, 6, 0, null);
			g.setColor(Color.RED);
			g.drawString(string, 1, 95);
			paintComponents(g);
		}

		@Override
		boolean change() {
			// TODO Auto-generated method stub

			imageIndex++;
			if (imageIndex == imageNum[state]) {
				imageIndex = 0;
				// return false;
			}
			image = images[state][imageIndex];
			return true;

		}

		@Override
		void delay() {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(REFRESH_INTERVAL);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public class TimerSysRefreshUI implements TimerSys.RefreshUI {

			private Timer timer = new Timer();
			boolean uiBusy = false;

			private void setMyCuteAPanelState(int state) {
				if (MyCuteAPanel.this.busy) {
					MyCuteAPanel.this.stateTemp = state;
				} else {
					MyCuteAPanel.this.state = state;
				}
			}

			@Override
			public void start() {
				// TODO Auto-generated method stub
				uiBusy = true;
				itemKeep.setEnabled(false);
				setMyCuteAPanelState(STATE_CHEER);
				string = "专注开始！";
				timer.schedule(new TurnToKeepTimerTask(), 4000);
			}

			@Override
			public void remind(String s) {
				// TODO Auto-generated method stub
				if (!uiBusy) {
					setMyCuteAPanelState(STATE_CHEER);
					string = s;
					timer.schedule(new TurnToKeepTimerTask(), 4000);
				}
			}

			@Override
			public void remind(String s, boolean priority) {
				// TODO Auto-generated method stub
				uiBusy = true;
				setMyCuteAPanelState(STATE_CHEER);
				string = s;
				timer.schedule(new TurnToKeepTimerTask(), 4000);
			}

			@Override
			public void timeUp() {
				// TODO Auto-generated method stub
				Toolkit toolKit = Toolkit.getDefaultToolkit();
				toolKit.beep();
				
				uiBusy = true;
				itemKeep.setEnabled(true);
				setMyCuteAPanelState(STATE_CHEER);
				string = "你变的更强了！";
				timer.schedule(new TurnToReadyTimerTask(), 10000);
				
				JFrameTimeKeep.this.setLocation(afterKeepWindowLocation);
			}

			@Override
			public void error(String info) {
				// TODO Auto-generated method stub
				uiBusy = true;
				setMyCuteAPanelState(STATE_DIZZY);
				string = info;
				timer.schedule(new TurnToReadyTimerTask(), 5000);
			}

			public class TurnToKeepTimerTask extends TimerTask {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					uiBusy = false;
					string = "专注尽你所能";
					setMyCuteAPanelState(STATE_KEEP);
				}
			}

			public class TurnToReadyTimerTask extends TimerTask {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					uiBusy = false;
					string = "⑨是最强的！";
					setMyCuteAPanelState(STATE_RUN);
				}

			}

		}
	}

}
