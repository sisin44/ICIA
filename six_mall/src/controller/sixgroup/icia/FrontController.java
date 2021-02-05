package controller.sixgroup.icia;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import auth.services.sixgroup.icia.Authentication;
import beans.sixgroup.icia.Action;
import orders.services.sixgroup.icia.Orders;
import search.services.sixgroup.icia.GoodsManagements;



//프론트 컨트롤러:로그인, 로그아웃, 검색, 상품상세보기, 주문취소

@WebServlet({"/LogInForm", "/LogIn", "/LogOut", "/Search", "/GoodsDetail", "/orderCancel"})
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Authentication auth;
    private GoodsManagements goods;
    private Action action;
    public FrontController() {
        super();

    }

    //로그인 페이지 이동과 검색, 상품상세보기
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String req = request.getRequestURI().substring(request.getContextPath().length()+1);
		
		
		if(req.equals("LogInForm")) {
			this.action = new Action();
			this.action.setPage("login.jsp");
			this.action.setActionType(false);
		}else if(req.equals("Search") || req.equals("GoodsDetail")) {
			goods = new GoodsManagements();
			action = goods.entrance(request);
		}else {
			this.action = new Action();
			this.action.setActionType(false);
			this.action.setMessage("잘못된 경로로 접근하셨습니다 메인으로 이동합니다.");
			this.action.setPage("index.jsp?message="+URLEncoder.encode(action.getMessage(), "UTF-8"));
		}
		
		if(action.isActionType()) {
			RequestDispatcher dis = request.getRequestDispatcher(action.getPage());
			dis.forward(request, response);
		}else {
			response.sendRedirect(action.getPage());
		}
	}

	//실제 로그인액션과 로그아웃 상품상세보기와 주문취소
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String reqV = request.getRequestURI().substring(request.getContextPath().length()+1);
		
		
		if(reqV.equals("LogIn")|| reqV.equals("LogOut")) {
			this.auth = new Authentication();
			this.action = auth.entrance(request);
		}else if(reqV.equals("GoodsDetail") || reqV.equals("orderCancel")){
			goods = new GoodsManagements();
			action = goods.entrance(request);
		}
		else {
			this.action = new Action();
			this.action.setActionType(false);
			this.action.setMessage("잘못된 경로(post)로 접근하셨습니다 메인으로 이동합니다.");
			this.action.setPage("index.jsp?message="+URLEncoder.encode(action.getMessage(), "UTF-8"));
		}
		
		if(action.isActionType()) {
			RequestDispatcher dis = request.getRequestDispatcher(action.getPage());
			dis.forward(request, response);
		}else {
			response.sendRedirect(action.getPage());
		}
	}

}
