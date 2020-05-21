package game;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSlider;

import login.ButtonSound;

public class PaintCanvas {

	private Game game;
	private Image bufferImg;
	private Graphics2D bufferG;
	private int x1, y1, x2, y2, z1, z2;
	private ObjectOutputStream writer;
	private ObjectInputStream reader;
	private Socket socket;
	private Canvas canvas;
	private JFrame frame;
	private List<PaintDTO> list;
	private JButton blackBtn, redBtn, greenBtn, blueBtn, yellowBtn, eraserBtn, clearBtn, exitBtn, startBtn;
	private int color, thickness;
	private JSlider slider;
	private ButtonSound bs;
	
	//private final String serverIP = "localhost";
	private final String serverIP = "14.138.202.117";
	private final int port = 1593;

	public PaintCanvas(Game game) {

		// 컴포넌트 연결
		this.game = game;
		this.frame = game.getFrame();
		this.blackBtn = game.getBlackBtn();
		this.redBtn = game.getRedBtn();
		this.greenBtn = game.getGreenBtn();
		this.blueBtn = game.getBlueBtn();
		this.yellowBtn = game.getYellowBtn();
		this.eraserBtn = game.getEraserBtn();
		this.clearBtn = game.getClearBtn();
		this.slider = game.getSlider();
		this.exitBtn = game.getExitBtn();
		this.startBtn = game.getStartBtn();
		// 그림 담아둘 리스트 생성
		list = new ArrayList<PaintDTO>();

		// 네트워크 연결
		try {
			socket = new Socket(serverIP, port);
			writer = new ObjectOutputStream(socket.getOutputStream());
			reader = new ObjectInputStream(socket.getInputStream());
		} catch (UnknownHostException e) {
			System.out.println("서버 찾을 수 없음");
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			System.out.println("서버 연결 실패");
			e.printStackTrace();
			System.exit(0);
		}

		// 프레임에 전달해줄 캔버스 생성
		canvas = new Canvas() {

			// bufferG를 이용해 bufferImg에 완성힌 이미지를 표현해주는 paint 메소드
			@Override
			public void paint(Graphics g) {
				g.drawImage(bufferImg, 0, 0, this);
			}

			
			@Override
			public void update(Graphics g) {
				// bufferG가 생성되지 않았을 경우, bufferImg를 캔버스 크기만큼 세팅
				Dimension d = canvas.getSize();
				if (bufferG == null) {
					bufferImg = canvas.createImage(d.width, d.height);
					bufferG = (Graphics2D) bufferImg.getGraphics();
				}

				try {
					if (list == null)
						return;

					for (PaintDTO dto : list) {
						// 리스트에서 dto를 하나씩 꺼내서 그리되, 컬러와 굵기를 세팅한 후에 그린다
						int color = dto.getColor();
						if(color == 0) {bufferG.setColor(Color.black);}
						if(color == 1) {bufferG.setColor(Color.red);}
						if(color == 2) {bufferG.setColor(Color.green);}
						if(color == 3) {bufferG.setColor(Color.blue);}
						if(color == 4) {bufferG.setColor(Color.yellow);}
						if(color == 5) {bufferG.setColor(canvas.getBackground());}
						
						int stroke = dto.getStroke();
						bufferG.setStroke(new BasicStroke(stroke));
						
						x1 = dto.getX1();
						y1 = dto.getY1();
						x2 = dto.getX2();
						y2 = dto.getY2();
						bufferG.drawLine(x1, y1, x2, y2);
					}
				} catch (ConcurrentModificationException e) {
				}
				paint(g);
			}
		};
		canvas.setBackground(Color.white);

		// ------------------------------- button actions --------------------------------
		// X눌렀을 때
	
		// 검 버튼 눌렀을 때
		blackBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				bs = new ButtonSound();
				color = 0;
			}
		});
		// 빨 버튼 눌렀을 때
		redBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				bs = new ButtonSound();
				color = 1;
			}
		});
		// 초 버튼 눌렀을 때
		greenBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				bs = new ButtonSound();
				color = 2;
			}
		});
		// 파 버튼 눌렀을 때
		blueBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				bs = new ButtonSound();
				color = 3;
			}
		});
		// 노 버튼 눌렀을 때
		yellowBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				bs = new ButtonSound();
				color = 4;
			}
		});
		// 지우개 버튼 눌렀을 때
		eraserBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				bs = new ButtonSound();
				color = 5;
			}
		});
		// 초기화 버튼 눌렀을 때
		clearBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				bs = new ButtonSound();
				if(!game.getIsOwner()) return;
				PaintDTO dto = new PaintDTO();
				dto.setSignal(1);
				try {
					writer.writeObject(dto);
					writer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		startBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				bs = new ButtonSound();
				PaintDTO dto = new PaintDTO();
				dto.setSignal(1);
				try {
					writer.writeObject(dto);
					writer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		
		// 캔버스 위에서 마우스 휠을 굴릴 경우 슬라이더 옮겨주기
		canvas.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				thickness = slider.getValue();
				int n = e.getWheelRotation();
				if (n < 0) {
					slider.setValue(thickness + 3);
				} else if (n > 0) {
					slider.setValue(thickness - 3);
				}
			}
		});

		// 마우스 프레스
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(!(game.getIsOwner())) return;
				z1 = e.getX();
				z2 = e.getY();
			}
		});

		// -------------------------발신부---------------------------------
		// 마우스 드래그
		canvas.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if(!(game.getIsOwner())) return;
				x2 = e.getX();
				y2 = e.getY();

				PaintDTO dto = new PaintDTO();
				dto.setX1(z1); // 아주 짧은 선을 그릴 첫 좌표 두개
				dto.setY1(z2);
				dto.setX2(x2); // 아주 짧은 선을 그릴 두번째 좌표 두개
				dto.setY2(y2);
				dto.setSignal(2); // 2는 정상메시지, 1은 초기화, 3은 퇴장 시그널
				dto.setColor(color); // 필드선언된 int color는 버튼액션에 반응해서 변경된 후 dto에 삽입된다.
				dto.setStroke(slider.getValue()/4);

				try {
					writer.writeObject(dto);
					writer.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				z1 = e.getX();
				z2 = e.getY();
			}
		});

		// -------------------------발신부 끝---------------------------------
		// ------------------------------- End of actions--------------------------------

		// ------------------------수신부--------------------------------
		// -----------------------Thread----------------------
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {

				while (true) {
					try {
						// 서버로부터 좌표 4개를 담은 dto를 받는다
						
						
						
						PaintDTO dto = (PaintDTO) reader.readObject();

						if (dto.getSignal() == 1) {
							setNewCanvas();
						}

						if (dto.getSignal() == 2) {
							canvas.repaint();
							list.add(dto);
						}

						// dto의 signal이 3이면 종료한다.
						if (dto.getSignal() == 3) {
							writer.close();
							reader.close();
							socket.close();
							frame.dispose();
							break;
						}

					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}
		});
		t.setDaemon(true);
		t.start();
		// ------------------------수신부 끝--------------------------------
		// -----------------------End of Thread---------------------
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
	// PaintCanvas Constructor

	public void setNewCanvas() {
		list.clear();
		bufferG = null;
		canvas.repaint();
	}

	public void exit() {
		PaintDTO dto = new PaintDTO();
		dto.setSignal(3);
		try {
			writer.writeObject(dto);
			writer.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	
	public ObjectOutputStream getWriter() {return writer;}
	public ObjectInputStream getReader() {return reader;}
	public Socket getSocket() {return socket;}
	public Canvas getCanvas() {return canvas;}


}


