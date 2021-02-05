package auth.services.sixgroup.icia;

import java.sql.SQLException;
import java.util.ArrayList;

import beans.sixgroup.icia.AuthBean;

class DataAccessObject extends beans.sixgroup.icia.DataAccessObject{
	
	DataAccessObject() {
		
	}
	int isMember(AuthBean auth) {
		pstatement = null;
		this.rs = null;
		int count = 0;
		
		String query = "SELECT COUNT(*) AS CNT FROM MM WHERE MM_ID = ?";
		try {
			this.pstatement = this.connection.prepareStatement(query);
			this.pstatement.setNString(1, auth.getmId());
			
			this.rs = this.pstatement.executeQuery();
			while(rs.next()) {
				count = rs.getInt("CNT");		
			}
			
		} catch (SQLException e) {e.printStackTrace();}
		
		return count;
	}
	int isActive(AuthBean auth) {
		this.pstatement = null;
		this.rs = null;
		int count = 0;
		String query = "SELECT COUNT(*) AS CNT FROM MM WHERE MM_ID = ? AND MM_STATE = ?";
		
		try {
			this.pstatement = this.connection.prepareStatement(query);
			this.pstatement.setNString(1, auth.getmId());
			this.pstatement.setNString(2, "A");
			
			this.rs = this.pstatement.executeQuery();
			while(rs.next()) {
				count = rs.getInt("CNT");
			}
		} catch (SQLException e) {e.printStackTrace();}
		
		return count;
	}
	// userId + userPassword 일치여부
	int isAccess(AuthBean auth) {
		this.pstatement = null;
		this.rs = null;
		int count = 0;
		String query = "SELECT COUNT(*) AS CNT FROM MM WHERE MM_ID = ? AND MM_PASSWORD = ?";
		
		try {
			this.pstatement = this.connection.prepareStatement(query);
			this.pstatement.setNString(1, auth.getmId());
			this.pstatement.setNString(2, auth.getmPassword());
			
			this.rs = this.pstatement.executeQuery();
			while(rs.next()) {
				count = rs.getInt("CNT");
			}
		} catch (SQLException e) {e.printStackTrace();}
		
		return count;
	}

	// ACCESSLOG INS 
	int insAccessLog(AuthBean auth) {
		this.pstatement = null;
		int count = 0;
		
		String dml = "INSERT INTO AL(AL_ID, AL_TIME, AL_TYPE) VALUES(?, DEFAULT, ?)";
		
		try {
			this.pstatement = this.connection.prepareStatement(dml);
			this.pstatement.setNString(1, auth.getmId());
			this.pstatement.setInt(2, auth.getAccessType());
						
			count = this.pstatement.executeUpdate();
			
		} catch (SQLException e) {e.printStackTrace();}
		
		return count;
	}
	// 회원정보 추출 :: 회원아이디, 회원이름, 로그인시간
		ArrayList<AuthBean> searchMemberInfo(AuthBean auth) {
			ArrayList<AuthBean> memberList = new ArrayList<AuthBean>();
			
			this.pstatement = null;
			this.rs = null;
			String query = "SELECT * FROM sixgroup.MMINFO WHERE MID = ? AND MTYPE = ?";
			try {
				this.pstatement = this.connection.prepareStatement(query);
				this.pstatement.setNString(1, auth.getmId());
				this.pstatement.setInt(2, auth.getAccessType());
				
				this.rs = this.pstatement.executeQuery();
				while(rs.next()) {
					AuthBean ab = new AuthBean();
					ab.setmId(rs.getNString("MID"));
					ab.setmName(rs.getNString("MNAME"));
					ab.setAccessTime(rs.getNString("MTYPE"));
					memberList.add(ab);
					
				}
			} catch (SQLException e) {}
			
			
			return memberList;
		} 
		int isLogIn(AuthBean auth) {
			this.pstatement = null;
			int count = 0;
			
			String query = "SELECT SUM(AL_TYPE) AS CNT FROM AL WHERE AL_ID = ?";
			
			try {
				this.pstatement = this.connection.prepareStatement(query);
				this.pstatement.setNString(1, auth.getmId());
				
				this.rs = this.pstatement.executeQuery();
				while(rs.next()) {
					count = rs.getInt("CNT");
				}
			}catch(Exception e) {e.printStackTrace();}
			
			return count;
		}

}
