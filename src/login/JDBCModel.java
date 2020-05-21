package login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;

import waiting.WaitingChatDTO;

//DAO
public class JDBCModel {
	// 14.138.202.117:159
	private final String ip = "14.138.202.117:159:xe";
// private final String ip = "localhost:1521:xe";
	private Connection connection;
	private Statement statement;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;

	public JDBCModel() {

		System.out.println("연결시도중0");

// 1. 해당 데이터 베이스에 대한 라이브러리 등록 작업
// Class.forName("클래스명"); // ClassNotFoundException 처리를 해야 함
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

// 2.데이터베이스와 연결함 // SQLException 처리를 해야한다.
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");
			System.out.println(connection);

// 3. 쿼리문 작성 후 DB에서 쿼리문 실행시키고 결과를 가지고 옴
			statement = connection.createStatement();
			String query = "SELECT * FROM db WHERE email is not null order by email";
			resultSet = statement.executeQuery(query);
			System.out.println("이메일주소" + "\t\t" + "비밀번호" + "\t" + "닉네임" + "\t" + "핸드폰번호");
			System.out.println("-------------------------------------");
			while (resultSet.next()) {
				System.out.print(resultSet.getString("email") + "\t");
				System.out.print(resultSet.getString("pw") + "\t");
				System.out.print(resultSet.getString("nickname") + "\t");
				System.out.println(resultSet.getString("phone") + "\t");
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {
				// 4. DB와 관련된 객체는 반드시 close 해야 함
				resultSet.close();
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
// JDBCModel Constructor
// select room1593.nickname, db.score from room1593, db where room1593.nickname
// = db.nickname;

	public String randomAnswer() {
		String answer = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");

			String query = "select * from (select * from answer order by dbms_random.value) where rownum <= 1";
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				answer = resultSet.getString("answer");
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				resultSet.close();
				preparedStatement.close();
				connection.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return answer;

	}

	public void displayForRoom(int roomNumber, JLabel nickname1, JLabel nickname2, JLabel nickname3, JLabel nickname4,
			JLabel score1, JLabel score2, JLabel score3, JLabel score4) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");

			String query = "SELECT nickname, score from room1593 where no = 1";
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				nickname1.setText(resultSet.getString("nickname"));
				score1.setText(resultSet.getInt("score") + "");
			}

			query = "SELECT nickname, score from room1593 where no = 2";
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				nickname2.setText(resultSet.getString("nickname"));
				score2.setText(resultSet.getInt("score") + "");
			}

			query = "SELECT nickname, score from room1593 where no = 3";
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				nickname3.setText(resultSet.getString("nickname"));
				score3.setText(resultSet.getInt("score") + "");
			}

