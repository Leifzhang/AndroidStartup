# AndroidStartUp

玩具工程

想优化下项目内的启动框架，还是基于有向无环图将任务排序。之后拆分多线程等待执行任务，保证所有初始化任务能够有序的执行，不产生由于别的sdk没有初始化导致的错乱问题。

因为原来的项目是多进程，所以考虑给多进程解耦，开发了一套ksp的注解生成分组逻辑。不同进程可以生成不同的任务分组逻辑。

增加锚点逻辑，让任务链可以更便捷的添加。

支持多线程真等待执行，解决了任务只是按照优先顺序放入，并不会等待前置任务完成，多线程情况下可能出现异常的情况

# 依赖tag化

剔除了传统的类形式的依赖方式，转化成tag，标识当前任务的依赖。

好处就是多模块解耦，劣势就是更加抽象，并不能形成立体的依赖关系。

# task分组

只是增加了个简单的组的概念而已，内部的依赖顺序还是要重新调整。

该功能主要就是服务大型工程，多进程初始化，并且初始化任务复杂，但是同时不同进程间需要初始化的任务也不同。

## 使用

通过ksp，以及添加注解的方式完成组任务生成。

1. build.gradle 添加

```gradle
plugins {
    id("com.google.devtools.ksp") version "1.5.30-1.0.0"
}
kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp")
    }
    sourceSets.test {
        kotlin.srcDir("build/generated/ksp/test/kotlin")
    }
}
dependencies {
    implementation project(':startup')
    implementation project(':startup-annotation')
    ksp project(":startup-ksp-compiler")
}

```

2. 创建一个任务并添加注解

新增新的启动注解，通过注解的排列组合来生成新的启动任务。

```kotlin
@Async
@Await
@DependOn(
    dependOn = [AsyncTask1Provider::class, SimpleTask2Provider::class],
    dependOnTag = ["taskB"]
)
@Startup(strategy = Process.MAIN)
@MustAfter
class SampleGenerate1Task : TaskRunner {

    override fun run(context: Context) {
        info("MyAnchorTask")
    }

}
```

老的启动任务声明

```kotlin
@StartupGroup(group = "ksp")
// 可选锚点
@MustAfter
class AsyncTask1 : SimpleStartupTask() {

    override fun mainThread(): Boolean {
        return false
    }

    override fun await(): Boolean {
        return true
    }

    override fun run(context: Context) {
        info("AsyncTask1")
    }
}
```

考虑到多进程，每个进程启动的任务不同的情况下需要对任务进行额外分组，下面demo为指定web进程的任务。获取注解方式大部分基于反射实现，属于合规的放心使用。



```kotlin
@StartupGroup(group = "ksp", strategy = Process.OTHER, processName = ["web"])
class SimpleTask3 : SimpleStartupTask() {

    override fun run(context: Context) {
        info("SimpleTask3")
    }
}

addMainProcTaskGroup { StartupTaskGroupApplicationKspAll() }
```

指定主进程任务

```kotlin
@StartupGroup(group = "ksp", strategy = Process.MAIN)
class SimpleTask2 : SimpleStartupTask() {

    override fun run(context: Context) {
        info("SimpleTask2")
    }

}
```

3. 添加组，该产物会存在在`build/generated/ksp`文件路径下，并根据group和moduleName等作为唯一类名。

优化项，把所有taskGroup放到了一起，单一module可能只有唯一。

```kotlin
public class StartupProcTaskGroupApplicationProc : StartupTaskProcessGroup {
    public override fun group(builder: Builder, process: String): MutableList<StartupTask> {
        val list = mutableListOf<StartupTask>()
        list.add(AsyncTask1())
        list.add(SimpleTask1())
        if(process.isMainProc()) {
            builder.mustAfterAnchorTask(SimpleTask2())
        }
        if(process.isMainProc()) {
            list.add(SampleGenerate1TaskTask())
        }
        if(process.contains("web")) {
            list.add(SimpleTask3())
        }
        if(process.contains("web")) {
            builder.mustAfterAnchorTask(SampleGenerate2TaskTask())
        }
        return list
    }
}


addProcTaskGroup { StartupProcTaskGroupApplicationProc() }
```


# dsl

通过dsl形式动态添加task

构造startup builder ，之后添加任务。 这部分主要就是因为需要构造较多的task函数，看起来不立体了。

以下是dsl版本参考，能力是将简单的任务可以通过dsl的形式添加进去。

