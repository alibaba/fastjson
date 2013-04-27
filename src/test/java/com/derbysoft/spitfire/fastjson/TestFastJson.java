package com.derbysoft.spitfire.fastjson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.derbysoft.spitfire.fastjson.dto.AvailGuaranteeDTO;
import com.derbysoft.spitfire.fastjson.dto.AvailRoomStayDTO;
import com.derbysoft.spitfire.fastjson.dto.CancelPenaltyType;
import com.derbysoft.spitfire.fastjson.dto.CancelPolicyDTO;
import com.derbysoft.spitfire.fastjson.dto.CardCode;
import com.derbysoft.spitfire.fastjson.dto.ChargeItemDTO;
import com.derbysoft.spitfire.fastjson.dto.ChargeType;
import com.derbysoft.spitfire.fastjson.dto.ChargeUnit;
import com.derbysoft.spitfire.fastjson.dto.Currency;
import com.derbysoft.spitfire.fastjson.dto.DateRangeDTO;
import com.derbysoft.spitfire.fastjson.dto.FreeMealDTO;
import com.derbysoft.spitfire.fastjson.dto.FreeMealType;
import com.derbysoft.spitfire.fastjson.dto.GenericRS;
import com.derbysoft.spitfire.fastjson.dto.GuaranteeType;
import com.derbysoft.spitfire.fastjson.dto.HotelAvailRS;
import com.derbysoft.spitfire.fastjson.dto.HotelAvailRoomStayDTO;
import com.derbysoft.spitfire.fastjson.dto.HotelRefDTO;
import com.derbysoft.spitfire.fastjson.dto.LanguageType;
import com.derbysoft.spitfire.fastjson.dto.MealsIncludedDTO;
import com.derbysoft.spitfire.fastjson.dto.MealsIncludedType;
import com.derbysoft.spitfire.fastjson.dto.PaymentType;
import com.derbysoft.spitfire.fastjson.dto.ProviderChainDTO;
import com.derbysoft.spitfire.fastjson.dto.RateDTO;
import com.derbysoft.spitfire.fastjson.dto.RatePlanDTO;
import com.derbysoft.spitfire.fastjson.dto.ResponseHeader;
import com.derbysoft.spitfire.fastjson.dto.RoomRateDTO;
import com.derbysoft.spitfire.fastjson.dto.RoomTypeDTO;
import com.derbysoft.spitfire.fastjson.dto.SimpleAmountDTO;
import com.derbysoft.spitfire.fastjson.dto.TPAExtensionsDTO;
import com.derbysoft.spitfire.fastjson.dto.UniqueIDDTO;
import com.derbysoft.spitfire.fastjson.dto.UniqueIDType;

//import com.derbysoft.spitfire.fastjson.dto.*;

public class TestFastJson {

    private static final int TIMES       = 10000;
    private static final int STAYS_COUNT = 10;

    public void f_testF() {
        Generic<String> q = new Generic<String>();
        byte[] text = JSON.toJSONBytes(q, SerializerFeature.WriteClassName);
        JSON.parseObject(text, Generic.class);
    }

    @SuppressWarnings("unchecked")
    public void f_test() throws Exception {

        String text = JSON.toJSONString(createTest(), SerializerFeature.WriteClassName);
        System.out.println(text.length());
        System.out.println(text);
        System.out.println("serialize finished");
        GenericRS<HotelAvailRS> o = (GenericRS<HotelAvailRS>) JSON.parseObject(text, GenericRS.class);

        System.out.println(o);
    }

    public void testFP() throws IOException {
        Generic<String> q = new Generic<String>();
        for (int x = 0; x < STAYS_COUNT; ++x) {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            for (int i = 0; i < TIMES; ++i) {
                jsonSerialize(q);
            }
            stopWatch.stop();

            System.out.println("JSON serialize:" + stopWatch.getTime());

            stopWatch.reset();
            stopWatch.start();
            for (int i = 0; i < TIMES; ++i) {
                javaSerialize(q);
            }
            stopWatch.stop();
            System.out.println("JAVA serialize:" + stopWatch.getTime());
            System.out.println();
        }
    }

    private <T> void jsonSerialize(T t) throws IOException {
        // String text = JSON.toJSONString(t, SerializerFeature.WriteClassName, SerializerFeature.PrettyFormat);
        // System.out.println(text);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        SerializeWriter out = new SerializeWriter(SerializerFeature.WriteClassName);
        // SerializeWriter out = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(out);
        serializer.write(t);
        out.writeTo(os, "UTF-8");
        os.toByteArray();
        //System.out.println(JSON.toJSONString(t, SerializerFeature.WriteClassName, SerializerFeature.PrettyFormat));
        //System.out.println("json " + os.toByteArray().length);
    }

