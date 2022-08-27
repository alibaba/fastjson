package com.alibaba.json.bvt.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.json.bvtVO.ae.huangliang2.*;
import junit.framework.TestCase;

import java.lang.reflect.Type;

/**
 * Created by wenshao on 09/05/2017.
 */
public class AEHuangliang2Test extends TestCase {
    static String jsonData = "{\n" +
            "    \"areas\": [\n" +
            "      {\n" +
            "        \"@type\": \"section\",\n" +
            "        \"templateId\": \"grid\",\n" +
            "        \"style\": {\n" +
            "          \"card\" : \"true\",\n" +
            "          \"column-count\":\"2\",\n" +
            "          \"aspect-ratio\":\"2\",\n" +
            "          \"margins\":\"16 0 16 16\",\n" +
            "          \"background-color\": \"#ffffff\",\n" +
            "          \"column-gap\": \"10\"\n" +
            "        },\n" +
            "        \"children\": [\n" +
            "        {\n" +
            "        \"@type\": \"section\",\n" +
            "        \"templateId\": \"grid\",\n" +
            "        \"style\": {\n" +
            "          \"card\" : \"true\",\n" +
            "          \"column-count\":\"2\",\n" +
            "          \"aspect-ratio\":\"2\",\n" +
            "          \"margins\":\"16 0 16 16\",\n" +
            "          \"background-color\": \"#ffffff\",\n" +
            "          \"column-gap\": \"10\"\n" +
            "        },\n" +
            "        \"children\": [\n" +
            "          {\n" +
            "            \"@type\": \"floorV2\",\n" +
            "            \"templateId\": \"base\",\n" +
            "            \"image\": \"http://xxx\",\n" +
            "            \"fields\": [\n" +
            "              {\n" +
            "                \"index\": 0,\n" +
            "                \"value\": \"xxxx\",\n" +
            "                \"type\": \"text\",\n" +
            "                \"track\": {\n" +
            "                  \"name\": \"track name\",\n" +
            "                  \"params\": {\n" +
            "                    \"trackParam1\": \"trackParam1\"\n" +
            "                  }\n" +
            "                },\n" +
            "                \"extInfo\": {\n" +
            "                  \"likeByMe\": \"true\",\n" +
            "                  \"isFollowed\": \"true\"\n" +
            "                },\n" +
            "                \"action\": {\n" +
            "                  \"type\": \"click\",\n" +
            "                  \"action\": \"aecmd://nativie/invokeApi?name=key1&likeId=111&likeByMe=true\"\n" +
            "                }\n" +
            "              }\n" +
            "            ],\n" +
            "            \"bizId\": \"banner-myae-1-746877468\",\n" +
            "            \"style\": {\n" +
            "              \"card\" : \"true\",\n" +
            "              \"background-color\": \"#000000\"\n" +
            "            },\n" +
            "            \"isTest\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"@type\": \"floorV2\",\n" +
            "            \"templateId\": \"base\",\n" +
            "            \"image\": \"http://xxx\",\n" +
            "            \"fields\": [\n" +
            "              {\n" +
            "                \"index\": 0,\n" +
            "                \"value\": \"xxxx\",\n" +
            "                \"type\": \"text\",\n" +
            "                \"track\": {\n" +
            "                  \"name\": \"track name\",\n" +
            "                  \"params\": {\n" +
            "                    \"trackParam1\": \"trackParam1\"\n" +
            "                  }\n" +
            "                },\n" +
            "                \"action\": {\n" +
            "                  \"type\": \"click\",\n" +
            "                  \"action\": \"aecmd://xxxx\"\n" +
            "                }\n" +
            "              }\n" +
            "            ],\n" +
            "            \"extInfo\": {\n" +
            "              \"likeByMe\": \"true\"\n" +
            "            },\n" +
            "            \"bizId\": \"banner-myae-1-746877468\",\n" +
            "            \"style\": {\n" +
            "              \"card\" : \"true\",\n" +
            "              \"background-color\": \"#ffc1c1\"\n" +
            "            },\n" +
            "            \"isTest\": false\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "          {\n" +
            "            \"@type\": \"floorV2\",\n" +
            "            \"templateId\": \"base\",\n" +
            "            \"image\": \"http://xxx\",\n" +
            "            \"fields\": [\n" +
            "              {\n" +
            "                \"index\": 0,\n" +
            "                \"value\": \"xxxx\",\n" +
            "                \"type\": \"text\",\n" +
            "                \"track\": {\n" +
            "                  \"name\": \"track name\",\n" +
            "                  \"params\": {\n" +
            "                    \"trackParam1\": \"trackParam1\"\n" +
            "                  }\n" +
            "                },\n" +
            "                \"extInfo\": {\n" +
            "                  \"likeByMe\": \"true\",\n" +
            "                  \"isFollowed\": \"true\"\n" +
            "                },\n" +
            "                \"action\": {\n" +
            "                  \"type\": \"click\",\n" +
            "                  \"action\": \"aecmd://nativie/invokeApi?name=key1&likeId=111&likeByMe=true\"\n" +
            "                }\n" +
            "              }\n" +
            "            ],\n" +
            "            \"bizId\": \"banner-myae-1-746877468\",\n" +
            "            \"style\": {\n" +
            "              \"card\" : \"true\",\n" +
            "              \"background-color\": \"#000000\"\n" +
            "            },\n" +
            "            \"isTest\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"@type\": \"floorV2\",\n" +
            "            \"templateId\": \"base\",\n" +
            "            \"image\": \"http://xxx\",\n" +
            "            \"fields\": [\n" +
            "              {\n" +
            "                \"index\": 0,\n" +
            "                \"value\": \"xxxx\",\n" +
            "                \"type\": \"text\",\n" +
            "                \"track\": {\n" +
            "                  \"name\": \"track name\",\n" +
            "                  \"params\": {\n" +
            "                    \"trackParam1\": \"trackParam1\"\n" +
            "                  }\n" +
            "                },\n" +
            "                \"action\": {\n" +
            "                  \"type\": \"click\",\n" +
            "                  \"action\": \"aecmd://xxxx\"\n" +
            "                }\n" +
            "              }\n" +
            "            ],\n" +
            "            \"extInfo\": {\n" +
            "              \"likeByMe\": \"true\"\n" +
            "            },\n" +
            "            \"bizId\": \"banner-myae-1-746877468\",\n" +
            "            \"style\": {\n" +
            "              \"card\" : \"true\",\n" +
            "              \"background-color\": \"#ffc1c1\"\n" +
            "            },\n" +
            "            \"isTest\": false\n" +
            "          }\n" +
            "        ]\n" +
            "      }\n" +
            "    ],\n" +
            "    \"version\": 3,\n" +
            "    \"currency\": \"RUB\"\n" +
            "  }";

    static String floordata = "{\n" +
            "    \"isTest\": true,\n" +
            "    \"mockResult\": {\n" +
            "  \"body\": {\n" +
            "    \"areas\": [\n" +
            "      {\n" +
            "        \"@type\": \"section\",\n" +
            "        \"templateId\": \"grid\",\n" +
            "        \"style\": {\n" +
            "          \"card\" : \"true\",\n" +
            "          \"column-count\":\"2\",\n" +
            "          \"aspect-ratio\":\"2\",\n" +
            "          \"margins\":\"16 0 16 16\",\n" +
            "          \"background-color\": \"#ffffff\",\n" +
            "          \"column-gap\": \"10\"\n" +
            "        },\n" +
            "        \"children\": [\n" +
            "        {\n" +
            "        \"@type\": \"section\",\n" +
            "        \"templateId\": \"grid\",\n" +
            "        \"style\": {\n" +
            "          \"card\" : \"true\",\n" +
            "          \"column-count\":\"2\",\n" +
            "          \"aspect-ratio\":\"2\",\n" +
            "          \"margins\":\"16 0 16 16\",\n" +
            "          \"background-color\": \"#ffffff\",\n" +
            "          \"column-gap\": \"10\"\n" +
            "        },\n" +
            "        \"children\": [\n" +
            "          {\n" +
            "            \"@type\": \"floorV2\",\n" +
            "            \"templateId\": \"base\",\n" +
            "            \"image\": \"http://xxx\",\n" +
            "            \"fields\": [\n" +
            "              {\n" +
            "                \"index\": 0,\n" +
            "                \"value\": \"xxxx\",\n" +
            "                \"type\": \"text\",\n" +
            "                \"track\": {\n" +
            "                  \"name\": \"track name\",\n" +
            "                  \"params\": {\n" +
            "                    \"trackParam1\": \"trackParam1\"\n" +
            "                  }\n" +
            "                },\n" +
            "                \"extInfo\": {\n" +
            "                  \"likeByMe\": \"true\",\n" +
            "                  \"isFollowed\": \"true\"\n" +
            "                },\n" +
            "                \"action\": {\n" +
            "                  \"type\": \"click\",\n" +
            "                  \"action\": \"aecmd://nativie/invokeApi?name=key1&likeId=111&likeByMe=true\"\n" +
            "                }\n" +
            "              }\n" +
            "            ],\n" +
            "            \"bizId\": \"banner-myae-1-746877468\",\n" +
            "            \"style\": {\n" +
            "              \"card\" : \"true\",\n" +
            "              \"background-color\": \"#000000\"\n" +
            "            },\n" +
            "            \"isTest\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"@type\": \"floorV2\",\n" +
            "            \"templateId\": \"base\",\n" +
            "            \"image\": \"http://xxx\",\n" +
            "            \"fields\": [\n" +
            "              {\n" +
            "                \"index\": 0,\n" +
            "                \"value\": \"xxxx\",\n" +
            "                \"type\": \"text\",\n" +
            "                \"track\": {\n" +
            "                  \"name\": \"track name\",\n" +
            "                  \"params\": {\n" +
            "                    \"trackParam1\": \"trackParam1\"\n" +
            "                  }\n" +
            "                },\n" +
            "                \"action\": {\n" +
            "                  \"type\": \"click\",\n" +
            "                  \"action\": \"aecmd://xxxx\"\n" +
            "                }\n" +
            "              }\n" +
            "            ],\n" +
            "            \"extInfo\": {\n" +
            "              \"likeByMe\": \"true\"\n" +
            "            },\n" +
            "            \"bizId\": \"banner-myae-1-746877468\",\n" +
            "            \"style\": {\n" +
            "              \"card\" : \"true\",\n" +
            "              \"background-color\": \"#ffc1c1\"\n" +
            "            },\n" +
            "            \"isTest\": false\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "          {\n" +
            "            \"@type\": \"floorV2\",\n" +
            "            \"templateId\": \"base\",\n" +
            "            \"image\": \"http://xxx\",\n" +
            "            \"fields\": [\n" +
            "              {\n" +
            "                \"index\": 0,\n" +
            "                \"value\": \"xxxx\",\n" +
            "                \"type\": \"text\",\n" +
            "                \"track\": {\n" +
            "                  \"name\": \"track name\",\n" +
            "                  \"params\": {\n" +
            "                    \"trackParam1\": \"trackParam1\"\n" +
            "                  }\n" +
            "                },\n" +
            "                \"extInfo\": {\n" +
            "                  \"likeByMe\": \"true\",\n" +
            "                  \"isFollowed\": \"true\"\n" +
            "                },\n" +
            "                \"action\": {\n" +
            "                  \"type\": \"click\",\n" +
            "                  \"action\": \"aecmd://nativie/invokeApi?name=key1&likeId=111&likeByMe=true\"\n" +
            "                }\n" +
            "              }\n" +
            "            ],\n" +
            "            \"bizId\": \"banner-myae-1-746877468\",\n" +
            "            \"style\": {\n" +
            "              \"card\" : \"true\",\n" +
            "              \"background-color\": \"#000000\"\n" +
            "            },\n" +
            "            \"isTest\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"@type\": \"floorV2\",\n" +
            "            \"templateId\": \"base\",\n" +
            "            \"image\": \"http://xxx\",\n" +
            "            \"fields\": [\n" +
            "              {\n" +
            "                \"index\": 0,\n" +
            "                \"value\": \"xxxx\",\n" +
            "                \"type\": \"text\",\n" +
            "                \"track\": {\n" +
            "                  \"name\": \"track name\",\n" +
            "                  \"params\": {\n" +
            "                    \"trackParam1\": \"trackParam1\"\n" +
            "                  }\n" +
            "                },\n" +
            "                \"action\": {\n" +
            "                  \"type\": \"click\",\n" +
            "                  \"action\": \"aecmd://xxxx\"\n" +
            "                }\n" +
            "              }\n" +
            "            ],\n" +
            "            \"extInfo\": {\n" +
            "              \"likeByMe\": \"true\"\n" +
            "            },\n" +
            "            \"bizId\": \"banner-myae-1-746877468\",\n" +
            "            \"style\": {\n" +
            "              \"card\" : \"true\",\n" +
            "              \"background-color\": \"#ffc1c1\"\n" +
            "            },\n" +
            "            \"isTest\": false\n" +
            "          }\n" +
            "        ]\n" +
            "      }\n" +
            "    ],\n" +
            "    \"version\": 3,\n" +
            "    \"currency\": \"RUB\"\n" +
            "  },\n" +
            "  \"head\": {\n" +
            "    \"message\": \"\",\n" +
            "    \"serverTime\": 1489473042814,\n" +
            "    \"code\": \"200\",\n" +
            "    \"ab\": \"yepxf_B\"\n" +
            "  }\n" +
            "}\n" +
            "}";

    public void test_for_issue() throws Exception {
        ParserConfig.getGlobalInstance().putDeserializer(Area.class, new ObjectDeserializer() {

            public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
                JSONObject jsonObject = (JSONObject) parser.parse();
                String areaType;

                if (jsonObject.get("type") instanceof String) {
                    areaType = (String) jsonObject.get("type");
                } else {
                    return null;
                }
                if (Area.TYPE_SECTION.equals(areaType)) {
                    String text = jsonObject.toJSONString();
                    return (T) JSON.parseObject(text, Section.class);
                } else if (Area.TYPE_FLOORV1.equals(areaType)) {
                    String text = jsonObject.toJSONString();
                    return (T) JSON.parseObject(text, FloorV1.class);
                } else if (Area.TYPE_FLOORV2.equals(areaType)) {
                    String text = jsonObject.toJSONString();
                    return (T) JSON.parseObject(text, FloorV2.class);
                }

                return null;
            }

            public int getFastMatchToken() {
                return JSONToken.LBRACE;
            }
        });

        ParserConfig.getGlobalInstance().addAccept("section");
        ParserConfig.getGlobalInstance().addAccept("floorV2");


        MockResult data = JSON.parseObject(floordata, MockResult.class);
        String mockResultJson = JSON.toJSONString(data.mockResult);
        NetResponse response = JSON.parseObject(mockResultJson, NetResponse.class);

        String bodyJson = JSON.toJSONString(response.body);
        System.out.println(bodyJson);
        FloorPageData pageData = JSON.parseObject(bodyJson, FloorPageData.class);
        assertNotNull(pageData.areas);
    }
}
