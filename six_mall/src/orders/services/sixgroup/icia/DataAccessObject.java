package orders.services.sixgroup.icia;

import java.sql.SQLException;

import beans.sixgroup.icia.GoodsBean;

class DataAccessObject extends beans.sixgroup.icia.DataAccessObject{
	
	DataAccessObject() {
		
	}
	
	// 구매하기 기본영수증 인서트
	int insOrder(GoodsBean gb) {
		int count = 0;
		String query = "INSERT INTO \"OR\"(OR_MMID, OR_DATE, OR_STATE) VALUES(?,Default,?)";

		try {
			this.pstatement = this.connection.prepareStatement(query);
			this.pstatement.setNString(1, gb.getmId());
			this.pstatement.setNString(2, "I");

			count = this.pstatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return count;

	}

	// 진행중 셀렉트
	String isOrder(GoodsBean gb) {
		String orderDate = null;
		String query = "SELECT TO_CHAR(MAX(OR_DATE), \'YYYYMMDDHH24MISS\') AS ORDATE FROM \"OR\" WHERE OR_MMID = ?";
		try {
			this.pstatement = this.connection.prepareStatement(query);
			this.pstatement.setNString(1, gb.getmId());
									
			this.rs = this.pstatement.executeQuery();
			while(rs.next()) {
				orderDate = rs.getNString("ORDATE");
			}
		} catch (SQLException e) {e.printStackTrace();}
		
		return orderDate;
	}

	// 구매하기 상세영수증 인서트
	int insOrderD(GoodsBean gb) {
		int count = 0;
		String query = "INSERT INTO od(OD_ORMMID, OD_ORDATE, OD_SAGOCODE, OD_QUANTITY, OD_STATE, OD_SASECODE) VALUES(?,to_date(?,\'YYYYMMDDHH24MISS\'),?,?,?,?)";
		try {
			this.pstatement = this.connection.prepareStatement(query);
			
			this.pstatement.setNString(1, gb.getmId());
			this.pstatement.setNString(2,gb.getDate());
			this.pstatement.setNString(3, gb.getGoCode());
			this.pstatement.setInt(4, gb.getGoQty());
			this.pstatement.setNString(5, "I");
			this.pstatement.setNString(6, gb.getSeCode());

			count = this.pstatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return count;

	}
	
	//주문영수증 업데이트
	int upOrder(GoodsBean gb) {
			int count = 0;
			String query = "UPDATE \"OR\" SET OR_STATE = 'C' where OR_MMID = ? AND OR_DATE = to_date(?,\'YYYYMMDDHH24MISS\')";
			try {
				this.pstatement = this.connection.prepareStatement(query);
				
				this.pstatement.setNString(1, gb.getmId());
				this.pstatement.setNString(2, gb.getDate());
				count = this.pstatement.executeUpdate();
				
			} catch (SQLException e) {e.printStackTrace();}
			
			return count;
		}
	
	//상세영수증 업데이트
	int upOrderD(GoodsBean gb) {
		int count = 0;
		String query = "UPDATE od SET od_STATE = 'C' where OD_ORMMID = ? AND od_orDATE = to_date(?,\'YYYYMMDDHH24MISS\')";
		try {
			this.pstatement = this.connection.prepareStatement(query);
			
			this.pstatement.setNString(1, gb.getmId());
			this.pstatement.setNString(2, gb.getDate());
			count = this.pstatement.executeUpdate();
			
		} catch (SQLException e) {e.printStackTrace();}
		if(count > 0) {
			count = 1;
		}
		return count;
	}
	
	//장바구니 딜리트
	int delBasket(String gb) {
		int count = 0;
		String query = "DELETE FROM ba WHERE ba_MMID = ? and ba_state=?";
		try {
			this.pstatement = this.connection.prepareStatement(query);
			
			this.pstatement.setNString(1, gb);
			this.pstatement.setNString(2, "A");
			count = this.pstatement.executeUpdate();
			
		} catch (SQLException e) {e.printStackTrace();}
		if(count > 0) {
			count = 1;
		}
		return count;
		
	}
	
	
}
	

