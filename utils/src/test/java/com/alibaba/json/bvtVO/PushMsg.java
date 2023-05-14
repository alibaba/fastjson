package com.alibaba.json.bvtVO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by haihong.xiahh on 13-12-23.
 */
public class PushMsg implements Serializable {

    public static final String DIR_PUSH = "push";

    /**
     * 
     */
    private static final long serialVersionUID = 8145512296629061628L;

    public static final String TAG = PushMsg.class.getSimpleName();

    public static final String TYPE_SYS = "sys";

    public static final String TYPE_WL = "wl";

    public static final long STATUS_TRANK_NO_NEW = 128;

    /**
     * id of PushMsg
     */
    private String id;
    /**
     * type
     */
    private String tp;
    /**
     * start time with unit second(s)
     */
    private long st;
    /**
     * end time with unit second(s)
     */
    private long et;
    /**
     * delay range 以10秒为单位，客户端会在[0 ~ dr*10seconds]的范围内，进行随机延时请求msg，防止服务器过载。
     */
    private long dr;

    private Msg msg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTp() {
        return tp;
    }

    public void setTp(String tp) {
        this.tp = tp;
    }

    public long getSt() {
        return st;
    }

    public void setSt(long st) {
        this.st = st;
    }

    public long getEt() {
        return et;
    }

    public void setEt(long et) {
        this.et = et;
    }

    public Msg getMsg() {
        return msg;
    }

    public void setMsg(Msg msg) {
        this.msg = msg;
    }

    /**
     * 条件： <br/>
     * 1、没过期(et>=当前时间，st可以大于也可以小于当前时间)<br/>
     * 2、消息体有效<br/>
     * 
     * @return true if valid.
     */
    public boolean isValid() {
        long now = new Date().getTime() / 1000;
 
        if (now > et) {
            return false;
        }// end if

        if (msg == null) {
            return false;
        }// end if

        if (!msg.isValid()) {
            return false;
        }// end if

        return true;
    }

    /**
     * 条件 1. isValid 2. st <= now <= et
     * */
    public boolean isActiveNow() {
        if (!isValid()) {
            return false;
        }
        long now = new Date().getTime() / 1000;

        if (now < st) {
            return false;
        }// end if

        if (now > et) {
            return false;
        }// end if

        if (!isImagesReady()) {
            return false;
        }// end if

        return true;
    }

    /**
     * 消息的URL是否存在
     * 
     * @return true if exist.
     */
    public boolean hasUrl() {
        boolean result = true;
        if (null != msg) {
        } else {
            result = false;
        }


        return result;
    }

    public boolean hasText() {
        boolean result = true;
        if (null != msg) {
        } else {
            result = false;
        }


        return result;
    }

    /**
     * 通知所需的图片资源是否就绪
     * 
     * @return true if ready, otherwise return false.
     */
    private boolean isImagesReady() {
        List<String> list = getNewImageUrlList();
        boolean ret = null == list || 0 == list.size();
        if (!ret) {
            preparedImages(list);
        }
        return ret;
    }

    /**
     * 主动下载未缓存到客户端的资源图片
     */
    public void preparedImages() {
        List<String> list = getNewImageUrlList();
        preparedImages(list);
    }

    public void preparedImages(List<String> list) {
    }

    /**
     * 获取需要下载图片的URL列表
     * 
     * @return list of image URL which image's URL is not cached, otherwise
     *         return null.
     */
    private List<String> getNewImageUrlList() {
        return null;
    }

    public static class Msg implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = -2020714577526457332L;

        private String gid;
        private String gtp;
        /**
         * 指显示在通知左侧的外部图标URL
         */
        private String ico;
        private String url;
        private String txt;

        private String flgs;
        private String stxt;
        private String surl;
        /**
         * 指在分享页附加的外部图片URL
         */
        private String simg;

        private ControlFlags controlFlags;

        public Msg() {
        }

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public String getGtp() {
            return gtp;
        }

        public void setGtp(String gtp) {
            this.gtp = gtp;
        }

        public String getIco() {
            return ico;
        }

        public void setIco(String icon) {
            this.ico = icon;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            if (null != url) {
                url = url.trim();
            }
            this.url = url;
        }

        public String getTxt() {
            return txt;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }

        public String getFlgs() {
            return flgs;
        }

        public void setFlgs(String flgs) {
            this.flgs = flgs;
            controlFlags = new ControlFlags(flgs);
        }

        public String getStxt() {
            return stxt;
        }

        public void setStxt(String stxt) {
            this.stxt = stxt;
        }

        public String getSurl() {
            return surl;
        }

        public void setSurl(String surl) {
            this.surl = surl;
        }

        public String getSimg() {
            return simg;
        }

        public void setSimg(String simg) {
            this.simg = simg;
        }

        public ControlFlags getControlFlags() {
            return controlFlags;
        }