    private <T> void javaSerialize(T t) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(t);
        // System.out.println("java " + os.toByteArray().length);
    }

    @SuppressWarnings("unchecked")
    private <T> T jsonDeserialize(byte[] bytes, Class<T> clazz) throws IOException {
        return (T) JSON.parseObject(bytes, clazz);
    }

    @SuppressWarnings("unchecked")
    private <T> T javaDeserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(is);
        return (T) ois.readObject();
    }

    @Test
    public void testSerializePerformance() throws IOException {
        Object obj = createTest();

        for (int x = 0; x < 20; ++x) {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            for (int i = 0; i < TIMES; ++i) {
                jsonSerialize(obj);
            }
            stopWatch.stop();

            System.out.println("JSON serialize:" + stopWatch.getTime());

            stopWatch.reset();
            stopWatch.start();
            for (int i = 0; i < TIMES; ++i) {
                javaSerialize(obj);
            }
            stopWatch.stop();
            System.out.println("JAVA serialize:" + stopWatch.getTime());
            System.out.println();
        }
    }

    public void testDeserializePerformance() throws IOException, ClassNotFoundException {
        Object obj = createTest();
        byte[] bytes = JSON.toJSONBytes(obj, SerializerFeature.WriteClassName);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(obj);
        byte[] javaBytes = os.toByteArray();

        System.out.println(bytes.length);

        for (int x = 0; x < 20; ++x) {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            for (int i = 0; i < TIMES; ++i) {
                // ByteArrayInputStream is = new ByteArrayInputStream(bytes);
                Object o = jsonDeserialize(bytes, GenericRS.class);
                o.getClass();
            }
            stopWatch.stop();

            System.out.println("JSON deserialize:" + stopWatch.getTime());

            stopWatch.reset();

            stopWatch.start();
            for (int i = 0; i < TIMES; ++i) {
                Object o = javaDeserialize(javaBytes);
                o.getClass();
            }
            stopWatch.stop();

            System.out.println("JAVA deserialize:" + stopWatch.getTime());
            System.out.println();
        }

    }

    public GenericRS<HotelAvailRS> createTest() {
        GenericRS<HotelAvailRS> rs = new GenericRS<HotelAvailRS>();
        rs.setHeader(new ResponseHeader("dsfsdfsd"));
        HotelAvailRS hotelAvailRS = createExpectedHotelAvailRS();
        TPAExtensionsDTO extensions = new TPAExtensionsDTO();
        extensions.getElements().put("dfd", "Dfdf");
        hotelAvailRS.setTpaExtensions(extensions);
        rs.setPayload(hotelAvailRS);
        return rs;
    }

    // private static final String HOTEL_DESC = "foo hotel desc";
    private static final String       ROOM_TYPE_CODE            = "foo room type code";
    private static final String       ROOM_TYPE_NAME            = "foo room type name";
    private static final String       RATE_PLAN_CODE            = "foo rate plan code";
    private static final String       RATE_PLAN_NAME            = "rate plan name";
    private static final Integer      ROOM_COUNT                = 2;

    // private static final Date CANCEL_POLICY_DEAD_LINE = DateUtils.parseUseDefaultFormat("2008-09-01");
    private static final String       CANCEL_POLICY_DESCRIPTION = "foo cancel policy description";

    private static final String       GUARANTEE_DESCRIPTION     = "foo guarantee description";
    // private static final String CARD_NUMBER = "foo card number";
    // private static final String CARD_HOLDER_NAME = "foo card holder name";
    // private static final String CARD_SERIES_CODE = "foo card series code";
    // private static final Date CARD_EXPIRE_DATE = DateUtils.parseUseDefaultFormat("2010-12-31");
    private static final Currency     CURRENCY                  = Currency.CNY;

    private static final PaymentType  PAYMENT_TYPE_POA          = PaymentType.POA;
    // private static final int DAY_COUNT = 4;
    // private static final String[] CHECKIN_DATES = {"2008-08-25", "2008-08-26", "2008-08-27", "2008-08-28"};
    // private static final String[] CHECKOUT_DATES = {"2008-08-26", "2008-08-27", "2008-08-28", "2008-08-29"};
    // private static final BigDecimal[] AMOUNT_AFTER_TAXS
    // = {new BigDecimal(800), new BigDecimal(800), new BigDecimal(800), new BigDecimal(900)};
    private static final BigDecimal[] AMOUNT_BEFORE_TAXS        = { new BigDecimal(750), new BigDecimal(750),
            new BigDecimal(760), new BigDecimal(880)           };
    private static final BigDecimal   AMOUNT_TAX                = new BigDecimal(50);
    private static final BigDecimal   SERVICE_CHARGE_AMOUNT     = new BigDecimal(10);
    private static final String       TAX_DESC                  = "foo tax desc";
    private static final String       SERVICE_CHARGE_DESC       = "foo repository charge desc";

    private static final String       PROVIDER_CODE             = "hilton";
    private static final String       HOTEL_NAME                = "foo hotel name";
    private static final String       HOTEL_CODE                = "foo hotel code";
    private static final LanguageType LANGUAGE_TYPE_CN          = LanguageType.ZH_CN;
    // private static final String TASK_ID = "task id";
    private static final boolean      NEED_GUARANTEE            = true;
    private static final CardCode     VISA                      = CardCode.VISA;
    // private static final String CARD_CODE = VISA.getCode();

    private static final int          NUMBER_ZERO               = 0;
    private static final int          NUMBER_ONE                = 1;
    private static final int          NUMBER_TWO                = 2;
    private static final int          NUMBER_THREE              = 3;
    
    private static AtomicLong seed = new AtomicLong();

    private HotelAvailRS createExpectedHotelAvailRS() {
        HotelAvailRS payLoad = new HotelAvailRS();
        payLoad.setHotelAvailRoomStay(createExpectedHotelAvailRoomStay());
        return payLoad;
    }

    private HotelAvailRoomStayDTO createExpectedHotelAvailRoomStay() {
        HotelAvailRoomStayDTO hotelAvailRoomStay = new HotelAvailRoomStayDTO();
        hotelAvailRoomStay.setHotelRef(createExpectedHotelRef());
        hotelAvailRoomStay.setRoomStays(createExpectedRoomStays());
        return hotelAvailRoomStay;
    }

    private List<AvailRoomStayDTO> createExpectedRoomStays() {
        ArrayList<AvailRoomStayDTO> roomStays = new ArrayList<AvailRoomStayDTO>();
        for (int i = 0; i < STAYS_COUNT; ++i) {
            AvailRoomStayDTO roomStay = new AvailRoomStayDTO();
            roomStay.setLanguageType(LANGUAGE_TYPE_CN);
            roomStay.setRoomType(createExpectedRoomType());
            roomStay.setRatePlan(createExpectedRatePlan());
            roomStay.setQuantity(ROOM_COUNT);
            roomStay.setRoomRate(createExpectedRoomRate());
            roomStay.setProviderChain(createExpectedProviderChain());
            roomStays.add(roomStay);
        }

        return roomStays;
    }

    private ProviderChainDTO createExpectedProviderChain() {
        ProviderChainDTO providerChain = new ProviderChainDTO();
        List<UniqueIDDTO> providers = new ArrayList<UniqueIDDTO>();
        UniqueIDDTO provider = new UniqueIDDTO(PROVIDER_CODE, UniqueIDType.HOTEL);
        provider.setCompanyName(PROVIDER_CODE);
        providers.add(provider);
        providerChain.setProviders(providers);
        return providerChain;
    }

    private List<AvailGuaranteeDTO> createExpectedAvailGuarantee() {
        List<AvailGuaranteeDTO> availGuarantees = new ArrayList<AvailGuaranteeDTO>();
        AvailGuaranteeDTO availGuaranteeDTO = new AvailGuaranteeDTO();
        availGuaranteeDTO.setGuaranteeType(GuaranteeType.CreditCard);
        availGuaranteeDTO.setCardCode(VISA);
        availGuaranteeDTO.setTpaExtensions(createExpectedEpaExtensions());
        availGuarantees.add(availGuaranteeDTO);
        return availGuarantees;
    }

    private TPAExtensionsDTO createExpectedEpaExtensions() {
        TPAExtensionsDTO tpaExtensions = new TPAExtensionsDTO();
        tpaExtensions.setElement("description", GUARANTEE_DESCRIPTION);
        return tpaExtensions;
    }

    private CancelPolicyDTO createExpectedCancelPolicy() {
        CancelPolicyDTO cancelPolicyDTO = new CancelPolicyDTO();
        cancelPolicyDTO.setCancelPenaltyType(CancelPenaltyType.UNKNOWN);
        cancelPolicyDTO.setDeadline("16:00");
        cancelPolicyDTO.setDescription(CANCEL_POLICY_DESCRIPTION);
        return cancelPolicyDTO;
    }

    private RoomRateDTO createExpectedRoomRate() {
        RoomRateDTO roomRate = new RoomRateDTO();
        List<RateDTO> rates = new ArrayList<RateDTO>();
        rates.add(createExpectedFirstDayRate());
        rates.add(createExpectedSecondDayRate());
        rates.add(createExpectedThirdDayRate());
        rates.add(createExpectedForthDayRate());
        roomRate.setRates(rates);
        return roomRate;
    }

    private RatePlanDTO createExpectedRatePlan() {
        RatePlanDTO ratePlan = new RatePlanDTO();
        ratePlan.setCode(RATE_PLAN_CODE);
        ratePlan.setName(RATE_PLAN_NAME);
        ratePlan.setPaymentType(PAYMENT_TYPE_POA); // TODO paymentType only cash back?
        ratePlan.setTaxes(createExpectedTaxs());
        ratePlan.setServiceCharges(createExpectedServiceCharges());
        ratePlan.setNeedGuarantee(NEED_GUARANTEE);
        ratePlan.setCancelPolicy(createExpectedCancelPolicy());
        ratePlan.setAvailGuarantees(createExpectedAvailGuarantee()); // TODO translator
        ratePlan.setFreeMeal(createExpectedFreeMeal());
        return ratePlan;
    }

    private FreeMealDTO createExpectedFreeMeal() {
        FreeMealDTO freeMeal = new FreeMealDTO();
        freeMeal.setType(FreeMealType.NONE);
        return freeMeal;
    }

    private RoomTypeDTO createExpectedRoomType() {
        RoomTypeDTO roomType = new RoomTypeDTO();
        roomType.setCode(ROOM_TYPE_CODE);
        roomType.setName(ROOM_TYPE_NAME);
        return roomType;
    }

    private RateDTO createExpectedForthDayRate() {
        return createExpectedRateDTO(NUMBER_THREE);
    }

    private RateDTO createExpectedThirdDayRate() {
        return createExpectedRateDTO(NUMBER_TWO);
    }

    private RateDTO createExpectedSecondDayRate() {
        return createExpectedRateDTO(NUMBER_ONE);
    }

    private RateDTO createExpectedFirstDayRate() {
        return createExpectedRateDTO(NUMBER_ZERO);
    }

    private RateDTO createExpectedRateDTO(int index) {
        RateDTO rate = new RateDTO();
        rate.setDateRange(createExpectedDateRangeDTO());
        rate.setPureAmount(createExpectedAmountDTO(CURRENCY, AMOUNT_BEFORE_TAXS[index]));
        rate.setMealsIncluded(new MealsIncludedDTO(MealsIncludedType.UNKNOWN));
        return rate;
    }

    private List<ChargeItemDTO> createExpectedServiceCharges() {
        List<ChargeItemDTO> serviceCharges = new ArrayList<ChargeItemDTO>();
        ChargeItemDTO serviceCharge = new ChargeItemDTO();
        serviceCharge.setUnit(ChargeUnit.PER_NIGHT);
        serviceCharge.setType(ChargeType.FIXED);
        serviceCharge.setValue(SERVICE_CHARGE_AMOUNT);
        serviceCharge.setDescription(new String(SERVICE_CHARGE_DESC + seed.incrementAndGet()));
        serviceCharges.add(serviceCharge);
        return serviceCharges;
    }

    private List<ChargeItemDTO> createExpectedTaxs() {
        List<ChargeItemDTO> taxs = new ArrayList<ChargeItemDTO>();
        ChargeItemDTO tax = new ChargeItemDTO();
        tax.setUnit(ChargeUnit.PER_NIGHT);
        tax.setValue(AMOUNT_TAX);
        tax.setType(ChargeType.FIXED);
        tax.setDescription(TAX_DESC + seed.incrementAndGet());
        taxs.add(tax);
        return taxs;
    }

    private SimpleAmountDTO createExpectedAmountDTO(Currency currency, BigDecimal amount) {
        SimpleAmountDTO simpleAmountDTO = new SimpleAmountDTO();
        simpleAmountDTO.setCurrency(currency);
        simpleAmountDTO.setAmount(amount);
        return simpleAmountDTO;
    }

    private DateRangeDTO createExpectedDateRangeDTO() {
        DateRangeDTO dateRangeDTO = new DateRangeDTO();
        dateRangeDTO.setStart(new Date());
        dateRangeDTO.setEnd(new Date());
        return dateRangeDTO;
    }

    private HotelRefDTO createExpectedHotelRef() {
        HotelRefDTO hotelRef = new HotelRefDTO();
        hotelRef.setCode(new String(HOTEL_CODE));
        hotelRef.setName(new String(HOTEL_NAME));
        return hotelRef;
    }

}
