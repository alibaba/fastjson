#Report SWE261P

##Team Name：Endloop

##Team Memebr：Bin Guo, Xin Tan, Chao Liu

###FastJson

**Github: https://github.com/alibaba/fastjson**

+ Fastjson, a library taht can be used to convert Java Objects into their JSON representation, is able to convert Json string to an equivalent Java object. 

+ Fastjson contain 14 packages ro provide the best performance on the server-side and android client and simeple toJSONString() and parseObject() methods to convert Java objects to JSONand vice versa. Also, it allows pre-existing unmodifiable objects to be converted to and form JSON and custom presentations for objects. 

  ![image-20220202153119167](/Users/sam/Library/Application Support/typora-user-images/image-20220202153119167.png)

+ LOC and Language:

  ![image-20220202153147398](/Users/sam/Library/Application Support/typora-user-images/image-20220202153147398.png)

  

###Instructions for build up

+ The file structure shown as below

  ![image-20220202153303027](/Users/sam/Library/Application Support/typora-user-images/image-20220202153303027.png)

  ![image-20220202153317424](/Users/sam/Library/Application Support/typora-user-images/image-20220202153317424.png)

![image-20220202153331118](/Users/sam/Library/Application Support/typora-user-images/image-20220202153331118.png)

+ How to build?

  1. Make a new directory
  2. git clone https://github.com/alibaba/fastjson
  3. Choose a IDE and then import the project to the IDE as a maven project. Then find pom.xml to reload the project

+ How to run?

  1. In pom.xml, add two dependencies (android or others)

     ```java
     <dependency>
         <groupId>com.alibaba</groupId>
         <artifactId>fastjson</artifactId>
         <version>1.2.76</version>
     </dependency>
       <dependency>
         <groupId>com.alibaba</groupId>
         <artifactId>fastjson</artifactId>
         <version>1.1.72.android</version>
     </dependency>
     ```
     
     





### Systematic functional testing & Partition testing:

+ Systematic functional testing:

  + Select inputs that are especially valuable.

  + Usually by choosing representatives of classes that are apt to fail often or not at all. It usually isolate regions with likely failures.

  + functional testing usually implies systematic testing.

Using systematic functional testing can make use of the attributes of classes we have. For example, if our purpose is to estimate the proportion of needles to hay, we can use their weight attribute for filtering. If we just test and sample randomly, it will be huge work.

+ Partition testing

  + Partition
    + It is one of the basic principles of general engineering principles.
    + Divide and conquer: divied complex activities into sets of simple activities that can be tackled independently. For example: we can partition testing process into: unit, integration, system, .. testing; we can partition analysis into modeling and analyzing the model.
    + Partition specification space for testing; Partition the program structure for testing.

  + Systematic partition testing
    + Sometimes failures are sparse in the space of possibile inputs but dense in some parts of the space. Use systematic partition testing can find the failures with more chances.

  + Boundaries
    + We use data type or classes to be the boundary. Since the Fastjson have the function to convert different types of object to JSON, we can simply partition our test by the types of the input.

+ Junit tests:
  + 