package com.jakduk.api.common;

import com.jakduk.api.model.embedded.LocalSimpleName;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public interface BoardCategory {

    @Getter
    enum FOOTBALL {
        CLASSIC(Arrays.asList(new LocalSimpleName(Locale.US.getLanguage(), "Classic"), new LocalSimpleName(Locale.KOREA.getLanguage(), "클래식"))),
        CHALLENGE(Arrays.asList(new LocalSimpleName(Locale.US.getLanguage(), "Challenge"), new LocalSimpleName(Locale.KOREA.getLanguage(), "챌린지"))),
        NATIONAL(Arrays.asList(new LocalSimpleName(Locale.US.getLanguage(), "NationalLeague"), new LocalSimpleName(Locale.KOREA.getLanguage(), "내셔널리그"))),
        K3(Arrays.asList(new LocalSimpleName(Locale.US.getLanguage(), "K3"), new LocalSimpleName(Locale.KOREA.getLanguage(), "K3"))),
        WK(Arrays.asList(new LocalSimpleName(Locale.US.getLanguage(), "WK"), new LocalSimpleName(Locale.KOREA.getLanguage(), "WK"))),
        NATIONAL_TEAM(Arrays.asList(new LocalSimpleName(Locale.US.getLanguage(), "NationalTeam"), new LocalSimpleName(Locale.KOREA.getLanguage(), "국가대표팀")));

        private List<LocalSimpleName> names;

        FOOTBALL(List<LocalSimpleName> names) {
            this.names = names;
        }
    }

    @Getter
    enum DEVELOPER {
        // by siverprize
        EMPLOYMENT(Arrays.asList(new LocalSimpleName(Locale.US.getLanguage(), "Employment"), new LocalSimpleName(Locale.KOREA.getLanguage(), "취업"))),
        // 특정문제에 대해서 가장 작은 바이트로 코딩하면 우승 by fxpark
        CODE_CHALLENGE(Arrays.asList(new LocalSimpleName(Locale.US.getLanguage(), "CodeChallenge"), new LocalSimpleName(Locale.KOREA.getLanguage(), "코드챌린지"))),
        // by nari
        IDEA(Arrays.asList(new LocalSimpleName(Locale.US.getLanguage(), "Idea"), new LocalSimpleName(Locale.KOREA.getLanguage(), "아이디어"))),
        TIP(Arrays.asList(new LocalSimpleName(Locale.US.getLanguage(), "Tip"), new LocalSimpleName(Locale.KOREA.getLanguage(), "팁")));

        private List<LocalSimpleName> names;

        DEVELOPER(List<LocalSimpleName> names) {
            this.names = names;
        }
    }

}
