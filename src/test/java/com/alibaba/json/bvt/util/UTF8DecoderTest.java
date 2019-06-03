package com.alibaba.json.bvt.util;

import com.alibaba.fastjson.util.ThreadLocalCache;
import com.alibaba.fastjson.util.UTF8Decoder;
import junit.framework.TestCase;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;

public class UTF8DecoderTest extends TestCase {
    public void test_0() throws Exception {
        CharsetDecoder decoder = ThreadLocalCache.getUTF8Decoder();

        String str = "asdfl中华人民共和国据《今日俄罗斯》17日消息，穆希卡总统自2015年卸任总统一职后一直在参议院就职。14日，穆希卡宣布辞职，理由是“长途跋涉后感到疲惫了”。按照规定，他作为参议员的任期将到2020年。";

        {
            byte[] bytes = str.getBytes("UTF-8");
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            decoder.decode(byteBuffer);
        }

        try {
            byte[] bytes = str.getBytes("GB18030");
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            decoder.decode(byteBuffer);
        } catch (CharacterCodingException ex) {

        }
    }

    public void test_1() throws Exception {
        int len = (Character.MAX_VALUE - Character.MIN_VALUE) + 1;
        char[] chars = new char[len];
        for (int i = 0; i < len; ++i) {
            char ch = (char) ((int) Character.MAX_VALUE + i);
            if (ch >= 55296 && ch <= 57344) {
                continue;
            }
            chars[i] = ch;
        }

        String str = new String(chars);

        CharsetDecoder decoder = ThreadLocalCache.getUTF8Decoder();

        {
            byte[] bytes = str.getBytes("UTF-8");
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            decoder.decode(byteBuffer);
        }
        try {
            byte[] bytes = str.getBytes("GB18030");
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            decoder.decode(byteBuffer);
        } catch (CharacterCodingException ex) {

        }
    }

    public void test_2() throws Exception {
        CharsetDecoder decoder = ThreadLocalCache.getUTF8Decoder();

        String str = "嫉妬心を止められない\n" +
                "服装はいつも地味なAですが、よく見るとアクセサリーやバッグがブランド品。そこで、その人の夫の職業を聞くと…。";

        {
            byte[] bytes = str.getBytes("UTF-8");
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            decoder.decode(byteBuffer);
        }
        try {
            byte[] bytes = str.getBytes("GB18030");
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            decoder.decode(byteBuffer);
        } catch (CharacterCodingException ex) {

        }
    }

    public void test_3() throws Exception {
        CharsetDecoder decoder = ThreadLocalCache.getUTF8Decoder();

        String str = "面的藏文有一个音节“བསྒྲོནད”（威利转写：bsgrond），由前加字ba、上加字sa，基字ga,下加字ra，元音o、第一后加字na、第二后加字da构成。bsgrond是7世纪的藏语语音，随着现在拉萨音里复辅音以及部分韵尾的消失和声调的出现，该词已转变读成/ʈʂø̃˩˨/（藏语拼音：zhön，藏文拉萨音拼音：zhoenv）。\n" +
                "\n" +
                "前加字只能是 ག /g/、 ད /d/、 བ /b/、 མ /m/、 འ /ɦ/。\n" +
                "上加字只能是 ཪ /r/、 ལ /l/、 ས /s/。\n" +
                "下加字只能是 ◌ྲ /r/、 ◌ྱ /j/、 ◌ྭ /w/、 ◌ླ /l/ 和用于音译梵文里送气浊辅音的送气符号 ◌ྷ，有一个复辅音 གྲྭ /grwa/ 有两个下加字 ◌ྲ /r/ 和 ◌ྭ /w/。\n" +
                "第一后加字只可能是 ཪ /r/、 ག /g/、 བ /b/、 མ /m/、 འ /ɦ/、 ང /ŋ/、 ས /s/、 ད /d/、 ན /n/、 ལ /l/。\n" +
                "第二后加字只可能是 ས /s/ 和 ད /d/，在现代藏语里不再发音，ད /d/ 在现代藏语中已经不用。\n" +
                "另外，以下是藏文带头字（དབུ་ཅན་）和无头字（དབུ་མེད་）两种字体和国际拉丁文转写的列表：";

        {
            byte[] bytes = str.getBytes("UTF-8");
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            decoder.decode(byteBuffer);
        }
        try {
            byte[] bytes = str.getBytes("GB18030");
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            decoder.decode(byteBuffer);
        } catch (CharacterCodingException ex) {

        }
    }

    public void test_4() throws Exception {
        CharsetDecoder decoder = ThreadLocalCache.getUTF8Decoder();

        String str = "\uD83E\uDD17 on Instagram\n" +
                "\uD83E\uDD17 on Twitter\n" +
                "\uD83E\uDD17 on Wikipedia\n" +
                "\uD83E\uDD17 on Yelp\n" +
                "\uD83E\uDD17 on YouTube\n" +
                "\uD83E\uDD17 on Google Trends\n" +
                "See also\n" +
                "\uD83C\uDFE5 Hospital\n" +
                "\uD83D\uDC50 Open Hands\n" +
                "\uD83E\uDD68 Pretzel\n" +
                "\uD83D\uDE42 Slightly Smiling Face\n" +
                "\uD83E\uDD27 Sneezing Face\n" +
                "\uD83E\uDD14 Thinking Face\n" +
                "\uD83D\uDC95 Two Hearts\n" +
                "☺ Smiling Face\n" +
                "\uD83D\uDD00 Random emoji";

        {
            byte[] bytes = str.getBytes("UTF-8");
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            decoder.decode(byteBuffer);
        }
        try {
            byte[] bytes = str.getBytes("GB18030");
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            decoder.decode(byteBuffer);
        } catch (CharacterCodingException ex) {

        }
    }

    /**
     * @deprecated
     */
    public void test_5() throws Exception {
        UTF8Decoder decoder = new UTF8Decoder();

        String str = "⌛︎€\uD83D\uDC69\uD83D\uDC68\uD83D\uDC68\uD83C\uDFFB\uD83D\uDC69\uD83C\uDFFFU+1F9D2: Child\tText\t\uD83E\uDDD2\t\uD83E\uDDD2\uD83C\uDFFB\t\uD83E\uDDD2\uD83C\uDFFC\t\uD83E\uDDD2\uD83C\uDFFD\t\uD83E\uDDD2\uD83C\uDFFE\t\uD83E\uDDD2\uD83C\uDFFF\n\uD83E\uDDD1\uD83C\uDFFF\uD83C\uDE1A️\uD83C\uDC04️❤️";
        byte[] bytes =str.getBytes("UTF-8");
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        decoder.decode(byteBuffer);
    }

    /**
     * @deprecated
     */
    public void test_6() throws Exception {
        UTF8Decoder decoder = new UTF8Decoder();

        String str = "\u20AC";
        byte[] bytes =str.getBytes("UTF-8");
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        decoder.decode(byteBuffer);
    }

}
