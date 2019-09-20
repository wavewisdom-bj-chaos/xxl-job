<p align="center">
    <img src="https://raw.githubusercontent.com/xuxueli/xxl-job/master/doc/images/xxl-logo.jpg" width="150">
    <h3 align="center">XXL-JOB</h3>
    <p align="center">
        XXL-JOB, a lightweight distributed task scheduling framework.
        <br>
        <a href="http://www.xuxueli.com/xxl-job/"><strong>-- Home Page --</strong></a>
        <br>
        <br>
        <a href="https://travis-ci.org/xuxueli/xxl-job">
            <img src="https://travis-ci.org/xuxueli/xxl-job.svg?branch=master" >
        </a>
        <a href="https://hub.docker.com/r/xuxueli/xxl-job-admin/">
            <img src="https://img.shields.io/badge/docker-passing-brightgreen.svg" >
        </a>
        <a href="https://maven-badges.herokuapp.com/maven-central/com.xuxueli/xxl-job/">
            <img src="https://maven-badges.herokuapp.com/maven-central/com.xuxueli/xxl-job/badge.svg" >
        </a>
         <a href="https://github.com/xuxueli/xxl-job/releases">
             <img src="https://img.shields.io/github/release/xuxueli/xxl-job.svg" >
         </a>
         <a href="http://www.gnu.org/licenses/gpl-3.0.html">
             <img src="https://img.shields.io/badge/license-GPLv3-blue.svg" >
         </a>
         <a href="https://gitter.im/xuxueli/xxl-job?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge">
              <img src="https://badges.gitter.im/xuxueli/xxl-job.svg" >
         </a>
         <a href="http://www.xuxueli.com/page/donate.html">
               <img src="https://img.shields.io/badge/%24-donate-ff69b4.svg?style=flat-square" >
         </a>
    </p>    
</p>

## Introduction
   定时任务是我们在业务开发过程中经常遇到的一类功能场景。指基于给定的时间点，给定的时间间隔或者给定的执行次数，执行任务。比如支付系统每日凌晨一点进行一天清算，某电商产品每天8点优惠，又比如支付超过30分钟等待就会过时等等场景，都需要我们使用定时任务来周期性的循环执行。定时任务一般具有如下特性：
- 时间驱动/事件驱动：内部系统一般可以通过事件驱动，但涉及到外部系统时，只能通过时间驱动。
- 批量处理/逐条处理：批量处理堆积的数据更加高效，在不需要实时性的情况下比消息中间件更有优势，例如花呗、信用卡固定周期内进行一次性结算。
- 系统内部/系统解耦：定时任务调度一般在系统内部，具有业务固定但是时间确定的特性，一定程度上定时任务和系统内部弱耦合的。

   单机调度和分布式调度
Java本身提供很多类型的定时工具，例如Timer、ScheduleExecutorService、SpringTask、Quartz等，这些定时工具在采用单机时其实可以满足大部分需求，但是在集群性和高可用性方面有所欠缺。由于单机定时任务的框架不是本文研究的重点，所以不再着重介绍。
在微服务理念盛行的当下，单机的定时任务已经远远无法满足需求，分布式任务调度应时诞生。分布式任务调度集中体现了三个概念：分布式、任务调度、配置中心。
- 分布式：平台是分布式部署的，各个节点之间可以无状态和无线水平扩展，满足HA。
- 任务调度：涉及任务状态管理、任务调度请求的发送与接收、具体任务的分配、任务的具体执行。
- 配置中心：感知整个集群的运行状态、统计、手动调整。
      通过对分布式任务调度框架的学习和调研，发现市场流行了很多产品，其中脱颖而出的有Elastic-job、Xxl-job、Saturn，通过一些对比（可参考附件分布式调度对比Excel表格），最终采用结构简单、开箱即用、接入公司多的Xxl-job。




## Duild
   出于业务需求的需要,我们采用了当下相对成熟和热门的 x-job 进行定时任务的管理。但是由于私有化的特性,我们对 x-job 进行了部分代码和配置的修改,以方便我们团队进行使用和维护团队技术风格。此文档旨在帮助新同学对 x-job 的团队个性化设置进行熟悉。以便使用。
-  端口:在 xxl-job-admin 项目下的资源文件夹下可以找配置文件 application.properties。里面的 server.port 即是端口号。gitHub 上源码默认的是 8080,我们这里将其修改成了 8123。

-  数 据 库 设 置 : 在 xxl-ob-admin项 目 下 的 资 源 文 件 夹 下 可 以 找 配 置 文 件 application.properties。

-  日志输出路径:在 xxl-job-admin 项目下的资源文件夹下可以找配置文件 logback.xml, 里面的 log.path 可以配置日志文件的存储路径。

在功能/测试环境进行部署时,我们没有使用 docker 的方式,而是直接启动 jar 或 tomcat部署 war 的形式,我们的 x-job 是以 jar 的形式启动的。 

Jar 启动命令:
 
