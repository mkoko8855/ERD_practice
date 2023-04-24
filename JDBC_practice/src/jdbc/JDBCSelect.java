package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class JDBCSelect {
	public static void main(String[] args) {

		// 모든 컬럼 전체 SELECT를 위해 Scanner 사용 X
		
		String sql = "SELECT * FROM members";
		
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String uid = "hr";
		String upw = "hr";		
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null; //SELECT 문에서만 사용하는 객체
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(url, uid, upw); //순서대로 커넥션 객체를 받아 온다~
			//커넥션 객체 잘 받아왔으면 sql도 전달해주고.
			pstmt = conn.prepareStatement(sql);
			//물음표를 채워야하지만 물음표 채울 것이 없으니 바로 실행한다.
			
			//executeQuery는 ResultSet이라는 객체를 리턴한다. 
			//그래서 rs써주자. rs는 SELECT문의 쿼리 실행 결과 전체를 들고 있다.
			rs = pstmt.executeQuery(); 
			
			 //이제 rs에서 값을 받아오는데, 컬럼얘기하면 한 행씩 돌려 받을 수 있다.
			 /*
			 	즉, SELECT 쿼리문의 실행 결과, 조회할 데이터가 단 한 줄이라도 존재한다면
			 	rs 객체의 next() 메서드는 true값을 리턴하면서(주면서) 해당 행(값)을 지목한다.
			 	그렇기 때문에 rs에게 데이터를 읽어오실 때는 반드시 next()를 먼저 호출 해서
			 	데이터의 존재 유무부터 물어 봐야 한다.
			 	next() 메서드를 호출 하셔야만 행 하나가 지목되면서 데이터를 불러들일 수 있다.
			 */
			
			
			
			
			//조회 할 데이터의 행의 개수가 여러 개 라면 반복문을 이용해서 처리하는 게 좋겠다.
			//만약 조회할 데이터가 한 행이라면 굳이 반복문을 열 필요 없이 if문으로 처리 한다.
			//한번만 물어보면 되니까.
			
			
			int count = 0;
			while(rs.next()){ //얘가 true주면 계속 조회, 조회할 게 없으면 false겠지.
							  //몇번 반복을 돌리는지 몰라도돼. 그래서 while문 쓴거고~
				
				//next메서드가 호출되면 우리한테 true를 주면서 행을 지목한다.
				//우리는 첫 행의 컬럼명을 얘기하면서 값을 들고오면 된다.
				
				 /*
				  SELECT의 실행 결과 컬럼을 읽어오려면,
				  rs의 getString(), getInt(), getDouble()...의 메서드를
				  사용해서 리턴받는다. (컬럼 타입에 따라 다르다.)
				 */
				 
				//그럼 해보자.
				//ID, PW, NAME, AGE, REGDATE 다 가져와보자
				//STRING STRING STRING INT DATE 로 받으면됨.
				String id = rs.getString("mem_id"); //괄호안에는 컬럼명을 작성한다.
				String pw = rs.getString("mem_pw");
				String name = rs.getString("mem_name"); //mem_name값을 문자열로줘~
				int age = rs.getInt("mem_age");
				
				//local과 Iimestamp를 서로 바꾸는 법은,
				//localDateTime > Timestamp로 바꾸려면 Timestamp.valueOf(localDateTime);
				//Timestamp > localDateTiem로 바꾸려면 Timestamp.toLocalDateTime();
				LocalDateTime regdate = rs.getTimestamp("mem_regdate").toLocalDateTime();
				count++;
				
				
				System.out.printf(" 아이디 : %s\n 비밀번호 : %s\n 이름 : %s\n 나이 : %d세\n 가입일 : %s\n", id, pw, name, age, regdate);
				System.out.println("-----------------------------------------");
			}
			System.out.println("조회된 행의 개수는 " + count + " 개 입니다.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
