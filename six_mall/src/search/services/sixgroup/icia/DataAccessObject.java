package search.services.sixgroup.icia;

import java.sql.SQLException;
import java.util.ArrayList;

import beans.sixgroup.icia.GoodsBean;


class DataAccessObject extends beans.sixgroup.icia.DataAccessObject{
   
   DataAccessObject() {
      
   }
   
   //주문서 취소
   int canBasket(GoodsBean gb) {
	   int count = 0;
		String query = "UPDATE ba SET ba_state = 'P' where BA_MMID = ?";
		try {
			this.pstatement = this.connection.prepareStatement(query);
			this.pstatement.setNString(1, gb.getmId());

			count = this.pstatement.executeUpdate();
			
		} catch (SQLException e) {e.printStackTrace();}
		if(count > 0) {
			count = 1;
		}
		return count;
   }
   
   //장바구니 상태 업데이트
   int jmUpBasket(GoodsBean gob) {
	   int count = 0;
			String query = "UPDATE ba SET ba_state = 'A' where BA_MMID = ? AND BA_SAGOCODE = ? and BA_SASECODE = ?";
			try {
				this.pstatement = this.connection.prepareStatement(query);
				this.pstatement.setNString(1, gob.getmId());
				this.pstatement.setNString(2, gob.getGoCode());
				this.pstatement.setNString(3, gob.getSeCode());
				count = this.pstatement.executeUpdate();
				
			} catch (SQLException e) {e.printStackTrace();}
			
			return count;
   }
   //주문서 장바구니 리스트
   ArrayList<GoodsBean> baJmList(GoodsBean gb){
			ArrayList<GoodsBean> gList = new ArrayList<GoodsBean>();
			String query = "SELECT * FROM BL WHERE MID = ? and bastate = ?";
			try {
				this.pstatement = this.connection.prepareStatement(query);
				this.pstatement.setNString(1, gb.getmId());
				this.pstatement.setNString(2, "A");
				this.rs = this.pstatement.executeQuery();	
				
				while(rs.next()) {
					GoodsBean goods = new GoodsBean();
					goods.setmId(rs.getNString("MID"));
					goods.setmName(rs.getNString("MNAME"));
					goods.setGoCode(rs.getNString("GOCODE"));
					goods.setGoName(rs.getNString("GONAME"));
					goods.setGoPrice(rs.getInt("GOPRICE"));
					goods.setGoQty(rs.getInt("QTY"));
					goods.setGoImage(rs.getNString("GOIMAGE"));
					goods.setSeCode(rs.getNString("SECODE"));
					goods.setSeName(rs.getNString("SENAME"));
					gList.add(goods);
				}
			}catch(Exception e) {e.printStackTrace();}
			return gList;
		}
   
   //주문서 바로구매 리스트
   ArrayList<GoodsBean> goJmList(GoodsBean gb){
       ArrayList<GoodsBean> gList = new ArrayList<GoodsBean>();
       String query = "SELECT GOCODE, GONAME, SECODE, PRICE, STOCK, LIMAGE, BIMAGE, SENAME FROM GOODSINFO WHERE GOCODE=? AND SECODE=?";
       
       try {
          this.pstatement = this.connection.prepareStatement(query);
          this.pstatement.setNString(1, gb.getGoCode());
          this.pstatement.setNString(2, gb.getSeCode());
          
          this.rs = this.pstatement.executeQuery();
          while(rs.next()) {
             GoodsBean goods = new GoodsBean();
             goods.setGoCode(rs.getNString("GOCODE"));
             goods.setGoName(rs.getNString("GONAME"));
             goods.setGoPrice(rs.getInt("PRICE"));
             goods.setGoStock(rs.getInt("STOCK"));
             goods.setGoImage(rs.getNString("LIMAGE"));
             goods.setGoBImage(rs.getNString("BIMAGE"));
             goods.setSeCode(rs.getNString("SECODE"));
             goods.setSeName(rs.getNString("SENAME"));
             gList.add(goods);
          }
       }catch(Exception e) {e.printStackTrace();}
       
       return gList;
   }
   //전체상품조회
      ArrayList<GoodsBean> searchGoods(){
         ArrayList<GoodsBean> gList = new ArrayList<GoodsBean>();
         String query = "SELECT GOCODE, GONAME, SECODE, PRICE, STOCK, LIMAGE FROM GOODSINFO";
         
         try {
            this.statement = this.connection.createStatement();
            this.rs = this.statement.executeQuery(query);
            while(rs.next()) {
               GoodsBean goods = new GoodsBean();
               goods.setGoCode(rs.getNString("GOCODE"));
               goods.setGoName(rs.getNString("GONAME"));
               goods.setGoPrice(rs.getInt("PRICE"));
               goods.setGoStock(rs.getInt("STOCK"));
               goods.setGoImage(rs.getNString("LIMAGE"));
               goods.setSeCode(rs.getNString("SECODE"));
               gList.add(goods);
            }
         }catch(Exception e) {e.printStackTrace();}
         
         return gList;
      }
      
      //단어 검색 상품 조회
      ArrayList<GoodsBean> searchGoods(GoodsBean gb){
         ArrayList<GoodsBean> gList = new ArrayList<GoodsBean>();
         String query = "SELECT * FROM GOODSINFO WHERE SEARCH LIKE '%' || ? || '%'";

         try {
            this.pstatement = this.connection.prepareStatement(query);
            this.pstatement.setNString(1, gb.getWord());
                     
            this.rs = this.pstatement.executeQuery();
            while(rs.next()) {
               GoodsBean goods = new GoodsBean();
               goods.setGoCode(rs.getNString("GOCODE"));
               goods.setGoName(rs.getNString("GONAME"));
               goods.setGoPrice(rs.getInt("PRICE"));
               goods.setGoStock(rs.getInt("STOCK"));
               goods.setGoImage(rs.getNString("LIMAGE"));
               goods.setSeCode(rs.getNString("SECODE"));
               gList.add(goods);
            }
         }catch(Exception e) {e.printStackTrace();}
         
         return gList;
      }
         
