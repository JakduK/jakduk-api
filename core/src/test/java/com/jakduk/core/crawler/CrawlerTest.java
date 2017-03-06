package com.jakduk.core.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by pyohwan on 16. 11. 13.
 */
public class CrawlerTest {

    // K리그 홈페이지 개편되며 사라졌음
    @Ignore
    @Test
    public void getKLeagueSchedules() throws Exception {

        Document doc = Jsoup.connect("http://www.kleague.com/KOR_2016/record/data_2.asp?year=2016&game=1&game_type=#").get();

        Elements tableTr = doc.select("table > tbody > tr");

        tableTr.forEach(tr -> {
                    Elements tableTd = tr.getElementsByTag("td");

                    System.out.println("size=" + tableTd.size());

                    tableTd.forEach(td -> {
                        System.out.println("td=" + td.text());
                    });

                    System.out.println("-----");
                }

        );
    }
}
