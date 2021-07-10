package com.alibaba.fastjson.deserializer.issues3796.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class LargeJavaBean {
	public static final String testName = "testName";
	
	@JSONField(name = "_id")
	private long id;
	
	private String a = "null";

	
	private String b = "null";
	
	private String c = "null";

	private int d = -1;
	
	private String e = "null";
	
	private String f = "null";
	
	private String g = "null";

	
	@JSONField(serialize = false)
	private ObjectA h = new ObjectA();

	private int i;

	private int j;

	
	private String k;
	
	private String l;

	
	private ObjectB m = new ObjectB();

	
	private int n;

	
	private String o;

	
	private int p;

	
	private long q;

	
	private long r;
	
	private long s;

	
	private long t;
	
	private long u;
	
	private long v;

	
	private int w = 0;
	
	private int x = 0;
	
	private boolean y = false;

	private List<ObjectC> z;

	
	private List<ObjectD> a1;

	
	private List<ObjectE> b1;

	
	private List<CommonObject> c1;

	
	private List<ObjectF> d1;

	
	@JSONField(serialize = false)
	private List<ObjectG> e1;

	
	private long f1;

	
	private long g1;

	
	private List<ObjectH> h1;

	

	private List<Integer> j1;

	
	private ObjectI k1;
	
	private List<Integer> l1;
	
	private List<ObjectJ> m1;

	
	private ObjectL n1;
	
	private List<ObjectM> o1;

	
	private ObjectN p1;

	
	private int q1;

	
	private long r1;

	
	private long[] s1;

	@JSONField(serialize = false)
	private ObjectO t1;

	@JSONField(serialize = false)
	private ObjectP u1;

	
	private List<ObjectQ> v1;

	
	private List<Integer> w1;
	
	private List<ObjectR> x1;
	
	private List<Integer> y1;
	
	private List<ObjectS> z1;

	
	private ObjectT a2 = new ObjectT();
	
	private List<ObjectU> b2;
	
	private ObjectV c2 = new ObjectV();
	
	private List<ObjectW> d2;

	
	private List<ObjectW> e2;

	
	private ObjectX f2;

	
	private ObjectY g2;

	
	private ObjectZ h2;

	
	private int i2 = 0;

	
	private ObjectA1 j2;

	
	private List<CommonObject2> k2;

	
	private ObjectB1 l2;

	
	private List<ObjectC1> m2;

	
	private int n2;

	
	private ObjectD1 o2;

	
	private int p2;

	
	long q2;

	
	long r2;

	
	int s2;

	
	int t2;

	
	int u2;

	
	int v2;

	
	int w2;

	
	private List<ObjectE1> x2;

	
	private List<ObjectE1> y2;

	ObjectF1 z2 = new ObjectF1();
	
	private List<Integer> a3;

	
	private List<ObjectG1> b3;
	
	private List<Integer> c3;
	
	private ObjectH1 d3;

	
	private List <CommonObject> e3;

	
	private ObjectI1 f3;

	
	private ObjectJ1 g3;

	
	private ObjectK1 h3;


	ObjectL1 i3 = new ObjectL1();

	
	private ObjectM1 j3;
	
	private ObjectN1 k3;

	
	private List<CommonObject> l3;

	
	private List<ObjectO1> m3;

	
	private ObjectP1 n3;

	
	private List<ObjectQ1> o3;

	
	private ObjectR1 p3;

	
	private ObjectS1 q3;

	
	private List<ObjectT1> r3;

	
	private List<ObjectU1> s3;

	
	private ObjectV1 t3;

	
	private List<Integer> u3;

	
	private int v3;

	
	private ObjectW1 w3;

	
	private List<ObjectX1>  x3;
	
	private List<Integer> y3;
	
	private List<Integer> z3;
	
	private int a4;
	
	private List<ObjectY1> b4;

	
	private ObjectZ1 c4;

	
	private List<ObjectA2> d4;

	
	private ObjectB2 e4;

	
	private List<ObjectC2> f4;
	
	private List<ObjectD2> g4 ;

	
	private List<ObjectE2> h4;

	
	private ObjectF2 i4;

	
	private ObjectG2 j4;
	
	private ObjectH2 k4;
	
	private ObjectI2 l4;
	
	private int m4;
	
	private ObjectJ2 n4;
	
	private List<ObjectK2> o4;
	private int p4;
	private int q4;
	private List<Integer> r4;

	
	private List<String> s4;

	
	private int t4;
	private boolean u4 = false;
	
	private List<ObjectL2> v4;

	
	private int w4;

	
	private ObjectM2 x4;

	
	private ObjectM2 y4;

	
	private List<ObjectN2> z4;

	
	private List<CommonObject> a5;

	
	private boolean[] b5;

	
	private ObjectO2 c5;

	public static String getTestName() {
		return testName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	public String getB() {
		return b;
	}

	public void setB(String b) {
		this.b = b;
	}

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public int getD() {
		return d;
	}

	public void setD(int d) {
		this.d = d;
	}

	public String getE() {
		return e;
	}

	public void setE(String e) {
		this.e = e;
	}

	public String getF() {
		return f;
	}

	public void setF(String f) {
		this.f = f;
	}

	public String getG() {
		return g;
	}

	public void setG(String g) {
		this.g = g;
	}

	public ObjectA getH() {
		return h;
	}

	public void setH(ObjectA h) {
		this.h = h;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}

	public String getK() {
		return k;
	}

	public void setK(String k) {
		this.k = k;
	}

	public String getL() {
		return l;
	}

	public void setL(String l) {
		this.l = l;
	}

	public ObjectB getM() {
		return m;
	}

	public void setM(ObjectB m) {
		this.m = m;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public String getO() {
		return o;
	}

	public void setO(String o) {
		this.o = o;
	}

	public int getP() {
		return p;
	}

	public void setP(int p) {
		this.p = p;
	}

	public long getQ() {
		return q;
	}

	public void setQ(long q) {
		this.q = q;
	}

	public long getR() {
		return r;
	}

	public void setR(long r) {
		this.r = r;
	}

	public long getS() {
		return s;
	}

	public void setS(long s) {
		this.s = s;
	}

	public long getT() {
		return t;
	}

	public void setT(long t) {
		this.t = t;
	}

	public long getU() {
		return u;
	}

	public void setU(long u) {
		this.u = u;
	}

	public long getV() {
		return v;
	}

	public void setV(long v) {
		this.v = v;
	}

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public boolean isY() {
		return y;
	}

	public void setY(boolean y) {
		this.y = y;
	}

	public List<ObjectC> getZ() {
		return z;
	}

	public void setZ(List<ObjectC> z) {
		this.z = z;
	}

	public List<ObjectD> getA1() {
		return a1;
	}

	public void setA1(List<ObjectD> a1) {
		this.a1 = a1;
	}

	public List<ObjectE> getB1() {
		return b1;
	}

	public void setB1(List<ObjectE> b1) {
		this.b1 = b1;
	}

	public List<CommonObject> getC1() {
		return c1;
	}

	public void setC1(List<CommonObject> c1) {
		this.c1 = c1;
	}

	public List<ObjectF> getD1() {
		return d1;
	}

	public void setD1(List<ObjectF> d1) {
		this.d1 = d1;
	}

	public List<ObjectG> getE1() {
		return e1;
	}

	public void setE1(List<ObjectG> e1) {
		this.e1 = e1;
	}

	public long getF1() {
		return f1;
	}

	public void setF1(long f1) {
		this.f1 = f1;
	}

	public long getG1() {
		return g1;
	}

	public void setG1(long g1) {
		this.g1 = g1;
	}

	public List<ObjectH> getH1() {
		return h1;
	}

	public void setH1(List<ObjectH> h1) {
		this.h1 = h1;
	}

	public List<Integer> getJ1() {
		return j1;
	}

	public void setJ1(List<Integer> j1) {
		this.j1 = j1;
	}

	public ObjectI getK1() {
		return k1;
	}

	public void setK1(ObjectI k1) {
		this.k1 = k1;
	}

	public List<Integer> getL1() {
		return l1;
	}

	public void setL1(List<Integer> l1) {
		this.l1 = l1;
	}

	public List<ObjectJ> getM1() {
		return m1;
	}

	public void setM1(List<ObjectJ> m1) {
		this.m1 = m1;
	}

	public ObjectL getN1() {
		return n1;
	}

	public void setN1(ObjectL n1) {
		this.n1 = n1;
	}

	public List<ObjectM> getO1() {
		return o1;
	}

	public void setO1(List<ObjectM> o1) {
		this.o1 = o1;
	}

	public ObjectN getP1() {
		return p1;
	}

	public void setP1(ObjectN p1) {
		this.p1 = p1;
	}

	public int getQ1() {
		return q1;
	}

	public void setQ1(int q1) {
		this.q1 = q1;
	}

	public long getR1() {
		return r1;
	}

	public void setR1(long r1) {
		this.r1 = r1;
	}

	public long[] getS1() {
		return s1;
	}

	public void setS1(long[] s1) {
		this.s1 = s1;
	}

	public ObjectO getT1() {
		return t1;
	}

	public void setT1(ObjectO t1) {
		this.t1 = t1;
	}

	public ObjectP getU1() {
		return u1;
	}

	public void setU1(ObjectP u1) {
		this.u1 = u1;
	}

	public List<ObjectQ> getV1() {
		return v1;
	}

	public void setV1(List<ObjectQ> v1) {
		this.v1 = v1;
	}

	public List<Integer> getW1() {
		return w1;
	}

	public void setW1(List<Integer> w1) {
		this.w1 = w1;
	}

	public List<ObjectR> getX1() {
		return x1;
	}

	public void setX1(List<ObjectR> x1) {
		this.x1 = x1;
	}

	public List<Integer> getY1() {
		return y1;
	}

	public void setY1(List<Integer> y1) {
		this.y1 = y1;
	}

	public List<ObjectS> getZ1() {
		return z1;
	}

	public void setZ1(List<ObjectS> z1) {
		this.z1 = z1;
	}

	public ObjectT getA2() {
		return a2;
	}

	public void setA2(ObjectT a2) {
		this.a2 = a2;
	}

	public List<ObjectU> getB2() {
		return b2;
	}

	public void setB2(List<ObjectU> b2) {
		this.b2 = b2;
	}

	public ObjectV getC2() {
		return c2;
	}

	public void setC2(ObjectV c2) {
		this.c2 = c2;
	}

	public List<ObjectW> getD2() {
		return d2;
	}

	public void setD2(List<ObjectW> d2) {
		this.d2 = d2;
	}

	public List<ObjectW> getE2() {
		return e2;
	}

	public void setE2(List<ObjectW> e2) {
		this.e2 = e2;
	}

	public ObjectX getF2() {
		return f2;
	}

	public void setF2(ObjectX f2) {
		this.f2 = f2;
	}

	public ObjectY getG2() {
		return g2;
	}

	public void setG2(ObjectY g2) {
		this.g2 = g2;
	}

	public ObjectZ getH2() {
		return h2;
	}

	public void setH2(ObjectZ h2) {
		this.h2 = h2;
	}

	public int getI2() {
		return i2;
	}

	public void setI2(int i2) {
		this.i2 = i2;
	}

	public ObjectA1 getJ2() {
		return j2;
	}

	public void setJ2(ObjectA1 j2) {
		this.j2 = j2;
	}

	public List<CommonObject2> getK2() {
		return k2;
	}

	public void setK2(List<CommonObject2> k2) {
		this.k2 = k2;
	}

	public ObjectB1 getL2() {
		return l2;
	}

	public void setL2(ObjectB1 l2) {
		this.l2 = l2;
	}

	public List<ObjectC1> getM2() {
		return m2;
	}

	public void setM2(List<ObjectC1> m2) {
		this.m2 = m2;
	}

	public int getN2() {
		return n2;
	}

	public void setN2(int n2) {
		this.n2 = n2;
	}

	public ObjectD1 getO2() {
		return o2;
	}

	public void setO2(ObjectD1 o2) {
		this.o2 = o2;
	}

	public int getP2() {
		return p2;
	}

	public void setP2(int p2) {
		this.p2 = p2;
	}

	public long getQ2() {
		return q2;
	}

	public void setQ2(long q2) {
		this.q2 = q2;
	}

	public long getR2() {
		return r2;
	}

	public void setR2(long r2) {
		this.r2 = r2;
	}

	public int getS2() {
		return s2;
	}

	public void setS2(int s2) {
		this.s2 = s2;
	}

	public int getT2() {
		return t2;
	}

	public void setT2(int t2) {
		this.t2 = t2;
	}

	public int getU2() {
		return u2;
	}

	public void setU2(int u2) {
		this.u2 = u2;
	}

	public int getV2() {
		return v2;
	}

	public void setV2(int v2) {
		this.v2 = v2;
	}

	public int getW2() {
		return w2;
	}

	public void setW2(int w2) {
		this.w2 = w2;
	}

	public List<ObjectE1> getX2() {
		return x2;
	}

	public void setX2(List<ObjectE1> x2) {
		this.x2 = x2;
	}

	public List<ObjectE1> getY2() {
		return y2;
	}

	public void setY2(List<ObjectE1> y2) {
		this.y2 = y2;
	}

	public ObjectF1 getZ2() {
		return z2;
	}

	public void setZ2(ObjectF1 z2) {
		this.z2 = z2;
	}

	public List<Integer> getA3() {
		return a3;
	}

	public void setA3(List<Integer> a3) {
		this.a3 = a3;
	}

	public List<ObjectG1> getB3() {
		return b3;
	}

	public void setB3(List<ObjectG1> b3) {
		this.b3 = b3;
	}

	public List<Integer> getC3() {
		return c3;
	}

	public void setC3(List<Integer> c3) {
		this.c3 = c3;
	}

	public ObjectH1 getD3() {
		return d3;
	}

	public void setD3(ObjectH1 d3) {
		this.d3 = d3;
	}

	public List<CommonObject> getE3() {
		return e3;
	}

	public void setE3(List<CommonObject> e3) {
		this.e3 = e3;
	}

	public ObjectI1 getF3() {
		return f3;
	}

	public void setF3(ObjectI1 f3) {
		this.f3 = f3;
	}

	public ObjectJ1 getG3() {
		return g3;
	}

	public void setG3(ObjectJ1 g3) {
		this.g3 = g3;
	}

	public ObjectK1 getH3() {
		return h3;
	}

	public void setH3(ObjectK1 h3) {
		this.h3 = h3;
	}

	public ObjectL1 getI3() {
		return i3;
	}

	public void setI3(ObjectL1 i3) {
		this.i3 = i3;
	}

	public ObjectM1 getJ3() {
		return j3;
	}

	public void setJ3(ObjectM1 j3) {
		this.j3 = j3;
	}

	public ObjectN1 getK3() {
		return k3;
	}

	public void setK3(ObjectN1 k3) {
		this.k3 = k3;
	}

	public List<CommonObject> getL3() {
		return l3;
	}

	public void setL3(List<CommonObject> l3) {
		this.l3 = l3;
	}

	public List<ObjectO1> getM3() {
		return m3;
	}

	public void setM3(List<ObjectO1> m3) {
		this.m3 = m3;
	}

	public ObjectP1 getN3() {
		return n3;
	}

	public void setN3(ObjectP1 n3) {
		this.n3 = n3;
	}

	public List<ObjectQ1> getO3() {
		return o3;
	}

	public void setO3(List<ObjectQ1> o3) {
		this.o3 = o3;
	}

	public ObjectR1 getP3() {
		return p3;
	}

	public void setP3(ObjectR1 p3) {
		this.p3 = p3;
	}

	public ObjectS1 getQ3() {
		return q3;
	}

	public void setQ3(ObjectS1 q3) {
		this.q3 = q3;
	}

	public List<ObjectT1> getR3() {
		return r3;
	}

	public void setR3(List<ObjectT1> r3) {
		this.r3 = r3;
	}

	public List<ObjectU1> getS3() {
		return s3;
	}

	public void setS3(List<ObjectU1> s3) {
		this.s3 = s3;
	}

	public ObjectV1 getT3() {
		return t3;
	}

	public void setT3(ObjectV1 t3) {
		this.t3 = t3;
	}

	public List<Integer> getU3() {
		return u3;
	}

	public void setU3(List<Integer> u3) {
		this.u3 = u3;
	}

	public int getV3() {
		return v3;
	}

	public void setV3(int v3) {
		this.v3 = v3;
	}

	public ObjectW1 getW3() {
		return w3;
	}

	public void setW3(ObjectW1 w3) {
		this.w3 = w3;
	}

	public List<ObjectX1> getX3() {
		return x3;
	}

	public void setX3(List<ObjectX1> x3) {
		this.x3 = x3;
	}

	public List<Integer> getY3() {
		return y3;
	}

	public void setY3(List<Integer> y3) {
		this.y3 = y3;
	}

	public List<Integer> getZ3() {
		return z3;
	}

	public void setZ3(List<Integer> z3) {
		this.z3 = z3;
	}

	public int getA4() {
		return a4;
	}

	public void setA4(int a4) {
		this.a4 = a4;
	}

	public List<ObjectY1> getB4() {
		return b4;
	}

	public void setB4(List<ObjectY1> b4) {
		this.b4 = b4;
	}

	public ObjectZ1 getC4() {
		return c4;
	}

	public void setC4(ObjectZ1 c4) {
		this.c4 = c4;
	}

	public List<ObjectA2> getD4() {
		return d4;
	}

	public void setD4(List<ObjectA2> d4) {
		this.d4 = d4;
	}

	public ObjectB2 getE4() {
		return e4;
	}

	public void setE4(ObjectB2 e4) {
		this.e4 = e4;
	}

	public List<ObjectC2> getF4() {
		return f4;
	}

	public void setF4(List<ObjectC2> f4) {
		this.f4 = f4;
	}

	public List<ObjectD2> getG4() {
		return g4;
	}

	public void setG4(List<ObjectD2> g4) {
		this.g4 = g4;
	}

	public List<ObjectE2> getH4() {
		return h4;
	}

	public void setH4(List<ObjectE2> h4) {
		this.h4 = h4;
	}

	public ObjectF2 getI4() {
		return i4;
	}

	public void setI4(ObjectF2 i4) {
		this.i4 = i4;
	}

	public ObjectG2 getJ4() {
		return j4;
	}

	public void setJ4(ObjectG2 j4) {
		this.j4 = j4;
	}

	public ObjectH2 getK4() {
		return k4;
	}

	public void setK4(ObjectH2 k4) {
		this.k4 = k4;
	}

	public ObjectI2 getL4() {
		return l4;
	}

	public void setL4(ObjectI2 l4) {
		this.l4 = l4;
	}

	public int getM4() {
		return m4;
	}

	public void setM4(int m4) {
		this.m4 = m4;
	}

	public ObjectJ2 getN4() {
		return n4;
	}

	public void setN4(ObjectJ2 n4) {
		this.n4 = n4;
	}

	public List<ObjectK2> getO4() {
		return o4;
	}

	public void setO4(List<ObjectK2> o4) {
		this.o4 = o4;
	}

	public int getP4() {
		return p4;
	}

	public void setP4(int p4) {
		this.p4 = p4;
	}

	public int getQ4() {
		return q4;
	}

	public void setQ4(int q4) {
		this.q4 = q4;
	}

	public List<Integer> getR4() {
		return r4;
	}

	public void setR4(List<Integer> r4) {
		this.r4 = r4;
	}

	public List<String> getS4() {
		return s4;
	}

	public void setS4(List<String> s4) {
		this.s4 = s4;
	}

	public int getT4() {
		return t4;
	}

	public void setT4(int t4) {
		this.t4 = t4;
	}

	public boolean isU4() {
		return u4;
	}

	public void setU4(boolean u4) {
		this.u4 = u4;
	}

	public List<ObjectL2> getV4() {
		return v4;
	}

	public void setV4(List<ObjectL2> v4) {
		this.v4 = v4;
	}

	public int getW4() {
		return w4;
	}

	public void setW4(int w4) {
		this.w4 = w4;
	}

	public ObjectM2 getX4() {
		return x4;
	}

	public void setX4(ObjectM2 x4) {
		this.x4 = x4;
	}

	public ObjectM2 getY4() {
		return y4;
	}

	public void setY4(ObjectM2 y4) {
		this.y4 = y4;
	}

	public List<ObjectN2> getZ4() {
		return z4;
	}

	public void setZ4(List<ObjectN2> z4) {
		this.z4 = z4;
	}

	public List<CommonObject> getA5() {
		return a5;
	}

	public void setA5(List<CommonObject> a5) {
		this.a5 = a5;
	}

	public boolean[] getB5() {
		return b5;
	}

	public void setB5(boolean[] b5) {
		this.b5 = b5;
	}

	public ObjectO2 getC5() {
		return c5;
	}

	public void setC5(ObjectO2 c5) {
		this.c5 = c5;
	}
}
