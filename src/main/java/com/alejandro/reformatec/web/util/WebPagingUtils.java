package com.alejandro.reformatec.web.util;

import javax.servlet.http.HttpServletRequest;

public class WebPagingUtils {

	public static final Integer getCurrentPage(HttpServletRequest request) {
		String pageStr = request.getParameter(ParameterNames.PAGE);
		int page = 1;
		try  {
			page = Integer.valueOf(pageStr);
		} catch (NumberFormatException pe) {					
		}
		return page;
	}
	
	public static final Integer getPageFrom(int currentPage, int pageCount, int totalPages) {
		int pageFrom = currentPage - (pageCount/2);
		if (pageFrom<1 || totalPages<=pageCount) {
			pageFrom = 1;
		} else if ((totalPages - pageCount) < pageFrom ) { // cercano a la ultima pagina
			pageFrom =  totalPages - pageCount;			
		}
		
		return pageFrom;		
	}
	
	public static final Integer getPageTo(int currentPage, int pageCount, int totalPages) {
		int pageTo = currentPage + (pageCount/2);
		if (pageTo>totalPages || pageCount >= totalPages) {
			pageTo = totalPages;
		} else if (pageTo<pageCount) {
			pageTo = pageCount;			
		}
		return pageTo;
	}
	
	public static final Integer getTotalPages(int totalResults, int pageSize) {
		return (int) Math.ceil((double) totalResults / (double) pageSize);
	}
	
	/**
	 * For test only
	 * @param args
	 */
/*	public static void main(String args[]) {
		System.out.println(getTotalPages(23, 10));
		System.out.println(getTotalPages(20, 10));
	}
*/
	
}