			query = "SELECT nickname, score from room1593 where no = 4";
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				nickname4.setText(resultSet.getString("nickname"));
				score4.setText(resultSet.getInt("score") + "");
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				connection.close();
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void signUp(String email, String pw, String nickName, String mobile) {

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");

			String query = "INSERT INTO db(email, pw, nickname, phone, lasttime, isonline, istemppw, score) Values(?,?,?,?,sysdate,0,0,0)";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, pw);
			preparedStatement.setString(3, nickName);
			preparedStatement.setString(4, mobile);
			preparedStatement.executeUpdate();// 실행
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public String getEmailByPhone(String phone) {
		String result = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");

			String query = "SELECT email FROM db WHERE PHONE = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, phone);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				result = resultSet.getString("email");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				connection.close();
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public List<String> getOnlineNickNames() {
		String tempResult = null;
		List<String> list = new ArrayList<String>();
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");
			String query = "SELECT nickname FROM db WHERE isonline = 1";
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				list.add(resultSet.getString("nickname"));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				connection.close();
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	/*
	 * public DefaultTableModel getRooms() { String[] userField = { "번호", "방제목",
	 * "방장", "인원", "비고", "상태" }; DefaultTableModel model = new
	 * DefaultTableModel(userField, 0);
	 * 
	 * try { Class.forName("oracle.jdbc.driver.OracleDriver"); connection =
	 * DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");
	 * String query = "SELECT * FROM room"; preparedStatement =
	 * connection.prepareStatement(query); resultSet =
	 * preparedStatement.executeQuery(); while (resultSet.next()) { Object[] data =
	 * { resultSet.getInt("no"), resultSet.getString("title"),
	 * resultSet.getString("owner"), resultSet.getInt("people"),
	 * resultSet.getInt("isopen"), resultSet.getInt("status") }; for (Object object
	 * : data) { System.out.println(object); } model.addRow(data); } } catch
	 * (ClassNotFoundException e) { e.printStackTrace(); } catch (SQLException e) {
	 * e.printStackTrace(); } finally { try { preparedStatement.close();
	 * connection.close(); resultSet.close(); } catch (SQLException e) {
	 * e.printStackTrace(); } } return model; }
	 */
//	public String[] getOnlineUsers(String phone) {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rset = null;
//		String result[] = null;
//		try {
//			Class.forName("oracle.jdbc.driver.OracleDriver");
//			conn = DriverManager.getConnection("jdbc:oracle:thin:@"+ip+":1521:xe", "c##catch", "bit");
//			
//			String query = "SELECT email FROM db WHERE PHONE = ?";
//			pstmt = conn.prepareStatement(query);
//			pstmt.setString(1, phone);
//			rset = pstmt.executeQuery();
//			while(rset.next()) {
//				result = rset.getString("email");
//			}
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				pstmt.close();
//				conn.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//		return result;
//	}

	public String getEmailByNickName(String nickName) {
		String result = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");

			String query = "SELECT email FROM db WHERE nickname = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, nickName);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				result = resultSet.getString("email");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				connection.close();
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

// 이메일 주소로 닉네임 불러오는 메소드
	public String getNickNameByEmail(String email) {
		String result = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");

			String query = "SELECT nickname FROM db WHERE email = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, email);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				result = resultSet.getString("nickname");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				connection.close();
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

// 이메일 주소로 핸드폰 불러오는 메소드
	public String getPhoneByEmail(String email) {
		String result = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");

			String query = "SELECT phone FROM db WHERE email = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, email);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				result = resultSet.getString("phone");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				connection.close();
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

// 이메일로 비번 가져오는 메소드. 비번 변경시 기존 비번과 같은지 확인할 때 사용.
	public String getPWByEmail(String email) {
		String result = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");

			String query = "SELECT pw FROM db WHERE email = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, email);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				result = resultSet.getString("pw");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				connection.close();
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public boolean isTempPW(String email) {
		String result = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");

			String query = "SELECT istempPW FROM db WHERE email = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, email);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				result = resultSet.getString("istempPW");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				connection.close();
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result.equals("1") ? true : false;
	}

// 입력한 이메일과 비밀번호가 매칭되는지 확인하는 메소드
	public boolean accountVerifier(String email, String pw) {
		boolean result = false;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");
			statement = connection.createStatement();

			String query = "SELECT pw FROM db WHERE email = " + "'" + email + "'";
			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				if (resultSet.getString("pw").equals(pw)) {
					result = true;
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				resultSet.close();
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public void update() {

	}

	public void delete() {

	}

// 접속 여부 검증 메소드(off면 true, on이면 false)
	public boolean isNotOnline(String userInputID) {
		boolean result = false;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");
			statement = connection.createStatement();

			String query = "SELECT isOnline FROM db WHERE email = " + "'" + userInputID + "'";
			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				if (Integer.parseInt(resultSet.getString("isOnline")) == 0) {
					result = true;
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				resultSet.close();
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

// 이메일 중복 검증 메소드
	public boolean hasEmail(String email) {
		boolean result = false;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");
			statement = connection.createStatement();

			String query = "SELECT email FROM db WHERE email = " + "'" + email + "'";
			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				if (resultSet.getString("email") != null) {
					result = true;
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				resultSet.close();
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

// 닉네임 중복 검증 메소드
	public boolean hasNickName(String nickName) {
		boolean result = false;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");
			statement = connection.createStatement();

			String query = "SELECT nickname FROM db WHERE nickname = " + "'" + nickName + "'";
			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				if (resultSet.getString("nickname") != null) {
					result = true;
				}
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				resultSet.close();
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

// 핸드폰 중복 검증 메소드
	public boolean hasPhone(String phone) {
		boolean result = false;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");
			statement = connection.createStatement();

			String query = "SELECT phone FROM db WHERE phone = " + "'" + phone + "'";
			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				if (resultSet.getString("phone") != null) {
					result = true;
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				resultSet.close();
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

// 비번 변경하는 메소드 (임시 비번 메일 발송시 해당 임시 비번을 db에 적용하기 위한 메소드)
	public void setPW(String email, String tempPW) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");
			String query = "UPDATE db set pw = ? WHERE email = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, tempPW);
			preparedStatement.setString(2, email);
			preparedStatement.executeUpdate();// 실행
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

// 닉네임 변경시 사용되는 메소드
	public void setNickName(String email, String newNickName) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");
			String query = "UPDATE db set nickname = ? WHERE email = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, newNickName);
			preparedStatement.setString(2, email);
			preparedStatement.executeUpdate();// 실행
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

// 핸드폰 변경시 사용되는 메소드
	public void setPhone(String email, String newPhone) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");
			String query = "UPDATE db set phone = ? WHERE email = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, newPhone);
			preparedStatement.setString(2, email);
			preparedStatement.executeUpdate();// 실행
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

// 임시비밀번호 사용중이라는 세팅을 위한 메소드
	public void setTempPW(String email, int i) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");
			String query = "UPDATE db set istemppw = ? WHERE email = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, i + "");
			preparedStatement.setString(2, email);
			preparedStatement.executeUpdate();// 실행
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void setOnline(String email) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");
			String query = "UPDATE db set isOnline = 1 WHERE email = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, email);
			preparedStatement.executeUpdate();// 실행
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void setOffline(String email) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");
			String query = "UPDATE db set isOnline = 0 WHERE email = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, email);
			preparedStatement.executeUpdate();// 실행
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void createRoom(String email) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");
			String query = "INSERT INTO room VALUES()";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, email);
			preparedStatement.executeUpdate();// 실행
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

// 이메일 주소로 스코어 불러오는 메소드
	public int getScoreByEmail(String email) {
		int result = 0;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");

			String query = "SELECT score FROM db WHERE email = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, email);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				result = resultSet.getInt("score");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				connection.close();
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

//	public void displayEXP(JLabel score1, JLabel sc, JLabel nickname3, JLabel nickname4) {
//		try {
//			Class.forName("oracle.jdbc.driver.OracleDriver");
//			connection = DriverManager.getConnection("jdbc:oracle:thin:@"+ip, "c##catch", "bit");
//
//			String query = "SELECT score from db where nickname = "+nickname1.getText();
//			preparedStatement = connection.prepareStatement(query);
//			resultSet = preparedStatement.executeQuery();
//			while(resultSet.next()) {
//				nickname1.setText(resultSet.getString("nickname"));
//				score1.setText(resultSet.getInt("score")+"");
//			}
//
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				preparedStatement.close();
//				connection.close();
//				resultSet.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//	}

	public DefaultTableModel online() {
		String[] userField = { "닉네임", "레벨", "위치" };
		DefaultTableModel model = new DefaultTableModel(userField, 0) {
			@Override
			public boolean isCellEditable(int row, int column) { // 수정, 입력 불가
				return false;
			}
		};
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");
			String sql = "select nickname,score from db where isonline = 1";
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String nickName = resultSet.getString("nickname");
				int score = resultSet.getInt("score");
				Object data[] = { nickName, score / 10, "대기방" };
				model.addRow(data);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return model;
	}

	public DefaultTableModel getRooms() {
		String[] userField = { "번호", "방제목", "방장", "인원", "비고" };
		DefaultTableModel model = new DefaultTableModel(userField, 0) {
			@Override
			public boolean isCellEditable(int row, int column) { // 수정, 입력 불가
				return false;
			}
		};

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		String open = new String();
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");
			String query = "SELECT * FROM room where status=0 order by no ";
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				if (resultSet.getInt("isopen") == 1) {
					open = "비공개";
				} else
					open = "공개";
				Object[] data = { resultSet.getInt("no"), resultSet.getString("title"), resultSet.getString("owner"),
						resultSet.getInt("people"), open };
				model.addRow(data);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return model;
	}

	public void makingRoom(String roomName, String ownerNickName, WaitingChatDTO dto, String pw) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");

			String query = "INSERT INTO room VALUES(ROOM_SEQ.nextval, ?, ?, ?, ?,?,?)";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, roomName);
			preparedStatement.setString(2, ownerNickName);
			preparedStatement.setInt(3, 1);
			preparedStatement.setInt(4, dto.getIsopen());
			preparedStatement.setInt(5, dto.getStatus());
			preparedStatement.setString(6, pw);
			preparedStatement.executeUpdate();// 실행
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public String showNickname(String email) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		String nickname = "";
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");
			String sql = "select nickname from db where email=?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, email);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				nickname = resultSet.getString("nickname");
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return nickname;
	}

	public int getScore(String email) {
		Connection connection = null;
		PreparedStatement preparedStetement = null;
		ResultSet resultSet = null;

		int score = 0;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");
			String sql = "select score from db where email=?";
			preparedStetement = connection.prepareStatement(sql);
			preparedStetement.setString(1, email);
			resultSet = preparedStetement.executeQuery();
			while (resultSet.next()) {
				score = resultSet.getInt("score");
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStetement != null)
					preparedStetement.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return score;
	}

	public int getEmptyRow() {
		List list = new ArrayList();

		Connection connection = null;
		PreparedStatement preparedStetement = null;
		ResultSet resultSet = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");
			String sql = "select no from room1593 where nickname is null";
			preparedStetement = connection.prepareStatement(sql);
			resultSet = preparedStetement.executeQuery();
			while (resultSet.next()) {
				list.add(resultSet.getInt("no"));
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStetement != null)
					preparedStetement.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return (int) Collections.min(list);
	}

	public int getFilledRow() {
		List list = new ArrayList();

		Connection connection = null;
		PreparedStatement preparedStetement = null;
		ResultSet resultSet = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");
			String sql = "select no from room1593 where nickname is not null";
			preparedStetement = connection.prepareStatement(sql);
			resultSet = preparedStetement.executeQuery();
			while (resultSet.next()) {
				list.add(resultSet.getInt("no"));
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStetement != null)
					preparedStetement.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (list.size() == 0)
			return 1;
		int result = (int) Collections.min(list);
		return result;
	}

	public boolean isOwner(String nickname) {

		boolean result = false;
		int owner = 0;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");
			String sql = "select isowner from room1593 where nickname = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, nickname);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				owner = resultSet.getInt("isowner");
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (owner == 1)
			result = true;
		return result;
	}

	/*
	 * Connection connection = null; Statement statement = null; PreparedStatement
	 * preparedStatement = null;
	 * 
	 * try { Class.forName("oracle.jdbc.driver.OracleDriver"); connection =
	 * DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");
	 * 
	 * String query = "UPDATE room1593 set isowner = 1 WHERE no = ?";
	 * preparedStatement = connection.prepareStatement(query);
	 * preparedStatement.setInt(1, no); preparedStatement.executeUpdate();
	 * 
	 * } catch (ClassNotFoundException e) { e.printStackTrace(); } catch
	 * (SQLException e) { e.printStackTrace(); } finally { try {
	 * preparedStatement.close(); connection.close(); } catch (SQLException e) {
	 * e.printStackTrace(); } }
	 * 
	 */

	public void ownerExit(String nickname) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");

//방장 없애기
			String query = "UPDATE room1593 set isowner = 0 WHERE nickname = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, nickname);
			preparedStatement.executeUpdate();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void setOwner(int no) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");

			String query = "UPDATE room1593 set isowner = 1 WHERE no = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, no);
			preparedStatement.executeUpdate();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void ownerPassToAnswerUser(String answerUser) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");

//현재 방장의 isOwner 값을 0으로 변경
			String query = "UPDATE room1593 set isowner = 0 WHERE isowner = 1";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.executeUpdate();

//정답자의 isOwner 값을 1로 변경
			query = "UPDATE room1593 set isowner = 1 WHERE nickname = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, answerUser);
			preparedStatement.executeUpdate();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void joinGame(String nickname, int no) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");
			String query = "UPDATE room1593 set nickname = ? WHERE no = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, nickname);
			preparedStatement.setInt(2, no);
			preparedStatement.executeUpdate();// 실행
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void exitGame(String nickname) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");
			String query = "UPDATE room1593 set nickname = null WHERE nickname = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, nickname);
			preparedStatement.executeUpdate();// 실행
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isEmptyRoom() {
		boolean result = false;
		List list = new ArrayList();

		Connection connection = null;
		PreparedStatement preparedStetement = null;
		ResultSet resultSet = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");
			String sql = "select no from room1593 where nickname is null";
			preparedStetement = connection.prepareStatement(sql);
			resultSet = preparedStetement.executeQuery();
			while (resultSet.next()) {
				list.add(resultSet.getInt("no"));
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStetement != null)
					preparedStetement.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (list.size() == 1)
			result = true;

		return result;
	}

	public boolean isOnline(String nickName) { // off이면 true, on면 false;
		boolean result = false;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");
			statement = connection.createStatement();

			String query = "SELECT isOnline FROM db WHERE nickname = " + "'" + nickName + "'";
			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				if (Integer.parseInt(resultSet.getString("isOnline")) == 0) {
					result = true;
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				resultSet.close();
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (!hasNickName(nickName))
			result = true;
		return result;
	}

	public void addScoreInGame(String answerUser) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");
			String query = "UPDATE room1593 set score = score + 1 WHERE nickname = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, answerUser);
			preparedStatement.executeUpdate();// 실행
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void addScoreWhenExitRoom(String nickname) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");

			int no = 0;
			String sql = "select score from room1593 where nickname = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, nickname);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				no = resultSet.getInt("score");
			}

			String query = "UPDATE db set score = score + ? WHERE nickname = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, no);
			preparedStatement.setString(2, nickname);
			preparedStatement.executeUpdate();// 실행
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				connection.close();
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public DefaultTableModel ShowRanking() {
		String[] userField = { "순위", "닉네임", "레벨" };
		DefaultTableModel model = new DefaultTableModel(userField, 0) {
			@Override
			public boolean isCellEditable(int row, int column) { // 수정, 입력 불가
				return false;
			}
		};
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int rank = 0;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");
			String sql = "select nickname,score from db order by score desc";
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				rank++;
				String nickName = resultSet.getString("nickname");
				int score = resultSet.getInt("score");
				Object data[] = { rank + "위", nickName, score / 10 };
				model.addRow(data);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return model;
	}

	public int exitScoreReset(String nickname) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int no = 0;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");

			String sql = "update room1593 set score = 0 where nickname = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, nickname);
			preparedStatement.executeUpdate();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return no;
	}
	
	public void setGrade(String nickname, JLabel grade) {
	      String result = null;
	      
	      Connection connection = null;
	      PreparedStatement preparedStetement = null;
	      ResultSet resultSet = null;
		
	      int score = 0;
	      try {
	         Class.forName("oracle.jdbc.driver.OracleDriver");
	         connection = DriverManager.getConnection("jdbc:oracle:thin:@" + ip, "c##catch", "bit");
	         String sql = "select score from db where nickname=?";
	         preparedStetement = connection.prepareStatement(sql);
	         preparedStetement.setString(1, nickname);
	         resultSet = preparedStetement.executeQuery();
	         while (resultSet.next()) {
	            score = resultSet.getInt("score");
	         }
	         if(score>=0 && score <10) {
	            result = "F";
	         }else if(score>=10 && score <20) {
	            result = "F+";
	         }else if(score>=20 && score <30) {
	            result = "E";
	         }else if(score>=30 && score <40) {
	            result = "E+";
	         }else if(score>=40 && score <50) {
	            result = "D";
	         }else if(score>=50 && score <60) {
	            result = "D+";
	         }else if(score>=60 && score <70) {
	            result = "C";
	         }else if(score>=70 && score <80) {
	            result = "C+";
	         }else if(score>=80 && score <90) {
	            result = "B";
	         }else if(score>=90 && score <100) {
	            result = "B+";
	         }else if(score>=100 && score <110) {
	            result = "A";
	         }else if(score>=150) {
	            result = "A+";
	         }
	      } catch (SQLException | ClassNotFoundException e) {
	         e.printStackTrace();
	      } finally {
	         try {
	            if (resultSet != null)
	               resultSet.close();
	            if (preparedStetement != null)
	               preparedStetement.close();
	            if (connection != null)
	               connection.close();
	         } catch (SQLException e) {
	            e.printStackTrace();
	         }
	      }
	      grade.setText(result);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
//JDBCModel class