- 以jar形式启动： 

     java -jar xxl-job-admin.jar

- 以守护进程方式运行jar： 
 
     java -jar xxl-job-admin.jar & 

Docker容器启动：

- Dockerfile

 
     FROM openjdk:7-jre-slim
 
     MAINTAINER wave

     label version=2.0.0

     ENV PARAMS=""

     ENV TZ=PRC

     RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

     ADD ./*.jar /app.jar

     ENTRYPOINT ["sh","-c","java -jar /app.jar  $PARAMS"]

- 编译镜像

     docker build -t wave-xxl-job-admin:2.1.0 . 

- 运行容器

    docker run -e PARAMS="--spring.datasource.url=jdbc:mysql://193.168.1.167:3306/xxl_job?Unicode=true&characterEncoding=UTF-8 --spring.datasource.username=root --spring.datasource.password=waveDevelop123 --spring.datasource.driver-class-name=com.mysql.jdbc.Driver --xxl.job.login.username=admin --xxl.job.login.password=123456" -p 8123:8123 -v /tmp:/data/applogs --name wave-xxl-job-admin -d wave-xxl-job-admin:2.1.0

这里面对 docker 里面的 x-job 的端口和数据库配置还有日志文件的挂载进行了启动 配置,可根据实际情况进行自定义修改。X-job 默认的管理员账号为 admin/123456,数据库连接地址是 193.168.1.167:3306/xxl_job,数据库用户名密码是 root/waveDevelop123。

Xxl-job 调度中心有一套自己的数据库和业务代码,将其看成我们自己的一个微服务模块即可。Xxl-job 的使用需要在调度平台以管理员权限(拥有一定权限)的角色进行定时任务的操作,但是我们项目中需要直接调用 http 接口,这样需要绕过原有的 cookies验证。

鉴权模式,而且原有的接口请求时表单请求,不符合我们现有的调用方式,为了保持统一,在原有表单请求不变的情况下,重新开发出 body 请求方式。我们项目中用到的接口见doc目录下的分布式调度接口文档。 

## Log

在xxl-job中我新增了任务系列的接口，针对这些接口，我们增加了odin项目中使用的日志方案。对这些新接口，我们增加了入参参数的输出，以方便开发查看和定位。

## Document    

-	[分布式定时任务调度系统技术选型](https://www.cnblogs.com/davidwang456/p/9057839.html)

-	[分布式调度框架大集合](https://blog.csdn.net/u012379844/article/details/82716146)

-	[分布式任务调度平台XXL-JOB](http://www.xuxueli.com/xxl-job/#/?id=_15-%e4%b8%8b%e8%bd%bd)

-	[Cron Maker（用于生成corn表达式）](http://www.cronmaker.com/)

-	[Spring 任务调度](https://blog.csdn.net/GroovyObject/article/details/5696229)

-	[BEAN模式下命令模式的理解](https://www.cnblogs.com/tohxyblog/p/6501396.html)

-	[理解HttpClient](https://www.cnblogs.com/cl1255674805/p/5708735.html)

-  [分布式任务调度调](https://drive.google.com/drive/folders/1_BUc9zgGnBx8vSfCMdqleoa-BEoiXkZd)
-  [ 分布式调度对比](https://docs.google.com/spreadsheets/d/1iTYBZ1qs8u7PxnBJfCzEDIGbDP1TLsO_/edit#gid=742520292)

## FAQ

Q:了解背景，例如该开源项目的开发人员、维护情况和社区活跃度

A:XXL-job是大众点评的大神独自开发出来的，开源后又有两位同事协助，如今版本一直在迭代优化，最近relase版本为2019-07的2.1.0。fork数2000+，接入公司有200+，相当活跃且成熟。

Q:衡量架构体系（对比架构复杂度选轻量级的分布式任务调度平台）

A:具体可参考附件中分布式调度对比，目前而言xxl-job是依赖性最低的一款轻量级框架。

Q:需要明确任务调度平台触发任务的执行都存在哪几种方式？以及每种方式是如何使用的？ A:①调度平台触发任务执行有两种，一种是corn表达式，一种是手动执行，依赖是需要在调度平台上进行配置或操作；②此外具官网透露，还存在事件触发，不过资料比较少，待进一步调研。③还有就是对源码进行简单修改后，还可以通过http post方式进行调用触发（可参考附件分布式调度接口文档）。

Q:服务搭建所需的组件有哪些？（将Dockerfile文件进行展示并将文字描述补充在上面的表格中）

A:xxl-job需要jdk、maven、mysql。Dockerfile可参考官网或者附件中xxl-job.xmind中部署栏目。

Q:如何触发任务的创建（定时任务和即时任务）？

A：调度平台配置或者调用二次开发后的http接口。

Q:触发发任务创建的同时是否可以传递参数，被传递的参数在触发任务执行的时可以回传过来？

A:可以传递参数。Xxl-job原生并不支持返回传递的参数，但是它保留了接口，需要二次开发后进行返回。
