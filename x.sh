export AMPLXE_EXPERIMENTAL=1
/opt/intel/vtune_amplifier_xe/bin64/amplxe-cl -collect hotspots /opt/taobao/install/jdk-1.7.0_10/bin/java -classpath target/classes/:target/test-classes/ com.alibaba.json.test.benchmark.BenchmarkMain
