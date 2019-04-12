package com.alibaba.json.bvt.issue_1500;

import org.junit.Assert;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;

import apijson.demo.server.model.RealmBean;
import io.realm.Realm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;
import io.realm.internal.OsSharedRealm;
import io.realm.internal.RealmNotifier;
import zuo.biao.apijson.Log;

import java.util.List;

/**解决序列化热门数据库 Realm 崩溃 
 * https://github.com/alibaba/fastjson/issues/1536
 * @author TommyLemon
 */
public class Issue1536 {
	private static final String TAG = "JSON";

	
	public static void main(String[] args) {
		RealmBean bean = new RealmBean();
		bean.setId(123L);
		bean.setUrl("https://github.com/TommyLemon/APIJSON");
		
		Assert.assertEquals("{\"id\":123,\"url\":\"https://github.com/TommyLemon/APIJSON\"}", toJSONString(bean));
	}
	
	
	/**实体类转json
	 * @param obj
	 * @return
	 */
	public static String toJSONString(final Object obj) {
		if (obj instanceof String) {
			return (String) obj;
		}
    
		try {
			PropertyFilter filter = new PropertyFilter() {
				@Override
				public boolean apply(Object object, String name, Object value) {
					if (object instanceof Realm
							|| object instanceof OsSharedRealm
							|| object instanceof RealmSchema
							|| object instanceof RealmMigration
							|| object instanceof RealmNotifier
							) {
						return false;
					}

					if (name.equalsIgnoreCase("realm")
							|| name.equalsIgnoreCase("loaded")
							|| name.equalsIgnoreCase("valid")
							|| name.equalsIgnoreCase("managed")
							){
						//false表示tel字段将被排除在外
						return false;
					}
					return true;
				}
			};

			return com.alibaba.fastjson.JSON.toJSONString(obj, filter);
		} catch (Exception e) {
			Log.e(TAG, "toJSONString  catch \n" + e.getMessage());
		}
		return null;
	}
  
}
