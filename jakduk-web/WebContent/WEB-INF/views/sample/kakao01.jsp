<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype HTML>
<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>KakaoLink Demo(Web Button) - Kakao Javascript SDK</title>
    <script src="https://developers.kakao.com/sdk/js/kakao.min.js"></script>
  </head>
  <body>
    <h3>카카오톡 링크는 카카오톡 앱이 설치되어 있는 모바일 기기에서만 전송 가능합니다.</h3>
    <a id="kakao-link-btn" href="javascript:;">
      <img src="http://dn.api1.kage.kakao.co.kr/14/dn/btqa9B90G1b/GESkkYjKCwJdYOkLvIBKZ0/o.jpg" />
    </a>

    <script>
    // 사용할 앱의 Javascript 키를 설정해 주세요.
    Kakao.init('238c2001d9c0e423b20adf0d6774223e');

    // 카카오톡 링크 버튼을 생성합니다. 처음 한번만 호출하면 됩니다.
    Kakao.Link.createTalkLinkButton({
      container: '#kakao-link-btn',
      label: '카카오링크 샘플에 오신 것을 환영합니다.',
      image: {
        src: 'https://jakduk.com/gallery/thumbnail/54f58623e4b0aaf2e4caf497',
        width: '300',
        height: '200'
      },
      webButton: {
        text: 'K리그 작두왕',
        url: 'https://jakduk.com/gallery/view/54f58623e4b0aaf2e4caf497' // 앱 설정의 웹 플랫폼에 등록한 도메인의 URL이어야 합니다.
      }
    });
    </script>
  </body>
</html>