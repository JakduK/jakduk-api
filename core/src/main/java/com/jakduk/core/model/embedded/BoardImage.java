package com.jakduk.core.model.embedded;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 25.
 * @desc     : ID 만 저장하는데 굳이 클래스를 만들 필요가 있을까 싶으면서도, 나중에 ID 이외 다른 필드가 추가되면 DB 마이그레이션이 필요 없기 때문에
 * 변동 가능성이 있다면 클래스로 만드는것이 낫겠다.
 */

@AllArgsConstructor
@Getter
public class BoardImage {
	private String id;
}