        /**
         * <p>
         * 条件
         * </p>
         * 0.gid 不为null 1.controlFlag 必须有效（即非空，则控制位数目足够）<br/>
         * 2.当开启分享功能时，stxt/surl/simg 至少有一个有效（非空）<br/>
         * 
         * @return true if valid.
         */
        public boolean isValid() {

            return true;
        }

        /**
         * 打印debug信息
         * 
         * @return
         */
        public String debug() {
            StringBuilder sb = new StringBuilder();
            sb.append("\n#gid=" + gid);
            sb.append("\n#gtp=" + gtp);
            sb.append("\n#ico=" + ico);
            sb.append("\n#url=" + url);
            sb.append("\n#txt=" + txt);
            sb.append("\n#flags=" + flgs);
            sb.append("\n#stxt=" + stxt);
            sb.append("\n#surl=" + surl);
            sb.append("\n#simg=" + simg);
            if (null != controlFlags) {
                sb.append(controlFlags.debug());
            }// end if
            return sb.toString();
        }

        /**
         * 标志控制
         * 
         * @author wangyue.wy
         */
        public static class ControlFlags implements Serializable {

            /**
             * 
             */
            private static final long serialVersionUID = 6289110973325625431L;

            private enum INDEX_TYPE {
                INDEX_POS, INDEX_OPEN_URL, INDEX_DIMISS, INDEX_CANCEL_BTN, INDEX_TEXT_EFFECTS, INDEX_SHARE, INDEX_ATTACH_IMAGE, INDEX_LIMIT_SHOW_MAX_ONCE
            }

            private final int COUNT = INDEX_TYPE.values().length;

            public static final int CTR_UNKNOWN = 0;
            private String text;

            /**
             * <p>
             * 展示位置（暂时只有顶部，居中）
             * </p>
             * <p>
             * A:顶部(default)<br/>
             * B：居中 <br/>
             * </p>
             */
            private char ctrlPos;
            /**
             * <p>
             * URL打开方式
             * </p>
             * <p>
             * A:内嵌打开(default)<br/>
             * B：外部浏览器打开 <br/>
             * </p>
             */
            private char ctrlOpenUrl;
            /**
             * <p>
             * 消失方式
             * </p>
             * <p>
             * A:不消失（直至过期失效）(default)<br/>
             * B:点击消失<br/>
             * C:解锁消失 <br/>
             * D:浏览消失<br/>
             * E:解锁+点击消失 <br/>
             * F:解锁+浏览消失 <br/>
             * G:解锁+点击+浏览消失 <br/>
             * </p>
             */
            private char ctrlDimiss;
            /**
             * <p>
             * 删除按钮
             * </p>
             * <p>
             * A:显示(default)<br/>
             * B:不显示 <br/>
             * </p>
             */
            private char ctrlCancelBtn;
            /**
             * <p>
             * 是否支持分享
             * </p>
             * <p>
             * A:开启(default)<br/>
             * B:关闭<br/>
             * </p>
             */
            private char ctrlShare;
            /**
             * <p>
             * 附加图片来源
             * </p>
             * <p>
             * A:无图片(default)<br/>
             * B:使用屏幕截图<br/>
             * C:使用服务器指定的URL网络图片
             * </p>
             */
            private char ctrlAttachImage;
            /**
             * <p>
             * 文案展示效果
             * </p>
             * <p>
             * A:静止显示(default)<br/>
             * B:滚动 <br/>
             * </p>
             */
            private char ctrlTextEffects;

            /**
             * <p>
             * <b>同一gid通知</b>，限制最多展示一次
             * </p>
             * <p>
             * A:否(default)
             * </p>
             * <p>
             * B:是
             * </p>
             */
            private char ctrlLimitShowMaxOnce;

            public ControlFlags(String param) {

                this.text = param;
                ctrlPos = text.charAt(INDEX_TYPE.INDEX_POS.ordinal());
                ctrlOpenUrl = text.charAt(INDEX_TYPE.INDEX_OPEN_URL.ordinal());
                ctrlDimiss = text.charAt(INDEX_TYPE.INDEX_DIMISS.ordinal());
                ctrlCancelBtn = text.charAt(INDEX_TYPE.INDEX_CANCEL_BTN.ordinal());
                ctrlShare = text.charAt(INDEX_TYPE.INDEX_SHARE.ordinal());
                ctrlAttachImage = text.charAt(INDEX_TYPE.INDEX_ATTACH_IMAGE.ordinal());
                ctrlTextEffects = text.charAt(INDEX_TYPE.INDEX_TEXT_EFFECTS.ordinal());
                ctrlLimitShowMaxOnce = text.charAt(INDEX_TYPE.INDEX_LIMIT_SHOW_MAX_ONCE.ordinal());
            }

            /* control of position */
            public boolean posTop() {
                // default
                return 'A' == ctrlPos || ctrlPos > 'B' || ctrlPos < 'A';
            }

            public boolean posCenter() {
                return 'B' == ctrlPos;
            }

