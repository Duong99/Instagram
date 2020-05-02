package com.example.instagram;

public class Common {
    public static final String URL_INSTAGRAM = "https://www.instagram.com/";

    // url followers = Common.URL_Following1 + id + Common.URL_Follow2  + Common.URL_Follow3 + end_cursor + Common.URL_Follow4 + Common.URL_Follow5;
    // url following = Common.URL_Followers1 + id + Common.URL_Follow2  + Common.URL_Follow3 + end_cursor + Common.URL_Follow4 + Common.URL_Follow5;
    public static final String URL_Following1 = "https://www.instagram.com/graphql/query/?query_hash=d04b0a864b4b54837c0d870b0e77e076&variables=%7B%22id%22%3A%22";
    public static final String URL_Followers1 = "https://www.instagram.com/graphql/query/?query_hash=c76146de99bb02f6415203be841dd25a&variables=%7B%22id%22%3A%22";
    public static final String URL_Follow2 = "%22%2C%22include_reel%22%3Atrue%2C%22fetch_mutual%22%3Afalse%2C%22first%22%3A50";
    public static final String URL_Follow3 = "%2C%22after%22%3A%22";
    public static final String URL_Follow4 = "%3D%3D%22";
    public static final String URL_Follow5 = "%7D";

    // url post personal = URL_PostPersonal1 + id + URL_PostPersonal2 + end_cursor + URL_PostPersonal3
    public static final String URL_PostPersonal1 = "https://www.instagram.com/graphql/query/?query_hash=d496eb541e5c789274548bf473cc553e&variables=%7B%22id%22%3A%22";
    public static final String URL_PostPersonal2 = "%22%2C%22first%22%3A50%2C%22after%22%3A%22";
    public static final String URL_PostPersonal3 = "%3D%3D%22%7D";

    // url people like post lan dau = URL_PEOPLELIKEPOST1 + shortcode + URL_PEOPLELIKEPOST2 + URL_PEOPLELIKEPOST5;
    // url people like post nhung lan tiep theo = URL_PEOPLELIKEPOST1 + shortcode + URL_PEOPLELIKEPOST2 + URL_PEOPLELIKEPOST3 + end_cursor + URL_PEOPLELIKEPOST4 + URL_PEOPLELIKEPOST5;
    public static final String URL_PEOPLELIKEPOST1 = "https://www.instagram.com/graphql/query/?query_hash=d5d763b1e2acf209d62d22d184488e57&variables=%7B%22shortcode%22%3A%22";
    public static final String URL_PEOPLELIKEPOST2 = "%22%2C%22include_reel%22%3Atrue%2C%22first%22%3A50";
    public static final String URL_PEOPLELIKEPOST3 = "%2C%22after%22%3A%22";
    public static final String URL_PEOPLELIKEPOST4 = "%3D%3D%22";
    public static final String URL_PEOPLELIKEPOST5 = "%7D";


    // url Loading post home URL_HOMEPOST1 + cursor + URL_HOMEPOST2

    public static final String URL_HOMEPOST1 = "https://www.instagram.com/graphql/query/?query_hash=6b838488258d7a4820e48d209ef79eb1&variables=%7B%22cached_feed_item_ids%22%3A%5B%5D%2C%22fetch_media_item_count%22%3A12%2C%22fetch_media_item_cursor%22%3A%22";
    public static final String URL_HOMEPOST2 = "%3D%3D%22%2C%22fetch_comment_count%22%3A4%2C%22fetch_like%22%3A3%2C%22has_stories%22%3Afalse%2C%22has_threaded_comments%22%3Atrue%7D";

    // Url search = URL_SEARCH1 + text + URL_SEARCH2
    public static final String URL_SEARCH1 = "https://www.instagram.com/web/search/topsearch/?context=blended&query=";
    public static final String URL_SEARCH2 = "&include_reel=true";


    // Url comment firt = "https://www.instagram.com/p/ +  short code +  /comments/?__a=1";
    // url Comment next = COMMENT1 + shortcode + COMMENT2 + idCursor + COMMENT3 + cursor + COMMENT4;
    public static final String COMMENT1 = "https://www.instagram.com/graphql/query/?query_hash=bc3296d1ce80a24b1b6e40b1e72903f5&variables=%7B%22shortcode%22%3A%22";
    public static final String COMMENT2 = "%22%2C%22first%22%3A50%2C%22after%22%3A%22%7B%5C%22cached_comments_cursor%5C%22%3A+%5C%22";
    public static final String COMMENT3 = "%5C%22%2C+%5C%22bifilter_token%5C%22%3A+%5C%22";
    public static final String COMMENT4 = "%3D%3D%5C%22%7D%22%7D";

    public final static String URL_THEME = "https://www.nhaccuatui.com/chu-de.html";
    public final static String COOKIE = "JSESSIONID=11a1aibmgb5of1sgkoa88nnhec; NCT_BALLOON_INDEX=true; touchEnable=true";

}
