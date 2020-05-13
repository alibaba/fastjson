package com.alibaba.json.bvt.bug;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;

public class bug_for_caoyaojun1988 extends TestCase {

    public void test_for_bug() throws Exception {
        // 创建 BusinessVO
        BusinessVO businessVO = new BusinessVO();
        businessVO.setName("name");

        // 创建 第一个List list中每一个对象都包含 BusinessVO对象
        ExpiredDto expiredDto = new ExpiredDto();
        expiredDto.setBusinessVO(businessVO);
        expiredDto.setId(10001);

        List<ExpiredDto> expiredReports = new ArrayList<ExpiredDto>();
        expiredReports.add(expiredDto);

        // 创建 第二个List list中每一个对象都包含 BusinessVO对象

        List<NormalDto> normalReports = new ArrayList<NormalDto>();
        {
            NormalDto normalDto = new NormalDto();
            normalDto.setBusinessVO(businessVO);
            normalDto.setId(10001);
            normalReports.add(normalDto);
        }

        // 创建 需要序列化的对象，包含两个list
        ReportDto reportDto = new ReportDto();
        reportDto.setExpiredReports(expiredReports);
        reportDto.setNormalReports(normalReports);
        reportDto.setCompanyId(10004);

        // 第一个场景 得到的businessVO为null；
        String serializeStr = (String) JSON.toJSONString(reportDto);
        System.out.println(serializeStr);
        ReportDto reuslt = (ReportDto) JSON.parseObject(serializeStr, ReportDto.class);
        System.out.println(reuslt.getNormalReports().get(0).getBusinessVO());

        // 第二个场景 得到的businessVO为正常数据
        expiredReports.add(expiredDto);
        serializeStr = (String) JSON.toJSONString(reportDto);
        System.out.println(serializeStr);
        reuslt = (ReportDto) JSON.parseObject(serializeStr, ReportDto.class);
        System.out.print(reuslt.getNormalReports().get(0).getBusinessVO().getName());
    }

    public static class BusinessVO implements Serializable {

        private static final long serialVersionUID = -191856665415285103L;
        private String            name;
        
        public BusinessVO() {
            
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }

    public static class ExpiredDto implements Serializable {

        private static final long serialVersionUID = -2361763020563748437L;
        private Integer           id;
        private BusinessVO        businessVO;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public BusinessVO getBusinessVO() {
            return businessVO;
        }

        public void setBusinessVO(BusinessVO businessVO) {
            this.businessVO = businessVO;
        }

    }

    public static class NormalDto implements Serializable {

        private static final long serialVersionUID = -2392077150026945111L;
        private Integer           id;
        private BusinessVO        businessVO;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public BusinessVO getBusinessVO() {
            return businessVO;
        }

        public void setBusinessVO(BusinessVO businessVO) {
            this.businessVO = businessVO;
        }

        public static long getSerialversionuid() {
            return serialVersionUID;
        }

    }

    public static class ReportDto implements Serializable {

        private static final long serialVersionUID = 4502937258945851832L;
        private Integer           companyId;
        private List<NormalDto>   normalReports;
        private List<ExpiredDto>  expiredReports;

        public Integer getCompanyId() {
            return companyId;
        }

        public void setCompanyId(Integer companyId) {
            this.companyId = companyId;
        }

        public List<NormalDto> getNormalReports() {
            return normalReports;
        }

        public void setNormalReports(List<NormalDto> normalReports) {
            this.normalReports = normalReports;
        }

        public List<ExpiredDto> getExpiredReports() {
            return expiredReports;
        }

        public void setExpiredReports(List<ExpiredDto> expiredReports) {
            this.expiredReports = expiredReports;
        }

    }

}