            /* control of open URL mode */
            public boolean openUrlByInner() {
                // default
                return 'A' == ctrlOpenUrl || ctrlOpenUrl > 'B' || ctrlPos < 'A';
            }

            public boolean openUrlByOutside() {
                return 'B' == ctrlOpenUrl;
            }

            /* control of dismiss */
            public boolean nerverDismiss() {
                // default
                return 'A' == ctrlDimiss || ctrlDimiss > 'G' || ctrlPos < 'A';
            }

            public boolean dismissByUnlock() {
                return 'C' == ctrlDimiss || 'D' == ctrlDimiss;
            }

            public boolean dismissByClick() {
                return 'B' == ctrlDimiss || 'D' == ctrlDimiss;
            }

            /* control of show cancel btn */
            public boolean showCancelBtn() {
                // default
                return 'A' == ctrlCancelBtn || ctrlCancelBtn > 'B' || ctrlPos < 'A';
            }

            /**
             * 是否首页 或 Web页，开启分享按钮
             * 
             * @return true if Not 'B'(B:首页 和 Web页均关闭分享按钮显示)
             */
            public boolean enableShare() {
                return 'B' != ctrlShare;
            }

            /* control of share */
            /**
             * 首页是否支持通知显示分享按钮
             * 
             * @return true if equal 'A', 'C' or [*,A] || [D,*]
             */
            public boolean enableShareInHomePage() {
                // default
                return 'A' == ctrlShare || 'C' == ctrlShare || ctrlShare > 'D' || ctrlPos < 'A';
            }

            /**
             * Web页是否支持通知显示分享按钮
             * 
             * @return true if equal 'A' Or 'D'
             */
            public boolean enableShareInWebPage() {
                // default
                return 'A' == ctrlShare || 'D' == ctrlShare || ctrlShare > 'D' || ctrlPos < 'A';
            }

            /* control of use screen shot image */
            public boolean attachNoImage() {
                // default
                return 'A' == ctrlAttachImage || ctrlAttachImage > 'C' || ctrlPos < 'A';
            }

            public boolean attachScreenShot() {
                return 'B' == ctrlAttachImage;
            }

            public boolean attachWebUrlImage() {
                return 'C' == ctrlAttachImage;
            }

            /* control of text effects */
            public boolean isStaicTextEffects() {
                return 'A' == ctrlTextEffects || ctrlTextEffects > 'C' || ctrlPos < 'A';
            }

            public boolean isScrollTextEffects() {
                return 'B' == ctrlTextEffects;
            }

            public boolean isBlingTextEffects() {
                return 'C' == ctrlTextEffects;
            }

            public boolean isLimitShowMaxOnce() {
                return 'B' == ctrlLimitShowMaxOnce;
            }

            /**
             * 
             * 控制字不能为空<br/>
             * 控制字长度不少于所需长度
             * 
             * @return true if valid, otherwise return false.
             */
            public boolean isValid() {

                return true;
            }

            /**
             * 打印调试信息
             * 
             * @return
             */
            public String debug() {
                StringBuilder sb = new StringBuilder();
                sb.append("\n>>>>>>>>>>>");
                sb.append("\nflag:" + text);
                sb.append("\n(" + INDEX_TYPE.INDEX_POS.ordinal() + ")ctrlPos=" + ctrlPos);
                sb.append("\n(" + INDEX_TYPE.INDEX_OPEN_URL.ordinal() + ")ctrlOpenUrl=" + ctrlOpenUrl);
                sb.append("\n(" + INDEX_TYPE.INDEX_DIMISS.ordinal() + ")ctrlDismiss=" + ctrlDimiss);
                sb.append("\n(" + INDEX_TYPE.INDEX_CANCEL_BTN.ordinal() + ")ctrlCancelBtn=" + ctrlCancelBtn);
                sb.append("\n(" + INDEX_TYPE.INDEX_TEXT_EFFECTS.ordinal() + ")ctrlTextEffects=" + ctrlTextEffects);
                sb.append("\n(" + INDEX_TYPE.INDEX_SHARE.ordinal() + ")ctrlShare=" + ctrlShare);
                sb.append("\n(" + INDEX_TYPE.INDEX_ATTACH_IMAGE.ordinal() + ")ctrlAttachImage=" + ctrlAttachImage);
                sb.append("\n(" + INDEX_TYPE.INDEX_LIMIT_SHOW_MAX_ONCE.ordinal() + ")ctrlLimitShowMaxOnce="
                        + ctrlLimitShowMaxOnce);
                sb.append("\n>>>>>>>>>>>");
                return sb.toString();
            }
        }
    }

    /**
     * 打印debug信息
     * 
     * @return
     */
    public String debug() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        sb.append("\nid=" + id);
        sb.append("\nst=" + st);
        sb.append("\net=" + et);
        sb.append("\ndr=" + dr);
        sb.append("\nmsg=\n" + msg.debug());
        sb.append("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        return sb.toString();
    }

    public long getDr() {
        return dr;
    }

    public void setDr(long dr) {
        this.dr = dr;
    }
}
