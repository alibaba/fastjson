package data.xuelu;

import java.util.LinkedHashMap;
import java.util.Random;

import com.alibaba.fastjson.JSONArray;

public class MyTestJson {
    
    public static final String STRING1 = "英国广播公司网站3月30日报道称，由于在同一天其他小组的比赛中，菲律宾3-2击败朝鲜，而伊朗主场2-0轻取阿曼，中国队成为八个小组中4支成绩最好的小组第二球队之一，晋身有12支出线球队参加的第三阶段预选赛";
    public static final String STRING2 ="负于中国队的卡塔尔仍然以小组第一的身份晋级下一阶段比赛。";
    public static final String STRING3 = "3月29日晚上在陕西省体育中心上演的，可能是中国男足多年来罕见的扣人心弦而又取得美好结果的一场比赛，并且可能在今后一段时间里被中国球迷记住。";
    public static final String STRING4 = "但与此前的很多比赛相似，中国队的战术部署在本场比赛中并未有明显的成效，尤其是李学鹏在左前卫位置上有明显的不适应。中国队在前半段时间整体在进攻端也未创造太多机会，上半时最好的进球机会是第25分钟武磊接张稀哲右路快速任意球右脚打门，但被对方后卫拦出底线。";
    public static final String STRING5 = "对于看台上和电视前的中国球迷而言，在没有进球的上半场得到的最好消息是早15分钟开球的另一小组比赛，菲律宾率先攻破朝鲜队大门，这意味着中国队只要取胜便大有机会出线。";
    public static final String STRING6 = "但至少，这已是中国队自2002年以来第一次不用提前两年告别世界杯。";
    public static final String STRING7 = "Except as otherwise noted, the content of this page is licensed under the Creative Commons Attribution 3.0 License, and code samples are licensed under the Apache 2.0 License. For details, see our Site Policies.";
    public static final String STRING8 = "Facebook SDK for Android makes it easier and faster to integrate your Android app with Facebook. ";
    public static final String STRING9 ="Enjoy millions of the latest Android apps, games, music, movies, TV, books.";
    public static final String STRING10 ="Android is a mobile operating platform owned by Google.";

    public static String[] sStringArray = {STRING1,STRING2,STRING3,STRING4,STRING4,STRING6,STRING7,STRING8,STRING9,STRING10};
    
    public int     mIntField1    = 0;
    public int     mIntField2    = 0;
    public int     mIntField3    = 0;
    public int     mIntField4    = 0;
    public int     mIntField5    = 0;
    public long    mLongField1   = 0L;
    public long    mLongField2   = 0L;
    public long    mLongField3   = 0L;
    public long    mLongField4   = 0L;
    public long    mLongField5   = 0L;
    public float   mFloatField1  = 0.0F;
    public float   mFloatField2  = 0.0F;
    public float   mFloatField3  = 0.0F;
    public float   mFloatField4  = 0.0F;
    public float   mFloatField5  = 0.0F;
    public double  mDoubleField1 = 0.0F;
    public double  mDoubleField2 = 0.0F;
    public double  mDoubleField3 = 0.0F;
    public double  mDoubleField4 = 0.0F;
    public double  mDoubleField5 = 0.0F;
    public boolean mBoolField1   = false;
    public boolean mBoolField2   = true;
    public boolean mBoolField3   = true;
    public boolean mBoolField4   = true;
    public boolean mBoolField5   = true;
    public String  mStringField1;
    public String  mStringField2;
    public String  mStringField3;
    public String  mStringField4;
    public String  mStringField5;
    public String  mStringField6;
    public String  mStringField7;
    public String  mStringField8;
    public String  mStringField9;
    public String  mStringField10;
    
    final static  int  SIZE = 2000;
    
    public static String createJson(){
        JSONArray jsonArray = new JSONArray();
        Random r = new Random();
        try {
            for (int i = 0; i < SIZE; i++) {
                // JSONObject objCom = new JSONObject();
                LinkedHashMap<String, Object> objCom = new LinkedHashMap<String, Object>();
                
                objCom.put("mBoolField1", r.nextBoolean());
                objCom.put("mBoolField2", r.nextBoolean());
                objCom.put("mBoolField3", r.nextBoolean());
                objCom.put("mBoolField4", r.nextBoolean());
                objCom.put("mBoolField5", r.nextBoolean());
                
                objCom.put("mDoubleField1", r.nextDouble());
                objCom.put("mDoubleField2", r.nextDouble());
                objCom.put("mDoubleField3", r.nextDouble());
                objCom.put("mDoubleField4", r.nextDouble());
                objCom.put("mDoubleField5", r.nextDouble());

                objCom.put("mFloatField1", r.nextFloat());
                objCom.put("mFloatField2", r.nextFloat());
                objCom.put("mFloatField3", r.nextFloat());
                objCom.put("mFloatField4", r.nextFloat());
                objCom.put("mFloatField5", r.nextFloat());

                objCom.put("mIntField1", r.nextInt(300));
                objCom.put("mIntField2", r.nextInt(1000));
                objCom.put("mIntField3", r.nextInt(10));
                objCom.put("mIntField4", r.nextInt(10000));
                objCom.put("mIntField5", r.nextInt(1000000));

                objCom.put("mLongField1", r.nextLong());
                objCom.put("mLongField2", r.nextLong());
                objCom.put("mLongField3", r.nextLong());
                objCom.put("mLongField4", r.nextLong());
                objCom.put("mLongField5", r.nextLong());

                if(i % 2 == 0) {
                    objCom.put("mStringField1", sStringArray[0]);
                    objCom.put("mStringField2", sStringArray[1]);
                    objCom.put("mStringField3", sStringArray[2]);
                    objCom.put("mStringField4", sStringArray[3]);
                    objCom.put("mStringField5", sStringArray[4]);
                    objCom.put("mStringField6", sStringArray[5]);
                    objCom.put("mStringField7", sStringArray[6]);
                    objCom.put("mStringField8", sStringArray[7]);
                    objCom.put("mStringField9", sStringArray[8]);
                    objCom.put("mStringField10", sStringArray[9]);
                } else {
                    objCom.put("mStringField1", sStringArray[3]);
                    objCom.put("mStringField2", sStringArray[4]);
                    objCom.put("mStringField3", sStringArray[6]);
                    objCom.put("mStringField4", sStringArray[0]);
                    objCom.put("mStringField5", sStringArray[7]);
                    objCom.put("mStringField6", sStringArray[5]);
                    objCom.put("mStringField7", sStringArray[1]);
                    objCom.put("mStringField8", sStringArray[9]);
                    objCom.put("mStringField9", sStringArray[2]);
                    objCom.put("mStringField10", sStringArray[8]);
                }

                jsonArray.add(objCom);

            }
            String testJson =  jsonArray.toString();
            
            return testJson;
        } catch (Exception e) {
            e.printStackTrace();
        }
         return null;
    }
}
