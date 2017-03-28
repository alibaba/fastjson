package com.alibaba.json.bvt.issue_1000;

import junit.framework.TestCase;

/**
 * Created by wenshao on 20/03/2017.
 */
public class Issue1086 extends TestCase {
    public void test_for_issue() throws Exception {
        String text = "{ 'status': 0, 'message': 'query ok', 'request_id': '6249108055938973921', 'result': { 'location': { 'lat': 39.91765, 'lng': 116.4601 }, 'address': '北京市朝阳区东三环中路7号东三环中路辅路', 'formatted_addresses': { 'recommend': '朝阳区东三环中路北京财富中心财富中心三期', 'rough': '朝阳区东三环中路北京财富中心财富中心三期' }, 'address_component': { 'nation': '中国', 'province': '北京市', 'city': '北京市', 'district': '朝阳区', 'street': '东三环中路辅路', 'street_number': '东三环中路7号' }, 'ad_info': { 'adcode': '110105', 'name': '中国,北京市,北京市,朝阳区', 'location': { 'lat': 39.917648, 'lng': 116.460098 }, 'nation': '中国', 'province': '北京市', 'city': '北京市', 'district': '朝阳区' }, 'address_reference': { 'crossroad': { 'title': '东三环中路辅路/兆丰街(路口)', 'location': { 'lat': 39.91835, 'lng': 116.461349 }, '_distance': 123.9, '_dir_desc': '西南' }, 'village': { 'title': '关东店社区', 'location': { 'lat': 39.917099, 'lng': 116.457176 }, '_distance': 256.9, '_dir_desc': '东' }, 'town': { 'title': '呼家楼街道', 'location': { 'lat': 39.917648, 'lng': 116.460098 }, '_distance': 0, '_dir_desc': '内' }, 'street_number': { 'title': '东三环中路7号', 'location': { 'lat': 39.91597, 'lng': 116.46048 }, '_distance': 0, '_dir_desc': '' }, 'street': { 'title': '东三环中路辅路', 'location': { 'lat': 39.917728, 'lng': 116.46138 }, '_distance': 96.3, '_dir_desc': '西' }, 'landmark_l1': { 'title': '北京财富中心', 'location': { 'lat': 39.91597, 'lng': 116.46048 }, '_distance': 0, '_dir_desc': '内' }, 'landmark_l2': { 'title': '财富中心三期', 'location': { 'lat': 39.917919, 'lng': 116.460533 }, '_distance': 14.3, '_dir_desc': '' } } } }";

    }
}
