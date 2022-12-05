package com.ssafy.myhome.detail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

@Component
public class FileDownLoadDetail extends AbstractView{
	
	public FileDownLoadDetail() {
		setContentType("application/download; charset=UTF-8");
	}
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("여기까진 옴");
		ServletContext ctx = getServletContext();
		String realPath = ctx.getRealPath("/upload");
		System.out.println("여기까지옴" + realPath);
		Map<String, Object> fileInfo = (Map<String, Object>) model.get("downloadFile");
		
		String saveFolder = (String) fileInfo.get("sfolder");
		String originalFile = (String) fileInfo.get("ofile");
		String saveFile = (String) fileInfo.get("sfile");
		File file = new File(realPath + File.separator + saveFolder, saveFile);
		
		response.setContentType(getContentType());
		response.setContentLength((int) file.length());
		
        String header = request.getHeader("User-Agent");
        boolean isIE = header.indexOf("MSIE") > -1 || header.indexOf("Trident") > -1;
        String fileName = null;
        // IE�� �ٸ��� ó��
        if (isIE) {
        	fileName = URLEncoder.encode(originalFile, "UTF-8").replaceAll("\\+", "%20");
        } else {
            fileName = new String(originalFile.getBytes("UTF-8"), "ISO-8859-1");
        }
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		OutputStream out = response.getOutputStream();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			FileCopyUtils.copy(fis, out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		out.flush();
	}
}
