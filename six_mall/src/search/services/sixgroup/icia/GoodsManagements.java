package search.services.sixgroup.icia;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import beans.sixgroup.icia.Action;
import beans.sixgroup.icia.GoodsBean;
import services.sixgroup.icia.Service;

public class GoodsManagements extends Service {
	private DataAccessObject dao;

	public GoodsManagements() {

	}

	@Override
	public Action entrance(HttpServletRequest req) {
		Action action = null;
		String reqValue = req.getRequestURI().substring(req.getContextPath().length() + 1);

		switch (reqValue) {
		case "Search":
			action = this.searchCtl(req);
			break;
		case "GoodsDetail":
			action = this.goodsDetailCtl(req);
			break;
		case "Basket":
			action = this.basketCtl(req);
			break;
		case "BasketCheck":
			action = this.basketListCtl(req);
			break;
		case "Joomoon":
			action = this.joomoonCtl(req);
			break;
		case "orderCancel":
			action = this.orderCancelCtl(req);
			break;
		}

		return action;
	}

	// 주문취소
	private Action orderCancelCtl(HttpServletRequest req) {
		Action action = new Action();
		GoodsBean gb = new GoodsBean();
		boolean isCommit = true;
		boolean actionType = true;
		String page = "joomoon.jsp";

		dao = new DataAccessObject();
		dao.getConnection();
		dao.setAutoCommit(false);
		// req --> bean

		String[] gInfo = req.getParameterValues("gInfo")[0].split(":");
		for (int i = 0; i < gInfo.length; i++) {
			String[] goods = gInfo[i].split(",");
			gb.setmId(goods[0]);
			gb.setGoCode(goods[1]);
			gb.setSeCode(goods[2]);
			gb.setGoQty(Integer.parseInt(goods[3]));
		}

		if (canBasket(gb)) {
			isCommit = true;
			page = "goods.jsp";
		}
		dao.setTransaction(isCommit);
		dao.setAutoCommit(true);
		dao.closeConnection();

		action.setActionType(actionType);
		action.setPage(page);
		return action;

	}

	private boolean canBasket(GoodsBean gb) {
		return convertToBoolean(dao.canBasket(gb));
	}

	// 주문서 if 장바구니에 내용물이 있는경우 else 장바구니에 내용물이 없는경우
	private Action joomoonCtl(HttpServletRequest req) {
		Action action = new Action();
		GoodsBean gb = new GoodsBean();
		boolean isCommit = true;
		boolean actionType = true;
		String orderType = "Order";
		String page = "joomoon.jsp";
		String tag = null;
		StringBuffer jInfo = new StringBuffer();

		dao = new DataAccessObject();
		dao.getConnection();
		dao.setAutoCommit(false);

		// req --> bean
		if (req.getParameter("goInfo") == null) {
			gb.setmId(req.getParameterValues("gInfo")[0]);
			gb.setGoCode(req.getParameterValues("gInfo")[1]);
			gb.setSeCode(req.getParameterValues("gInfo")[2]);
			gb.setGoQty(Integer.parseInt(req.getParameterValues("opt")[0]));

			tag = this.jmMakeHtml(this.getDetail(gb), jInfo);
		} else {
			String[] gInfo = req.getParameter("goInfo").split(":");
			for (int i = 0; i < gInfo.length; i++) {
				String[] goods = gInfo[i].split(",");
				gb.setmId(goods[0]);
				gb.setGoCode(goods[1]);
				gb.setSeCode(goods[2]);
				gb.setGoQty(Integer.parseInt(goods[3]));
				// gList.add(gb);
				if (jmUpBasket(gb)) {
					orderType = "BasketOrder";
				}

			}
			tag = this.jmMakeHtml(this.baJmList(gb), jInfo);

		}

		dao.setTransaction(isCommit);
		dao.setAutoCommit(true);
		dao.closeConnection();

		req.setAttribute("ordersInfo", tag);
		req.setAttribute("goodsInfo", jInfo.toString());
		req.setAttribute("ordersType", orderType);
		action.setActionType(actionType);
		action.setPage(page);
		return action;
	}

	// 장바구니 주문서 업데이트
	private boolean jmUpBasket(GoodsBean gob) {
		return convertToBoolean(dao.jmUpBasket(gob));

	}

	// 장바구니 주문서 리스트
	private ArrayList<GoodsBean> baJmList(GoodsBean goodsBean) {
		return dao.baJmList(goodsBean);
	}

	// 검색
	private Action searchCtl(HttpServletRequest req) {
		Action action = new Action();
		String page = "goods.jsp";
		boolean actionType = true;

		GoodsBean gb = new GoodsBean();
		// req -> GoodsBean
		gb.setWord(req.getParameter("word"));

		// dao 연결
		dao = new DataAccessObject();
		dao.getConnection();
		dao.setAutoCommit(false);
		HttpSession session = req.getSession();
		if(session.getAttribute("accessInfo") != null) {
			// 상품조회
			req.setAttribute("gList",
					this.makeGoodsList((gb.getWord().equals("")) ? this.searchGoods() : this.searchGoods(gb)));
		}else {
			page = "LogInForm";
		}
		
		dao.setAutoCommit(true);
		dao.closeConnection();

		// Action 설정
		action.setActionType(actionType);
		action.setPage(page);

		return action;
	}

