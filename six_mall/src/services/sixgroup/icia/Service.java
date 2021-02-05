package services.sixgroup.icia;

import javax.servlet.http.HttpServletRequest;

import beans.sixgroup.icia.Action;

public abstract class Service {
//abstract(추상) :: 인터페이스를 부모가 제시 자식은 반드시 구현
//	하나 이상의 추상 메서드가 존재
	protected String reqV;
	
	//자식클래스에서 구현
	public abstract Action entrance(HttpServletRequest req);
	
	//수정불가
	protected final boolean convertToBoolean(int value) {
		return (value==1)? true : false;
		
	}
}
