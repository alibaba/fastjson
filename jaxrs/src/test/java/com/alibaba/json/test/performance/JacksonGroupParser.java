package com.alibaba.json.test.performance;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONException;
import com.alibaba.json.test.entity.Company;
import com.alibaba.json.test.entity.Department;
import com.alibaba.json.test.entity.Employee;
import com.alibaba.json.test.entity.Group;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JacksonGroupParser {

    private JsonParser parser;

    public JacksonGroupParser(JsonParser parser) throws Exception{
        this.parser = parser;
        parser.nextToken(); // move to the start of the
    }

    public Group parseGroup() throws Exception {
        Group group = new Group();

        accept(JsonToken.START_OBJECT);

        for (;;) {
            JsonToken tok = parser.getCurrentToken();

            if (tok == JsonToken.END_OBJECT) {
                break;
            }

            if (tok == JsonToken.FIELD_NAME) {
                String name = parser.getCurrentName();
                tok = parser.nextToken();

                if ("name" == name) {
                    group.setName(acceptString(tok));
                } else if ("description" == name) {
                    group.setDescription(acceptString(tok));
                } else if ("companies" == name) {
                    parseCompany(group.getCompanies());
                } else {
                    throw new RuntimeException("not match property : " + name);
                }
            }
        }

        accept(JsonToken.END_OBJECT);

        return group;
    }

    private Company parseCompany() throws Exception {
        Company company = new Company();

        accept(JsonToken.START_OBJECT);

        for (;;) {
            JsonToken tok = parser.getCurrentToken();

            if (tok == JsonToken.END_OBJECT) {
                break;
            }

            if (tok == JsonToken.FIELD_NAME) {
                String name = parser.getCurrentName();
                tok = parser.nextToken();

                if ("name" == name) {
                    company.setName(acceptString(tok));
                } else if ("description" == name) {
                    company.setDescription(acceptString(tok));
                } else if ("stock" == name) {
                    company.setStock(acceptString(tok));
                } else if ("id" == name) {
                    company.setId(acceptLong(tok));
                } else if ("rootDepartment" == name) {
                    company.setRootDepartment(parseDepartment());
                } else {
                    throw new RuntimeException("not match property : " + name);
                }
            }
        }

        accept(JsonToken.END_OBJECT);

        return company;
    }

    private Department parseDepartment() throws Exception {
        accept(JsonToken.START_OBJECT);

        Department company = new Department();

        for (;;) {
            JsonToken tok = parser.getCurrentToken();

            if (tok == JsonToken.END_OBJECT) {
                break;
            }

            if (tok == JsonToken.FIELD_NAME) {
                String name = parser.getCurrentName();
                tok = parser.nextToken();

                if ("name" == name) {
                    company.setName(acceptString(tok));
                } else if ("description" == name) {
                    company.setDescription(acceptString(tok));
                } else if ("id" == name) {
                    company.setId(acceptLong(tok));
                } else if ("children" == name) {
                    parseDepartment(company.getChildren());
                } else if ("members" == name) {
                    parseEmployee(company.getMembers());
                } else {
                    throw new RuntimeException("not match property : " + name);
                }
            }
        }

        accept(JsonToken.END_OBJECT);

        return company;
    }

    private Employee parseEmployee() throws Exception {
        accept(JsonToken.START_OBJECT);

        Employee emp = new Employee();

        for (;;) {
            JsonToken tok = parser.getCurrentToken();

            if (tok == JsonToken.END_OBJECT) {
                break;
            }

            if (tok == JsonToken.FIELD_NAME) {
                String name = parser.getCurrentName();
                tok = parser.nextToken();

                if ("name" == name) {
                    emp.setName(acceptString(tok));
                } else if ("description" == name) {
                    emp.setDescription(acceptString(tok));
                } else if ("number" == name) {
                    emp.setNumber(acceptString(tok));
                } else if ("id" == name) {
                    emp.setId(acceptLong(tok));
                } else if ("age" == name) {
                    emp.setAge(acceptInteger(tok));
                } else if ("salary" == name) {
                    emp.setSalary(acceptBigDecimal(tok));
                } else if ("birthdate" == name) {
                    emp.setBirthdate(new Date(acceptLong(tok)));
                } else if ("badboy" == name) {
                    emp.setBadboy(acceptBoolean(tok));
                } else {
                    throw new RuntimeException("not match property : " + name);
                }
            }
        }

        accept(JsonToken.END_OBJECT);

        return emp;
    }

    private void parseEmployee(List<Employee> list) throws Exception {
        accept(JsonToken.START_ARRAY);
        for (;;) {
            JsonToken tok = parser.getCurrentToken();

            if (tok == JsonToken.END_ARRAY) {
                break;
            }

            list.add(parseEmployee());
        }
        accept(JsonToken.END_ARRAY);
    }

    private void parseDepartment(List<Department> list) throws Exception {
        accept(JsonToken.START_ARRAY);
        for (;;) {
            JsonToken tok = parser.getCurrentToken();

            if (tok == JsonToken.END_ARRAY) {
                break;
            }

            list.add(parseDepartment());
        }
        accept(JsonToken.END_ARRAY);
    }

    private void parseCompany(List<Company> list) throws Exception {
        accept(JsonToken.START_ARRAY);
        for (;;) {
            JsonToken tok = parser.getCurrentToken();

            if (tok == JsonToken.END_ARRAY) {
                break;
            }

            list.add(parseCompany());
        }
        accept(JsonToken.END_ARRAY);
    }

    private String acceptString() throws Exception {
        if (parser.getCurrentToken() == JsonToken.VALUE_STRING) {
            String stringValue = parser.getText();
            parser.nextToken();
            return stringValue;
        } else {
            throw new JSONException("syntax error, expect string, actual " + parser.getCurrentToken());
        }
    }

    private String acceptString(JsonToken token) throws Exception {
        if (token == JsonToken.VALUE_STRING) {
            String stringValue = parser.getText();
            parser.nextToken();
            return stringValue;
        } else {
            throw new JSONException("syntax error, expect string, actual " + parser.getCurrentToken());
        }
    }

    private Long acceptLong(JsonToken token) throws Exception {
        if (token == JsonToken.VALUE_NUMBER_INT) {
            long value = parser.getLongValue();
            parser.nextToken();
            return value;
        } else {
            throw new JSONException("syntax error, expect string, actual " + parser.getCurrentToken());
        }
    }

    private boolean acceptBoolean(JsonToken token) throws Exception {
        if (token == JsonToken.VALUE_TRUE) {
            parser.nextToken();
            return true;
        } else if (token == JsonToken.VALUE_FALSE) {
            parser.nextToken();
            return false;
        } else {
            throw new JSONException("syntax error, expect string, actual " + parser.getCurrentToken());
        }
    }

    private BigDecimal acceptBigDecimal(JsonToken token) throws Exception {
        if (token == JsonToken.VALUE_NUMBER_FLOAT) {
            BigDecimal value = parser.getDecimalValue();
            parser.nextToken();
            return value;
        } else {
            throw new JSONException("syntax error, expect string, actual " + parser.getCurrentToken());
        }
    }

    private Integer acceptInteger(JsonToken token) throws Exception {
        if (token == JsonToken.VALUE_NUMBER_INT) {
            int value = parser.getIntValue();
            parser.nextToken();
            return value;
        } else {
            throw new JSONException("syntax error, expect string, actual " + parser.getCurrentToken());
        }
    }

    private void accept(JsonToken token) throws Exception {
        if (parser.getCurrentToken() == token) {
            parser.nextToken();
        } else {
            throw new JSONException("syntax error, expect " + token + ", actual " + parser.getCurrentToken());
        }
    }

    public static String getNextTextValue(String fieldName, JsonParser parser) throws JsonParseException, IOException {
        JsonToken current = parser.nextToken(); // move to filed
        if (current != JsonToken.FIELD_NAME || !fieldName.equals(parser.getCurrentName())) {
            reportParseError("Error occoured while getting value by field name:" + fieldName, parser.getCurrentLocation());
        }
        current = parser.nextToken(); // move to value
        return parser.getText();
    }

    public static void reportParseError(String errorMsg, JsonLocation jsonLoc) throws JsonParseException {
        throw new JsonParseException(errorMsg, jsonLoc);
    }
}