```kotlin
fun Application.createStartup(): Startup.Builder = run {
    startUp(this) {
        addTask {
            simpleTask("taskA") {
                info("taskA")
            }
        }
        addTask {
            simpleTask("taskB") {
                info("taskB")
            }
        }
        addTask {
            simpleTask("taskC") {
                info("taskC")
            }
        }
        addTask {
            simpleTaskBuilder("taskD") {
                info("taskD")
            }.apply {
                dependOn("taskC")
            }.build()
        }
        addTask("taskC") {
            info("taskC")
        }
        setAnchorTask {
            MyAnchorTask()
        }
        addTask {
            asyncTask("asyncTaskA", {
                info("asyncTaskA")
            }, {
                dependOn("asyncTaskD")
            })
        }
        addAnchorTask {
            asyncTask("asyncTaskB", {
                info("asyncTaskB")
            }, {
                dependOn("asyncTaskA")
                await = true
            })
        }
        addAnchorTask {
            asyncTaskBuilder("asyncTaskC") {
                info("asyncTaskC")
                sleep(1000)
            }.apply {
                await = true
                dependOn("asyncTaskE")
            }.build()
        }
        addAnchorTask {
            asyncTask("asyncTaskD") {
                info("asyncTaskD")
                sleep(1000)
            }
        }
        addAnchorTask {
            asyncTask("asyncTaskE") {
                info("asyncTaskE")
                sleep(10000)
            }
        }
        addTaskGroup { taskGroup() }
        addTaskGroup { StartupTaskGroupApplicationKspMain() }
        addMainProcTaskGroup { StartupTaskGroupApplicationKspAll() }
        addProcTaskGroup { StartupProcTaskGroupApplicationKsp() }
    }
}
```

# 锚点任务组

等项目稳定之后，会设立几个锚点任务，后续任务只要挂载到锚点任务之后执行即可，可以简单的设立任务基准。

1.添加锚点任务

```kotlin
buildAnchorTask {
    MyAnchorTask()
}
```

2. 挂载mustAfter 

```kotlin
 mustAfterAnchor {
            asyncTask("asyncTaskE") {
                info("asyncTaskE")
                sleep(10000)
            }
        }
```

# 调试能力

通过提供额外的调试工程，当启动的所有任务完成后会自动跳转页面并渲染结果，无需任何注册逻辑。

## 使用

```gradle
dependencies {
    debugImplementation project(':startup-dag-view')
}
```

> 小贴士 不想要该功能则只需 在values下配置如下

```xml

<bool name="startup_install_provider_enable">false</bool>
```

# ksp

通过ksp 生成一部分动态的组，这部分暂时只能手动添加，后续考虑优先梳理依赖，但是并没有完全完成解耦。

# 调试组件

任务防止劣化的调试工具完成。 

![device-2022-01-02-120141.png](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/3e5d3b85c7334c8d80eee44b4c219cdf~tplv-k3u1fbpfcp-watermark.image?)

## 启动时间轴

江湖上一直流传着我的外号-ui大湿，在下也不是浪得虚名，ui大湿画出来的图形那叫一个美如画啊。

![device-2022-01-02-120203.png](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/746b475967c947ea83421b954456db94~tplv-k3u1fbpfcp-watermark.image?)


这部分原理比较简单，我们把当前启动任务的数据进行了收集，然后根据线程名进行分发，记录任务开始和结束的节点，然后通过图形化进行展示。

如果你第一时间看不懂，可以参考下自选股列表，每一列都是代表一个线程执行的时间轴。

## 启动顺序是否变更

我们会在每次启动的时候将当前启动的顺序进行数据库记录，然后通过数据库找出和当前hashcode不一样的任务，然后比对下用textview的形式展示出来，方便测试同学反馈问题。

这个地方的原理的，我是将整个启动任务通过字符串拼接，然后生成一个字符串，之后通过字符串的hashcode作为唯一标识符，不同字符串生成的hashcode也是不同的。

这里有个傻事就是我一开始对比的是`stringbuilder`的hashcode，然后发现一样的任务竟然值变更了，我真傻真的。

![device-2022-01-02-120221.png](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/ba1f718ec1794140bd53306f351ae782~tplv-k3u1fbpfcp-watermark.image?)

别问，问就是ui大湿，textview不香？


# 玩具属性

因为是玩具工程，仅供大家学习了，有问题可以直接联系我，联系方式如下

> qq: 454327998
