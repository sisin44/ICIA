package controller.sixgroup.icia;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.sixgroup.icia.Action;
import orders.services.sixgroup.icia.Orders;
import search.services.sixgroup.icia.GoodsManagements;

/**
 * Servlet implementation class Ordercontroller
 */
//장바구니, 바로주문, 장바구니리스트이동, 장바구니리스트주문, 주문서
@WebServlet({"/Basket", "/Order", "/BasketCheck", "/BasketOrder", "/Joomoon"})
public class OrderController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public OrderController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
	//주문하는 영역 이라서 데이터 전송방식을 post로 보안을 강화
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		
		
		String reqV = 
				request.getRequestURI().substring(request.getContextPath().length() + 1);
		
		Action action = null;

		
		if(reqV.equals("Basket") ||  reqV.equals("BasketCheck")  || reqV.equals("Joomoon")){
			//세션만료
			
			if(request.getSession().getAttribute("accessInfo") != null) {
				
				GoodsManagements goods = new GoodsManagements();
				action = goods.entrance(request);
			}else {
				action = new Action();
				action.setActionType(true);
				action.setPage("login.jsp?" +this.setParam(reqV,"gInfo" ,"opt",request));
			}
			
		}else if(reqV.equals("Order")|| reqV.equals("BasketOrder")){
			if(request.getSession().getAttribute("accessInfo") != null) {	
				Orders orders = new Orders();
				action = orders.entrance(request);
			}else {
				action = new Action();
				action.setActionType(true);
				action.setPage("login.jsp?" +this.setParam(reqV,"gInfo" ,"opt",request));
			}
		}else {
			
		}
		
		//응답
		if(action.isActionType()) {
			RequestDispatcher dispatcher = request.getRequestDispatcher(action.getPage());
			dispatcher.forward(request, response);
		}else {
			response.sendRedirect(action.getPage());
		}
	}
	//상품상세에서 필요한 데이터들을 세션만료일때 데이터 전송
	private String setParam(String faction, String paramName, String opt, HttpServletRequest request) throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
		sb.append("action="+URLEncoder.encode(faction, "UTF-8")+ "&");
		for(int i=0; i<request.getParameterValues(paramName).length; i++) {
			sb.append(paramName + "="); 
			sb.append(URLEncoder.encode(request.getParameterValues(paramName)[i],"UTF-8"));
			sb.append(i==request.getParameterValues(paramName).length?"":"&");
		}
		sb.append(opt+"=");
		sb.append(URLEncoder.encode(request.getParameterValues(opt)[0], "UTF-8"));

		return sb.toString();
	}

}
