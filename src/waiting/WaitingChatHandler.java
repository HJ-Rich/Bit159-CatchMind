package waiting;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class WaitingChatHandler extends Thread { // 핸들러가 소켓,IO(reader,writer) 를 쥐고있음.
	private ObjectInputStream reader;
	private ObjectOutputStream writer;
	private Socket socket;
	private List<WaitingChatHandler> list;
	private String nickName, receivedName;

	/*
	 * 1) 서버가 클라이언트를 낚아채서 핸들러와 짝궁 지어줄 때 list의 주소값을 주는 것이기 때문에 해당 리스트에 추가되는 값, 제거되는
	 * 값이 동일하게 적용된다. 만들어진 list 객체를 넘겨주는 게 아니라 list객체가 있는 곳(주소)을 알려주는 것이다!
	 * 
	 * 2) 최초 입장 메시지 처리 시에 dto에서 닉네임 값을 꺼내서 필드에 선언해둔 nickname이 닉네임 값의 주소값을 갖도록 연결!
	 * 
	 * 1과 2로 인해 같은 list객체 주소값만 가지고 있다면 그 리스트에서 특정 닉네임을 갖고 있는 핸들러를 특정할 수 있고 특정한 뒤에
	 * 귓속말, 쪽지, 채팅 기록 db저장 등의 기능으로 확장시킬 수 있다.
	 */

	public WaitingChatHandler(Socket socket, List<WaitingChatHandler> list) {
		this.socket = socket;
		this.list = list;
		try {
			writer = new ObjectOutputStream(socket.getOutputStream());
			reader = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		WaitingChatDTO dto = null;
		nickName = null;
		// 클라이언트로부터 받기
		// 닉네임을 먼저 받는다.
		try {
			while (true) {
				dto = (WaitingChatDTO) reader.readObject();

				if (dto.getCommand() == Info.JOIN) { // 입장시
					nickName = dto.getNickName();
					WaitingChatDTO sendDTO = new WaitingChatDTO();
					sendDTO.setCommand(Info.SEND);
					sendDTO.setMessage(nickName + "님 입장하셨습니다");
					broadcast(sendDTO);

				} else if (dto.getCommand() == Info.EXIT) { // 종료시
					WaitingChatDTO sendDTO = new WaitingChatDTO();
					// 나가려고 exit를 보낸 클라이언트에게 답변 보내기
					sendDTO.setCommand(Info.EXIT);
					writer.writeObject(sendDTO);
					writer.flush();

					reader.close();
					writer.close();
					socket.close();
					// 남아있는 클라이언트에게 퇴장메세지 보내기
					list.remove(this);

					sendDTO.setCommand(Info.SEND);
					sendDTO.setMessage(nickName + "님 퇴장하셨습니다");

					broadcast(sendDTO);
					break;

				} else if (dto.getCommand() == Info.SEND) {
					WaitingChatDTO sendDTO = new WaitingChatDTO();
					sendDTO.setCommand(Info.SEND);
					String msg = dto.getMessage();
					nickName = dto.getNickName();
					sendDTO.setMessage("[" + nickName + "]: " + msg);

					broadcast(sendDTO);

				} else if (dto.getCommand() == Info.MESSAGE) {
					messageCast(dto);

				} else if (dto.getCommand() == Info.WHISPER) {
					WaitingChatDTO sendDTO = new WaitingChatDTO();
					receivedName = dto.getReceivedName();
					sendDTO.setMessage("[" + nickName + " -> " + receivedName + "]  : " + dto.getMessage());
					sendDTO.setCommand(Info.SEND);
					sendDTO.setReceivedName(receivedName);

					whisper(sendDTO);

				}
			} // while
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	public void broadcast(WaitingChatDTO dto) {
		for (WaitingChatHandler cho : list) {
			try {
				cho.writer.writeObject(dto);
				cho.writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public void messageCast(WaitingChatDTO dto) {
		for (WaitingChatHandler handler : list) {
			if (handler.nickName.equalsIgnoreCase(dto.getReceivedName())) {
				try {
					handler.writer.writeObject(dto);
					handler.writer.flush();

				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}
	}

	public void whisper(WaitingChatDTO dto) {
		for (WaitingChatHandler handler : list) {
			if (handler.nickName.equalsIgnoreCase(dto.getReceivedName())) {
				try {
					handler.writer.writeObject(dto);
					handler.writer.flush();

				} catch (IOException e) {
					e.printStackTrace();
				}

			} else if (handler.nickName.equalsIgnoreCase(this.nickName)) {
				try {
					handler.writer.writeObject(dto);
					handler.writer.flush();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

}// ChatHandler
