package com.test.github;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by zhangchenhui on 2018/5/16.
 */

public class GithubRepoPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);
    private static int count = 0;

    public void process(Page page) {
        //判断链接是否符合http://www.cnblogs.com/任意个数字字母-/p/7个数字.html格式
        if (!page.getUrl().regex("http://www.cnblogs.com/[a-z 0-9 -]+/p/[0-9]{7}.html").match()) {
            //加入满足条件的链接
            page.addTargetRequests(
                    page.getHtml().xpath("//*[@id=\"post_list\"]/div/div[@class='post_item_body']/h3/a/@href").all());
            page.putField("author", page.getHtml().xpath("//*[@id=\"Header1_HeaderTitle\"]/text()"));
            count++;
        }
    }


    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        long startTime, endTime;
        System.out.println("开始爬取..");

        startTime = System.currentTimeMillis();
        Spider.create(new GithubRepoPageProcessor()).addUrl("https://www.cnblogs.com/").addPipeline(new ConsolePipeline()).addPipeline(new JsonFilePipeline("/Users/bluetear/workplace/webspider/")).thread(5).run();
        endTime = System.currentTimeMillis();

        System.out.print("爬取结束,耗时约" + ((endTime - startTime) / 1000) + "秒, 抓取了" + count + "条记录");
    }
}