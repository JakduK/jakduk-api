package com.jakduk.api.common;

import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.MalformedURLException;
import java.util.Date;

/**
 * Created by pyohwanjang on 2017. 3. 12..
 */

@RunWith(MockitoJUnitRunner.class)
public class SitemapTest {

    @Test
    public void test01() {
        try {
            WebSitemapGenerator wsg = new WebSitemapGenerator("https://jakduk.com");
            WebSitemapUrl url = new WebSitemapUrl.Options("https://jakduk.com/board/free/1")
                    .lastMod(new Date()).priority(1.0).changeFreq(ChangeFreq.DAILY).build();

            wsg.addUrl(url);
            System.out.println("phjang=" + wsg.writeAsStrings());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