	// 굿즈디테일
	private Action goodsDetailCtl(HttpServletRequest req) {
		Action action = new Action();
		String page = "goodsDetail.jsp";
		boolean actionType = true;
		ArrayList<GoodsBean> goodsInfo;

		// req -> gb
		GoodsBean gb = new GoodsBean();
		gb.setGoCode(req.getParameterValues("code")[0]);
		gb.setSeCode(req.getParameterValues("code")[1]);

		// dao 생성
		dao = new DataAccessObject();
		dao.getConnection();

		goodsInfo = this.getDetail(gb);

		if (goodsInfo.size() != 1) {
			page = "goods.jsp";
			actionType = false;
			req.setAttribute("gList",
					this.makeGoodsList((gb.getWord() == null) ? this.searchGoods() : this.searchGoods(gb)));
			req.setAttribute("message", "죄송합니다. 품절상태입니다.");
		} else {
			req.setAttribute("goodsImage", "image/" + goodsInfo.get(0).getGoImage());
			req.setAttribute("item", goodsInfo.get(0).getGoName());
			req.setAttribute("price", goodsInfo.get(0).getGoPrice());
			req.setAttribute("gInfo", req.getSession().getAttribute("accessInfo") + ":" + goodsInfo.get(0).getGoCode()
					+ ":" + goodsInfo.get(0).getSeCode());
			req.setAttribute("detailImage", "image/" + goodsInfo.get(0).getGoBImage());
			req.setAttribute("seller", goodsInfo.get(0).getSeName());
		}

		dao.closeConnection();

		action.setActionType(actionType);
		action.setPage(page);

		return action;
	}

	// 상품 리스트
	private String makeGoodsList(ArrayList<GoodsBean> gList) {
		StringBuffer sb = new StringBuffer();
		int index = 0;
		for (GoodsBean goods : gList) {
			index++;

			// 3으로 나눠서 1이 나오면 이미를 하나씩 3개까지 출력
			if (index % 3 == 1) {
				sb.append("<div class=\"line\">");
			}

			sb.append("<div class=\"item\" onClick=\"goDetail(\'" + goods.getGoCode() + ":" + goods.getSeCode()
					+ "\')\">");
			sb.append("<div class=\"item__top\"><img src=\"image/" + goods.getGoImage() + "\" /></div>");
			sb.append("<div class=\"item__bottom\"><div class=\"item-name\">" + goods.getGoName() + "</div>"
					+ "<div class=\"item-price\">" + goods.getGoPrice() + "원</div>" + "<div class=\"item-stock\">재고 "
					+ goods.getGoStock() + "&nbsp;&nbsp;&nbsp;무료배송</div></div>");
			sb.append("</div>");

			// 3번째 상품씩 넘기기
			if (index % 3 == 0) {
				sb.append("</div>");
			}

		}

		if (index % 3 != 0) {
			sb.append("</div>");
		}

		return sb.toString();
	}

	// goodsDetail 에서 장바구니 클릭시
	private Action basketCtl(HttpServletRequest req) {

		Action action = new Action();
		GoodsBean gb = new GoodsBean();
		boolean isCommit = false;
		boolean actionType = true;
		String page = "GoodsDetail";
		String message = null;
		boolean messageType = false;

		// req --> bean
		gb.setmId(req.getParameterValues("gInfo")[0]);
		gb.setGoCode(req.getParameterValues("gInfo")[1]);
		gb.setSeCode(req.getParameterValues("gInfo")[2]);
		gb.setGoQty(Integer.parseInt(req.getParameter("opt")));

		dao = new DataAccessObject();
		dao.getConnection();
		dao.setAutoCommit(false);

		// 장바구니 구현 --> goodsDetail.jsp

		if (!this.seBasket(gb)) {
			if (this.insBasket(gb)) {
				isCommit = true;
				page += "?code=" + gb.getGoCode() + "&code=" + gb.getSeCode();
				message = "상품이 장바구니에 담겼습니다. 장바구니로 이동하시겠습니까?";
				messageType = true;
			} else {
				message = "다시 시도해주세요.";
			}
		} else {
			if (this.upBasket(gb)) {
				isCommit = true;
				page += "?code=" + gb.getGoCode() + "&code=" + gb.getSeCode();
				message = "상품이 장바구니에 담겼습니다. 장바구니로 이동하시겠습니까?";
				messageType = true;
			}

		}

		req.setAttribute("message", message);
		req.setAttribute("mType", messageType);
		dao.setTransaction(isCommit);
		dao.setAutoCommit(true);
		dao.closeConnection();

		action.setActionType(actionType);
		action.setPage(page);
		return action;
	}

