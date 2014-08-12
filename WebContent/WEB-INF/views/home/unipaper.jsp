<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
Custom URL Scheme:<BR>
unipaperlink://view?cvtu=[convertion_url]&amp;cvtps=[convertion_params]&amp;uid=[unique_id]&amp;vender=[vender]&amp;model=[model]<BR><BR>
Parameters Explanation<BR>cvtu : 컨버전서버URL (URL인코딩할 것)<BR>cvtps : 컨버전서버에 전달할
파리미터들 (URL인코딩할 것)<BR>uid :<BR>vender :<BR>model :<BR><BR>Exmple<BR><A href="unipaperlink://view/?cvtu=http%3A%2F%2Fhdemo.thinkfree.com%3A8080%2Fhermes%2Fconvert&amp;cvtps=inputfile%3Dhttp%3A%2F%2Fhdemo.thinkfree.com%3A8080%2Fhermes%2Fsample.hs%3Fid%3Dsample.doc%26filter%3Ddoc-image%26ignorecache%3Dtrue%26responsetype%3Dunidoc%26imagetype%3Djpg&amp;uid=uid&amp;vender=vender&amp;model=model">unipaperlink://view?cvtu=http%3A%2F%2Fhdemo.thinkfree.com%3A8080%2Fhermes%2Fconvert&amp;cvtps=inputfile%3Dhttp%3A%2F%2Fhdemo.thinkfree.com%3A8080%2Fhermes%2Fsample.hs%3Fid%3Dsample.doc%26filter%3Ddoc-image%26ignorecache%3Dtrue%26responsetype%3Dunidoc%26imagetype%3Djpg&amp;uid=uid&amp;vender=vender&amp;model=model</A><BR><BR><BR><BR>
</body>
</html>