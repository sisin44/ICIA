package orders.services.sixgroup.icia;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import beans.sixgroup.icia.Action;
import beans.sixgroup.icia.GoodsBean;
import services.sixgroup.icia.Service;

public class Orders extends Service{
	DataAccessObject dao;
	public Orders() {
		
	}
	@Override
	public Action entrance(HttpServletRequest req) {
		Action action = null;
		String reqValue = req.getRequestURI().substring(req.getContextPath().length() + 1);
		
		switch (reqValue) {
		case "Order":
			action = this.orderCtl(req);
			break;
		case "BasketOrder":
			action = this.BasketOrderCtl(req);
			break;
		}

		return action;
	}
	//바로구매
	private Action orderCtl(HttpServletRequest req) {
		Action action = new Action();
		ArrayList<GoodsBean> gList = new ArrayList<GoodsBean>();
		boolean commit = false;
		boolean actionType = true;
		String page = "joomoon.jsp";
		String message = "주문이 완료 되었습니다 상품페이지로 이동합니다";
		// req -> beab
		String[] gInfo = req.getParameter("gInfo").split(":");
		for (int i = 0; i < gInfo.length; i++) {
			String[] goods = gInfo[i].split(",");
			GoodsBean gb = new GoodsBean();
			gb.setmId(goods[0]);
			gb.setGoCode(goods[1]);
			gb.setSeCode(goods[2]);
			gb.setGoQty(Integer.parseInt(goods[3]));
			gList.add(gb);
		}

		dao = new DataAccessObject();
		dao.getConnection();
		dao.setAutoCommit(false);

		String date = null;
		if (insOrder(gList.get(0))) {
			date = this.isOrder(gList.get(0));
			for (GoodsBean gb : gList) {
				gb.setDate(date);
				if (insOrderD(gb)) {
					commit = true;
					page = "goods.jsp";

				}else {
					message = "주문이 실패하였습니다";
				}
			}
			GoodsBean gb = new GoodsBean();
			gb.setDate(date);
			if(upOrder(gList.get(0))) {
				if(upOrderD(gList.get(0))) {
					
				}
			}

		}else {
			message = "주문이 실패하였습니다";
		}
		
		dao.setTransaction(commit);
		dao.setAutoCommit(true);
		dao.closeConnection();
		req.setAttribute("message", message);
		action.setActionType(actionType);
		action.setPage(page);
		return action;
	}
	
	private boolean upOrderD(GoodsBean gb) {
		return convertToBoolean(dao.upOrderD(gb));
	}
	private boolean upOrder(GoodsBean gb) {
		return convertToBoolean(dao.upOrder(gb));
	}
	//장바구니 -> 구매
	private Action BasketOrderCtl(HttpServletRequest req) {
		Action action = new Action();
		ArrayList<GoodsBean> gList = new ArrayList<GoodsBean>();
		boolean commit = false;
		boolean actionType = true;
		String page = "goods.jsp";
		String message = "주문이 완료되었습니다.";
		// req -> beab
		String[] gInfo = req.getParameter("gInfo").split(":");
		for (int i = 0; i < gInfo.length; i++) {
			String[] goods = gInfo[i].split(",");
			GoodsBean gb = new GoodsBean();
			gb.setmId(goods[0]);
			gb.setGoCode(goods[1]);
			gb.setSeCode(goods[2]);
			gb.setGoQty(Integer.parseInt(goods[3]));
			gList.add(gb);
		}

		dao = new DataAccessObject();
		dao.getConnection();
		dao.setAutoCommit(false);
		String date = null;
		if (insOrder(gList.get(0))) {
			date = this.isOrder(gList.get(0));
			for (GoodsBean gob : gList) {
				gob.setDate(date);
				if (insOrderD(gob)) {
				}
			}
			GoodsBean gb = new GoodsBean();
			gb.setDate(date);
			if(upOrder(gList.get(0))) {
				if(upOrderD(gList.get(0))) {
					
				}
			}
		}
		if(delBasket(gList.get(0).getmId())) {
			commit = true;
			page = "goods.jsp";
		}

		dao.setTransaction(commit);
		dao.setAutoCommit(true);
		dao.closeConnection();
		req.setAttribute("message", message);
		action.setActionType(actionType);
		action.setPage(page);

		return action;
	}
	//장바구니 딜리트
	private boolean delBasket(String gb) {
		return convertToBoolean(dao.delBasket(gb));
	}
	//주문영수증 인서트
	private boolean insOrder(GoodsBean gb) {
		return convertToBoolean(dao.insOrder(gb));
		
	}
	
	//주문영수증의 날자값 셀렉트
	private String isOrder(GoodsBean gb) {
		return dao.isOrder(gb);
	}

	//상세주문영수증 인서트
	private boolean insOrderD(GoodsBean gb) {
		return convertToBoolean(dao.insOrderD(gb));
		
	}


	
}