	private Action basketListCtl(HttpServletRequest req) {

		Action action = new Action();
		GoodsBean gb = new GoodsBean();
		boolean isCommit = false;
		boolean actionType = true;
		String page = "orderdoc.jsp";
		String tag = null;
		StringBuffer gInfo = new StringBuffer();

		// req --> bean
		gb.setmId(req.getParameter("gInfo"));

		dao = new DataAccessObject();
		dao.getConnection();
		dao.setAutoCommit(false);
		
		this.canBasket(gb);
		tag = this.makeHtml(this.getBasketList(gb), gInfo);

		dao.setTransaction(isCommit);
		dao.setAutoCommit(true);
		dao.closeConnection();

		req.setAttribute("ordersInfo", tag);
		req.setAttribute("goodsInfo", gInfo.toString());
		action.setActionType(actionType);
		action.setPage(page);
		return action;
	}

	// 장바구니 리스트에 정보 표시
	private String makeHtml(ArrayList<GoodsBean> gList, StringBuffer gInfo) {
		StringBuffer sb = new StringBuffer();
		int amount = 0;
		int toAmount = 0;

		int count = 0;
		sb.append("<table>");
		sb.append("<tr>");
		sb.append("<td>상품정보</td>");
		sb.append("<td>가격</td>");
		sb.append("<td>수량</td>");
		sb.append("<td>금액</td>");
		sb.append("<td>판매자</td>");
		sb.append("</tr>");
		for (GoodsBean gb : gList) {
			count++;
			amount = (gb.getGoPrice() * gb.getGoQty());
			toAmount += amount;
			sb.append("<tr>");
			sb.append(
					"<td><input type=\"checkbox\" name=\"check\"/> <img src=\"image/" + gb.getGoImage() + "\"/></td>");
			sb.append("<td>" + gb.getGoName() + "</td>");
			sb.append("<td>" + gb.getGoPrice() + "</td>");
			sb.append("<td>" + gb.getGoQty() + "</td>");
			sb.append("<td>" + amount + "</td>");
			sb.append("<td>" + gb.getSeName() + "</td>");
			sb.append("</tr>");
			gInfo.append(gb.getmId() + "," + gb.getGoCode() + "," + gb.getSeCode() + "," + gb.getGoQty()
					+ (count == gList.size() ? "" : ":"));
		}

		sb.append("<tr>");
		sb.append("<td colspan=\"6\">" + toAmount + "</td>");
		sb.append("</tr>");
		sb.append("</table>");
		sb.append("<input type=\"button\" value=\"주문하기\" onclick=\"order()\" />");
		return sb.toString();
	}

	// 주문서 리스트에 정보 표시
	private String jmMakeHtml(ArrayList<GoodsBean> gList, StringBuffer gInfo) {
		StringBuffer sb = new StringBuffer();
		int amount = 0;
		int toAmount = 0;

		int count = 0;
		sb.append("<table>");
		sb.append("<tr>");
		sb.append("<td>상품정보</td>");
		sb.append("<td>가격</td>");
		sb.append("<td>수량</td>");
		sb.append("<td>금액</td>");
		sb.append("<td>판매자</td>");
		sb.append("</tr>");
		for (GoodsBean gb : gList) {
			count++;
			amount = (gb.getGoPrice() * gb.getGoQty());
			toAmount += amount;
			sb.append("<tr>");
			sb.append("<td><img src=\"image/" + gb.getGoImage() + "\"/></td>");
			sb.append("<td>" + gb.getGoName() + "</td>");
			sb.append("<td>" + gb.getGoPrice() + "</td>");
			sb.append("<td>" + gb.getGoQty() + "</td>");
			sb.append("<td>" + amount + "</td>");
			sb.append("<td>" + gb.getSeName() + "</td>");
			sb.append("</tr>");
			gInfo.append(gb.getmId() + "," + gb.getGoCode() + "," + gb.getSeCode() + "," + gb.getGoQty()
					+ (count == gList.size() ? "" : ":"));
		}

		sb.append("<tr>");
		sb.append("<td colspan=\"6\">" + toAmount + "</td>");
		sb.append("</tr>");
		sb.append("</table>");
		sb.append("<input type=\"button\" value=\"구매하기\" onclick=\"order()\" />");
		sb.append("<input type=\"button\" value=\"취소\" onclick=\"orderCancel()\" />");
		return sb.toString();
	}

	private boolean upBasket(GoodsBean gb) {
		return convertToBoolean(dao.upBasket(gb));
	}

	private boolean seBasket(GoodsBean gb) {
		return convertToBoolean(dao.seBasket(gb));
	}

	private boolean insBasket(GoodsBean gb) {
		return convertToBoolean(dao.insBasket(gb));
	}

	private ArrayList<GoodsBean> getBasketList(GoodsBean gb) {
		return dao.getBasketList(gb);
	}

	private ArrayList<GoodsBean> getDetail(GoodsBean gb) {
		return dao.getDetail(gb);
	}

	// 상품 전체 검색
	private ArrayList<GoodsBean> searchGoods() {
		return dao.searchGoods();
	}

	// 상품 단어 검색
	private ArrayList<GoodsBean> searchGoods(GoodsBean gb) {
		return dao.searchGoods(gb);
	}

}