      // 상세정보 검색
      ArrayList<GoodsBean> getDetail(GoodsBean gb){
         ArrayList<GoodsBean> gList = new ArrayList<GoodsBean>();
         String query = "SELECT GOCODE, GONAME, SECODE, PRICE, STOCK, LIMAGE, BIMAGE, SENAME FROM GOODSINFO WHERE GOCODE=? AND SECODE=?";
         
         try {
            this.pstatement = this.connection.prepareStatement(query);
            this.pstatement.setNString(1, gb.getGoCode());
            this.pstatement.setNString(2, gb.getSeCode());
            
            this.rs = this.pstatement.executeQuery();
            while(rs.next()) {
               GoodsBean goods = new GoodsBean();
               goods.setGoCode(rs.getNString("GOCODE"));
               goods.setGoName(rs.getNString("GONAME"));
               goods.setGoPrice(rs.getInt("PRICE"));
               goods.setGoStock(rs.getInt("STOCK"));
               goods.setGoImage(rs.getNString("LIMAGE"));
               goods.setGoBImage(rs.getNString("BIMAGE"));
               goods.setSeCode(rs.getNString("SECODE"));
               goods.setSeName(rs.getNString("SENAME"));
               goods.setGoQty(gb.getGoQty());
               goods.setmId(gb.getmId());
               gList.add(goods);
            }
         }catch(Exception e) {e.printStackTrace();}
         
         return gList;
      }
   // Basket List
   		ArrayList<beans.sixgroup.icia.GoodsBean> getBasketList(GoodsBean gb){
   			ArrayList<GoodsBean> gList = new ArrayList<GoodsBean>();
   			String query = "SELECT * FROM BL WHERE MID = ? and bastate = ?";
   			
   			try {
   				this.pstatement = this.connection.prepareStatement(query);
   				this.pstatement.setNString(1, gb.getmId());
   				this.pstatement.setNString(2, "P");
   				
   				this.rs = this.pstatement.executeQuery();	
   				while(rs.next()) {
   					GoodsBean goods = new GoodsBean();
   					goods.setmId(rs.getNString("MID"));
   					goods.setmName(rs.getNString("MNAME"));
   					goods.setGoCode(rs.getNString("GOCODE"));
   					goods.setGoName(rs.getNString("GONAME"));
   					goods.setGoPrice(rs.getInt("GOPRICE"));
   					goods.setGoQty(rs.getInt("QTY"));
   					goods.setGoImage(rs.getNString("GOIMAGE"));
   					goods.setSeCode(rs.getNString("SECODE"));
   					goods.setSeName(rs.getNString("SENAME"));
   					
   				
   					gList.add(goods);
   				
   				}
   			}catch(Exception e) {e.printStackTrace();}
   			
   			return gList;
   		}
   		// 장바구니 셀렉트
   		int seBasket(GoodsBean gb) {
   			int count = 0;
   			this.rs =null;
   			String query = "SELECT COUNT(*) AS CNT FROM BA WHERE BA_MMID = ? AND BA_SAGOCODE=? AND BA_SASECODE=? ";
   			try {
   				this.pstatement = this.connection.prepareStatement(query);
   				this.pstatement.setNString(1, gb.getmId());
   				this.pstatement.setNString(2, gb.getGoCode());
   				this.pstatement.setNString(3, gb.getSeCode());
   				
   				
   				this.rs = this.pstatement.executeQuery();
   				while(rs.next()) {
   					count = rs.getInt("CNT");		
   				}
   				
   				
   			} catch (SQLException e) {e.printStackTrace();}
   			
   			return count;
   		}
   		// 장바구니 업데이트
   		int upBasket(GoodsBean gb) {
   			int count = 0;
   			String query = "UPDATE ba SET ba_quantity = ba_quantity + ? where BA_MMID = ? AND BA_SAGOCODE = ? and BA_SASECODE = ? ";
   			try {
   				this.pstatement = this.connection.prepareStatement(query);
   				this.pstatement.setInt(1, gb.getGoQty());
   				this.pstatement.setNString(2, gb.getmId());
   				this.pstatement.setNString(3, gb.getGoCode());
   				this.pstatement.setNString(4, gb.getSeCode());
   				
   				count = this.pstatement.executeUpdate();
   				
   			} catch (SQLException e) {e.printStackTrace();}
   			
   			return count;
   		}
   		
   		// 장바구니 입력
   		int insBasket(GoodsBean gb){
   			int count = 0 ;
   			String query = "INSERT INTO BA(BA_MMID, BA_SAGOCODE, BA_SASECODE, BA_QUANTITY) VALUES(?,?,?,?)";
   			try {
   				this.pstatement = this.connection.prepareStatement(query);
   				this.pstatement.setNString(1, gb.getmId());
   				this.pstatement.setNString(2, gb.getGoCode());
   				this.pstatement.setNString(3, gb.getSeCode());
   				this.pstatement.setInt(4, gb.getGoQty());
   				
   				count = this.pstatement.executeUpdate();
   			} catch (SQLException e) {
   				
   				e.printStackTrace();
   			}
   			
   			return count;
   			
   		}
   		
   		
}