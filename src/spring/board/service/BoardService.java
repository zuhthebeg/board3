package spring.board.service;
import java.io.File;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.board.beans.Board;

import spring.board.controller.*;
import spring.board.model.BoardDao;
@Service
public class BoardService extends BoardController {
	@Autowired
	private BoardDao boardDao;
	
	@Override
	public ModelAndView listAction(@PathVariable Integer page) throws Exception {
		if(page == null){				 // 넘어온 파라미터가 있다면 	
			page =0;
		}			  // 해당 파라미터를 int형으로 캐스팅후 변수에 대입합니다.
		ArrayList<Board> articleList = boardDao.getArticleList(page);			// 그리고 변경된 dao 메서드에 넣어줍니다.
		ModelAndView mav = new ModelAndView("list");
		mav.addObject("articleList", articleList);	// 셋팅된 리스트를 뷰에 포워드합니다.
		mav.addObject("page", page);// 페이지번호를 뷰에서 보기위해 표시합니다.
		return mav;
	}

	@Override
	public ModelAndView insertAction(@ModelAttribute Board article, HttpServletRequest request)
			throws Exception {
		MultipartFile file = article.getFile(); // 파일을 받고
		if(!file.isEmpty()){		// 파일이 존재하면 
			String filename = file.getOriginalFilename();		//파일에서 업로드 파일 이름을 받고
			File tempfile =new File(request.getRealPath("/upload"), file.getOriginalFilename());	//파일 생성후 
			if(tempfile.exists() && tempfile.isFile()){	// 이미 존재하는 파일일경우 현재시간을 가져와서 리네임
				filename =System.currentTimeMillis()  +"_"+ file.getOriginalFilename() ;
				tempfile = new File(request.getRealPath("/upload"),filename);	//리네임된 파일이름으로 재생성
			}
			file.transferTo(tempfile);	// 업로드 디렉토리로 파일 이동
			article.setFilename(filename);	// 게시글에 업로드된 파일이름 등록
		}
		article.setRegip(request.getRemoteAddr());
		article.setCount(0);
		
		boardDao.insertArticle(article);	
		return new ModelAndView("redirect:list/0");	// redirect: 라는 접두어로 바로 리다이렉트가 가능합니다.
	}

	@Override
	public ModelAndView ajaxListAction(Integer page) throws Exception {
		if(page == null){				 // 넘어온 파라미터가 없으면
			page = 0;		//0으로 초기화
		}			 
			ArrayList<Board> articleList =boardDao.getArticleList(page);	
			return new ModelAndView("ajaxList", "articleList", articleList);
		}

	@Override
	public ModelAndView writeAction() throws Exception {
		return new ModelAndView("write");
	}

	@Override
	public ModelAndView countAction(Integer idx, HttpServletRequest request) throws Exception {
		Board article =boardDao.getArticle(idx);		// 게시글 전체를 가져옵니다.
		String regip = request.getRemoteAddr();	// 현재 조회를 요청한 사용자의 ip를 받고

		if(!regip.equals(article.getRegip())){			// 게시글의 ip와 동일하지 않으면
			int count = article.getCount();					// 게시글의 ip를 받아서 
			article.setCount(++count);						// +1 해주고 게시글빈에 셋팅합니다.
			boardDao.setArticleCount(article);	// 이후 이 빈을 파라미터로 업데이트합니다.
		}
		return new ModelAndView("redirect:content","idx", idx);
	}

	@Override
	public ModelAndView contentAction(Integer idx) throws Exception {
		Board article = boardDao.getArticle(idx);	
		
		return new ModelAndView("content","article", article);
	}

	@Override
	public ModelAndView deleteAction(Integer idx, HttpServletRequest request) throws Exception {
		Board article = boardDao.getArticle(idx);
		String filename = article.getFilename();
		String uploadFileName = request.getRealPath("/upload") +"/"+ filename;

		File uploadfile = new File (uploadFileName);
		if ( uploadfile.exists()&& uploadfile.isFile() )
		{
			 uploadfile.delete();		// 파일 삭제
		}
		boardDao.deleteArticle(idx);
		return new ModelAndView("json", "status", "success");
	}

	@Override
	public ModelAndView downloadAction(@PathVariable Integer idx, HttpServletRequest request)
			throws Exception {
		Board article = boardDao.getArticle(idx);
		String filename = article.getFilename();
		
		String uploadFileName = request.getRealPath("/upload") +"/"+ filename;
		
		File downFile = new File(uploadFileName);
		return new ModelAndView("downloadView", "file", downFile);
	}

}
